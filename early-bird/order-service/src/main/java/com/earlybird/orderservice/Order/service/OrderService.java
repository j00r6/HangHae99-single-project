package com.earlybird.orderservice.Order.service;

import com.earlybird.orderservice.Global.exception.BadRequestException;
import com.earlybird.orderservice.Order.dto.OrderRequest;
import com.earlybird.orderservice.Order.dto.OrderResponse;
import com.earlybird.orderservice.Order.entity.Order;
import com.earlybird.orderservice.Order.entity.OrderStatus;
import com.earlybird.orderservice.Order.repository.OrderRepository;
import com.earlybird.orderservice.OrderItem.dto.OrderItemRequest;
import com.earlybird.orderservice.OrderItem.entity.OrderItem;
import com.earlybird.orderservice.OrderItem.service.OrderItemService;
import com.earlybird.productservice.Product.dto.ProductResponseDto;
import com.earlybird.productservice.Product.service.ProductService;
import com.earlybird.userservice.User.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final UserService userService;
    private final OrderItemService orderItemService;
    private final RestClient restClient;

    public void createOrder(Long userId, OrderRequest request) {
        log.info("주문 정보 : " + request.toString());

//        User findUser = userService.findVerifyUser(userId);
//        if (findUser == null) {
//            throw new UserNotFoundException("제품 주문을 위해 로그인을 진행해 주세요");
//        }
        // TODO : 현재는 orderId 를 주문번호로 사용하고 있지만, 주문번호를 따로 분리할 필요가 있음
        // 주문 초기값 생성
        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.PROCESSING);
        BigDecimal totalPrice = BigDecimal.ZERO;
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);

        // 주문 요청 OrderItemRequest 에서 주문정보를 받아오는 과정
        for (OrderItemRequest itemRequest : request.getCart()) {
//            Product product = productService.findVerifyProduct(itemRequest.getProductId());

            int requestQuantity = itemRequest.getQuantity();
            BigDecimal requestPrice = itemRequest.getPrice();

            // 주문에 대한 재고만 관리
//            productService.decreaseStock(product, requestQuantity);

            // 개별 혹은 다중 주문에 대한 생성자 생성
            orderItemService.createOrderItem(itemRequest.getProductId(), requestQuantity, requestPrice, order);

            totalPrice = calculateTotalPrice(request);
        }

        // totalPrice 갱신
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);
    }

    // 전체 가격 계산하는 로직을 createOrder 에서 분리
    private BigDecimal calculateTotalPrice(OrderRequest request) {
        return request.getCart().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // 주문 리스트 가져오기
    public List<OrderResponse> getOrdersAfterCursor(Long userId, Long cursor, int pageSize) {
        Order findOrder = findOrderByUserId(userId);

        Pageable pageable = PageRequest.of(0, pageSize, Sort.by(Sort.Order.desc("createdAt")));
        List<Order> orders = orderRepository.findOrdersByUserIdAndCursor(userId, cursor, pageable);

        orders.forEach(this::calculateOrderStatus);

        return orders.stream()
                .map(OrderResponse::OrderEntityToOrderResponse)
                .collect(Collectors.toList());
    }

    // 주문 리스트에 현재 주문 상태 추가하기
    // 주문 완료일로부터 1일 후에 배송중, 2일 후에 도착
    public void calculateOrderStatus(Order order) {
        // today = 서버 시간을 오늘로 설정하고 주문 내역에서 날짜와 비교
        LocalDateTime today = LocalDateTime.now();
        // 하루가 지나면 배송중
        LocalDateTime shippingDate = order.getCreatedAt().plusDays(1);
        // 이틀이 지나면 도착
        LocalDateTime completedDate = order.getCreatedAt().plusDays(2);

        if (today.isBefore(shippingDate)) {
            order.setStatus(OrderStatus.PENDING);
        } else if (today.isBefore(completedDate)) {
            order.setStatus(OrderStatus.SHIPPING);
        } else {
            order.setStatus(OrderStatus.COMPLETED);
        }
    }

    public void cancelOrder(Long userId, Long orderId) {
        // 주문번호와 유저 정보로 특정 유저의 주문내역을 가져온다.
        Order findOrder = orderRepository.findOrderByUserIdAndOrderId(userId, orderId);

        // 주문 내역에서 재고만 뽑아 따로 저장 (취소 시 재고관리를 위해)
        int cancelledStock = extractQuantity(findOrder);

        // 주문 정보에 포함되있는 제품 정보로 상세조회
        Long orderItemServiceProductId = orderItemService.getProductId(orderId);
//        Product findProduct = productService.findVerifyProduct(productId);

        // 주문이 배송 단계로 넘어가기 전일 경우
        if(findOrder.getStatus() == OrderStatus.PENDING || findOrder.getStatus() == OrderStatus.PROCESSING) {
            // 주문 상태를 주문 취소로 변경
            findOrder.setStatus(OrderStatus.CANCELED);
            // 주문 내역에서 가져온 수량만큼 재고 증가
//            productService.increaseStock(productId, cancelledStock);
        } else {
            // 주문이 배송 단계로 넘어간 경우
            throw new BadRequestException("배송이 시작되어 주문 취소 진행이 어렵습니다. 판매처에 문의해주세요");
        }
    }

    public void refundOrder(Long userId, Long orderId) {
        Order findOrder = orderRepository.findOrderByUserIdAndOrderId(userId, orderId);

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime completedDate = findOrder.getCreatedAt().plusDays(2);

        if (today.isAfter(completedDate.plusDays(1))) {
            throw new BadRequestException("반품 기간이 지나 반품이 불가합니다.");
        }

        Long orderItemServiceProductId = orderItemService.getProductId(orderId);
        // Product-service 에서 통신을 통해 Product 객체 반환
        fetchProductByOrderItemProductId(orderItemServiceProductId);

        int cancelledStock = extractQuantity(findOrder);
//        productService.increaseStock(productId, cancelledStock);

        findOrder.setStatus(OrderStatus.REFUNDED);
        orderRepository.save(findOrder);
    }

    public Order findOrderByUserId(Long userId) {
        return orderRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("주문 정보가 없습니다"));
    }

    public int extractQuantity (Order order) {
        return order.getOrderItems().stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();
    }

    private ProductResponseDto fetchProductByOrderItemProductId(Long productId) {
        ResponseEntity<ProductResponseDto> response = restClient.get()
                .uri("http://product-service/products/{productId}", productId)
                .retrieve()
                .toEntity(ProductResponseDto.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            // TODO : 예외처리
            return null;
        }
    }
}


package com.single_project.early_bird.Order.service;

import com.single_project.early_bird.Global.exception.BadRequestException;
import com.single_project.early_bird.Global.exception.UserNotFoundException;
import com.single_project.early_bird.Order.dto.OrderRequest;
import com.single_project.early_bird.Order.entity.Order;
import com.single_project.early_bird.Order.entity.OrderStatus;
import com.single_project.early_bird.Order.repository.OrderRepository;
import com.single_project.early_bird.OrderItem.dto.OrderItemRequest;
import com.single_project.early_bird.OrderItem.entity.OrderItem;
import com.single_project.early_bird.OrderItem.repository.OrderItemRepository;
import com.single_project.early_bird.OrderItem.service.OrderItemService;
import com.single_project.early_bird.Product.entity.Product;
import com.single_project.early_bird.Product.service.ProductService;
import com.single_project.early_bird.User.entity.User;
import com.single_project.early_bird.User.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final UserService userService;
    private final OrderItemService orderItemService;

    public void createOrder(Long userId, OrderRequest request) {
        log.info("주문 정보 : " + request.toString());

        User findUser = userService.findVerifyUser(userId);
        if (findUser == null) {
            throw new UserNotFoundException("제품 주문을 위해 로그인을 진행해 주세요");
        }
        // TODO : 현재는 orderId 를 주문번호로 사용하고 있지만, 주문번호를 따로 분리할 필요가 있음
        // 주문 초기값 생성
        Order order = new Order();
        order.setUser(findUser);
        order.setStatus(OrderStatus.PROCESSING);
        BigDecimal totalPrice = BigDecimal.ZERO;
        orderRepository.save(order);

        // 주문 요청 OrderItemRequest 에서 주문정보를 받아오는 과정
        for (OrderItemRequest itemRequest : request.getCart()) {
            Product product = productService.findVerifyProduct(itemRequest.getProductId());

            int requestQuantity = itemRequest.getQuantity();
            BigDecimal requestPrice = itemRequest.getPrice();

            // 주문에 대한 재고만 관리
            productService.decreaseStock(product, requestQuantity);

            // 개별 혹은 다중 주문에 대한 생성자 생성
            orderItemService.createOrderItem(product, requestQuantity, requestPrice, order);

            totalPrice = calculateTotalPrice(request);
            order.setTotalPrice(totalPrice);
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
}


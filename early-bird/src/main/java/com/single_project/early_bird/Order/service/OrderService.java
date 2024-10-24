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
import com.single_project.early_bird.Product.entity.Product;
import com.single_project.early_bird.Product.service.ProductService;
import com.single_project.early_bird.User.entity.User;
import com.single_project.early_bird.User.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final UserService userService;
    private final OrderItemRepository orderItemRepository;

    public void orderProcess(Long userId, OrderRequest request) {
        log.info("주문 정보 : " + request.toString());

        User findUser = userService.findVerifyUser(userId);
        if (findUser == null) {
            throw new UserNotFoundException("제품 주문을 위해 로그인을 진행해 주세요");
        }

        //주문 초기값 생성
        Order order = new Order();
        order.setUser(findUser);
        order.setStatus(OrderStatus.PENDING);
        BigDecimal totalPrice = BigDecimal.ZERO;
        orderRepository.save(order);

        // 주문 정보 OrderItemRequest 에서 주문정보를 받아오는 과정
        for (OrderItemRequest itemRequest : request.getCart()) {
            Product product = productService.findVerifyProduct(itemRequest.getProductId());

            int requestQuantity = itemRequest.getQuantity();
            BigDecimal requestPrice = itemRequest.getPrice();

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(requestQuantity);
            orderItem.setPrice(requestPrice);
            orderItem.setOrder(order);


            int stockQuantity = product.getStockQuantity();

            if(stockQuantity < requestQuantity) {
                throw new BadRequestException("제품 수량이 부족합니다");
            }
            totalPrice = totalPrice.add(requestPrice.multiply(BigDecimal.valueOf(requestQuantity)));

            orderItemRepository.save(orderItem);
        }

        // totalPrice 갱신
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);
    }
}


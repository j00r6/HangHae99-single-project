package com.earlybird.orderservice.Order.entity;

/**
 * PENDING: 주문 접수
 * PROCESSING: 배송 준비중
 * SHIPPING: 배송 중 (D+1)
 * COMPLETED: 배송 완료 (D+2)
 * CANCELED: 주문 취소 (PENDING, PROCESSING 상태에서만 가능)
 * RETURN_REQUESTED: 반품 요청 (COMPLETED 부터 1일이내만 가능)
 * RETURNED: 반품 완료
 */
public enum OrderStatus {
    PENDING,
    PROCESSING,
    SHIPPING,
    COMPLETED,
    CANCELED,
    REFUND_REQUEST,
    REFUNDED;

    public boolean canTransitionTo(OrderStatus targetStatus) {
        return switch (this) {
            case PENDING -> targetStatus == CANCELED;
            case SHIPPING -> targetStatus == COMPLETED || targetStatus == CANCELED;
            case COMPLETED -> targetStatus == REFUND_REQUEST;
            case REFUND_REQUEST -> targetStatus == REFUNDED;
            default -> false;
        };
    }
}

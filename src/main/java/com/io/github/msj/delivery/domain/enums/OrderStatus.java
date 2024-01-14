package com.io.github.msj.delivery.domain.enums;

public enum OrderStatus {
    WAITING_PAYMENT("Waiting for Payment"),
    PAYMENT_APPROVED("Payment Approved"),
    PROCESSING("Processing"),
    READY_FOR_DELIVERY("Ready for Delivery"),
    DELIVERED("Delivered"),
    CANCELED("Canceled");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}


package com.example.connect.api.domain.delivery;

public enum DeliveryStatus {
    READY("준비"),
    COMP("배송");

    private final String description;

    DeliveryStatus(String description) {
        this.description = description;
    }
}

package com.example.connect.api.domain;

import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;

@MappedSuperclass
public class BaseEntity {
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}

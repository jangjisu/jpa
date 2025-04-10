package com.example.connect.api.listener;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PrePersist;

public class DuckListener {
    @PrePersist
    private void prePersist(Object obj) {
        System.out.println("DuckListener.prePersist obj = [" + obj + "]");
    }

    @PostPersist
    private void postPersist(Object obj) {
        System.out.println("DuckListener.postPersist obj = [" + obj + "]");
    }
}

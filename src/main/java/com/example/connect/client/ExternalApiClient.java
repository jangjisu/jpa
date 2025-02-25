package com.example.connect.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "ExternalApiReceiver", url = "localhost:8080/external-api")
public interface ExternalApiClient {
    @GetMapping("/random")
    String getExternalData();
}

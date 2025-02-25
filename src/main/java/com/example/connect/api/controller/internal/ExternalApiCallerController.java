package com.example.connect.api.controller.internal;

import com.example.connect.service.ExternalApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExternalApiCallerController {
    private final ExternalApiService externalApiService;

    @GetMapping("/request")
    public String requestExternalApi() {
        return externalApiService.getExternalRandom();
    }
}

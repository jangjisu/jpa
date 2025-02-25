package com.example.connect.service;

import com.example.connect.client.ExternalApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExternalApiService {
    private final ExternalApiClient externalApiClient;

    public String getExternalRandom() {
        return externalApiClient.getExternalData();
    }
}

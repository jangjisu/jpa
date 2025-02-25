package com.example.connect.api.controller.external;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/external-api")
public class ExternalGenerateController {

    private final Random random = new Random();

    @GetMapping("/random")
    public ResponseEntity<String> getExternalRandom() {
        if (random.nextBoolean()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail");
        }

        return ResponseEntity.ok("success");
    }

}

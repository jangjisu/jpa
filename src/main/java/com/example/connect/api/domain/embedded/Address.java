package com.example.connect.api.domain.embedded;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {
    private String city;
    private String street;
    private String zipCode;

    public static Address create(String city, String street, String zipCode) {
        return new Address(city, street, zipCode);
    }
}

package com.foxminded.university.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private int id;
    private String city;
    private String street;
    private String postcode;
    private String district;
}

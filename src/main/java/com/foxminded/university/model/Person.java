package com.foxminded.university.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    private int id;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String email;
    private LocalDate birthDate;
    private String phone;
    private Address address;
}

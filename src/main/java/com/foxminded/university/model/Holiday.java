package com.foxminded.university.model;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Holiday {

    private int id;
    private String name;
    private LocalDate date;
}

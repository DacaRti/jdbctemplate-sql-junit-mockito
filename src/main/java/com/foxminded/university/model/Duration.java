package com.foxminded.university.model;

import lombok.*;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Duration {

    private int id;
    private LocalTime startTime;
    private LocalTime endTime;
}

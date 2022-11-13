package com.foxminded.university.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Classroom {

    private int id;
    private int number;
    private int floor;
    private int capacity;
}

package com.foxminded.university.model;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Group {

    private int id;
    private String name;
    private int course;
    private Faculty faculty;
    private List<Student> students;
}

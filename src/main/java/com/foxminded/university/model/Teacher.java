package com.foxminded.university.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class Teacher extends Person {

    private Degree degree;
    private String rank;
    private Faculty faculty;
    private List<Vacation> vacations;
    private List<Subject> skills;
}

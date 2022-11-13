package com.foxminded.university.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {

    private int id;
    private Subject subject;
    private Classroom classroom;
    private LocalDate date;
    private Duration duration;
    private Teacher teacher;
    private List<Group> groups;
}

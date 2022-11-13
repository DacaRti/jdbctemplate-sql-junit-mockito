package com.foxminded.university.model;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Syllabus {

    private int id;
    private List<Subject> subjects;
    private int fullTime;
}

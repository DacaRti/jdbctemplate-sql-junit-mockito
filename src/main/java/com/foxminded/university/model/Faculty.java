package com.foxminded.university.model;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Faculty {

    private int id;
    private String name;
    private Syllabus syllabus;
    private List<Group> groups;
    private List<Teacher> teachers;
}

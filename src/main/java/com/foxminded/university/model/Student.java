package com.foxminded.university.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class Student extends Person {

    private Group group;
    private StudyForm studyForm;
}

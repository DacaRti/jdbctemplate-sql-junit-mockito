package com.foxminded.university.model;

import lombok.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vacation {

    private int id;
    private Teacher teacher;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isPaid;
    private String cause;

    public int getCountOfVacationDays() {
        return (int) ChronoUnit.DAYS.between(startDate, endDate);
    }
}

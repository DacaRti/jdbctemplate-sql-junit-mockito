package com.foxminded.university;

import com.foxminded.university.config.AppConfig;
import com.foxminded.university.model.*;
import com.foxminded.university.service.VacationService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        VacationService vacationService = context.getBean(VacationService.class);

        Teacher standardTeacher = Teacher.builder().id(1)
            .firstName("Tsunade")
            .lastName("Senju")
            .gender(Gender.FEMALE)
            .email("Hokage@Gmail.com")
            .birthDate(LocalDate.parse("1900-03-03"))
            .phone("05034343434")
            .address(new Address(1, "a", "a", "a", "a"))
            .degree(Degree.DOCTOR).rank("Hokage")
            .faculty(Faculty.builder()
                .id(1).name("a")
                .syllabus(Syllabus.builder().id(1).fullTime(25).build()).build()).build();

        Vacation standardVacation = Vacation.builder()
            .teacher(standardTeacher)
            .startDate(LocalDate.parse("1985-03-03"))
            .endDate(LocalDate.parse("1985-03-13"))
            .isPaid(true)
            .cause("cause")
            .build();

        Vacation standardVacation2 = Vacation.builder()
            .id(1)
            .teacher(standardTeacher)
            .startDate(LocalDate.parse("1985-07-03"))
            .endDate(LocalDate.parse("1985-07-23"))
            .isPaid(true)
            .cause("cause")
            .build();

        vacationService.create(standardVacation);
        vacationService.update(standardVacation2);
    }
}

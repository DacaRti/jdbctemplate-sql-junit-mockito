package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.VacationDao;
import com.foxminded.university.dao.jdbc.mappers.VacationMapper;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Vacation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Repository
public class JdbcVacationDao implements VacationDao {

    private static final String CREATE_VACATION_QUERY = "INSERT INTO vacations(teacher_id, start_date, end_date, is_paid, cause) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_VACATIONS_QUERY = "SELECT * FROM vacations v " +
        "JOIN teachers t ON t.id = v.teacher_id " +
        "JOIN addresses a on a.id = t.address_id " +
        "JOIN faculties f on f.id = t.faculty_id " +
        "JOIN syllabuses s on s.id = f.syllabus_id";
    private static final String GET_VACATION_BY_ID_QUERY = "SELECT * FROM vacations v " +
        "JOIN teachers t ON t.id = v.teacher_id " +
        "JOIN addresses a on a.id = t.address_id " +
        "JOIN faculties f on f.id = t.faculty_id " +
        "JOIN syllabuses s on s.id = f.syllabus_id " +
        "WHERE v.id = ?";
    private static final String UPDATE_VACATION_QUERY = "UPDATE vacations SET teacher_id=?, start_date=?, end_date=?, is_paid=?, cause=? WHERE id=?";
    private static final String DELETE_VACATION_QUERY = "DELETE from vacations WHERE id=?";
    private static final String GET_VACATIONS_BY_TEACHER_BETWEEN_DATE_RANGE_QUERY =
        "SELECT * FROM vacations v " +
            "JOIN teachers t ON t.id = v.teacher_id " +
            "JOIN addresses a on a.id = t.address_id " +
            "JOIN faculties f on f.id = t.faculty_id " +
            "JOIN syllabuses s on s.id = f.syllabus_id " +
            "WHERE teacher_id = ? " +
            "AND (v.start_date BETWEEN ? AND ?) " +
            "AND (v.end_date BETWEEN ? AND ?)";
    private static final String GET_VACATIONS_BY_TEACHER_AND_DATE_QUERY =
        "SELECT * FROM vacations v " +
            "JOIN teachers t ON t.id = v.teacher_id " +
            "JOIN addresses a on a.id = t.address_id " +
            "JOIN faculties f on f.id = t.faculty_id " +
            "JOIN syllabuses s on s.id = f.syllabus_id " +
            "WHERE teacher_id = ? " +
            "AND (? BETWEEN v.start_date AND v.end_date)";

    private final JdbcTemplate jdbcTemplate;

    private final VacationMapper vacationMapper;

    public JdbcVacationDao(JdbcTemplate jdbcTemplate, VacationMapper vacationMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.vacationMapper = vacationMapper;
    }

    @Override
    public void create(Vacation vacation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(CREATE_VACATION_QUERY, new String[]{"id"});
            statement.setInt(1, vacation.getTeacher().getId());
            statement.setObject(2, vacation.getStartDate());
            statement.setObject(3, vacation.getEndDate());
            statement.setBoolean(4, vacation.isPaid());
            statement.setString(5, vacation.getCause());

            return statement;
        }, keyHolder);

        vacation.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    public List<Vacation> getAll() {
        return jdbcTemplate.query(GET_VACATIONS_QUERY, vacationMapper);
    }

    @Override
    public List<Vacation> getByTeacherBetweenDateRange(Teacher teacher, LocalDate startDate, LocalDate endDate) {
        return jdbcTemplate.query(GET_VACATIONS_BY_TEACHER_BETWEEN_DATE_RANGE_QUERY,
            new Object[]{teacher.getId(), startDate, endDate, startDate, endDate}, vacationMapper);
    }

    @Override
    public Vacation getByTeacherAndDate(Teacher teacher, LocalDate date) {
        return jdbcTemplate.queryForObject(GET_VACATIONS_BY_TEACHER_AND_DATE_QUERY, new Object[]{teacher.getId(), date}, vacationMapper);
    }

    @Override
    public Vacation getById(int id) {
        return jdbcTemplate.queryForObject(GET_VACATION_BY_ID_QUERY, new Object[]{id}, vacationMapper);
    }

    @Override
    public void update(Vacation vacation) {
        jdbcTemplate.update(UPDATE_VACATION_QUERY, vacation.getTeacher().getId(), vacation.getStartDate(),
            vacation.getEndDate(), vacation.isPaid(), vacation.getCause(), vacation.getId());
    }

    @Override
    public void remove(Vacation vacation) {
        jdbcTemplate.update(DELETE_VACATION_QUERY, vacation.getId());
    }
}

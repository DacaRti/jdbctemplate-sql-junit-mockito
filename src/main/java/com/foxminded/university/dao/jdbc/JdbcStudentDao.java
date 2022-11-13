package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.AddressDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.dao.jdbc.mappers.StudentMapper;
import com.foxminded.university.model.Student;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
public class JdbcStudentDao implements StudentDao {

    private static final String CREATE_STUDENT_QUERY = "INSERT INTO students (first_name, last_name, gender, email, birth_date, phone, address_id, study_form) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String GET_STUDENTS_QUERY = "SELECT * FROM students s JOIN addresses a on a.id = s.address_id";
    private static final String GET_STUDENT_BY_ID_QUERY = "SELECT * FROM students s JOIN addresses a on a.id = s.address_id WHERE s.id = ?";
    private static final String UPDATE_STUDENT_QUERY = "UPDATE students SET first_name=?, last_name=?, " +
        "gender=?, email=?, birth_date=?, phone=?, address_id=?, study_form=? WHERE id = ?";
    private static final String UPDATE_STUDENT_GROUP_QUERY = "UPDATE students SET group_id=? WHERE id=?";
    private static final String DELETE_STUDENT_QUERY = "DELETE from students WHERE id=?";
    private static final String GET_ALL_STUDENTS_BY_GROUP = "SELECT * FROM students s " +
        "JOIN addresses a on a.id = s.address_id " +
        "JOIN groups g ON g.id = s.group_id WHERE group_id=?";

    private final JdbcTemplate jdbcTemplate;

    private final StudentMapper studentMapper;

    private final AddressDao addressDao;

    public JdbcStudentDao(JdbcTemplate jdbcTemplate, StudentMapper studentMapper, AddressDao addressDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.studentMapper = studentMapper;
        this.addressDao = addressDao;
    }

    @Override
    @Transactional
    public void create(Student student) {
        addressDao.create(student.getAddress());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(CREATE_STUDENT_QUERY, new String[]{"id"});
            statement.setString(1, student.getFirstName());
            statement.setString(2, student.getLastName());
            statement.setString(3, String.valueOf(student.getGender()));
            statement.setString(4, student.getEmail());
            statement.setObject(5, student.getBirthDate());
            statement.setString(6, student.getPhone());
            statement.setInt(7, student.getAddress().getId());
            statement.setString(8, String.valueOf(student.getStudyForm()));

            return statement;
        }, keyHolder);

        student.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    public List<Student> getAll() {
        return jdbcTemplate.query(GET_STUDENTS_QUERY, studentMapper);
    }

    @Override
    public Student getById(int id) {
        return jdbcTemplate.queryForObject(GET_STUDENT_BY_ID_QUERY, new Object[]{id}, studentMapper);
    }

    @Override
    public List<Student> getByGroupId(int id) {
        return jdbcTemplate.query(GET_ALL_STUDENTS_BY_GROUP, new Object[]{id}, studentMapper);
    }

    @Override
    @Transactional
    public void update(Student student) {
        addressDao.update(student.getAddress());
        jdbcTemplate.update(UPDATE_STUDENT_QUERY, student.getFirstName(), student.getLastName(),
            String.valueOf(student.getGender()), student.getEmail(), student.getBirthDate(), student.getPhone(),
            student.getAddress().getId(), String.valueOf(student.getStudyForm()), student.getId());

        if (student.getGroup() != null) {
            jdbcTemplate.update(UPDATE_STUDENT_GROUP_QUERY, student.getGroup().getId(), student.getId());
        }
    }

    @Override
    public void remove(Student student) {
        jdbcTemplate.update(DELETE_STUDENT_QUERY, student.getId());
    }
}

package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.AddressDao;
import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.dao.jdbc.mappers.TeacherMapper;
import com.foxminded.university.model.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@Repository
public class JdbcTeacherDao implements TeacherDao {

    private static final String CREATE_TEACHER_QUERY = "INSERT INTO teachers" +
        "(first_name, last_name, gender, email, birth_date, phone, address_id, degree, rank, faculty_id)" +
        " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String GET_TEACHERS_QUERY = "SELECT * FROM teachers t " +
        "JOIN addresses a on a.id = t.address_id " +
        "JOIN faculties f on f.id = t.faculty_id " +
        "JOIN syllabuses s on s.id = f.syllabus_id";
    private static final String GET_TEACHER_BY_ID_QUERY = "SELECT * FROM teachers t " +
        "JOIN addresses a on a.id = t.address_id " +
        "JOIN faculties f on f.id = t.faculty_id " +
        "JOIN syllabuses s on s.id = f.syllabus_id " +
        "WHERE t.id = ?";
    private static final String UPDATE_TEACHER_QUERY = "UPDATE teachers SET first_name=?, last_name=?, " +
        "gender=?, email=?, birth_date=?, phone=?, address_id=?, degree=?, rank=?, faculty_id=?  WHERE id = ?";
    private static final String DELETE_TEACHER_QUERY = "DELETE from teachers WHERE id=?";

    private static final String ADD_SUBJECTS_TO_TEACHER_QUERY = "INSERT INTO teachers_subjects (teacher_id, subject_id) VALUES (?, ?)";
    private static final String DELETE_TEACHER_SUBJECT_QUERY = "DELETE FROM teachers_subjects where teacher_id=? AND subject_id=?";

    private final JdbcTemplate jdbcTemplate;

    private final TeacherMapper teacherMapper;

    private final AddressDao addressDao;

    private final SubjectDao subjectDao;

    public JdbcTeacherDao(JdbcTemplate jdbcTemplate, TeacherMapper teacherMapper, AddressDao addressDao, SubjectDao subjectDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.teacherMapper = teacherMapper;
        this.addressDao = addressDao;
        this.subjectDao = subjectDao;
    }

    @Override
    @Transactional
    public void create(Teacher teacher) {
        addressDao.create(teacher.getAddress());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(CREATE_TEACHER_QUERY, new String[]{"id"});
            statement.setString(1, teacher.getFirstName());
            statement.setString(2, teacher.getLastName());
            statement.setString(3, String.valueOf(teacher.getGender()));
            statement.setString(4, teacher.getEmail());
            statement.setObject(5, teacher.getBirthDate());
            statement.setString(6, teacher.getPhone());
            statement.setInt(7, teacher.getAddress().getId());
            statement.setString(8, String.valueOf(teacher.getDegree()));
            statement.setString(9, teacher.getRank());
            statement.setInt(10, teacher.getFaculty().getId());

            return statement;
        }, keyHolder);

        teacher.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        teacher.getSkills().forEach(subject -> jdbcTemplate.update(ADD_SUBJECTS_TO_TEACHER_QUERY, teacher.getId(), subject.getId()));
    }

    @Override
    public List<Teacher> getAll() {
        return jdbcTemplate.query(GET_TEACHERS_QUERY, teacherMapper);
    }

    @Override
    public Teacher getById(int id) {
        return jdbcTemplate.queryForObject(GET_TEACHER_BY_ID_QUERY, new Object[]{id}, teacherMapper);
    }

    @Override
    @Transactional
    public void update(Teacher teacher) {
        addressDao.update(teacher.getAddress());
        jdbcTemplate.update(UPDATE_TEACHER_QUERY, teacher.getFirstName(), teacher.getLastName(),
            String.valueOf(teacher.getGender()), teacher.getEmail(), teacher.getBirthDate(), teacher.getPhone(),
            teacher.getAddress().getId(), String.valueOf(teacher.getDegree()), teacher.getRank(),
            teacher.getFaculty().getId(), teacher.getId());

        List<Subject> subjectsByTeacherId = subjectDao.getByTeacherId(teacher.getId());

        subjectsByTeacherId.stream()
            .filter(Predicate.not(teacher.getSkills()::contains))
            .forEach(subject -> jdbcTemplate.update(DELETE_TEACHER_SUBJECT_QUERY, teacher.getId(), subject.getId()));

        teacher.getSkills().stream()
            .filter(Predicate.not(subjectsByTeacherId::contains))
            .forEach(subject -> jdbcTemplate.update(ADD_SUBJECTS_TO_TEACHER_QUERY, teacher.getId(), subject.getId()));
    }

    @Override
    public void remove(Teacher teacher) {
        jdbcTemplate.update(DELETE_TEACHER_QUERY, teacher.getId());
    }
}

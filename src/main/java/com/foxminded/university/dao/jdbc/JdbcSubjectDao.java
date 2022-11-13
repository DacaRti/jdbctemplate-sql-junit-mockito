package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.dao.jdbc.mappers.SubjectMapper;
import com.foxminded.university.model.Subject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
public class JdbcSubjectDao implements SubjectDao {

    private static final String CREATE_SUBJECT_QUERY = "INSERT INTO subjects (name) VALUES (?)";
    private static final String GET_SUBJECTS_QUERY = "SELECT * FROM subjects";
    private static final String GET_SUBJECT_BY_ID_QUERY = "SELECT * FROM subjects WHERE id = ?";
    private static final String UPDATE_SUBJECT_QUERY = "UPDATE subjects SET name=? WHERE id = ?";
    private static final String DELETE_SUBJECT_QUERY = "DELETE from subjects WHERE id=?";
    private static final String GET_ALL_SUBJECTS_BY_TEACHER_ID_QUERY = "SELECT * FROM subjects s " +
        "JOIN teachers_subjects ts ON s.id = ts.subject_id WHERE ts.teacher_id=?";
    private static final String GET_ALL_SUBJECTS_BY_SYLLABUS_ID_QUERY = "SELECT * FROM subjects s " +
        "JOIN syllabuses_subjects ss ON s.id = ss.subject_id WHERE ss.syllabus_id=?";

    private final JdbcTemplate jdbcTemplate;

    private final SubjectMapper subjectMapper;

    public JdbcSubjectDao(JdbcTemplate jdbcTemplate, SubjectMapper subjectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.subjectMapper = subjectMapper;
    }

    @Override
    public void create(Subject subject) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(CREATE_SUBJECT_QUERY, new String[]{"id"});
            statement.setString(1, subject.getName());

            return statement;
        }, keyHolder);

        subject.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    public List<Subject> getAll() {
        return jdbcTemplate.query(GET_SUBJECTS_QUERY, subjectMapper);
    }

    @Override
    public Subject getById(int id) {
        return jdbcTemplate.queryForObject(GET_SUBJECT_BY_ID_QUERY, new Object[]{id}, subjectMapper);
    }

    @Override
    public void update(Subject subject) {
        jdbcTemplate.update(UPDATE_SUBJECT_QUERY, subject.getName(), subject.getId());
    }

    @Override
    public void remove(Subject subject) {
        jdbcTemplate.update(DELETE_SUBJECT_QUERY, subject.getId());
    }

    @Override
    public List<Subject> getByTeacherId(int id) {
        return jdbcTemplate.query(GET_ALL_SUBJECTS_BY_TEACHER_ID_QUERY, new Object[]{id}, subjectMapper);
    }

    @Override
    public List<Subject> getBySyllabusId(int id) {
        return jdbcTemplate.query(GET_ALL_SUBJECTS_BY_SYLLABUS_ID_QUERY, new Object[]{id}, subjectMapper);
    }
}

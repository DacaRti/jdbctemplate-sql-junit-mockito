package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.dao.SyllabusDao;
import com.foxminded.university.dao.jdbc.mappers.SyllabusMapper;
import com.foxminded.university.model.Subject;
import com.foxminded.university.model.Syllabus;
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
public class JdbcSyllabusDao implements SyllabusDao {

    private static final String CREATE_SYLLABUS_QUERY = "INSERT INTO syllabuses (full_time) VALUES (?)";
    private static final String GET_SYLLABUSES_QUERY = "SELECT * FROM syllabuses";
    private static final String GET_SYLLABUS_BY_ID_QUERY = "SELECT * FROM syllabuses WHERE id = ?";
    private static final String UPDATE_SYLLABUS_QUERY = "UPDATE syllabuses SET full_time=? WHERE id = ?";
    private static final String DELETE_SYLLABUS_QUERY = "DELETE from syllabuses WHERE id=?";

    private static final String ADD_SUBJECTS_TO_SYLLABUS_QUERY = "INSERT INTO syllabuses_subjects (syllabus_id, subject_id) VALUES (?, ?)";
    private static final String DELETE_SUBJECT_FROM_SYLLABUS_QUERY = "DELETE FROM syllabuses_subjects where syllabus_id=? AND subject_id=?";


    private final JdbcTemplate jdbcTemplate;

    private final SyllabusMapper syllabusMapper;

    private final SubjectDao subjectDao;

    public JdbcSyllabusDao(JdbcTemplate jdbcTemplate, SyllabusMapper syllabusMapper, SubjectDao subjectDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.syllabusMapper = syllabusMapper;
        this.subjectDao = subjectDao;
    }

    @Override
    public void create(Syllabus syllabus) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(CREATE_SYLLABUS_QUERY, new String[]{"id"});
            statement.setInt(1, syllabus.getFullTime());

            return statement;
        }, keyHolder);

        syllabus.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    public List<Syllabus> getAll() {
        return jdbcTemplate.query(GET_SYLLABUSES_QUERY, syllabusMapper);
    }

    @Override
    public Syllabus getById(int id) {
        return jdbcTemplate.queryForObject(GET_SYLLABUS_BY_ID_QUERY, new Object[]{id}, syllabusMapper);
    }

    @Override
    @Transactional
    public void update(Syllabus syllabus) {
        jdbcTemplate.update(UPDATE_SYLLABUS_QUERY, syllabus.getFullTime(), syllabus.getId());

        List<Subject> subjectsBySyllabusId = subjectDao.getBySyllabusId(syllabus.getId());

        subjectsBySyllabusId.stream()
            .filter(Predicate.not(syllabus.getSubjects()::contains))
            .forEach(subject -> jdbcTemplate.update(DELETE_SUBJECT_FROM_SYLLABUS_QUERY, syllabus.getId(), subject.getId()));

        syllabus.getSubjects().stream()
            .filter(Predicate.not(subjectsBySyllabusId::contains))
            .forEach(subject -> jdbcTemplate.update(ADD_SUBJECTS_TO_SYLLABUS_QUERY, syllabus.getId(), subject.getId()));
    }

    @Override
    public void remove(Syllabus syllabus) {
        jdbcTemplate.update(DELETE_SYLLABUS_QUERY, syllabus.getId());
    }
}

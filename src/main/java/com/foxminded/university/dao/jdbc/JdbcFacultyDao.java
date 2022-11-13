package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.FacultyDao;
import com.foxminded.university.dao.SyllabusDao;
import com.foxminded.university.dao.jdbc.mappers.FacultyMapper;
import com.foxminded.university.model.Faculty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
public class JdbcFacultyDao implements FacultyDao {

    private static final String CREATE_FACULTY_QUERY = "INSERT INTO faculties (name, syllabus_id) VALUES (?, ?)";
    private static final String GET_FACULTIES_QUERY = "SELECT * FROM faculties f JOIN syllabuses s on f.syllabus_id = s.id";
    private static final String GET_FACULTY_BY_ID_QUERY = "SELECT * FROM faculties f JOIN syllabuses s on f.syllabus_id = s.id WHERE f.id = ?";
    private static final String GET_FACULTY_BY_NAME_QUERY = "SELECT * FROM faculties f JOIN syllabuses s on f.syllabus_id = s.id WHERE f.name = ?";
    private static final String UPDATE_FACULTY_QUERY = "UPDATE faculties SET name=?, syllabus_id=? WHERE id = ?";
    private static final String DELETE_FACULTY_QUERY = "DELETE from faculties WHERE id=?";

    private final JdbcTemplate jdbcTemplate;

    private final FacultyMapper facultyMapper;

    private final SyllabusDao syllabusDao;

    public JdbcFacultyDao(JdbcTemplate jdbcTemplate, FacultyMapper facultyMapper, SyllabusDao syllabusDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.facultyMapper = facultyMapper;
        this.syllabusDao = syllabusDao;
    }

    @Override
    @Transactional
    public void create(Faculty faculty) {
        syllabusDao.create(faculty.getSyllabus());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(CREATE_FACULTY_QUERY, new String[]{"id"});
            statement.setString(1, faculty.getName());
            statement.setInt(2, faculty.getSyllabus().getId());

            return statement;
        }, keyHolder);

        faculty.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    public List<Faculty> getAll() {
        return jdbcTemplate.query(GET_FACULTIES_QUERY, facultyMapper);
    }

    @Override
    public Faculty getById(int id) {
        return jdbcTemplate.queryForObject(GET_FACULTY_BY_ID_QUERY, new Object[]{id}, facultyMapper);
    }

    @Override
    public Faculty getByName(String name) {
        return jdbcTemplate.queryForObject(GET_FACULTY_BY_NAME_QUERY, new Object[]{name}, facultyMapper);
    }

    @Override
    public void update(Faculty faculty) {
        jdbcTemplate.update(UPDATE_FACULTY_QUERY, faculty.getName(), faculty.getSyllabus().getId(), faculty.getId());
    }

    @Override
    public void remove(Faculty faculty) {
        jdbcTemplate.update(DELETE_FACULTY_QUERY, faculty.getId());
    }
}

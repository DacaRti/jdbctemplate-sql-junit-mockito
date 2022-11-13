package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.ClassroomDao;
import com.foxminded.university.dao.jdbc.mappers.ClassroomMapper;
import com.foxminded.university.model.Classroom;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
public class JdbcClassroomDao implements ClassroomDao {

    private static final String CREATE_CLASSROOM_QUERY = "INSERT INTO classrooms (number, floor, capacity) VALUES (?, ?, ?)";
    private static final String GET_CLASSROOMS_QUERY = "SELECT * FROM classrooms";
    private static final String GET_CLASSROOM_BY_ID_QUERY = "SELECT * FROM classrooms WHERE id = ?";
    private static final String UPDATE_CLASSROOM_QUERY = "UPDATE classrooms SET number = ?, floor = ?, capacity = ? WHERE id = ?";
    private static final String DELETE_CLASSROOM_QUERY = "DELETE FROM classrooms WHERE id = ?";
    private static final String GET_CLASSROOM_BY_NUMBER_QUERY = "SELECT * FROM classrooms WHERE number = ?";

    private final JdbcTemplate jdbcTemplate;

    private final ClassroomMapper classroomMapper;

    public JdbcClassroomDao(JdbcTemplate jdbcTemplate, ClassroomMapper classroomMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.classroomMapper = classroomMapper;
    }

    @Override
    public void create(Classroom classroom) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(CREATE_CLASSROOM_QUERY, new String[]{"id"});
            statement.setInt(1, classroom.getNumber());
            statement.setInt(2, classroom.getFloor());
            statement.setInt(3, classroom.getCapacity());

            return statement;
        }, keyHolder);

        classroom.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    public List<Classroom> getAll() {
        return jdbcTemplate.query(GET_CLASSROOMS_QUERY, classroomMapper);
    }

    @Override
    public Classroom getById(int id) {
        return jdbcTemplate.queryForObject(GET_CLASSROOM_BY_ID_QUERY, new Object[]{id}, classroomMapper);
    }

    @Override
    public Classroom getByNumber(int number) {
        return jdbcTemplate.queryForObject(GET_CLASSROOM_BY_NUMBER_QUERY, new Object[]{number}, classroomMapper);
    }

    @Override
    public void update(Classroom classroom) {
        jdbcTemplate.update(UPDATE_CLASSROOM_QUERY, classroom.getNumber(), classroom.getFloor(), classroom.getCapacity(), classroom.getId());
    }

    @Override
    public void remove(Classroom classroom) {
        jdbcTemplate.update(DELETE_CLASSROOM_QUERY, classroom.getId());
    }
}
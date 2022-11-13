package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.jdbc.mappers.GroupMapper;
import com.foxminded.university.model.Group;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
public class JdbcGroupDao implements GroupDao {

    private static final String CREATE_GROUP_QUERY = "INSERT INTO groups(name, course, faculty_id) VALUES (?, ?, ?)";
    private static final String GET_GROUPS_QUERY = "SELECT * FROM groups";
    private static final String GET_GROUP_BY_ID_QUERY = "SELECT * FROM groups WHERE id=?";
    private static final String UPDATE_GROUP_QUERY = "UPDATE groups SET name=?, course=?, faculty_id=? WHERE id=?";
    private static final String DELETE_GROUP_QUERY = "DELETE FROM groups WHERE id=?";
    private static final String GET_ALL_GROUPS_ON_LESSON_QUERY = "SELECT * FROM groups g " +
        "JOIN groups_lessons gl ON g.id = gl.group_id WHERE lesson_id=?";
    private static final String GET_GROUP_BY_NAME_QUERY = "SELECT * FROM groups WHERE name = ?";

    private final JdbcTemplate jdbcTemplate;

    private final GroupMapper groupMapper;

    public JdbcGroupDao(JdbcTemplate jdbcTemplate, GroupMapper groupMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.groupMapper = groupMapper;
    }

    @Override
    public void create(Group group) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(CREATE_GROUP_QUERY, new String[]{"id"});
            statement.setString(1, group.getName());
            statement.setInt(2, group.getCourse());
            statement.setInt(3, group.getFaculty().getId());

            return statement;
        }, keyHolder);

        group.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    public List<Group> getAll() {
        return jdbcTemplate.query(GET_GROUPS_QUERY, groupMapper);
    }

    @Override
    public Group getById(int id) {
        return jdbcTemplate.queryForObject(GET_GROUP_BY_ID_QUERY, new Object[]{id}, groupMapper);
    }

    @Override
    public List<Group> getByLessonId(int id) {
        return jdbcTemplate.query(GET_ALL_GROUPS_ON_LESSON_QUERY, new Object[]{id}, groupMapper);
    }

    @Override
    public Group getByName(String name) {
        return jdbcTemplate.queryForObject(GET_GROUP_BY_NAME_QUERY, new Object[]{name}, groupMapper);
    }

    @Override
    public void update(Group group) {
        jdbcTemplate.update(UPDATE_GROUP_QUERY, group.getName(), group.getCourse(), group.getFaculty().getId(), group.getId());
    }

    @Override
    public void remove(Group group) {
        jdbcTemplate.update(DELETE_GROUP_QUERY, group.getId());
    }
}

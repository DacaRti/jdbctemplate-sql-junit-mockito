package com.foxminded.university.dao.jdbc.mappers;

import com.foxminded.university.model.Group;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GroupMapper implements RowMapper<Group> {

    public Group mapRow(ResultSet rs, int rowNum) throws SQLException {

        Group group = new Group();
        group.setId(rs.getInt("id"));
        group.setName(rs.getString("name"));
        group.setCourse(rs.getInt("course"));

        return group;
    }
}

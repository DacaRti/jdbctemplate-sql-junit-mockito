package com.foxminded.university.dao;

import com.foxminded.university.model.Faculty;

public interface FacultyDao extends Dao<Faculty> {

    Faculty getByName(String name);
}

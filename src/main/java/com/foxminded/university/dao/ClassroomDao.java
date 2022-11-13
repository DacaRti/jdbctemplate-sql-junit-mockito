package com.foxminded.university.dao;

import com.foxminded.university.model.Classroom;

public interface ClassroomDao extends Dao<Classroom> {

    Classroom getByNumber(int number);
}

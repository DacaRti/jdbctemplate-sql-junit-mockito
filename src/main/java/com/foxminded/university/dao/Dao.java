package com.foxminded.university.dao;

import java.util.List;

public interface Dao<E> {

    void create(E entity);

    List<E> getAll();

    E getById(int id);

    void update(E entity);

    void remove(E entity);
}

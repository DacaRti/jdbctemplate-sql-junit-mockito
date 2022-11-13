package com.foxminded.university.dao.jdbc;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.dao.jdbc.mappers.LessonMapper;
import com.foxminded.university.model.*;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@Repository
public class JdbcLessonDao implements LessonDao {

    private static final String CREATE_LESSON_QUERY = "INSERT INTO lessons(subject_id, classroom_id, " +
        "date, duration_id, teacher_id) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_LESSONS_QUERY = "SELECT * FROM lessons l " +
        "JOIN classrooms c on c.id = l.classroom_id JOIN subjects sub on sub.id = l.subject_id " +
        "JOIN teachers t on t.id = l.teacher_id JOIN faculties f on f.id = t.faculty_id " +
        "JOIN addresses a on a.id = t.address_id JOIN syllabuses s on s.id = f.syllabus_id " +
        "JOIN durations d on l.duration_id = d.id";
    private static final String GET_LESSON_BY_ID_QUERY = "SELECT * FROM lessons l " +
        "JOIN classrooms c on c.id = l.classroom_id JOIN subjects sub on sub.id = l.subject_id " +
        "JOIN teachers t on t.id = l.teacher_id JOIN faculties f on f.id = t.faculty_id " +
        "JOIN addresses a on a.id = t.address_id JOIN syllabuses s on s.id = f.syllabus_id " +
        "JOIN groups_lessons gl on gl.lesson_id = l.id " +
        "JOIN groups g on gl.group_id = g.id " +
        "JOIN durations d on l.duration_id = d.id " +
        "WHERE l.id=?";
    private static final String UPDATE_LESSON_QUERY = "UPDATE lessons " +
        "SET subject_id=?, classroom_id=?, " +
        "date=?, duration_id=?, teacher_id=? WHERE id=?";
    private static final String DELETE_LESSON_QUERY = "DELETE FROM lessons WHERE id=?";

    private static final String ADD_GROUP_TO_LESSON_QUERY = "INSERT INTO groups_lessons (group_id, lesson_id) VALUES (?, ?)";
    private static final String DELETE_GROUP_FROM_LESSON_QUERY = "DELETE FROM groups_lessons WHERE group_id=? AND lesson_id=?";

    private static final String GET_GROUP_DAY_TIMETABLE_QUERY =
        "SELECT * FROM lessons l " +
            "JOIN classrooms c on c.id = l.classroom_id JOIN subjects sub on sub.id = l.subject_id " +
            "JOIN teachers t on t.id = l.teacher_id JOIN faculties f on f.id = t.faculty_id " +
            "JOIN addresses a on a.id = t.address_id JOIN syllabuses s on s.id = f.syllabus_id " +
            "JOIN groups_lessons gl on gl.lesson_id = l.id " +
            "JOIN groups g on gl.group_id = g.id " +
            "JOIN durations d on l.duration_id = d.id " +
            "WHERE g.id=? AND l.date=?";

    private static final String GET_GROUP_MONTH_TIMETABLE_QUERY =
        "SELECT * FROM lessons l " +
            "JOIN classrooms c on c.id = l.classroom_id JOIN subjects sub on sub.id = l.subject_id " +
            "JOIN teachers t on t.id = l.teacher_id JOIN faculties f on f.id = t.faculty_id " +
            "JOIN addresses a on a.id = t.address_id JOIN syllabuses s on s.id = f.syllabus_id " +
            "JOIN groups_lessons gl on gl.lesson_id = l.id " +
            "JOIN groups g on gl.group_id = g.id " +
            "JOIN durations d on l.duration_id = d.id " +
            "WHERE g.id=? AND l.date BETWEEN ? and ?";

    private static final String GET_TEACHER_DAY_TIMETABLE_QUERY =
        "SELECT * FROM lessons l " +
            "JOIN classrooms c on c.id = l.classroom_id JOIN subjects sub on sub.id = l.subject_id " +
            "JOIN teachers t on t.id = l.teacher_id JOIN faculties f on f.id = t.faculty_id " +
            "JOIN addresses a on a.id = t.address_id JOIN syllabuses s on s.id = f.syllabus_id " +
            "JOIN groups_lessons gl on gl.lesson_id = l.id " +
            "JOIN groups g on gl.group_id = g.id " +
            "JOIN durations d on l.duration_id = d.id " +
            "WHERE t.id=? AND l.date=?";

    private static final String GET_TEACHER_MONTH_TIMETABLE_QUERY =
        "SELECT * FROM lessons l " +
            "JOIN classrooms c on c.id = l.classroom_id JOIN subjects sub on sub.id = l.subject_id " +
            "JOIN teachers t on t.id = l.teacher_id JOIN faculties f on f.id = t.faculty_id " +
            "JOIN addresses a on a.id = t.address_id JOIN syllabuses s on s.id = f.syllabus_id " +
            "JOIN groups_lessons gl on gl.lesson_id = l.id " +
            "JOIN groups g on gl.group_id = g.id " +
            "JOIN durations d on l.duration_id = d.id " +
            "WHERE t.id=? AND l.date BETWEEN ? and ?";

    private static final String GET_CLASSROOM_DAY_TIMETABLE_QUERY =
        "SELECT * FROM lessons l " +
            "JOIN classrooms c on c.id = l.classroom_id JOIN subjects sub on sub.id = l.subject_id " +
            "JOIN teachers t on t.id = l.teacher_id JOIN faculties f on f.id = t.faculty_id " +
            "JOIN addresses a on a.id = t.address_id JOIN syllabuses s on s.id = f.syllabus_id " +
            "JOIN groups_lessons gl on gl.lesson_id = l.id " +
            "JOIN groups g on gl.group_id = g.id " +
            "JOIN durations d on l.duration_id = d.id " +
            "WHERE c.id=? AND l.date=?";

    private static final String GET_CLASSROOM_MONTH_TIMETABLE_QUERY =
        "SELECT * FROM lessons l " +
            "JOIN classrooms c on c.id = l.classroom_id JOIN subjects sub on sub.id = l.subject_id " +
            "JOIN teachers t on t.id = l.teacher_id JOIN faculties f on f.id = t.faculty_id " +
            "JOIN addresses a on a.id = t.address_id JOIN syllabuses s on s.id = f.syllabus_id " +
            "JOIN groups_lessons gl on gl.lesson_id = l.id " +
            "JOIN groups g on gl.group_id = g.id " +
            "JOIN durations d on l.duration_id = d.id " +
            "WHERE c.id=? AND l.date BETWEEN ? and ?";

    private static final String GET_LESSONS_BY_TEACHER_AND_DATETIME =
        "SELECT * FROM lessons l " +
            "JOIN classrooms c on c.id = l.classroom_id JOIN subjects sub on sub.id = l.subject_id " +
            "JOIN teachers t on t.id = l.teacher_id JOIN faculties f on f.id = t.faculty_id " +
            "JOIN addresses a on a.id = t.address_id JOIN syllabuses s on s.id = f.syllabus_id " +
            "JOIN groups_lessons gl on gl.lesson_id = l.id " +
            "JOIN groups g on gl.group_id = g.id " +
            "JOIN durations d on l.duration_id = d.id " +
            "WHERE l.teacher_id = ? AND l.date = ? AND l.duration_id = ?";

    private static final String GET_LESSONS_BY_CLASSROOM_AND_DATETIME =
        "SELECT * FROM lessons l " +
            "JOIN classrooms c on c.id = l.classroom_id JOIN subjects sub on sub.id = l.subject_id " +
            "JOIN teachers t on t.id = l.teacher_id JOIN faculties f on f.id = t.faculty_id " +
            "JOIN addresses a on a.id = t.address_id JOIN syllabuses s on s.id = f.syllabus_id " +
            "JOIN groups_lessons gl on gl.lesson_id = l.id " +
            "JOIN groups g on gl.group_id = g.id " +
            "JOIN durations d on l.duration_id = d.id " +
            "WHERE l.classroom_id = ? AND l.date = ? AND l.duration_id = ?";

    private static final String GET_LESSONS_BY_GROUP_AND_DATETIME =
        "SELECT * FROM lessons l " +
            "JOIN classrooms c on c.id = l.classroom_id JOIN subjects sub on sub.id = l.subject_id " +
            "JOIN teachers t on t.id = l.teacher_id JOIN faculties f on f.id = t.faculty_id " +
            "JOIN addresses a on a.id = t.address_id JOIN syllabuses s on s.id = f.syllabus_id " +
            "JOIN groups_lessons gl on gl.lesson_id = l.id " +
            "JOIN groups g on gl.group_id = g.id " +
            "JOIN durations d on l.duration_id = d.id " +
            "WHERE l.date = ? AND l.duration_id = ? AND g.id = ?";

    private final JdbcTemplate jdbcTemplate;

    private final LessonMapper lessonMapper;

    private final GroupDao groupDao;

    public JdbcLessonDao(JdbcTemplate jdbcTemplate, LessonMapper lessonMapper, GroupDao groupDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.lessonMapper = lessonMapper;
        this.groupDao = groupDao;
    }

    @Override
    @Transactional
    public void create(Lesson lesson) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(CREATE_LESSON_QUERY, new String[]{"id"});
            statement.setInt(1, lesson.getSubject().getId());
            statement.setInt(2, lesson.getClassroom().getId());
            statement.setObject(3, lesson.getDate());
            statement.setInt(4, lesson.getDuration().getId());
            statement.setInt(5, lesson.getTeacher().getId());

            return statement;
        }, keyHolder);

        lesson.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        lesson.getGroups().forEach(group -> jdbcTemplate.update(ADD_GROUP_TO_LESSON_QUERY, group.getId(), lesson.getId()));
    }

    @Override
    public List<Lesson> getAll() {
        return jdbcTemplate.query(GET_LESSONS_QUERY, lessonMapper);
    }

    @Override
    public Lesson getById(int id) {
        return jdbcTemplate.queryForObject(GET_LESSON_BY_ID_QUERY, new Object[]{id}, lessonMapper);
    }

    @Override
    public List<Lesson> getByGroupAndDate(Group group, LocalDate date) {
        return jdbcTemplate.query(GET_GROUP_DAY_TIMETABLE_QUERY, new Object[]{group.getId(), date}, lessonMapper);
    }

    @Override
    public List<Lesson> getByGroupAndDateRange(Group group, LocalDate startDate, LocalDate endDate) {
        return jdbcTemplate.query(GET_GROUP_MONTH_TIMETABLE_QUERY, new Object[]{group.getId(), startDate, endDate}, lessonMapper);
    }

    @Override
    public List<Lesson> getByTeacherAndDate(Teacher teacher, LocalDate date) {
        return jdbcTemplate.query(GET_TEACHER_DAY_TIMETABLE_QUERY, new Object[]{teacher.getId(), date}, lessonMapper);
    }

    @Override
    public List<Lesson> getByTeacherAndDateRange(Teacher teacher, LocalDate startDate, LocalDate endDate) {
        return jdbcTemplate.query(GET_TEACHER_MONTH_TIMETABLE_QUERY, new Object[]{teacher.getId(), startDate, endDate}, lessonMapper);
    }

    @Override
    public List<Lesson> getByClassroomAndDate(Classroom classroom, LocalDate date) {
        return jdbcTemplate.query(GET_CLASSROOM_DAY_TIMETABLE_QUERY, new Object[]{classroom.getId(), date}, lessonMapper);
    }

    @Override
    public List<Lesson> getByClassroomAndDateRange(Classroom classroom, LocalDate startDate, LocalDate endDate) {
        return jdbcTemplate.query(GET_CLASSROOM_MONTH_TIMETABLE_QUERY,
            new Object[]{classroom.getId(), startDate, endDate}, lessonMapper);
    }

    @Override
    public Lesson getByTeacherAndDateTime(Teacher teacher, LocalDate date, Duration duration) {
        return jdbcTemplate.queryForObject(GET_LESSONS_BY_TEACHER_AND_DATETIME, new Object[]{teacher.getId(), date, duration.getId()}, lessonMapper);
    }

    @Override
    public Lesson getByClassroomAndDateTime(Classroom classroom, LocalDate date, Duration duration) {
        return jdbcTemplate.queryForObject(GET_LESSONS_BY_CLASSROOM_AND_DATETIME, new Object[]{classroom.getId(), date, duration.getId()}, lessonMapper);
    }

    @Override
    public Lesson getByGroupAndDateTime(Group group, LocalDate date, Duration duration) {
        return jdbcTemplate.queryForObject(GET_LESSONS_BY_GROUP_AND_DATETIME, new Object[]{date, duration.getId(), group.getId()}, lessonMapper);
    }

    @Override
    @Transactional
    public void update(Lesson lesson) {
        jdbcTemplate.update(UPDATE_LESSON_QUERY, lesson.getSubject().getId(), lesson.getClassroom().getId(),
            lesson.getDate(), lesson.getDuration().getId(), lesson.getTeacher().getId(), lesson.getId());

        List<Group> groupsByLessonId = groupDao.getByLessonId(lesson.getId());

        groupsByLessonId.stream()
            .filter(Predicate.not(lesson.getGroups()::contains))
            .forEach(group -> jdbcTemplate.update(DELETE_GROUP_FROM_LESSON_QUERY, group.getId(), lesson.getId()));

        lesson.getGroups().stream()
            .filter(Predicate.not(groupsByLessonId::contains))
            .forEach(group -> jdbcTemplate.update(ADD_GROUP_TO_LESSON_QUERY, group.getId(), lesson.getId()));
    }

    @Override
    public void remove(Lesson lesson) {
        jdbcTemplate.update(DELETE_LESSON_QUERY, lesson.getId());
    }
}

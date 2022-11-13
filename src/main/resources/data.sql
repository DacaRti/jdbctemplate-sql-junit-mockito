INSERT INTO addresses
VALUES (1, 'a', 'a', 'a', 'a');
INSERT INTO addresses
VALUES (2, 'b', 'b', 'b', 'b');
INSERT INTO addresses
VALUES (3, 'c', 'c', 'c', 'c');

INSERT INTO classrooms
VALUES (1, 101, 1, 20);
INSERT INTO classrooms
VALUES (2, 102, 1, 60);
INSERT INTO classrooms
VALUES (3, 201, 2, 20);
INSERT INTO classrooms
VALUES (4, 202, 2, 60);

INSERT INTO subjects
VALUES (1, 'MATH');
INSERT INTO subjects
VALUES (2, 'LITERATURE');
INSERT INTO subjects
VALUES (3, 'PHYSICS');

INSERT INTO syllabuses
VALUES (1, 25);
INSERT INTO syllabuses
VALUES (2, 25);

INSERT INTO faculties
VALUES (1, 'a', 1);
INSERT INTO faculties
VALUES (2, 'b', 2);

INSERT INTO teachers
VALUES (1, 'Tsunade', 'Senju', 'FEMALE', 'Hokage@Gmail.com', '1900-03-03', '05034343434', 1, 'DOCTOR', 'Hokage', 1);
INSERT INTO teachers
VALUES (2, 'Kakashi', 'Hatake', 'MALE', 'Hatake@Gmail.com', '1960-03-03', '0503234234', 2, 'DOCTOR', 'Hokage', 2);

INSERT INTO groups
VALUES (1, 'SMK-16', 1, 1);
INSERT INTO groups
VALUES (2, 'SMK-17', 1, 1);
INSERT INTO groups
VALUES (3, 'SMK-18', 1, 2);

INSERT INTO students
VALUES (1, 1, 'Naruto', 'Uzumaki', 'MALE', 'DacaP64@gmail.com', '1999-05-05', '033333333', 1, 'EXTRAMURAL');
INSERT INTO students
VALUES (2, 2, 'Sacura', 'Haruno', 'FEMALE', 'Sacura64@gmail.com', '1999-04-04', '0222222223', 2, 'EXTRAMURAL');

INSERT INTO durations
VALUES (1,'8:00:00', '10:00:00');
INSERT INTO durations
VALUES (2,'13:00:00', '15:00:00');

INSERT INTO lessons
VALUES (1, 1, 1, '2022-05-05', 1, 1);
INSERT INTO lessons
VALUES (2, 2, 2, '2022-09-09', 2, 2);
INSERT INTO lessons
VALUES (3, 3, 2, '2022-09-09', 2, 2);

INSERT INTO holidays
VALUES (1, 'New Year','2022-03-03');
INSERT INTO holidays
VALUES (2, 'Cristmas','2022-05-05');

INSERT INTO teachers_subjects
VALUES (1, 1);

INSERT INTO groups_lessons
VALUES (1, 1);

INSERT INTO syllabuses_subjects
VALUES (1, 1);





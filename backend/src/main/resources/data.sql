INSERT INTO users (username, password, role)
SELECT 'teacher1',
       '$2a$10$YOIHVxwifREwIgRNBmZZEenGb1L1OL/9sVJtiujAF1s0nwz2XUQym',
       'TEACHER'
WHERE NOT EXISTS (
  SELECT 1 FROM users WHERE username = 'teacher1'
);

INSERT INTO course (id, name, code)
SELECT 1, 'IVS', 'IVS2025'
WHERE NOT EXISTS (
    SELECT 1 FROM course WHERE id = 1
);

INSERT INTO course (id, name, code)
SELECT 2, 'Databases', 'DBI2025'
WHERE NOT EXISTS (
    SELECT 1 FROM course WHERE id = 2
);

INSERT INTO course (id, name, code)
SELECT 3, 'Web Development', 'WEB2025'
WHERE NOT EXISTS (
    SELECT 1 FROM course WHERE id = 3
);

INSERT INTO course (id, name, code)
SELECT 4, 'Software Engineering', 'SWE2025'
WHERE NOT EXISTS (
    SELECT 1 FROM course WHERE id = 4
);

INSERT INTO activity (id, name, type, course_id)
SELECT 100, 'Project 1', 'PROJECT', 1
WHERE NOT EXISTS (
    SELECT 1 FROM activity WHERE id = 100
);

INSERT INTO activity (id, name, type, course_id)
SELECT 101, 'Lectures', 'LECTURE', 1
WHERE NOT EXISTS (
    SELECT 1 FROM activity WHERE id = 101
);

INSERT INTO activity (id, name, type, course_id)
SELECT 102, 'Midterm Test', 'EXAM', 1
WHERE NOT EXISTS (
    SELECT 1 FROM activity WHERE id = 102
);

INSERT INTO activity (id, name, type, course_id)
SELECT 103, 'Database Design', 'PROJECT', 2
WHERE NOT EXISTS (
    SELECT 1 FROM activity WHERE id = 103
);

INSERT INTO activity (id, name, type, course_id)
SELECT 104, 'SQL Basics', 'LECTURE', 2
WHERE NOT EXISTS (
    SELECT 1 FROM activity WHERE id = 104
);

INSERT INTO activity (id, name, type, course_id)
SELECT 105, 'Frontend Project', 'PROJECT', 3
WHERE NOT EXISTS (
    SELECT 1 FROM activity WHERE id = 105
);

INSERT INTO activity (id, name, type, course_id)
SELECT 106, 'React Workshop', 'LECTURE', 3
WHERE NOT EXISTS (
    SELECT 1 FROM activity WHERE id = 106
);

INSERT INTO team (name, course_id)
SELECT 'Team A', 1
WHERE NOT EXISTS (
    SELECT 1 FROM team WHERE name = 'Team A' AND course_id = 1
);

INSERT INTO team (name, course_id)
SELECT 'Team B', 1
WHERE NOT EXISTS (
    SELECT 1 FROM team WHERE name = 'Team B' AND course_id = 1
);

INSERT INTO team (name, course_id)
SELECT 'Team C', 2
WHERE NOT EXISTS (
    SELECT 1 FROM team WHERE name = 'Team C' AND course_id = 2
);

INSERT INTO student (id, first_name, last_name, email)
SELECT 1, 'Alice', 'Smith', 'alice@fit.cvut.cz'
WHERE NOT EXISTS (
    SELECT 1 FROM student WHERE email = 'alice@fit.cvut.cz'
);

INSERT INTO student (id, first_name, last_name, email)
SELECT 2, 'Bob', 'Jones', 'bob@fit.cvut.cz'
WHERE NOT EXISTS (
    SELECT 1 FROM student WHERE email = 'bob@fit.cvut.cz'
);

INSERT INTO student (id, first_name, last_name, email)
SELECT 3, 'Carol', 'White', 'carol@fit.cvut.cz'
WHERE NOT EXISTS (
    SELECT 1 FROM student WHERE email = 'carol@fit.cvut.cz'
);

INSERT INTO student (id, first_name, last_name, email)
SELECT 4, 'David', 'Brown', 'david@fit.cvut.cz'
WHERE NOT EXISTS (
    SELECT 1 FROM student WHERE email = 'david@fit.cvut.cz'
);

INSERT INTO student (id, first_name, last_name, email)
SELECT 5, 'Eve', 'Davis', 'eve@fit.cvut.cz'
WHERE NOT EXISTS (
    SELECT 1 FROM student WHERE email = 'eve@fit.cvut.cz'
);

INSERT INTO student (id, first_name, last_name, email)
SELECT 6, 'Frank', 'Miller', 'frank@fit.cvut.cz'
WHERE NOT EXISTS (
    SELECT 1 FROM student WHERE email = 'frank@fit.cvut.cz'
);

INSERT INTO criterion
(id, category, name, checking_procedure,
 evaluation_method, max_points, points_redistribution, default_feedback)
SELECT
1000, 'Documentation', 'Structure', 'Check structure',
'PER_TEAM', 20, false, ''
WHERE NOT EXISTS (
    SELECT 1 FROM criterion WHERE id = 1000
);

INSERT INTO criterion
(id, category, name, checking_procedure,
 evaluation_method, max_points, points_redistribution, default_feedback)
SELECT
1001, 'Contribution', 'Activity', 'Check activity',
'PER_STUDENT', 10, false, ''
WHERE NOT EXISTS (
    SELECT 1 FROM criterion WHERE id = 1001
);

-- SOLVERS
INSERT INTO solver (id, name)
SELECT 1, 'Alice Smith'
WHERE NOT EXISTS (
    SELECT 1 FROM solver WHERE id = 1
);

INSERT INTO solver (id, name)
SELECT 2, 'Bob Jones'
WHERE NOT EXISTS (
    SELECT 1 FROM solver WHERE id = 2
);

INSERT INTO solver (id, name)
SELECT 3, 'Carol White'
WHERE NOT EXISTS (
    SELECT 1 FROM solver WHERE id = 3
);

INSERT INTO solver (id, name)
SELECT 4, 'David Brown'
WHERE NOT EXISTS (
    SELECT 1 FROM solver WHERE id = 4
);

-- TASKS (without explicit IDs - let H2 auto-generate them)
INSERT INTO task (title, description, type, task_order, start_date, end_date, activity_id, main_solver_id)
VALUES ('Project Setup', 'Initialize the project repository and development environment', 'PROJECT', 1, '2026-01-06', '2026-01-10', 100, 1);

INSERT INTO task (title, description, type, task_order, start_date, end_date, activity_id, main_solver_id)
VALUES ('Implementation Phase', 'Develop the core functionality of the project', 'PROJECT', 2, '2026-01-11', '2026-01-20', 100, 2);

INSERT INTO task (title, description, type, task_order, start_date, end_date, activity_id, main_solver_id)
VALUES ('Testing and Debugging', 'Write unit tests and fix any issues found', 'PROJECT', 3, '2026-01-21', '2026-01-25', 100, 1);

INSERT INTO task (title, description, type, task_order, start_date, end_date, activity_id, main_solver_id)
VALUES ('Introduction to IVS', 'Overview of IVS concepts and principles', 'LECTURE', 1, '2026-01-06', '2026-01-06', 101, 3);

INSERT INTO task (title, description, type, task_order, start_date, end_date, activity_id, main_solver_id)
VALUES ('Advanced Concepts', 'Deep dive into advanced IVS topics', 'LECTURE', 2, '2026-01-13', '2026-01-13', 101, 4);

INSERT INTO task (title, description, type, task_order, start_date, end_date, activity_id, main_solver_id)
VALUES ('Midterm Test', 'Comprehensive test covering all course material', 'EXAM', 1, '2026-01-27', '2026-01-27', 102, NULL);

INSERT INTO task (title, description, type, task_order, start_date, end_date, activity_id, main_solver_id)
VALUES ('Schema Design', 'Design the database schema for the project', 'PROJECT', 1, '2026-01-06', '2026-01-12', 103, 2);

INSERT INTO task (title, description, type, task_order, start_date, end_date, activity_id, main_solver_id)
VALUES ('SQL Implementation', 'Implement SQL scripts for the database', 'PROJECT', 2, '2026-01-13', '2026-01-18', 103, 3);

INSERT INTO task (title, description, type, task_order, start_date, end_date, activity_id, main_solver_id)
VALUES ('SQL Queries Practice', 'Write and optimize SQL queries', 'LECTURE', 1, '2026-01-15', '2026-01-19', 104, 4);
-- liquibase formatted sql

-- changeset e_rz:1
CREATE INDEX name_student_index ON student (name);

-- changeset e_rz:2
CREATE INDEX name_color_faculty_index ON faculty (name, color);
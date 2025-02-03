SELECT student.name, student.age, faculty.name
FROM student
INNER JOIN faculty ON student.faculty_id = faculty.id;

SELECT student.id, student.name, avatar.student_id
FROM student
RIGHT JOIN avatar ON avatar.student_id = student_id;

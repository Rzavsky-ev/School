package ru.hogwarts.school.controller;


import net.minidev.json.JSONObject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;


import java.util.ArrayList;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(StudentController.class)
public class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentServiceMock;

    @InjectMocks
    private StudentController studentController;

    @Test
    public void testFindStudent() throws Exception {
        Long id = 1L;
        String name = "Иван";
        int age = 25;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        when(studentServiceMock.findStudent(any(Long.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders.get("/student/" + student.getId()).
                        contentType(MediaType.APPLICATION_JSON).
                        accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.id").value(id)).
                andExpect(jsonPath("$.name").value(name)).
                andExpect(jsonPath("$.age").value(age));
    }

    @Test
    public void testFindByAgeBetween() throws Exception {
        Long id = 1L;
        String name = "Иван";
        int age = 22;
        int minAge = 20, maxAge = 25;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);
        List<Student> list = new ArrayList<>(List.of(student));

        when(studentServiceMock.findByAgeBetween(minAge, maxAge)).thenReturn(list);
        mockMvc.perform(MockMvcRequestBuilders.get("/student/find?minAge=" + minAge + "&maxAge=" +
                                maxAge).
                        contentType(MediaType.APPLICATION_JSON).
                        accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$[0].id").value(id)).
                andExpect(jsonPath("$[0].name").value(name)).
                andExpect(jsonPath("$[0].age").value(age));
    }

    @Test
    public void testGetStudentFaculty() throws Exception {
        Long id = 1L;
        String name = "ФЭМА";
        String color = "Красный";
        Faculty faculty = new Faculty(id, name, color);

        when(studentServiceMock.getStudentFaculty(any(Long.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders.get("/student/faculty/" + id).
                        contentType(MediaType.APPLICATION_JSON).
                        accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.id").value(id)).
                andExpect(jsonPath("$.name").value(name)).
                andExpect(jsonPath("$.color").value(color));

    }

    @Test
    public void testGetAll() throws Exception {
        Long id = 1L;
        String name = "Иван";
        int age = 22;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        List<Student> list = new ArrayList<>(List.of(student));

        when(studentServiceMock.getAll()).thenReturn(list);

        mockMvc.perform(MockMvcRequestBuilders.get("/student/all").
                        contentType(MediaType.APPLICATION_JSON).
                        accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$[0].id").value(id)).
                andExpect(jsonPath("$[0].name").value(name)).
                andExpect(jsonPath("$[0].age").value(age));
    }

    @Test
    public void testDownloadAvatar() throws Exception {
        Avatar avatar = new Avatar();
        Long id = 1L;
        byte[] arr = {1};
        String expectedMediaType = "image/jpeg";
        avatar.setData(arr);
        avatar.setMediaType(expectedMediaType);

        when(studentServiceMock.findAvatar(any(Long.class))).thenReturn(avatar);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/student/" + 1 + "/avatar/preview").
                        accept(MediaType.parseMediaType(expectedMediaType)))
                .andExpect(status().isOk())
                .andReturn();
        byte[] actualBytes = result.getResponse().getContentAsByteArray();
        Assertions.assertThat(actualBytes).isEqualTo(arr);
    }

    @Test
    public void testAddStudents() throws Exception {
        Long id = 1L;
        String name = "Иван";
        int age = 25;

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);
        studentObject.put("age", age);


        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);
        when(studentServiceMock.addStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders.post("/student").
                        content(studentObject.toString()).
                        contentType(MediaType.APPLICATION_JSON).
                        accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.id").value(id)).
                andExpect(jsonPath("$.name").value(name)).
                andExpect(jsonPath("$.age").value(age));

    }

    @Test
    public void testUploadAvatar() throws Exception {
        MockMultipartFile file = new MockMultipartFile("avatar", "test.png",
                "image/png", "Test data" .getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/student/1/avatar")
                        .file(file))
                .andExpect(status().isOk());
    }

    @Test
    public void testEditStudent() throws Exception {
        Long id = 1L;
        String name = "Иван";
        int age = 25;

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);
        studentObject.put("age", age);

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);
        when(studentServiceMock.editStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders.put("/student").
                        content(studentObject.toString()).
                        contentType(MediaType.APPLICATION_JSON).
                        accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.id").value(id)).
                andExpect(jsonPath("$.name").value(name)).
                andExpect(jsonPath("$.age").value(age));
        verify(studentServiceMock, times(1)).editStudent(any(Student.class));
    }

    @Test
    public void testRemoveStudent() throws Exception {
        Long id = 1L;
        String name = "Иван";
        int age = 20;
        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        when(studentServiceMock.removeStudent(student.getId())).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders.delete("/student/" + student.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(student.getId()))
                .andExpect(jsonPath("$.name").value(student.getName())).
                andExpect(jsonPath("$.age").value(student.getAge()));
        verify(studentServiceMock, times(1)).removeStudent(student.getId());
    }
}


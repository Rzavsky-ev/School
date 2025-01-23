package ru.hogwarts.school.controller;


import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(FacultyController.class)
public class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FacultyService facultyServiceMock;

    @InjectMocks
    private FacultyController facultyController;

    @Test
    public void testFindFaculty() throws Exception {
        Long id = 1L;
        String name = "ФЭМА";
        String color = "красный";
        Faculty faculty = new Faculty(id, name, color);

        when(facultyServiceMock.findFaculty(any(Long.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/" + faculty.getId()).
                        contentType(MediaType.APPLICATION_JSON).
                        accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.id").value(id)).
                andExpect(jsonPath("$.name").value(name)).
                andExpect(jsonPath("$.color").value(color));
    }

    @Test
    public void testFindByName() throws Exception {
        Long id = 1L;
        String name = "ФЭМА";
        String color = "красный";
        Faculty faculty = new Faculty(id, name, color);

        when(facultyServiceMock.findByName(any(String.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/name?name=" + faculty.getName()).
                        contentType(MediaType.APPLICATION_JSON).
                        accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.id").value(id)).
                andExpect(jsonPath("$.name").value(name)).
                andExpect(jsonPath("$.color").value(color));
    }

    @Test
    public void testGetAllStudentsFaculty() throws Exception {
        Long id = 1L;
        String name = "Иван";
        int age = 22;
        int minAge = 20, maxAge = 25;
        Long idFaculty = 1L;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);
        List<Student> list = new ArrayList<>(List.of(student));

        when(facultyServiceMock.getAllStudentsFaculty(any(Long.class))).thenReturn(list);
        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/allStudents/" + idFaculty).
                        contentType(MediaType.APPLICATION_JSON).
                        accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$[0].id").value(id)).
                andExpect(jsonPath("$[0].name").value(name)).
                andExpect(jsonPath("$[0].age").value(age));
    }

    @Test
    public void testGetAll() throws Exception {
        Long id = 1L;
        String name = "ФЭМА";
        String color = "красный";
        Faculty faculty = new Faculty(id, name, color);
        List<Faculty> list = new ArrayList<>(List.of(faculty));

        when(facultyServiceMock.getAll()).thenReturn(list);

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/all").
                        contentType(MediaType.APPLICATION_JSON).
                        accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$[0].id").value(id)).
                andExpect(jsonPath("$[0].name").value(name)).
                andExpect(jsonPath("$[0].color").value(color));
    }

    @Test
    public void testAddFaculty() throws Exception {
        Long id = 1L;
        String name = "ФЭМА";
        String color = "красный";
        Faculty faculty = new Faculty(id, name, color);

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);
        studentObject.put("color", color);

        when(facultyServiceMock.addFaculties(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders.post("/faculty").
                        content(studentObject.toString()).
                        contentType(MediaType.APPLICATION_JSON).
                        accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.id").value(id)).
                andExpect(jsonPath("$.name").value(name)).
                andExpect(jsonPath("$.color").value(color));
    }

    @Test
    public void testEditFaculty() throws Exception {
        Long id = 1L;
        String name = "ФЭМА";
        String color = "красный";
        Faculty faculty = new Faculty(id, name, color);

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);
        studentObject.put("color", color);

        when(facultyServiceMock.editFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders.put("/faculty").
                        content(studentObject.toString()).
                        contentType(MediaType.APPLICATION_JSON).
                        accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.id").value(id)).
                andExpect(jsonPath("$.name").value(name)).
                andExpect(jsonPath("$.color").value(color));
        verify(facultyServiceMock, times(1)).editFaculty(any(Faculty.class));
    }

    @Test
    public void testRemoveFaculty() throws Exception {
        Long id = 1L;
        String name = "ФЭМА";
        String color = "красный";
        Faculty faculty = new Faculty(id, name, color);

        when(facultyServiceMock.removeFaculty(faculty.getId())).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders.delete("/faculty/" + faculty.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName())).
                andExpect(jsonPath("$.color").value(faculty.getColor()));
        verify(facultyServiceMock, times(1)).removeFaculty(faculty.getId());
    }

}


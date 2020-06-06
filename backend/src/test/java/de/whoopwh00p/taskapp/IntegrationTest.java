package de.whoopwh00p.taskapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.whoopwh00p.taskapp.model.Task;
import de.whoopwh00p.taskapp.persistence.TaskRepository;
import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class IntegrationTest {

    @Inject
    @Client("/")
    private RxHttpClient client;

    @Inject
    private BeanContext beanContext;

    private TaskRepository taskRepository;

    private ObjectMapper mapper;

    @BeforeEach
    public void init() {
        mapper = new ObjectMapper();
        Task task1 = new Task(1,"TestTask1", false, "Test-Description 1");
        Task task2 = new Task(2,"TestTask2", true, "Test-Description 2");

        taskRepository = beanContext.getBean(TaskRepository.class);
        taskRepository.update(task1);
        taskRepository.update(task2);
    }

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void findAllTest() {
        HttpRequest<String> request = HttpRequest.GET("/tasks/findAll");
        String body = client.toBlocking().retrieve(request);
        assertNotNull(body);
        try {
            List<Task> resultList = Arrays.asList(mapper.readValue(body, Task[].class));
            assertEquals(2, resultList.size());

            Task expectedTask1 = new Task(1,"TestTask1", false, "Test-Description 1");
            Task expectedTask2 = new Task(2,"TestTask2", true, "Test-Description 2");

            assertTrue(resultList.containsAll(Arrays.asList(expectedTask1,expectedTask2)));
        } catch(JsonProcessingException e)
        {
            fail("Couldn't parse result");
        }
    }

    @Test
    public void findByNameTest() {
        HttpRequest<String> request = HttpRequest.GET("/tasks/findByName/TestTask1");
        String body = client.toBlocking().retrieve(request);
        assertNotNull(body);
        try {
            List<Task> resultList = Arrays.asList(mapper.readValue(body, Task[].class));
            assertEquals(1, resultList.size());

            Task expectedTask1 = new Task(1,"TestTask1", false, "Test-Description 1");

            assertTrue(resultList.contains(expectedTask1));
        } catch(JsonProcessingException e)
        {
            fail("Couldn't parse result");
        }
    }

    @Test
    public void findByNameUnknownTest() {
        HttpRequest<String> request = HttpRequest.GET("/tasks/findByName/uknown");
        String body = client.toBlocking().retrieve(request);
        assertNotNull(body);
        try {
            List<Task> resultList = Arrays.asList(mapper.readValue(body, Task[].class));
            assertTrue(resultList.isEmpty());
        } catch(JsonProcessingException e)
        {
            fail("Couldn't parse result");
        }
    }
}
package taskmanager.server.handlers.taskshandlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.server.HttpTaskServer;
import taskmanager.server.typeadapters.DurationAdapter;
import taskmanager.server.typeadapters.LocalDateTimeAdapter;
import taskmanager.servise.TaskManager;
import taskmanager.servise.impl.InMemoryTaskManager;
import taskmanager.tasktypes.Epic;
import taskmanager.tasktypes.Subtask;
import taskmanager.utility.Managers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicsHandlerTest {

    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .serializeNulls()
            .create();

    TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager(Managers.getDefaultHistoryManager());
        HttpTaskServer.start(taskManager);
    }

    @AfterEach
    public void shutDown() {
        HttpTaskServer.stop();
    }



    @Test
    public void shouldReturn200WhenEpicsGET() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");

        Epic epic = new Epic("epic1", "epicTesting1");
        taskManager.addEpic(epic);

        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(taskManager.getEpics()), response.body());
    }

    @Test
    public void shouldReturn415WhenIdIsNotIntegerGET() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/a");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(415, response.statusCode());
        assertEquals("Epic id should be integer", response.body());
    }

    @Test
    public void shouldReturn404WhenEpicNotFoundGET() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
        assertEquals("Epic with id=1 not found", response.body());
    }

    @Test
    public void shouldReturn200WhenEpicFoundGET() throws IOException, InterruptedException {
        Epic epic = new Epic("epic1", "epicTesting1");
        taskManager.addEpic(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epic.getId());

        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(epic), response.body());
    }

    @Test
    public void shouldReturn200WhenEpicSubtasksGET() throws IOException, InterruptedException {
        Epic epic = new Epic("epic1", "epicTesting1");
        taskManager.addEpic(epic);

        Subtask subtask = new Subtask("subtask1", "subtaskTesting1", epic.getId());
        subtask.setStartTime(LocalDateTime.now());
        subtask.setDuration(Duration.ZERO);
        taskManager.addSubtask(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epic.getId() + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(epic.getSubtasksIds()), response.body());
    }

    @Test
    public void shouldReturn400WhenInvalidRequestGET() throws IOException, InterruptedException {
        Epic epic = new Epic("epic1", "epicTesting1");
        taskManager.addEpic(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epic.getId() + "/a");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
        assertEquals("Invalid request", response.body());
    }



    @Test
    public void shouldReturn201WhenEpicAddedPOST() throws IOException, InterruptedException {
        Epic epic = new Epic("epic1", "epicTesting1");
        String epicJson = gson.toJson(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals("Epic added", response.body());
    }

    @Test
    public void shouldReturn400WhenInvalidRequestPOST() throws IOException, InterruptedException {
        Epic epic = new Epic("epic1", "epicTesting1");
        String epicJson = gson.toJson(List.of(epic));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
        assertEquals("Invalid request", response.body());
    }



    @Test
    public void shouldReturn400WhenInvalidRequestDELETE() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
        assertEquals("Invalid request", response.body());
    }

    @Test
    public void shouldReturn415WhenIdIsNotIntegerDELETE() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/a");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(415, response.statusCode());
        assertEquals("Epic id should be integer", response.body());
    }

    @Test
    public void shouldReturn404WhenEpicNotFoundDELETE() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
        assertEquals("Epic with id=1 not found", response.body());
    }

    @Test
    public void shouldReturn200WhenEpicDELETE() throws IOException, InterruptedException {
        Epic epic = new Epic("epic1", "epicTesting1");
        taskManager.addEpic(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epic.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(epic), response.body());
    }
}
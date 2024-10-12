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
import taskmanager.tasktypes.Task;
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

class TasksHandlerTest {

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
    public void shouldReturn405ForNotAllowedMethod() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(HttpRequest.BodyPublishers.ofString("")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(405, response.statusCode());
    }



    @Test
    public void shouldReturn200WhenAllTasksRequiredGET() throws IOException, InterruptedException {
        Task task = new Task("task1", "taskTesting1");
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ZERO);
        Task task2 = new Task("task2", "taskTesting2");
        task2.setStartTime(LocalDateTime.now().plusHours(1));
        task2.setDuration(Duration.ZERO);
        taskManager.addTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(List.of(task)), response.body());
    }

    @Test
    public void shouldReturn200WhenTaskFoundGET() throws IOException, InterruptedException {
        Task task = new Task("task1", "taskTesting1");
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ZERO);
        taskManager.addTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + task.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(task), response.body());
    }

    @Test
    public void shouldReturn404WhenTaskNotFoundGET() throws IOException, InterruptedException {
        Task task = new Task("task1", "taskTesting1");
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ZERO);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/0");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
        assertEquals("Task with id=0 not found", response.body());
    }

    @Test
    public void shouldReturn415WhenTaskIdNotIntegerGET() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/a");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(415, response.statusCode());
        assertEquals("Task id should be integer", response.body());
    }

    @Test
    public void shouldReturn400WhenInvalidRequestGET() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1/b");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
        assertEquals("Invalid request", response.body());
    }



    @Test
    public void shouldReturn400WhenInvalidRequestPOST() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString("taskJson")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
        assertEquals("Invalid request", response.body());
    }

    @Test
    public void shouldReturn406WhenTaskStatusIsNullPOST() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        Task task = new Task("task1", "taskTesting1");
        task.setStatus(null);
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ZERO);
        String taskJson = gson.toJson(task);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode());
        assertEquals("Invalid task", response.body());
    }

    @Test
    public void shouldReturn406WhenTaskTypeNotMatchPOST() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        String taskJson = "{\n" +
                "  \"name\": \"task3\",\n" +
                "  \"description\": \"taskTesting3\",\n" +
                "  \"status\": \"DONE\",\n" +
                "  \"type\": \"ABCD\",\n" +
                "  \"startTime\": \"0000-11-07T22:00:00\",\n" +
                "  \"duration\": \"PT1H\"\n" +
                "}";
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode());
        assertEquals("Invalid task", response.body());
    }

    @Test
    public void shouldReturn201WhenTaskAddedPOST() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        Task task = new Task("task1", "taskTesting1");
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ZERO);
        String taskJson = gson.toJson(task);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals("Task added", response.body());
    }

    @Test
    public void shouldReturn201WhenTaskUpdatedPOST() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");

        Task task = new Task("task1", "taskTesting1");
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ZERO);
        taskManager.addTask(task);

        Task updatedTask = new Task("task2", "taskTesting2");
        updatedTask.setId(taskManager.getTasks().get(0).getId());
        updatedTask.setStartTime(task.getStartTime().get());
        updatedTask.setDuration(task.getDuration().get());

        String updatedTaskJson = gson.toJson(updatedTask);

        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(updatedTaskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals("Task updated", response.body());
    }

    @Test
    public void shouldReturn404WhenTaskNotFoundPOST() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");

        Task task = new Task("task1", "taskTesting1");
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ZERO);
        taskManager.addTask(task);

        Task updatedTask = new Task("updatedTask1", "taskTesting1");
        updatedTask.setId(-1);
        updatedTask.setStartTime(LocalDateTime.now());
        updatedTask.setDuration(Duration.ZERO);

        String updatedTaskJson = gson.toJson(updatedTask);

        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(updatedTaskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
        assertEquals("Task with id=-1 not found", response.body());
    }



    @Test
    public void shouldReturn400WhenInvalidRequestDELETE() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1/b");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
        assertEquals("Invalid request", response.body());
    }

    @Test
    public void shouldReturn415WhenIdIsNotIntegerDELETE() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/b");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(415, response.statusCode());
        assertEquals("Task id should be integer", response.body());
    }

    @Test
    public void shouldReturn404WhenTaskNotFoundDELETE() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
        assertEquals("Task with id=1 not found", response.body());
    }

    @Test
    public void shouldReturn200WhenTaskDeletedDELETE() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Task task = new Task("task1", "taskTesting1");
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ZERO);
        taskManager.addTask(task);

        URI urlToDelete = URI.create("http://localhost:8080/tasks/" + task.getId());

        HttpRequest request = HttpRequest.newBuilder().uri(urlToDelete).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(task), response.body());
    }
}
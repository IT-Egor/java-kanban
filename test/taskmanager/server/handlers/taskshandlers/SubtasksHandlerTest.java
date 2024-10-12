package taskmanager.server.handlers.taskshandlers;

import org.junit.jupiter.api.Test;
import taskmanager.server.handlers.AbstractTaskManagerHandlerTest;
import taskmanager.tasktypes.Epic;
import taskmanager.tasktypes.Subtask;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubtasksHandlerTest extends AbstractTaskManagerHandlerTest {

    @Test
    public void shouldReturn400WhenInvalidRequestGET() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/a/b/c");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
        assertEquals("Invalid request", response.body());
    }

    @Test
    public void shouldReturn200WhenSubtasksGET() throws IOException, InterruptedException {
        Epic epic = new Epic("epic1", "epicTesting1");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting1", epic.getId());
        subtask.setStartTime(LocalDateTime.now());
        subtask.setDuration(Duration.ZERO);
        taskManager.addSubtask(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(List.of(subtask)), response.body());
    }

    @Test
    public void shouldReturn404WhenSubtaskNotFoundGET() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
        assertEquals("Subtask with id=1 not found", response.body());
    }

    @Test
    public void shouldReturn200WhenSubtaskFoundGET() throws IOException, InterruptedException {
        Epic epic = new Epic("epic1", "epicTesting1");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting1", epic.getId());
        subtask.setStartTime(LocalDateTime.now());
        subtask.setDuration(Duration.ZERO);
        taskManager.addSubtask(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subtask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(subtask), response.body());
    }

    @Test
    public void shouldReturn415WhenIdIsNotIntegerGET() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/a");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(415, response.statusCode());
        assertEquals("Subtask id should be integer", response.body());
    }



    @Test
    public void shouldReturn406WhenSubtaskIsHaveZeroEpicIdPOST() throws IOException, InterruptedException {
        Subtask subtask = new Subtask("subtask1", "subtaskTesting1", 0);
        String subtaskJson = gson.toJson(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode());
        assertEquals("Invalid subtask", response.body());
    }

    @Test
    public void shouldReturn406WhenSubtaskIsHaveNullStatusPOST() throws IOException, InterruptedException {
        Subtask subtask = new Subtask("subtask1", "subtaskTesting1", 1);
        subtask.setStatus(null);
        String subtaskJson = gson.toJson(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode());
        assertEquals("Invalid subtask", response.body());
    }

    @Test
    public void shouldReturn406WhenSubtaskIsHaveTypeMismatchPOST() throws IOException, InterruptedException {
        String subtaskJson = "{\n" +
                "  \"name\": \"subtask4\",\n" +
                "  \"description\": \"subtaskTesting4\",\n" +
                "  \"status\": \"DONE\",\n" +
                "\t\"containingEpicId\": 8,\n" +
                "  \"type\": \"ABCD\",\n" +
                "  \"startTime\": \"2024-12-07T22:00:00\",\n" +
                "  \"duration\": \"PT1H\"\n" +
                "}";

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode());
        assertEquals("Invalid subtask", response.body());
    }

    @Test
    public void shouldReturn404WhenEpicNotFoundWhenAddingSubtaskPOST() throws IOException, InterruptedException {
        Subtask subtask = new Subtask("subtask1", "subtaskTesting1", 1);
        subtask.setStartTime(LocalDateTime.now());
        subtask.setDuration(Duration.ZERO);
        String subtaskJson = gson.toJson(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
        assertEquals("Epic with id=1 not found", response.body());
    }

    @Test
    public void shouldReturn200WhenAddingSubtaskPOST() throws IOException, InterruptedException {
        Epic epic = new Epic("epic1", "epicTesting1");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting1", epic.getId());
        subtask.setStartTime(LocalDateTime.now());
        subtask.setDuration(Duration.ZERO);
        String subtaskJson = gson.toJson(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals("Subtask added", response.body());
    }

    @Test
    public void shouldReturn200WhenUpdatingSubtaskPOST() throws IOException, InterruptedException {
        Epic epic = new Epic("epic1", "epicTesting1");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting1", epic.getId());
        subtask.setStartTime(LocalDateTime.now());
        subtask.setDuration(Duration.ZERO);
        taskManager.addSubtask(subtask);

        Subtask updatedSubtask = new Subtask("updatedSubtask1", "updatedSubtaskTesting1", epic.getId());
        updatedSubtask.setId(subtask.getId());
        updatedSubtask.setStartTime(LocalDateTime.now());
        updatedSubtask.setDuration(Duration.ZERO);
        String updatedSubtaskJson = gson.toJson(updatedSubtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(updatedSubtaskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals("Subtask updated", response.body());
    }

    @Test
    public void shouldReturn404WhenUpdatingSubtaskAndEpicNotFoundPOST() throws IOException, InterruptedException {
        Epic epic = new Epic("epic1", "epicTesting1");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting1", epic.getId());
        subtask.setStartTime(LocalDateTime.now());
        subtask.setDuration(Duration.ZERO);
        taskManager.addSubtask(subtask);

        Subtask updatedSubtask = new Subtask("updatedSubtask1", "updatedSubtaskTesting1", -1);
        updatedSubtask.setId(subtask.getId());
        updatedSubtask.setStartTime(LocalDateTime.now());
        updatedSubtask.setDuration(Duration.ZERO);
        String updatedSubtaskJson = gson.toJson(updatedSubtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(updatedSubtaskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
        assertEquals("Epic with id=-1 not found", response.body());
    }

    @Test
    public void shouldReturn404WhenUpdatingSubtaskAndSubtaskNotFoundPOST() throws IOException, InterruptedException {
        Epic epic = new Epic("epic1", "epicTesting1");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting1", epic.getId());
        subtask.setStartTime(LocalDateTime.now());
        subtask.setDuration(Duration.ZERO);
        taskManager.addSubtask(subtask);

        Subtask updatedSubtask = new Subtask("updatedSubtask1", "updatedSubtaskTesting1", epic.getId());
        updatedSubtask.setId(-1);
        updatedSubtask.setStartTime(LocalDateTime.now());
        updatedSubtask.setDuration(Duration.ZERO);
        String updatedSubtaskJson = gson.toJson(updatedSubtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(updatedSubtaskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
        assertEquals("Subtask with id=-1 not found", response.body());
    }

    @Test
    public void shouldReturn400WhenInvalidRequestPOST() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/a");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString("")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
        assertEquals("Invalid request", response.body());
    }



    @Test
    public void shouldReturn400WhenInvalidRequestDELETE() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/1/a");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
        assertEquals("Invalid request", response.body());
    }

    @Test
    public void shouldReturn200WhenSubtaskDELETE() throws IOException, InterruptedException {
        Epic epic = new Epic("epic1", "epicTesting1");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask1", "subtaskTesting1", epic.getId());
        subtask.setStartTime(LocalDateTime.now());
        subtask.setDuration(Duration.ZERO);
        taskManager.addSubtask(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subtask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(subtask), response.body());
    }

    @Test
    public void shouldReturn404WhenSubtaskNotFoundDELETE() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
        assertEquals("Subtask with id=1 not found", response.body());
    }

    @Test
    public void shouldReturn415WhenIdIsNotIntegerDELETE() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/a");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(415, response.statusCode());
        assertEquals("Subtask id should be integer", response.body());
    }
}
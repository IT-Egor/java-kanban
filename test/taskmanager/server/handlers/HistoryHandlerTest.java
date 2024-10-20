package taskmanager.server.handlers;

import org.junit.jupiter.api.Test;
import taskmanager.tasktypes.Epic;
import taskmanager.tasktypes.Subtask;
import taskmanager.tasktypes.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HistoryHandlerTest extends AbstractTaskManagerHandlerTest {

    @Test
    public void shouldReturnHistory() throws IOException, InterruptedException {
        Task task = new Task("task1", "taskTesting");
        task.setStartTime(LocalDateTime.of(1,1,1, 0,0,0));
        task.setDuration(Duration.ofMinutes(1));
        Task task2 = new Task("task2", "taskTesting2");
        task2.setStartTime(LocalDateTime.of(2,1,1, 0,0,0));
        task2.setDuration(Duration.ofMinutes(2));
        taskManager.addTask(task);
        taskManager.addTask(task2);
        taskManager.findTask(task.getId());

        Epic epic = new Epic("epic1", "epicTesting");
        Epic epic2 = new Epic("epic2", "epicTesting2");
        taskManager.addEpic(epic);
        taskManager.addEpic(epic2);
        taskManager.findEpic(epic.getId());

        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        subtask.setStartTime(LocalDateTime.of(3,1,1, 0,0,0));
        subtask.setDuration(Duration.ofMinutes(3));
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic2.getId());
        subtask2.setStartTime(LocalDateTime.of(4,1,1, 0,0,0));
        subtask2.setDuration(Duration.ofMinutes(4));

        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);
        taskManager.findSubtask(subtask.getId());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(taskManager.getHistory()), response.body());
    }

    @Test
    public void shouldReturn405WhenUsingUnsupportedMethod() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(HttpRequest.BodyPublishers.noBody()).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(405, response.statusCode());
        assertEquals("Method not allowed", response.body());
    }
}
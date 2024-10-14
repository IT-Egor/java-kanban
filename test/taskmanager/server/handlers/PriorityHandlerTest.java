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

class PriorityHandlerTest extends AbstractTaskManagerHandlerTest {

    @Test
    public void shouldReturnPrioritizedTasks() throws IOException, InterruptedException {
        Epic epic = new Epic("epic1", "epicTesting");
        taskManager.addEpic(epic);
        LocalDateTime start = LocalDateTime.now();

        Subtask subtask = new Subtask("subtask1", "subtaskTesting", epic.getId());
        subtask.setStartTime(start);
        subtask.setDuration(Duration.ofMinutes(1));
        Subtask subtask2 = new Subtask("subtask2", "subtaskTesting2", epic.getId());
        subtask2.setStartTime(start.plusHours(1));
        subtask2.setDuration(Duration.ofMinutes(2));
        Subtask subtask3 = new Subtask("subtask3", "subtaskTesting3", epic.getId());
        subtask3.setStartTime(start.plusHours(2));
        subtask3.setDuration(Duration.ofMinutes(3));
        Subtask subtask4 = new Subtask("subtask4", "subtaskTesting4", epic.getId());
        subtask4.setStartTime(start.plusHours(3));
        subtask4.setDuration(Duration.ofMinutes(4));

        Task task = new Task("task1", "taskTesting");
        task.setStartTime(start.plusHours(4));
        task.setDuration(Duration.ofMinutes(6));

        taskManager.addTask(task);
        taskManager.addSubtask(subtask4);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        taskManager.addSubtask(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(taskManager.getPrioritizedTasks()), response.body());
    }

    @Test
    public void shouldReturn405WhenUsingUnsupportedMethod() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(HttpRequest.BodyPublishers.noBody()).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(405, response.statusCode());
        assertEquals("Method not allowed", response.body());
    }
}
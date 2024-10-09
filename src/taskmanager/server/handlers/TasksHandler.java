package taskmanager.server.handlers;

import com.google.gson.*;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanager.exceptions.TaskValidationException;
import taskmanager.server.typeadapters.DurationAdapter;
import taskmanager.server.typeadapters.LocalDateTimeAdapter;
import taskmanager.servise.TaskManager;
import taskmanager.tasktypes.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class TasksHandler implements HttpHandler {
    private TaskManager taskManager;
    private Gson gson;

    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        String requestMethod = exchange.getRequestMethod();

        switch (requestMethod) {
            case "GET":
                handleGetRequest(exchange, path);
                break;

            case "POST":
                handlePostRequest(exchange);
                break;

            default:
                int statusCode = 405;
                String response = "Method not allowed";
                sendResponse(exchange, statusCode, response);
        }
    }

    private void handleGetRequest(HttpExchange exchange, String path) throws IOException {
        String response;
        int statusCode;
        String[] pathElements = path.split("/");

        if (pathElements.length == 2) {
            response = gson.toJson(taskManager.getTasks());
            statusCode = 200;
        } else if (pathElements.length == 3) {
            try {
                Task task = taskManager.findTask(Integer.parseInt(pathElements[2]));
                if (task == null) {
                    statusCode = 404;
                    response = String.format("Task with id=%s not found", pathElements[2]);
                } else {
                    response = gson.toJson(task);
                    statusCode = 200;
                }
            } catch (NumberFormatException e) {
                statusCode = 415;
                response = "Task id should be integer";
            }
        } else {
            statusCode = 400;
            response = "Invalid request";
        }
        sendResponse(exchange, statusCode, response);
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        String response;
        int statusCode;
        try {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            JsonElement jsonElement = JsonParser.parseString(body);
            if (jsonElement.isJsonObject()) {
                Task task = gson.fromJson(jsonElement, Task.class);
                System.out.println("task = " + task);
                try {
                    if (task.getId() == 0) {
                        taskManager.addTask(task);
                        statusCode = 201;
                        response = "Task added";
                    } else {
                        int updateTaskCode = taskManager.updateTask(task);
                        if (updateTaskCode > 0) {
                            statusCode = 201;
                            response = "Task updated";
                        } else if (updateTaskCode == -1) {
                            statusCode = 404;
                            response = String.format("Task with id=%s not found", task.getId());
                        } else {
                            statusCode = 406;
                            response = "Invalid task";
                        }
                    }
                } catch (TaskValidationException e) {
                    response = e.getMessage();
                    statusCode = 406;
                }

            } else {
                statusCode = 400;
                response = "Invalid request";
            }
        } catch (JsonSyntaxException e) {
            statusCode = 400;
            response = "Invalid request";
        }
        sendResponse(exchange, statusCode, response);
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {

//        Headers headers = exchange.getResponseHeaders();
//        headers.set("Content-Type", "application/json");

        exchange.sendResponseHeaders(statusCode, response.length());
        // System.out.println("Response: " + response);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}

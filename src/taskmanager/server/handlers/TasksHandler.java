package taskmanager.server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanager.server.typeadapters.DurationAdapter;
import taskmanager.server.typeadapters.LocalDateTimeAdapter;
import taskmanager.servise.TaskManager;
import taskmanager.tasktypes.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
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
        String response;
        int statusCode;

        switch (requestMethod) {
            case "GET":
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
                break;

            default:
                statusCode = 405;
                response = "Method not allowed";

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

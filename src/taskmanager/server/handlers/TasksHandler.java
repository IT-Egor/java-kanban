package taskmanager.server.handlers;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import taskmanager.exceptions.TaskValidationException;
import taskmanager.servise.TaskManager;
import taskmanager.tasktypes.Task;
import taskmanager.utility.Type;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class TasksHandler extends AbstractTaskManagerHandler {

    public TasksHandler(TaskManager taskManager) {
        super(taskManager);
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

            case "DELETE":
                handleDeleteRequest(exchange, path);
                break;

            default:
                int statusCode = 405;
                String response = "Method not allowed";
                sendResponse(exchange, statusCode, response);
        }
    }

    protected void handleGetRequest(HttpExchange exchange, String path) throws IOException {
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

    protected void handlePostRequest(HttpExchange exchange) throws IOException {
        String response;
        int statusCode;
        try {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            JsonElement jsonElement = JsonParser.parseString(body);
            if (jsonElement.isJsonObject()) {
                Task task = gson.fromJson(jsonElement, Task.class);
                try {
                    if (task.getStatus() == null || task.getType() != Type.TASK) {
                        throw new TaskValidationException("Invalid task");
                    }
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

    protected void handleDeleteRequest(HttpExchange exchange, String path) throws IOException {
        String response;
        int statusCode;
        String[] pathElements = path.split("/");

        if (pathElements.length == 3) {
            try {
                int taskId = Integer.parseInt(pathElements[2]);
                Task deletedTask = taskManager.removeTask(taskId);
                if (deletedTask != null) {
                    statusCode = 200;
                    response = gson.toJson(deletedTask);
                } else {
                    statusCode = 404;
                    response = String.format("Task with id=%s not found", taskId);
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
}

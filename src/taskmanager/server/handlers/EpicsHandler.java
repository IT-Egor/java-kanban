package taskmanager.server.handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import taskmanager.exceptions.TaskValidationException;
import taskmanager.servise.TaskManager;
import taskmanager.tasktypes.Epic;
import taskmanager.tasktypes.Task;
import taskmanager.utility.Type;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class EpicsHandler extends TasksHandler {
    public EpicsHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void handleGetRequest(HttpExchange exchange, String path) throws IOException {
        String response;
        int statusCode;
        String[] pathElements = path.split("/");

        if (pathElements.length == 2) {
            response = gson.toJson(taskManager.getEpics());
            statusCode = 200;
        } else if (pathElements.length > 2) {
            try {
                int epicId = Integer.parseInt(pathElements[2]);
                Epic epic = taskManager.findEpic(epicId);
                System.out.println(epic);
                if (epic == null) {
                    statusCode = 404;
                    response = String.format("Epic with id=%s not found", epicId);
                } else {
                    if (pathElements.length == 3) {
                        response = gson.toJson(epic);
                        statusCode = 200;
                    } else if (pathElements.length == 4 && pathElements[3].equals("subtasks")) {
                        response = gson.toJson(epic.getSubtasksIds());
                        statusCode = 200;
                    } else {
                        statusCode = 400;
                        response = "Invalid request";
                    }
                }
            } catch (NumberFormatException e) {
                statusCode = 415;
                response = "Epic id should be integer";
            }
        } else {
            statusCode = 400;
            response = "Invalid request";
        }
        sendResponse(exchange, statusCode, response);
    }

    @Override
    protected void handlePostRequest(HttpExchange exchange) throws IOException {
        String response;
        int statusCode;

        try {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            JsonElement jsonElement = JsonParser.parseString(body);
            if (jsonElement.isJsonObject()) {
                Epic epic = gson.fromJson(jsonElement, Epic.class);
                try {
                    throwIfEpicIsInvalid(epic);
                    taskManager.addEpic(epic);
                    statusCode = 201;
                    response = "Epic added";

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

    private void throwIfEpicIsInvalid(Epic epic) {
        if (epic.getStatus() != null
                || epic.getType() != Type.EPIC
                || epic.getId() != 0
                || epic.getStartTime().isPresent()
                || epic.getDuration().isPresent()
                || epic.getEndTime().isPresent()) {
            throw new TaskValidationException("Invalid epic");
        }
    }
}

package taskmanager.server.handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import taskmanager.exceptions.TaskValidationException;
import taskmanager.servise.TaskManager;
import taskmanager.tasktypes.Subtask;
import taskmanager.utility.Type;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SubtasksHandler extends TasksHandler {
    public SubtasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void handleGetRequest(HttpExchange exchange, String path) throws IOException {
        String response;
        int statusCode;
        String[] pathElements = path.split("/");

        if (pathElements.length == 2) {
            response = gson.toJson(taskManager.getSubtasks());
            statusCode = 200;
        } else if (pathElements.length == 3) {
            try {
                Subtask subtask = taskManager.findSubtask(Integer.parseInt(pathElements[2]));
                if (subtask == null) {
                    statusCode = 404;
                    response = String.format("Subtask with id=%s not found", pathElements[2]);
                } else {
                    response = gson.toJson(subtask);
                    statusCode = 200;
                }
            } catch (NumberFormatException e) {
                statusCode = 415;
                response = "Subtask id should be integer";
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
                Subtask subtask = gson.fromJson(jsonElement, Subtask.class);
                try {
                    throwIfSubtaskNotValid(subtask);
                    if (subtask.getId() == 0) {
                        int subtaskAddStatus = taskManager.addSubtask(subtask);
                        if (subtaskAddStatus == -1) {
                            statusCode = 404;
                            response = String.format("Epic with id=%s not found", subtask.getContainingEpicId());
                        } else {
                            statusCode = 201;
                            response = "Subtask added";
                        }
                    } else {
                        int updateSubtaskCode = taskManager.updateTask(subtask);
                        if (updateSubtaskCode > 0) {
                            statusCode = 201;
                            response = "Subtask updated";
                        } else if (updateSubtaskCode == -1) {
                            statusCode = 404;
                            response = String.format("Subtask with id=%s not found", subtask.getId());
                        } else if (updateSubtaskCode == -2) {
                            statusCode = 404;
                            response = String.format("Epic with id=%s not found", subtask.getContainingEpicId());
                        } else {
                            statusCode = 406;
                            response = "Invalid subtask";
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

    @Override
    protected void handleDeleteRequest(HttpExchange exchange, String path) throws IOException {
        String response;
        int statusCode;
        String[] pathElements = path.split("/");

        if (pathElements.length == 3) {
            try {
                int subtaskId = Integer.parseInt(pathElements[2]);
                Subtask deletedSubtask = taskManager.removeSubtask(subtaskId);
                if (deletedSubtask != null) {
                    statusCode = 200;
                    response = gson.toJson(deletedSubtask);
                } else {
                    statusCode = 404;
                    response = String.format("Subtask with id=%s not found", subtaskId);
                }
            } catch (NumberFormatException e) {
                statusCode = 415;
                response = "Subtask id should be integer";
            }
        } else {
            statusCode = 400;
            response = "Invalid request";
        }
        sendResponse(exchange, statusCode, response);
    }

    private void throwIfSubtaskNotValid(Subtask subtask) {
        if (subtask.getType() != Type.SUBTASK || subtask.getStatus() == null || subtask.getContainingEpicId() == 0) {
            throw new TaskValidationException("Invalid subtask");
        }
    }
}

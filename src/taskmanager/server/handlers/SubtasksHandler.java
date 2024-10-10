package taskmanager.server.handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import taskmanager.exceptions.TaskValidationException;
import taskmanager.servise.TaskManager;
import taskmanager.tasktypes.Subtask;
import taskmanager.tasktypes.Task;

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
}

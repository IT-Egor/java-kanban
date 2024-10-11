package taskmanager.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import taskmanager.servise.TaskManager;
import taskmanager.tasktypes.Epic;

import java.io.IOException;

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
}

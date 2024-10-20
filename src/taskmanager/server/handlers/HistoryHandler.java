package taskmanager.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import taskmanager.servise.TaskManager;
import taskmanager.tasktypes.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends AbstractTaskManagerHandler {
    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response;
        int statusCode;

        if (exchange.getRequestMethod().equals("GET")) {
            List<Task> history = taskManager.getHistory();
            response = gson.toJson(history);
            statusCode = 200;
        } else {
            statusCode = 405;
            response = "Method not allowed";
        }
        sendResponse(exchange, statusCode, response);
    }
}

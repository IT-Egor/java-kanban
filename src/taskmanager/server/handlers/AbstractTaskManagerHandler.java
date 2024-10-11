package taskmanager.server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanager.server.typeadapters.DurationAdapter;
import taskmanager.server.typeadapters.LocalDateTimeAdapter;
import taskmanager.servise.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class AbstractTaskManagerHandler implements HttpHandler {
    protected TaskManager taskManager;
    protected Gson gson;

    protected AbstractTaskManagerHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .serializeNulls()
                .create();
    }

    protected void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.length());

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}

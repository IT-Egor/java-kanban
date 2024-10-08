package taskmanager.server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanager.server.typeadapters.DurationAdapter;
import taskmanager.server.typeadapters.LocalDateTimeAdapter;
import taskmanager.servise.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;
import java.time.LocalDateTime;

public class TasksHandler implements HttpHandler {
    private TaskManager taskManager;

    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();

        String response = gson.toJson(taskManager.getTasks());

//        Headers headers = exchange.getResponseHeaders();
//        headers.set("Content-Type", "application/json");

        exchange.sendResponseHeaders(200, response.length());
        System.out.println("Response: " + response);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}

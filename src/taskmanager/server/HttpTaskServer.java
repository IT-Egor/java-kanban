package taskmanager.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import taskmanager.server.handlers.EpicsHandler;
import taskmanager.server.handlers.PriorityHandler;
import taskmanager.server.handlers.SubtasksHandler;
import taskmanager.server.handlers.TasksHandler;

import taskmanager.servise.TaskManager;
import taskmanager.servise.impl.FileBackedTaskManager;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private static final File CSV_FILE = new File( "src/taskmanager/resources/serverData.csv");

    public static void main(String[] args) {

        TaskManager taskManager = FileBackedTaskManager.loadFromFile(CSV_FILE);

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
            server.createContext("/start", new TestHandler());
            server.createContext("/tasks", new TasksHandler(taskManager));
            server.createContext("/subtasks", new SubtasksHandler(taskManager));
            server.createContext("/epics", new EpicsHandler(taskManager));
            server.createContext("/prioritized", new PriorityHandler(taskManager));
            server.start();
            System.out.println("Started on port " + PORT);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}


class TestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "start";
        exchange.sendResponseHeaders(200, 0);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
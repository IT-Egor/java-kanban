package taskmanager.server;

import com.sun.net.httpserver.HttpServer;
import taskmanager.server.handlers.*;

import taskmanager.server.handlers.taskshandlers.EpicsHandler;
import taskmanager.server.handlers.taskshandlers.SubtasksHandler;
import taskmanager.server.handlers.taskshandlers.TasksHandler;
import taskmanager.servise.TaskManager;
import taskmanager.servise.impl.FileBackedTaskManager;
import taskmanager.servise.impl.InMemoryTaskManager;
import taskmanager.utility.Managers;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private static final File CSV_FILE = new File("src/taskmanager/resources/serverData.csv");
    private static final boolean FILE_BACKED = false;
    private static HttpServer server;

    public static void main(String[] args) {
        if (FILE_BACKED) {
            start(FileBackedTaskManager.loadFromFile(CSV_FILE));
        } else {
            start(new InMemoryTaskManager(Managers.getDefaultHistoryManager()));
        }
        System.out.println("Started on port " + PORT);
    }

    public static void start(TaskManager taskManager) {
        try {
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
            server.createContext("/tasks", new TasksHandler(taskManager));
            server.createContext("/subtasks", new SubtasksHandler(taskManager));
            server.createContext("/epics", new EpicsHandler(taskManager));
            server.createContext("/history", new HistoryHandler(taskManager));
            server.createContext("/prioritized", new PriorityHandler(taskManager));
            server.start();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void stop() {
        server.stop(0);
    }
}
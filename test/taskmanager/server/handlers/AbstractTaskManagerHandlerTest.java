package taskmanager.server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import taskmanager.server.HttpTaskServer;
import taskmanager.server.typeadapters.DurationAdapter;
import taskmanager.server.typeadapters.LocalDateTimeAdapter;
import taskmanager.servise.TaskManager;
import taskmanager.servise.impl.InMemoryTaskManager;
import taskmanager.utility.Managers;

import java.time.Duration;
import java.time.LocalDateTime;

public abstract class AbstractTaskManagerHandlerTest {
    protected Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .serializeNulls()
            .create();

    protected TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager(Managers.getDefaultHistoryManager());
        HttpTaskServer.start(taskManager);
    }

    @AfterEach
    public void shutDown() {
        HttpTaskServer.stop();
    }
}

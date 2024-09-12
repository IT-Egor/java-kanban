package taskmanager.servise.impl;

import taskmanager.exceptions.ManagerSaveException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;

public class FileBackedTaskManager {
    private Path fileToSavePath;

    public FileBackedTaskManager(Path path) {
        fileToSavePath = path;
    }

    public void save() {
        try (Writer writer = new FileWriter(fileToSavePath.toFile())) {
            writer.write("test");
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл");
        }
    }
}

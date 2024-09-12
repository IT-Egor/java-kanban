package taskmanager.servise.impl;

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
            e.printStackTrace();
        }
    }
}

package taskmanager.servise.impl;

import taskmanager.exceptions.ManagerSaveException;
import taskmanager.servise.HistoryManager;
import taskmanager.servise.TaskManager;
import taskmanager.tasktypes.Epic;
import taskmanager.tasktypes.Subtask;
import taskmanager.tasktypes.Task;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private Path fileToSavePath;

    public FileBackedTaskManager(Path path, HistoryManager historyManager) {
        super(historyManager);
        fileToSavePath = path;
    }

    @Override
    public int addTask(Task task) {
        int id = super.addTask(task);
        save();
        return id;
    }

    @Override
    public int addEpic(Epic epic) {
        int id = super.addEpic(epic);
        save();
        return id;
    }

    @Override
    public int addSubtask(Subtask subtask) {
        int id = super.addSubtask(subtask);
        save();
        return id;
    }

    @Override
    public List<Task> clearTasks() {
        List<Task> tasks = super.clearTasks();
        save();
        return tasks;
    }

    @Override
    public List<Epic> clearEpics() {
        List<Epic> epics = super.clearEpics();
        save();
        return epics;
    }

    @Override
    public List<Subtask> clearSubtasks() {
        List<Subtask> subtasks = super.clearSubtasks();
        save();
        return subtasks;
    }

    @Override
    public int updateTask(Task task) {
        int id = super.updateTask(task);
        save();
        return id;
    }

    @Override
    public int updateEpic(Epic epic) {
        int id = super.updateEpic(epic);
        save();
        return id;
    }

    @Override
    public int updateSubtask(Subtask subtask) {
        int id = super.updateSubtask(subtask);
        save();
        return id;
    }

    @Override
    public Task removeTask(int id) {
        Task task = super.removeTask(id);
        save();
        return task;
    }

    @Override
    public Epic removeEpic(int id) {
        Epic epic = super.removeEpic(id);
        save();
        return epic;
    }

    @Override
    public Subtask removeSubtask(int id) {
        Subtask subtask = super.removeSubtask(id);
        save();
        return subtask;
    }


    public void save() {
        try (Writer writer = new FileWriter(fileToSavePath.toFile())) {
            //writer.write("test");
            writer.write("id,type,name,description,status,epic" + "\n");
            for (Task task : getTasks()) {
                writer.write(task.toCSV() + "\n");
            }
            for (Epic epic : getEpics()) {
                writer.write(epic.toCSV() + "\n");
            }
            for (Subtask subtask : getSubtasks()) {
                writer.write(subtask.toCSV() + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл");
        }
    }
}

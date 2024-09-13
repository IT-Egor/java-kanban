package taskmanager.servise.impl;

import taskmanager.exceptions.ManagerSaveException;
import taskmanager.servise.HistoryManager;
import taskmanager.servise.TaskManager;
import taskmanager.tasktypes.Epic;
import taskmanager.tasktypes.Subtask;
import taskmanager.tasktypes.Task;
import taskmanager.utility.Managers;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private File file;

    public FileBackedTaskManager(File file, HistoryManager historyManager) {
        super(historyManager);
        this.file = file;
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
        try (Writer writer = new FileWriter(file)) {
            //writer.write("test");
            writer.write("type,id,name,description,status,epic" + "\n");
            for (Task task : getTasks()) {
                writer.write(task.toCSVLine() + "\n");
            }
            for (Epic epic : getEpics()) {
                writer.write(epic.toCSVLine() + "\n");
            }
            for (Subtask subtask : getSubtasks()) {
                writer.write(subtask.toCSVLine() + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            FileBackedTaskManager taskManager = new FileBackedTaskManager(file, Managers.getDefaultHistoryManager());
            HashMap<Integer, Integer> fromOldToNewEpicId = new HashMap<>();

            String readLine;
            List<String> lines = new ArrayList<>();
            br.readLine();

            while (br.ready()) {
                readLine = br.readLine();
                lines.add(readLine);
            }

            for (String line : lines) {
                if (line.startsWith("TASK")) {
                    Task task = Task.fromCSVLine(line);
                    taskManager.addTask(task);
                } else if (line.startsWith("EPIC")) {
                    Epic epic = Epic.fromCSVLine(line);
                    int oldId = epic.getId();
                    taskManager.addEpic(epic);
                    fromOldToNewEpicId.put(oldId, epic.getId());
                } else if (line.startsWith("SUBTASK")) {
                    Subtask subtask = Subtask.fromCSVLine(line);
                    subtask.setContainingEpicId(fromOldToNewEpicId.get(subtask.getContainingEpicId()));
                    taskManager.addSubtask(subtask);
                }
            }
            return taskManager;

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения из файла");
        }
    }
}

package taskmanager.servise.impl;

import taskmanager.exceptions.ManagerSaveException;
import taskmanager.servise.HistoryManager;
import taskmanager.servise.TaskManager;
import taskmanager.tasktypes.Epic;
import taskmanager.tasktypes.Subtask;
import taskmanager.tasktypes.Task;
import taskmanager.utility.Converter;
import taskmanager.utility.Managers;
import taskmanager.utility.Type;

import java.io.*;
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
            writer.write("type,id,name,description,status,startTime,duration,endTime,epic\n");
            for (Task task : getTasks()) {
                writer.write(Converter.anyTaskToCSVLine(task));
            }
            for (Epic epic : getEpics()) {
                writer.write(Converter.anyTaskToCSVLine(epic));
            }
            for (Subtask subtask : getSubtasks()) {
                writer.write(Converter.anyTaskToCSVLine(subtask));
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Error writing to a file: " + e.getMessage(), e.getCause());
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            FileBackedTaskManager taskManager = new FileBackedTaskManager(file, Managers.getDefaultHistoryManager());

            String line;
            br.readLine();

            while (br.ready()) {
                line = br.readLine();
                Task task = Converter.fromCSVLineToAnyTask(line);
                if (task.getType() == Type.TASK) {
                    taskManager.tasks.put(task.getId(), task);
                } else if (task.getType() == Type.EPIC) {
                    taskManager.epics.put(task.getId(), (Epic) task);
                } else if (task.getType() == Type.SUBTASK) {
                    taskManager.subtasks.put(task.getId(), (Subtask) task);
                    taskManager.epics.get(((Subtask) task).getContainingEpicId()).addSubtaskId(task.getId());
                }
                taskManager.prioritizedTasks.add(task);
            }
            return taskManager;

        } catch (IOException e) {
            throw new ManagerSaveException("Error reading from a file: " + e.getMessage(), e.getCause());
        }
    }
}

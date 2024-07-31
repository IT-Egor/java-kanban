package taskmanager.servise;

import taskmanager.tasktypes.Epic;
import taskmanager.tasktypes.Subtask;
import taskmanager.tasktypes.Task;
import taskmanager.utility.IdManager;

import java.util.HashMap;
import java.util.ArrayList;

public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public int addTask(Task task) {
        int id = IdManager.generateId();
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    public int addEpic(Epic epic) {
        int id = IdManager.generateId();
        epic.setId(id);
        epics.put(id, epic);
        return id;
    }

    public int addSubtask(Subtask subtask, int epicId) {
        if (!epics.containsKey(epicId)) {
            return -1;
        }
        int subtaskId = IdManager.generateId();
        subtask.setId(subtaskId);
        subtasks.put(subtaskId, subtask);
        subtask.setContainingEpicId(epicId);
        Epic containingEpic = epics.get(epicId);
        containingEpic.addSubtaskId(subtask.getId());
        return subtaskId;
    }

    public HashMap<Integer, Task> clearTasks() {
        HashMap<Integer, Task> out = new HashMap<>(tasks);
        tasks.clear();
        return out;
    }

    public HashMap<Integer, Epic> clearEpics() {
        HashMap<Integer, Epic> out = new HashMap<>(epics);
        epics.clear();
        subtasks.clear();
        return out;
    }

    public HashMap<Integer, Subtask> clearSubtasks() {
        for (Epic epic : epics.values()) {
            epic.clearSubtasksIds();
        }
        HashMap<Integer, Subtask> out = new HashMap<>(subtasks);
        subtasks.clear();
        return out;
    }

    public Task findTask(int id) {
        return tasks.get(id);
    }

    public Epic findEpic(int id) {
        return epics.get(id);
    }

    public Subtask findSubtask(int id) {
        return subtasks.get(id);
    }

    public int updateTask(Task updatedTask) {
        if (!tasks.containsKey(updatedTask.getId())) {
            return -1;
        }
        tasks.put(updatedTask.getId(), updatedTask);
        return updatedTask.getId();
    }

    public int updateEpic(Epic updatedEpic) {
        if (!epics.containsKey(updatedEpic.getId())) {
            return -1;
        }
        Epic oldEpic = epics.get(updatedEpic.getId());  //их id равны
        ArrayList<Integer> oldSubtaskIds = oldEpic.getSubtasksIds();
        ArrayList<Integer> newSubtaskIds = updatedEpic.getSubtasksIds();
        if (!oldSubtaskIds.equals(newSubtaskIds)) {
            return -2;
        }
        epics.put(updatedEpic.getId(), updatedEpic);
        return updatedEpic.getId();
    }

    public int updateSubtask(Subtask updatedSubtask) {
        if (!subtasks.containsKey(updatedSubtask.getId())) {
            return -1;
        }
        Subtask oldSubtask = subtasks.get(updatedSubtask.getId());  //их id равны
        if (oldSubtask.getContainingEpicId() != updatedSubtask.getContainingEpicId()) {
            return -2;
        }
        subtasks.put(updatedSubtask.getId(), updatedSubtask);
        return updatedSubtask.getId();
    }
}

package taskmanager.servise;

import taskmanager.tasktypes.Epic;
import taskmanager.tasktypes.Subtask;
import taskmanager.tasktypes.Task;
import taskmanager.utility.IdManager;
import taskmanager.utility.Status;

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
        if (subtask.getContainingEpicId() != 0) { //если подзадача уже принадлежит какому-то эпику
            return -4;
        }
        int subtaskId = IdManager.generateId();
        subtask.setId(subtaskId);
        subtasks.put(subtaskId, subtask);
        subtask.setContainingEpicId(epicId);
        Epic containingEpic = epics.get(epicId);
        containingEpic.addSubtaskId(subtask.getId());
        setSubtasksStatusToEpic(containingEpic.getId());
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
        Task oldTask = tasks.get(updatedTask.getId());  //их id равны
        if (oldTask.getStatus() != updatedTask.getStatus()) {
            return -3;
        }
        tasks.put(updatedTask.getId(), updatedTask);
        return updatedTask.getId();
    }

    public int updateEpic(Epic updatedEpic) {
        if (!epics.containsKey(updatedEpic.getId())) {
            return -1;
        }
        Epic oldEpic = epics.get(updatedEpic.getId());  //их id равны
        if (oldEpic.getStatus() != updatedEpic.getStatus()) {
            return -3;
        }
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
        if (oldSubtask.getStatus() != updatedSubtask.getStatus()) {
            return -3;
        }
        subtasks.put(updatedSubtask.getId(), updatedSubtask);
        return updatedSubtask.getId();
    }

    public Task removeTask(int id) {
        return tasks.remove(id);
    }

    public Epic removeEpic(int id) {
        if (!epics.containsKey(id)) {
            return null;
        }
        Epic epic = epics.get(id);
        ArrayList<Integer> subtaskIds = epic.getSubtasksIds();
        for (int subtaskId : subtaskIds) {  //нужно удалить подзадачи принадлежащие этому эпику
            subtasks.remove(subtaskId);
        }
        return epics.remove(id);
    }

    public Subtask removeSubtask(int id) {
        if (!subtasks.containsKey(id)) {
            return null;
        }
        Subtask subtask = subtasks.get(id);
        Epic containingEpic = epics.get(subtask.getContainingEpicId());
        containingEpic.removeSubtaskId(id); //нужно удалить эту подзадачу из содержащего эпика
        Subtask removedSubtask = subtasks.remove(id);
        setSubtasksStatusToEpic(containingEpic.getId());
        return removedSubtask;
    }

    public Task setTaskStatus(int id, Status status) {
        if (!tasks.containsKey(id)) {
            return null;
        }
        Task task = tasks.get(id);
        task.setStatus(status);
        return task;
    }

    public Subtask setSubtaskStatus(int id, Status status) {
        if (!subtasks.containsKey(id)) {
            return null;
        }
        Subtask subtask = subtasks.get(id);
        subtask.setStatus(status);
        setSubtasksStatusToEpic(subtask.getContainingEpicId());
        return subtask;
    }

    public ArrayList<Integer> getEpicSubtasksIds(int epicId) {
        if (!epics.containsKey(epicId)) {
            return null;
        }
        return epics.get(epicId).getSubtasksIds();
    }

    private void setSubtasksStatusToEpic(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Integer> subtaskIds = epic.getSubtasksIds();
        if (subtaskIds.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        int numberOfNEWs = 0;
        int numberOfDONEs = 0;

        for (int subtaskId : subtaskIds) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask.getStatus() == Status.IN_PROGRESS) {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            } else if (subtask.getStatus() == Status.DONE) {
                if (numberOfNEWs > 0) {
                    epic.setStatus(Status.IN_PROGRESS);
                    return;
                }
                numberOfDONEs++;
            } else if (subtask.getStatus() == Status.NEW) {
                if (numberOfDONEs > 0) {
                    epic.setStatus(Status.IN_PROGRESS);
                    return;
                }
                numberOfNEWs++;
            }
        }
        if (numberOfDONEs == subtaskIds.size()) {
            epic.setStatus(Status.DONE);
        } else if (numberOfNEWs == subtaskIds.size()) {
            epic.setStatus(Status.NEW);
        }
    }
}

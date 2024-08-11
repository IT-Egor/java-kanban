package taskmanager.servise.TaskManagers;

import taskmanager.servise.HistoryManagers.HistoryManager;
import taskmanager.tasktypes.Epic;
import taskmanager.tasktypes.Subtask;
import taskmanager.tasktypes.Task;
import taskmanager.utility.Managers;
import taskmanager.utility.Status;

import java.util.HashMap;
import java.util.ArrayList;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public int addTask(Task task) {
        if (tasks.containsKey(task.getId())) {  //если такая задача уже есть
            return -5;
        }
        int id = Managers.getNextId();
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    @Override
    public int addEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {  //если такой эпик уже есть

            return -5;
        }
        int id = Managers.getNextId();
        epic.setId(id);
        epics.put(id, epic);
        return id;
    }

    @Override
    public int addSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {  //если такая подзадача уже есть
            return -5;
        }
        int epicId = subtask.getContainingEpicId();
        if (!epics.containsKey(epicId)) {
            return -1;
        }
        int subtaskId = Managers.getNextId();
        subtask.setId(subtaskId);
        subtasks.put(subtaskId, subtask);
        Epic containingEpic = epics.get(epicId);
        containingEpic.addSubtaskId(subtask.getId());
        setSubtasksStatusToEpic(containingEpic.getId());
        return subtaskId;
    }

    @Override
    public ArrayList<Task> clearTasks() {
        ArrayList<Task> out = new ArrayList<>(tasks.values());
        tasks.clear();
        return out;
    }

    @Override
    public ArrayList<Epic> clearEpics() {
        ArrayList<Epic> out = new ArrayList<>(epics.values());
        epics.clear();
        subtasks.clear();
        return out;
    }

    @Override
    public ArrayList<Subtask> clearSubtasks() {
        for (Epic epic : epics.values()) {
            epic.clearSubtasksIds();
        }
        ArrayList<Subtask> out = new ArrayList<>(subtasks.values());
        subtasks.clear();
        for (Epic epic : epics.values()) {
            int epicId = epic.getId();
            setSubtasksStatusToEpic(epicId);
        }
        return out;
    }

    @Override
    public Task findTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Epic findEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public Subtask findSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
    public Task removeTask(int id) {
        return tasks.remove(id);
    }

    @Override
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

    @Override
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

    @Override
    public Task setTaskStatus(int id, Status status) {
        if (!tasks.containsKey(id)) {
            return null;
        }
        Task task = tasks.get(id);
        task.setStatus(status);
        return task;
    }

    @Override
    public Subtask setSubtaskStatus(int id, Status status) {
        if (!subtasks.containsKey(id)) {
            return null;
        }
        Subtask subtask = subtasks.get(id);
        subtask.setStatus(status);
        setSubtasksStatusToEpic(subtask.getContainingEpicId());
        return subtask;
    }

    @Override
    public ArrayList<Integer> getEpicSubtasksIds(int epicId) {
        if (!epics.containsKey(epicId)) {
            return null;
        }
        return epics.get(epicId).getSubtasksIds();
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
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

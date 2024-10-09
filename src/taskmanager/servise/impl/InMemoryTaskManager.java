package taskmanager.servise.impl;

import taskmanager.exceptions.TaskValidationException;
import taskmanager.servise.HistoryManager;
import taskmanager.servise.TaskManager;
import taskmanager.tasktypes.Epic;
import taskmanager.tasktypes.Subtask;
import taskmanager.tasktypes.Task;
import taskmanager.utility.Managers;
import taskmanager.utility.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected HistoryManager historyManager;
    protected Set<Task> prioritizedTasks = new TreeSet<>(
            Comparator.comparing(task -> task.getStartTime().get()));

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public int addTask(Task task) {
        throwIfTaskTimesIsNull(task);
        throwIfTasksOverlaps(task);
        int id = Managers.getNextId();
        task.setId(id);
        task.setStatus(Status.NEW);
        tasks.put(id, task);
        prioritizedTasks.add(task);
        return id;
    }

    @Override
    public int addEpic(Epic epic) {
        int id = Managers.getNextId();
        epic.setId(id);
        epic.setStatus(Status.NEW);
        epics.put(id, epic);
        return id;
    }

    @Override
    public int addSubtask(Subtask subtask) {
        throwIfTaskTimesIsNull(subtask);
        throwIfTasksOverlaps(subtask);
        int epicId = subtask.getContainingEpicId();
        if (!epics.containsKey(epicId)) {
            return -1;
        }
        int subtaskId = Managers.getNextId();
        subtask.setId(subtaskId);
        subtask.setStatus(Status.NEW);
        subtasks.put(subtaskId, subtask);
        prioritizedTasks.add(subtask);
        Epic containingEpic = epics.get(epicId);
        containingEpic.addSubtaskId(subtask.getId());
        setSubtasksStatusToEpic(containingEpic.getId());
        setTimesToEpic(containingEpic.getId());
        return subtaskId;
    }

    @Override
    public List<Task> clearTasks() {
        List<Task> out = new ArrayList<>(tasks.values());

        tasks.values().forEach(task -> {
            historyManager.remove(task.getId());
            prioritizedTasks.remove(task);
        });
        tasks.clear();
        return out;
    }

    @Override
    public List<Epic> clearEpics() {
        List<Epic> out = new ArrayList<>(epics.values());

        epics.values().forEach(epic -> {
            historyManager.remove(epic.getId());
        });

        subtasks.values().forEach(subtask -> {
            historyManager.remove(subtask.getId());
            prioritizedTasks.remove(subtask);
        });
        epics.clear();
        subtasks.clear();
        return out;
    }

    @Override
    public List<Subtask> clearSubtasks() {
        epics.values().forEach(Epic::clearSubtasksIds);

        subtasks.values().forEach(subtask -> {
                    historyManager.remove(subtask.getId());
                    prioritizedTasks.remove(subtask);
        });
        List<Subtask> out = new ArrayList<>(subtasks.values());
        subtasks.clear();

        epics.values().forEach(epic -> {
            int epicId = epic.getId();
            setSubtasksStatusToEpic(epicId);
            setTimesToEpic(epicId);
        });
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
        throwIfTaskTimesIsNull(updatedTask);
        throwIfTasksOverlaps(updatedTask);
        if (!tasks.containsKey(updatedTask.getId())) {
            return -1;
        }
        tasks.put(updatedTask.getId(), updatedTask);
        Task oldTask = tasks.get(updatedTask.getId());
        prioritizedTasks.remove(oldTask);
        prioritizedTasks.add(updatedTask);
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
        List<Integer> oldSubtaskIds = oldEpic.getSubtasksIds();
        List<Integer> newSubtaskIds = updatedEpic.getSubtasksIds();
        if (!oldSubtaskIds.equals(newSubtaskIds)) {
            return -2;
        }
        epics.put(updatedEpic.getId(), updatedEpic);
        return updatedEpic.getId();
    }

    @Override
    public int updateSubtask(Subtask updatedSubtask) {
        throwIfTaskTimesIsNull(updatedSubtask);
        throwIfTasksOverlaps(updatedSubtask);
        if (!subtasks.containsKey(updatedSubtask.getId())) {
            return -1;
        }
        Subtask oldSubtask = subtasks.get(updatedSubtask.getId());  //их id равны
        int oldSubtaskContainingEpicId = oldSubtask.getContainingEpicId();
        if (oldSubtaskContainingEpicId != updatedSubtask.getContainingEpicId()) {
            return -2;
        }
        subtasks.put(updatedSubtask.getId(), updatedSubtask);
        prioritizedTasks.remove(oldSubtask);
        prioritizedTasks.add(updatedSubtask);
        setSubtasksStatusToEpic(oldSubtaskContainingEpicId);
        setTimesToEpic(oldSubtaskContainingEpicId);
        return updatedSubtask.getId();
    }

    @Override
    public Task removeTask(int id) {
        if (!tasks.containsKey(id)) {
            return null;
        }
        historyManager.remove(id);
        prioritizedTasks.remove(tasks.get(id));
        return tasks.remove(id);
    }

    @Override
    public Epic removeEpic(int id) {
        if (!epics.containsKey(id)) {
            return null;
        }
        Epic epic = epics.get(id);
        List<Integer> subtaskIds = epic.getSubtasksIds();
        historyManager.remove(id);
        for (int subtaskId : subtaskIds) {  //нужно удалить подзадачи принадлежащие этому эпику
            historyManager.remove(subtaskId);
            prioritizedTasks.remove(subtasks.get(subtaskId));
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
        historyManager.remove(id);
        prioritizedTasks.remove(subtask);
        Epic containingEpic = epics.get(subtask.getContainingEpicId());
        containingEpic.removeSubtaskId(id); //нужно удалить эту подзадачу из содержащего эпика
        Subtask removedSubtask = subtasks.remove(id);
        setSubtasksStatusToEpic(containingEpic.getId());
        setTimesToEpic(containingEpic.getId());
        return removedSubtask;
    }

    @Override
    public List<Integer> getEpicSubtasksIds(int epicId) {
        if (!epics.containsKey(epicId)) {
            return null;
        }
        return epics.get(epicId).getSubtasksIds();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }


    private boolean isTasksOverlaps(Task task) {
        return !prioritizedTasks.isEmpty() && prioritizedTasks.stream()
                .anyMatch(currentTask -> {
                    LocalDateTime currentTaskStartTime = currentTask.getStartTime().get();
                    LocalDateTime taskStartTime = task.getStartTime().get();
                    LocalDateTime currentTaskEndTime = currentTask.getEndTime().get();
                    LocalDateTime taskEndTime = task.getEndTime().get();

                    return !task.equals(currentTask)
                            && (((taskStartTime.isBefore(currentTaskStartTime)
                            || taskStartTime.isEqual(currentTaskStartTime))
                            && taskEndTime.isAfter(currentTaskStartTime))
                            ||
                            ((currentTaskStartTime.isBefore(taskStartTime)
                            || currentTaskStartTime.isEqual(taskStartTime))
                            && currentTaskEndTime.isAfter(taskStartTime)));
                });
    }

    private void throwIfTasksOverlaps(Task task) {
        if (isTasksOverlaps(task)) {
            throw new TaskValidationException(String.format("%s with name='%s'" +
                    " overlaps in time with one of the tasks in the manager", task.getType(), task.getName()));
        }
    }

    private void throwIfTaskTimesIsNull(Task task) {
        if (task.getStartTime().isEmpty() || task.getDuration().isEmpty()) {
            throw new TaskValidationException("Task with id=" + task.getId() + " has null times");
        }
    }

    private void setSubtasksStatusToEpic(int epicId) {
        Epic epic = epics.get(epicId);
        List<Integer> subtaskIds = epic.getSubtasksIds();
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

    private void setTimesToEpic(int epicId) {
        Epic epic = epics.get(epicId);
        List<Integer> subtaskIds = epic.getSubtasksIds();
        if (subtaskIds.isEmpty()) {
            epic.setStartTime(null);
            epic.setDuration(null);
            epic.setEndTime(null);
            return;
        }

        // менеджер работает в тепличных условиях, поэтому null можно не обрабатывать
        LocalDateTime subtasksMinTime = subtasks.get(subtaskIds.getFirst()).getStartTime().get();
        LocalDateTime subtasksMaxTime = subtasks.get(subtaskIds.getFirst()).getEndTime().get();

        Duration subtasksSumDuration = Duration.ZERO;

        for (int subtaskId : subtaskIds) {
            Subtask subtask = subtasks.get(subtaskId);
            LocalDateTime subtaskStartTime = subtask.getStartTime().get();
            LocalDateTime subtaskEndTime = subtask.getEndTime().get();

            subtasksSumDuration = subtasksSumDuration.plus(subtask.getDuration().get());

            if (subtasksMinTime.isAfter(subtaskStartTime)) {
                subtasksMinTime = subtaskStartTime;
            }
            if (subtasksMaxTime.isBefore(subtaskEndTime)) {
                subtasksMaxTime = subtaskStartTime;
            }
        }
        epic.setStartTime(subtasksMinTime);
        epic.setEndTime(subtasksMaxTime);
        epic.setDuration(subtasksSumDuration);
    }
}

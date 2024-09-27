package taskmanager.servise;

import taskmanager.tasktypes.Epic;
import taskmanager.tasktypes.Subtask;
import taskmanager.tasktypes.Task;

import java.util.List;
import java.util.Set;

public interface TaskManager {

    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    int addTask(Task task);

    int addEpic(Epic epic);

    int addSubtask(Subtask subtask);

    List<Task> clearTasks();

    List<Epic> clearEpics();

    List<Subtask> clearSubtasks();

    Task findTask(int id);

    Epic findEpic(int id);

    Subtask findSubtask(int id);

    int updateTask(Task updatedTask);

    int updateEpic(Epic updatedEpic);

    int updateSubtask(Subtask updatedSubtask);

    Task removeTask(int id);

    Epic removeEpic(int id);

    Subtask removeSubtask(int id);

    List<Integer> getEpicSubtasksIds(int epicId);

    List<Task> getHistory();

    Set<Task> getPrioritizedTasks();
}

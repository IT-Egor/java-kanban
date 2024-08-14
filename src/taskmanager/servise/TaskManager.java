package taskmanager.servise;

import taskmanager.tasktypes.Epic;
import taskmanager.tasktypes.Subtask;
import taskmanager.tasktypes.Task;
import taskmanager.utility.Status;

import java.util.ArrayList;
import java.util.LinkedList;

public interface TaskManager {

    ArrayList<Task> getTasks();

    ArrayList<Epic> getEpics();

    ArrayList<Subtask> getSubtasks();

    int addTask(Task task);

    int addEpic(Epic epic);

    int addSubtask(Subtask subtask);

    ArrayList<Task> clearTasks();

    ArrayList<Epic> clearEpics();

    ArrayList<Subtask> clearSubtasks();

    Task findTask(int id);

    Epic findEpic(int id);

    Subtask findSubtask(int id);

    int updateTask(Task updatedTask);

    int updateEpic(Epic updatedEpic);

    int updateSubtask(Subtask updatedSubtask);

    Task removeTask(int id);

    Epic removeEpic(int id);

    Subtask removeSubtask(int id);

    ArrayList<Integer> getEpicSubtasksIds(int epicId);

    LinkedList<Task> getHistory();
}

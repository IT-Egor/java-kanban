package taskmanager.servise.impl;

import taskmanager.servise.HistoryManager;
import taskmanager.servise.Node;
import taskmanager.tasktypes.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    private Map<Integer, Node<Task>> history = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;
    private int size = 0;

    @Override
    public void add(Task task) {
        if (history.containsKey(task.getId())) {
            remove(task.getId());
        }
        linkLast(task);
        history.put(task.getId(), tail);
    }

    @Override
    public void remove(int id) {
        Node<Task> nodeToRemove = history.get(id);
        if (nodeToRemove != null) {
            removeNode(nodeToRemove);
            history.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(getTasks());
    }


    private void linkLast(Task task) {
        Node<Task> node = new Node<>(task);

        if (head == null) {
            head = node;
            tail = node;
        } else {
            tail.setNext(node);
            node.setPrev(tail);
            tail = node;
        }
        size++;
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node<Task> current = head;

        while (current != null) {
            tasks.add(current.getData());
            current = current.getNext();
        }

        return tasks;
    }

    private void removeNode(Node<Task> node) {
        if (size == 0) {
            return;
        } else if (size == 1) {
            head = null;
            tail = null;
            size--;
        } else if (node == head) {
            head = head.getNext();
            head.setPrev(null);
            size--;
        } else if (node == tail) {
            tail = tail.getPrev();
            tail.setNext(null);
            size--;
        } else {
            Node<Task> current = node.getPrev();
            current.setNext(node.getNext());
            current = node.getNext();
            current.setPrev(node.getPrev());
            size--;
        }
    }
}

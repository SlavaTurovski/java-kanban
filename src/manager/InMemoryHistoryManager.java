package manager;

import interfaces.HistoryManager;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final HashMap<Integer, Node> history = new HashMap<>();
    private Node head;
    private Node tail;

    private void linkLast(Task task) {

        Node node = new Node(task, null, tail);

        if (tail == null) {
            head = node;
        } else {
            tail.setNext(node);
            node.setPrev(tail);
        }

        tail = node;
    }

    private ArrayList<Task> getTasks() {

        ArrayList<Task> tasks = new ArrayList<>();
        Node currentNode = head;

        while (currentNode != null) {
            tasks.add(currentNode.getData());
            currentNode = currentNode.getNext();
        }

        return tasks;
    }


    private void removeNode(Node node) {

        if (node == null) {
            return;
        }

        Node nextNode = node.getNext();
        Node prevNode = node.getPrev();

        if (prevNode != null) {
            prevNode.setNext(nextNode);
        } else {
            head = nextNode;
        }

        if (nextNode != null) {
            nextNode.setPrev(prevNode);
        } else {
            tail = prevNode;
        }

        node.setNext(null);
        node.setPrev(null);
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            remove(task.getId());
            linkLast(task);
            history.put(task.getId(), tail);
        }
    }

    @Override
    public void remove(int id) {
        if (history.isEmpty()) {
            return;
        }

        Node nodeToRemove = history.remove(id);

        if (nodeToRemove == null) {
            return;
        }

        removeNode(nodeToRemove);
    }


    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

}
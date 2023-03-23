package managers;


import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    CustomLinkedList customLinkedList;

    public InMemoryHistoryManager() {
        customLinkedList = new CustomLinkedList();
    }

    @Override
    public void add(Task task) {
        customLinkedList.linkLast(task);
    }

    @Override
    public void remove(int id) {
        customLinkedList.removeNode(customLinkedList.getNode(id));
    }

    @Override
    public List<Task> getHistory() {
        return customLinkedList.getTasks();
    }

    class CustomLinkedList {
        private Node last;
        private Map<Integer, Node> nodeLinks;

        public CustomLinkedList() {
            last = null;
            nodeLinks = new HashMap<>();
        }

        private void removeNode(Node node) {
            Node prevNode = node.getPrevNode();
            Node nextNode = node.getNextNode();
            nodeLinks.remove(node.getData().getId());
            if (prevNode == null && nextNode == null) {
                last = null;
                return;
            }
            if (nextNode != null) {
                nextNode.setPrevNode(prevNode);
            }
            if (prevNode != null) {
                if (node.getNextNode() == null) {
                    last = prevNode;
                }
                prevNode.setNextNode(nextNode);
            }
        }

        private void linkLast(Task task) {
            if (nodeLinks.containsKey(task.getId())) {
                removeNode(nodeLinks.get(task.getId()));
            }
            if (last == null) {
                Node newNode = new Node(null, null, task);
                last = newNode;
                nodeLinks.put(task.getId(), newNode);
                return;
            }
            Node newNode = new Node(last, null, task);
            last.setNextNode(newNode);
            last = newNode;
            nodeLinks.put(task.getId(), newNode);
        }

        private Node getNode(int id) {
            return nodeLinks.get(id);
        }

        List<Task> getTasks() {//
            if (last == null) {
                return Collections.emptyList();
            }
            ArrayList<Task> history = new ArrayList<>();
            Node prevNode = last;
            do {
                history.add(prevNode.getData());
                prevNode = prevNode.getPrevNode();
            } while (prevNode != null);
            return history;
        }
    }

}

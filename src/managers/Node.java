package managers;

import tasks.Task;

class Node {
    private Node prevNode;
    private Node nextNode;
    private final Task data;

    public Node(Node prevNode, Node nextNode, Task data) {
        this.prevNode = prevNode;
        this.nextNode = nextNode;
        this.data = data;
    }

    public Node getPrevNode() {
        return prevNode;
    }

    public Node getNextNode() {
        return nextNode;
    }

    public Task getData() {
        return data;
    }

    public void setPrevNode(Node prevNode) {
        this.prevNode = prevNode;
    }

    public void setNextNode(Node nextNode) {
        this.nextNode = nextNode;
    }
}

public class Queue<T> {
    private CircularDoublyLinkedList<T> list;

    public Queue() {
        list = new CircularDoublyLinkedList<>();
    }

    public void enqueue(T data) {
        list.add(data);
    }

    public T dequeue() {
        return list.removeFirstElement();
    }
}

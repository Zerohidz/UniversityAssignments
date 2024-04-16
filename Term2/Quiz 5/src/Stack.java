public class Stack<T> {
    private CircularDoublyLinkedList<T> list;

    public Stack() {
        list = new CircularDoublyLinkedList<>();
    }

    public void push(T data) {
        list.add(data);
    }

    public T pop() {
        return list.removeLastElement();
    }

    public boolean isEmpty() {
        return list.head == null;
    }
}

public class CircularDoublyLinkedList<T> {
    Node<T> head;

    public CircularDoublyLinkedList() {
        head = null;
    }

    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
            newNode.next = newNode;
            newNode.previous = newNode;
        } else {
            Node<T> last = head.previous;
            newNode.next = head;
            head.previous = newNode;
            newNode.previous = last;
            last.next = newNode;
        }
    }

    public T removeFirstElement() {
        if (head == null) {
            return null;
        }
        Node<T> last = head.previous;
        if (last == head) {
            head = null;
            return last.data;
        }
        Node<T> removedElement = head;
        head = head.next;
        last.next = head;
        head.previous = last;
        return removedElement.data;
    }

    public T removeLastElement() {
        if (head == null) {
            return null;
        }
        Node<T> last = head.previous;
        if (last == head) {
            head = null;
            return last.data;
        }
        Node<T> secondLast = last.previous;
        secondLast.next = head;
        head.previous = secondLast;

        return last.data;
    }
}

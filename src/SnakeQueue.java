public class SnakeQueue {

    //this class is for creating a Queue which elements are Snake
    private Snake[] elements;
    private int rear, front;

    public SnakeQueue(int capacity) {
        elements = new Snake[capacity];
        rear = -1;
        front = 0;
    }

    public boolean isEmpty() {
        if (elements[front] == null)
            return true;
        else
            return false;
    }

    public boolean isFull() {
        if (front == (rear + 1) % elements.length && elements[front] != null &&
                elements[rear] != null) {
            return true;
        } else
            return false;
    }

    public void enqueue(Snake data) {
        if (isFull()) {
            System.out.println("Queue is Full");
        } else {
            rear = (rear + 1) % elements.length;
            elements[rear] = data;
        }
    }

    public Snake dequeue() {
        if (isEmpty()) {
            System.out.println("Queue is Empty");
            return null;
        } else {
            Snake retdata = elements[front];
            elements[front] = null;
            front = (front + 1) % elements.length;
            return retdata;
        }
    }

    public Snake peek() {
        if (isEmpty()) {
            return null;
        }
        else {
            return elements[front];
        }
    }

    public int size() {
        if (elements[front] == null) {
            return 0;
        } else {
            if (rear >= front)
                return rear - front + 1;
            else
                return elements.length - (front - rear) + 1;
        }
    }
}

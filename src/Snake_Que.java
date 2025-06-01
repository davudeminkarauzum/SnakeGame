public class Snake_Que {

    private Snakes[] elements;
    private int rear, front;

    public Snake_Que(int capacity) {
        elements = new Snakes[capacity];
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

    public void enqueue(Snakes data) {
        if (isFull()) {
            System.out.println("Queue is Full");
        } else {
            rear = (rear + 1) % elements.length;
            elements[rear] = data;
        }
    }

    public Snakes dequeue() {
        if (isEmpty()) {
            System.out.println("Queue is Empty");
            return null;
        } else {
            Snakes retdata = elements[front];
            elements[front] = null;
            front = (front + 1) % elements.length;
            return retdata;
        }
    }

    public Snakes peek() {
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

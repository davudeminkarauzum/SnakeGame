public class CircularQueue {
    private int rear, front;
    private Object[] elements;

    public CircularQueue(int capacity) {
        elements = new Object[capacity];
        rear = -1;
        front = 0;
    }
    void enqueue(Object element){
        if(isFull()){
            System.out.println("Queue is full");
        }else{
            rear = (rear + 1) % elements.length;
            elements[rear] = element;
        }
    }
    Object dequeue(){
        if(isEmpty()){
            System.out.println("Queue is empty");
            return null;
        }else{
            Object data = elements[front];
            elements[front] = null;
            front = (front + 1) % elements.length;
            return data;
        }
    }
    Object peek(){
        if(isEmpty()){
            System.out.println("Queue is empty");
            return null;
        }else{
            return elements[front];
        }
    }
    boolean isEmpty(){
        return elements[front] == null;
    }

    boolean isFull(){
        if (rear == -1) return false;
        return ((front == (rear + 1) % elements.length) && elements[front]!=null && elements[rear]!=null );
    }

    int size(){
        if(rear>=front){
            return (rear-front+1);
        }else if(elements[front]==null){
            return 0;
        }else{
            return elements.length -(front-rear-1);
        }
    }




}

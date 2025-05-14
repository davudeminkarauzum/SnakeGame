public class Snakes {

    private SingleLinkedList[] elements;
    private int rear, front;

    public Snakes(int capacity){
        elements = new SingleLinkedList[capacity]; 
        rear = -1;
        front = 0;
    }

    boolean isEmpty(){
        return elements[front] == null;
    }

    boolean isFull(){
        return (front == (rear + 1) % elements.length && elements[rear] != null);
    }

    void enqueue(SingleLinkedList data){

            rear = (rear + 1) % elements.length;
            elements[rear] = data;

    }

    SingleLinkedList dequeue(){
        if(isEmpty()){
            System.out.println("Queue is Empty");
            return null;
        } else {
            SingleLinkedList retdata = elements[front];
            elements[front] = null;
            front = (front + 1) % elements.length;
            return retdata;
        }
    }

    SingleLinkedList peek(){
        return elements[front];
    }

    int size(){
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

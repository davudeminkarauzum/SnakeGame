import java.util.Random;

public class InputQueue {
    Random rand = new Random();
    private int randVal;
    private CircularQueue queue = new CircularQueue(15);

    InputQueue() { // filling input queue before the game starts
        for (int i = 0; i < 15; i++) {
            addElement();
        }
    }

    void addElement() {
        randVal = rand.nextInt(100) + 1;
        if (randVal <= 50)
            queue.enqueue(1);
        else if (randVal <= 75)
            queue.enqueue(2);
        else if (randVal <= 88)
            queue.enqueue(3);
        else if (randVal <= 97)
            queue.enqueue("@");
        else
            queue.enqueue("S");
    }

    Object dequeueInput() {
    	
    	Object a = queue.dequeue();
        addElement();
        return a;      
    }

     void writeElements() {
        Object current;     
       for (int i = 0; i < 15; i++) { 
            current = queue.dequeue();

            char c = current.toString().charAt(0);  
            GameField.map[2][56 + i] = c;  
            
            queue.enqueue(current);
        }

    }
}
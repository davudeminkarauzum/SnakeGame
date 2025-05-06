import java.util.Random;

public class InputQueue {
    Random rand = new Random();
    private int randVal;
    private CircularQueue queue = new CircularQueue(15);

    InputQueue() {//başlangıçta queueyi random elementlerle dolduruyoruz
        for (int i = 0; i < 15; i++) {
            addElement();
        }
    }

    void addElement() {//1 ile 100 arası sayı belirleyip olasılıklara göre değerleri atıyoruz
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

    Object dequeueInput() { // önce elementi çekiyoruz sonra element ekliyoruz. böyle yaparsak inputQueue'yu 16 boyutluk açmamıza gerek kalmaz
    	
    	Object a = queue.dequeue();
        addElement();
        return a;      
    }

     void writeElements() {
        Object current;     
       for (int i = 0; i < 15; i++) { 
            current = queue.dequeue();
           
         // current öğesini char'a dönüştürüp haritaya ekliyoruz
            char c = current.toString().charAt(0);  
            GameField.map[2][58 + i] = c;  
            
            queue.enqueue(current);
        }

    }
}

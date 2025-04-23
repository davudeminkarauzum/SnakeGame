import java.util.Random;

public class InputQueue {
    Random rand = new Random();
    private int randVal;
    private CircularQueue queue = new CircularQueue(16);//15 değil 16 olmasının sebebi çıkartmadan önce ekleme yapmamız

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

    Object dequeueInput() {//önce yeni bir element ekliyoruz ardından en öndekini çekiyoruz
        addElement();
        return queue.dequeue();

    }

    String writeElements() {
        Object current;//itemleri string değere dönüştürüyor yazdırmak için
        String queueitems = "";
        for (int i = 0; i < 16; i++) {
            current = queue.dequeue();
            queueitems += current.toString();
            queue.enqueue(current);
        }
        return queueitems;

    }
}

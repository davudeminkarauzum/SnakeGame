import java.util.Random;

public class Snakes {

    private int xcoor, ycoor;
    private int targetx, targety;
    private boolean movemode;
    private int stuckCounter;
    Node head;

    public Snakes(int x, int y){
        head = null;
        movemode = true;
        stuckCounter = 0;
        xcoor = x;
        ycoor = y;
        Random rnd = new Random();
        int tx, ty;
        boolean isAvaible = false;
        do {
            tx = rnd.nextInt(23);
            ty = rnd.nextInt(55);
            if (GameField.map[tx][ty] == '1' || GameField.map[tx][ty] == '2' || GameField.map[tx][ty] == '3'){
                isAvaible = true;
            }
        }while (!isAvaible);
        targetx = tx;
        targety = ty;
    }

    public int getXcoor(){
        return xcoor;
    }

    public void setXcoor(int xcoor) {
        this.xcoor = xcoor;
    }

    public int getYcoor(){
        return ycoor;
    }

    public int getTargetx(){
        return targetx;
    }

    public void setTargetx(int targetx) {
        this.targetx = targetx;
    }

    public void setYcoor(int ycoor) {
        this.ycoor = ycoor;
    }

    public int getTargety() {
        return targety;
    }

    public void setTargety(int targety) {
        this.targety = targety;
    }

    public boolean getMovemode() {
        return movemode;
    }

    public void setMovemode(boolean movemode) {
        this.movemode = movemode;
    }

    public int getStuckCounter() {
        return stuckCounter;
    }

    public void setStuckCounter(int stuckCounter) {
        this.stuckCounter = stuckCounter;
    }

    public void add(Object data) {
        if(head == null) {
            head = new Node(data);
        } else {
            Node temp = head;
            while(temp.getLink() != null) {
                temp = (Node) temp.getLink();
            }
            Node toAdd = new Node(data);
            temp.setLink(toAdd);
        }
    }

    public void display() {
        if(head == null) {
            System.out.println("The list is empty.");
        } else {
            Node temp = head;
            while(temp != null) {
                System.out.print(temp.getData() + " ");
                temp =  temp.getLink();
            }
        }
    }

    public int size() {
        if(head == null) {
            return 0;
        } else {
            int count = 0;
            Node temp = head;
            while(temp != null) {
                temp = temp.getLink();
                count++;
            }
            return count;
        }
    }
    
}

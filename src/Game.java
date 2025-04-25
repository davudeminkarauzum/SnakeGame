import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import enigma.event.TextMouseEvent;
import enigma.event.TextMouseListener;
import enigma.core.Enigma;


public class Game {
    String choice;
    enigma.console.Console cn = Enigma.getConsole("Snake Game",80,30);
    
    private Scanner scanner;
    
    boolean isAlive = true;
    
    public KeyListener klis; 
 
    public int keypr;   // key pressed?
    public int rkey;    // key   (for press/release)

   
    public Game()throws Exception
    {
        
    scanner = new Scanner(System.in);
    klis=new KeyListener() {
        public void keyTyped(KeyEvent e) {}
        public void keyPressed(KeyEvent e) {
            if(keypr==0) {
                keypr=1;
                rkey=e.getKeyCode();
            }
        }
        public void keyReleased(KeyEvent e) {}
    };

    
    long lastTime = System.currentTimeMillis();
        long timeUnit = 100;
    
    cn.getTextWindow().setCursorPosition(25, 0);
    cn.getTextWindow().output("Welcome to the snake game");
    cn.getTextWindow().setCursorPosition(25, 1);
    cn.getTextWindow().output("To start the game press number 1");
    cn.getTextWindow().setCursorPosition(25, 2);
    cn.getTextWindow().output("To exit the game press number 2\n");
        
    cn.getTextWindow().setCursorPosition(50, 3);
    choice = scanner.nextLine();

    if(choice.equals("1"))
    {
        clearscreen();
        GameField gameField= new GameField(cn);
        while(isAlive)
        {

            
            cn.getTextWindow().addKeyListener(klis);
           
             // ----------------------------------------------------
             int px=5,py=5;
             
             cn.getTextWindow().output(px,py,'P');
             while(true) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastTime >= timeUnit) {
                if (keypr == 1) {
                    if (rkey == KeyEvent.VK_LEFT && !gameField.isWall(px - 1, py)) {
                        cn.getTextWindow().output(px, py, ' ');
                        px--;
                        cn.getTextWindow().output(px, py, 'P');
                    }
                    if (rkey == KeyEvent.VK_RIGHT && !gameField.isWall(px + 1, py)) {
                        cn.getTextWindow().output(px, py, ' ');
                        px++;
                        cn.getTextWindow().output(px, py, 'P');
                    }
                    if (rkey == KeyEvent.VK_UP && !gameField.isWall(px, py - 1)) {
                        cn.getTextWindow().output(px, py, ' ');
                        py--;
                        cn.getTextWindow().output(px, py, 'P');
                    }
                    if (rkey == KeyEvent.VK_DOWN && !gameField.isWall(px, py + 1)) {
                        cn.getTextWindow().output(px, py, ' ');
                        py++;
                        cn.getTextWindow().output(px, py, 'P');
                    }
                
                
                    keypr = 0;
                    
                } 
                lastTime = currentTime;

                }
                Thread.sleep(20);
             }

        }


    }
    

    
}
public void clearscreen()
{
for (int i=0;i<80;i++)
{
    for (int z=0;z<30;z++)
    {
        cn.getTextWindow().output(i, z, ' ');


    }
}


}

    
}


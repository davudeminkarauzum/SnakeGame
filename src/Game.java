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
    enigma.console.Console cn = Enigma.getConsole("Snake Game",80,15);
    
    private Scanner scanner;
    Random random = new Random();
    
    boolean isAlive = true;
    
    public KeyListener klis; 
 
    public int keypr;   // key pressed?
    public int rkey;    // key   (for press/release)
    
    public void clearscreen(){ // ekranın temizlenmesi
        for (int i=0;i<80;i++){
            for (int z=0;z<30;z++){
                cn.getTextWindow().output(i, z, ' ');
            }
        }
    }  
    
    // oyundaki skor değerleri
    int time = 0;   
    int energy = 500;  
    int life = 1000;
    int trap = 0;
    int playerscore = 0;    
    int sRobot = 0;
    int computerscore = 0;
    
    // Oyuncunun ilk pozisyonu
    int px=5, py=5;
       
    void updateStatusPanel() { // skor değerlerinin güncellenip ekrana yazdırılması
    	
    	String timeText =  "" + time;
    	String energyText =  "" + energy;
    	String lifeText =   "" + life;
    	String trapText =   "" + trap;
    	String playerscoreText =  "" + playerscore;
    	String sRobotText = "" + sRobot;
        String computerscoreText =  "" + computerscore;
   
    	writeToMap(5, 66, timeText);
        writeToMap(8, 66, energyText); 
        writeToMap(9, 66, lifeText);
        writeToMap(10, 66, trapText);
        writeToMap(11, 66, playerscoreText);
        writeToMap(16, 66, sRobotText);
        writeToMap(17, 66, computerscoreText);
    }
   
    void writeToMap(int row, int col, String text) { // yardımcı fonksiyon
        for (int i = 0; i < text.length(); i++) {
            GameField.map[row][col + i] = text.charAt(i);
        }
    }
    
    void collectTreasures(int newPx, int newPy) { // treasure yedikten sonra puanlarını ekleyen fonksiyon
    	
    	if(GameField.map[newPx][newPy] == '1') {
    		playerscore += 1;
    		energy += 50;
    	}
    	
    	if(GameField.map[newPx][newPy] == '2') {
    		playerscore += 4;
    		energy += 150;
    	}
    	
    	if(GameField.map[newPx][newPy] == '3') {
    		playerscore += 16;
    		energy += 250;
    	}
    	
    	if(GameField.map[newPx][newPy] == '@') { 
    		trap++;
    	}
    	
    	if(GameField.map[newPx][newPy] == 'S') {
    		playerscore += 200;
    		energy += 500;
    	}   	
    }
    
    void movePlayer(int newPx, int newPy) { // oyuncunun hareketi fonksiyon haline getirdim
        GameField.map[px][py] = ' ';
        px = newPx;
        py = newPy;
        GameField.map[px][py] = 'P';
    }
       
   void updateGameBoard() { // tahtayı güncelleyen fonksiyon
    	
    	updateStatusPanel(); // değerlerin güncellenmesi       
        clearscreen();   // ekranın temizlenmesi
        cn.getTextWindow().setCursorPosition(0, 0); // cursoru ayarla
        GameField.printScreen();    	
    }
       
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
        
    long lastInputTime = System.currentTimeMillis();
        long inputInterval = 2000; // 2 saniye
        
    long lastSecondTime = System.currentTimeMillis();
         long secondInterval = 1000; // 1 saniye
    
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
                                     
             GameField.map[px][py] = 'P';
             
             for(int i = 0; i < 30; i++) // oyunun başında tahtaya input queue'dan 30 element boşalt
                gameField.unloadInputQueue();
             
             updateGameBoard();             
             
             while(true) {

                long currentTime = System.currentTimeMillis();

                if (currentTime - lastTime >= timeUnit) {
                if (keypr == 1) {
                	
                    if (rkey == KeyEvent.VK_LEFT && !gameField.isWall(px, py - 1)) {
                    	energy -= 1;
                    	collectTreasures(px, py - 1);
                    	movePlayer(px, py - 1);
                    }
                    if (rkey == KeyEvent.VK_RIGHT && !gameField.isWall(px, py + 1)) {
                    	energy -= 1;
                    	collectTreasures(px, py + 1);
                    	movePlayer(px, py + 1);
                    }
                    if (rkey == KeyEvent.VK_UP && !gameField.isWall(px - 1, py)) { 
                    	energy -= 1;
                    	collectTreasures(px - 1, py);
                    	movePlayer(px - 1, py);
                    }
                    if (rkey == KeyEvent.VK_DOWN && !gameField.isWall(px + 1, py)) {
                    	energy -= 1;
                    	collectTreasures(px + 1, py);
                    	movePlayer(px + 1, py);
                    }

                    if(rkey == KeyEvent.VK_SPACE) {                    	                                       	
                    	if(trap > 0) {
                    		
                    	trap--;                      	
                    	int randomx;
                    	int randomy;
                    	                    	
                    	do {                  		
                    		randomx = 0;
                    		randomy = 0;
                    		
                    		int randommove = random.nextInt(4);
                    		
                    		switch(randommove) {
                    		
                    		case(0): { randomx = -1; break; }
                    		case(1): { randomx = 1; break; }
                    		case(2): { randomy = -1; break; }
                    		case(3): { randomy = 1; break; }                     		
                    		}                  	                    	
                    	}while(gameField.isWall(px + randomx, py + randomy));
                                       	
                    	int oldPx = px;
                    	int oldPy = py;

                        energy -= 1;
                    	collectTreasures(px + randomx, py + randomy);
                    	movePlayer(px + randomx, py + randomy);
                    	GameField.map[oldPx][oldPy] = '=';                    	                	                   	                  	                	
                    	}                    	
                    }
                    
                    updateGameBoard();  
                   
                    keypr = 0;                   
                } 
                lastTime = currentTime;                
               }
                          
                if (currentTime - lastInputTime >= inputInterval)  { // 2 saniyede bir input Queue'dan eleman yerleştirilmesi
                    gameField.unloadInputQueue();                   
                    updateGameBoard();                  
                    lastInputTime = currentTime;
                }
                
                if (currentTime - lastSecondTime >= secondInterval) { // her saniyede time değişkeninin arttırılması
                    time++;
                    updateGameBoard();    
                    lastSecondTime = currentTime;
                }
                            
                Thread.sleep(20);
             }
        }
     }
  }
}

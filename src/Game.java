import java.util.Scanner;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import enigma.core.Enigma;
import java.util.Random;

public class Game {
    String choice;
    static enigma.console.Console cn = Enigma.getConsole("Snake Game",80,30);
    
    private Scanner scanner;
    
    boolean isAlive = true;
    boolean arrived = false;
    
    Stack pathfinding = new Stack(500);
    
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
    
    //Coordinates of player and computer
    int px = 0, py = 0, cx = 0, cy = 0 ;
       
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
    
    void moveComputer(int newPx, int newPy) { // oyuncunun hareketi fonksiyon haline getirdim
        GameField.map[cx][cy] = ' ';
        cx = newPx;
        cy = newPy;
        GameField.map[cx][cy] = 'C';
    }
       
    void updateGameBoard() { // tahtayı güncelleyen fonksiyon
    	
    	updateStatusPanel(); // değerlerin güncellenmesi
        GameField.loadMapToText(); // oyun haritasının text'e aktarılması
        clearscreen();   // ekranın temizlenmesi
        cn.getTextWindow().setCursorPosition(0, 0); // cursoru ayarla
        GameField.loadTextToScreen(); // text'i ekrana yazdır.
    	
    }
       
    public Game()throws Exception
    {
        
    scanner = new Scanner(System.in);
    klis = new KeyListener() {
        public void keyTyped(KeyEvent e) {}
        public void keyPressed(KeyEvent e) {
            if(keypr == 0) {
                keypr = 1;
                rkey = e.getKeyCode();
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
    
    Random rnd = new Random();

    if(choice.equals("1"))
    {
        clearscreen();
        GameField gameField= new GameField(cn);
        
        while(!(GameField.map[py][px] == ' ' && GameField.map[cy][cx] == ' ')) {
        	if(GameField.map[py][px] != ' ') {
            	px = rnd.nextInt(1,55);
            	py = rnd.nextInt(1,22);
        	}
        	if(GameField.map[cy][cx] != ' ') {
            	cx = rnd.nextInt(1,55);
            	cy = rnd.nextInt(1,22);
        	}
        }
        cn.getTextWindow().output(px , py, 'P');   cn.getTextWindow().output(cx , cy, 'C'); 
        
        while(isAlive)
        {
            cn.getTextWindow().addKeyListener(klis);
            
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
                    
                    updateGameBoard();  
                   
                    keypr = 0;    
                } 
                
                if(arrived == false) {   
                    while(!pathfinding.isEmpty()) {
                   	    String way = (String) pathfinding.pop();
                     	if(way.equals("R")) { // Right
                    		moveComputer(cx, cy + 1);
                    	} else if(way.equals("D")) { //Down
                    		moveComputer(cx + 1, cy);
                    	} else if(way.equals("L")) { //Left
                    	    moveComputer(cx, cy - 1);
                    	} else if(way.equals("U")) {  //Up
                    		moveComputer(cx - 1, cy);
                    	}
                    }
                    arrived = true;
                } else {
                    char[][] tempMap= new char[23][55];
                    
                    for(int i = 0; i < 23; i++) {
                        for(int j = 0; j < 55; j++) {
                        	tempMap[i][j] = GameField.map[i][j] ;
                        }
                    }
                    pathfinding = findPath(tempMap, cx, cy);
                    arrived = false;
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
  
    public static Stack findPath(char[][] map, int cx, int cy) {

        Random rnd = new Random();
        int targetedX = 0, targetedY = 0;
        
        while (!(map[targetedY][targetedX] == '1' || 
        		 map[targetedY][targetedX] == '2' || 
                 map[targetedY][targetedX] == '3' || 
                 map[targetedY][targetedX] == '@' || 
                 map[targetedY][targetedX] == 'S')) {
            targetedX = rnd.nextInt(1, 55);
            targetedY = rnd.nextInt(1, 22); 
        }

        boolean[][] visited = new boolean[map[0].length][map[1].length];
        int[][][] parent = new int[map[0].length][map[1].length][2]; 

        Stack stack = new Stack(500);
        stack.push(new int[]{cx, cy});

        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}}; // right, down, left, up

        while (!stack.isEmpty()) {
            int[] current = (int[]) stack.pop();
            int x = current[0], y = current[1];

            if (x < 0 || y < 0 || x >= map[0].length || y >= map[1].length || visited[y][x] || map[y][x] == '#')
                continue;

            visited[x][y] = true;

            if (x == targetedX && y == targetedY) {
                Stack result = new Stack(500);
                
                while (!(targetedX == cx && targetedY == cy)) {
                    int tempX = parent[targetedY][targetedX][0];
                    int tempY = parent[targetedY][targetedX][1];

                    if (targetedX - tempX == 0 && targetedY - tempY == 1) {
                        result.push("D"); // Down
                    } else if (targetedX - tempX == 0 && targetedY - tempY == -1) {
                        result.push("U"); // Up
                    } else if (targetedX - tempX == 1 && targetedY - tempY == 0) {
                        result.push("R"); // Right
                    } else if (targetedX - tempX == -1 && targetedY - tempY == 0) {
                        result.push("L"); // Left
                    }
                    
                    if (!(tempX == cx && tempY == cy)) { 
                    	cn.getTextWindow().output(cx, cy, ' ');
                        GameField.map[tempY][tempX] = '.';
                    }
                    
                    targetedX = tempX;
                    targetedY = tempY;
                }
                return result;
            }

            for (int i = 0; i < 4; i++) {
                int nx = x + directions[i][0];
                int ny = y + directions[i][1];

                if (nx >= 0 && ny >= 0 && nx < map[0].length && ny < map[1].length && !visited[ny][nx] && map[ny][nx] != ' ') {
                    visited[ny][nx] = true;
                    stack.push(new int[]{nx, ny});
                    parent[ny][nx][0] = x;
                    parent[ny][ny][1] = y;
                }
            }
        }

        return new Stack(500);
    }

}

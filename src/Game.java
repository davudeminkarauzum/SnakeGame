
import java.util.Scanner;
import java.util.Random;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import enigma.core.Enigma;

public class Game {
    String choice;
    enigma.console.Console cn = Enigma.getConsole("Snake Game",80,15);
    
    private Scanner scanner;
    Random random = new Random();
    
    boolean playersMove = false;
    boolean computersMove = false;
    boolean robotSMove = false;
    
    public KeyListener klis; 
 
    public int keypr; // key pressed?
    public int rkey; // key (for press/release)
    public int tempkey;
    
    public void clearscreen(){ // ekranın temizlenmesi
        for (int i=0;i<80;i++){
            for (int z=0;z<30;z++){
                cn.getTextWindow().output(i, z, ' ');
            }
        }
    }  
    
    //Creating necessary variables
    int time = 0;   
    int energy = 500;  
    int life = 1000;
    int trap = 0;
    int playerscore = 0;    
    public static int robotSCounter = 0;
    int computerscore = 0;
    
    int px = 0, py = 0, cx = 0, cy = 0;
    Stack pathfinding = new Stack(500);
    public static SingleLinkedList[] robotS = new SingleLinkedList[15];
    public static int[] robotSX = new int[15];     public int[] robotSTargetedX = new int[15];
    public static int[] robotSY = new int[15];     public int[] robotSTargetedY = new int[15];
       
    void updateStatusPanel() { // skor değerlerinin güncellenip ekrana yazdırılması
    	
    	String timeText =  "" + time;
    	String energyText =  "" + energy;
    	String lifeText =   "" + life;
    	String trapText =   "" + trap;
    	String playerscoreText =  "" + playerscore;
    	String sRobotText = "" + robotSCounter;
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
    	for(int i = 0; i < text.length() + 3; i++) {
    		GameField.map[row][col + i] = ' ';
    	}
        for (int i = 0; i < text.length(); i++) {
            GameField.map[row][col + i] = text.charAt(i);
        }
    }
    
    void collectTreasures(int newPx, int newPy) { // treasure yedikten sonra puanlarını ekleyen fonksiyon
    	
    	if(GameField.map[newPx][newPy] == '1') {
    		if(playersMove) {
    		    playerscore += 1;
    		    energy += 50; }
    		else
    		    computerscore += 1;
    	}
    	
    	if(GameField.map[newPx][newPy] == '2') {
    		if(playersMove) {
    		    playerscore += 4;
    		    energy += 150; 
    		} else
        	    computerscore += 4;
    	}
    	
    	if(GameField.map[newPx][newPy] == '3') {
    		if(playersMove) {
    		    playerscore += 16;
    		    energy += 250; 
    		} else
                computerscore += 16;
    	}
    	
    	if(GameField.map[newPx][newPy] == '@') { 
    		if(playersMove) 
    		    trap++;
    		else
                computerscore += 50;
    	}   	
    }

    void move(int newX, int newY, int i) {
    	if(playersMove) {
            GameField.map[px][py] = ' ';
            px = newX;     py = newY;
            GameField.map[px][py] = 'P';
        	if(energy > 0) { //Spending energy
        		energy--;
        	}
    	} else if(robotSMove) {
    		GameField.map[robotSX[i]][robotSY[i]] = ' ';
    		robotSX[i] = newX;     robotSY[i] = newY;
    		GameField.map[robotSX[i]][robotSY[i]] = 'S';
    	} else if(computersMove) {
            GameField.map[cx][cy] = ' ';
            cx = newX;     cy = newY;
            GameField.map[cx][cy] = 'C';
    	}
    }
     
   public void updateGameBoard() { // tahtayı güncelleyen fonksiyon
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
    
    int gametiming = 0;    
    
    cn.getTextWindow().setCursorPosition(25, 0);
    cn.getTextWindow().output("Welcome to the snake game");
    cn.getTextWindow().setCursorPosition(25, 1);
    cn.getTextWindow().output("To start the game press number 1");
    cn.getTextWindow().setCursorPosition(25, 2);
    cn.getTextWindow().output("To exit the game press number 2\n");
        
    cn.getTextWindow().setCursorPosition(50, 3);
    choice = scanner.nextLine();

    if(choice.equals("1")) {
        clearscreen();
        GameField gameField= new GameField(cn);
        while(life > 0) {
            cn.getTextWindow().addKeyListener(klis);
                                     
            while(!(GameField.map[px][py] == ' ' && GameField.map[cx][cy] == ' ')) {
            	if(GameField.map[px][py] != ' ') {
                	px = random.nextInt(1,22);
                	py = random.nextInt(1,55);
            	}
            	if(GameField.map[cx][cy] != ' ') {
                	cx = random.nextInt(1,22);
                	cy = random.nextInt(1,55);
            	}
            }
            GameField.map[px][py] = 'P';   GameField.map[cx][cy] = 'C';
             
             for(int i = 0; i < 30; i++) // oyunun başında tahtaya input queue'dan 30 element boşalt
                gameField.unloadInputQueue();
                          
             while(true) {
            	 
             updateGameBoard();  
                     
                if (energy > 0 || gametiming % 2 == 0) {  // 0.1 ya da 0.2 saniye 
                	
                    if (keypr == 1) {
                    	
                     playersMove = true;

                        if (rkey == KeyEvent.VK_LEFT && !gameField.isWall(px, py - 1) && !gameField.isCrashedRobots(px, py - 1)) {
                        	collectTreasures(px, py - 1);
                        	move(px, py - 1, 0);
                        	tempkey = rkey;
                        }
                        if (rkey == KeyEvent.VK_RIGHT && !gameField.isWall(px, py + 1) && !gameField.isCrashedRobots(px, py + 1)) {
                        	collectTreasures(px, py + 1);
                        	move(px, py + 1, 0);
                        	tempkey = rkey;
                        }
                        if (rkey == KeyEvent.VK_UP && !gameField.isWall(px - 1, py) && !gameField.isCrashedRobots(px - 1, py)) { 
                        	collectTreasures(px - 1, py);
                        	move(px - 1, py, 0);
                        	tempkey = rkey;
                        }
                        if (rkey == KeyEvent.VK_DOWN && !gameField.isWall(px + 1, py) && !gameField.isCrashedRobots(px + 1, py)) {
                        	collectTreasures(px + 1, py);
                        	move(px + 1, py, 0);
                        	tempkey = rkey;
                        }
                    
                        if (rkey == KeyEvent.VK_SPACE) {
                            if (trap > 0) {

                                trap--;
                                int randomx = 0, randomy = 0;

                                int lastmove = 4;

                                if (tempkey == KeyEvent.VK_RIGHT && !gameField.isWall(px, py + 1)) {
                                    randomy = 1;
                                } else if (tempkey == KeyEvent.VK_LEFT && !gameField.isWall(px, py - 1)) {
                                    randomy = -1;
                                } else if (tempkey == KeyEvent.VK_UP && !gameField.isWall(px - 1, py)) {
                                    randomx = -1;
                                } else if (tempkey == KeyEvent.VK_DOWN && !gameField.isWall(px + 1, py)) {
                                    randomx = 1;
                                } else {
                                	
                                   do {
                                	   randomx = 0;
                               		   randomy = 0;
                               		   
                                        lastmove = random.nextInt(4);
                                        switch (lastmove) {

                                            case (0): {
                                                randomx = -1;
                                                break;
                                            }
                                            case (1): {
                                                randomx = 1;
                                                break;
                                            }
                                            case (2): {
                                                randomy = -1;
                                                break;
                                            }
                                            case (3): {
                                                randomy = 1;
                                                break;
                                            }
                                        }
                                    } while (gameField.isWall(px + randomx, py + randomy));
                                }

                                int oldPx = px;
                                int oldPy = py;

                                collectTreasures(px + randomx, py + randomy);
                                move(px + randomx, py + randomy, 0);
                                GameField.map[oldPx][oldPy] = '=';
                            }                            
                        }
                        
                        playersMove = false;
                        keypr = 0;                     
                    }                  
                }
                
              if (gametiming % 4 == 0) { // computer move (0.4 saniye)
            	        	    
            	  computersMove = true;
            	  boolean moved = false;
          	      if(!pathfinding.isEmpty()) {
            	    	String way = (String) pathfinding.peek();
                        if (way.equals("R") && GameField.map[cx][cy + 1] != 'P') {
                        	collectTreasures(cx, cy + 1);
                            move(cx, cy + 1, 0);
                            moved = true;
                        } else if (way.equals("D") && GameField.map[cx + 1][cy] != 'P') {
                      	    collectTreasures(cx + 1, cy);
                            move(cx + 1, cy, 0);
                            moved = true;
                        } else if (way.equals("L") && GameField.map[cx][cy - 1] != 'P') {
                      	    collectTreasures(cx, cy - 1);
                            move(cx, cy - 1, 0);
                            moved = true;
                        } else if (way.equals("U") && GameField.map[cx - 1][cy] != 'P') {
                      	    collectTreasures(cx - 1, cy);
                            move(cx - 1, cy, 0);
                            moved = true;
                        } 
                        
                        if(moved) {
                        	pathfinding.pop();
                        }
          	       } else {
                      char[][] tempMap= new char[23][55];
                      
                      for(int i = 0; i < 23; i++) {
                          for(int j = 0; j < 55; j++) {
                          	tempMap[i][j] = GameField.map[i][j] ;
                          }
                      }
                      pathfinding = findPath(tempMap, cx, cy);
                    }
           
          	        computersMove = false;
                }
                                      
        	  /*for(int i = 0; i < robotSCounter; i++) {
        		  if (currentTime - lastRobotSTime >= robotSInterval) { 
        			  robotSMove = true;
        			  if(robotSTargetedX[i] == robotSX[i] && robotSTargetedY[i] == robotSY[i]) { // If this S robot would arrived the target
        				  while(!(GameField.map[robotSTargetedX[i]][robotSTargetedY[i]] == '1' || 
        					    GameField.map[robotSTargetedX[i]][robotSTargetedY[i]] == '2' || 
        						GameField.map[robotSTargetedX[i]][robotSTargetedY[i]] == '3')) { //Targeting treasure randomly
        					  robotSTargetedX[i] = random.nextInt(1, 22);
        					  robotSTargetedY[i] = random.nextInt(1, 55);
        				  }
        			  } else {
                		  if(gameField.isStuck(robotSX[i], robotSY[i])) { //Random moving mode
                			  while(gameField.isStuck(robotSX[i], robotSY[i])) {
                				  int probability = random.nextInt(100) + 1;
                			  }
                		  } else { //Targeted moving mode
                			  
                		  }
        			  }
                	  robotSMove = false;
                	  lastRobotSTime = currentTime;
        		  }
        	  }*/
            
                if (gametiming % 20 == 0)  { // 2 saniyede bir input Queue'dan eleman yerleştirilmesi
                    gameField.unloadInputQueue();                   
                }
                
                if (gametiming % 10 == 0) { // her saniyede time değişkeninin arttırılması
                    time++;
                }
                
                if(gametiming % 1 == 0) { 
                	if(GameField.map[px + 1][py] == 'C' || GameField.map[px - 1][py] == 'C'
                			|| GameField.map[px][py + 1] == 'C' || GameField.map[px][py - 1] == 'C') {
                		life -= 30;
                	} else if(GameField.map[px + 1][py] == 'S' || GameField.map[px - 1][py] == 'S'
                			|| GameField.map[px][py + 1] == 'S' || GameField.map[px][py - 1] == 'S') {
                		life -= 1;
                	}
                	
                }       

                try {
                    Thread.sleep(100); // 1 time unit
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                gametiming++;  // 0.1 saniyede bir artar
                
             }
        }
     }
  }
    
    public static Stack findPath(char[][] map, int cx, int cy) {
        Random rnd = new Random();
        int targetedX = 0, targetedY = 0;

        while (!(map[targetedX][targetedY] == '1' || 
                 map[targetedX][targetedY] == '2' || 
                 map[targetedX][targetedY] == '3' || 
                 map[targetedX][targetedY] == '@')) {
            targetedX = rnd.nextInt(1, map.length);
            targetedY = rnd.nextInt(1, map[0].length); 
        }

        boolean[][] visited = new boolean[map.length][map[0].length];
        int[][][] parent = new int[map.length][map[0].length][2];

        Stack stack = new Stack(500);
        stack.push(new int[]{cx, cy});

        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}}; //right, up, left, down

        while (!stack.isEmpty()) {
            int[] current = (int[]) stack.pop();
            int x = current[0], y = current[1];

            if (x < 0 || y < 0 || x >= map.length || y >= map[0].length|| visited[x][y] || map[x][y] == '#') {
            	continue;
            }

            visited[x][y] = true;

            if (x == targetedX && y == targetedY) {
            	break;
            }

            for (int i = 0; i < 4; i++) {
                int nx = x + directions[i][0], ny = y + directions[i][1];

                if (nx >= 0 && ny >= 0 && nx < map.length && ny < map[0].length &&
                    !visited[nx][ny] && map[nx][ny] != '#') {
                    stack.push(new int[]{nx, ny});
                    parent[nx][ny][0] = x;
                    parent[nx][ny][1] = y;
                }
            }
        }

        Stack result = new Stack(500);

        while (!(targetedX == cx && targetedY == cy)) {
            int tempX = parent[targetedX][targetedY][0], tempY = parent[targetedX][targetedY][1];

            if (targetedX - tempX == 0 && targetedY - tempY == 1) { //Right
            	result.push("R");
            }
            else if (targetedX - tempX == 0 && targetedY - tempY == -1) { // Left
            	result.push("L");
            }
            else if (targetedX - tempX == 1 && targetedY - tempY == 0) { //Down
            	result.push("D");
            }
            else if (targetedX - tempX == -1 && targetedY - tempY == 0) { //Up
            	result.push("U");
            }

            targetedX = tempX;    targetedY = tempY;           

        }

        return result;
    }
}

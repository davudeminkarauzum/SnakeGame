import enigma.console.TextAttributes;
import java.awt.Color;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class GameField {
	
    InputQueue inputQueue = new InputQueue();

    public static char[][] map = new char[23][80]; //We added parameters in next to the map to y-axis
    public static enigma.console.Console cn;
    
    public GameField(enigma.console.Console console)
    {
        cn = console;

        try {
            BufferedReader reader = new BufferedReader(new FileReader("maze.txt"));
            String s;
            int row = 0;
            while ((s = reader.readLine()) != null && row < map.length) {
                for (int col = 0; col < s.length() && col < map[0].length; col++) {
                    char c = s.charAt(col);
                    map[row][col] = c;
                    cn.getTextWindow().output(col, row, c);
                }
                row++;
            }
            reader.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void printScreen() {
        for (int i = 0; i < 23; i++) {
            for (int j = 0; j < 80; j++) {
                char ch = map[i][j];

                TextAttributes attrs;

                if(j < 55) {
                    if (ch == 'P' && j < 55) {
                        attrs = new TextAttributes(Color.WHITE, Color.BLUE);
                    } else if (ch == 'C' && j < 55) {
                        attrs = new TextAttributes(Color.BLACK, Color.GREEN);
                    } else if(ch == 'S') {
                    	attrs = new TextAttributes(Color.BLACK, Color.RED);
                    } else if (ch == '#') {
                        attrs = new TextAttributes(Color.GRAY, Color.GRAY); 
                    } else if (ch == '=') {
                        attrs = new TextAttributes(Color.BLACK, Color.GREEN); 
                    } else if (ch == 'X') {
                        attrs = new TextAttributes(Color.BLACK, Color.WHITE); 
                    } else if(ch == '.') {
                    	attrs = new TextAttributes(Color.BLUE, Color.WHITE);
                    } else if(isTreasure(i, j) || ch == '@'){
                    	attrs = new TextAttributes(Color.RED, Color.WHITE);
                    } else {
                    	attrs = new TextAttributes(Color.BLACK, Color.WHITE);
                    }
                } else {
                	attrs = new TextAttributes(Color.WHITE, Color.BLACK);
                }
                
                cn.getTextWindow().output(j, i, ch, attrs); 
            }
        }
    }


    public boolean isWall(int x, int y) {
        return map[x][y] == '#';
    }

    public boolean isCrashedRobotsOrActiveTrap(int x, int y) {
    	return map[x][y] == 'C' || Snake.isCrashedSnake(x, y) || map[x][y] == '=';
    }
    
    public static boolean isTreasure(int x, int y) {
    	return (map[x][y] == '1' || map[x][y] == '2' || map[x][y] == '3') && !Snake.isCrashedSnake(x, y);
    }
    

    public void unloadInputQueue() {
        Random random = new Random();
        boolean isInserted = false;
        char c = ' ';
        while (!isInserted) {
            int x = random.nextInt(23), y = random.nextInt(55); //Selects the coordinates randomly

            if (map[x][y] == ' ' || map[x][y] == '.') { //Inserts the treasure this coordinate
                Object next = inputQueue.dequeueInput();
                c = next.toString().charAt(0);

                map[x][y] = c;

                isInserted = true;
                
                if (c == 'S'){
                    Snake snake = new Snake(x,y);
                    Game.snakeCounter++;
                    Game.snakes.enqueue(snake);
                }

                inputQueue.writeElements(); //Printing InputQueue after inserting this treasure
            } 
            
        }
    }
}

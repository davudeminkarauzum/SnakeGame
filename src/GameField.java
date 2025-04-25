import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import enigma.core.Enigma;

public class GameField {
    
    public static char[][] map=new char[23][55];
    enigma.console.Console cn;

    public GameField(enigma.console.Console console)
    {
        this.cn = console; 

        
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
    public boolean isWall(int x, int y) {
        return map[y][x] == '#';
    }
    
}

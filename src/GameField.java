import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import enigma.core.Enigma;

public class GameField {

    InputQueue inputQueue = new InputQueue();

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
    public void addRandomInputQueue() {
        Random random = new Random();
             boolean isInserted = false;

            while(!isInserted) {

                int x = random.nextInt(23);
                int y = random.nextInt(55); // rastgele koordinat belirlenmesi

                if(map[x][y] == ' ') { // koordinat boşsa

                    Object next = inputQueue.dequeueInput();
                    char c = next.toString().charAt(0);

                    cn.getTextWindow().setCursorPosition(y, x);
                    cn.getTextWindow().output(c);  // o koordinata inputQueue elemanını yazdır

                    map[x][y] = c; // elementleri bir daha aynı yere eklememek için mazeMap'i de güncellemeliyiz

                    isInserted = true; // element başarıyla yerleşti

                    cn.getTextWindow().setCursorPosition(58, 2);
                    cn.getTextWindow().output("                 "); // Queue üst üste yazılmasın diye önce boşluk koy
                    cn.getTextWindow().setCursorPosition(58, 2);
                    cn.getTextWindow().output(inputQueue.writeElements()); // Queue'yu tekrar yazdır
                }
            }
        }



    public void unloadInputQueue() {
	 
	 Random random = new Random();
    	
        for(int i = 0; i < 30; i++) { // oyunun başında inputQueue'dan 30 element boşalt
    	  
    	  boolean isInserted = false;
    	  
    	  while(!isInserted) {
    	
    	  int x = random.nextInt(23);  
    	  int y = random.nextInt(55); // rastgele koordinat belirlenmesi 
    	  
    	  if(map[x][y] == ' ') { // koordinat boşsa 
    		  
    		  Object next = inputQueue.dequeueInput(); 
              char c = next.toString().charAt(0);
        
              cn.getTextWindow().setCursorPosition(y, x); 
              cn.getTextWindow().output(c);  // o koordinata inputQueue elemanını yazdır
              
              map[x][y] = c; // elementleri bir daha aynı yere eklememek için mazeMap'i de güncellemeliyiz
              
              isInserted = true; // element başarıyla yerleşti
              
              cn.getTextWindow().setCursorPosition(58, 2);
        	  cn.getTextWindow().output("                 "); // Queue üst üste yazılmasın diye önce boşluk koy
           	  cn.getTextWindow().setCursorPosition(58, 2);
        	  cn.getTextWindow().output(inputQueue.writeElements()); // Queue'yu tekrar yazdır
    	  }	  
       }     	     	    
     } 	
  }
    
}

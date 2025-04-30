package snakegame;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import enigma.core.Enigma;

public class GameField {

    InputQueue inputQueue = new InputQueue();

    public static char[][] map = new char[23][80]; // map arrayini oyun parametrelerini de göstermesi için genişlettim.
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
    
    
    public static void loadMapToText() {  // önce map arrayini ekran.txt'e atarız
    	
    	try {
            FileWriter writer = new FileWriter("ekran.txt", false);

            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    writer.write(map[i][j]);
                }
                writer.write("\n");
            }
            writer.close();
           
        } catch (IOException e) {
        	e.printStackTrace();
        }    	    	  	
    }
 
     public static void loadTextToScreen() {  // sonra ekran.txt'i yazdırırız.	
    	
    	 try {
             BufferedReader reader = new BufferedReader(new FileReader("ekran.txt"));
             String s;

             while ((s = reader.readLine()) != null) {
                 System.out.println(s);
             }
             reader.close();
         } catch (IOException e) {
         	e.printStackTrace();

         }
    }
    
         
    public boolean isWall(int x, int y) {
        return map[x][y] == '#';
    }  
    
    
    public void unloadInputQueue() {
	 
	 Random random = new Random();
    		  
    	  boolean isInserted = false;
    	  
    	  while(!isInserted) {
    	
    	  int x = random.nextInt(23);  
    	  int y = random.nextInt(55); // rastgele koordinat belirlenmesi 
    	  
    	  if(map[x][y] == ' ') { // koordinat boşsa 
    		  
    		  Object next = inputQueue.dequeueInput(); 
              char c = next.toString().charAt(0);
                       
              map[x][y] = c; // elementleri bir daha aynı yere eklememek için mazeMap'i de güncellemeliyiz
              
              isInserted = true; // element başarıyla yerleşti
              
              inputQueue.writeElements(); // Queue'yu tekrar yazdır. 
    	  }	  
       }     	     	    
      	
  }
    
}

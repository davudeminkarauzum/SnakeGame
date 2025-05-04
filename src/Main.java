import enigma.console.Console;
import enigma.console.TextAttributes;
import enigma.console.TextWindow;
import enigma.console.TextWindowNotAvailableException;
import enigma.core.Enigma;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Random;


public class Main {
    public static void main(String[] args) {
    	
      Random random = new Random();
      char[][] mazeMap = new char[23][55]; // oyun haritasındaki elementleri saklayan iki boyutlu array
      
      enigma.console.Console cn = Enigma.getConsole("Snake Game");  

      try {
          BufferedReader reader = new BufferedReader(new FileReader("maze.txt"));
          String s = reader.readLine();;
          int row = 0; // mazeMap satırı. 

          while(s != null && !s.trim().isEmpty()) {
       	  
        	System.out.println(s);
           
              s = s.substring(0, 55); // yazdırdığımız string'i şimdi array'e yerleştirmek için kırparız
              
              for (int j = 0; j < 55; j++)  // mazeMap'in row. satırına maze.txt'in satırlarını yerleştirir.                  
                   mazeMap[row][j] = s.charAt(j);                
                       
              s = reader.readLine();
              row++; 
          }

          reader.close();
      } catch (IOException e) {
          e.printStackTrace();
      }
              
        InputQueue inputQueue=new InputQueue();
        
        cn.getTextWindow().setCursorPosition(58, 2);
  	    cn.getTextWindow().output(inputQueue.writeElements());
            
        for(int i = 0; i < 30; i++) { // oyunun başında inputQueue'dan 30 element boşalt
    	  
    	  boolean isInserted = false;
    	  
    	  while(!isInserted) {
    	
    	  int x = random.nextInt(23);  
    	  int y = random.nextInt(55); // rastgele koordinat belirlenmesi 
    	  
    	  if(mazeMap[x][y] == ' ') { // koordinat boşsa 
    		  
    		  Object next = inputQueue.dequeueInput(); 
              char c = next.toString().charAt(0);
        
              cn.getTextWindow().setCursorPosition(y, x); 
              cn.getTextWindow().output(c);  // o koordinata inputQueue elemanını yazdır
              
              mazeMap[x][y] = c; // elementleri bir daha aynı yere eklememek için mazeMap'i de güncellemeliyiz
              
              isInserted = true; // element başarıyla yerleşti
              
              cn.getTextWindow().setCursorPosition(58, 2);
        	  cn.getTextWindow().output("                 "); // Queue üst üste yazılmasın diye önce boşluk koy
           	  cn.getTextWindow().setCursorPosition(58, 2);
        	  cn.getTextWindow().output(inputQueue.writeElements()); // Queue'yu tekrar yazdır
    	  }
    	  
    }
    	     	     	  
    	  try { // oyun döngüsü boyunca her 2 saniyede bir element haritaya yerleştirilir. 
    		    // normalde oyunun başındaki elementleri yerleştirirken buna gerek yoktur şu an sadece göstermelik
    		    Thread.sleep(2000); 
    		} catch (InterruptedException e) {
    		    e.printStackTrace();
    		}  	  
       }
    
    }
}

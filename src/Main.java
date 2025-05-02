import enigma.console.Console;
import enigma.console.TextAttributes;
import enigma.console.TextWindow;
import enigma.console.TextWindowNotAvailableException;
import enigma.core.Enigma;

import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;


public class Main {
    public static void main(String[] args) {
      enigma.console.Console console = Enigma.getConsole("Snake Game");
        InputQueue inputQueue=new InputQueue();
<<<<<<< Updated upstream
        console.getTextWindow().output(inputQueue.writeElements());

=======
        
        cn.getTextWindow().setCursorPosition(58, 2);
  	    cn.getTextWindow().output(inputQueue.writeElements());
            
        for(int i = 0; i < 30; i++) { // oyunun başında inputQueue'dan 30 element boşalt
    	  
    	  boolean isInserted = false;
    	  
    	  while(!isInserted) {//deneme cart curt
    	
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
    	  
>>>>>>> Stashed changes
    }
}
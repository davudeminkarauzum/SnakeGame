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


public class Main {
    public static void main(String[] args) {
      enigma.console.Console cn = Enigma.getConsole("Snake Game");
        
        try {
        	BufferedReader reader = new BufferedReader(new FileReader("maze.txt"));
        	String s = reader.readLine();
        	while(!s.equals(" ")) {
        		System.out.println(s);
        		s = reader.readLine();
        	}
        	reader.close();
        } catch(IOException e) {
        	e.printStackTrace();
        }
        
        cn.getTextWindow().setCursorPosition(58, 2);
        InputQueue inputQueue=new InputQueue();
        cn.getTextWindow().output(inputQueue.writeElements());

    }
}

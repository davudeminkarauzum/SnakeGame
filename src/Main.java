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
        console.getTextWindow().output(inputQueue.writeElements());

    }
}
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import enigma.console.TextAttributes;


public class GameField {

    InputQueue inputQueue = new InputQueue();

    public static char[][] map = new char[23][80]; // map arrayini oyun parametrelerini de göstermesi için genişlettim.
    public static enigma.console.Console cn;

    private static Game mainGameReference;

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


    public boolean isWall(int x, int y) {
        return map[x][y] == '#';
    }

    public static void printScreen() {
        for (int i = 0; i < 23; i++) {
            for (int j = 0; j < 80; j++) {
                char ch = map[i][j];

                TextAttributes attrs;

                if (ch == 'P' && j < 55) {
                    attrs = new TextAttributes(Color.GREEN, Color.BLACK);
                } else if (ch == 'C' && j < 55) {
                    attrs = new TextAttributes(Color.RED, Color.BLACK);
                } else if (ch == '#') {
                    attrs = new TextAttributes(Color.WHITE, Color.BLUE); // duvar: mavi arka plan
                } else {
                    attrs = new TextAttributes(Color.WHITE, Color.BLACK); // varsayılan
                }

                cn.getTextWindow().output(j, i, ch, attrs);
            }
        }
    }

    public void unloadInputQueue() {
        Random random = new Random();
        boolean isInserted = false;
        char c = ' ';
        while (!isInserted) {
             int x = random.nextInt(23);
             int y = random.nextInt(55); // rastgele koordinat belirlenmesi

            if (map[x][y] == ' ') { // koordinat boşsa
                Object next = inputQueue.dequeueInput();
                c = next.toString().charAt(0);

                map[x][y] = c;

                isInserted = true;

                inputQueue.writeElements(); // Queue'yu tekrar yazdır
            }
            if (c == 'S'){
                Game.addNewSnake(x,y);
            }
        }

    }

    public boolean isCrashedRobots(int px, int i) {
        return false;
    }
}

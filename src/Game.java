import java.util.Scanner;
import java.util.Random;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import enigma.core.Enigma;

public class Game {
    String choice;
    enigma.console.Console cn = Enigma.getConsole("Snake Game", 80, 40);

    private Scanner scanner;
    Random random = new Random();

    boolean isAlive = true;
    boolean playersMove = false;

    public KeyListener klis;

    public int keypr; // key pressed?
    public int rkey; // key (for press/release)
    public int tempkey;

    

    // oyundaki skor değerleri
    int time = 0;
    int energy = 0;
    int life = 1000;
    int trap = 0;
    int playerscore = 0;
    int sRobot = 0;
    int computerscore = 0;

    // Oyuncunun ilk pozisyonu
    int px = 0, py = 0, cx = 0, cy = 0;
    Stack pathfinding = new Stack(500);
    boolean arrived = false;

    public Game() throws Exception {

        scanner = new Scanner(System.in);
        klis = new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                if (keypr == 0) {
                    keypr = 1;
                    rkey = e.getKeyCode();
                }
            }

            public void keyReleased(KeyEvent e) {
            }
        };

        long lastPlayerTime = System.currentTimeMillis();
        long timeUnit = 100;

        long lastInputTime = System.currentTimeMillis();
        long inputInterval = 2000; // 2 saniye

        long lastSecondTime = System.currentTimeMillis();
        long secondInterval = 1000; // 1 saniye

        long lastComputerTime = System.currentTimeMillis();
        long computerInterval = 400;

        cn.getTextWindow().setCursorPosition(25, 0);
        cn.getTextWindow().output("Welcome to the snake game");
        cn.getTextWindow().setCursorPosition(25, 1);
        cn.getTextWindow().output("To start the game press number 1");
        cn.getTextWindow().setCursorPosition(25, 2);
        cn.getTextWindow().output("To exit the game press number 2\n");

        cn.getTextWindow().setCursorPosition(25, 3);
        choice = scanner.nextLine();

        if (choice.equals("1")) {
            clearscreen();
            GameField gameField = new GameField(cn);
            while (isAlive) {
                cn.getTextWindow().addKeyListener(klis);

                while (!(GameField.map[px][py] == ' ' && GameField.map[cx][cy] == ' ')) {
                    if (GameField.map[px][py] != ' ') {
                        px = random.nextInt(1, 22);
                        py = random.nextInt(1, 55);
                    }
                    if (GameField.map[cx][cy] != ' ') {
                        cx = random.nextInt(1, 22);
                        cy = random.nextInt(1, 55);
                    }
                }
                GameField.map[px][py] = 'P';
                GameField.map[cx][cy] = 'C';

                for (int i = 0; i < 30; i++) // oyunun başında tahtaya input queue'dan 30 element boşalt
                    gameField.unloadInputQueue();

                updateGameBoard();

                while (true) {

                    long currentTime = System.currentTimeMillis();
                    if (keypr == 1) {
                        if (energy > 0) {
                            if (currentTime - lastPlayerTime >= timeUnit) {
                                playersMove = true;

                                if (rkey == KeyEvent.VK_LEFT && !gameField.isWall(px, py - 1)) {
                                    energy -= 1;
                                    collectTreasures(px, py - 1);
                                    movePlayer(px, py - 1);
                                    tempkey = rkey;
                                }
                                if (rkey == KeyEvent.VK_RIGHT && !gameField.isWall(px, py + 1)) {
                                    energy -= 1;
                                    collectTreasures(px, py + 1);
                                    movePlayer(px, py + 1);
                                    tempkey = rkey;
                                }
                                if (rkey == KeyEvent.VK_UP && !gameField.isWall(px - 1, py)) {
                                    energy -= 1;
                                    collectTreasures(px - 1, py);
                                    movePlayer(px - 1, py);
                                    tempkey = rkey;
                                }
                                if (rkey == KeyEvent.VK_DOWN && !gameField.isWall(px + 1, py)) {
                                    energy -= 1;
                                    collectTreasures(px + 1, py);
                                    movePlayer(px + 1, py);
                                    tempkey = rkey;
                                }
                            }
                        }
                        if (energy == 0) {
                            if (currentTime - lastPlayerTime >= 2 * timeUnit) {
                                playersMove = true;

                                if (rkey == KeyEvent.VK_LEFT && !gameField.isWall(px, py - 1)) {
                                    collectTreasures(px, py - 1);
                                    movePlayer(px, py - 1);
                                    tempkey = rkey;
                                }
                                if (rkey == KeyEvent.VK_RIGHT && !gameField.isWall(px, py + 1)) {
                                    collectTreasures(px, py + 1);
                                    movePlayer(px, py + 1);
                                    tempkey = rkey;
                                }
                                if (rkey == KeyEvent.VK_UP && !gameField.isWall(px - 1, py)) {
                                    collectTreasures(px - 1, py);
                                    movePlayer(px - 1, py);
                                    tempkey = rkey;
                                }
                                if (rkey == KeyEvent.VK_DOWN && !gameField.isWall(px + 1, py)) {
                                    collectTreasures(px + 1, py);
                                    movePlayer(px + 1, py);
                                    tempkey = rkey;
                                }
                            }
                        }

                        if (rkey == KeyEvent.VK_SPACE) {
                            if (trap > 0) {
                                if (energy > 0) {

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

                                    energy -= 1;
                                    collectTreasures(px + randomx, py + randomy);
                                    movePlayer(px + randomx, py + randomy);
                                    GameField.map[oldPx][oldPy] = '=';
                                }
                            }
                        }

                        updateGameBoard();
                        playersMove = false;
                        keypr = 0;

                        lastPlayerTime = currentTime;
                    }

                    if (currentTime - lastComputerTime >= computerInterval) { // computer move

                        if (arrived == false) {
                            while (!pathfinding.isEmpty()) {

                                ComputerTurn();
                            }
                            arrived = true;
                        } else {
                            char[][] tempMap = new char[23][55];

                            for (int i = 0; i < 23; i++) {
                                for (int j = 0; j < 55; j++) {
                                    tempMap[i][j] = GameField.map[i][j];
                                }
                            }
                            pathfinding = findPath(tempMap, cx, cy);
                            arrived = false;
                        }

                        updateGameBoard();
                        lastComputerTime = currentTime;

                    }

                    if (currentTime - lastInputTime >= inputInterval) { // 2 saniyede bir input Queue'dan eleman
                                                                        // yerleştirilmesi
                        gameField.unloadInputQueue();
                        updateGameBoard();
                        lastInputTime = currentTime;
                    }

                    if (currentTime - lastSecondTime >= secondInterval) { // her saniyede time değişkeninin arttırılması
                        time++;
                        updateGameBoard();
                        lastSecondTime = currentTime;
                    }

                    Thread.sleep(20);
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
                map[targetedX][targetedY] == '@' ||
                map[targetedX][targetedY] == 'S')) {
            targetedX = rnd.nextInt(1, 22);
            targetedY = rnd.nextInt(1, 55);
        }

        boolean[][] visited = new boolean[map[0].length][map[1].length];
        int[][][] parent = new int[map[0].length][map[1].length][2];

        Stack stack = new Stack(500);
        stack.push(new int[] { cx, cy });

        int[][] directions = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } }; // right, down, left, up

        while (!stack.isEmpty()) {
            int[] current = (int[]) stack.pop();
            int x = current[0], y = current[1];

            if (x < 0 || y < 0 || x >= map[0].length || y >= map[1].length || visited[x][y] || map[x][y] == '#')
                continue;

            visited[x][y] = true;

            if (x == targetedX && y == targetedY) {
                Stack result = new Stack(500);

                while (!(targetedX == cx && targetedY == cy)) {
                    int tempX = parent[targetedX][targetedY][0];
                    int tempY = parent[targetedX][targetedY][1];

                    if (targetedX - tempX == 0 && targetedY - tempY == 1) {
                        result.push("D"); // Down
                    } else if (targetedX - tempX == 0 && targetedY - tempY == -1) {
                        result.push("U"); // Up
                    } else if (targetedX - tempX == 1 && targetedY - tempY == 0) {
                        result.push("R"); // Right
                    } else if (targetedX - tempX == -1 && targetedY - tempY == 0) {
                        result.push("L"); // Left
                    }

                    if (!(tempX == cx && tempY == cy)) {
                        GameField.map[tempX][tempY] = '.';
                    }

                    targetedX = tempX;
                    targetedY = tempY;
                }
                return result;
            }

            for (int i = 0; i < 4; i++) {
                int nx = x + directions[i][0];
                int ny = y + directions[i][1];

                if (nx >= 0 && ny >= 0 && nx < map[0].length && ny < map[1].length && !visited[nx][ny]
                        && map[nx][ny] != ' ') {
                    visited[nx][ny] = true;
                    stack.push(new int[] { nx, ny });
                    parent[nx][ny][0] = x;
                    parent[nx][ny][1] = y;
                }
            }
        }

        return new Stack(500);
    }

    void updateStatusPanel() { // skor değerlerinin güncellenip ekrana yazdırılması

        String timeText = "" + time;
        String energyText = "" + energy;
        String lifeText = "" + life;
        String trapText = "" + trap;
        String playerscoreText = "" + playerscore;
        String sRobotText = "" + sRobot;
        String computerscoreText = "" + computerscore;

        writeToMap(5, 66, timeText);
        writeToMap(8, 66, energyText);
        writeToMap(9, 66, lifeText);
        writeToMap(10, 66, trapText);
        writeToMap(11, 66, playerscoreText);
        writeToMap(16, 66, sRobotText);
        writeToMap(17, 66, computerscoreText);
    }

    void writeToMap(int row, int col, String text) { // yardımcı fonksiyon
        for (int i = 0; i < text.length(); i++) {
            GameField.map[row][col + i] = text.charAt(i);
        }
    }

    void collectTreasures(int newPx, int newPy) { // treasure yedikten sonra puanlarını ekleyen fonksiyon

        if (GameField.map[newPx][newPy] == '1') {
            if (playersMove) {
                playerscore += 1;
                energy += 50;
            } else
                computerscore += 1;
        }

        if (GameField.map[newPx][newPy] == '2') {
            if (playersMove) {
                playerscore += 4;
                energy += 150;
            } else
                computerscore += 4;

        }

        if (GameField.map[newPx][newPy] == '3') {
            if (playersMove) {
                playerscore += 16;
                energy += 250;
            } else
                computerscore += 16;
        }

        if (GameField.map[newPx][newPy] == '@') {
            if (playersMove)
                trap++;
            else
                computerscore += 50;

        }

        if (GameField.map[newPx][newPy] == 'S') {
            if (playersMove) {
                playerscore += 200;
                energy += 500;
            }

        }
    }

    void movePlayer(int newPx, int newPy) { // oyuncunun hareketi fonksiyon haline getirdim
        GameField.map[px][py] = ' ';
        px = newPx;
        py = newPy;
        GameField.map[px][py] = 'P';
    }

    void moveComputer(int newPx, int newPy) { // oyuncunun hareketi fonksiyon haline getirdim
        GameField.map[cx][cy] = ' ';
        cx = newPx;
        cy = newPy;
        GameField.map[cx][cy] = 'C';
    }

    void ComputerTurn() {

        String way = (String) pathfinding.pop();
        if (way.equals("R")) {
            collectTreasures(cx, cy + 1);
            moveComputer(cx, cy + 1);
        } else if (way.equals("D")) {
            collectTreasures(cx + 1, cy);
            moveComputer(cx + 1, cy);
        } else if (way.equals("L")) {
            collectTreasures(cx, cy - 1);
            moveComputer(cx, cy - 1);
        } else if (way.equals("U")) {
            collectTreasures(cx - 1, cy);
            moveComputer(cx - 1, cy);
        }
    }

    void updateGameBoard() { // tahtayı güncelleyen fonksiyon

        updateStatusPanel(); // değerlerin güncellenmesi
        clearscreen(); // ekranın temizlenmesi
        cn.getTextWindow().setCursorPosition(0, 0); // cursoru ayarla
        GameField.printScreen();
    }
    public void clearscreen() { // ekranın temizlenmesi
        for (int i = 0; i < 80; i++) {
            for (int z = 0; z < 30; z++) {
                cn.getTextWindow().output(i, z, ' ');
            }
        }
    }
}

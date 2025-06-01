import java.util.Scanner;
import java.util.Random;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import enigma.core.Enigma;

public class Game {
    String choice;
    enigma.console.Console cn = Enigma.getConsole("Snake Game", 180, 44);

    private Scanner scanner;
    static Random random = new Random();

    public KeyListener klis;

    public int keypr; // key pressed?
    public int rkey; // key (for press/release)
    public int tempkey;

    // Creating necessary variables
    int time = 0;
    int energy = 500;
    int life = 1000;
    int trap = 0;

    // Variables for player and C robot
    boolean playersMove = false, computersMove = false;
    int px = 0, py = 0, playerscore = 0, cx = 0, cy = 0, targetedCX = 0, targetedCY = 0, computerscore = 0;
    Stack pathfinding = new Stack(500);

    // Snakes constructors
    public static int snakeCounter = 0;
    public Snake_Que snakes = new Snake_Que(15);

/*
    public static CircularQueue snakesX = new CircularQueue(15);
    public static CircularQueue snakeTargetX = new CircularQueue(15);
    public static CircularQueue snakesY = new CircularQueue(15);
    public static CircularQueue snakeTargetY = new CircularQueue(15);
    public static CircularQueue snakeMoveMove = new CircularQueue(15);
    public static CircularQueue randomMoveCounter = new CircularQueue(15);


 */
    Trap[] traps = new Trap[100];

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

        int gametiming = 0;
        int trapcounter = 0;

        do {
            cn.getTextWindow().setCursorPosition(25, 0);
            cn.getTextWindow().output("Welcome to the snake game");
            cn.getTextWindow().setCursorPosition(25, 1);
            cn.getTextWindow().output("To start the game press number 1");
            cn.getTextWindow().setCursorPosition(25, 2);
            cn.getTextWindow().output("To exit the game press number 2\n");

            cn.getTextWindow().setCursorPosition(25, 3);
            choice = scanner.nextLine();
            if (!choice.equals("1") && !choice.equals("2")) {
                cn.getTextWindow().setCursorPosition(25, 4);
                cn.getTextWindow().output("Please enter 1 or 2!\n");
            }
        } while (!choice.equals("1") && !choice.equals("2"));

        while(true) {
            if (choice.equals("2")) {
                cn.getTextWindow().setCursorPosition(25, 4);
                cn.getTextWindow().output("Exiting...");
                System.exit(0);
            } else if (choice.equals("1")) {
                clearScreen();
                GameField gameField = new GameField(cn);
                cn.getTextWindow().addKeyListener(klis);

                while (!(GameField.map[px][py] == ' ' && GameField.map[cx][cy] == ' ')) {
                    if (GameField.map[px][py] != ' ') {
                        px = random.nextInt(21) + 1;
                        py = random.nextInt(54) + 1;
                    }
                    if (GameField.map[cx][cy] != ' ') {
                        cx = random.nextInt(21) + 1;
                        cy = random.nextInt(54) + 1;
                    }
                }
                GameField.map[px][py] = 'P';
                GameField.map[cx][cy] = 'C';

                for (int i = 0; i < 30; i++) // oyunun başında tahtaya input queue'dan 30 element boşalt
                    gameField.unloadInputQueue();

                while (life >= 0) { // main loop of the game

                    updateGameBoard();

                    if (energy > 0 || gametiming % 2 == 0) { // 0.1 ya da 0.2 saniye

                        if (keypr == 1) {

                            playersMove = true;

                            if (rkey == KeyEvent.VK_LEFT && !gameField.isWall(px, py - 1)
                                    && !gameField.isCrashedRobots(px, py - 1)) {
                                collectTreasures(px, py - 1);
                                move(px, py - 1);
                                tempkey = rkey;
                            }
                            if (rkey == KeyEvent.VK_RIGHT && !gameField.isWall(px, py + 1)
                                    && !gameField.isCrashedRobots(px, py + 1)) {
                                collectTreasures(px, py + 1);
                                move(px, py + 1);
                                tempkey = rkey;
                            }
                            if (rkey == KeyEvent.VK_UP && !gameField.isWall(px - 1, py)
                                    && !gameField.isCrashedRobots(px - 1, py)) {
                                collectTreasures(px - 1, py);
                                move(px - 1, py);
                                tempkey = rkey;
                            }
                            if (rkey == KeyEvent.VK_DOWN && !gameField.isWall(px + 1, py)
                                    && !gameField.isCrashedRobots(px + 1, py)) {
                                collectTreasures(px + 1, py);
                                move(px + 1, py);
                                tempkey = rkey;
                            }

                            if (rkey == KeyEvent.VK_SPACE) {
                                if (trap > 0) {

                                    trap--;
                                    int randomx = 0, randomy = 0;

                                    int lastmove = 4;

                                    if (tempkey == KeyEvent.VK_RIGHT && !gameField.isWall(px, py + 1)
                                            && !gameField.isCrashedRobots(px, py - 1)) {
                                        randomy = 1;
                                    } else if (tempkey == KeyEvent.VK_LEFT && !gameField.isWall(px, py - 1)
                                            && !gameField.isCrashedRobots(px, py + 1)) {
                                        randomy = -1;
                                    } else if (tempkey == KeyEvent.VK_UP && !gameField.isWall(px - 1, py)
                                            && !gameField.isCrashedRobots(px - 1, py)) {
                                        randomx = -1;
                                    } else if (tempkey == KeyEvent.VK_DOWN && !gameField.isWall(px + 1, py)
                                            && !gameField.isCrashedRobots(px + 1, py)) {
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
                                        } while (gameField.isWall(px + randomx, py + randomy)
                                                || gameField.isCrashedRobots(px + randomx, py + randomy));
                                    }

                                    int oldPx = px;
                                    int oldPy = py;

                                    collectTreasures(px + randomx, py + randomy);
                                    move(px + randomx, py + randomy);
                                    GameField.map[oldPx][oldPy] = '=';

                                    traps[trapcounter] = new Trap(oldPx, oldPy, gametiming);
                                    trapcounter++;
                                }
                            }

                            playersMove = false;
                            keypr = 0;
                        }
                    }

                    if (gametiming % 4 == 0) { // Both robots movement (0.4 second)

                        // S robot movements
                        moveSnakes();

                        // C robot movements
                        computersMove = true;
                        if (!pathfinding.isEmpty()) {
                            String way = (String) pathfinding.peek();
                            if (way.equals("R")) {
                                collectTreasures(cx, cy + 1);
                                move(cx, cy + 1);
                            } else if (way.equals("D")) {
                                collectTreasures(cx + 1, cy);
                                move(cx + 1, cy);
                            } else if (way.equals("L")) {
                                collectTreasures(cx, cy - 1);
                                move(cx, cy - 1);
                            } else if (way.equals("U")) {
                                collectTreasures(cx - 1, cy);
                                move(cx - 1, cy);
                            }

                        }

                        if (!computersMove && (GameField.map[targetedCX][targetedCY] == '1' ||
                                GameField.map[targetedCX][targetedCY] == '2' ||
                                GameField.map[targetedCX][targetedCY] == '3' ||
                                GameField.map[targetedCX][targetedCY] == '@')) {
                            pathfinding.pop();
                        } else {
                            while (!(GameField.map[targetedCX][targetedCY] == '1' ||
                                    GameField.map[targetedCX][targetedCY] == '2' ||
                                    GameField.map[targetedCX][targetedCY] == '3' ||
                                    GameField.map[targetedCX][targetedCY] == '@')) {
                                targetedCX = random.nextInt(22) + 1;
                                targetedCY = random.nextInt(54) + 1;
                            }
                            char[][] tempMap = new char[23][55];

                            for (int i = 0; i < 23; i++) {
                                for (int j = 0; j < 55; j++) {
                                    if(GameField.map[i][j] == '.') {
                                        GameField.map[i][j] = ' ';
                                    }
                                    tempMap[i][j] = GameField.map[i][j];
                                }
                            }
                            pathfinding = pathfinding(tempMap, cx, cy, targetedCX, targetedCY);
                        }

                        computersMove = false;
                    }

                    if (gametiming % 20 == 0) // 2 saniyede bir input Queue'dan eleman yerleştirilmesi
                        gameField.unloadInputQueue();

                    if (gametiming % 10 == 0) // her saniyede time değişkeninin arttırılması
                        time++;

                    if (GameField.map[px + 1][py] == 'C' || GameField.map[px - 1][py] == 'C'
                            || GameField.map[px][py + 1] == 'C' || GameField.map[px][py - 1] == 'C')
                        life -= 30;
                    else if (GameField.map[px + 1][py] == 'S' || GameField.map[px - 1][py] == 'S'
                            || GameField.map[px][py + 1] == 'S' || GameField.map[px][py - 1] == 'S')
                        life -= 1;

                    for (int k = 0; k < trapcounter; k++) {
                        if (traps[k].getTime() != -1) {
                            if (gametiming - traps[k].getTime() >= 100)
                                traps[k].disappear();

                            else {
                                if (traps[k].checkSnake()) {
                                    traps[k].boom();
                                    playerscore += 200;
                                    energy += 500;
                                }
                            }
                        }
                    }

                    try {
                        Thread.sleep(100); // 1 time unit
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    gametiming++; // 0.1 saniyede bir artar

                }

                clearScreen();
                System.out.println("You're died.");
                System.out.print("Please enter your name: ");
                String name = scanner.nextLine();
                DoubleLinkedList highScoreList = new DoubleLinkedList();
                highScoreList.addOrderedScores(name + " " + playerscore);
                try {
                    BufferedReader reader = new BufferedReader(new FileReader("highscore.txt"));
                    String s = reader.readLine();
                    while(s != null) {
                        highScoreList.addOrderedScores(s);
                        s = reader.readLine();
                    }
                    reader.close();
                    BufferedWriter writer = new BufferedWriter(new FileWriter("highscore.txt"));
                    System.out.println();
                    System.out.println("High Score List");
                    System.out.println("-------------------------");
                    for(int i = 0; i < highScoreList.size(); i++) {
                        if(i == 0) {
                            writer.write((String) highScoreList.returnData(i));
                        } else {
                            writer.write("\n" + (String) highScoreList.returnData(i));
                        }
                    }
                    writer.close();
                    highScoreList.display();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println();
                System.out.println("Play again? ");
                choice = "";
                do {
                    choice = scanner.nextLine();
                    if (!choice.equals("1") && !choice.equals("2")) {
                        System.out.println("Please enter only Y or N");
                    }
                } while (!choice.equals("Y") && !choice.equals("N"));

                if(choice.equals("Y")) {
                    //Resetting variables for next round
                    time = 0; energy = 500; life = 1000; trap = 0;

                    playersMove = false; computersMove = false;
                    px = 0; py = 0; playerscore = 0; cx = 0; cy = 0; targetedCX = 0; targetedCY = 0; computerscore = 0;
                    pathfinding = new Stack(500);
                    /*
                    snakeCounter = 0;
                    snakes = new Snakes(15);
                    snakesX = new CircularQueue(15); snakeTargetX = new CircularQueue(15);
                    snakesY = new CircularQueue(15); snakeTargetY = new CircularQueue(15);
                    snakeMoveMove = new CircularQueue(15);
                    randomMoveCounter = new CircularQueue(15);
                    choice = "1";

                     */
                } else if(choice.equals("N")) {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
            }
        }
    }

    public void clearScreen() { // ekranın temizlenmesi
        for (int i = 0; i < 80; i++) {
            for (int z = 0; z < 30; z++) {
                cn.getTextWindow().output(i, z, ' ');
            }
        }
    }

    public void updateGameBoard() { // tahtayı güncelleyen fonksiyon
        updateStatusPanel(); // değerlerin güncellenmesi
        clearScreen(); // ekranın temizlenmesi
        cn.getTextWindow().setCursorPosition(0, 0); // cursoru ayarla
        GameField.printScreen();
    }

    public static Stack pathfinding(char[][] map, int cx, int cy, int targetedX, int targetedY) {

        boolean[][] visited = new boolean[23][55];
        int[][][] neighbor = new int[23][55][2];

        Stack stack = new Stack(1000);
        stack.push(new int[] { cx, cy });
        visited[cx][cy] = true;

        int[][] directions = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } }; // Right, Down, Left, Up

        Stack[] bfsGraph = new Stack[23 * 55];
        int depth = 0, x = 0, y = 0;

        while (!stack.isEmpty() && !(x == targetedX && y == targetedY)) {
            Stack currentLevelStack = new Stack(1000);
            while (!stack.isEmpty()) {
                int[] current = (int[]) stack.pop();
                x = current[0];
                y = current[1];

                for (int i = 0; i < 4; i++) {
                    int nx = x + directions[i][0], ny = y + directions[i][1];

                    if (!visited[nx][ny] && map[nx][ny] != '#' && map[nx][ny] != 'P' && map[nx][ny] != 'S') {
                        currentLevelStack.push(new int[] { nx, ny });
                        visited[nx][ny] = true;
                        neighbor[nx][ny][0] = x;
                        neighbor[nx][ny][1] = y;
                    }
                }
            }

            bfsGraph[depth] = currentLevelStack;
            stack = currentLevelStack;
            depth++;
        }

        Stack result = new Stack(1000);
        while (!(targetedX == cx && targetedY == cy)) {
            int tempX = neighbor[targetedX][targetedY][0], tempY = neighbor[targetedX][targetedY][1];

            if (targetedX - tempX == 0 && targetedY - tempY == 1) {
                result.push("R");
            } else if (targetedX - tempX == 0 && targetedY - tempY == -1) {
                result.push("L");
            } else if (targetedX - tempX == 1 && targetedY - tempY == 0) {
                result.push("D");
            } else if (targetedX - tempX == -1 && targetedY - tempY == 0) {
                result.push("U");
            }

            targetedX = tempX;
            targetedY = tempY;
            if (GameField.map[targetedX][targetedY] == ' ')
                GameField.map[targetedX][targetedY] = '.';
        }

        return result;
    }



    void updateStatusPanel() { // skor değerlerinin güncellenip ekrana yazdırılması

        String timeText = "" + time;
        String energyText = "" + energy;
        String lifeText = "" + life;
        String trapText = "" + trap;
        String playerscoreText = "" + playerscore;
        String sRobotText = "" + snakeCounter;
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
        for (int i = 0; i < text.length() + 3; i++) {
            GameField.map[row][col + i] = ' ';
        }
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
    }

    void move(int newX, int newY) {
        if (playersMove) {
            // In case of crossing C robot's path
            GameField.map[px][py] = ' ';
            px = newX;
            py = newY;
            GameField.map[px][py] = 'P';
            if (energy > 0) { // Spending energy
                energy--;
            }
        }

        else if (computersMove && GameField.map[newX][newY] != 'P' && GameField.map[newX][newY] != 'S') {
            GameField.map[cx][cy] = ' ';
            cx = newX;
            cy = newY;
            GameField.map[cx][cy] = 'C';
            computersMove = false;
        }
    }

    // this function adds a new snake and hold coordinates at different queues
    public void addNewSnake(int i, int j) {
        Snakes a = new Snakes(i,j);
        snakes.enqueue(a);
        snakeCounter++;
    }

    public void moveSnakes() {
        Snakes s;
        for (int i = 0; i < snakeCounter; i++) {
            s = snakes.peek();
            //if it arrived the target, choose new target
            if (s.getXcoor() == s.getTargetx() && s.getYcoor() == s.getTargety()) {
                chooseNewTarget();
            }
            if (s.getMovemode()) {
                targetedMove();
            } else {
                randomMove();
            }
            snakes.enqueue(snakes.dequeue());
        }
    }

    private void chooseNewTarget() {
        int tXnew, tYnew;
        boolean isAvaible = false;
        Snakes s = snakes.peek();
        do {
            tXnew = random.nextInt(23);
            tYnew = random.nextInt(55);
            if (GameField.map[tXnew][tYnew] == '1' || GameField.map[tXnew][tYnew] == '2' ||
                    GameField.map[tXnew][tYnew] == '3') {
                isAvaible = true;
            }
        }while (!isAvaible);
        s.setTargetx(tXnew);
        s.setTargety(tYnew);
        s.setStuckCounter(0);
        s.setMovemode(true);
    }

    public void targetedMove() {
        Snakes s = snakes.peek();

        int sX = s.getXcoor();
        int sY = s.getYcoor();
        int tX = s.getTargetx();
        int tY = s.getTargety();
        int diffX = tX - sX;
        int diffY = tY - sY;
        boolean moved = false;

        if (!moved &&  diffX < 0) { //up
            if (sX - 1 > 0 && GameField.map[sX - 1][sY] != '#' &&
                    GameField.map[sX - 1][sY] != 'P' &&
                    GameField.map[sX - 1][sY] != 'C'){
                GameField.map[sX][sY] = ' ';
                GameField.map[sX - 1][sY] = 'S';
                moved = true;
                s.setXcoor(sX - 1);
            }
        }
        if (!moved && diffX > 0) { //down
            if(sX + 1 < 23 && GameField.map[sX + 1][sY] != '#' &&
                    GameField.map[sX + 1][sY] != 'P' &&
                    GameField.map[sX + 1][sY] != 'C'){
                GameField.map[sX][sY] = ' ';
                GameField.map[sX + 1][sY] = 'S';
                moved = true;
                s.setXcoor(sX + 1);
            }
        }
        if (!moved && diffY < 0){ //left
            if (sY - 1 > 0 && GameField.map[sX][sY - 1] != '#' &&
                    GameField.map[sX][sY - 1] != 'P' &&
                    GameField.map[sX][sY - 1] != 'C'){
                GameField.map[sX][sY] = ' ';
                GameField.map[sX][sY - 1] = 'S';
                moved = true;
                s.setYcoor(sY - 1);
            }
        }
        if (!moved && diffY > 0){ //right
            if (sY + 1 < 55 && GameField.map[sX][sY + 1] != '#' &&
                    GameField.map[sX][sY + 1] != 'P' &&
                    GameField.map[sX][sY + 1] != 'C') {
                GameField.map[sX][sY] = ' ';
                GameField.map[sX][sY + 1] = 'S';
                moved = true;
                s.setYcoor(sY + 1);
            }
        }

        //we check for if it moved or not
        if (!moved){
            s.setMovemode(false);
            randomMove();
        }

    }

    private void randomMove() {
        Snakes s = snakes.peek();
        int sX, sY;
        sX = s.getXcoor();
        sY = s.getYcoor();
        boolean moved = false;

        while (!moved){
            int dir = random.nextInt(4);

            if (dir == 0) { //means up
                if (sX - 1 > 0 &&  GameField.map[sX - 1][sY] != '#' && GameField.map[sX - 1][sY] != 'C' &&
                        GameField.map[sX - 1][sY] != 'P'){
                    GameField.map[sX][sY] = ' ';
                    GameField.map[sX - 1][sY] = 'S';
                    moved = true;
                    s.setXcoor(sX - 1);
                }
            }
            else if (dir == 1) { //means down
                if (sX + 1 < 23 &&  GameField.map[sX + 1][sY] != '#' && GameField.map[sX + 1][sY] != 'C' &&
                        GameField.map[sX + 1][sY] != 'P') {
                    GameField.map[sX][sY] = ' ';
                    GameField.map[sX + 1][sY] = 'S';
                    moved = true;
                    s.setXcoor(sX + 1);
                }
            }
            else if (dir == 2) { //means left
                if (sY - 1 > 0 && GameField.map[sX][sY - 1] != '#' && GameField.map[sX][sY - 1] != 'C' &&
                        GameField.map[sX][sY - 1] != 'P'){
                    GameField.map[sX][sY] = ' ';
                    GameField.map[sX][sY - 1] = 'S';
                    moved = true;
                    s.setYcoor(sY - 1);
                }
            }
            else if (dir == 3){ //means right
                if (sY + 1 < 55 && GameField.map[sX][sY + 1] != '#' && GameField.map[sX][sY + 1] != 'C' &&
                        GameField.map[sX][sY + 1] != 'P'){
                    GameField.map[sX][sY] = ' ';
                    GameField.map[sX][sY + 1] = 'S';
                    moved = true;
                    s.setYcoor(sY + 1);
                }
            }
        }
        s.setStuckCounter(s.getStuckCounter() + 1);

        if (s.getStuckCounter() > 25){
            s.setStuckCounter(0);
            s.setMovemode(true);
        }
    }
}

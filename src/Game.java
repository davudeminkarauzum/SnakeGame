import java.util.Scanner;
import java.util.Random;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

    public void clearScreen() { // ekranın temizlenmesi
        for (int i = 0; i < 80; i++) {
            for (int z = 0; z < 30; z++) {
                cn.getTextWindow().output(i, z, ' ');
            }
        }
    }

    //Creating necessary variables
    int time = 0;
    int energy = 500;
    int life = 1000;
    int trap = 0;

    //Variables for player and C robot
    boolean playersMove = false,           computersMove = false;
    int px = 0, py = 0, playerscore = 0,   cx = 0, cy = 0, computerscore = 0;
    Stack pathfinding = new Stack(500);
    boolean intersectsPath = false;

    //Snakes constructors
    public static int snakeCounter = 0;
    public static Snakes snakes = new Snakes(15);
    public static CircularQueue snakesX = new CircularQueue(15);
    public static CircularQueue snakeTargetX = new CircularQueue(15);
    public static CircularQueue snakesY = new CircularQueue(15);
    public static CircularQueue snakeTargetY = new CircularQueue(15);
    public static CircularQueue snakeMoveMove = new CircularQueue(15);
    public static CircularQueue randomMoveCounter = new CircularQueue(15);

    Trap[]traps = new Trap [100];

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
        	//In case of crossing C robot's path
        	if(intersectsPath) {
        		GameField.map[px][py] = '.';
        	} else {            
        		GameField.map[px][py] = ' ';  
        		intersectsPath = false;
        	}
        	
        	if(GameField.map[newX][newY] == '.') {
        		intersectsPath = true;
        	} else {
        		intersectsPath = false;
        	}
        	
            px = newX;
            py = newY;
            GameField.map[px][py] = 'P';
            if (energy > 0) { //Spending energy
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

    public void updateGameBoard() { // tahtayı güncelleyen fonksiyon
        updateStatusPanel(); // değerlerin güncellenmesi
        clearScreen();   // ekranın temizlenmesi
        cn.getTextWindow().setCursorPosition(0, 0); // cursoru ayarla
        GameField.printScreen();
    }


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

        cn.getTextWindow().setCursorPosition(25, 0);
        cn.getTextWindow().output("Welcome to the snake game");
        cn.getTextWindow().setCursorPosition(25, 1);
        cn.getTextWindow().output("To start the game press number 1");
        cn.getTextWindow().setCursorPosition(25, 2);
        cn.getTextWindow().output("To exit the game press number 2\n");

        cn.getTextWindow().setCursorPosition(50, 3);
        choice = scanner.nextLine();

        if (choice.equals("1")) {
            clearScreen();
            GameField gameField = new GameField(cn);
            while (life > 0) {
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
                GameField.map[px][py] = 'P';   GameField.map[cx][cy] = 'C';

                for (int i = 0; i < 30; i++) // oyunun başında tahtaya input queue'dan 30 element boşalt
                    gameField.unloadInputQueue();

                while (true) { //main loop of the game

                    updateGameBoard();

                    if (energy > 0 || gametiming % 2 == 0) {  // 0.1 ya da 0.2 saniye

                        if (keypr == 1) {

                            playersMove = true;

                            if (rkey == KeyEvent.VK_LEFT && !gameField.isWall(px, py - 1) && !gameField.isCrashedRobots(px, py - 1)) {
                                collectTreasures(px, py - 1);
                                move(px, py - 1);
                                tempkey = rkey;
                            }
                            if (rkey == KeyEvent.VK_RIGHT && !gameField.isWall(px, py + 1) && !gameField.isCrashedRobots(px, py + 1)) {
                                collectTreasures(px, py + 1);
                                move(px, py + 1);
                                tempkey = rkey;
                            }
                            if (rkey == KeyEvent.VK_UP && !gameField.isWall(px - 1, py) && !gameField.isCrashedRobots(px - 1, py)) {
                                collectTreasures(px - 1, py);
                                move(px - 1, py);
                                tempkey = rkey;
                            }
                            if (rkey == KeyEvent.VK_DOWN && !gameField.isWall(px + 1, py) && !gameField.isCrashedRobots(px + 1, py)) {
                                collectTreasures(px + 1, py);
                                move(px + 1, py);
                                tempkey = rkey;
                            }

                            if (rkey == KeyEvent.VK_SPACE) {
                                if (trap > 0) {

                                    trap--;
                                    int randomx = 0, randomy = 0;

                                    int lastmove = 4;

                                    if (tempkey == KeyEvent.VK_RIGHT && !gameField.isWall(px, py + 1) && !gameField.isCrashedRobots(px, py - 1)) {
                                        randomy = 1;
                                    } else if (tempkey == KeyEvent.VK_LEFT && !gameField.isWall(px, py - 1) && !gameField.isCrashedRobots(px, py + 1)) {
                                        randomy = -1;
                                    } else if (tempkey == KeyEvent.VK_UP && !gameField.isWall(px - 1, py) && !gameField.isCrashedRobots(px - 1, py)) {
                                        randomx = -1;
                                    } else if (tempkey == KeyEvent.VK_DOWN && !gameField.isWall(px + 1, py) && !gameField.isCrashedRobots(px + 1, py)) {
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
                                        } while (gameField.isWall(px + randomx, py + randomy) || gameField.isCrashedRobots(px + randomx, py + randomy));
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

                    if (gametiming % 4 == 0) { //Both robots movement (0.4 second)

                        //S robot movements
                        moveSnakes();

                        //C robot movements
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

                            if (!computersMove) {
                                pathfinding.pop();
                            }
                        } else {
                            char[][] tempMap = new char[23][55];

                            for (int i = 0; i < 23; i++) {
                                for (int j = 0; j < 55; j++) {
                                    tempMap[i][j] = GameField.map[i][j];
                                }
                            }
                            pathfinding = pathfinding(tempMap, cx, cy);
                        }

                        computersMove = false;
                    }

                    if (gametiming % 20 == 0)  // 2 saniyede bir input Queue'dan eleman yerleştirilmesi
                        gameField.unloadInputQueue();


                    if (gametiming % 10 == 0)  // her saniyede time değişkeninin arttırılması
                        time++;


                    if (GameField.map[px + 1][py] == 'C' || GameField.map[px - 1][py] == 'C'
                            || GameField.map[px][py + 1] == 'C' || GameField.map[px][py - 1] == 'C')
                        life -= 30;
                    else if (GameField.map[px + 1][py] == 'S' || GameField.map[px - 1][py] == 'S'
                            || GameField.map[px][py + 1] == 'S' || GameField.map[px][py - 1] == 'S')
                        life -= 1;

                    for (int k = 0; k < trapcounter; k++) {
                        if (traps[k].getTime() != -1) {
                            if(gametiming - traps[k].getTime() >= 100)
                                traps[k].disappear();

                            else {
                                if(traps[k].checkSnake()) {
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

                    gametiming++;  // 0.1 saniyede bir artar

                }
            }
        }
    }

    public static Stack pathfinding(char[][] map, int cx, int cy) {
        int targetedX = 0, targetedY = 0;

        while (!(map[targetedX][targetedY] == '1' ||
                map[targetedX][targetedY] == '2' ||
                map[targetedX][targetedY] == '3' ||
                map[targetedX][targetedY] == '@')) {
            targetedX = random.nextInt(1, 23);
            targetedY = random.nextInt(1, 55);
        }

        boolean[][] visited = new boolean[23][55];
        int[][][] neighbor = new int[23][55][2];

        Stack stack = new Stack(1000);
        stack.push(new int[]{cx, cy});
        visited[cx][cy] = true;

        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}}; //Right, Down, Left, Up

        Stack[] bfsGraph = new Stack[23 * 55];
        int depth = 0, x = 0, y = 0;

        while (!stack.isEmpty() && !(x == targetedX && y == targetedY)) {
            Stack currentLevelStack = new Stack(1000);
            while (!stack.isEmpty()) {
                int[] current = (int[]) stack.pop();
                x = current[0];     y = current[1];

                for (int i = 0; i < 4; i++) {
                    int nx = x + directions[i][0], ny = y + directions[i][1];

                    if (!visited[nx][ny] && map[nx][ny] != '#' && map[nx][ny] != 'P' && map[nx][ny] != 'S') {
                        currentLevelStack.push(new int[]{nx, ny});
                        visited[nx][ny] = true;
                        neighbor[nx][ny][0] = x;     neighbor[nx][ny][1] = y;
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

            targetedX = tempX;     targetedY = tempY;
            if(GameField.map[targetedX][targetedY] == ' ') 
            	GameField.map[targetedX][targetedY] = '.';
        }

        return result;
    }

    //this function adds a new snake and hold coordinates at different queues
    public static void addNewSnake(int i , int j) {
        SingleLinkedList newSnake = new SingleLinkedList();
        snakes.enqueue(newSnake);
        snakesX.enqueue(i);          snakesY.enqueue(j);
        snakeTargetX.enqueue(i);     snakeTargetY.enqueue(j);
        snakeMoveMove.enqueue(true);
        snakeCounter++;
        randomMoveCounter.enqueue(0);
    }

    public void moveSnakes() {
        for (int i = 0; i < snakeCounter; i++) {
        	int sx = (int) snakesX.peek(),      sy = (int) snakesY.peek();
        	int tx = (int) snakeTargetX.peek(), ty = (int) snakeTargetY.peek();

            //if it arrived the target, choose new target
            if (sx == tx && sy == ty) {
                chooseNewTarget();
            }
            if ((boolean) snakeMoveMove.peek()) {
                targetedMove();
            } else {
                randomMove();
            }

            snakes.enqueue(snakes.dequeue());
            snakesX.enqueue(snakesX.dequeue());
            snakesY.enqueue(snakesY.dequeue());
            snakeTargetX.enqueue(snakeTargetX.dequeue());
            snakeTargetY.enqueue(snakeTargetY.dequeue());
            snakeMoveMove.enqueue(snakeMoveMove.dequeue());
            randomMoveCounter.enqueue(randomMoveCounter.dequeue());
        }
    }

    private void chooseNewTarget() {
        int tXnew, tYnew;
        boolean isAvaible = false;
        do {
            tXnew = random.nextInt(23);
            tYnew = random.nextInt(55);
            if (GameField.map[tXnew][tYnew] == '1' || GameField.map[tXnew][tYnew] == '2' ||
                    GameField.map[tXnew][tYnew] == '3') {
                isAvaible = true;
            }
        }while (!isAvaible);

        //removing old values
        snakeTargetX.dequeue();
        snakeTargetY.dequeue();
        snakeMoveMove.dequeue();

        snakeTargetX.enqueue(tXnew);
        snakeTargetY.enqueue(tYnew);
        snakeMoveMove.enqueue(true);
        randomMoveCounter.dequeue();
        randomMoveCounter.enqueue(0);
        snakes.enqueue(snakes.dequeue());
        snakesX.enqueue(snakesX.dequeue());
        snakesY.enqueue(snakesY.dequeue());

        for (int j = 0; j < snakeCounter - 1; j++) {
            snakeTargetX.enqueue(snakeTargetX.dequeue());
            snakeTargetY.enqueue(snakeTargetY.dequeue());
            snakeMoveMove.enqueue(snakeMoveMove.dequeue());
            snakesX.enqueue(snakesX.dequeue());
            snakesY.enqueue(snakesY.dequeue());
            randomMoveCounter.enqueue(randomMoveCounter.dequeue());
            snakes.enqueue(snakes.dequeue());
        }
    }

    public void targetedMove() {
        int sX = (int)snakesX.peek();
        int sY = (int)snakesY.peek();
        int tX = (int)snakeTargetX.peek();
        int tY = (int)snakeTargetY.peek();
        int diffX = tX - sX;
        int diffY = tY - sY;
        boolean moved = false;

        if (!moved &&  diffX < 0) { //up
            if (sX - 1 > 0 && GameField.map[sX - 1][sY] != '#' &&
                    GameField.map[sX - 1][sY] != 'P' &&
                    GameField.map[sX - 1][sY] != 'C' &&
                    GameField.map[sX - 1][sY] != 'S'){
                GameField.map[sX][sY] = ' ';
                GameField.map[sX - 1][sY] = 'S';
                moved = true;
                snakesX.dequeue();
                snakesX.enqueue(sX - 1);
                snakesY.enqueue(snakesY.dequeue());

                snakes.enqueue(snakes.dequeue());
                snakeMoveMove.enqueue(snakeMoveMove.dequeue());
                snakeTargetX.enqueue(snakeTargetX.dequeue());
                snakeTargetY.enqueue(snakeTargetY.dequeue());
                randomMoveCounter.enqueue(randomMoveCounter.dequeue());
            }
        }
        if (!moved && diffX > 0) { //down
            if(sX + 1 < 23 && GameField.map[sX + 1][sY] != '#' &&
                    GameField.map[sX + 1][sY] != 'P' &&
                    GameField.map[sX + 1][sY] != 'C' &&
                    GameField.map[sX + 1][sY] != 'S'){
                GameField.map[sX][sY] = ' ';
                GameField.map[sX + 1][sY] = 'S';
                moved = true;
                snakesX.dequeue();
                snakesX.enqueue(sX + 1);
                snakesY.enqueue(snakesY.dequeue());

                snakes.enqueue(snakes.dequeue());
                snakeMoveMove.enqueue(snakeMoveMove.dequeue());
                snakeTargetX.enqueue(snakeTargetX.dequeue());
                snakeTargetY.enqueue(snakeTargetY.dequeue());
                randomMoveCounter.enqueue(randomMoveCounter.dequeue());
            }
        }
        if (!moved && diffY < 0){ //left
            if (sY - 1 > 0 && GameField.map[sX][sY - 1] != '#' &&
                    GameField.map[sX][sY - 1] != 'P' &&
                    GameField.map[sX][sY - 1] != 'C' &&
                    GameField.map[sX][sY - 1] != 'S'){
                GameField.map[sX][sY] = ' ';
                GameField.map[sX][sY - 1] = 'S';
                moved = true;
                snakesY.dequeue();
                snakesY.enqueue(sY - 1);
                snakesX.enqueue(snakesX.dequeue());

                snakes.enqueue(snakes.dequeue());
                snakeMoveMove.enqueue(snakeMoveMove.dequeue());
                snakeTargetX.enqueue(snakeTargetX.dequeue());
                snakeTargetY.enqueue(snakeTargetY.dequeue());
                randomMoveCounter.enqueue(randomMoveCounter.dequeue());
            }
        }
        if (!moved && diffY > 0){ //right
            if (sY + 1 < 55 && GameField.map[sX][sY + 1] != '#' &&
                    GameField.map[sX][sY + 1] != 'P' &&
                    GameField.map[sX][sY + 1] != 'C' &&
                    GameField.map[sX][sY + 1] != 'S') {
                GameField.map[sX][sY] = ' ';
                GameField.map[sX][sY + 1] = 'S';
                moved = true;
                snakesY.dequeue();
                snakesY.enqueue(sY + 1);
                snakesX.enqueue(snakesX.dequeue());

                snakes.enqueue(snakes.dequeue());
                snakeMoveMove.enqueue(snakeMoveMove.dequeue());
                snakeTargetX.enqueue(snakeTargetX.dequeue());
                snakeTargetY.enqueue(snakeTargetY.dequeue());
                randomMoveCounter.enqueue(randomMoveCounter.dequeue());
            }
        }

        //we check for if it moved or not
        if (!moved){
            snakes.enqueue(snakes.dequeue());
            snakesX.enqueue(snakesX.dequeue());
            snakesY.enqueue(snakesY.dequeue());
            snakeMoveMove.dequeue();
            snakeMoveMove.enqueue(false);
            snakeTargetX.enqueue(snakeTargetX.dequeue());
            snakeTargetY.enqueue(snakeTargetY.dequeue());
            randomMoveCounter.enqueue(randomMoveCounter.dequeue());
        }


            for (int i = 0; i < snakeCounter - 1; i++) {
                snakesX.enqueue(snakesX.dequeue());
                snakesY.enqueue(snakesY.dequeue());
                snakeMoveMove.enqueue(snakeMoveMove.dequeue());
                snakeTargetX.enqueue(snakeTargetX.dequeue());
                snakeTargetY.enqueue(snakeTargetY.dequeue());
                randomMoveCounter.enqueue(randomMoveCounter.dequeue());
                snakes.enqueue(snakes.dequeue());
            }
    }


    private void randomMove() {
        int sX, sY;
        sX = (int)snakesX.peek();
        sY = (int)snakesY.peek();
        boolean moved = false;
        int attemps = 0;
        int randomCountered;

        while (!moved){
            int dir = random.nextInt(4);

            if (dir == 0) { //means up
                if (sX - 1 > 0 &&  GameField.map[sX - 1][sY] != '#' && GameField.map[sX - 1][sY] != 'C' &&
                        GameField.map[sX - 1][sY] != 'P'){
                    GameField.map[sX][sY] = ' ';
                    GameField.map[sX - 1][sY] = 'S';
                    moved = true;
                    snakesX.dequeue();
                    snakesX.enqueue(sX - 1);
                    snakesY.enqueue(snakesY.dequeue());
                }
            }
            else if (dir == 1) { //means down
                if (sX + 1 < 23 &&  GameField.map[sX + 1][sY] != '#' && GameField.map[sX + 1][sY] != 'C' &&
                        GameField.map[sX + 1][sY] != 'P') {
                    GameField.map[sX][sY] = ' ';
                    GameField.map[sX + 1][sY] = 'S';
                    moved = true;
                    snakesX.dequeue();
                    snakesX.enqueue(sX + 1);
                    snakesY.enqueue(snakesY.dequeue());
                }
            }
            else if (dir == 2) { //means left
                if (sY - 1 > 0 && GameField.map[sX][sY - 1] != '#' && GameField.map[sX][sY - 1] != 'C' &&
                        GameField.map[sX][sY - 1] != 'P'){
                    GameField.map[sX][sY] = ' ';
                    GameField.map[sX][sY - 1] = 'S';
                    moved = true;
                    snakesY.dequeue();
                    snakesY.enqueue(sY - 1);
                    snakesX.enqueue(snakesX.dequeue());
                }
            }
            else if (dir == 3){ //means right
                if (sY + 1 < 55 && GameField.map[sX][sY + 1] != '#' && GameField.map[sX][sY + 1] != 'C' &&
                        GameField.map[sX][sY + 1] != 'P'){
                    GameField.map[sX][sY] = ' ';
                    GameField.map[sX][sY + 1] = 'S';
                    moved = true;
                    snakesY.dequeue();
                    snakesY.enqueue(sY + 1);
                    snakesX.enqueue(snakesX.dequeue());
                }
            }
            attemps++;
        }
        randomCountered = (int)randomMoveCounter.peek();
        randomCountered++;

        if (randomCountered > 25){
            randomCountered = 0;
            snakeMoveMove.dequeue();
            snakeMoveMove.enqueue(true);
        }
        else
            snakeMoveMove.enqueue(snakeMoveMove.dequeue());

        randomMoveCounter.dequeue();
        randomMoveCounter.enqueue(randomCountered);

        snakes.enqueue(snakes.dequeue());
        snakeTargetX.enqueue(snakeTargetX.dequeue());
        snakeTargetY.enqueue(snakeTargetY.dequeue());

        for (int i = 0; i < snakeCounter - 1; i++) {
            snakesX.enqueue(snakesX.dequeue());
            snakesY.enqueue(snakesY.dequeue());
            snakeMoveMove.enqueue(snakeMoveMove.dequeue());
            randomMoveCounter.enqueue(randomMoveCounter.dequeue());
            snakes.enqueue(snakes.dequeue());
            snakeTargetX.enqueue(snakeTargetX.dequeue());
            snakeTargetY.enqueue(snakeTargetY.dequeue());
        }
    }
}

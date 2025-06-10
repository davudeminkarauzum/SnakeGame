import java.util.Random;

public class Snake {
	
  private SingleLinkedList bodyParts; // the list that contains Snake's elements.
  private boolean alive; // boolean variable for snake's live situation
  
  private int targetedX; // x coordinate of snake's target
  private int targetedY; // y coordinate of snake's target
  
  int randomMoveCounter; // number of random moves to play
  
  public Snake(int sx, int sy) { // construct method for creating new snake
	  alive = true;
		
	  randomMoveCounter = 0;
	  targetedX = 0;
	  targetedY = 0;		
	  findTreasure(); 
		
	  bodyParts = new SingleLinkedList(); // initializing list
	  SnakeElement SnakeHead = new SnakeElement('S', sx, sy); // create snake's head as snake element.
	  bodyParts.eat(SnakeHead); // add snake's head to list
  }
    
  public void findTreasure() { // finds random target for snake
      Random random = new Random();
      do {
          targetedX = random.nextInt(22) + 1;
          targetedY = random.nextInt(54) + 1;
      } while (!(GameField.map[targetedX][targetedY] == '1' ||
   		        GameField.map[targetedX][targetedY] == '2' ||
    		    GameField.map[targetedX][targetedY] == '3'));	    
  }

  public void snakeMovement() {
	  
	  SnakeElement snakeHead = (SnakeElement) bodyParts.getHead().getData();
	  int x = snakeHead.getX(), y = snakeHead.getY();
	
	  if(GameField.map[targetedX][targetedY] == ' ')
		  findTreasure();
		
      if(stuck()) {
		  if (bodyParts.getHead().getLink() != null) {
		      reverseSnake();	
		  } else {
		      randomMove();				
		  }
		  randomMoveCounter = 25;
	  }
	
      else if(randomMoveCounter > 0)
			  randomMove();
		
	  else if(y > targetedY && !isTail(x, y - 1)) {
		 
		  if(!(GameField.map[x][y - 1] == '#' || GameField.map[x][y - 1] == 'P' 
			|| GameField.map[x][y - 1] == 'C'|| isCrashedSnake(x, y - 1)))
			  move(x, y - 1, x, y);	

		  else 
		    randomMoveCounter = 25;		
	  }
	
      else if(y < targetedY && !isTail(x, y + 1)) {
    	
		  if(!(GameField.map[x][y + 1] == '#' || GameField.map[x][y + 1] == 'P' 
			|| GameField.map[x][y + 1] == 'C'|| isCrashedSnake(x, y + 1)))
			  move(x, y + 1, x, y);	

		  else 
		    randomMoveCounter = 25;			
	  }
	
	  else if(x > targetedX && !isTail(x - 1, y)) {
		  if(!(GameField.map[x - 1][y] == '#' || GameField.map[x - 1][y] == 'P' 
			|| GameField.map[x - 1][y] == 'C'|| isCrashedSnake(x - 1, y)))
		 	  move(x - 1, y, x, y);	

		  else 
		    randomMoveCounter = 25;					
	  }
	
	  else if(x < targetedX && !isTail(x + 1, y)) {
		  if(!(GameField.map[x + 1][y] == '#' || GameField.map[x + 1][y] == 'P' 
			|| GameField.map[x + 1][y] == 'C'|| isCrashedSnake(x + 1, y)))
			  move(x + 1, y, x, y);	

		  else 
		    randomMoveCounter = 25;		
	  }
  }

  public void move(int newSx, int newSy, int x, int y) {
	
	deleteSnakeOnMap();
	
	if(GameField.map[newSx][newSy] == '1' ||
	   GameField.map[newSx][newSy] == '2' ||
	   GameField.map[newSx][newSy] == '3') {
		int sX = 0, sY = 0, count = 0;
		boolean flag = false;
		for(int i = 0; i < Game.snakes.size(); i++) {
			Snake snake = (Snake) Game.snakes.peek();
			Node temp = snake.bodyParts.getHead();
			count = 0;
			while(temp != null) {
				SnakeElement element = (SnakeElement) temp.getData();
				sX = element.getX();   sY = element.getY();
				if(sX == newSx && sY == newSy) {
					flag = true;
					break;
				}
				count++;
				temp = (Node) temp.getLink();
			}
			
			if(flag) {
				break;
			}
			Game.snakes.enqueue(Game.snakes.dequeue());
		}
		
		if(flag) {
			Snake snake = (Snake) Game.snakes.peek();
			if(GameField.map[newSx][newSy] == '1') {
				if(snake.bodyParts.size() > 1) {
					snake.bodyParts.deleteFirst();
					snake.bodyParts.collisionType1(snake.bodyParts, count);
				}
				for(int i = 0; i < Game.snakes.size(); i++) {
					SnakeElement element = (SnakeElement) Game.snakes.peek().bodyParts.getHead().getData();
					sX = element.getX();   sY = element.getY();
					
					if(sX == x && sY == y) {
						snake.bodyParts.clear();
						Game.snakes.dequeue();
						break;
					}
					Game.snakes.enqueue(Game.snakes.dequeue());
				}
			} else {
				snake.bodyParts.collisionType2andType3(count);
				snake.reverseSnake();
			}
		} else {
			eat(newSx, newSy);	
		}
	} else {
		if(bodyParts.getHead().getLink() == null) {
			Node head = bodyParts.getHead();
			SnakeElement snakeHead = (SnakeElement) head.getData();
			snakeHead.setX(newSx);
			snakeHead.setY(newSy);
		} else {
			
	 	    Node next = (Node) bodyParts.getHead().getLink();	   
			   
		    SnakeElement snakeHead = (SnakeElement) bodyParts.getHead().getData();
			  
		    int currentX = snakeHead.getX();
		    int currentY = snakeHead.getY();
			   
		    while(next != null) {   
			    SnakeElement nextPart = (SnakeElement) next.getData();
				  
			    int nextX = nextPart.getX();
			    int nextY = nextPart.getY();
				  
			    nextPart.setX(currentX);
			    nextPart.setY(currentY);

			    currentX = nextX;
			    currentY = nextY;

			    next = (Node) next.getLink();
		    }
			   
		    snakeHead.setX(newSx);
		    snakeHead.setY(newSy);	
		}	
	}
	displaySnakeOnMap();
}

public void randomMove() {
	
	SnakeElement snakeHead = (SnakeElement) bodyParts.getHead().getData();
	int x = snakeHead.getX();
	int y = snakeHead.getY();
	
	Random random = new Random();
	
	int randomX = 0, randomY = 0;
	
	  do {
          randomX = 0;
          randomY = 0;

          int randomMove = random.nextInt(4);
          switch (randomMove) {

              case (0): randomX = -1; break;            
              case (1): randomX = 1; break;             
              case (2): randomY = -1; break;             
              case (3): randomY = 1; break;           
          }
      } while (GameField.map[x + randomX][y + randomY] == '#' || isTail(x + randomX, y + randomY)
    		  || GameField.map[x + randomX][y + randomY] == 'P' || GameField.map[x + randomX][y + randomY] == 'C'
    		  || isCrashedSnake(x + randomX, y + randomY));
	
	  move(x + randomX, y + randomY, x, y);
	  randomMoveCounter--;
}

public void eat(int newSx, int newSy) {
	
	SnakeElement snakeHead = (SnakeElement) bodyParts.getHead().getData();
	int x = snakeHead.getX(), y = snakeHead.getY();
	
	Object element = GameField.map[newSx][newSy];
	SnakeElement addedPart = new SnakeElement(element, x, y);
	
	bodyParts.eat(addedPart);	

	snakeHead.setX(newSx);
	snakeHead.setY(newSy);
}

public boolean stuck() {
	
	SnakeElement head = (SnakeElement) bodyParts.getHead().getData();
	int x = head.getX();
	int y = head.getY();
	
	int deadStuckCount = 0;
	
	for (int i = -1; i <= 1; i += 2) {
	    if (GameField.map[x + i][y] != ' ') 
	    	deadStuckCount++;
	    if (GameField.map[x][y + i] != ' ') 
	    	deadStuckCount++;
	}
	
	if(deadStuckCount == 4)
		return true;
	
    int wallCount = 0;
	
	for (int i = -1; i <= 1; i += 2) {
	    if (GameField.map[x + i][y] == '#') 
	    	wallCount++;
	    if (GameField.map[x][y + i] == '#') 
	    	wallCount++;
	}
	
	if(wallCount == 3)
		return true;
	
	return false;
}

public void reverseSnake() {
	deleteSnakeOnMap();
	bodyParts.reverseCoordinate();
	bodyParts.reverseList();
	displaySnakeOnMap();
}

public void displaySnakeOnMap() {	
	Node temp = bodyParts.getHead();
	
	while(temp != null) {
		
		SnakeElement bodyPart = (SnakeElement) temp.getData();
		
		char element = (char) bodyPart.getElement();
		int x = bodyPart.getX();
		int y = bodyPart.getY();

		GameField.map[x][y] = element;
		temp = (Node) temp.getLink();		
	}	
}

public void deleteSnakeOnMap() {
    Node temp = bodyParts.getHead();
	
	while(temp != null) {
		
		SnakeElement bodyPart = (SnakeElement) temp.getData();
		
		int x = bodyPart.getX();
		int y = bodyPart.getY();

		GameField.map[x][y] = ' ';
		temp = (Node) temp.getLink();		
	}
}

public boolean isTail(int newSx, int newSy) { // checks tail
		
    Node temp = bodyParts.getHead();
    
    if(temp.getLink() == null) 
    	return false;
	
	while(temp != null) {
		
		SnakeElement bodyPart = (SnakeElement) temp.getData();
		
		int x = bodyPart.getX();
		int y = bodyPart.getY();

		if(x == newSx && y == newSy)
			return true;
		
		temp = (Node) temp.getLink();		
	}		
	return false;
}

public boolean checkTrap() { // checks for trap in 3 * 3 area	
	
	SnakeElement head = (SnakeElement) bodyParts.getHead().getData();
	int x = head.getX();
	int y = head.getY();
	
	for(int i = -1; i <= 1; i++) {
		for(int j = -1; j <= 1; j++) {			
			if(GameField.map[x + i][y + j] == '=') 
				return true;								
		}	
	}		
	return false;		
}

 public void die() { // snake dies.
	alive = false;	
	bodyParts.deleteAll();	
	deleteSnakeOnMap();
  }
  
 public boolean isAlive() {
	return alive;
 }
 
 public static int snakeNeighborCount(int x, int y) {
		int sX = 0, sY = 0, count = 0;
		for(int i = 0; i < Game.snakes.size(); i++) {
			Node temp = Game.snakes.peek().bodyParts.getHead();
			while(temp != null) {
				SnakeElement element = (SnakeElement) temp.getData();
				sX = element.getX();   sY = element.getY();
				if((sX == x + 1 && sY == y)
					||(sX == x - 1 && sY == y)
					||(sX == x && sY == y + 1)
					||(sX == x && sY == y - 1)) {
					count++;
				}
				temp = (Node) temp.getLink();
			}
			
			Game.snakes.enqueue(Game.snakes.dequeue());
		}
	 
	 return count;
 }
 
 public static boolean isCrashedSnake(int x, int y) {
		int sX = 0, sY = 0;
		boolean flag = false;
		for(int i = 0; i < Game.snakes.size(); i++) {
			Node temp = Game.snakes.peek().bodyParts.getHead();
			while(temp != null) {
				SnakeElement element = (SnakeElement) temp.getData();
				sX = element.getX();   sY = element.getY();
				if(sX == x && sY == y) {
					flag = true;
				}
				temp = (Node) temp.getLink();
			}
			
			Game.snakes.enqueue(Game.snakes.dequeue());
		}
		
		return flag;
 }

}

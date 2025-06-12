import java.util.Random;

public class SingleLinkedList {

   private Node head;
   
   public Node getHead() {
	   return head;
   }
	 
   public void eat(SnakeElement BodyPart) {
	   
	  	 Node newnode = new Node(BodyPart);
	  	 
	  	 if (head == null)  // list is empty
	         head = newnode; 
		   
	  	 else if (head.getLink() == null)  
	         head.setLink(newnode); 
	        
	     else { 	 
	    	 Node next = (Node) head.getLink();
	    	 
	    	 head.setLink(newnode);
	    	 newnode.setLink(next);
	     }	         
	}
   
   public void collisionType1(SingleLinkedList snake, int count) {
	   Node temp = head;
	   for(int i = 0; i < count; i++) {
		   if(temp.getLink() != null) {
			   temp = (Node) temp.getLink();
		   }
	   }
	   
	   SnakeElement element = (SnakeElement) temp.getData();
	   int oldX = element.getX(), oldY = element.getY();
 
	   Node temp2 = snake.getHead();
	   while(temp2.getLink() != null) {
		   element = (SnakeElement) temp2.getData();
		   int x = element.getX(), y = element.getY();
		   Random random = new Random();
           int randomx = 0, randomy = 0;

           int lastmove1 = 4;

           if (!(GameField.map[x][y + 1] == '#' || GameField.map[x][y + 1] == 'P'
        	   || GameField.map[x][y + 1] == 'P' || Snake.isCrashedSnake(x, y + 1))) {
               randomy = 1;
           } else if (!(GameField.map[x][y - 1] == '#' || GameField.map[x][y - 1] == 'P'
            	   || GameField.map[x][y - 1] == 'P' || Snake.isCrashedSnake(x, y - 1))) {
               randomy = -1;
           } else if (!(GameField.map[x - 1][y] == '#' || GameField.map[x - 1][y] == 'P'
            	   || GameField.map[x - 1][y] == 'P' || Snake.isCrashedSnake(x - 1, y))) {
               randomx = -1;
           } else if (!(GameField.map[x + 1][y] == '#' || GameField.map[x + 1][y] == 'P'
            	   || GameField.map[x + 1][y] == 'P' || Snake.isCrashedSnake(x + 1, y))) {
               randomx = 1;
           } else {

               do {
                   randomx = 0;
                   randomy = 0;

                   lastmove1 = random.nextInt(4);
                   switch (lastmove1) {

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
               } while (GameField.map[x + randomx][y + randomy] == '#' || GameField.map[x + randomx][y + randomy] == 'P'
                	   || GameField.map[x + randomx][y + randomy] == 'C' || Snake.isCrashedSnake(x + randomx, y + randomy));
           }
           
           element.setX(oldX + randomx);
           element.setY(oldY + randomy);
           
           oldX = element.getX();
           oldY = element.getY();
		   temp2 = (Node) temp2.getLink();
	   }
	
	   Node afterTemp = null;
	   if(temp.getLink() != null) {
		   afterTemp = (Node) temp.getLink();
	   }
	   
	   if(afterTemp != null) {
		   temp2.setLink(afterTemp);
		   while(afterTemp != null) {
			   element = (SnakeElement) temp2.getData();
			   int x = element.getX(), y = element.getY();
			   Random random = new Random();
	           int randomx = 0, randomy = 0;

	           int lastmove1 = 4;

	           if (!(GameField.map[x][y + 1] == '#' || GameField.map[x][y + 1] == 'P'
	        	   || GameField.map[x][y + 1] == 'P' || Snake.isCrashedSnake(x, y + 1))) {
	               randomy = 1;
	           } else if (!(GameField.map[x][y - 1] == '#' || GameField.map[x][y - 1] == 'P'
	            	   || GameField.map[x][y - 1] == 'P' || Snake.isCrashedSnake(x, y - 1))) {
	               randomy = -1;
	           } else if (!(GameField.map[x - 1][y] == '#' || GameField.map[x - 1][y] == 'P'
	            	   || GameField.map[x - 1][y] == 'P' || Snake.isCrashedSnake(x - 1, y))) {
	               randomx = -1;
	           } else if (!(GameField.map[x + 1][y] == '#' || GameField.map[x + 1][y] == 'P'
	            	   || GameField.map[x + 1][y] == 'P' || Snake.isCrashedSnake(x + 1, y))) {
	               randomx = 1;
	           } else {

	               do {
	                   randomx = 0;
	                   randomy = 0;

	                   lastmove1 = random.nextInt(4);
	                   switch (lastmove1) {

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
	               } while (GameField.map[x + randomx][y + randomy] == '#' || GameField.map[x + randomx][y + randomy] == 'P'
	                	   || GameField.map[x + randomx][y + randomy] == 'C' || Snake.isCrashedSnake(x + randomx, y + randomy));
	           }
	           
	           element.setX(oldX + randomx);
	           element.setY(oldY + randomy);
	           
	           oldX = element.getX();
	           oldY = element.getY();
			   afterTemp = (Node) afterTemp.getLink();
		   }
	   }
	   snake.getHead().setLink(temp);
   }
   
   public void collisionType2andType3(int count) {
	   Node temp = head;
	   for(int i = 0; i < count; i++) {
		   temp = (Node) temp.getLink();
	   }
	   
	   SnakeElement element = (SnakeElement) temp.getData();
	   int headX = element.getX(), headY = element.getY();
	   Snake snake = new Snake(headX, headY);
	   
	   temp = (Node) temp.getLink();
	   while(temp != null) {
		   SnakeElement element1 = (SnakeElement) temp.getData();
		   int sX = element1.getX(), sY = element1.getY();
		   snake.eat(sX, sY);
		   temp = (Node) temp.getLink();
	   }
	   
	   Game.snakes.enqueue(snake);
	   snake.displaySnakeOnMap();
	   temp = head;
	   for(int i = 0; i < count; i++) {
		   temp = (Node) temp.getLink();
	   }
	   temp.setLink(null);
   }
   
   public void reverseCoordinate() {  
	   
	  Node next = (Node) head.getLink();	   
	   
	  SnakeElement snakeHead = (SnakeElement) head.getData();
	  
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
	   
	   snakeHead.setX(currentX);
	   snakeHead.setY(currentY);
   }
   
   public void reverseList() {
	 if (head.getLink() != null) {
	      
		 Node prev = null;
         Node current = head;
         Node next = null;

         while (current != null) {
             next = (Node) current.getLink();  
             current.setLink(prev);   
             prev = current;        
             current = next;  
         }
         head = prev; 
         
         current = head; 
         prev = null;
         
         while(current.getLink() != null) {
        	 prev = current;
        	 current = (Node) current.getLink();
         }
         
         prev.setLink(null);
         current.setLink(head);
         head = current;        
	   }
   }
   
   public void deleteAll() {
	   Node temp = head;
	   
	   while(temp != null) {
		   SnakeElement bodyPart = (SnakeElement) temp.getData();
		   int x = bodyPart.getX(), y = bodyPart.getY();
		   GameField.map[x][y] = ' ';
		   bodyPart.setElement(' '); bodyPart.setX(0); bodyPart.setY(0);
		   temp = (Node) temp.getLink();
	   }
	   GameField.map[0][0] = '#';
   }
   
   public int size() {
	   int count = 0;
	   Node temp = head;
	   
	   while(temp != null) {
		   count++;
		   temp = (Node) temp.getLink();
	   }
	   return count;
   }
   
   public void deleteFirst() {
	   if(head == null) {
		   
	   } else if(head.getLink() == null){
		   head = null;
	   } else {
		   head = (Node) head.getLink();
	   }
   }
}
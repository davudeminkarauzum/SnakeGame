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
	   
	   Node afterTemp = null;
	   if(temp.getLink() != null) {
		   afterTemp = (Node) temp.getLink();
	   }
	   
	   Node temp2 = snake.getHead();
	   while(temp2.getLink() != null) {
		   temp2 = (Node) temp2.getLink();
	   }
	
	   if(afterTemp != null) {
		   temp2.setLink(afterTemp);
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
	   Game.snakes.enqueue(snake);
	   
	   temp = (Node) temp.getLink();
	   while(temp != null) {
		   SnakeElement element1 = (SnakeElement) temp.getData();
		   int sX = element1.getX(), sY = element1.getY();
		   snake.eat(sX, sY);
		   temp = (Node) temp.getLink();
	   }
	   
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
		   bodyPart.setElement(' '); bodyPart.setX(' '); bodyPart.setY(' ');
		   temp = (Node) temp.getLink();
	   }
	   
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

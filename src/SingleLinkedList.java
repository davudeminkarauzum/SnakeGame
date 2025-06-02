package snakegame;

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
		bodyPart.setElement(' ');
		temp = (Node) temp.getLink();
	 }
 }
	
		
}

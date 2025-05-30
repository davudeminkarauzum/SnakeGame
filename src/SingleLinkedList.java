public class SingleLinkedList {

	Node head;
	
	public void eating(Object input) { //This function will be implemented to Game class
		if(head == null) {
			head = new Node(input);
		} else {
			Node temp = head, newnode = new Node(input);
			temp.getLink();
			
			if(temp != null) {
				newnode.setLink(temp);
			}
			head.setLink(newnode);
		}
	}
	
	public SingleLinkedList reverse(SingleLinkedList snack) {
		return snack;
	}

}

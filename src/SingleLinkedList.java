public class SingleLinkedList {

	Node head;
	
	public void eating(Object input) {
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

	
	public int size() {
		if(head == null) {
			return 0;
		} else {
			int calculator = 0;
			Node temp = head;
			while(temp != null) {
				temp = (Node) temp.getLink();
				calculator++;
			}
			return calculator;
		}
	}
	
}

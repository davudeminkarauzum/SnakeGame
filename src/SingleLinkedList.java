public class SingleLinkedList {

	Node head;
	
	public void add(Object data) {
		if(head == null) {
			head = new Node(data);
		} else {
			Node temp = head;
			while(temp.getLink() != null) {
				temp = (Node) temp.getLink();
			}
			Node toAdd = new Node(data);
			temp.setLink(toAdd);
		}
	}
	
	public void display() {
		if(head == null) {
			System.out.println("The list is empty.");
		} else {
			Node temp = head;
			while(temp != null) {
				System.out.print(temp.getData() + " ");
				temp = (Node) temp.getLink();
			}
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

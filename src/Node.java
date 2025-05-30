public class Node {
	public Object data;
	Node link, next, prev;
	
	public Node(Object data) {
		this.data = data;   this.link = null;   this.next = null;   this.prev = null;
	}
	
	//SingleLinkedList functions
	public Object getData() { return data; }
	public void setData(Object data) { this.data = data; }
	
	public Node getLink() { return link; }
	public void setLink(Node link) { this.link = link; }
	
	//DoubleLinkedList functions
	public Node getNext() { return next; }
	public void setNext(Node next) { this.next = next; }
	
	public Node getPrev() { return prev; }
	public void setPrev(Node prev) { this.prev = prev; }
	
}

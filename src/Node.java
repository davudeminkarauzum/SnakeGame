public class Node {
		
	Object data;
	Node link;
	
	public Node(Object data) {
		this.data = data;
		this.link = null;
		
	}
	public Object getData() {
		return data;
	}
	
	public void setData(Object data) {
		this.data = data;
	}
	
	public Object getLink() {
		return link;
	}
	
	public void setLink(Node link) {
		this.link = link;
	}
}



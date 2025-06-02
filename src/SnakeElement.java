package snakegame;

public class SnakeElement { 
	
	private Object element;	// stores data
	private int x; // x coordinate of element
	private int y; // y coordinate of element
		
	public SnakeElement(Object element, int x, int y) { // construct method for new Snake Element
		this.element = element;
		this.x = x;
		this.y = y;
	}
	
	public Object getElement() {
		return element;
	}
		
	public void setElement(Object data) {
		this.element = data;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}


public class Stack {

	public int top;
	public Object elements[];
	
	Stack(int size) 
		top = -1;
		elements = new Object[size];
	}
	
	public void push(Object data) {
		if(!isFull()) {
			top++;
			elements[top] = data;
		} else {
			System.out.println("The stack is full");
		}
	}
	
	public Object pop() {
		if(!isEmpty()) {
			Object data = elements[top];
			top--;
			return data;
		} else {
			return "The stack is empty.";
		}
	}
	
	public Object peek() {
		if(!isEmpty()) {
			return elements[top];
		} else {
			return "The stack is empty.";
		}
	}
	
	public boolean isFull() {
		return top + 1 == elements.length;
	}
	
	public boolean isEmpty() {
		return top == -1;
	}
}

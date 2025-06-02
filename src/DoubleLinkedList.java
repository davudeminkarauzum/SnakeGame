package snakegame;

public class DoubleLinkedList {

	private DoubleNode head, tail;
	
	public void add(Object input) {
		DoubleNode newNode = new DoubleNode(input);
		if(head == null) {
			head = newNode;
			tail = newNode;
		} else {
			DoubleNode temp = head;
			while(temp.getNext() != null) {
				temp = temp.getNext();
			}
			temp.setNext(newNode);
			newNode.setPrev(temp);
			tail = newNode;
		}
	}
	
	public void display() {
		DoubleNode temp = head;
		while(temp != null) {
			System.out.println(temp.getData());
			temp = temp.getNext();
		}
	}
	
	public int size() {
		int count = 0;
		DoubleNode temp = head;
		while(temp != null) {
			count++;
			temp = temp.getNext();
		}
		
		return count;
	}
	
	public void addOrderedScores(Object input) {
		DoubleNode newNode = new DoubleNode(input);
		if(head == null) {
			head = newNode;
			tail = newNode;
		} else {
			String s1 = (String) input, tempS = "";
			int inputScore = 0;
			for(int i = 0; i < s1.length(); i++) {
        		char ch = s1.charAt(i);
        		if(ch == ' ') {
        			tempS = "";
        		} else {
        			tempS += ch;
        		}
        		
    			if(i == s1.length() - 1) {
    				inputScore = Integer.parseInt(tempS);
    			}
			}
			
			DoubleNode temp = head;
			boolean flag = false;
			
            while(temp != null && !flag) {
            	String s2 = (String) temp.getData();
            	int score = 0;
                tempS = "";
    			for(int i = 0; i < s2.length(); i++) {
    				char ch = s2.charAt(i);
            		if(ch == ' ') {
            			tempS = "";
            		} else {
            			tempS += ch;
            		}
            		
        			if(i == s2.length() - 1) {
        				score = Integer.parseInt(tempS);
        			}
    			}
    			
    			if(inputScore >= score) {
    				flag = true;
    			} else {
    				temp = temp.getNext();
    			}
            }
            
			if(temp == head) { //To the beginning
				newNode.setNext(head);
				head.setPrev(newNode);
				head = newNode;
			} else if(temp == null) { //To the end
				tail.setNext(newNode);
				newNode.setPrev(tail);
				tail = newNode;
			} else { //To between two nodes
				DoubleNode prev = temp.getPrev();
				newNode.setPrev(prev);
				newNode.setNext(temp);
				temp.setPrev(newNode);
				if (prev != null) {
				    prev.setNext(newNode);
				}

			}
		}
	}
	
	public Object returnData(int index) {
		DoubleNode temp = head;
		for(int i = 0; i < index; i++) {
			temp = temp.getNext();
		}
		return temp.getData();
	}
	
}

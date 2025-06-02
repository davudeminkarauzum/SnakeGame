package snakegame;

public class Trap {
	
	private int x; // x coordinate of trap
	private int y; // y coordinate of trap
	private int time; // trap's placement time

	Trap(int x, int y, int time) { // construct method for creating new traps
		
		this.x = x; // initializing attributes
		this.y = y;
		this.time = time;
	}
	
	//---------getter setter methods----------
	
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

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}	
	//----------------------------------------
	
	public boolean checkSnake() { // checks for snake in 3 * 3 area		
		
		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 1; j++) {				
				if(GameField.map[x + i][y + j] == 'S') 
					return true;								
			}	
		}		
		return false;		
	}
	
	public void boom() { // trap explodes in 3 * 3 area
						
		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 1; j++) {
					
				if(GameField.map[x + i][y + j] != '#' && GameField.map[x + i][y + j] != 'P')
				GameField.map[x + i][y + j] = 'x'; 
			}	
		}			
		time = -1;					
	}
	

}

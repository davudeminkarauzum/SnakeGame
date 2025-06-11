public class Trap {
	
	private int x;
	private int y;
	private int time;
	
	Trap(int x, int y, int time) {
		
		this.x = x;
		this.y = y;
		this.time = time;
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

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
	

	public void boom() {
		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 1; j++) {
				if(!(GameField.map[x + i][y + j] == '#' || GameField.map[x + i][y + j] == 'P'
					|| GameField.map[x + i][y + j] == 'C' || GameField.isTreasure(x + i, y + j)))
				GameField.map[x + i][y + j] = ' '; 
			}	
		}
	}
	
	public void disappear() {
		
		GameField.map[x][y] = ' ';	
		time = -1;
	}
}

public class Piece {

	private boolean king;
	private char team;
	private int row;
	private int col;
	
	public Piece(char team, boolean king, int row, int col){
		this.king = king;
		this.team = team;
		this.row = row;
		this.col = col;
	}
	
	public int getRow(){
		return row;
	}
	
	public int getCol(){
		return col;
	}
	
	public boolean isKing(){
		return king;
	}
	
	public char team(){
		return team;
	}

	public void kingIt(){
		king = true;
	}
	
	public Piece clone(){
		Piece temp = new Piece(team,king,row,col);
		return temp;
	}
}

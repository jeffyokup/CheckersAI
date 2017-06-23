import java.util.ArrayList;
import java.util.Random;

/**
 * The superior AI
 * @author Jeff
 *
 */
public class KINGM3 {

	Board checkerboard;
	Piece[][] brd;
	char color;
	int round = 1;
	Random rand = new Random();
	
	public KINGM3(Board brd, char color){
		this.checkerboard = brd;
		this.color = color;
		this.brd = checkerboard.checkerBoard;
	}
	
	public String getSuperiorMove(){
		String move = "";
		if(round == 0){
			move = randomMove();
		}
		else{
			String protection = protect();
			if(protection.length() != 0){
				System.out.println("Moved piece to " + protection.substring(2) + " TO PROTECT HIS NATION!");
				move = protection;
			}
			else{
				move = randomMove();
			}
		}
		
		round++;
		return move;
	}

	//TODO Fix this method so it doesn't move into dangerous position.
	/**
	 * returns coordinates for a random move
	 * @return
	 */
	private String randomMove(){
		String move = "";
		int row, col;
		while(true){
			row = rand.nextInt(8);
			col = rand.nextInt(8);
			if(brd[row][col] != null && brd[row][col].team() == color){
				if(color == 'r'){
					move = redRandom(row,col);
				}
				else{
					move = blackRandom(row,col);
				}
				if(move.length() != 0){
					move = row + "" + col + move;
					return move;
				}
					
			}
		}
	}

	/**
	 * Check each allied Piece if needs protection.
	 * @return
	 */
	private String protect(){
		String empty = "";
		for(int row = 0; row < 8; row++){
			for(int col = 0; col < 8; col++){
				if(brd[row][col] != null && brd[row][col].team() == color){
					String enemyCord = adjacentEnemy(row,col);

					if(enemyCord.length()!=0){ // If there are adjacent enemies
						String allyMove = allyProtection(row,col); //check if blank spaces are behind it.
						if(allyMove.length() != 0)
							return allyMove;
					}
				}
			}
		}
		return empty;
	}


			//TODO More try catch blocks
	/**
	 * Returns coordinates of a friendly piece, and move cords to protect a piece.
	 * @return
	 */
	private String allyProtection(int row, int col){
		String empty = "";
		if(color == 'r'){
			if(brd[row-1][col+1] != null &&  brd[row-1][col+1].team() != color){//enemy to top right
				if(brd[row+1][col - 1] == null){ // check for blank space
					if(brd[row+2][col - 2].team() == color){//check for ally to bottom left
						empty = empty + "" + (row+2) + "" + (col-2) + "" + (row+1) + "" + (col-1);
						return empty;
					}
					if(brd[row+2][col + 2].team() == color){//check for ally to bottom right
						empty = empty + "" + (row+2) + "" + (col+2) + "" + (row+1) + "" + (col-1);
						return empty;
				}
			}
			if(brd[row-1][col-1] != null && brd[row-1][col-1].team() != color){ //if enemy to top left
				if(brd[row+1][col + 1] == null){
					if(brd[row+2][col - 2].team() == color){//check for ally to bottom left
						empty = empty + "" + (row+2) + "" + (col-2) + "" + (row+1) + "" + (col-1);
						return empty;
					}
					if(brd[row+2][col + 2].team() == color){//check for ally to bottom right
						empty = empty + "" + (row+2) + "" + (col+2) + "" + (row+1) + "" + (col-1);
						return empty;
					}
				}

				}
			}

		}
		else{ // if black
			try{
				if(brd[row+1][col+1] != null && brd[row+1][col+1].team() != color){ //if enemy to bottom right
					if(brd[row-1][col - 1] == null){ // check for blank space
						if(brd[row-2][col - 2].team() == color){//check for ally to bottom left
							empty = empty + "" + (row-2) + "" + (col-2) + "" + (row-1) + "" + (col-1);
							return empty;
						}
						if(brd[row-2][col + 2].team() == color){//check for ally to bottom right
							empty = empty + "" + (row-2) + "" + (col+2) + "" + (row-1) + "" + (col-1);
							return empty;
						}
					}
				}
			}
			catch (Exception e){

			}
			try{
				if(brd[row+1][col-1] != null && brd[row-1][col-1].team() != color){ //if enemy to bottom left
					if(brd[row-1][col + 1] == null){
						if(brd[row-2][col - 2].team() == color){//check for ally to bottom left
							empty = empty + "" + (row-2) + "" + (col-2) + "" + (row-1) + "" + (col+1);
							return empty;
						}
						if(brd[row-2][col + 2].team() == color){//check for ally to bottom right
							empty = empty + "" + (row-2) + "" + (col+2) + "" + (row-1) + "" + (col+1);
							return empty;
						}
					}
				}
			}
			catch (Exception e){

			}
		}
		return empty;
	}

	/**
	 * Returns a string of cords of adjacent enemies.
	 * @param row
	 * @param col
	 * @return
	 */
	private String adjacentEnemy(int row, int col){
		String empty = "";
		if(color == 'r'){
			try{
				if(brd[row-1][col-1] != null && brd[row-1][col-1].team() != color){
					empty = empty + ""  + (row-1) +""+ (col-1);
				}
			}
			catch(Exception e){
			}
			try{
				if(brd[row-1][col+1] != null && brd[row-1][col+1].team() != color){
					empty = empty + "" + (row-1) +""+ (col+1);
				}
			}
			catch(Exception e){
			}
			return empty;
		}
		else{
			try {
				if (brd[row + 1][col - 1] != null && brd[row + 1][col - 1].team() != color) {
					empty = empty + "" + (row + 1) + "" + (col - 1);
				}
			}
			catch(Exception e){
			}
			try{
				if(brd[row+1][col+1] != null && brd[row+1][col+1].team() != color){
					empty = empty + "" + (row+1) +""+ (col+1);
				}
			}
			catch(Exception e){
			}
			return empty;

		}

	}

	
	private String redRandom(int startRow, int startCol){
		ArrayList<String> pos = new ArrayList<String>();
		try{
			if(brd[startRow - 1][startCol - 1] == null)
				pos.add((startRow-1) + "" + (startCol - 1));
		}
		catch(Exception e){
		}
		try{
			if(brd[startRow - 1][startCol + 1] == null)
				pos.add((startRow-1) + "" + (startCol + 1));
		}
		catch(Exception e){
		}
		if(pos.size() == 1)
			return pos.get(0);
		else if(pos.size() == 2){
			int num = rand.nextInt(2);
			return pos.get(num);
		}
		else
			return "";
	}
	
	private String blackRandom(int startRow, int startCol){
		ArrayList<String> pos = new ArrayList<String>();
		try{
			if(brd[startRow + 1][startCol + 1] == null)
				pos.add((startRow+1) + "" + (startCol + 1));
		}
		catch(Exception e){
		}
		try{
			if(brd[startRow + 1][startCol - 1] == null)
				pos.add((startRow+1) + "" + (startCol - 1));
		}
		catch(Exception e){
		}
		if(pos.size() == 1)
			return pos.get(0);
		else if(pos.size() == 2){
			int num = rand.nextInt(2);
			return pos.get(num);
		}
		else
			return "";
	}
	
	
}

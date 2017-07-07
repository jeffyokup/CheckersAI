public class Board {

	public Piece[][] checkerBoard = new Piece[8][8];
	public char turn = 'b';

	public Board(){
		for(int row = 0; row < 3; row++){
			for(int col = 0; col < 8; col++){
				if(row % 2 == 0){
					if(col % 2 != 0)
						checkerBoard[row][col] = new Piece('b', false, row, col);
				}
				else{
					if(col % 2 == 0)
						checkerBoard[row][col] = new Piece('b', false, row, col);
				}
			}
		}
		
		for(int row = 5; row < 8; row++){
			for(int col = 0; col < 8; col++){
				if(row % 2 == 0){
					if(col % 2 != 0)
						checkerBoard[row][col] = new Piece('r', false, row, col);
				}
				else{
					if(col % 2 == 0)
						checkerBoard[row][col] = new Piece('r', false, row, col);
				}
			}
		}
		
	}

	
	/*
	 *NEED TO BE ABLE TO CHECK FOR KILLS! 
	 * 
	 */
	
	
	/**
	 * Attempts to move a piece
	 * @return True if move was successful, false if move unsuccessful
	 */
	public boolean movePiece(String start, String moves){
		String realStart = convert(start);
		int rowStart = Integer.parseInt(realStart.charAt(0) + "");
		int colStart = Integer.parseInt(realStart.charAt(1) + "");
		Piece piece = checkerBoard[rowStart][colStart].clone(); //Piece being moved

		for(int i = 0; i < moves.length(); i += 2){ // go through each coordinate
			String tempMove = convert(moves.substring(i, i + 2));
			int rowEnd = Integer.parseInt(tempMove.charAt(0) + "");
			int colEnd = Integer.parseInt(tempMove.charAt(1) + "");
			
			if(!checkForMoveErrors(rowStart, colStart, rowEnd, colEnd, piece)){
				System.out.println("Move error");
				return false;
			}
			
			boolean isAKill = false;
			//Check whether it is move or kill
			if(Math.abs(rowEnd - rowStart) == 2){
				isAKill = true;
			}
			boolean success = false;
			if(isAKill){
				success = goodKill(rowStart, colStart, rowEnd, colEnd, piece);
			}
			else{
				success = regularMove(rowStart, colStart, rowEnd, colEnd, piece);
			}
			
			if(!success){
				System.out.println("Move error");
				return false;
			}
			rowStart = rowEnd;
			colStart = colEnd;
		}
		return true;
	}
	
	public void changeTurn(){
		if(turn == 'b')
			turn = 'r';
		else
			turn = 'b';
	}

	private boolean regularMove(int rowStart,int colStart, int rowEnd, int colEnd, Piece piece){
		boolean isKing = piece.isKing();
		if(isKing){ //move for King
			if(fairKingMove(rowStart, colStart, rowEnd, colEnd)){
				updatePiecePosition(rowStart, colStart, rowEnd, colEnd, piece);
			}
			else{
				System.out.println("Invalid King move");
				return false;
			}
		}
		else{ // move for regular Piece
			if(piece.team() == 'r'){
				if(fairRedMove(rowStart, colStart, rowEnd, colEnd)){
					updatePiecePosition(rowStart, colStart, rowEnd, colEnd, piece);
				}
				else{
					System.out.println("Invalid Red Move");
					return false;
				}
			}
			else{
				if(fairBlackMove(rowStart, colStart, rowEnd, colEnd)){
					updatePiecePosition(rowStart, colStart, rowEnd, colEnd, piece);
				}
				else{
					System.out.println("Invalid Black Move");
					return false;
				}
			}
		}
		return true;
	}
	
	
	
	private void updatePiecePosition(int rowStart,int colStart, int rowEnd, int colEnd, Piece piece){
		checkerBoard[rowStart][colStart] = null;
		checkerBoard[rowEnd][colEnd] = new Piece(piece.team(), piece.isKing(), rowEnd, colEnd);
		piece = checkerBoard[rowEnd][colEnd];
	}
	
	/**
	 * Returns true if NO ERRORS. False indicated error
	 * @param rowStart
	 * @param colStart
	 * @param rowEnd
	 * @param colEnd
	 * @param piece
	 * @return
	 */
	private boolean checkForMoveErrors(int rowStart,int colStart, int rowEnd, int colEnd, Piece piece){
		if(!pieceAtCord(rowStart,colStart)){ //checks if piece is at coordinates. 
			System.out.println("Invalid start cordinates");
			return false;
		}
		
		if(piece.team() != turn){ //checks team and whether there is a piece at the coordinates
			System.out.println("Not on your team");
			return false;
		}
		if(pieceAtCord(rowEnd,colEnd)){
			System.out.println("Piece at end Coordinates");
		}
		if(rowEnd > 7 || rowEnd < 0 || colEnd > 7 || colEnd < 0){
			System.out.println("Out of Bounds");
			return false;
		}
		return true;
	}
	
	public boolean pieceAtCord(int row, int col){
		if(checkerBoard[row][col] == null)
			return false;
		return true;
	}
	
	/**
	 * Regular Jump
	 * @param rowStart
	 * @param colStart
	 * @param rowEnd
	 * @param colEnd
	 * @return
	 */
	public boolean fairRedMove(int rowStart, int colStart, int rowEnd, int colEnd){
		if((rowEnd == rowStart - 1) && ((colEnd == colStart + 1) || (colEnd == colStart - 1)))
			if(checkerBoard[rowEnd][colEnd] == null)
			return true;
		return false;
	}
	
	/**
	 * Regular Jump
	 * @param rowStart
	 * @param colStart
	 * @param rowEnd
	 * @param colEnd
	 * @return
	 */
	public boolean fairBlackMove(int rowStart, int colStart, int rowEnd, int colEnd){
		if((rowEnd == rowStart + 1) && ((colEnd == colStart + 1) || (colEnd == colStart - 1)))
			if(checkerBoard[rowEnd][colEnd] == null)
			return true;
		return false;
	}
	
	/**
	 * Regular King Jump
	 * @param rowStart
	 * @param colStart
	 * @param rowEnd
	 * @param colEnd
	 * @return
	 */
	private boolean fairKingMove(int rowStart, int colStart, int rowEnd, int colEnd){
		if((rowEnd == rowStart + 1) && ((colEnd == colStart + 1) || (colEnd == colStart - 1)))
			if(checkerBoard[rowEnd][colEnd] == null)
				return true;
		 if((rowEnd == rowStart - 1) && ((colEnd == colStart + 1) || (colEnd == colStart - 1)))
				if(checkerBoard[rowEnd][colEnd] == null)
					return true;
		return false;
	}
	
	
	/**
	 * Indicated if Kill is successfull. If true, KILLS piece, and MOVES the attacker. 
	 * @param rowStart
	 * @param colStart
	 * @param rowEnd
	 * @param colEnd
	 * @param piece
	 * @return
	 */
	private boolean goodKill(int rowStart,int colStart, int rowEnd, int colEnd, Piece piece){
		boolean killSuccess = false;
		if(piece.isKing()){
			killSuccess = fairKingKill( rowStart, colStart,  rowEnd,  colEnd, piece);
		}
		else if(piece.team() == 'r'){
			killSuccess = fairRedKill( rowStart, colStart,  rowEnd,  colEnd);
		}
		else{
			killSuccess = fairBlackKill( rowStart, colStart,  rowEnd,  colEnd);
		}
		
		//moves the attacker
		if(killSuccess){
			checkerBoard[rowStart][colStart] = null;
			checkerBoard[rowEnd][colEnd] = piece;
		}
		
		return killSuccess;
	}

	
	private boolean fairRedKill(int rowStart, int colStart, int rowEnd, int colEnd){
		if((rowEnd == rowStart - 2) && (colEnd == colStart - 2)){ //if Red is moving up 2 and 2 spaces to left
				if(pieceAtCord(rowEnd+1,colEnd+1)){//enemy topleft of start
					if(checkerBoard[rowEnd+1][colEnd+1].team() != turn){ // check if enemy
						System.out.println("Killed Piece at" + (rowEnd + 1) + " " + (colEnd + 1));
						checkerBoard[rowEnd+1][colEnd+1] = null;
						return true;
					}
				}
		}
		if((rowEnd == rowStart - 2) && (colEnd == colStart + 2)){ //if Red is moving up, and 2 to the right
				if(pieceAtCord(rowEnd+1,colEnd-1)){//if enemy topRight of start
					if(checkerBoard[rowEnd+1][colEnd-1].team() != turn){
						System.out.println("Killed Piece at" + (rowEnd + 1) + " " + (colEnd - 1));
						checkerBoard[rowEnd+1][colEnd-1] = null;
						return true;
					}
				}
		}
		return false;
		
	}
	
	
	private boolean fairBlackKill(int rowStart, int colStart, int rowEnd, int colEnd){
		if((rowEnd == rowStart + 2) && (colEnd == colStart + 2)) { //if black is moving down and 2 to the right
			if (pieceAtCord(rowEnd - 1, colEnd - 1)) {//bottom right
				if (checkerBoard[rowEnd - 1][colEnd - 1].team() != turn) {
					System.out.println("Killed Piece at" + (rowEnd - 1) + " " + (colEnd - 1));
					checkerBoard[rowEnd - 1][colEnd - 1] = null;
					return true;
				}
			}

		}
		if((rowEnd == rowStart + 2) && (colEnd == colStart - 2)) { //if black is moving down and 2 to the left
			if(pieceAtCord(rowEnd-1,colEnd+1)){//bottom left
				if(checkerBoard[rowEnd-1][colEnd+1].team() != turn){
					System.out.println("Killed Piece at" + (rowEnd - 1) + " " + (colEnd + 1));
					checkerBoard[rowEnd-1][colEnd+1] = null;
					return true;
				}
			}
		}
		return false;

	}


	/**
	 *
	 * @param rowStart
	 * @param colStart
	 * @param rowEnd
	 * @param colEnd
	 * @param piece being movied
	 * @return
	 */
	private boolean fairKingKill(int rowStart, int colStart, int rowEnd, int colEnd, Piece piece){
		char color = piece.team();
		if(rowEnd < rowStart){ // Moving up
			if(colEnd > colStart){//moving right
				if(checkerBoard[rowEnd+1][colEnd-1].team() != color){
					checkerBoard[rowEnd+1][colEnd-1] = null;
					System.out.println("Killed Piece at" + (rowEnd + 1) + " " + (colEnd - 1));
					return true;
				}
			}
			else{//moving left
				if(checkerBoard[rowEnd+1][colEnd+1].team() != color){
					checkerBoard[rowEnd+1][colEnd+1] = null;
					System.out.println("Killed Piece at" + (rowEnd + 1) + " " + (colEnd + 1));
					return true;
				}
			}
		}
		else{ //moving down
			if(colEnd > colStart){//moving right
				if(checkerBoard[rowEnd-1][colEnd-1].team() != color) {
					checkerBoard[rowEnd - 1][colEnd - 1] = null;
					System.out.println("Killed Piece at" + (rowEnd - 1) + " " + (colEnd - 1));
					return true;
				}
			}
			else{//moving left
				if(checkerBoard[rowEnd-1][colEnd+1].team() != color){
					checkerBoard[rowEnd-1][colEnd+1] = null;
					System.out.println("Killed Piece at" + (rowEnd - 1) + " " + (colEnd + 1));
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Returns numbers cordinates from letter num cordinates.
	 * @param cord
	 * @return
	 */
	public String convert(String cord){
		String firstLetter = cord.charAt(0) + "";
		String num = "";
		switch(firstLetter){
		case "a" : num = "0";
		break;
		case "b" : num = "1";
		break;
		case "c" : num = "2";
		break;
		case "d" : num = "3";
		break;
		case "e" : num = "4";
		break;
		case "f" : num = "5";
		break;
		case "g" : num = "6";
		break;
		case "h" : num = "7";
		break;
		default: num = firstLetter;
		}
		String complete = num + "" + cord.charAt(1);
		return complete;
	}
	
	public void printBoard(){
		System.out.print("  ");
		for(int i = 0; i < 8; i++){
			System.out.print(i + " ");
		}
		System.out.println();
		String letters = "abcdefgh";
		for(int row = 0; row < 8; row++){
			System.out.print(letters.charAt(row) + " ");
			for(int col = 0; col < 8; col++){
				if(checkerBoard[row][col] == null){
					System.out.print("  ");
				}
				else if(checkerBoard[row][col].team() == 'r'){
					if(checkerBoard[row][col].isKing())
						System.out.print("R ");
					else
					System.out.print("r ");
				}
				else{
					if(checkerBoard[row][col].isKing())
						System.out.print("B ");
					else
						System.out.print("b ");
				}
			}
			System.out.println();
		}
	}

	public void kingMe(){
		for(int i = 0; i < 8; i++){
			if(checkerBoard[0][i] != null && checkerBoard[0][i].team() == 'r')
				checkerBoard[0][i].kingIt();
		}
		for(int i = 0; i < 8; i++){
			if(checkerBoard[7][i] != null && checkerBoard[7][i].team() == 'b')
				checkerBoard[7][i].kingIt();
		}
	}
}

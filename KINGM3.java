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
	Random rand = new Random();
	
	public KINGM3(Board brd, char color){
		this.checkerboard = brd;
		this.color = color;
		this.brd = checkerboard.checkerBoard;
	}

	public String getSuperiorMove(){
		String move = "";
		//Sees what moves are available.

		String forcedDouble = getForcedDoubleJump(); //Checks for forced DOUBLE jump
		if(forcedDouble.length() != 0){
			move = forcedDouble;
			return move;
		}

		String protection = protect(); //CHECKS FOR PROTECTION
		if(protection.length() != 0){
			System.out.println("Moved piece to " + protection.substring(2) + " TO PROTECT HIS NATION!");
			return protection;
		}

		move = randomMove();

		return move;
	}

	//Fix, if piece is vulnerable, save it.
	public String getSuperiorMove(ArrayList<String> kills){
		    String move = "";
			//Sees what moves are available.
			if(kills.size() != 0){
				for(int i = 0; i < kills.size(); i++){
					kills.set(i,canDoDouble(kills.get(i)));
					if(kills.get(i).length()>4)
						return kills.get(i);
				}
				return kills.get(0);
			}

			String forcedDouble = getForcedDoubleJump(); //Checks for forced DOUBLE jump
			if(forcedDouble.length() != 0)
			return forcedDouble;

			String protection = protect(); //CHECKS FOR PROTECTION
			if(protection.length() != 0 && protection.length() > 2){
				System.out.println("Moved piece to " + protection.substring(2) + " TO PROTECT HIS NATION!");
				return protection;
			}
			String kingMove = safeKingMove();
			if(kingMove.length() != 0)
				return kingMove;

		    move = randomMove();
		return move;
	}

	private String safeKingMove(){
		ArrayList<String> kingCordinates = new ArrayList<String>();
		String move = "";
		for(int row = 0; row < 8; row++){ // add all piece coordinates to list.
			for(int col = 0; col < 8; col++){
				if(brd[row][col] != null && brd[row][col].team() == color && brd[row][col].isKing()){
					kingCordinates.add(row+""+col);
				}
			}
		}

		for(int i = 0; i < kingCordinates.size(); i++){
			int row = Integer.parseInt(kingCordinates.get(i).charAt(0)+"");int col = Integer.parseInt(kingCordinates.get(i).charAt(1)+"");
			ArrayList<String> list = skm(row,col);
			if(list.size() != 0){
				int num = rand.nextInt(list.size());
				return list.get(num);
			}
		}
		return move;
	}

	/**
	 * Returns a list of all safe Moves
	 * @param row
	 * @param col
	 * @return
	 */
	private ArrayList<String> skm(int row, int col){
		ArrayList<String> safeMoves = new ArrayList<>();
		try{
			if(safeUpMove(row,col,row-1,col-1)){
				safeMoves.add(row+""+col+""+(row-1)+""+(col-1));
			}
			if(safeUpMove(row,col,row-1,col+1)){
				safeMoves.add(row+""+col+""+(row-1)+""+(col+1));
			}
		}catch (Exception e){}
		try{
			if(safeDownMove(row,col,row+1,col+1)){
				safeMoves.add(row+""+col+""+(row+1)+""+(col+1));
			}
			if(safeDownMove(row,col,row+1,col-1)){
				safeMoves.add(row+""+col+""+(row+1)+""+(col-1));
			}
		}catch (Exception e){}

		return safeMoves;
	}




	private String canDoDouble(String move){
		String end = move.substring(2,4);
		int endRow = Integer.parseInt(end.charAt(0)+"");int endCol = Integer.parseInt(end.charAt(1)+"");
		if(color == 'r'){
			try{
				if(brd[endRow-1][endCol-1].team() != color && brd[endRow-2][endCol-2] == null)
					return (move + (endRow-2)+""+(endCol-2));
			}catch (Exception e){}
			try{
				if(brd[endRow-1][endCol+1].team() != color && brd[endRow-2][endCol+2] == null)
					return (move + (endRow-2)+""+(endCol+2));
			}catch (Exception e){}
		}
		else{//if black
			try{
				if(brd[endRow+1][endCol-1].team() != color && brd[endRow+2][endCol-2] == null)
					return (move + (endRow+2)+""+(endCol-2));
			}catch (Exception e){}
			try{
				if(brd[endRow+1][endCol+1].team() != color && brd[endRow+2][endCol+2] == null)
					return (move + (endRow+2)+""+(endCol+2));
			}catch (Exception e){}
		}
		return move;
	}


	private String getForcedDoubleJump(){
		String empty = "";

		ArrayList<String> pieceCordinates = new ArrayList<String>();
		for(int row = 0; row < 8; row++){ // add all piece coordinates to list.
			for(int col = 0; col < 8; col++){
				if(brd[row][col] != null && brd[row][col].team() == color){
					pieceCordinates.add(row+""+col);
				}
			}
		}

		for(int i = 0; i < pieceCordinates.size(); i++){ //check each piece for forced double jump
			String move="";
			if(color == 'r')
			 move = checkPieceForForcedDoubleRed(pieceCordinates.get(i),"","","","",0,0,0);
			else if(color == 'b'){
			 move = checkPieceForForcedDoubleBlack(pieceCordinates.get(i),"","","","",0,0,0);
			}
			if(move.length() != 0) {
				System.out.println("FORCED A DOUBLE JUMP PUNK !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				return move;
			}
		}

		return empty;
	}

	//trying recursion, with red first
	//TODO Thinks it forces when actually doesn't

	//TODO certain situations kinda allow double jump but there are 2 enemies so enemy choooses safe jump
	/**
	 *
	 * @param startCord
	 * @param arrived
	 * @param enemyCordinates Only ever returns 1 pair of cordinates
	 * @param wentRight
	 * @param wentLeft
	 * @param goodMove
	 * @return
	 */
	private String checkPieceForForcedDoubleRed(String startCord, String arrived, String enemyCordinates, String enemyArrived, String ally, int wentRight, int wentLeft, int goodMove ) {
		String empty = "";
		String enemyCord = "";
		int StartRow = Integer.parseInt(startCord.charAt(0) + "");
		int StartCol = Integer.parseInt(startCord.charAt(1) + "");
		int newRow = -1;
		int newCol = -1;
		if (arrived.length() != 0) {
			newRow = Integer.parseInt(arrived.charAt(0) + "");
			newCol = Integer.parseInt(arrived.charAt(1) + "");
		}

		if (goodMove == 1)//base case
			return startCord + arrived;

		if(ally.length() != 0) { //processess 1 ally
			int allyRow = Integer.parseInt(ally.charAt(0) + "");
			int allyCol = Integer.parseInt(ally.charAt(1) + "");

			int enemyRow = Integer.parseInt(enemyCordinates.charAt(0) + "");
			int enemyCol = Integer.parseInt(enemyCordinates.charAt(1) + "");
			if (ally.charAt(2) == 'r') { //if Ally to bottom right
				try {
					if (brd[allyRow - 2][allyCol - 2] == null) {//jump to top left
						try {
							if (brd[allyRow - 3][allyCol - 3].team() != color && brd[allyRow - 4][allyCol - 4] == null && (((allyRow-3) != enemyRow) && (allyCol-3 != enemyCol))) { //jump again to the left
								return checkPieceForForcedDoubleRed(startCord, arrived, enemyCordinates, enemyArrived,ally, wentRight, wentLeft, 1);
							}
						} catch (Exception e) {
						}
						try {
							if (brd[allyRow - 3][allyCol - 1].team() != color && brd[allyRow - 4][allyCol] == null && (((allyRow-3) != enemyRow) && (allyCol-1 != enemyCol))) { //jump to the right
								return checkPieceForForcedDoubleRed(startCord, arrived, enemyCordinates, enemyArrived,ally, wentRight, wentLeft, 1);
							}
						} catch (Exception e) {
						}
					}
				} catch (Exception e) {
				}
			} else { //ally to bottom left
				try {
					if (brd[allyRow - 2][allyCol + 2] == null) {//jump to top right
						try {
							if (brd[allyRow - 3][allyCol + 3].team() != color && brd[allyRow - 4][allyCol + 4] == null && (((allyRow-3) != enemyRow) && (allyCol+3 != enemyCol))) { //jump again to the right
								return checkPieceForForcedDoubleRed(startCord, arrived, enemyCordinates, enemyArrived,ally, wentRight, wentLeft, 1);
							}
						} catch (Exception e) {
						}
						try {
							if (brd[allyRow - 3][allyCol + 1].team() != color && brd[allyRow - 4][allyCol] == null&& (((allyRow-3) != enemyRow) && (allyCol+1 != enemyCol))) { //jump to the left
								return checkPieceForForcedDoubleRed(startCord, arrived, enemyCordinates, enemyArrived,ally, wentRight, wentLeft, 1);
							}
						} catch (Exception e) {
						}
					}
				} catch (Exception e) {
				}

			}
			return empty;
		}

		if(enemyArrived.length() != 0) {
			ArrayList<String> alliesThatCanJump = returnAllies(startCord, arrived, enemyCordinates, enemyArrived);
			if (alliesThatCanJump.size() != 0) {
				if (alliesThatCanJump.size() == 1) {
					return checkPieceForForcedDoubleRed(startCord, arrived, enemyCordinates, enemyArrived, alliesThatCanJump.get(0), wentRight, wentLeft, goodMove);
				} else if (alliesThatCanJump.size() == 2) {
					String one = checkPieceForForcedDoubleRed(startCord, arrived, enemyCordinates, enemyArrived, alliesThatCanJump.get(0), wentRight, wentLeft, goodMove);
					String two = checkPieceForForcedDoubleRed(startCord, arrived, enemyCordinates, enemyArrived, alliesThatCanJump.get(1), wentRight, wentLeft, goodMove);
					if (one.length() != 0)
						return one;
					else if(two.length() != 0)
						return two;
					else
						return empty;
				} else
					return empty;
			}
			return empty;
		}

//Currently knows there is a nearby enemy by the moved red piece
		if (enemyCordinates.length() != 0) { //returns the new position of the "moved" enemy

			try {
				String testEnemyCord = (newRow - 1) + "" + (newCol - 1);
				if ((brd[newRow + 1][newCol + 1] == null || ((newRow+1) == StartRow) && (newCol + 1) == StartCol) && testEnemyCord.equals(enemyCordinates)) {//enemy top left
					String temp = (newRow + 1) + "" + (newCol + 1);
					return checkPieceForForcedDoubleRed(startCord, arrived, enemyCordinates, temp,ally, wentRight, wentLeft, goodMove);
				}
			} catch (Exception e) {
			}
			try {
				String testEnemyCord = (newRow - 1) + "" + (newCol + 1);
				if (((brd[newRow + 1][newCol - 1] == null) || ((newRow+1) == StartRow) && ((newCol - 1) == StartCol)) && (testEnemyCord.equals(enemyCordinates))) {//enemy top right
					String temp = (newRow + 1) + "" + (newCol - 1);
					return checkPieceForForcedDoubleRed(startCord, arrived, enemyCordinates, temp,ally, wentRight, wentLeft, goodMove);
				}
			} catch (Exception e) {
			}
			return  empty;
		}

		if (wentRight == 1 || wentLeft == 1) { //check for enemies
			try {
				if (brd[newRow - 1][newCol - 1] != null && brd[newRow - 1][newCol - 1].team() != color) {
					enemyCord += (newRow - 1) + "" + (newCol - 1);
				}
			} catch (Exception e) {
			}
			try {
				if (brd[newRow - 1][newCol + 1] != null && brd[newRow - 1][newCol + 1].team() != color) {
					enemyCord += (newRow - 1) + "" + (newCol + 1);
				}
			} catch (Exception e) {
			}
			if(enemyCord.length() == 2)
				return checkPieceForForcedDoubleRed(startCord,arrived,enemyCord,enemyArrived,ally,wentRight,wentLeft,goodMove);
			else if(enemyCord.length() == 4){
				String one = checkPieceForForcedDoubleRed(startCord,arrived,enemyCord.substring(0,2),enemyArrived,ally,wentRight,wentLeft,goodMove);
				String two = checkPieceForForcedDoubleRed(startCord,arrived,enemyCord.substring(2),enemyArrived,ally,wentRight,wentLeft,goodMove);
				if(one.length() != 0 && two.length() != 0){
					return one;
				}
				else
					return empty;
			}
			else{
				return empty;
			}
		}

		if(wentLeft == 0 && wentRight == 0) { //check empty space
			try {
				if (brd[StartRow - 1][StartCol + 1] == null) { //went right
					String newArrived = (StartRow - 1) + "" + (StartCol + 1);
					empty = checkPieceForForcedDoubleRed(startCord, newArrived, "","",ally, 1, 0, 0);
				}
			} catch (Exception e) {
			}
			if(empty.length() != 0)
				return empty;
			try {
				if (brd[StartRow - 1][StartCol - 1] == null) { //went left
					String newArrived = (StartRow - 1) + "" + (StartCol - 1);
					empty = checkPieceForForcedDoubleRed(startCord, newArrived, "","","", 0, 1, 0);
				}
			} catch (Exception e) {
			}
			if(empty.length() != 0)
				return empty;
		}

		return empty;
	}

	/**
	 *
	 * @param startCord
	 * @param arrived
	 * @param enemyCordinates Only ever returns 1 pair of cordinates
	 * @param wentRight
	 * @param wentLeft
	 * @param goodMove
	 * @return
	 */
	private String checkPieceForForcedDoubleBlack(String startCord, String arrived, String enemyCordinates, String enemyArrived, String ally, int wentRight, int wentLeft, int goodMove ) {
		String empty = "";
		String enemyCord = "";
		int StartRow = Integer.parseInt(startCord.charAt(0) + "");
		int StartCol = Integer.parseInt(startCord.charAt(1) + "");
		int newRow = -1;
		int newCol = -1;
		if (arrived.length() != 0) {
			newRow = Integer.parseInt(arrived.charAt(0) + "");
			newCol = Integer.parseInt(arrived.charAt(1) + "");
		}

		if (goodMove == 1)//base case
			return startCord + arrived;

		if(ally.length() != 0) { //processess 1 ally
			int allyRow = Integer.parseInt(ally.charAt(0) + "");
			int allyCol = Integer.parseInt(ally.charAt(1) + "");

			int enemyRow = Integer.parseInt(enemyCordinates.charAt(0) + "");
			int enemyCol = Integer.parseInt(enemyCordinates.charAt(1) + "");
			if (ally.charAt(2) == 'r') { //if Ally to top right
				try {
					if (brd[allyRow + 2][allyCol - 2] == null) {//jump to bottom left
						try {
							if (brd[allyRow + 3][allyCol - 3].team() != color && brd[allyRow + 4][allyCol - 4] == null && (((allyRow+3) != enemyRow) && (allyCol-3 != enemyCol))) { //jump again to the left
								return checkPieceForForcedDoubleRed(startCord, arrived, enemyCordinates, enemyArrived,ally, wentRight, wentLeft, 1);
							}
						} catch (Exception e) {
						}
						try {
							if (brd[allyRow + 3][allyCol - 1].team() != color && brd[allyRow + 4][allyCol] == null && (((allyRow+3) != enemyRow) && (allyCol-1 != enemyCol))) { //jump to the right
								return checkPieceForForcedDoubleRed(startCord, arrived, enemyCordinates, enemyArrived,ally, wentRight, wentLeft, 1);
							}
						} catch (Exception e) {
						}
					}
				} catch (Exception e) {
				}
			} else { //ally to top left
				try {
					if (brd[allyRow + 2][allyCol + 2] == null) {//jump to bottom right
						try {
							if (brd[allyRow + 3][allyCol + 3].team() != color && brd[allyRow + 4][allyCol + 4] == null && (((allyRow+3) != enemyRow) && (allyCol+3 != enemyCol))) { //jump again to the right
								return checkPieceForForcedDoubleRed(startCord, arrived, enemyCordinates, enemyArrived,ally, wentRight, wentLeft, 1);
							}
						} catch (Exception e) {
						}
						try {
							if (brd[allyRow + 3][allyCol + 1].team() != color && brd[allyRow + 4][allyCol] == null&& (((allyRow+3) != enemyRow) && (allyCol+1 != enemyCol))) { //jump to the left
								return checkPieceForForcedDoubleRed(startCord, arrived, enemyCordinates, enemyArrived,ally, wentRight, wentLeft, 1);
							}
						} catch (Exception e) {
						}
					}
				} catch (Exception e) {
				}
			}
			return empty;
		}

		if(enemyArrived.length() != 0) {
			ArrayList<String> alliesThatCanJump = returnAllies(startCord, arrived, enemyCordinates, enemyArrived);
			if (alliesThatCanJump.size() != 0) {
				if (alliesThatCanJump.size() == 1) {
					return checkPieceForForcedDoubleRed(startCord, arrived, enemyCordinates, enemyArrived, alliesThatCanJump.get(0), wentRight, wentLeft, goodMove);
				} else if (alliesThatCanJump.size() == 2) {
					String one = checkPieceForForcedDoubleRed(startCord, arrived, enemyCordinates, enemyArrived, alliesThatCanJump.get(0), wentRight, wentLeft, goodMove);
					String two = checkPieceForForcedDoubleRed(startCord, arrived, enemyCordinates, enemyArrived, alliesThatCanJump.get(1), wentRight, wentLeft, goodMove);
					if (one.length() != 0)
						return one;
					else if(two.length() != 0)
						return two;
					else
						return empty;
				} else
					return empty;
			}
			return empty;
		}

//Currently knows there is a nearby enemy by the moved red piece
		if (enemyCordinates.length() != 0) { //returns the new position of the "moved" enemy
			try {
				String testEnemyCord = (newRow + 1) + "" + (newCol - 1);
				if ((brd[newRow - 1][newCol + 1] == null || ((newRow+1) == StartRow) && (newCol - 1) == StartCol) && testEnemyCord.equals(enemyCordinates)) {//enemy bottom left
					String temp = (newRow - 1) + "" + (newCol + 1);
					return checkPieceForForcedDoubleRed(startCord, arrived, enemyCordinates, temp,ally, wentRight, wentLeft, goodMove);
				}
			} catch (Exception e) {
			}
			try {
				String testEnemyCord = (newRow + 1) + "" + (newCol + 1);
				if (((brd[newRow - 1][newCol - 1] == null) || ((newRow+1) == StartRow) && ((newCol + 1) == StartCol)) && (testEnemyCord.equals(enemyCordinates))) {//enemy bottom right
					String temp = (newRow - 1) + "" + (newCol - 1);
					return checkPieceForForcedDoubleRed(startCord, arrived, enemyCordinates, temp,ally, wentRight, wentLeft, goodMove);
				}
			} catch (Exception e) {
			}
			return  empty;
		}

		if (wentRight == 1 || wentLeft == 1) { //check for enemies
			try {
				if (brd[newRow + 1][newCol - 1] != null && brd[newRow + 1][newCol - 1].team() != color) { //enemy to bottom left
					enemyCord += (newRow + 1) + "" + (newCol - 1);
				}
			} catch (Exception e) {
			}
			try {
				if (brd[newRow + 1][newCol + 1] != null && brd[newRow + 1][newCol + 1].team() != color) { //enemy to bottom right
					enemyCord += (newRow + 1) + "" + (newCol + 1);
				}
			} catch (Exception e) {
			}
			if(enemyCord.length() == 2)
				return checkPieceForForcedDoubleRed(startCord,arrived,enemyCord,enemyArrived,ally,wentRight,wentLeft,goodMove);
			else if(enemyCord.length() == 4){
				String one = checkPieceForForcedDoubleRed(startCord,arrived,enemyCord.substring(0,2),enemyArrived,ally,wentRight,wentLeft,goodMove);
				String two = checkPieceForForcedDoubleRed(startCord,arrived,enemyCord.substring(2),enemyArrived,ally,wentRight,wentLeft,goodMove);
				if(one.length() != 0 && two.length() != 0){
					return one;
				}
				else
					return empty;
			}
			else{
				return empty;
			}
		}

		if(wentLeft == 0 && wentRight == 0) { //check empty space
			try {
				if (brd[StartRow + 1][StartCol + 1] == null) { //went right
					String newArrived = (StartRow + 1) + "" + (StartCol + 1);
					empty = checkPieceForForcedDoubleRed(startCord, newArrived, "","",ally, 1, 0, 0);
				}
			} catch (Exception e) {
			}
			if(empty.length() != 0)
				return empty;
			try {
				if (brd[StartRow + 1][StartCol - 1] == null) { //went left
					String newArrived = (StartRow + 1) + "" + (StartCol - 1);
					empty = checkPieceForForcedDoubleRed(startCord, newArrived, "","","", 0, 1, 0);
				}
			} catch (Exception e) {
			}
			if(empty.length() != 0)
				return empty;
		}

		return empty;
	}

		private ArrayList<String> returnAllies(String startCord, String arrived, String enemyCordinates, String enemyArrived){
			ArrayList<String> alliesThatCanJump = new ArrayList<String>();
			int enemyArrivedRow = Integer.parseInt(enemyArrived.charAt(0)+""); int enemyArrivedCol = Integer.parseInt(enemyArrived.charAt(1)+"");
			if(color == 'r'){
				int redStartRow = Integer.parseInt(startCord.charAt(0)+""); int redStartCol = Integer.parseInt(startCord.charAt(1)+"");
				try{ //if nearby ally to moved enemy, and ally can jump new enemy
					if(brd[enemyArrivedRow+1][enemyArrivedCol+1].team() == color && (((enemyArrivedRow+1) != redStartRow) && (enemyArrivedCol + 1) != redStartCol) && brd[enemyArrivedRow-1][enemyArrivedCol-1] == null){
						String temp = (enemyArrivedRow+1)+""+(enemyArrivedCol+1)+"r";
						alliesThatCanJump.add(temp);
					}
				}catch (Exception e){}

				try{
					if(brd[enemyArrivedRow+1][enemyArrivedCol-1].team() == color && (((enemyArrivedRow+1) != redStartRow) && (enemyArrivedCol - 1) != redStartCol) && brd[enemyArrivedRow-1][enemyArrivedCol+1] == null){//adds ally to bottom left
						String temp = (enemyArrivedRow+1)+""+(enemyArrivedCol-1) + "l";
						alliesThatCanJump.add(temp);
					}
				}catch (Exception e){}
			}
			else{//if black
				int blackStartRow = Integer.parseInt(startCord.charAt(0)+""); int blackStartCol = Integer.parseInt(startCord.charAt(1)+"");
				try{
					if(brd[enemyArrivedRow-1][enemyArrivedCol+1].team() == color && (((enemyArrivedRow-1) != blackStartRow) && (enemyArrivedCol + 1) != blackStartCol) && brd[enemyArrivedRow+1][enemyArrivedCol-1] == null){
						String temp = (enemyArrivedRow-1)+""+(enemyArrivedCol+1)+"r";
						alliesThatCanJump.add(temp);
					}
				}catch (Exception e){}

				try{
					if(brd[enemyArrivedRow-1][enemyArrivedCol-1].team() == color && (((enemyArrivedRow-1) != blackStartRow) && (enemyArrivedCol - 1) != blackStartCol) && brd[enemyArrivedRow+1][enemyArrivedCol+1] == null){//adds ally to bottom left
						String temp = (enemyArrivedRow-1)+""+(enemyArrivedCol-1) + "l";
						alliesThatCanJump.add(temp);
					}
				}catch (Exception e){}
			}
			return alliesThatCanJump;
		}


	/**
	 * returns coordinates for a random move
	 * @return
	 */
	private String randomMove(){
		String move = "";
		ArrayList<String> pieceCordinates = new ArrayList<String>();
		for(int row = 0; row < 8; row++){ // add all piece coordinates to list.
			for(int col = 0; col < 8; col++){
				if(brd[row][col] != null && brd[row][col].team() == color){
					pieceCordinates.add(row+""+col);
				}
			}
		}

		ArrayList<String> moves = new ArrayList<String>();
		getMoves(moves, pieceCordinates); // Adds valid moves to this arraylist

		ArrayList<String> safeMoves = new ArrayList<String>();
		for(int i = 0; i < moves.size();i++){//go through each move and find SAFE moves.
			int row = Integer.parseInt(moves.get(i).charAt(0)+"");
			int col = Integer.parseInt(moves.get(i).charAt(1)+"");

			int rowEnd = Integer.parseInt(moves.get(i).charAt(2)+"");
			int colEnd = Integer.parseInt(moves.get(i).charAt(3)+"");
			boolean safe = false;
			if(color == 'r')
				safe = safeDownMove(row,col,rowEnd,colEnd);
			else
				safe = safeUpMove(row,col,rowEnd,colEnd);

			if(safe)
				safeMoves.add(row+""+col+""+rowEnd+""+colEnd);
		}
		if(safeMoves.size() != 0){
			int num = rand.nextInt(safeMoves.size());
			move = safeMoves.get(num);
		}
		else{ //If no safe moves
			int num = rand.nextInt(moves.size());
			move = moves.get(num);
		}

		return move;
	}

	private boolean safeDownMove(int row, int col, int rowEnd, int colEnd){
		boolean safe = true;
		if(brd[rowEnd][colEnd] != null)
			return false;
		Piece temp = brd[row][col].clone();
		brd[rowEnd][colEnd] = temp;
		brd[row][col] = null;
		try{
			if(brd[rowEnd-1][colEnd+1]!= null && brd[rowEnd-1][colEnd+1].team() != color){//black to top right
				if(brd[rowEnd+1][colEnd-1] == null)
					safe = false;
			}
		}catch (Exception e){}
		try{
			if(brd[rowEnd-1][colEnd-1]!= null && brd[rowEnd-1][colEnd-1].team() != color){//black to top left
				if(brd[rowEnd+1][colEnd+1] == null)
					safe = false;
			}
		} catch (Exception e){}
		temp = brd[rowEnd][colEnd].clone();
		brd[rowEnd][colEnd] = null;
		brd[row][col] = temp;
		return safe;
	}

	//todo error happening here
	private boolean safeUpMove(int row, int col, int rowEnd, int colEnd){
		boolean safe = true;
		if(brd[rowEnd][colEnd] != null)
			return false;
		checkerboard.movePiece((row + "" + col), (rowEnd + "" + colEnd));

		try{
			if(brd[rowEnd+1][colEnd+1]!= null && brd[rowEnd+1][colEnd+1].team() != color){//red to bottom right
				if(brd[rowEnd-1][colEnd-1] == null)
					safe = false;
			}
		}catch (Exception e){}
		try{
			if(brd[rowEnd+1][colEnd-1]!= null && brd[rowEnd+1][colEnd-1].team() != color){//red to bottom left
				if(brd[rowEnd-1][colEnd+1] == null)
					safe = false;
			}
		} catch (Exception e){}
		Piece original = brd[rowEnd][colEnd];
		Piece temp;
		temp = brd[rowEnd][colEnd].clone();
		brd[rowEnd][colEnd] = null;
		brd[row][col] = temp;
		return safe;
	}


	//returns arraylist of possible moves
	private void getMoves(ArrayList<String> list, ArrayList<String> pieceCordinates){
		for(int i = 0; i < pieceCordinates.size(); i++){
			boolean upLeft = false; boolean upRight = false;
			boolean downLeft = false; boolean downRight = false;
			int row = Integer.parseInt(pieceCordinates.get(i).charAt(0) + "");
			int col = Integer.parseInt(pieceCordinates.get(i).charAt(1) + "");
			boolean isAKing = brd[row][col].isKing();
			if(isAKing){
				try{
					upLeft = checkerboard.fairRedMove(row, col, row - 1, col - 1);
				}catch (Exception e){}
				try{
					upRight = checkerboard.fairRedMove(row, col, row - 1, col + 1);
				}catch (Exception e){}
				try{
					downRight = checkerboard.fairBlackMove(row,col,row+1,col+1);
				}catch (Exception e){}
				try{
					downLeft = checkerboard.fairBlackMove(row,col,row+1,col-1);
				}catch (Exception e){}
			}
			else if(color == 'r'){
				try{
					upLeft = checkerboard.fairRedMove(row, col, row - 1, col - 1);
				}
				catch (Exception e){}
				try{
					upRight = checkerboard.fairRedMove(row, col, row - 1, col + 1);
				}
				catch (Exception e){}

			}
			else{
				try{
					downRight = checkerboard.fairBlackMove(row,col,row+1,col+1);
				}
				catch (Exception e){}
				try{
					downLeft = checkerboard.fairBlackMove(row,col,row+1,col-1);
				}
				catch (Exception e){}

			}

			if(upLeft){
				list.add(row+""+col+""+(row-1) + ""+(col-1));
			}
			if(upRight){
				list.add(row+""+col+""+(row-1) + ""+(col+1));
			}
			if(downLeft){
				list.add(row+""+col+""+(row+1) + ""+(col-1));
			}
			if(downRight){
				list.add(row+""+col+""+(row+1) + ""+(col+1));
			}
		}
	}

	//TODO Sometimes moves to protect but leaves other nearby ally open for attack
	//TODO BUGGY FIX NOW
	/**
	 * Check each allied Piece if needs protection.
	 * @return
	 */
	private String protect(){
		String empty = "";
		for(int row = 0; row < 8; row++){
			for(int col = 0; col < 8; col++){
				if(brd[row][col] != null && brd[row][col].team() == color){
					String squaresInDanger = vulnerable(row,col);
					if(squaresInDanger.length() != 0){
						String enemyCord = adjacentEnemy(row,col);
						if(enemyCord.length()!=0){ // If there are adjacent enemies
							String allyMove = allyProtection(row,col); //check if blank spaces are behind it.
							if(allyMove.length() != 0)
								return allyMove;
						}
					}
				}
			}
		}
		return empty;
	}

	//returns cordinates of blank square where enemy can attack
	private String vulnerable(int row, int col){
		String empty = "";
		if(color == 'r'){
			try{
				if(brd[row+1][col-1] == null && brd[row-1][col+1] != null &&  brd[row-1][col+1].team() != color){
					empty += (row+1)+""+(col-1);
				}
			}catch (Exception e){}
			try {
				 if(brd[row+1][col+1] == null && brd[row-1][col-1] != null &&  brd[row-1][col-1].team() != color){
					empty += (row+1)+""+(col+1);
				}
			}catch (Exception e){}
			return empty;
		}
		else{ //if black
			try {
				if(brd[row-1][col-1] == null && brd[row+1][col+1] != null &&  brd[row+1][col+1].team() != color){
					empty += (row-1)+""+(col-1);
				}
			}catch (Exception e){}
			try {
				if(brd[row-1][col+1] == null && brd[row+1][col-1] != null &&  brd[row+1][col-1].team() != color){
					empty += (row-1)+""+(col+1);
				}
			}catch (Exception e){}

			return empty;
		}
	}


			//TODO fix issue where piece moves and leaves nearby ally vulnerable
	/**
	 * Returns coordinates of a friendly piece, and move cords to protect a piece.
	 * @return
	 */
	private String allyProtection(int row, int col){
		String empty = "";
		if(row == 0 || row == 7 || col == 0 || col == 7)
			return empty;

		if(color == 'r'){
			try{
				if(brd[row-1][col+1] != null &&  brd[row-1][col+1].team() != color) {//enemy to top right
					if (brd[row + 1][col - 1] == null) { // check for blank space
						if (brd[row + 2][col - 2] != null && brd[row + 2][col - 2].team() == color) {//check for ally to bottom left
							empty = empty + "" + (row + 2) + "" + (col - 2) + "" + (row + 1) + "" + (col - 1);
							return empty;
						}
						if (brd[row + 2][col] != null && brd[row + 2][col].team() == color) {//check for ally to bottom right
							empty = empty + "" + (row + 2) + "" + (col) + "" + (row + 1) + "" + (col - 1);
							return empty;
						}
					}
				}
			}catch (Exception e){}
			try{
				if(brd[row-1][col-1] != null && brd[row-1][col-1].team() != color){ //if enemy to top left
					if(brd[row+1][col + 1] == null){
						if(brd[row+2][col] != null && brd[row+2][col].team() == color){//check for ally to bottom left
							empty = empty + "" + (row+2) + "" + (col) + "" + (row+1) + "" + (col+1);
							return empty;
						}
						if(brd[row+2][col + 2] != null && brd[row+2][col + 2].team() == color){//check for ally to bottom right
							empty = empty + "" + (row+2) + "" + (col+2) + "" + (row+1) + "" + (col+1);
							return empty;
						}
					}

				}
			}catch (Exception e){}
			}
		else{ // if black
			try{
				if(brd[row+1][col+1] != null && brd[row+1][col+1].team() != color){ //if enemy to bottom right
					if(brd[row-1][col - 1] == null){ // check for blank space
						if(brd[row-2][col - 2] != null && brd[row-2][col - 2].team() == color){//check for ally to upper left
							empty = empty + "" + (row-2) + "" + (col-2) + "" + (row-1) + "" + (col-1);
							return empty;
						}
						if(brd[row-2][col] != null && brd[row-2][col].team() == color){//check for ally to bottom right
							empty = empty + "" + (row-2) + "" + (col) + "" + (row-1) + "" + (col-1);
							return empty;
						}
					}
				}
			}
			catch (Exception e){

			}
			try{
				if(brd[row+1][col-1] != null && brd[row+1][col-1].team() != color){ //if enemy to bottom left
					if(brd[row-1][col + 1] == null){
						if(brd[row-2][col] != null && brd[row-2][col].team() == color){//check for ally to bottom left
							empty = empty + "" + (row-2) + "" + (col) + "" + (row-1) + "" + (col+1);
							return empty;
						}
						if(brd[row-2][col + 2] != null && brd[row-2][col + 2].team() == color){//check for ally to bottom right
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
		else{ //if black
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

}

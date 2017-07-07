import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

public class StartUp {
	static Scanner scan = new Scanner(System.in);
	static Board brd;
	static Victory vic;
	static KINGM3 evilAI;
	static KINGM3 secondEvilAI;

	public static void main(String[] args) throws InterruptedException {
		brd = new Board();
		brd.printBoard();
		vic = new Victory(brd.checkerBoard);
		
		evilAISetup(brd);
		boolean end = false;
		while(true){
			//Black Move
			end = blackMovee();
			System.out.println();
			if(end)
				break;
			brd.kingMe();
			//Thread.sleep(200);
			brd.changeTurn();
			//RED MOVE
			end = redMovee();
			System.out.println();
			if(end)
				break;
			brd.kingMe();
			//Thread.sleep(200);
			brd.changeTurn();
		}
		if(vic.colorCount()[0] == 0){
			System.out.println("Black is Victorious!!!");
		}
		else{
			System.out.println("Red is Victorious!!!");
		}
	}
	
	private static boolean redMovee(){
		//Black Move
		ArrayList<String> killMoves = forceJumpRed();
		String redMove = ""; boolean good = false;
		while(!good){
			System.out.println("Red  Move");
			redMove = "";
			if(evilAI.color == 'r'){
				redMove = evilAI.getSuperiorMove(killMoves);
				System.out.println(redMove + " : AI Move");
			}
			else if(secondEvilAI != null && secondEvilAI.color == 'r'){
				redMove = secondEvilAI.getSuperiorMove(killMoves);
				System.out.println(redMove + " : 2nd AI Move");
			}
			else
				redMove = scan.nextLine();

			if(killMoves.size() == 0)
				break;

			String converted = brd.convert(redMove.substring(0,2)) + brd.convert(redMove.substring(2,4));
			for(int i = 0; i < killMoves.size();i++ ){
				if(converted.equals(killMoves.get(i).substring(0,4))){
					good = true;
					break;
				}
			}
		}

		boolean goodMove = brd.movePiece(redMove.substring(0,2), redMove.substring(2));
		while(!goodMove){
			System.out.println("Red Enter Move");
			redMove = scan.nextLine();
			goodMove = brd.movePiece(redMove.substring(0,2), redMove.substring(2,redMove.length()));
		}

		//Print Board
		brd.printBoard();

		//Check Victory
		if(vic.checkVictory()){
			return true; //end while loop/game
		}
		return false;
	}
	
	private static boolean blackMovee(){
		//Black Move
		ArrayList<String> killMoves = forceJumpBlack();
		String blackMove = ""; boolean good = false;
		while(!good){
			System.out.println("Black  Move");
			blackMove = "";
			if(evilAI.color == 'b'){
				blackMove = evilAI.getSuperiorMove(killMoves);
				System.out.println(blackMove + " : 1st AI Move");
			}
			else if(secondEvilAI != null && secondEvilAI.color == 'b'){
				blackMove = secondEvilAI.getSuperiorMove(killMoves);
				System.out.println(blackMove + " : 2nd AI Move");
			}
			else
				blackMove = scan.nextLine();
			if(killMoves.size() == 0)
				break;
			String converted = brd.convert(blackMove.substring(0,2)) + brd.convert(blackMove.substring(2,4));
			for(int i = 0; i < killMoves.size();i++ ){
				if(converted.equals(killMoves.get(i).substring(0,4))){
					good = true;
					break;
				}
			}
			System.out.println("Must enter kill move");
		}

			boolean goodMove = brd.movePiece(blackMove.substring(0,2), blackMove.substring(2));
			while(!goodMove){
				System.out.println("Black Enter Move");
				 blackMove = scan.nextLine();
				 goodMove = brd.movePiece(blackMove.substring(0,2), blackMove.substring(2,blackMove.length()));
			}

		//Print Board
		brd.printBoard();

		//Check Victory
		if(vic.checkVictory()){
			return true; //end while loop/game
		}
		return false;
	}

	public static ArrayList<String> forceJumpRed(){
		ArrayList<String> killMoves = new ArrayList<String>();
		for(int row = 0; row < 8; row++){
			for(int col = 0; col < 8; col++){
				if(brd.checkerBoard[row][col] != null && brd.checkerBoard[row][col].team() == 'r'){// If red is found

						try {
							if (brd.checkerBoard[row - 1][col + 1] != null && brd.checkerBoard[row - 1][col + 1].team() != 'r') {//up right
								if (brd.checkerBoard[row - 2][col + 2] == null) {
									killMoves.add(row+""+col+""+(row-2)+""+(col+2));
								}
							}
						}
						catch(Exception e){}

						try {
							if(brd.checkerBoard[row-1][col-1] != null && brd.checkerBoard[row-1][col-1].team() != 'r'){ //up left
								if(brd.checkerBoard[row-2][col-2] == null){
									killMoves.add(row+""+col+""+(row-2)+""+(col-2));
								}
							}
						}
						catch(Exception e){}
						if(brd.checkerBoard[row][col].isKing()){
							try{
								if (brd.checkerBoard[row + 1][col + 1] != null && brd.checkerBoard[row + 1][col + 1].team() != 'r') {//bottom right
									if (brd.checkerBoard[row + 2][col + 2] == null) {
										killMoves.add(row+""+col+""+(row+2)+""+(col+2));
									}
								}
							}catch (Exception e){}
							try{
								if (brd.checkerBoard[row + 1][col - 1] != null && brd.checkerBoard[row + 1][col - 1].team() != 'r') {//bottom left
									if (brd.checkerBoard[row + 2][col - 2] == null) {
										killMoves.add(row+""+col+""+(row+2)+""+(col-2));
									}
								}
							}catch (Exception e){}
				}
				}
			}
		}
		return killMoves;
	}
	
	public static ArrayList<String> forceJumpBlack(){
		ArrayList<String> validMoves = new ArrayList<String>();
		for(int row = 0; row < 8; row++){
			for(int col = 0; col < 8; col++){
				if(brd.checkerBoard[row][col] != null && brd.checkerBoard[row][col].team() == 'b'){// If black is found
					try{
						if(brd.checkerBoard[row+1][col+1] != null && brd.checkerBoard[row+1][col+1].team() != 'b'){//bottom right
							if(brd.checkerBoard[row+2][col+2] == null){
								validMoves.add(row+""+col+""+(row+2)+""+(col+2));
							}
						}
					}
					catch(Exception e){}
					try{
						if(brd.checkerBoard[row+1][col-1] != null && brd.checkerBoard[row+1][col-1].team() != 'b'){ //bottom left
							if(brd.checkerBoard[row+2][col-2] == null){
								validMoves.add(row+""+col+""+(row+2)+""+(col-2));
							}
						}
					}
					catch (Exception e){}

					if(brd.checkerBoard[row][col].isKing()){
						try{
							if (brd.checkerBoard[row - 1][col + 1] != null && brd.checkerBoard[row - 1][col + 1].team() != 'b') {//top right
								if (brd.checkerBoard[row - 2][col + 2] == null) {
									validMoves.add(row+""+col+""+(row-2)+""+(col+2));
								}
							}
						}catch (Exception e){}
						try{
							if (brd.checkerBoard[row - 1][col - 1] != null && brd.checkerBoard[row - 1][col - 1].team() != 'b') {//top left
								if (brd.checkerBoard[row - 2][col - 2] == null) {
									validMoves.add(row+""+col+""+(row-2)+""+(col-2));
								}
							}
						}catch (Exception e){}
					}
				}
			}
		}
		return validMoves;
	}
	
	private static void evilAISetup(Board brd){
		System.out.println("Two AI's ?");
		String answer = scan.next();
		if(answer.charAt(0) == 'y'){
			evilAI = new KINGM3(brd,'r');
			secondEvilAI = new KINGM3(brd,'b');
		}
		else{
		System.out.println("Enter AI color");
			char color = scan.nextLine().charAt(0);
			evilAI = new KINGM3(brd,color);
		}

	}
	
	
}
	
	

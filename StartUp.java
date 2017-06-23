import java.util.Scanner;

public class StartUp {
	static Scanner scan = new Scanner(System.in);
	static Board brd;
	static Victory vic;
	static KINGM3 evilAI;
	 //TODO Make Black go first
	//TODO Fix force jump killing wrong piece
	//TODO Fix force jump not recognizing force jump
	public static void main(String[] args) throws InterruptedException {
		brd = new Board();
		brd.printBoard();
		vic = new Victory(brd.checkerBoard);
		
		evilAISetup(brd);
		boolean end = false;
		while(true){
			//RED MOVE
			end = redMovee();
			System.out.println();
			if(end)
				break;
			Thread.sleep(1000);
			//Black Move
			end = blackMovee();
			System.out.println();
			if(end)
				break;
			Thread.sleep(1000);
		}
	}
	
	private static boolean redMovee(){
		
		boolean forceJump = forceJumpRed();
		if(!forceJump){
			System.out.println("Red Move");
			String redMove = ""; 
			if(evilAI.color == 'r')
				redMove = evilAI.getSuperiorMove();
			else
				redMove = scan.nextLine();
			boolean goodMove = brd.movePiece(redMove.substring(0,2), redMove.substring(2,redMove.length()));
			while(!goodMove){
				System.out.println("Red Enter Move");
				 redMove = scan.nextLine();
				 goodMove = brd.movePiece(redMove.substring(0,2), redMove.substring(2,redMove.length()));
			}
		}
		
		//PRINT BOARD
		brd.printBoard();

		//Check Victory
		if(vic.checkVictory()){
			return true; //end while loop/game
		}
		return false;
	}
	
	private static boolean blackMovee(){
		//Black Move
		boolean forceJump = forceJumpBlack();
		if(!forceJump){
			System.out.println("Black  Move");
			String blackMove = ""; 
			if(evilAI.color == 'b')
				blackMove = evilAI.getSuperiorMove();
			else
				blackMove = scan.nextLine();
			boolean goodMove = brd.movePiece(blackMove.substring(0,2), blackMove.substring(2,blackMove.length()));
			while(!goodMove){
				System.out.println("Black Enter Move");
				 blackMove = scan.nextLine();
				 goodMove = brd.movePiece(blackMove.substring(0,2), blackMove.substring(2,blackMove.length()));
			}
		}
		//Print Board
		brd.printBoard();

		//Check Victory
		if(vic.checkVictory()){
			return true; //end while loop/game
		}
		return false;
	}

	private static boolean forceJumpRed(){
		for(int row = 0; row < 7; row++){
			for(int col = 0; col < 7; col++){
				if(brd.checkerBoard[row][col] != null && brd.checkerBoard[row][col].team() == 'r'){// If red is found
					try {
						if (brd.checkerBoard[row - 1][col + 1] != null && brd.checkerBoard[row - 1][col + 1].team() != 'r') {//up right
							if (brd.checkerBoard[row - 2][col + 2] == null) {
								brd.movePiece(row + "" + col, (row - 2) + "" + (col + 2));
								return true;
							}
						}
					}
					catch(Exception e){}

					try {
						if(brd.checkerBoard[row-1][col-1] != null && brd.checkerBoard[row-1][col+1].team() != 'r'){ //up left
							if(brd.checkerBoard[row-2][col-2] == null){
								brd.movePiece(row +""+ col, (row-2) + "" +(col - 2));
								return true;
							}
						}
					}
					catch(Exception e){}
					}
				}
			}
		return false;
		}
	
	private static boolean forceJumpBlack(){
		for(int row = 0; row < 7; row++){
			for(int col = 0; col < 7; col++){
				if(brd.checkerBoard[row][col] != null && brd.checkerBoard[row][col].team() == 'b'){// If red is found
					try{
						if(brd.checkerBoard[row+1][col+1] != null && brd.checkerBoard[row+1][col+1].team() != 'b'){//bottom right
							if(brd.checkerBoard[row+2][col+2] == null){
								brd.movePiece(row +""+ col, (row+2) + "" +(col + 2));
								return true;
							}
						}
					}
					catch(Exception e){}
					try{
						if(brd.checkerBoard[row+1][col-1] != null && brd.checkerBoard[row+1][col-1].team() != 'b'){ //bottom left
							if(brd.checkerBoard[row+2][col-2] == null){
								brd.movePiece(row +""+ col, (row+2) + "" +(col - 2));
								return true;
							}
						}
					}
					catch (Exception e){}
					
				}
			}
		}
		return false;
	}
	
	private static void evilAISetup(Board brd){
		System.out.println("Jeff enter your color");
		char color = scan.nextLine().charAt(0);
		evilAI = new KINGM3(brd,color);
	}
	
	
}
	
	

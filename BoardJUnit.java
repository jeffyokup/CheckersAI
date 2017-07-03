import static com.sun.xml.internal.ws.dump.LoggingDumpTube.Position.Before;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class BoardJUnit {

	private static Board brd;
	private KINGM3 ai;

	@Before
	public void setUp(){
		brd = new Board();
		 ai = new KINGM3(brd,'r');
	}

	@Test
	public void move1(){
		assertFalse(brd.movePiece("c0","d1" ));
	}

	@Test
	public void move2(){
		assertTrue(brd.movePiece("f1", "e0"));
	}

	@Test
	public void singleJump(){
		assertTrue(brd.movePiece("f1","e0"));//red
		assertTrue(brd.movePiece("c0","d1"));//black
		assertTrue(brd.movePiece("g0","f1"));//red
		assertTrue(brd.movePiece("d1","e2"));//black
		//brd.printBoard();
		assertTrue(brd.movePiece("f3","d1"));//red
		//brd.printBoard();
	}

	@Test
	public void doubleJump(){
		assertTrue(brd.movePiece("f1","e0"));//red
		assertTrue(brd.movePiece("c0","d1"));//black
		assertTrue(brd.movePiece("g0","f1"));//red
		assertTrue(brd.movePiece("d1","e2"));//black

		assertTrue(brd.movePiece("f7","e6"));//red\
		assertTrue(brd.movePiece("b1","c0"));//black
		brd.printBoard();
		assertTrue(brd.movePiece("f1", "d3b1"));
		brd.printBoard();
	}

	@Test
	public void protect1(){
		brd.movePiece("f1","e0");
		brd.movePiece("c2","d1");
		brd.movePiece("f7","e6");
		brd.printBoard();
		assertEquals("1122",ai.getSuperiorMove());
	}
//TODO FIX THIS
	@Test
	public void protect2(){
		brd.movePiece("f3","e2");//red
		brd.movePiece("c2","d1");//black
		brd.printBoard();
		assertEquals("6253",ai.getSuperiorMove());//red
	}

	@Test
	public void safeRandom(){
		brd.movePiece("c0","d1");
		brd.printBoard();
		String move = ai.getSuperiorMove();
		brd.movePiece(move.substring(0,2), move.substring(2));
		brd.printBoard();
	}

	@Test
	public void checkAIDouble(){
		brd.movePiece("c4","d5");
		brd.movePiece("f1","e0");
		brd.printBoard();
		brd.movePiece("c6","d7");
		brd.movePiece("g0","f1");
		System.out.println();brd.printBoard();
		brd.movePiece("b7","c6");
		System.out.println(); brd.printBoard();
		String move = ai.getSuperiorMove();
		System.out.println();
		brd.movePiece(move.substring(0,2),move.substring(2));
		brd.printBoard();
		assertTrue(brd.checkerBoard[4][6] != null);
	}

	@Test
    public void checkDouble2(){
	    brd.movePiece("c1","d2");
	    brd.printBoard();
        String move = ai.getSuperiorMove();
        System.out.println();
        brd.movePiece(move.substring(0,2),move.substring(2));
	    brd.printBoard();
    }

    @Test
	public void checkDouble3(){
    	brd.movePiece("c1","d0");

		brd.movePiece("52","43");
		brd.movePiece("c3","d4");

		brd.movePiece("61","52");
		brd.movePiece("b4","c3");

		brd.movePiece("70","61");
		brd.movePiece("b2","c1");

		brd.movePiece("56","47");
		brd.movePiece("c7","d6");

		brd.movePiece("50","41");
		brd.movePiece("c3","d2");

		brd.movePiece("41","23");

		brd.movePiece("a3","b4");
		brd.movePiece("43","32");
		brd.movePiece("c1","e3");

		brd.printBoard();

		brd.movePiece("54","32");
		brd.movePiece("d4","e3");
		brd.movePiece("52","34");
		brd.movePiece("c5","e3");
		brd.printBoard();
		ArrayList<String> goodMoves = forceJumpRed();
		String move = ai.getSuperiorMove(goodMoves);
		System.out.println();
		brd.movePiece(move.substring(0,2),move.substring(2));
		brd.printBoard();
	}

	private static ArrayList<String> forceJumpRed(){
		ArrayList<String> validMoves = new ArrayList<String>();
		for(int row = 0; row < 8; row++){
			for(int col = 0; col < 8; col++){
				if(brd.checkerBoard[row][col] != null && brd.checkerBoard[row][col].team() == 'r'){// If red is found
					try {
						if (brd.checkerBoard[row - 1][col + 1] != null && brd.checkerBoard[row - 1][col + 1].team() != 'r') {//up right
							if (brd.checkerBoard[row - 2][col + 2] == null) {
								validMoves.add(row+""+col+""+(row-2)+""+(col+2));
							}
						}
					}
					catch(Exception e){}

					try {
						if(brd.checkerBoard[row-1][col-1] != null && brd.checkerBoard[row-1][col-1].team() != 'r'){ //up left
							if(brd.checkerBoard[row-2][col-2] == null){
								validMoves.add(row+""+col+""+(row-2)+""+(col-2));
							}
						}
					}
					catch(Exception e){}
				}
			}
		}
		return validMoves;
	}




}

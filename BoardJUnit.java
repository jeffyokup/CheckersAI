//import static org.junit.Assert.*;
//
//import org.junit.Before;
//import org.junit.Test;
//
//public class BoardJUnit {
//
//	private Board brd;
//
//	@Before
//	public void setUp(){
//		brd = new Board();
//	}
//
//	@Test
//	public void move1(){
//		assertFalse(brd.movePiece("c0","d1" ));
//	}
//
//	@Test
//	public void move2(){
//		assertTrue(brd.movePiece("f1", "e0"));
//	}
//
//	@Test
//	public void singleJump(){
//		assertTrue(brd.movePiece("f1","e0"));//red
//		assertTrue(brd.movePiece("c0","d1"));//black
//		assertTrue(brd.movePiece("g0","f1"));//red
//		assertTrue(brd.movePiece("d1","e2"));//black
//		//brd.printBoard();
//		assertTrue(brd.movePiece("f3","d1"));//red
//		//brd.printBoard();
//	}
//
//	@Test
//	public void doubleJump(){
//		assertTrue(brd.movePiece("f1","e0"));//red
//		assertTrue(brd.movePiece("c0","d1"));//black
//		assertTrue(brd.movePiece("g0","f1"));//red
//		assertTrue(brd.movePiece("d1","e2"));//black
//
//		assertTrue(brd.movePiece("f7","e6"));//red\
//		assertTrue(brd.movePiece("b1","c0"));//black
//		brd.printBoard();
//		assertTrue(brd.movePiece("f1", "d3b1"));
//		brd.printBoard();
//	}
//}

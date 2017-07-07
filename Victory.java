public class Victory {
	
	Piece[][] board;
	
	public Victory(Piece[][] board){
		this.board = board;
	}
	
	
	public boolean checkVictory(){
		int[] count = colorCount();
		if(count[0] == 0 || count[1] == 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 0 is R, 1 is B
	 * @return
	 */
	public int[] colorCount(){
		int[] count = new int[2]; // 0 is R, 1 is black
		for(int row = 0; row < 8; row++){
			for(int col = 0; col < 8; col++){
				if(board[row][col] == null){
				}
				else if(board[row][col].team() == 'r'){
					count[0]++;
				}
				else{
					count[1]++;
				}
			}
		}
		return count;
	}
}

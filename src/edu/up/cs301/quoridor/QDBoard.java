package edu.up.cs301.quoridor;



	import android.app.AlertDialog;
	import android.content.DialogInterface;
	import android.graphics.Color;
	import java.awt.*;

	/**
	 * Board class
	 * holds the info for what a board is
	 * holds methods to move and shuffle pieces
	 * holds method to see if user has won the game
	 * @author Sara Meisburger
	 * @version 22 September 2014
	 *
	 */
	public class QDBoard {

		//DECLARATIONS
		private int boardSize = 9;//board game size, get input from main activity
		private int[][] winningBoard; //initialize a board that we check against to see if win
		private int[][] pieces;//array of game pieces that will keep track of current board
		private int emptyPieceX = boardSize-1; //keep track of empty piece x
		private int emptyPieceY = boardSize-1; //keep track of empty piece y

		public QDBoard(int boardSize){
			this.boardSize = boardSize; //create a new board
			pieces = new int[boardSize][boardSize]; //make a 2D array based on input size
			winningBoard = new int [boardSize][boardSize]; //create matching winning board based on size

			int i=0; //iterator
			int j=0; //iterator
			int value = 1; //keep track of tile value
			for (i=0; i< boardSize; i++){
				for (j=0; j< boardSize;j++){
					if ((i == boardSize-1) && (j == boardSize -1)){
						pieces[i][j] = 0; //create last square as a value of 0
						winningBoard[i][j] = 0; //create a winning board where last value is 0
						emptyPieceX = j; //store empty piece x
						emptyPieceY = i; //store empty peice y
					}
					else{
						pieces[i][j] = value; //fill out rest of values in board
						winningBoard[i][j] = value; //fill out rest of values in winning board
						value++;
					}
				}
			}
		}
		
		/**getValue()
		 * @param int i
		 * @param int j
		 * take location of piece and return its tile value
		 */
		public int getValue(int i, int j){
			int value = pieces[i][j];
			return value;
		}

		/**movePieceDown()
		 * move a piece down into empty spot, move empty space into old space
		 */
		public void movePieceDown(){
			if (emptyPieceY == 0){ //check if valid move, if not, return
				return;
			}
			int temp = pieces[emptyPieceY-1][emptyPieceX];
			pieces[emptyPieceY-1][emptyPieceX] = 0;
			pieces[emptyPieceY][emptyPieceX] = temp;

			emptyPieceY = emptyPieceY-1;

		}
		
		/**movePieceRight()
		 * move a piece right into empty spot, move empty space into old space
		 */
		public void movePieceRight(){
			if (emptyPieceX == 0){ //check if valid move, if not, return
				return;
			}
			int temp = pieces[emptyPieceY][emptyPieceX-1];
			pieces[emptyPieceY][emptyPieceX-1] = 0;
			pieces[emptyPieceY][emptyPieceX] = temp;

			emptyPieceX = emptyPieceX -1;
		}
		/**movePieceLeft()
		 * move a piece left into empty spot, move empty space into old space
		 */
		public void movePieceLeft(){
			if (emptyPieceX == boardSize-1){ //check if valid move, if not, return
				return;
			}
			int temp = pieces[emptyPieceY][emptyPieceX+1];
			pieces[emptyPieceY][emptyPieceX+1] = 0;
			pieces[emptyPieceY][emptyPieceX] = temp;

			emptyPieceX = emptyPieceX +1;
		}
		
		/**movePieceUp()
		 * move a piece up into empty spot, move empty space into old space
		 */
		public void movePieceUp(){
			if (emptyPieceY == boardSize-1){ //check if valid move, if not, return
				return;
			}
			int temp = pieces[emptyPieceY+1][emptyPieceX];
			pieces[emptyPieceY+1][emptyPieceX] = 0;
			pieces[emptyPieceY][emptyPieceX] = temp;

			emptyPieceY = emptyPieceY+1;

		}
		
		/**shuffleBoard()
		 * make 750 random movements of blank tile to shuffle board. 
		 * This also ensures that the game is can be won
		 * create random number, that number corresponds to a move
		 */
		public void shuffleBoard(){

			for (int i=0; i < 750; i++){
				int random = (int)(Math.random()*4);
				if (random == 0){
					movePieceDown();
				}
				if (random == 1){
					movePieceUp();
				}
				if (random == 2){
					movePieceLeft();
				}
				if (random == 3){
					movePieceRight();
				}
			}
		}

		/**hasWon()
		 * compare the current board to what a winning board would look like
		 * return true if they match
		 */
		public boolean hasWon(){
			for (int i=0; i <boardSize; i++){
				for (int j=0;j<boardSize;j++){
					if (pieces[i][j] != winningBoard[i][j]){
						return false;
					}	
				}
			}
			return true;
		}
	}








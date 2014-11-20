package edu.up.cs301.quoridor;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;

/**
 * The TTTLocalGame class for a simple tic-tac-toe game.  Defines and enforces
 * the game rules; handles interactions with players.
 * 
 * @author Steven R. Vegdahl 
 * @version July 2013
 */

public class QDLocalGame extends LocalGame implements QDGame {

	// the game's state
	protected QDState state;

	// the marks for player 0 and player 1, respectively
	private final static char[] mark = {'X','O'};

	// the number of moves that have been played so far, used to
	// determine whether the game is over
	protected int moveCount;

	/**
	 * Constructor for the TTTLocalGame.
	 */
	public QDLocalGame() {

		// perform superclass initialization
		super();

		// create a new, unfilled-in TTTState object
		state = new QDState(2);
	}

	/**
	 * Check if the game is over. It is over, return a string that tells
	 * who the winner(s), if any, are. If the game is not over, return null;
	 * 
	 * @return
	 * 		a message that tells who has won the game, or null if the
	 * 		game is not over
	 */
	@Override
	protected String checkIfGameOver() {

		// the idea is that we simultaneously look at a row, column and
		// a diagonal, using the variables 'rowToken', 'colToken' and
		// 'diagToken'; we do this three times so that we get all three
		// rows, all three columns, and both diagonals.  (The way the
		// math works out, one of the diagonal tests tests the middle
		// column.)  The respective variables get set to ' ' unless
		// all characters in the line that have currently been seen are
		// identical; in this case the variable contains that character

		// the character that will eventually contain an 'X' or 'O' if we
		// find a winner
		char resultChar = ' ';

		// to all three lines in the current group
		for (int i = 0; i < 3; i++) {
			// get the initial character in each line
//			char rowToken = state.getPiece(i,0);
//			char colToken = state.getPiece(0,i);;
//			char diagToken = state.getPiece(0,i);
//			// determine the direction that the diagonal moves
//			int diagDelta = 1-i;
//			// look for matches for each of the three positions in each
//			// of the current lines; set the corresponding variable to ' '
//			// if a mismatch is found
//			for (int j = 1; j < 3; j++) {
//				if (state.getPiece(i,j) != rowToken) rowToken = ' ';
//				if (state.getPiece(j,i) != colToken) colToken = ' ';
//				if (state.getPiece(j, i+(diagDelta*j)) != diagToken) diagToken = ' ';
//			}

			////////////////////////////////////////////////////////////
			// At this point, if any of our three variables is non-blank
			// then we have found a winner.
			////////////////////////////////////////////////////////////

			// if we find a winner, indicate such by setting 'resultChar'
			// to the winning mark.
//			if (rowToken != ' ') resultChar = rowToken;
//			else if (colToken != ' ') resultChar = colToken;
//			else if (diagToken != ' ') resultChar = diagToken;
		}

		// if resultChar is blank, we found no winner, so return null,
		// unless the board is filled up. In that case, it's a cat's game.
		if (resultChar == ' ') {
			if  (moveCount >= 1000) {
				// no winner, but all 9 spots have been filled
				return "It's a cat's game.";
			}
			else {
				return null; // no winner, but game not over
			}
		}

		// if we get here, then we've found a winner, so return the 0/1
		// value that corresponds to that mark; then return a message
		int gameWinner = resultChar == mark[0] ? 0 : 1;
		return playerNames[gameWinner]+" is the winner.";
	}

	/**
	 * Notify the given player that its state has changed. This should involve sending
	 * a GameInfo object to the player. If the game is not a perfect-information game
	 * this method should remove any information from the game that the player is not
	 * allowed to know.
	 * 
	 * @param p
	 * 			the player to notify
	 */
	@Override
	protected void sendUpdatedStateTo(GamePlayer p) {
		// make a copy of the state, and send it to the player
		p.sendInfo(new QDState(state));

	}

	/**
	 * Tell whether the given player is allowed to make a move at the
	 * present point in the game. 
	 * 
	 * @param playerIdx
	 * 		the player's player-number (ID)
	 * @return
	 * 		true iff the player is allowed to move
	 */
	protected boolean canMove(int playerIdx) {
		return playerIdx == state.getWhoseMove();
	}

	/**
	 * Makes a move on behalf of a player.
	 * 
	 * @param action
	 * 			The move that the player has sent to the game
	 * @return
	 * 			Tells whether the move was a legal one.
	 */
	@Override
	protected boolean makeMove(GameAction action) {



		// get the 0/1 id of our player
		

		// if that space is not blank, indicate an illegal move
//		if (state.getPiece(row, col) != ' ') {
//			return false;
//		}

		// get the 0/1 id of the player whose move it is
		int whoseMove = state.getWhoseMove();

		// place the player's piece on the selected square
//		state.setPiece(row, col, mark[playerId]);
		boolean changeTurns = false;
		
		if (action instanceof QDMovePawnAction) {
			QDMovePawnAction tm = (QDMovePawnAction) action;
			int y = tm.getY();
			int x = tm.getX();
			
			int playerId = getPlayerIdx(tm.getPlayer());
//			int whoseMove = state.getWhoseMove();
			
			changeTurns = state.movePawn(playerId, x, y);
		}
		else if (action instanceof QDMoveWallAction) {
			QDMoveWallAction tm = (QDMoveWallAction) action;
			int x = tm.getX();
			int y = tm.getY();
			int ori = tm.getOri();
			
			int playerId = getPlayerIdx(tm.getPlayer());
//			int whoseMove = state.getWhoseMove();
			
			changeTurns = state.placeWall(playerId, x, y, ori);
		}
		// get the row and column position of the player's move
		


		// if the move was successful, make it the other player's turn
		if(changeTurns)	{
			state.nextTurn();
		}
		// bump the move count
		moveCount++;
		
		// return true, indicating the it was a legal move
		return true;
	}
	
	private boolean canPlaceWall(int p, int x, int y, int dir) {
		// Obvious guards, valid inputs, etc
		
		// players
		if (p < 0 || p >= playerNames.length) { return false; }
		
		// X and Y are valid
		if (y < 0 || y >= state.getWallsLoc().length)    { return false; }
		if (x < 0 || x >= state.getWallsLoc()[y].length) { return false; }
		
		// Valid direction/orientation
		if (dir != QDState.VERTICAL && dir != QDState.HORIZONTAL) { return false; }
		
		// Check if the intersection is occupied, if it is, the move
		// is illegal
		if (state.intersectIsWalled(x, y)) { return false; }
		
		// Are any of the spaces where the new wall will be already occupied?
		if (dir == QDState.VERTICAL) {
			if (state.isWalled(x, y, QDState.RIGHT)
					|| state.isWalled(x, y+1, QDState.RIGHT)) {
				return false;
			}
		} else if (dir == QDState.HORIZONTAL) {
			if(state.isWalled(x, y, QDState.DOWN)
					|| state.isWalled(x+1, y, QDState.DOWN)) {
				return false;
			}
		}
		
		// And does the player have any walls remaining?
		if (state.getWallsRem()[p] == 0) { return false; }
		
		// TODO Check for blocking path to goal
		
		return true;
	}

}

package edu.up.cs301.quoridor;

import android.graphics.Point;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.NotYourTurnInfo;
import edu.up.cs301.quoridor.QDState;

/**
 * This is a really dumb computer player that always just makes a random move
 * Half of the time, it tries to move a wall, and the rest of the time, it moves
 * the pawn in a random legal direction.
 * 
 * 
 * @author Steven R. Vegdahl
 * @versio2 July 2013 
 */

public class QDComputerPlayer2 extends QDComputerPlayer
{

	protected QDState state; 
	protected Point[] moves; 
	//private Point[] pawns;

	/*
	 * Constructor for the TTTComputerPlayer1 class
	 */
	public QDComputerPlayer2(String name) {
		// invoke superclass constructor
		super(name); // invoke superclass constructor
	}

	/**
	 * Called when the player receives a game-state (or other info) from the
	 * game.
	 * 
	 * @param gameInfo
	 * 		the message from the game
	 */

	/**
	 * Called when the player receives a game-state (or other info) from the
	 * game.
	 * 
	 * @param gameInfo
	 * 		the message from the game
	 */
	@Override
	protected void receiveInfo(GameInfo info) {

		// if it was a "not your turn" message, just ignore it
		if (info instanceof NotYourTurnInfo) return;

		//otherwise, ComputearPlayer1's turn, so needs to make a move

		//make it look like we're thinking
		
		int opponent; 
		sleep(1000);

		//randomly place pawn or wall
		int piece;
		//decide pawn or wall
		int val = (int)(Math.random()*4);

		if(val == 0){
			piece = 0;
		}//wall
		else {
			piece = 1;
		}//pawn

		//int counter = 0;
		//check if pawn or wall
		if(piece == 0) {
			//randomly choose any intersection
			if (state.getPawns().length > 0) //getPawns.length == 2){
				opponent = (int)(Math.random() * state.getPawns().length);
				while (opponent == playerNum)
				{
					opponent = (int)(Math.random() * state.getPawns().length);

				}
			}
		
			int x = (int)(Math.random()*8);
			int y = (int)(Math.random()*8);

			//randomly choose an orientation
			int ori = (int)(Math.random() * 2);
			//if 0, vertical
			if (ori == 0) { ori = QDState.VERTICAL;}
			else {ori = QDState.HORIZONTAL;}

			//create place wall action and send it to the state
			//QDLocalGame.makeMove(new QDMoveWallAction(this, x, y, ori));
			game.sendAction(new QDMoveWallAction(this, x, y, ori));

			//now, send the action to see if it goes through
		} else {
			//move pawn instead
			//first, get the legal moves from the state
			this.state = (QDState) info;
			moves = state.legalPawnMoves(1); //???

			//randomly pick a valid move
			if(moves != null)
			{
				int i = (int)(Math.random() * (moves.length));
				game.sendAction(new QDMovePawnAction(this, moves[i].x, moves[i].y));
			} else {
				return;
			}
		}
	}

	//=======
	// Submit our move to the game object. We haven't even checked it it's
	// our turn, or that that position is unoccupied. If it was not our turn,
	// we'll get a message back that we'll ignore. If it was an illegal move,
	// we'll end up here again (and possibly again, and again). At some point,
	// we'll end up randomly pick a move that is legal.
	//		game.sendAction(new QDMovePawnAction(this, yVal, xVal));
	//
	//	}
	//
	//}
	//>>>>>>> origin/master
}
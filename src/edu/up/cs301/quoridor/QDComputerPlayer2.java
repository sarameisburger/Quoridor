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
	private Point[] pawns;

	//private Point[] pawns;

	/*
	 * Constructor for the TTTComputerPlayer1 class
	 */
	public QDComputerPlayer2(String name) {
		// invoke superclass constructor
		super(name); // invoke superclass constructor
	}
	protected void placeBlockingWall(int playerNum){
		int x = state.getPawns()[playerNum].x;
		int y = state.getPawns()[playerNum].y;
				
		if (playerNum == 0){
			if (state.canPlaceWall(playerNum, x, y-1, QDState.HORIZONTAL) == true){
				game.sendAction(new QDMoveWallAction(this, x, y-1, QDState.HORIZONTAL));
				//return true;
			} else if (state.canPlaceWall(playerNum, x-1, y-1, QDState.HORIZONTAL) == true){
				game.sendAction(new QDMoveWallAction(this, x-1, y-1, QDState.HORIZONTAL));
			}
				
		}
		else if (playerNum == 1){
			if (state.canPlaceWall(playerNum, x, y, QDState.HORIZONTAL) == true){
				game.sendAction(new QDMoveWallAction(this, x, y, QDState.HORIZONTAL));
			} else if (state.canPlaceWall(playerNum, x-1, y, QDState.HORIZONTAL) == true){
				game.sendAction(new QDMoveWallAction(this, x-1, y, QDState.HORIZONTAL));
			}
		}
		else if (playerNum == 2){
			if (state.canPlaceWall(playerNum, x, y, QDState.VERTICAL) == true){
				game.sendAction(new QDMoveWallAction(this, x, y, QDState.VERTICAL));
			} else if (state.canPlaceWall(playerNum, x, y-1, QDState.VERTICAL) == true){
				game.sendAction(new QDMoveWallAction(this, x, y-1, QDState.VERTICAL));
			}
		}
		else if (playerNum == 3){
			if (state.canPlaceWall(playerNum, x-1, y, QDState.VERTICAL) == true){
				game.sendAction(new QDMoveWallAction(this, x-1, y, QDState.VERTICAL));
			} else if (state.canPlaceWall(playerNum, x-1, y-1, QDState.VERTICAL) == true){
				game.sendAction(new QDMoveWallAction(this, x-1, y-1, QDState.VERTICAL));
			}
		}
	}

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
		this.state = (QDState) info;
		//otherwise, ComputearPlayer1's turn, so needs to make a move

		//make it look like we're thinking

		int opponent; 
		sleep(1000);

		if(state.getPawns() != null)
		{
			pawns = state.getPawns();
		}
		moves = state.legalPawnMoves(playerNum);

		//check if we are about to win - if so, win
		for (int i = 0; i < moves.length; i++){
			if (playerNum == 0){
				if (moves[i].y == 0){
					//send the move
					game.sendAction(new QDMovePawnAction(this, moves[i].x, moves[i].y));
					//return;
				}
			}
			if (playerNum == 1){
				if (moves[i].y == 8){
					game.sendAction(new QDMovePawnAction(this, moves[i].x, moves[i].y));
					//return;
				}
			}

			if (playerNum == 2){
				if (moves[i].x == 8){
					game.sendAction(new QDMovePawnAction(this, moves[i].x, moves[i].y));
					//return;
				}
			}

			if (playerNum == 3){
				if (moves[i].x == 0){
					game.sendAction(new QDMovePawnAction(this, moves[i].x, moves[i].y));
					//return;
				}
			}
		}

		//check if someone else about to win, and block them in order of whose turn is next
		int counter = 0;
		for (int i = playerNum; i < state.getPawns().length; i++){
			counter++;
			if(i == playerNum) i++;
			if (i == state.getPawns().length ) i = 0; 
			
			
			if (i == 0){
				if (pawns[i].y - 1 == 0){
					//PLACE BLOCKING WALL
					placeBlockingWall(i);
				}
			} else if (i == 1){
				if (pawns[i].y + 1 == 8){
					//PLACE BLOCKING WALL
					placeBlockingWall(i);
				}
			} else if (i == 2){
				if (pawns[i].x + 1 == 8){
					//PLACE BLOCKING WALL
					placeBlockingWall(i);
				}
			} else if (i == 3){
				if (pawns[i].x - 1 == 0){
					//PLACE BLOCKING WALL
					placeBlockingWall(i);
				}
			}
			if (counter > state.getPawns().length) i = 5;
		}

		// otherwise randomly place pawn or wall
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
			//randomly choose another player to block
			if (state.getPawns().length > 0) {//getPawns.length == 2){
				opponent = (int)(Math.random() * state.getPawns().length);
				while (opponent == playerNum)
				{
					opponent = (int)(Math.random() * state.getPawns().length);
				}
				placeBlockingWall(opponent);
			}
		} else {
			//move pawn instead
			//first, get the legal moves from the state
			this.state = (QDState) info;
			moves = state.legalPawnMoves(playerNum); //???

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
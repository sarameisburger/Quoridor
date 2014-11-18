package edu.up.cs301.quoridor;

import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.NotYourTurnInfo;

/**
 * This is a really dumb computer player that always just makes a random move
 * it's so stupid that it sometimes tries to make moves on non-blank spots.
 * 
 * @author Steven R. Vegdahl
 * @versio2 July 2013 
 */

public class QDComputerPlayer1 extends QDComputerPlayer
{
	/*
	 * Constructor for the TTTComputerPlayer1 class
	 */
	public QDComputerPlayer1(String name) {
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
	//	@Override
	//	protected void receiveInfo(GameInfo info) {
	//
	//		// if it was a "not your turn" message, just ignore it
	//		if (info instanceof NotYourTurnInfo) return;
	//
	//		int piece;
	//		//decide pawn or wall
	//		int val = (int)(Math.random()*2);
	//		if(val==0){piece = 0;}//wall
	//		else{piece = 1;}//pawn
	//		//get pawn or wall then decide on location
	//		if(piece==0){
	//			int x = (int)(Math.random()*9);
	//			int y = (int)(Math.random()*9);
	//			game.sendAction(new QDMoveWallAction(this, x,y));
	//		}
	//
	//
	//		// pick x and y positions at random (0-2)
	//		int xVal = (int)(3*Math.random());
	//		int yVal = (int)(3*Math.random());
	//
	//		// delay for a second to make opponent think we're thinking
	//		sleep(1000);
	//
	//<<<<<<< HEAD
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

		//randomly place pawn or wall
		int piece;
		//decide pawn or wall
		int val = (int)(Math.random()*2);
		
		if(val == 0){
			piece = 0;
		}//wall
		else {
			piece = 1;
		}//pawn
		
		int x = (int)(Math.random()*9);
		int y = (int)(Math.random()*9);
		int counter=0;
		//get pawn or wall then randomly select location

//		//for walls try to randomly place in 5 intersections in both configurations
//		if(piece == 0){
//			do{ 
//				x = (int)(Math.random()*9);
//				y = (int)(Math.random()*9);
//
//				//try horizontal then vertical
//				game.sendAction(new QDMoveWallAction(this, x, y, QDState.HORIZONTAL));
//				if(!QDMoveWallAction(this, x, y)){
//					game.sendAction(new QDMoveWallAction(this, x, y, QDState.VERTICAL));
//				}
//				counter++;
//			} while(!QDMoveWallAction(this, x, y) && counter < 5);
//			//if no legal wall placement is found, move pawn instead
//			if(!QDMoveWallAction(this, x, y)) { piece = 1; }
//		}
//		if(piece == 1){
//			while(!QDMovePawnAction(this,x,y)){
//				//get legal moves and randomly pick one to execute
//
//				game.sendAction(new QDMovePawnAction(this, x, y));
//			}


		}

		//    	// pick x and y positions at random (0-2)
		//    	int xVal = (int)(3*Math.random());
		//    	int yVal = (int)(3*Math.random());
		//
		//    	// delay for a second to make opponent think we're thinking
		//    	sleep(1000);
		//
		//    	// Submit our move to the game object. We haven't even checked it it's
		//    	// our turn, or that that position is unoccupied. If it was not our turn,
		//    	// we'll get a message back that we'll ignore. If it was an illegal move,
		//    	// we'll end up here again (and possibly again, and again). At some point,
		//    	// we'll end up randomly pick a move that is legal.
		//    	game.sendAction(new QDMovePawnAction(this, yVal, xVal));

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


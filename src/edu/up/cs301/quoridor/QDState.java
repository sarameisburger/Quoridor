package edu.up.cs301.quoridor;

import android.graphics.Point;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.infoMsg.GameState;


/**
 * Contains the state of a Quoridor game.  Sent by the game when
 * a player wants to enquire about the state of the game.  (E.g., to display
 * it, or to help figure out its next move.)
 * 
 * @author Steven R. Vegdahl 
 * @version July 2013
 */
public class QDState extends GameState
{
	private static final long serialVersionUID = 7552321013488624386L;

    ///////////////////////////////////////////////////
    // ************** instance variables ************
    ///////////////////////////////////////////////////
	
	private char[][] board;
    
    // the coordinates of the pawns
    private Point[] pawns;
    
    // the mysterious wall array
    private int[][] wallLoc;
    
    // each player's remaining walls
    private int[] wallRem;
    
    // an int that tells whose move it is
    private int playerToMove;
    
    // players
    private GamePlayer[] players;
    
    // Constants
    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;
    private static final int VERTICAL = 4;
    private static final int HORIZONTAL = 5;
    

    /**
     * Constructor for objects of class QDState
     */
    public QDState(int players)
    {
        // initialize the state to be a brand new game
//        board = new char[3][3];
//        for (int i = 0; i < 3; i++) {
//        	for (int j = 0; j < 3; j++) {
//        		board[i][j] = ' ';
//        	}
//        }
    	
    	pawns = new Point[players];
    	
    	wallLoc = new int[9][9];
    	
    	for (int i = 0; i < wallLoc.length; i++) {
    		for (int j = 0; j < wallLoc[i].length; j++) {
    			wallLoc[i][j] = 0;
    		}
    	}
    	
    	// QD TODO: pawns based on number of players
    	// QD TODO: initialize empty wall array
        
        // make it player 0's move
        playerToMove = 0;
    }// constructor
    
    /**
     * Copy constructor for class TTTState
     *  
     * @param original
     * 		the TTTState object that we want to clong
     */
    public QDState(QDState original)
    {
    	// create a new 3x3 array, and copy the values from
    	// the original
//    	board = new char[3][3];
//    	for (int i = 0; i < 3; i++) {
//    		for (int j = 0; j < 3; j++) {
//    			board[i][j] = original.board[i][j];
//    		}
//    	}
    	
    	// copy the player-to-move information
        playerToMove = original.playerToMove;
    }

    /**
     * 
     * @return
     */
    public Point[] getPawns() {
    	return pawns;
    }

    /**
     * 
     * @return
     */
    public int[][] getWallsLoc() {
    	return wallLoc;
    }
    
    /**
     * Tells whose move it is.
     * 
     * @return the index (0-3) of the player whose move it is.
     */
    public int getWhoseMove() {
        return playerToMove;
    }
    
    /**
     * set whose move it is
     * @param id
     * 		the player we want to set as to whose move it is
     */
    public void setWhoseMove(int id) {
    	playerToMove = id;
    }
    
    public boolean isWalled(int x, int y, int dir) {
    	return false;
    }
    
    public boolean intersectIsWalled(int x, int y) {
    	return false;
    }
    
    public int getCurrTurn() {
    	return 0;
    }
    
    public void movePawn(int p, int x, int y) {
    	return;
    }
    
    public void placeWall(int p, int x, int y, int dir) {
    	return;
    }
    
    public void nextTurn() {
    	return;
    }
    
    public String encode() {
    	return null;
    }
    
    public QDState makeCopy() {
    	return this;
    }
}

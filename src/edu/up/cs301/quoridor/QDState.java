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
	
//	private char[][] board;
    
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
    private static final int EMPTY = 0;
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private static final int UP = 4;
    private static final int DOWN = 8;
    private static final int VERTICAL = 16;
    private static final int HORIZONTAL = 32;
    

    /**
     * Constructor for objects of class QDState
     */
    public QDState(int players)
    {	
    	pawns = new Point[players];
    	wallRem = new int[players];
    	
    	// Initialize pawns
    	if (pawns.length >= 2) {
    		pawns[0] = new Point(4,8);
    		pawns[1] = new Point(4,0);
    		wallRem[0] = 10;
    		wallRem[1] = 10;
    	}
    	if (pawns.length == 4) {
    		pawns[2] = new Point(0,4);
    		pawns[3] = new Point(8,4);
    		for (int i=0; i < wallRem.length; i++) {
    			wallRem[i] = 5;
    		}
    	}
    	
    	// QD TODO: pawns based on number of players
//    	players[players] = new GamePlayer();
    	// QD TODO: initialize empty wall array
    	wallLoc = new int[9][9];
    	
    	for (int i = 0; i < wallLoc.length; i++) {
    		for (int j = 0; j < wallLoc[i].length; j++) {
    			wallLoc[i][j] = 0;
    		}
    	}
        
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
    	pawns = original.pawns;
    	
    	wallRem = original.wallRem;
    	// create a new wall array, and copy the values from
    	// the original
    	wallLoc = new int[9][9];
    	for (int i = 0; i < wallLoc.length; i++) {
    		for (int j = 0; j < wallLoc.length; j++) {
    			wallLoc[i][j] = original.wallLoc[i][j];
    		}
    	}
    	
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
    	return playerToMove;
    }
    
    public void movePawn(int p, int x, int y) {
    	if (p > pawns.length || x > 9 || y > 9) { return; }
    	pawns[p] = new Point(x,y);
    	return;
    }
    
    public void placeWall(int p, int x, int y, int dir) {
    	// guard
    	
    	if (intersectIsWalled(x, y)) { return; }
    	
    	if (dir == HORIZONTAL) {
    		wallLoc[y][x] |= RIGHT;
    		wallLoc[y][x+1] |= LEFT;
    		wallLoc
    	}
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

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
    public static final int EMPTY = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int UP = 4;
    public static final int DOWN = 8;
    public static final int VERTICAL = 16;
    public static final int HORIZONTAL = 32;
    

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
    
    public int[] getWallsRem() {
    	return wallRem;
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
    	return (wallLoc[y][x] & dir) == dir; 
//    	return false;
    }
    
    public boolean intersectIsWalled(int x, int y) {
    	boolean walled = true;
    	int walledSides = 0;
    	
    	// Check if there are any walls at all
    	if (wallLoc[y][x] == 0
    			&& wallLoc[y+1][x] == 0
    			&& wallLoc[y][x+1] == 0
    			&& wallLoc[y+1][x+1] == 0) {
    		
    		return false;
    	}
    	
    	// Check Vertically
    	if (((wallLoc[y][x] & RIGHT) == RIGHT || (wallLoc[y][x+1] & LEFT) == LEFT)
    			&& ((wallLoc[y+1][x] & RIGHT) == RIGHT || (wallLoc[y+1][x+1] & LEFT) == LEFT)) {
    		// There's a wall on top and bottom of intersection
    		
    		// If the intersection is at an edge, it must be walled
    		if (y == 0 || y == wallLoc.length - 1) { return true; }
    		
    		// Count walled sides down to zero, if it's an even
    		// number, then the intersection is open
    		walledSides = 0;
    		for (int i = y; i >= 0; i--) {
    			if ((wallLoc[i][x] & RIGHT) == RIGHT) {
    				walledSides++;
    			}
    		}
    		
    		// Odd walled sides means a wall crosses the intersection
    		return walledSides % 2 != 0;
    	}
    	
    	// Check Horizontally
    	if (((wallLoc[y][x] & DOWN) == DOWN || (wallLoc[y+1][x] & UP) == UP)
    			&& ((wallLoc[y][x+1] & DOWN) == DOWN || (wallLoc[y+1][x+1] & UP) == UP)) {
    		// There's a wall on left and right of intersection
    		
    		// If the intersection is at an edge, it must be walled
    		if (x == 0 || x == wallLoc.length - 1) { return true; }
    		
    		// Count walled sides down to zero, if it's an even
    		// number, then the intersection is open
    		walledSides = 0;
    		for (int i = x; i >= 0; i--) {
    			if ((wallLoc[y][i] & DOWN) == DOWN) {
    				walledSides++;
    			}
    		}
    		
    		// Odd walled sides means a wall crosses the intersection
    		return walledSides % 2 != 0;
    	}
    	
    	return false;
    }
    
    public int getCurrTurn() {
    	return playerToMove;
    }
    
    public boolean movePawn(int p, int x, int y) {
    	if (p >= pawns.length || x >= 9 || y >= 9 || x < 0 || y < 0) { return false; }
    	pawns[p] = new Point(x,y);
    	return true;
    }
    
    public boolean placeWall(int p, int x, int y, int dir) {
    	// guard
    	
    	// Stuff LocalGame should check, but we can double check
    	if (intersectIsWalled(x, y)
    			|| p >= wallRem.length
    			|| wallRem[p] == 0) {
    		return false;
    	}
    	
        if (dir == VERTICAL) {
        	// Double check a wall isn't already there
            if ((wallLoc[y][x] & RIGHT) == RIGHT
                    || (wallLoc[y][x+1] & LEFT) == LEFT
                    || (wallLoc[y+1][x] & RIGHT) == RIGHT
                    || (wallLoc[y+1][x+1] & LEFT) == LEFT) {
                return false;
            }
            
            wallLoc[y][x] |= RIGHT;
            wallLoc[y][x+1] |= LEFT;
            wallLoc[y+1][x] |= RIGHT;
            wallLoc[y+1][x+1] |= LEFT;
        } else if (dir == HORIZONTAL) {
        	// Double check a wall isn't already there
            if ((wallLoc[y][x] & DOWN) == DOWN
                    || (wallLoc[y][x+1] & DOWN) == DOWN
                    || (wallLoc[y+1][x] & UP) == UP
                    || (wallLoc[y+1][x+1] & UP) == UP) {
                return false;
            }
            
            wallLoc[y][x] |= DOWN;
            wallLoc[y][x+1] |= DOWN;
            wallLoc[y+1][x] |= UP;
            wallLoc[y+1][x+1] |= UP;
        }
    	
    	wallRem[p]--;
    	
    	return true;
    }
    
    public void nextTurn() {
    	playerToMove = (playerToMove + 1) % pawns.length;
    	return;
    }
    
    public String encode() {
    	return null;
    }
    
    public QDState makeCopy() {
    	return this;
    }
}

package edu.up.cs301.quoridor;

import java.util.ArrayList;
import java.util.Arrays;

import android.graphics.Point;
import android.util.Log;
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
	//intersections and whether they are filled
	private int[][] intersections;

	// the coordinates of the pawns
	private Point[] pawns;

	// the mysterious wall array
	private int[][] wallLoc;
	private int[][] wallLocTemp;

	// each player's remaining walls
	private int[] wallRem;

	// an int that tells whose move it is
	private int playerToMove;

	// players
	private GamePlayer[] players;

	// Legal pawn moves per player, not safe
	// to directly access. Don't do it.
	private ArrayList<Point> legalMoves;
	
	// Filled paths for winnability checking
	private boolean filledPath[][];

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


		// Wall location arrays
		wallLoc = new int[9][9];
		wallLocTemp = new int[9][9];
		
		// Array of intersections and walled-ness
		intersections = new int[8][8];

		// Initialize wall arrays
		for (int i = 0; i < wallLoc.length; i++) {
			for (int j = 0; j < wallLoc[i].length; j++) {
				wallLoc[i][j] = 0;
				wallLocTemp[i][j] = 0;
			}
		}

		// Initialize intersections array
		for (int i = 0; i < intersections.length; i++) {
			for (int j = 0; j < intersections[i].length; j++) {
				intersections[i][j] = 0;
			}
		}
		
		// Array used for winnability checking
		filledPath = new boolean[wallLoc.length][wallLoc[0].length];
		
		//

		// make it player 0's move
		playerToMove = 0;
	}// constructor

	/**
	 * Copy constructor for class QDState
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
		wallLocTemp = new int[9][9];
		for (int i = 0; i < wallLoc.length; i++) {
			for (int j = 0; j < wallLoc.length; j++) {
				wallLoc[i][j] = original.wallLoc[i][j];
				wallLocTemp[i][j] = original.wallLocTemp[i][j];
			}
		}

		// Copy other data
		intersections = original.intersections;
		filledPath = original.filledPath;

		// copy the player-to-move information
		playerToMove = original.playerToMove;
	}

	/**
	 * getPawns
	 * 
	 * @return Locations of all pawns
	 */
	public Point[] getPawns() {
		return pawns;
	}

	/**
	 * getWallsRem
	 * 
	 * @return Array of walls remaining per player
	 */
	public int[] getWallsRem() {
		return wallRem;
	}

	/**
	 * getWallsLoc
	 * 
	 * @return Array of wall locations
	 */
	public int[][] getWallsLoc() {
		return wallLoc;
	}

	/**
	 * getIntersections
	 * 
	 * @return array of intersections and whether a wall passes through them
	 */
	public int[][] getIntersections() {
		return intersections;
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
	 * Gets id of player whose turn it is
	 * @return id of currently playing player
	 */
	public int getCurrTurn() {
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

	/**
	 * Returns an array of Points of legal coordinates
	 * the player's pawn can move to.
	 * 
	 * @param p player to retrieve legal moves for
	 * @return Array of Points of legal locations for player's
	 * 		   pawn to move it
	 */
	public Point[] legalPawnMoves(int p) {
		// Player range guard. If it's not, return an empty
		// point array.
		if (p < 0 || p >= pawns.length) { return new Point[0]; }
	
		// Fresh legalMoves ArrayList
		legalMoves = new ArrayList<Point>();
	
		// Check each direction of potential allowed moves
		// If there is a bordering pawn, find legal jump
		// moves.
	
		// LEFT
		if (pawns[p].x != 0) {
			if (!isWalled(pawns[p].x, pawns[p].y, LEFT)
					&& !isPawned(pawns[p].x - 1, pawns[p].y)) {
				legalMoves.add(new Point(pawns[p].x - 1, pawns[p].y));
			}
			else if (isPawned(pawns[p].x - 1, pawns[p].y)) {
				jumpMoves(pawns[p].x - 1, pawns[p].y);
			}
		}
	
		// RIGHT
		if (pawns[p].x != 8) {
			if (!isWalled(pawns[p].x, pawns[p].y, RIGHT)
					&& !isPawned(pawns[p].x + 1, pawns[p].y)) {
				legalMoves.add(new Point(pawns[p].x + 1, pawns[p].y));
			}
			else if (isPawned(pawns[p].x + 1, pawns[p].y)) {
				jumpMoves(pawns[p].x + 1, pawns[p].y);
			}
		}
	
		// UP
		if (pawns[p].y != 0) {
			if (!isWalled(pawns[p].x, pawns[p].y, UP)
					&& !isPawned(pawns[p].x, pawns[p].y - 1)) {
				legalMoves.add(new Point(pawns[p].x, pawns[p].y - 1));
			}
			else if (isPawned(pawns[p].x, pawns[p].y - 1)) {
				jumpMoves(pawns[p].x, pawns[p].y - 1);
			}
		}
	
		// DOWN
		if (pawns[p].y != 8) {
			if (!isWalled(pawns[p].x, pawns[p].y, DOWN)
					&& !isPawned(pawns[p].x, pawns[p].y + 1)) {
				legalMoves.add(new Point(pawns[p].x, pawns[p].y + 1));
			}
			else if (isPawned(pawns[p].x, pawns[p].y + 1)) {
				jumpMoves(pawns[p].x, pawns[p].y + 1);
			}
		}
	
		// Convert ArrayList of legal moves to an Array for convenience.
		Point[] legalArray = new Point[legalMoves.size()];
		legalArray = legalMoves.toArray(legalArray);
		legalMoves.clear();
	
		return legalArray;
	}

	/**
	 * Checks a square and direction for the existence of a wall
	 * 
	 * @param x coordinate of square
	 * @param y coordinate of square
	 * @param dir of square to check for wall
	 * @return If there is a wall in the direction of the square
	 */
	public boolean isWalled(int x, int y, int dir) {
		// guard for wall range
		if (x < 0 || y < 0 || x >= wallLoc.length || y >= wallLoc[x].length) { return false; }

		// Check if the direction bit is set in the wall location array
		return (wallLoc[y][x] & dir) == dir;
	}

	/**
	 * isWinnable
	 * 
	 * Returns true or false based on if the player given is
	 * still able to win. Used to check for wall placement legality.
	 * That is, blocking a player in is illegal.
	 * 
	 * @param player
	 * @return true if the player is able to win
	 */
	public boolean isWinnable(int p) {
		// guard for player value
		if (p < 0 || p >= pawns.length) { return false; }
		
		// Sets the goal row, 0 (top) for Player 1, 8 (bottom) for
		// Player 2
		int goal = p == 0 ? 0 : 8;
		
		// Creates a new filledPath array
		filledPath = new boolean[wallLoc.length][wallLoc[0].length];
		
		// Start the flood fill algorithm
		fillPath(pawns[p].x, pawns[p].y);
		
		// If any of the squares on the goal row are accessible,
		// then the player isn't blocked from winning.
		for (boolean b : filledPath[goal]) {
			if (b) { return true; }
		}
		
		// Or else the goal is blocked.
		return false;
	}

	/**
	 * isWinnableForAll
	 * 
	 * Calls isWinnable for each player.
	 * 
	 * @return true if all players can win
	 */
	public boolean isWinnableForAll() {
		
		// If any player can't win, then return false.
		for (int p = 0; p < pawns.length; p++) {
			if (!isWinnable(p)) { return false; }
		}
		
		return true;
	}

	public boolean intersectIsWalled(int x, int y) {
		
		// Guard for range
		if (x < 0 || y < 0 || y >= intersections.length || x >= intersections[y].length) {
			return false;
		}
		
		// If the specified intersection isn't 0, then a wall runs through it
		return intersections[y][x] != 0;
	}

	/**
	 * Checks if a pawn occupies the square specified.
	 * 
	 * @param x x coordinate of square to check
	 * @param y y coordinate of square to check
	 * @return True if there is a pawn at those coordinates
	 */
	public boolean isPawned(int x, int y) {
		// X and Y are valid square coordinates
		if (y < 0 || y >= wallLoc.length)    { return false; }
		if (x < 0 || x >= wallLoc[y].length) { return false; }
	
		// Check the coordinates of each pawn, return true
		// if any match the given x and y
		for (Point p : pawns) {
			if (p.x == x && p.y == y) { return true; }
		}
	
		// No match
		return false;
	}

	/**
	 * Checks if the wall is able to be placed in the specified
	 * location with the specified orientation by the specified player
	 * 
	 * @param p player to place wall
	 * @param x x coordinate of intersection of wall
	 * @param y y coordinate of intersection of wall
	 * @param dir orientation of wall
	 * @return true if the wall can be placed
	 */
	public boolean canPlaceWall(int p, int x, int y, int dir) {
		// Obvious guards, valid inputs, etc
	
		// players
		if (p < 0 || p >= pawns.length) { return false; }
	
		// X and Y are valid intersection coordinates
		if (y < 0 || y >= intersections.length)    { return false; }
		if (x < 0 || x >= intersections[y].length) { return false; }
	
		// Valid direction/orientation
		if (dir != VERTICAL && dir != HORIZONTAL) { return false; }
	
		// Check if the intersection is occupied, if it is, the move
		// is illegal
		if (intersectIsWalled(x, y)) { return false; }
	
		// Are any of the spaces where the new wall will be already occupied?
		if (dir == VERTICAL) {
			if (isWalled(x, y, RIGHT)
					|| isWalled(x, y+1, RIGHT)) {
				return false;
			}
		} else if (dir == HORIZONTAL) {
			if(isWalled(x, y, DOWN)
					|| isWalled(x+1, y, DOWN)) {
				return false;
			}
		}
	
		// And does the player have any walls remaining?
		if (getWallsRem()[p] == 0) { return false; }
		
		// Finally, check if this placement blocks any players from winning
		
		// Copy the current wallLoc array
		for (int i = 0; i < wallLoc.length; i++) {
			System.arraycopy(wallLoc[i], 0, wallLocTemp[i], 0, wallLoc[i].length); 
		}
	
		// Temporary place the wall
		if (dir == VERTICAL) {
			wallLoc[y][x] |= RIGHT;
			wallLoc[y][x+1] |= LEFT;
			wallLoc[y+1][x] |= RIGHT;
			wallLoc[y+1][x+1] |= LEFT;
		} else if (dir == HORIZONTAL) {
			wallLoc[y][x] |= DOWN;
			wallLoc[y][x+1] |= DOWN;
			wallLoc[y+1][x] |= UP;
			wallLoc[y+1][x+1] |= UP;
		}
		
		// Set variable to the legal value as clean-up is needed.
		boolean legal = isWinnableForAll();
		
		// Restore the original wallLoc array
		for (int i = 0; i < wallLocTemp.length; i++) {
			System.arraycopy(wallLocTemp[i], 0, wallLoc[i], 0, wallLoc[i].length); 
		}
		
		// Return the result of isWinnableForAll()
		return legal;
	}

	/**
	 * Attempts to place a wall on the board.
	 * 
	 * @param p player to place wall
	 * @param x x coordinate of intersection of wall
	 * @param y y coordinate of intersection of wall
	 * @param dir orientation of wall
	 * @return true if the wall was successfully placed
	 */
	public boolean placeWall(int p, int x, int y, int dir) {
		// guard for player
		if (p >= pawns.length) { return false; }
	
		// guard for coordinates
		if (y < 0 || y >= intersections.length)    { return false; }
		if (x < 0 || x >= intersections[y].length) { return false; }
		
		// Valid direction/orientation
		if (dir != VERTICAL && dir != HORIZONTAL) { return false; }
	
		// Check if the move is possible
		if (!canPlaceWall(p, x, y, dir)) { return false; }
	
		// Set the wall bits
		if (dir == VERTICAL) {
			wallLoc[y][x] |= RIGHT;
			wallLoc[y][x+1] |= LEFT;
			wallLoc[y+1][x] |= RIGHT;
			wallLoc[y+1][x+1] |= LEFT;
		} else if (dir == HORIZONTAL) {
			wallLoc[y][x] |= DOWN;
			wallLoc[y][x+1] |= DOWN;
			wallLoc[y+1][x] |= UP;
			wallLoc[y+1][x+1] |= UP;
		}
	
		// Deduct a wall from the player's wall supply
		wallRem[p]--;
	
		// Record the newly walled intersection
		intersections[y][x] = dir;
	
		return true;
	}

	/**
	 * Moves a pawn to a legal square
	 * @param p player whose pawn to move
	 * @param x x coordinate of pawn's new position
	 * @param y y coordinate of pawn's new position
	 * @return true if the move was successful
	 */
	public boolean movePawn(int p, int x, int y) {
		// Initial bounds check
		if (p >= pawns.length || x < 0 || y < 0 || y >= wallLoc.length || x >= wallLoc[y].length) {
			return false;
		}

		// Check through legal moves, make the move if legal
		Point[] legalMoves = legalPawnMoves(p);
		for (Point move : legalMoves) {
			if (move.x == x && move.y == y) {
				pawns[p] = new Point(x,y);
				return true;
			}
		}

		// Return false if pawn is unmoved
		return false;
	}

	/**
	 * Places a pawn with a disregard for all but
	 * bounds checks, used for testing.
	 * 
	 * @param p player whose pawn to move
	 * @param x x coordinate of pawn's new position
	 * @param y y coordinate of pawn's new position
	 * @return true if the move was successful
	 */
	public boolean placePawnManually(int p, int x, int y) {
		if (p >= pawns.length || x >= 9 || y >= 9 || x < 0 || y < 0) { return false; }

		pawns[p] = new Point(x,y);

		return true;
	}

	/**
	 * Advances the turn
	 */
	public void nextTurn() {
		playerToMove = (playerToMove + 1) % pawns.length;
		return;
	}

	/**
	 * Creates copy of the current QDState
	 * @return copy of current QDState
	 */
	public QDState makeCopy() {
		return this;
	}
	
	/**
	 * Based on algorithm found here:
	 * http://www.princeton.edu/~achaney/tmve/wiki100k/docs/Flood_fill.html
	 * 
	 * Basic flood fill algorithm to check if starting at a given location (a pawn)
	 * is able to reach any given square. In this case, player's goals.
	 * 
	 * @param x starting x coordinate 
	 * @param y starting x coordinate 
	 */
	private void fillPath(int x, int y) {
		
		// Edge conditions, out of bounds
		if (y < 0 || y >= filledPath.length)    { return; }
		if (x < 0 || x >= filledPath[y].length) { return; }
		
		// If the square has been marked, return
		if(filledPath[y][x]) { return; }
		
		// New square, set as accessible
		filledPath[y][x] = true;
		
        // Check going left, if the square is accessible from
		// the current square, recurse.
        if (x != 0) {
            if (!isWalled(x, y, LEFT)
                    && !isPawned(x - 1, y)) {
                fillPath(x - 1, y);
            }
        }
        
        // Check going right, if the square is accessible from
		// the current square, recurse.
        if (x != 8) {
            if (!isWalled(x, y, RIGHT)
                    && !isPawned(x + 1, y)) {
                fillPath(x + 1, y);
            }
        }
        
        // Check going up, if the square is accessible from
		// the current square, recurse.
        if (y != 0) {
            if (!isWalled(x, y, UP)
                    && !isPawned(x, y - 1)) {
                fillPath(x, y - 1);
            }
        }
        
        // Check going down, if the square is accessible from
		// the current square, recurse.
        if (y != 8) {
            if (!isWalled(x, y, DOWN)
                    && !isPawned(x, y + 1)) {
                fillPath(x, y + 1);
            }
        }
		
        // Done!
		return;
	}

	/**
	 * Adds legal moves jumping around a specified square
	 * 
	 * @param x x coordinate of square to jump over
	 * @param y y coordinate of square to jump over
	 */
	private void jumpMoves(int x, int y) {
	
		// LEFT
		if (x != 0) {
			if (!isWalled(x, y, LEFT)
					&& !isPawned(x - 1, y)) {
				legalMoves.add(new Point(x - 1, y));
			}
		}
	
		// RIGHT
		if (x != 8) {
			if (!isWalled(x, y, RIGHT)
					&& !isPawned(x + 1, y)) {
				legalMoves.add(new Point(x + 1, y));
			}
		}
	
		// UP
		if (y != 0) {
			if (!isWalled(x, y, UP)
					&& !isPawned(x, y - 1)) {
				legalMoves.add(new Point(x, y - 1));
			}
		}
	
		// DOWN
		if (y != 8) {
			if (!isWalled(x, y, DOWN)
					&& !isPawned(x, y + 1)) {
				legalMoves.add(new Point(x, y + 1));
			}
		}
	}
}

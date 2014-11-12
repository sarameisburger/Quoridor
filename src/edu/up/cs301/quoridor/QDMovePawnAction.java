package edu.up.cs301.quoridor;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * A game-move object that a tic-tac-toe player sends to the game to make
 * a move.
 * 
 * @author Steven R. Vegdahl
 * @version 2 July 2001
 */
public class QDMovePawnAction extends GameAction
{
	private static final long serialVersionUID = -2242980258970485343L;
	
	// instance variables: the selected row and column
    private int x;
    private int y;

    /**
     * Constructor for QDMoveAction
     *
     * @param source the player making the move
     * @param row the row of the square selected (0-2)
     * @param col the column of the square selected
     */
    public QDMovePawnAction(GamePlayer player, int xVal, int yVal)
    {
        // invoke superclass constructor to set the player
        super(player);

        // set the row and column as passed to us
        this.x = xVal; //= Math.max(0, Math.min(2, row));
        this.y = yVal;  //= Math.max(0, Math.min(2, col));
    }

    /**
     * get the object's row
     *
     * @return the row selected
     */
    public int getX() { return x; }

    /**
     * get the object's column
     *
     * @return the column selected
     */
    public int getY() { return y; }

}

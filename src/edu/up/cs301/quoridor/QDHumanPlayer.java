package edu.up.cs301.quoridor;

import edu.up.cs301.game.GameHumanPlayer;

/**
 * A human (i.e., GUI) version of a tic-tac-toe player.  This is an abstract
 * class and should be subclassed to "fill out" the behavior.
 * 
 * @author Steven R. Vegdahl 
 * @version 26 July 2002
 */

public abstract class QDHumanPlayer extends GameHumanPlayer
	implements QDPlayer
{
    /**
     * TTTHumanPlayer constructor.
     */
    public QDHumanPlayer(String name) {
        // invoke the superclass constructor
        super(name);
    }
}


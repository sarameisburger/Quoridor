package edu.up.cs301.quoridor;

import edu.up.cs301.game.GameComputerPlayer;

/**
 * A computerized (automated) tic-tac-toe player. This is an abstract class,
 * that may be subclassed to allow different strategies.
 * 
 * @author Steven R. Vegdahl
 * @version July 2013
 */

public abstract class QDComputerPlayer extends GameComputerPlayer
    implements QDPlayer {
    /**
     * instance variable that tells which piece am I playing ('X' or 'O').
     * This is set once the player finds out which player they are, in the
     * 'initAfterReady' method.
     */
    protected char piece;

    /**
     * Constructor for objects of class TTTComputerPlayer
     */
    public QDComputerPlayer(String name) {
        // invoke superclass constructor
        super(name);
    }// constructor
    
}// class TTTComputerPlayer

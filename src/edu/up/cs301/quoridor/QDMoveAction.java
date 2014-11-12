package edu.up.cs301.quoridor;

public abstract class QDMoveAction {

	/*
	 * QDMoveAction Constructor 
	 */
	public QDMoveAction(){
		
	}
	
	public boolean isPawnMove(){
		return false; 
	}
	
	public boolean isWallPlace(){
		return false; 
	}
}

//class QDMoveAction 
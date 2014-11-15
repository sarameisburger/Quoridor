package edu.up.cs301.quoridor;

public abstract class QDMoveAction {

	public QDMoveAction(){
		
	}
	
	public boolean isPawnMove(){
		return false; 
	}
	
	public boolean isWallPlace(){
		return false; 
	}
}

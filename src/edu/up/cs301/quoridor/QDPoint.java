package edu.up.cs301.quoridor;

import java.io.IOException;
import java.io.Serializable;

public class QDPoint implements Serializable {

	private static final long serialVersionUID = 8788070325049300980L;
	
	public int x;
	public int y;
	
	public QDPoint() {}

	public QDPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	  private void writeObject(java.io.ObjectOutputStream out)
	  throws IOException{
		  // note, here we don't need out.defaultWriteObject(); because
		  // QDPoint has no other state to serialize
		  out.writeInt(x);
		  out.writeInt(y);
	  }

	  private void readObject(java.io.ObjectInputStream in)
	  throws IOException {
		  // note, here we don't need in.defaultReadObject();
		  // because QDPoint has no other state to deserialize
		  x = in.readInt();
		  y = in.readInt();
	  }
}

package net.sourceforge.capcode.S57Library.objects;

import java.awt.Shape;


/**
 * A node referred to as a beginning and/or end node by one or more edge.
 * Connected nodes are defined only in the chain-node, planar graph and full
 * topology data structures.
 */
public class S57ConnectedNode extends S57Node{
	public Shape getPath(){
		return null;
	}
	
	public Shape getInversedPath(){
		return null;
	}

}

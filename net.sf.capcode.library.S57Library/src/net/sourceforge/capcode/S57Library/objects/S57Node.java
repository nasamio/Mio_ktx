package net.sourceforge.capcode.S57Library.objects;

import java.awt.Shape;

/**
 * A zero-dimensional spatial object, located by a coordinate pair. 
 * A node is either isolated or connected.
 */
public abstract class S57Node extends S57Spatial {
	public abstract Shape getPath(); 
	public abstract Shape getInversedPath();
}

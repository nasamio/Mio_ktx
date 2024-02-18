package net.sourceforge.capcode.S57Library.objects;

import java.awt.Shape;
import java.awt.geom.Path2D;

import net.sourceforge.capcode.S57Library.basics.S57Pos2D;

/**
 * An isolated zero-dimensional spatial object that represents the geometric
 * location of a point feature. An isolated node is never used as a beginning or end node.
 */
public class S57IsolatedNode extends S57Node{
	public Shape getPath(){
		if (path == null){
			path = new Path2D.Float();
			if (positions != null){
				S57Pos2D p = (S57Pos2D)positions.elementAt(0);
				path.moveTo(p.longitude, p.latitude);
				
				for (int i = 1; i < positions.size(); i++){
					p = (S57Pos2D)positions.elementAt(i);
					path.lineTo(p.longitude, p.latitude);
				}	
			}
		}
		return path;
	}
	
	public Shape getInversedPath(){
		if (inversedPath == null){
			inversedPath = new Path2D.Float();
			if (positions != null){
				S57Pos2D p = (S57Pos2D)positions.elementAt(positions.size()-1);
				inversedPath.moveTo(p.longitude, p.latitude);
				for (int i = positions.size()-2; i>= 0; i--){
					 p = (S57Pos2D)positions.elementAt(positions.size()-1);
					 inversedPath.lineTo(p.longitude, p.latitude);
				}				
			}
		}
		return inversedPath;
	}
}

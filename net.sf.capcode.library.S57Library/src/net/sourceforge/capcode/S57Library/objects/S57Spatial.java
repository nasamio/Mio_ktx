/**
 * 
 */
package net.sourceforge.capcode.S57Library.objects;

import java.awt.Shape;
import java.awt.geom.Path2D;
import net.sourceforge.capcode.S57Library.basics.PositionsVector;
import net.sourceforge.capcode.S57Library.basics.S57Pos2D;
import net.sourceforge.capcode.S57Library.basics.S57Pos3D;
import net.sourceforge.capcode.S57Library.fiedsRecords.S57FieldVRID;

/**
 * @author cyrille
 *
 */
public class S57Spatial extends S57Object {
	public PositionsVector positions;
	public boolean show = true;
	public S57Pos2D coordinate;
	public Path2D.Float path, inversedPath;

	public String toString(){
		return super.toString() 
		+ (positions != null && positions.size()>0 ? " " + positions.size() 
				+ " " + positions.elementAt(0).getClass().getSimpleName() : "");
		
	}
	
	public void addPositions(PositionsVector listOfPos) {
		if (positions == null){
			positions = listOfPos;
			if (positions.size()>0){
				this.coordinate = (S57Pos2D) positions.elementAt(0);
			}
		}else{
			if (listOfPos != null){
				positions.addAll(listOfPos);				
			}
		}
	}
	
	public Shape getPath(){
		//do nothing, but avoid abstract class
		return null;
	}

	public Shape getInversedPath(){
		//do nothing, but avoid abstract class		
		return null;
	}

	public boolean hasPositions() {
		return positions != null && positions.size()>0;
	}

	public boolean isClosed(){
		return false;
	}

	public boolean hasDepths() {
		return positions != null && positions.size()>0 && positions.elementAt(0) instanceof S57Pos3D;
	}

	public static S57Object buildFromVRID(S57FieldVRID vrid) {
		S57Object res = null;
		if (vrid.recordType != null){
			switch (vrid.recordType){
			case vectorIsolatedNode:
				res = new S57IsolatedNode();
				break;
			case vectorConnectedNode:
				res =  new S57ConnectedNode();
				break;
			case vectorEdge:
				res = new S57Edge();
				break;
			default:
				res = new S57Spatial();
			}
		}else{
			res = new S57Spatial();			
		}
		res.name = (int) vrid.name;
		return res;
	}
	
}

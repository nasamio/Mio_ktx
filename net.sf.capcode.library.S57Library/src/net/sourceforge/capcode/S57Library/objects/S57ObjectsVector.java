/**
 * 
 */
package net.sourceforge.capcode.S57Library.objects;

import java.util.Vector;

/**
 * @author cyrille
 *
 */
@SuppressWarnings("serial")
public class S57ObjectsVector extends Vector<S57Object>{

	public S57ObjectsVector(){
		super(100);
	}

	public S57ObjectsVector(int initialSize) {
		super(initialSize);
	}

	
	public boolean add(S57Object o){
		return super.add(o);
	}
	
	public S57Object searchByCode(long longName) {
		for (S57Object o : this){
			if (o.name == longName){
				return o;
			}
		}
		return null;
	}
	
	public S57Object searchByLongIdentifier(long wwi) {
		for (S57Object o : this){
			if (o instanceof S57Feature){
				S57Feature f = (S57Feature) o;
				if (f.worldWideIdentifier == wwi){
					return f;
				}
			}
		}
		return null;
	}
	
	public String toString(){
		String res = "\n[";
		int bcl = 0;
		for (S57Object o : this){			
			bcl++;
			res += o.toStringShort() + (bcl < this.size()? ";" :"]");
		}
		return res;
	}

	public int getNumberOfPositions() {
		for (S57Object o : this){
			if (o instanceof S57Spatial){
				S57Spatial f = (S57Spatial) o;
				return f.positions.size();
			}
		}
	return 0;
	}
}

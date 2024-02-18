/**
 * 
 */
package net.sourceforge.capcode.S57Library.basics;

import java.util.Vector;

/**
 * @author cyrille
 *
 */
@SuppressWarnings("serial")
public class PositionsVector extends Vector<S57Pos2D> {
	
	public boolean add(S57Pos2D pos){
		return super.add(pos);
	}
	
	private static final int MAX_PRINT = 20;
	
	public String toString(){
		String res = "";
		int nbPrint = 0;
		for (S57Pos2D pos : this){
			res += pos.toString() + ";";
			if (nbPrint >= MAX_PRINT){
				res += ("...(" + this.size()+")");			
			}			
		}
		return res;
	}
	
	public void addAll(PositionsVector otherVector){
		super.addAll(otherVector);
	}
}

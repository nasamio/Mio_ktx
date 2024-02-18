/**
 * 
 */
package net.sourceforge.capcode.S57Library.fiedsRecords;

import java.util.Vector;

/**
 * @author cyrille
 * 
 */
@SuppressWarnings("serial")
public class SubFieldDefinitionTable extends Vector<S57SubFieldDefinition> {

	public SubFieldDefinitionTable(int size) {
		super(size);
	}

	public S57SubFieldDefinition get(String key) {
		for (S57SubFieldDefinition d : this) {
			if (d.name.equals(key)) {
				return d;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		String res = "";
		int i = 0;
		for (S57SubFieldDefinition d : this) {
			i++;
			res = res + d.name + (i < elementCount ? ";" : "");
		}
		return res;
	}

}
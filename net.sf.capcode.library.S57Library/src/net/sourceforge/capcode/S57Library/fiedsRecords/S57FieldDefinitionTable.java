/**
 * 
 */
package net.sourceforge.capcode.S57Library.fiedsRecords;

import java.util.Enumeration;

import net.sourceforge.capcode.S57Library.fiedsRecords.descriptive.S57DDRDataDescriptiveField;

/**
 * @author cyrille
 *
 */
@SuppressWarnings("serial")
public class S57FieldDefinitionTable extends java.util.Hashtable<String, S57DDRDataDescriptiveField> {
	
	public S57FieldDefinitionTable(int size) {
		super(size);
	}

	public S57DDRDataDescriptiveField get(String key){
		return super.get(key);
	}

	public S57DDRDataDescriptiveField put(String key, S57DDRDataDescriptiveField data){
		return super.put(key, data);
	}

	public String toString(){
		String res = "";
		Enumeration<S57DDRDataDescriptiveField> elements = elements();
		while(elements.hasMoreElements()){
			S57DDRDataDescriptiveField e = elements.nextElement();
			res = res + ";" + e.getTag();
		}
		return res;
	}
}

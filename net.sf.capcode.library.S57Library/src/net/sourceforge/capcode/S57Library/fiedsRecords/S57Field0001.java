/**
 * 
 */
package net.sourceforge.capcode.S57Library.fiedsRecords;

import net.sourceforge.capcode.S57Library.basics.S57FieldAnnotation;
import net.sourceforge.capcode.S57Library.fiedsRecords.descriptive.S57DDRDataDescriptiveField;




/**
 * @author cyrille
 *
 */
public class S57Field0001 extends S57DataField{
	@S57FieldAnnotation(name="0001")
	public int id = 0;
	
	public S57Field0001(String tag, byte[] fieldData,
			S57DDRDataDescriptiveField fieldDefinition) {
		super(tag, fieldData, fieldDefinition);
		decode();
	}
	public String toString(){
		return "------ record number :"+ id;
	}
}

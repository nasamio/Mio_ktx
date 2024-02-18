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
public class S57FieldVRID extends S57IdentityField {
	@S57FieldAnnotation(name="RVER")
	public int recordVersion;
	@S57FieldAnnotation(name="RUIN")
	public int recordUpdateInstruction;
	public S57FieldVRID(String tag, byte[] fieldData,
			S57DDRDataDescriptiveField fieldDefinition) throws Exception {
		super(tag, fieldData, fieldDefinition);
		decode();	
	}
	
}

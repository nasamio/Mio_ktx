/**
 * 
 */
package net.sourceforge.capcode.S57Library.fiedsRecords;

import net.sourceforge.capcode.S57Library.fiedsRecords.descriptive.S57DDRDataDescriptiveField;

/**
 * Field Tag: ATTF Field Name: Feature record attribute
 * implemented in S57AttributeField
 */
public class S57FieldATTF extends S57AttributeField {
	public S57FieldATTF(String tag, byte[] fieldData,
			S57DDRDataDescriptiveField fieldDefinition) throws Exception {
		super(tag, fieldData, fieldDefinition);
	}

}

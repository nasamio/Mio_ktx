/**
 * 
 */
package net.sourceforge.capcode.S57Library.fiedsRecords;

import net.sourceforge.capcode.S57Library.fiedsRecords.descriptive.S57DDRDataDescriptiveField;

/**
 * NATF : Feature record national attributew<br>
 * implemented in S57AttributeField
 */
public class S57FieldNATF extends S57AttributeField {
	public S57FieldNATF(String tag, byte[] fieldData,
			S57DDRDataDescriptiveField fieldDefinition) throws Exception {
		super(tag, fieldData, fieldDefinition);
	}
}

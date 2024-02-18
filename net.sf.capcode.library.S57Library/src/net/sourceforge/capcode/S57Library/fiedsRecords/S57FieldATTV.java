/**
 * 
 */
package net.sourceforge.capcode.S57Library.fiedsRecords;

import net.sourceforge.capcode.S57Library.fiedsRecords.descriptive.S57DDRDataDescriptiveField;

/**
 * ATTV : Vector Record S57Attribute.<br>
 * implemented in S57AttributeField
*/
public class S57FieldATTV extends S57AttributeField {
	public S57FieldATTV(String tag, byte[] fieldData,
			S57DDRDataDescriptiveField fieldDefinition) throws Exception {
		super(tag, fieldData, fieldDefinition);
	}
}

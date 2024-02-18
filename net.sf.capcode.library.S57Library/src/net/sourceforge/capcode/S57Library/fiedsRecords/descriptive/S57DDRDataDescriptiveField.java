/**
 * 
 */
package net.sourceforge.capcode.S57Library.fiedsRecords.descriptive;

import net.sourceforge.capcode.S57Library.fiedsRecords.S57SubFieldDefinition;
import net.sourceforge.capcode.S57Library.fiedsRecords.SubFieldDefinitionTable;
import net.sourceforge.capcode.S57Library.files.S57ByteBuffer;
import net.sourceforge.capcode.S57Library.records.S57LogicalRecord;

/**
 * @author cyrille
 * 
 */
public class S57DDRDataDescriptiveField extends S57DDRField {

	private final SubFieldDefinitionTable subFieldDefinitions;
	public String[] formats;
	public String[] subFields;

	/**
	 * @param index
	 * @param buffer
	 * @param record
	 * @throws Exception
	 */
	public S57DDRDataDescriptiveField(int index, S57ByteBuffer buffer,
			S57LogicalRecord record) throws Exception {
		super(index, buffer, record);
		buffer.getFieldAsString(TERMINATORS);
		subFieldDefinitions = new SubFieldDefinitionTable(2);
	}

	public void extractSubFields() {
		subFields = super.getSubFields("!");
		formats = super.getFormats();
		for (int i = 0; i < subFields.length; i++) {
			String tag1 = subFields[i];
			if (tag1.isEmpty()) {
				tag1 = this.tag;
			}
			S57SubFieldDefinition fieldDefinition = new S57SubFieldDefinition(
					tag1, formats[i]);
			subFieldDefinitions.add(fieldDefinition);
		}
	}

	/**
	 * @return the subFieldDefinitions
	 */
	public SubFieldDefinitionTable getSubFieldDefinitions() {
		return subFieldDefinitions;
	}
}

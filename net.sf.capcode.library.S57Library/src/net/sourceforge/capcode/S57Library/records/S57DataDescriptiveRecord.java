/**
 * 
 */
package net.sourceforge.capcode.S57Library.records;

import net.sourceforge.capcode.S57Library.fiedsRecords.S57FieldDefinitionTable;
import net.sourceforge.capcode.S57Library.fiedsRecords.descriptive.S57DDRDataDescriptiveField;
import net.sourceforge.capcode.S57Library.fiedsRecords.descriptive.S57DDRField;
import net.sourceforge.capcode.S57Library.fiedsRecords.descriptive.S57DataDescriptiveFieldFactory;


/**
 * @author cyrille
 *
 */
public class S57DataDescriptiveRecord extends S57LogicalRecord{

	private S57FieldDefinitionTable fieldDefinitions;

	public S57DataDescriptiveRecord(byte[] array, S57FieldDefinitionTable fieldDefinitions) throws Exception {
		super(array);
		this.fieldDefinitions = fieldDefinitions;
		buildDirectory();
	}

	@Override
	protected boolean checkValidity() {
		return true;
//		return interchangeLevel == 3 && identifier == 'L'
//			&& codeExtensionIndicator == 'E' && versionNumber == 1;
	}

	@Override
	public String toString(){
		return "DDR-" + super.toString();
	}

	public void buildDirectory() throws Exception {
		// read the DDR directory 
		int width = this.getFieldEntryWidth();
		int index = HEADER_RECORD_SIZE;
		int bclRec = 0;
		while (array[index] != S57DDRField.FIELD_TERMINATOR){
			index += width;
			S57DDRField df = S57DataDescriptiveFieldFactory.build(bclRec, array, this);
			if (df != null){
				if (bclRec > 0){
					addDirectoryEntry((S57DDRDataDescriptiveField) df);
				}
//				else{
//					TODAY UNUSED
//					S57DDRField0000 fcf = (S57DDRField0000)df;
//				}
			}
			bclRec++;
		}
	}
	
	protected void addDirectoryEntry(S57DDRDataDescriptiveField ddf) {
		String tag = ddf.getTag();
		fieldDefinitions.put(tag, ddf);
		ddf.extractSubFields();
	}
	
}

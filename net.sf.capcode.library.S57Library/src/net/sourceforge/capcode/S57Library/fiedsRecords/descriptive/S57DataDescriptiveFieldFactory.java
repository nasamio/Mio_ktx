/**
 * 
 */
package net.sourceforge.capcode.S57Library.fiedsRecords.descriptive;

import net.sourceforge.capcode.S57Library.files.S57ByteBuffer;
import net.sourceforge.capcode.S57Library.records.S57LogicalRecord;


/**
 * @author cyrille
 *
 */
public class S57DataDescriptiveFieldFactory {
 	
	public static S57DDRField build(int index, byte[] array, S57LogicalRecord record) throws Exception{
		int fieldEntryWidth = record.getFieldEntryWidth();
		int offset = record.getHeaderSize() + index * fieldEntryWidth;
		String tag = S57ByteBuffer.getString(array, offset, 4);
		if (tag.equals("0000")){
			return new S57DDRField0000(index, new S57ByteBuffer(array), record);	
		}
		return new S57DDRDataDescriptiveField(index, new S57ByteBuffer(array), record);
	}
	

}

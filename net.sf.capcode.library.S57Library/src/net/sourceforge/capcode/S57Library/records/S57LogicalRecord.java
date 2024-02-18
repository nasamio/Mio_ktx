/**
 * 
 */
package net.sourceforge.capcode.S57Library.records;

import java.util.Vector;

import net.sourceforge.capcode.S57Library.fiedsRecords.descriptive.S57DDRField;
import net.sourceforge.capcode.S57Library.fiedsRecords.descriptive.S57DataDescriptiveFieldFactory;
import net.sourceforge.capcode.S57Library.files.S57ByteBuffer;

/**
 * @author cyrille
 *
 */
public abstract class S57LogicalRecord {
	public static final int HEADER_RECORD_SIZE = 24;
	protected byte[] header = new byte[HEADER_RECORD_SIZE];


	protected boolean valid = true;
	protected int recordLength;
	protected int fieldControlLength = 9;
	protected int fieldAreaBaseAddress;
	protected String extendedCharSetIndicator;
	protected S57DataRecordEntryMap entryMap;
	protected byte[] array;
	protected Vector<S57DDRField> directory;	
	
	public S57LogicalRecord(){
		super();
	}

	public S57LogicalRecord(byte[] array) {
		this();
		this.array = array;
		recordLength  = S57ByteBuffer.getInteger(array, 0, 5);
		fieldAreaBaseAddress = S57ByteBuffer.getInteger(array, 12, 5);
		extendedCharSetIndicator = S57ByteBuffer.getString(array, 17, 3);
		entryMap = new S57DataRecordEntryMap(array, 20);
	}

	public void buildDirectory() throws Exception {
		directory = new Vector<S57DDRField>();
		// read the DR directory 
		int width = this.getFieldEntryWidth();
		int index = HEADER_RECORD_SIZE;
		int bclRec = 0;
		while (array[index] != S57DDRField.FIELD_TERMINATOR){
			index += width;
			S57DDRField df = S57DataDescriptiveFieldFactory.build(bclRec, array, this);
			if (df != null){
				directory.add(df);
			}
			bclRec++;
		}
	}
	
	public boolean isTagPresent(String tag){
		int index = HEADER_RECORD_SIZE;
		int width = this.getFieldEntryWidth();
		while (array[index] != S57DDRField.FIELD_TERMINATOR){
			String s = S57ByteBuffer.getString(array, index, 4);
			index += width;
			if (s.equals(tag)){
				return true;
			}
		}
		return false;
	}
	
	public S57DDRField getDirectoryEntry(String tag) throws Exception{
		if (directory == null){
			buildDirectory();
		}
		for (S57DDRField f : directory){
			if (f.getTag().equals(tag)){
				return f;
			}
		}
		return null;
	}

	protected abstract boolean checkValidity();

	public boolean isValid() {
		return valid;
	}

	/**
	 * @return the recordLength
	 */
	public int getRecordLength() {
		return recordLength;
	}

	/**
	 * @return the fieldControlLength
	 */
	public int getFieldControlLength() {
		return fieldControlLength;
	}


	/**
	 * @return the fieldAreaBaseAddress
	 */
	public int getFieldAreaStart() {
		return fieldAreaBaseAddress;
	}


	/**
	 * @return the extendedCharSetIndicator
	 */
	public String getExtendedCharSet() {
		return extendedCharSetIndicator;
	}


	/**
	 * @return the size of the field size.
	 * The field size is coded in the directory of every LogicalRecord
	 * this is used to decode the directory fields (tag, length, offset) 
	 */
	public int getFieldLengthSize() {
		return entryMap != null ? entryMap.fieldLengthSize : 0;
	}


	/**
	 * @return the size of the field offset.
	 * The field size is coded in the directory of every LogicalRecord
	 * this is used to decode the directory fields (tag, length, offset) 
	 */
	public int getFieldOffsetSize() {
		return entryMap != null ? entryMap.fieldPosSize : 0;
	}


	/**
	 * @return the size of the field tag (name).
	 * The field size is coded in the directory of every LogicalRecord
	 * this is used to decode the directory fields (tag, length, offset) 
	 */
	public int getFieldTagSize() {
		return entryMap != null ? entryMap.fieldTagSize : 0;
	}


	public int getFieldEntryWidth() {
		return entryMap != null ? entryMap.getWidth() : 0;
	}


	public String toString(){
		return String.format("length %d, map: %s", recordLength, entryMap.toString());
	}

	public int getHeaderSize() {
		return HEADER_RECORD_SIZE;
	}

}

/**
 * 
 */
package net.sourceforge.capcode.S57Library.records;

import net.sourceforge.capcode.S57Library.files.S57ByteBuffer;

/**
 * the structure of the Entry map as define by IHO
 * 20 Size of field length field 1 Variable 1-9 (defined by encoder)
 * 21 Size of field position field 1 Variable 1-9 (defined by encoder)
 * 22 Reserved 1 "0"
 * 23 Size of field tag field 1 "4"
 */
public class S57DataRecordEntryMap {
	int fieldLengthSize;
	int fieldPosSize;
	int fieldTagSize;
	/**
	 * read at the given position in the buffer the entry map
	 * @param buffer
	 */
	public S57DataRecordEntryMap(S57ByteBuffer buffer, int pos){
		fieldLengthSize = buffer.decodeInteger(pos, 1);
		fieldPosSize = buffer.decodeInteger(1);
		buffer.decodeInteger(1);
		fieldTagSize = buffer.decodeInteger(1);
	}
	public S57DataRecordEntryMap(byte[] array, int pos) {
		fieldLengthSize = Character.digit((char)array[pos], 10);
		fieldPosSize = Character.digit((char)array[pos+1], 10);
		fieldTagSize = Character.digit((char)array[pos+3], 10);
	}
	public int getWidth() {
		return fieldLengthSize + fieldPosSize + fieldTagSize;
	}
	public String toString(){
		return String.format("length %d pos %d tag %d", fieldLengthSize, fieldPosSize, fieldTagSize);
	}
}

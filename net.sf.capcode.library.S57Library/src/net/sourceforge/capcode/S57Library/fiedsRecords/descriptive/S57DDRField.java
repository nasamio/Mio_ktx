/**
 * 
 */
package net.sourceforge.capcode.S57Library.fiedsRecords.descriptive;

import net.sourceforge.capcode.S57Library.basics.E_DataStructureCode;
import net.sourceforge.capcode.S57Library.basics.E_DataTypeCode;
import net.sourceforge.capcode.S57Library.files.S57ByteBuffer;
import net.sourceforge.capcode.S57Library.records.S57LogicalRecord;

/**
 * @author cyrille
 *
 */
public abstract class S57DDRField extends S57GenericField{
	protected E_DataStructureCode dataStructure;
	protected E_DataTypeCode dataType;
	protected int fieldAreaStart;
	protected S57ByteBuffer buffer;
	
	public S57DDRField(int index, S57ByteBuffer buffer, S57LogicalRecord record) throws Exception{
		super();
		this.buffer = buffer;
		int fieldEntryWidth = record.getFieldEntryWidth();
		int offset = record.getHeaderSize() + index * fieldEntryWidth;
		tag = buffer.getString(offset, record.getFieldTagSize());
		fieldLength = buffer.decodeInteger(record.getFieldLengthSize());
		fieldPos = buffer.decodeInteger(record.getFieldOffsetSize());
		fieldAreaStart = record.getFieldAreaStart();
		//position the buffer to the field area
		buffer.gotoPosition(fieldAreaStart + fieldPos);
		//read and decode the dataStructure
		int c = buffer.decodeInteger(1);
		dataStructure = DecodeDataStructureCode(c);
		//read and decode the dataType
	    c = buffer.decodeInteger(1);
	    dataType = decodeDataTypeCode(c);
	}
	
	protected E_DataTypeCode decodeDataTypeCode(int c) {
	    switch(c){
	      case 0: return E_DataTypeCode.char_string;
	      case 1: return E_DataTypeCode.implicit_point;
	      case 2: return E_DataTypeCode.explicit_point;
	      case 3: return E_DataTypeCode.explicit_point_scaled;
	      case 4: return E_DataTypeCode.char_bit_string;
	      case 5: return E_DataTypeCode.bit_string;
	      case 6: return E_DataTypeCode.mixed_data_type;
	      default:
	    	  return E_DataTypeCode.char_string;
	    }
	}

	protected E_DataStructureCode DecodeDataStructureCode(int c) {
	    switch(c){
	      case 0: return E_DataStructureCode.elementary;
	      case 1: return E_DataStructureCode.vector;
	      case 2: return E_DataStructureCode.array;
	      case 3: return E_DataStructureCode.concatenated;
	      default:
	    	  return E_DataStructureCode.elementary;
	    }
	}

	public String toString(){
		return String.format("tag:%s(%s,%s)", 
				tag, dataStructure.name(), dataType.name());
	}

	/**
	 * @return the fieldLength
	 */
	public int getFieldLength() {
		return fieldLength;
	}

	/**
	 * @return the dataStructure
	 */
	public E_DataStructureCode getDataStructure() {
		return dataStructure;
	}

	/**
	 * @return the dataType
	 */
	public E_DataTypeCode getDataType() {
		return dataType;
	}

	/**
	 * @return the fieldAreaStart
	 */
	public int getFieldAreaStart() {
		return fieldAreaStart;
	}

	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * @return the fieldPos
	 */
	public int getFieldPos() {
		return fieldPos;
	}

	protected String[] getSubFields(String fieldSeparator) {
		return buffer.getFieldAsString(TERMINATORS).split(fieldSeparator);
	}

	public String[]  getFormats() {
		String formatString = buffer.getFieldAsString(TERMINATORS);
		formatString = formatString.substring(1, formatString.length()-1);
		String finalFormatString = "";
		String[] compressedFormats = formatString.split(",");
		//decompress some formats
		for (String format : compressedFormats){
			int i = 0;
			String strNum = "";
			while(format.charAt(i) >= '0' && format.charAt(i)<='9'){
				strNum += format.charAt(i);
				i++;				
			}
			if (!strNum.isEmpty()){
				format = format.replace(strNum, "");
				int repeatFactor = Integer.parseInt(strNum);
				if (repeatFactor > 1){
					for (i=0; i<repeatFactor;i++){
						finalFormatString += format + ",";
					}
				}				
			}else{
				finalFormatString += format + ",";				
			}
		}
		if(finalFormatString.charAt(finalFormatString.length()-1) == ','){
			finalFormatString = finalFormatString.substring(0, finalFormatString.length()-1); 
		}
		return finalFormatString.split(",");
	}

	public boolean equals(Object o){
		return this.tag.equals(o);
	}
}

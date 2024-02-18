/**
 * 
 */
package net.sourceforge.capcode.S57Library.fiedsRecords;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Enumeration;

import net.sourceforge.capcode.S57Library.basics.S57FieldAnnotation;
import net.sourceforge.capcode.S57Library.fiedsRecords.descriptive.S57DDRDataDescriptiveField;
import net.sourceforge.capcode.S57Library.fiedsRecords.descriptive.S57GenericField;

/**
 * @author cyrille
 *
 */
public class S57DataField {
	protected byte[] data;
	protected String tag;
	protected S57DDRDataDescriptiveField fieldDefinition;
	protected int bufferIndex;

	protected S57DataField() {
		super();
		this.data = null;
		this.tag = "";
		fieldDefinition = null;
		bufferIndex = 0;
	}

	public S57DataField(String tag, byte[] data, S57DDRDataDescriptiveField fieldDefinition) {
		this();
		this.data = data;
		this.tag = tag;
		this.fieldDefinition = fieldDefinition;
		bufferIndex = 0;
	}

	/**
	 * callback to be overiden by children to implement post process.
	 * this call back is called by decode()
	 */
	protected void updateField(){
		//do nothing, must be overriden if needed
	}
	
	public void decode(){
		Enumeration<S57SubFieldDefinition> sfDefs;
		Field[] fields = getClass().getFields();
		while (bufferIndex < data.length -1){
			sfDefs = fieldDefinition.getSubFieldDefinitions().elements();
			while (sfDefs.hasMoreElements()){
				S57SubFieldDefinition subFieldDef = sfDefs.nextElement();
				//read always the field to have the correct buffer bufferIndex
				byte[] fieldAsBytes = getNextFieldAsBytes(subFieldDef.format);
				for (Field f:fields){
					S57FieldAnnotation annotation = f.getAnnotation(S57FieldAnnotation.class);
					
					//try to set the field (find one with the correct attribute name
					if (annotation != null && annotation.name().equals(subFieldDef.name)){
						try {
							Object value = getSubFieldValue(fieldAsBytes, subFieldDef.format);
							if (value != null){
								if (annotation.setter().isEmpty()){
									//no setter function, set directly the meaning
									f.set(this, value);							
								}else{
									//there is a setter declared, try to use it
									Method m = this.getClass().getMethod(annotation.setter(), Integer.class);
									m.invoke(this, value);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					}
				}		
			}
			updateField();
		}
	}

//	private boolean isEndOfField() {
//		//TODO: IF IS CHARSET 0 or 1 THEN
//		return bufferIndex >= data.length-1;
////		return data[bufferIndex] == S57GenericField.FIELD_TERMINATOR || bufferIndex >= data.length || data[bufferIndex+1] == S57GenericField.FIELD_TERMINATOR;
//		//TODO: ELSE  bufferIndex >= data.length OR (data[bufferIndex] == 0 AND data[bufferIndex+1] == S57GenericField.FIELD_TERMINATOR))
//	}

	protected byte[] getNextFieldAsBytes(S57SubFieldFormat format){
		int lg = 0;
		//check if it is a fixed size (-1 for unspecified size)
		if (format.length != -1){
			lg = format.length <= data.length ? format.length : data.length;
		}else{
			lg = countUntilNextTerminator(bufferIndex);
		}

		byte[] fieldAsBytes = new byte[lg];
		System.arraycopy(data, bufferIndex, fieldAsBytes, 0, lg);			
		if (format.length != -1){
			bufferIndex += lg;
		}else{
			bufferIndex += lg + 1;
		}
		return fieldAsBytes;
	}

	private int countUntilNextTerminator(int index) {
		int res = 0;
		while (index + res < data.length 
				&& data[index + res] != S57GenericField.UNIT_TERMINATOR
				&& data[index + res] != S57GenericField.FIELD_TERMINATOR){
			res++;
		}
		return res;
	}


	public Object getSubFieldValue(byte[] fieldInBytes, S57SubFieldFormat format){
		if (fieldInBytes == null || fieldInBytes.length==0){
			return null;
		}
		switch (format.type){
		case ascii:
			return new String(fieldInBytes);
		case integer:
			return Integer.parseInt(new String(fieldInBytes));
		case real: {
			return Double.parseDouble(new String(fieldInBytes));
		}
		default:
			BigInteger bi = new BigInteger(reverseArray(fieldInBytes));
			switch (format.type){				
			case binarySignedInteger:
				return new Integer(bi.intValue());
			case binaryUnsignedInteger:
				if (format.length == 1){
					return (0x000000FF & ((int)fieldInBytes[0]));
				}else if (format.length == 2){
			         int b1 = (0x000000FF & ((int)fieldInBytes[1]));
			         int b2 = (0x000000FF & ((int)fieldInBytes[0]));
			         return (int) (b1 << 8 | b2);					
				}else if (format.length == 4){
			         int b1 = (0x000000FF & ((int)fieldInBytes[3]));
			         int b2 = (0x000000FF & ((int)fieldInBytes[2]));
			         int b3 = (0x000000FF & ((int)fieldInBytes[1]));
			         int b4 = (0x000000FF & ((int)fieldInBytes[0]));
				  	 return (int) ((b1 << 24 | b2 << 16 | b3 << 8 | b4)& 0xFFFFFFFFL);				
				}else if (format.length == 5){
			         int b1 = (0x000000FF & ((int)fieldInBytes[4]));
			         int b2 = (0x000000FF & ((int)fieldInBytes[3]));
			         int b3 = (0x000000FF & ((int)fieldInBytes[2]));
			         int b4 = (0x000000FF & ((int)fieldInBytes[1]));
			         int b5 = (0x000000FF & ((int)fieldInBytes[0]));
				  	return (b1 << 32 | b2 << 24 | b3 << 16 | b4 << 8 | b5)& 0xFFFFFFFFL;
				}else if (format.length == 8){
			         int b1 = (0x000000FF & ((int)fieldInBytes[7]));
			         int b2 = (0x000000FF & ((int)fieldInBytes[6]));
			         int b3 = (0x000000FF & ((int)fieldInBytes[5]));
			         int b4 = (0x000000FF & ((int)fieldInBytes[4]));
			         int b5 = (0x000000FF & ((int)fieldInBytes[3]));
			         int b6 = (0x000000FF & ((int)fieldInBytes[2]));
			         int b7 = (0x000000FF & ((int)fieldInBytes[1]));
			         int b8 = (0x000000FF & ((int)fieldInBytes[0]));
				  	  return (b1 << 56 | b2 << 48 | b3 << 40 | b4 << 32 | b5 << 24 | b6 << 16 | b7 << 8 | b8)& 0xFFFFFFFFL;				
				}else{
					return new Integer(bi.intValue());
				}
			case binaryFixedPoint:
				return new Double(Double.longBitsToDouble(bi.longValue()));
			case binaryFloatingPoint:
				return new Double(Double.longBitsToDouble(bi.longValue()));
			default:
			}			
		}
		return null;
	}
	
	protected byte[] reverseArray(byte[] byteArray) {
        byte[] reversed = new byte[byteArray.length];
        for (int i = 0; i < byteArray.length; i++ ){
        	reversed[byteArray.length-i-1] = byteArray[i];
        }
		return reversed;
	}
	

	public String toString(boolean detailled){
		if (detailled){
			return toString() + "\n"
			+ fieldDefinition.subFields + "\n" 
			+ fieldDefinition.formats + "\n" 
			+ fieldDefinition.getSubFieldDefinitions() + "\n";
		}
		else{
			return toString();
		}
	}
	public String toString(){
		String res = tag + "=";
		Field[] fields = getClass().getFields();
		for (Field f:fields){
			if (f.isAnnotationPresent(S57FieldAnnotation.class)){
				S57FieldAnnotation annotation = f.getAnnotation(S57FieldAnnotation.class);
				try {
					res += annotation.name() + ":" + f.get(this);
				} catch (Exception e) {
					res += annotation.name() + "**error**";
				}
				res += ";";
			}
		}
		return res;
	}

}

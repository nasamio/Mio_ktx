/**
 * 
 */
package net.sourceforge.capcode.S57Library.fiedsRecords;

import net.sourceforge.capcode.S57Library.basics.S57FieldAnnotation;
import net.sourceforge.capcode.S57Library.fiedsRecords.descriptive.S57DDRDataDescriptiveField;

/**
 * @author cyrille
 *
 */
public class S57FieldCATD extends S57DataField {
	@S57FieldAnnotation(name="FILE")
	public String fileName;
	@S57FieldAnnotation(name="LFIL")
	public String longFileName;
	@S57FieldAnnotation(name="VOLM")
	public String volume;
	@S57FieldAnnotation(name="SLAT")
	public double southLat;
	@S57FieldAnnotation(name="WLON")
	public double westLon;
	@S57FieldAnnotation(name="NLAT")
	public double northLat;
	@S57FieldAnnotation(name="ELON")
	public double eastLon;

	/**
	 * 
	 */
	public S57FieldCATD() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param tag
	 * @param data
	 * @param fieldDefinition
	 */
	public S57FieldCATD(String tag, byte[] data, S57DDRDataDescriptiveField fieldDefinition) {
		super(tag, data, fieldDefinition);
		decode();
	}

	public String toString(){
		return String.format("%s-%s (%s) %f/%f;%f/%f", fileName, longFileName, volume, northLat, westLon, southLat, eastLon);
	}
}

/**
 * 
 */
package net.sourceforge.capcode.S57Library.fiedsRecords;

import net.sourceforge.capcode.S57Library.basics.S57FieldAnnotation;
import net.sourceforge.capcode.S57Library.fiedsRecords.descriptive.S57DDRDataDescriptiveField;

/**
 * Field Tag: FSPC [Field Name: Feature Record to Spatial Record Pointer Control
 * Feature to spatial record pointer update instruction FSUI A(1) b11 
 * 	"I" {1} Insert
 * 	"D" {2} Delete
 * 	"M" {3} Modify
 * 	(see 8.4.2.4)
 * Feature to spatial record pointer index FSIX I( ) b12 int 
 * Index (position) of the adressed record pointer within the FSPT field(s) of the target record
 * (see 8.4.2.4)
 * Number of feature to spatial record pointers NSPT I( ) b12 int 
 * Number of record pointers in the FSPT field(s) of the update record (see 8.4.2.4) 
 */
public class S57FieldFSPC extends S57DataField {
	@S57FieldAnnotation(name="FSUI")
	public int fsui;
	@S57FieldAnnotation(name="FSIX")
	public int fsix;
	@S57FieldAnnotation(name="NSPT")
	public int nspt;
	public S57FieldFSPC(String tag, byte[] fieldData,
			S57DDRDataDescriptiveField fieldDefinition) throws Exception {
		super(tag, fieldData, fieldDefinition);
		decode();	
	}
	
	@Override
	protected void updateField(){
		
	}

}

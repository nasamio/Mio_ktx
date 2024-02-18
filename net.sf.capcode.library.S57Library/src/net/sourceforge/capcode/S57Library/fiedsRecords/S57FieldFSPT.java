/**
 *
 */
package net.sourceforge.capcode.S57Library.fiedsRecords;

import java.util.Enumeration;
import java.util.Vector;

import net.sourceforge.capcode.S57Library.basics.Link;
import net.sourceforge.capcode.S57Library.fiedsRecords.descriptive.S57DDRDataDescriptiveField;

/**
 * Field Tag: FSPT Field Name: Feature Record to Spatial Record Pointer Subfield.<br>
 * name Label Format
 * Name *NAME A(12) B(40) an Foreign pointer (see 2.2)<br>
 * Orientation ORNT A(1) b11 "F" {1} Forward "R" {2} Reverse "N" {255} NULL<br>
 * Usage indicator USAG A(1) b11 "E" {1} Exterior "I" {2} Interior "C" {3}<br>
 * Exterior boundary truncated by the data limit "N" {255} NULL
 * Masking indicator MASK A(1) b11 "M" {1} Mask "S" {2} Show "N" {255} NULL<br>
 */
public class S57FieldFSPT extends S57DataField {
	public Vector<Link> links;

	public S57FieldFSPT(String tag, byte[] fieldData,
			S57DDRDataDescriptiveField fieldDefinition) throws Exception {
		super(tag, fieldData, fieldDefinition);
		links = new Vector<Link>();
		decode();
	}

	@Override
	public void decode(){
		Enumeration<S57SubFieldDefinition> sfDefs;
		while (bufferIndex < data.length -1){
			sfDefs = fieldDefinition.getSubFieldDefinitions().elements();
			long name = 0;
			int ornt = 0;
			int usag = 0;
			int mask = 0;
			while (sfDefs.hasMoreElements()){
				S57SubFieldDefinition subFieldDef = sfDefs.nextElement();
				//read always the field to have the correct buffer bufferIndex
				byte[] fieldAsBytes = getNextFieldAsBytes(subFieldDef.format);
				Object value = getSubFieldValue(fieldAsBytes, subFieldDef.format);
				if (subFieldDef.name.equals("*NAME")){
					name = (Long) value;
				}else if (subFieldDef.name.equals("ORNT")){
					ornt = (Integer) value;
				}else if (subFieldDef.name.equals("USAG")){
					usag = (Integer) value;
				}else if (subFieldDef.name.equals("MASK")){
					mask = (Integer) value;
				}
			}
			Link link = new Link();
			link.name = name;
			link.ornt = ornt;
			link.usag = usag;
			link.mask = mask;
			links.add(link);
		}
	}


	@Override
	public String toString(){
		return super.toString() + links;
	}

}

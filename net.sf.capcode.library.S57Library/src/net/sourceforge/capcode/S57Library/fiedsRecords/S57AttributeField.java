/**
 * 
 */
package net.sourceforge.capcode.S57Library.fiedsRecords;

import net.sourceforge.capcode.S57Library.basics.S57FieldAnnotation;
import net.sourceforge.capcode.S57Library.catalogs.AttributesList;
import net.sourceforge.capcode.S57Library.catalogs.S57Attribute;
import net.sourceforge.capcode.S57Library.catalogs.S57AttributesCatalog;
import net.sourceforge.capcode.S57Library.fiedsRecords.descriptive.S57DDRDataDescriptiveField;

/**
 * Common ancestor for fields ATTF, ATTV and NATF
 * S57Attribute label/code *ATTL I(5) b12 int A valid attribute label/code
 * S57Attribute meaning ATVL A( ) gt A string containing a valid meaning for the domain
 * specified by the attribute label/code in ATTL */
public class S57AttributeField extends S57DataField {
	@S57FieldAnnotation(name="*ATTL")
	public int code;
	@S57FieldAnnotation(name="ATVL")
	public String attributeValue;

	public AttributesList attributes;
	
	public S57AttributeField(String tag, byte[] fieldData,
			S57DDRDataDescriptiveField fieldDefinition) throws Exception {
		super(tag, fieldData, fieldDefinition);
		attributes = new AttributesList(1);
		decode();	
	}
	
	public void updateField(){
		S57Attribute s57Attribute = new S57Attribute();
		s57Attribute.name = S57AttributesCatalog.getByCode(code);
		s57Attribute.value = attributeValue;
		attributes.add(s57Attribute);
	}

}

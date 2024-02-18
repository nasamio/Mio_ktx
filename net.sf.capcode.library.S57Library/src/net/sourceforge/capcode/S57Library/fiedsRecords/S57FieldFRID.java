/**
 * 
 */
package net.sourceforge.capcode.S57Library.fiedsRecords;

import net.sourceforge.capcode.S57Library.basics.S57FieldAnnotation;
import net.sourceforge.capcode.S57Library.catalogs.E_S57Object;
import net.sourceforge.capcode.S57Library.catalogs.S57ObjectsCatalog;
import net.sourceforge.capcode.S57Library.fiedsRecords.descriptive.S57DDRDataDescriptiveField;
import net.sourceforge.capcode.S57Library.objects.E_S57ObjectPrimitiveType;

/**
Field Tag: FRID Field Name: Feature Record Identifier
Record name RCNM A(2) b11  "FE" {100} Record identification number 
RCID I(10 ) b14 int Range: 1 to 232-2
Object geometric primitive PRIM A(1) b11 
	"P" {1} Point
	"L" {2} Line
	"A" {3} Area
	"N" {255} Object does not directly reference any spatial objects (see 4.2.1)
Group GRUP I(3) b11 int Range: 1 to 254, 255 - No group (binary)
Object label/code OBJL I(5) b12 int A valid object label/code
Record version RVER I(3) b12 int contains the serial number of the record edition (see 8.4.2.1)
Record update instruction RUIN A(1) b11 
	"I" {1} Insert
	"D" {2} Delete
	"M" {3} Modify (see 8.4.2.2) 
*/
public class S57FieldFRID extends S57IdentityField {	
	@S57FieldAnnotation(name="PRIM", setter = "setPRIM")
	public int primCode;
	@S57FieldAnnotation(name="GRUP")
	public int group;
	@S57FieldAnnotation(name="OBJL", setter="setOBJL")
	public int objectLabelCode;
	@S57FieldAnnotation(name="RVER")
	public int recordVersion;
	@S57FieldAnnotation(name="RUIN")
	public int recordUpdateInstruction;
	

	public E_S57Object object;
	public E_S57ObjectPrimitiveType objectPrimitiveType;
	
	public S57FieldFRID(String tag, byte[] fieldData,
			S57DDRDataDescriptiveField fieldDefinition) throws Exception {
		super(tag, fieldData, fieldDefinition);
		decode();	
	}
	
	public void setOBJL(Integer x){
		object = S57ObjectsCatalog.getByCode(x);
	}
	
	public void setPRIM(Integer x){
		objectPrimitiveType = E_S57ObjectPrimitiveType.byCode(x);
	}
	
	public String toString(){
		return super.toString() + String.format("%s; %s", objectPrimitiveType, object);
	}
}

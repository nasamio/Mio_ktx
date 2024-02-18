/**
 * 
 */
package net.sourceforge.capcode.S57Library.fiedsRecords;

import net.sourceforge.capcode.S57Library.basics.S57FieldAnnotation;
import net.sourceforge.capcode.S57Library.fiedsRecords.S57SubFieldFormat.FormatType;
import net.sourceforge.capcode.S57Library.fiedsRecords.descriptive.S57DDRDataDescriptiveField;

/**
 * Field Tag: FOID Field Name: Feature Object Identifier<br>
 * Producing agency AGEN A(2) b12 an Agency code (see 4.3)<br>
 * Feature identification number FIDN I(10) b14 int Range: 1 to 232-2 (see 4.3.2)<br>
 * Feature identification subdivision FIDS I(5) b12 int Range: 1 to 216-2 (see 4.3.2)<br>
 */
public class S57FieldFOID extends S57DataField {
	@S57FieldAnnotation(name="AGEN")
	public int producingAgency;
	@S57FieldAnnotation(name="FIDN")
	public int featureIdentificationNumber;
	@S57FieldAnnotation(name="FIDS")
	public int featureIdentificationSubdivision;
	public long worldWideIdentifier;
	public S57FieldFOID(String tag, byte[] fieldData,
			S57DDRDataDescriptiveField fieldDefinition) throws Exception {
		super(tag, fieldData, fieldDefinition);
		decode();	
	}
	
	@Override
	protected void updateField(){
		worldWideIdentifier = (Long) getSubFieldValue(data, new S57SubFieldFormat(FormatType.binaryUnsignedInteger, 8));
	}

}

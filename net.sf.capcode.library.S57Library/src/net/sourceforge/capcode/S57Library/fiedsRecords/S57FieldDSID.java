/**
 * 
 */
package net.sourceforge.capcode.S57Library.fiedsRecords;

import net.sourceforge.capcode.S57Library.basics.E_S57IntededUsage;
import net.sourceforge.capcode.S57Library.basics.E_S57RecordType;
import net.sourceforge.capcode.S57Library.basics.S57FieldAnnotation;
import net.sourceforge.capcode.S57Library.fiedsRecords.descriptive.S57DDRDataDescriptiveField;


/**
 * @author cyrille
 *
 */
public class S57FieldDSID extends S57IdentityField {
	@S57FieldAnnotation(name="EXPP")
	public int exchangePurpose;
	@S57FieldAnnotation(name="INTU", setter = "setINTU")
	public int intu;
	@S57FieldAnnotation(name="DSNM")
	public String datasetName;
	@S57FieldAnnotation(name="EDTN")
	public String editionNumber;
	@S57FieldAnnotation(name="UPDN")
	public String updateNumber;
	@S57FieldAnnotation(name="UADT")
	public String updateApplicationDate;
	@S57FieldAnnotation(name="ISDT")
	public String issueDate;
	@S57FieldAnnotation(name="STED")
	public double s57EditionNumber;
	@S57FieldAnnotation(name="PRSP")
	public int productSpecification;
	@S57FieldAnnotation(name="AGEN")
	public int producingAgency;
	@S57FieldAnnotation(name="COMT")
	public String comment;
	
	/** the setRCNM set this meaning based on recordName */
	public E_S57RecordType recordType;
	public E_S57IntededUsage intendedUsage;
	
	public S57FieldDSID(String tag, byte[] fieldData,
			S57DDRDataDescriptiveField fieldDefinition) {
		super(tag, fieldData, fieldDefinition);
		decode();
	}
	
	public void setINTU(Integer i){
		intendedUsage = E_S57IntededUsage.byCode(i);
	}
	


}

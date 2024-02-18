/**
 * 
 */
package net.sourceforge.capcode.S57Library.fiedsRecords;

import java.util.Vector;

import net.sourceforge.capcode.S57Library.basics.S57FieldAnnotation;
import net.sourceforge.capcode.S57Library.fiedsRecords.descriptive.S57DDRDataDescriptiveField;

/**
 * Field Tag: FFPT Field Name: Feature Record to Feature Object Pointer
 * Long Name *LNAM A(17) B(64) an Foreign pointer (see 4.3)
 * Relationship indicator RIND A( ) b11 
 * 	"M" {1} Master
 * 	"S" {2} Slave
 * 	"P" {3} Peer
 * Other values may be defined by the relevant product specification (see 6.2 and 6.3)
 * Comment COMT A( ) bt A string of characters 
*/
public class S57FieldFFPT extends S57DataField {
	@S57FieldAnnotation(name="*LNAM")
	public long longName;
	@S57FieldAnnotation(name="RIND")
	public int relationshipIndicator;
	@S57FieldAnnotation(name="COMT")
	public String comments;
	
	public Vector<Long> names;
	
	public S57FieldFFPT(String tag, byte[] fieldData,
			S57DDRDataDescriptiveField fieldDefinition) throws Exception {
		super(tag, fieldData, fieldDefinition);
		names = new Vector<Long>(1);
		decode();	
	}

	@Override
	protected void updateField(){
		names.add(longName);
	}
}

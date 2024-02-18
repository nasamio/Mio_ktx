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
public class S57FieldDSSI extends S57DataField {
	@S57FieldAnnotation(name="DSTR")
	public int dataStructure;
	@S57FieldAnnotation(name="AALL")
	public int attfLexicalLevel;
	@S57FieldAnnotation(name="NALL")
	public int nationalLexicalLevel;
	@S57FieldAnnotation(name="NOMR")
	public int numberOfMetaRecords;
	@S57FieldAnnotation(name="NOCR")
	public int numberOfCartographicRecords;
	@S57FieldAnnotation(name="NOGR")
	public int numberOfGeoRecords;
	@S57FieldAnnotation(name="NOLR")
	public int numberOfCollectionrecords;
	@S57FieldAnnotation(name="NOIN")
	public int numberOfIsolatedNodes;
	@S57FieldAnnotation(name="NOCN")
	public int numberOfConnectedNodes;
	@S57FieldAnnotation(name="NOED")
	public int numberOfEdgeRecords;
	@S57FieldAnnotation(name="NOFA")
	public int numberOfFaceRecords;

	public S57FieldDSSI(String tag, byte[] fieldData,
			S57DDRDataDescriptiveField fieldDefinition) throws Exception {
		super(tag, fieldData, fieldDefinition);
		decode();
	}

}

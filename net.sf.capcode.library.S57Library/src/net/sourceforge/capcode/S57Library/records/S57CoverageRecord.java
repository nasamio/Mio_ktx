/**
 * 
 */
package net.sourceforge.capcode.S57Library.records;

import net.sourceforge.capcode.S57Library.fiedsRecords.S57DataField;
import net.sourceforge.capcode.S57Library.fiedsRecords.S57FieldDSPM;
import net.sourceforge.capcode.S57Library.fiedsRecords.S57FieldSG2D;
import net.sourceforge.capcode.S57Library.fiedsRecords.descriptive.S57DDRDataDescriptiveField;
import net.sourceforge.capcode.S57Library.fiedsRecords.descriptive.S57DDRField;
import net.sourceforge.capcode.S57Library.files.S57ModuleReader;


/**
 * @author cyrille
 *
 */
public class S57CoverageRecord extends S57LogicalRecord{

	private S57ModuleReader module;
	private boolean spatial;
	public S57FieldSG2D sg2d;

	public S57CoverageRecord(byte[] array, S57ModuleReader module) throws Exception {
		super(array);
		this.module = module;
	}

	@Override
	protected boolean checkValidity() {
		return true;
	}

	private S57DataField readField(String tag) throws Exception {
		S57DDRField directoryEntry = getDirectoryEntry(tag);
		if (directoryEntry != null){
			S57DDRDataDescriptiveField fieldDefinition = module.fieldDefinitions.get(tag);
			int pos = directoryEntry.getFieldPos();
			int length = directoryEntry.getFieldLength();
			byte[] fieldData = new byte[length];
			System.arraycopy(array, fieldAreaBaseAddress+pos, fieldData, 0, length);
			if (tag.equals("SG2D")) return new S57FieldSG2D(tag, fieldData, fieldDefinition, module.getCoordinateMultiplicationFactor());
			else if (tag.equals("DSPM")) return new S57FieldDSPM(tag, fieldData, fieldDefinition);
//			else if (tag.equals("ATTF")) return new S57FieldATTF(tag, fieldData, fieldDefinition);
//			else if (tag.equals("FSPT")) return new S57FieldFSPT(tag, fieldData, fieldDefinition);
//			else if (tag.equals("FRID")) return new S57FieldFRID(tag, fieldData, fieldDefinition);
//			else if (tag.equals("VRID")) return new S57FieldVRID(tag, fieldData, fieldDefinition);
		}
		return null;
	}

	public void readSpatials() throws Exception {
		if (module.getCoordinateMultiplicationFactor() == 0.0 && this.isTagPresent("DSPM")){
			module.setDSPM((S57FieldDSPM) readField("DSPM"));
		}
		if (this.isTagPresent("SG2D")){
			sg2d = (S57FieldSG2D) readField("SG2D");
			setSpatial(true);
		}else{
			setSpatial(false);
		}
	}

	/**
	 * @param spatial the spatial to set
	 */
	public void setSpatial(boolean spatial) {
		this.spatial = spatial;
	}

	/**
	 * @return the spatial
	 */
	public boolean isSpatial() {
		return spatial;
	}


}

/**
 * 
 */
package net.sourceforge.capcode.S57Library.records;

import net.sourceforge.capcode.S57Library.fiedsRecords.S57FieldATTF;
import net.sourceforge.capcode.S57Library.fiedsRecords.S57FieldATTV;
import net.sourceforge.capcode.S57Library.fiedsRecords.S57FieldCATD;
import net.sourceforge.capcode.S57Library.fiedsRecords.S57FieldDSID;
import net.sourceforge.capcode.S57Library.fiedsRecords.S57FieldDSPM;
import net.sourceforge.capcode.S57Library.fiedsRecords.S57FieldDSSI;
import net.sourceforge.capcode.S57Library.fiedsRecords.S57FieldFFPT;
import net.sourceforge.capcode.S57Library.fiedsRecords.S57FieldFOID;
import net.sourceforge.capcode.S57Library.fiedsRecords.S57FieldFRID;
import net.sourceforge.capcode.S57Library.fiedsRecords.S57FieldFSPT;
import net.sourceforge.capcode.S57Library.fiedsRecords.S57FieldNATF;
import net.sourceforge.capcode.S57Library.fiedsRecords.S57FieldSG2D;
import net.sourceforge.capcode.S57Library.fiedsRecords.S57FieldSG3D;
import net.sourceforge.capcode.S57Library.fiedsRecords.S57FieldVRID;
import net.sourceforge.capcode.S57Library.fiedsRecords.S57FieldVRPT;
import net.sourceforge.capcode.S57Library.fiedsRecords.descriptive.S57DDRDataDescriptiveField;
import net.sourceforge.capcode.S57Library.fiedsRecords.descriptive.S57DDRField;
import net.sourceforge.capcode.S57Library.files.S57ByteBuffer;
import net.sourceforge.capcode.S57Library.files.S57ModuleReader;
import net.sourceforge.capcode.S57Library.objects.S57Edge;
import net.sourceforge.capcode.S57Library.objects.S57Feature;
import net.sourceforge.capcode.S57Library.objects.S57ObjectsVector;
import net.sourceforge.capcode.S57Library.objects.S57Spatial;


/**
 * @author cyrille
 *
 */
public class S57DataRecord extends S57LogicalRecord{

	private S57ModuleReader module;

	public S57DataRecord(byte[] array, S57ModuleReader module) throws Exception {
		super(array);
		this.module = module;
	}

	@Override
	protected boolean checkValidity() {
		return true;
//		return identifier == 'D'
//			&& codeExtensionIndicator == ' ' && versionNumber == 0;
	}
	
	@Override
	public String toString(){
		return String.format("DR/%s", super.toString(), module.fieldDefinitions.toString());
	}

	public void readFields() throws Exception {
		S57ObjectsVector spatials = module.getSpatials();
		S57ObjectsVector features = module.getFeatures();
		int width = this.getFieldEntryWidth();
		int fieldOffsetSize = getFieldOffsetSize();
		int index = HEADER_RECORD_SIZE;
		int fieldLengthSize = getFieldLengthSize();
		while (array[index] != S57DDRField.FIELD_TERMINATOR){
			//cast the current object
			S57Spatial spatial = module.currentObject instanceof S57Spatial ? (S57Spatial) module.currentObject : null;
			S57Feature feature =  module.currentObject instanceof S57Feature ? (S57Feature) module.currentObject : null;
			//extract the tag
			String tag = S57ByteBuffer.getString(array, index, 4);
			//search the fields definition for this tag
			S57DDRDataDescriptiveField fieldDefinition = module.fieldDefinitions.get(tag);
			//create a byte array of the field size 
			int length = S57ByteBuffer.getInteger(array, index + 4, fieldLengthSize);
			int pos = S57ByteBuffer.getInteger(array, index + 4 + fieldLengthSize, fieldOffsetSize);
			//and copy the content of the buffer in this byte array
			byte[] fieldData = new byte[length];
			System.arraycopy(array, fieldAreaBaseAddress+pos, fieldData, 0, length);

			if (tag.equals("0001")){
				//ignored int id = new S57Field0001(tag, fieldData, fieldDefinition).id;
			}
			else if (tag.equals("CATD")){
				module.newCatEntryCallBack(new S57FieldCATD(tag, fieldData, fieldDefinition));
			}
			else if (tag.equals("DSID")){
				module.setDSID(new S57FieldDSID(tag, fieldData, fieldDefinition));
			}
			else if (tag.equals("DSSI")){
				module.setDSSI(new S57FieldDSSI(tag, fieldData, fieldDefinition));
			}
			else if (tag.equals("DSPM")){
				module.setDSPM(new S57FieldDSPM(tag, fieldData, fieldDefinition));
			}
			/*=============== SPATIAL RECORDS======================*/
			else if (tag.equals("VRID")){
				module.currentObject = S57Spatial.buildFromVRID(new S57FieldVRID(tag, fieldData, fieldDefinition));
				spatial = (S57Spatial) module.currentObject;
				spatials.add(spatial);
			}
			else if (tag.equals("ATTV")){
				S57FieldATTV attv = new S57FieldATTV(tag, fieldData, fieldDefinition);
				spatial.addAttributes(attv.attributes);
			}
			else if (tag.equals("VRPT")){
				S57FieldVRPT vrpt = new S57FieldVRPT(tag, fieldData, fieldDefinition);					
				if (spatial instanceof S57Edge){
					S57Edge edge = (S57Edge) spatial;
					edge.addVectors(vrpt.links, spatials);						
				}else{
					throw new Exception("invalid VRPT structure");
				}
			}
			else if (tag.equals("SG2D")){
				S57FieldSG2D sg2d = new S57FieldSG2D(tag, fieldData, fieldDefinition, 
						module.getCoordinateMultiplicationFactor());
				spatial.addPositions(sg2d.positions);
			}
			else if (tag.equals("SG3D")){
				S57FieldSG3D sg3d = new S57FieldSG3D(tag, fieldData, fieldDefinition, 
						module.getCoordinateMultiplicationFactor(),
						module.getSoundingMultiplicationFactor());					
				spatial.addPositions(sg3d.positions);
			}
			/*================= FEATURE RECORDS ===================*/
			else if (tag.equals("FRID")){
				module.currentObject = S57Feature.buildFromFRID(new S57FieldFRID(tag, fieldData, fieldDefinition));					
				if (module.currentObject != null){
					features.add(module.currentObject);
				}
				feature = (S57Feature) module.currentObject;
			}
			else if (tag.equals("FOID")){
				S57FieldFOID foid = new S57FieldFOID(tag, fieldData, fieldDefinition);
				if (feature != null){
					feature.setWorldWideIdentifier(foid.worldWideIdentifier);						
				}
			}
			else if (tag.equals("ATTF")){
				S57FieldATTF attf = new S57FieldATTF(tag, fieldData, fieldDefinition);
				if (feature != null){
					feature.addAttributes(attf.attributes);
				}
			}
			else if (tag.equals("NATF")){
				S57FieldNATF natf = new S57FieldNATF(tag, fieldData, fieldDefinition);
				if (feature != null){
					feature.addAttributes(natf.attributes);						
				}
			}
			else if (tag.equals("FFPT")){
				S57FieldFFPT ffpt = new S57FieldFFPT(tag, fieldData, fieldDefinition);
				if (feature != null){
					feature.addFeatures(ffpt, features);
				}
			}
			else if (tag.equals("FSPT")){
				S57FieldFSPT fspt = new S57FieldFSPT(tag, fieldData, fieldDefinition);	
				if (feature != null){
					feature.addVectors(fspt.links);
				}
			}
			else{
				System.err.println(tag + " not decoded");
			}
			index += width;
		}
	}

}

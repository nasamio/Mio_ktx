/**
 * 
 */
package net.sourceforge.capcode.S57Library.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import net.sourceforge.capcode.S57Library.basics.E_DataStructure;
import net.sourceforge.capcode.S57Library.basics.E_HorizontalDatum;
import net.sourceforge.capcode.S57Library.basics.E_VerticalDatum;
import net.sourceforge.capcode.S57Library.basics.S57Pos2D;
import net.sourceforge.capcode.S57Library.catalogs.S57CatalogExpectedValues;
import net.sourceforge.capcode.S57Library.fiedsRecords.S57FieldCATD;
import net.sourceforge.capcode.S57Library.fiedsRecords.S57FieldDSID;
import net.sourceforge.capcode.S57Library.fiedsRecords.S57FieldDSPM;
import net.sourceforge.capcode.S57Library.fiedsRecords.S57FieldDSSI;
import net.sourceforge.capcode.S57Library.fiedsRecords.S57FieldDefinitionTable;
import net.sourceforge.capcode.S57Library.objects.S57Object;
import net.sourceforge.capcode.S57Library.objects.S57ObjectsVector;
import net.sourceforge.capcode.S57Library.records.S57CoverageRecord;
import net.sourceforge.capcode.S57Library.records.S57DataDescriptiveRecord;
import net.sourceforge.capcode.S57Library.records.S57DataRecord;
import net.sourceforge.capcode.S57Library.records.S57LogicalRecord;

/**
 * @author cyrille
 *
 */
public class S57ModuleReader{
	protected String fileName;
	public S57FieldDefinitionTable fieldDefinitions;
	protected S57ObjectsVector vectors;
	protected S57ObjectsVector features;
	//used to store the current object being analysed in the file
	public S57Object currentObject = null;
	private String intendedUsage;
	protected String dataSetName;
	private String editionNumber;
	private String updateNumber;
	private boolean showProgressWindow;
	private E_VerticalDatum verticalDatum;
	private E_HorizontalDatum horizontalDatum;
	
	private double coordinateMultiplicationFactor;
	protected int scale;
	private double soundingMultiplicationFactor;
	private E_DataStructure dataStructure;
	private int lexicalLevel;
	private int nationalLexicalLevel;
	private int numberOfMetaRecords;
	private int numberOfCartographicRecords;
	private int numberOfGeoRecords;
	private int numberOfCollectionrecords;
	private int numberOfIsolatedNodes;
	private int numberOfConnectedNodes;
	private int numberOfEdgeRecords;
	private int numberOfFaceRecords;
	private boolean canceled = false;
	private ByteBuffer buffer;
	private String depthUnit;
	private String heightUnit;
	private String issueDate;
	
	public S57ModuleReader(){
		super();
		fieldDefinitions = new S57FieldDefinitionTable(50);
		vectors = new S57ObjectsVector();
		features = new S57ObjectsVector();
	}
				
	/**
	 * reads the LogicalRecord at the index position.<br>
	 * the first 5 bytes at the index must give the size of the LogicalRecord.
	 * the first record is the Data Descriptive Record.
	 * all the following ones are Data record.
	 * the fields read from data record are organised and classified in the field objects.
	 * @param index : position in the file where should start a LogicalRecord 
	 * @return a LogicalRecord class
	 * @throws Exception 
	 */
	private S57LogicalRecord readRecordAtIndex(int index) throws Exception {
		S57DataRecord res = null;
		try{
			int size = extractRecordSize(index);	
			byte[] array = new byte[size];
			buffer.get(array);
			if (index == 0){
				return new S57DataDescriptiveRecord(array, fieldDefinitions);			
			}
			//implicite else: index is not 0
			res = new S57DataRecord(array, this);
			res.readFields();
		}catch(Exception e){
		}
		return res;
	}

	/**
	 * extract the record size from the file.
	 * then return to the position. This is used locally to allocate the exact size needed
	 * for a record.
	 * @param index
	 * @param size
	 * @return an integer representing the size of the record to read.
	 * @throws IOException
	 */
	private int extractRecordSize(int index) throws IOException {
		int res = 0;
		byte[] b;
		int oldPos = buffer.position();
		buffer.position(index);
		if (buffer.remaining()>= 5){
			b = new byte[5];
			buffer.get(b);			
		}else if (buffer.remaining()>0){			
			b = new byte[buffer.remaining()];
			buffer.get(b);			
		}else{
			b = new byte[]{'0'};
		}
		buffer.position(oldPos);
		String s = S57ByteBuffer.getString(b, b.length);
		try{
			res = Integer.parseInt(s);			
		}catch(Exception e){
			res = 0;
		}
		return res;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	public S57ObjectsVector getFeatures() {
		return features;
	}
	
	public S57ObjectsVector getSpatials() {
		return vectors;
	}

	/**
	 * Update the record with informations found in the DSID field.<br>
	 *see IHO S-57 section 6.3.2.1 for more details on DSID
	 * @param dsid a DSID field
	 */
	public void setDSID(S57FieldDSID dsid) {
		setIntendedUsage(dsid.intendedUsage.name());
		setDataSetName(dsid.datasetName);
		editionNumber = dsid.editionNumber;
		updateNumber = dsid.updateNumber;
		setIssueDate(dsid.issueDate);
	}
	
	/**
	 * Update the record with informations found in the DSPM field.<br>
	 *see IHO S-57 section 6.3.2.3 for more details on DSPM
	 * @param dspm a DSPM field
	 */
	public void setDSPM(S57FieldDSPM dspm) {
		verticalDatum = E_VerticalDatum.byCode(dspm.verticalDatum);
		setHorizontalDatum(E_HorizontalDatum.byCode(dspm.horizontalDatum));
		coordinateMultiplicationFactor = (double)(1 / (double)dspm.coordinateMultiplicationFactor);
		setSoundingMultiplicationFactor((double) (1 / (double)dspm.soundingMultiplicationFactor));
		setScale(dspm.compilationScaleOfData);
		setDepthUnit(S57CatalogExpectedValues.getByCode(89, dspm.unitOfDepthMeasurement));
		setHeightUnit(S57CatalogExpectedValues.getByCode(96, dspm.unitOfHeightMeasurement));
	}
	
	/**
	 * Update the record with informations found in the DSSI field.<br>
	 *see IHO S-57 section 7.3.1.2 for more details on DSSI
	 * @param dssi a DSSI field
	 */
	public void setDSSI(S57FieldDSSI dssi) {
		setDataStructure(E_DataStructure.byCode(dssi.dataStructure));
		setLexicalLevel(dssi.attfLexicalLevel);
		setNationalLexicalLevel(dssi.nationalLexicalLevel);
		numberOfMetaRecords = dssi.numberOfMetaRecords;
		numberOfCartographicRecords = dssi.numberOfCartographicRecords;
		numberOfGeoRecords = dssi.numberOfGeoRecords;
		numberOfCollectionrecords = dssi.numberOfCollectionrecords;
		numberOfIsolatedNodes = dssi.numberOfIsolatedNodes;
		numberOfConnectedNodes = dssi.numberOfConnectedNodes;
		numberOfEdgeRecords = dssi.numberOfEdgeRecords;
		numberOfFaceRecords = dssi.numberOfFaceRecords;
		
	}	


	/**
	 * @param intendedUsage the intendedUsage to set
	 */
	public void setIntendedUsage(String intendedUsage) {
		this.intendedUsage = intendedUsage;
	}

	/**
	 * @return the intendedUsage
	 */
	public String getIntendedUsage() {
		return intendedUsage;
	}

	/**
	 * @param dataSetName the dataSetName to set
	 */
	public void setDataSetName(String dataSetName) {
		this.dataSetName = dataSetName;
	}

	/**
	 * @return the dataSetName
	 */
	public String getDataSetName() {
		return dataSetName;
	}

	/**
	 * @return the editionNumber
	 */
	public String getEditionNumber() {
		return editionNumber;
	}

	/**
	 * @param editionNumber the editionNumber to set
	 */
	public void setEditionNumber(String editionNumber) {
		this.editionNumber = editionNumber;
	}

	/**
	 * @return the updateNumber
	 */
	public String getUpdateNumber() {
		return updateNumber;
	}

	/**
	 * @param updateNumber the updateNumber to set
	 */
	public void setUpdateNumber(String updateNumber) {
		this.updateNumber = updateNumber;
	}
	
	/**
	 * @return the showProgressWindow
	 */
	public boolean isShowProgressWindow() {
		return showProgressWindow;
	}

	/**
	 * @param showProgressWindow the showProgressWindow to set
	 */
	public void setShowProgressWindow(boolean showProgressWindow) {
		this.showProgressWindow = showProgressWindow;
	}
	
/**
	 * @return the verticalDatum
	 */
	public E_VerticalDatum getVerticalDatum() {
		return verticalDatum;
	}

	/**
	 * @param verticalDatum the verticalDatum to set
	 */
	public void setVerticalDatum(E_VerticalDatum verticalDatum) {
		this.verticalDatum = verticalDatum;
	}

	/**
	 * @return the coordinateMultiplicationFactor
	 */
	public double getCoordinateMultiplicationFactor() {
		return coordinateMultiplicationFactor;
	}

	/**
	 * @param coordinateMultiplicationFactor the coordinateMultiplicationFactor to set
	 */
	public void setCoordinateMultiplicationFactor(
			double coordinateMultiplicationFactor) {
		this.coordinateMultiplicationFactor = coordinateMultiplicationFactor;
	}

	/**
	 * @param soundingMultiplicationFactor the soundingMultiplicationFactor to set
	 */
	public void setSoundingMultiplicationFactor(double soundingMultiplicationFactor) {
		this.soundingMultiplicationFactor = soundingMultiplicationFactor;
	}

	/**
	 * @return the soundingMultiplicationFactor
	 */
	public double getSoundingMultiplicationFactor() {
		return soundingMultiplicationFactor;
	}

	/**
	 * @param dataStructure the dataStructure to set
	 */
	public void setDataStructure(E_DataStructure dataStructure) {
		this.dataStructure = dataStructure;
	}

	/**
	 * @return the dataStructure
	 */
	public E_DataStructure getDataStructure() {
		return dataStructure;
	}

	/**
	 * @param lexicalLevel the lexicalLevel to set
	 */
	public void setLexicalLevel(int lexicalLevel) {
		this.lexicalLevel = lexicalLevel;
	}

	/**
	 * @return the lexicalLevel
	 */
	public int getLexicalLevel() {
		return lexicalLevel;
	}

	/**
	 * @param nationalLexicalLevel the nationalLexicalLevel to set
	 */
	public void setNationalLexicalLevel(int nationalLexicalLevel) {
		this.nationalLexicalLevel = nationalLexicalLevel;
	}

	/**
	 * @return the nationalLexicalLevel
	 */
	public int getNationalLexicalLevel() {
		return nationalLexicalLevel;
	}

	/**
	 * @return the numberOfMetaRecords
	 */
	public int getNumberOfMetaRecords() {
		return numberOfMetaRecords;
	}

	/**
	 * @param numberOfMetaRecords the numberOfMetaRecords to set
	 */
	public void setNumberOfMetaRecords(int numberOfMetaRecords) {
		this.numberOfMetaRecords = numberOfMetaRecords;
	}

	/**
	 * @return the numberOfCartographicRecords
	 */
	public int getNumberOfCartographicRecords() {
		return numberOfCartographicRecords;
	}

	/**
	 * @param numberOfCartographicRecords the numberOfCartographicRecords to set
	 */
	public void setNumberOfCartographicRecords(int numberOfCartographicRecords) {
		this.numberOfCartographicRecords = numberOfCartographicRecords;
	}

	/**
	 * @return the numberOfGeoRecords
	 */
	public int getNumberOfGeoRecords() {
		return numberOfGeoRecords;
	}

	/**
	 * @param numberOfGeoRecords the numberOfGeoRecords to set
	 */
	public void setNumberOfGeoRecords(int numberOfGeoRecords) {
		this.numberOfGeoRecords = numberOfGeoRecords;
	}

	/**
	 * @return the numberOfCollectionrecords
	 */
	public int getNumberOfCollectionrecords() {
		return numberOfCollectionrecords;
	}

	/**
	 * @param numberOfCollectionrecords the numberOfCollectionrecords to set
	 */
	public void setNumberOfCollectionrecords(int numberOfCollectionrecords) {
		this.numberOfCollectionrecords = numberOfCollectionrecords;
	}

	/**
	 * @return the numberOfIsolatedNodes
	 */
	public int getNumberOfIsolatedNodes() {
		return numberOfIsolatedNodes;
	}

	/**
	 * @param numberOfIsolatedNodes the numberOfIsolatedNodes to set
	 */
	public void setNumberOfIsolatedNodes(int numberOfIsolatedNodes) {
		this.numberOfIsolatedNodes = numberOfIsolatedNodes;
	}

	/**
	 * @return the numberOfConnectedNodes
	 */
	public int getNumberOfConnectedNodes() {
		return numberOfConnectedNodes;
	}

	/**
	 * @param numberOfConnectedNodes the numberOfConnectedNodes to set
	 */
	public void setNumberOfConnectedNodes(int numberOfConnectedNodes) {
		this.numberOfConnectedNodes = numberOfConnectedNodes;
	}

	/**
	 * @return the numberOfEdgeRecords
	 */
	public int getNumberOfEdgeRecords() {
		return numberOfEdgeRecords;
	}

	/**
	 * @param numberOfEdgeRecords the numberOfEdgeRecords to set
	 */
	public void setNumberOfEdgeRecords(int numberOfEdgeRecords) {
		this.numberOfEdgeRecords = numberOfEdgeRecords;
	}

	/**
	 * @return the numberOfFaceRecords
	 */
	public int getNumberOfFaceRecords() {
		return numberOfFaceRecords;
	}

	/**
	 * @param numberOfFaceRecords the numberOfFaceRecords to set
	 */
	public void setNumberOfFaceRecords(int numberOfFaceRecords) {
		this.numberOfFaceRecords = numberOfFaceRecords;
	}

	public void load(final String aFileName){
		commonLoadProc(aFileName);
//		Thread t = new Thread(new Runnable(){
//			public void run() {
//				commonLoadProc(aFileName);
//			}		
//		});
//		t.start();
	}

	/**
	 * Opens a file as being a Catalog. This can be known if the extention fo the file is .031
	 * @param aFileName
	 */
	public void loadCatalog(String aFileName){
		commonLoadProc(aFileName);
	}

	public void loadNonThreaded(String aFileName){
		commonLoadProc(aFileName);		
	}
	
	private void commonLoadProc(String aFileName){
		this.fileName = aFileName;		
		int index = 0;
		int recNum = 0;
		int size = (int) new File(fileName).length();
		try {
			FileChannel fc = new FileInputStream(fileName).getChannel();
			buffer = ByteBuffer.allocate(size);
			callBackInit(size);
			//read the whole file into the byteBuffer
			fc.read(buffer);
			fc.close();
			buffer.rewind();
			while (buffer.position() < size && !canceled){
				S57LogicalRecord record = readRecordAtIndex(index);
				if (record != null){
					if(!record.isValid()){
						throw new S57ReadException(String.format("invalid record at rec N: %d (@%d)", recNum, index));
					}
					index += record.getRecordLength();
					callBackWhileLoading(record, record.getRecordLength());
					if (canceled){
						break;
					}					
				}else{
					//error in file
					break;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		callBackEnd();
		
	}


	/**
	 * reads the LogicalRecord at the index position.<br>
	 * the first 5 bytes at the index must give the size of the LogicalRecord.
	 * the first record is the Data Descriptive Record.
	 * all the following ones are Data record.
	 * the fields read from data record are organised and classified in the field objects.
	 * @param index : position in the file where should start a LogicalRecord 
	 * @return a LogicalRecord class
	 * @throws Exception 
	 */
	private S57LogicalRecord readCoverageRecordAtIndex(int index) throws Exception {
		int size = extractRecordSize(index);	
		byte[] array = new byte[size];
		buffer.get(array);
		if (index == 0){
			return new S57DataDescriptiveRecord(array, fieldDefinitions);
		}
		//implicite else: index is not 0
		S57CoverageRecord res = new S57CoverageRecord(array, this);
		res.readSpatials();
		return res;
	}

	public double[] findCoverageInFile(String aFileName) throws Exception {
		double[] res = new double[]{180,-90,-180,90};
		
		this.fileName = aFileName;		
		int index = 0;
		int size = (int) new File(fileName).length();
		try {
			FileChannel fc = new FileInputStream(fileName).getChannel();
			buffer = ByteBuffer.allocate(size);
			//read the whole file into the byteBuffer
			fc.read(buffer);
			fc.close();
			buffer.rewind();
			boolean previousSpatial = false;
			index += readCoverageRecordAtIndex(index).getRecordLength();
			while (buffer.position() < size){
				//TODO search for spatial record only and get the max lon/lat and min lon/lat
				S57CoverageRecord record = (S57CoverageRecord) readCoverageRecordAtIndex(index);
				if (record.isSpatial()){
					for (Object o : record.sg2d.positions){
						S57Pos2D p = (S57Pos2D) o;
						res[0] = Math.min(p.longitude, res[0]);
						res[1] = Math.max(p.latitude, res[1]);
						res[2] = Math.max(p.longitude, res[2]);
						res[3] = Math.min(p.latitude, res[3]);
						
					}
					previousSpatial  = true;
				}else if (previousSpatial){
					//no more spatial records then exit
					break;
				}
				index += record.getRecordLength();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
		
	/**
	 * Override this method to do something particular 
	 * before the load really starts (progress window for example)		
	 * @param size : the size of the file to read
	 */
	protected void callBackInit(int size) {
		//do nothing here
	}

	/**
	 * Override this method to do something particular 
	 * during the load (progress window update, for example)
	 * @param record 
	 * @param index : the actual position in the file		
	 */
	protected void callBackWhileLoading(S57LogicalRecord record, int index) {
		//do nothing here
	}
	
	/**
	 * Override this method to do something particular 
	 * at the end of the load process (terminate progress info)		
	 */
	protected void callBackEnd() {
		//do nothing here
	}	
	
	public void cancel(){
		canceled = true; // this should stop the load process
	}

	/**
	 * @param scale the scale to set
	 */
	public void setScale(int scale) {
		this.scale = scale;
	}

	/**
	 * @return the scale
	 */
	public int getScale() {
		return scale;
	}

	public void newCatEntryCallBack(S57FieldCATD catd) {
		// do nothing, called by S57DataRecord
		
	}

	/**
	 * @param horizontalDatum the horizontalDatum to set
	 */
	public void setHorizontalDatum(E_HorizontalDatum horizontalDatum) {
		this.horizontalDatum = horizontalDatum;
	}

	/**
	 * @return the horizontalDatum
	 */
	public E_HorizontalDatum getHorizontalDatum() {
		return horizontalDatum;
	}

	/**
	 * @param depthUnit the depthUnit to set
	 */
	public void setDepthUnit(String depthUnit) {
		this.depthUnit = depthUnit;
	}

	/**
	 * @return the depthUnit
	 */
	public String getDepthUnit() {
		return depthUnit;
	}

	/**
	 * @param heightUnit the heightUnit to set
	 */
	public void setHeightUnit(String heightUnit) {
		this.heightUnit = heightUnit;
	}

	/**
	 * @return the heightUnit
	 */
	public String getHeightUnit() {
		return heightUnit;
	}

	/**
	 * @param issueDate the issueDate to set
	 */
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	/**
	 * @return the issueDate
	 */
	public String getIssueDate() {
		return issueDate;
	}

}
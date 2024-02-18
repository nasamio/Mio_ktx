/**
 * 
 */
package net.sourceforge.capcode.S57Library.fiedsRecords;

import net.sourceforge.capcode.S57Library.basics.PositionsVector;
import net.sourceforge.capcode.S57Library.basics.S57Pos3D;
import net.sourceforge.capcode.S57Library.fiedsRecords.descriptive.S57DDRDataDescriptiveField;


/**
 * @author cyrille
 *
 */
public class S57FieldSG3D extends S57DataField {
	public PositionsVector positions;
	private double cmf;
	private double smf;

	public S57FieldSG3D(String tag, byte[] fieldData,
			S57DDRDataDescriptiveField fieldDefinition, double cmf, double smf) throws Exception {
		super(tag, fieldData, fieldDefinition);
		this.cmf = cmf; //coordinates multiplication factor
		this.smf = smf; //sounding multiplication factor
		positions = new PositionsVector();
		decode();	
	}

	@Override
	public void decode(){
		byte[] bts = new byte[4];
		while (bufferIndex < data.length -1){
			System.arraycopy(data, bufferIndex, bts, 0, 4);			
	         int b1 = (0x000000FF & ((int)bts[3]));
	         int b2 = (0x000000FF & ((int)bts[2]));
	         int b3 = (0x000000FF & ((int)bts[1]));
	         int b4 = (0x000000FF & ((int)bts[0]));
		  	 int y  = (int) ((b1 << 24 | b2 << 16 | b3 << 8 | b4)& 0xFFFFFFFFL);				
			bufferIndex += 4;
			System.arraycopy(data, bufferIndex, bts, 0, 4);			
	         b1 = (0x000000FF & ((int)bts[3]));
	         b2 = (0x000000FF & ((int)bts[2]));
	         b3 = (0x000000FF & ((int)bts[1]));
	         b4 = (0x000000FF & ((int)bts[0]));
		  	 int x  = (int) ((b1 << 24 | b2 << 16 | b3 << 8 | b4)& 0xFFFFFFFFL);				
			bufferIndex += 4;
			System.arraycopy(data, bufferIndex, bts, 0, 4);			
	         b1 = (0x000000FF & ((int)bts[3]));
	         b2 = (0x000000FF & ((int)bts[2]));
	         b3 = (0x000000FF & ((int)bts[1]));
	         b4 = (0x000000FF & ((int)bts[0]));
		  	 int s  = (int) ((b1 << 24 | b2 << 16 | b3 << 8 | b4)& 0xFFFFFFFFL);				
			double sounding = s * smf;
			bufferIndex += 4;
			double lon = x * cmf;
			double lat = y * cmf;
			positions.add(new S57Pos3D(lat, lon, sounding));
		}	
	}
			
}

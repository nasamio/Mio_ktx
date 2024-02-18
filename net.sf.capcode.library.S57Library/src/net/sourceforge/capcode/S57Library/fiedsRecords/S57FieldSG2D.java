/**
 * 
 */
package net.sourceforge.capcode.S57Library.fiedsRecords;

import net.sourceforge.capcode.S57Library.basics.PositionsVector;
import net.sourceforge.capcode.S57Library.basics.S57FieldAnnotation;
import net.sourceforge.capcode.S57Library.basics.S57Pos2D;
import net.sourceforge.capcode.S57Library.fiedsRecords.descriptive.S57DDRDataDescriptiveField;

/**
 * @author cyrille
 * 
 */
public class S57FieldSG2D extends S57DataField {
	@S57FieldAnnotation(name = "*YCOO", setter = "setYCOO")
	public int y;
	@S57FieldAnnotation(name = "XCOO", setter = "setXCOO")
	public int x;

	private final double coordMultFactor;

	public PositionsVector positions;
	private double lon;
	private double lat;

	public S57FieldSG2D(String tag, byte[] fieldData,
			S57DDRDataDescriptiveField fieldDefinition, double cmf)
			throws Exception {
		super(tag, fieldData, fieldDefinition);
		this.coordMultFactor = cmf;
		positions = new PositionsVector();
		decode();
	}

	/**
	 * decode the field SG2D. Overriden for acceleration purpose.
	 */
	@Override
	public void decode() {
		byte[] yAsBytes = new byte[4];
		byte[] xAsBytes = new byte[4];
		while (bufferIndex < data.length - 1) {
			System.arraycopy(data, bufferIndex, yAsBytes, 0, 4);
			int b1 = (0x000000FF & yAsBytes[3]);
			int b2 = (0x000000FF & yAsBytes[2]);
			int b3 = (0x000000FF & yAsBytes[1]);
			int b4 = (0x000000FF & yAsBytes[0]);
			this.y = (int) ((b1 << 24 | b2 << 16 | b3 << 8 | b4) & 0xFFFFFFFFL);
			bufferIndex += 4;
			System.arraycopy(data, bufferIndex, xAsBytes, 0, 4);
			b1 = (0x000000FF & xAsBytes[3]);
			b2 = (0x000000FF & xAsBytes[2]);
			b3 = (0x000000FF & xAsBytes[1]);
			b4 = (0x000000FF & xAsBytes[0]);
			this.x = (int) ((b1 << 24 | b2 << 16 | b3 << 8 | b4) & 0xFFFFFFFFL);
			bufferIndex += 4;
			lon = x * coordMultFactor;
			lat = y * coordMultFactor;
			updateField();
		}

	}

	@Override
	protected void updateField() {
		positions.add(new S57Pos2D(lat, lon));
	}

	public void setXCOO(Integer x) {
		lon = x * coordMultFactor;
	}

	public void setYCOO(Integer y) {
		lat = y * coordMultFactor;
	}

	@Override
	public String toString(boolean detailled) {
		String res = super.toString(detailled) + this.toString();
		return res;
	}

}

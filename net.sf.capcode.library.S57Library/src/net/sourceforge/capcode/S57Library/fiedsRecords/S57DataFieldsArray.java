/**
 * 
 */
package net.sourceforge.capcode.S57Library.fiedsRecords;

import java.util.Vector;

/**
 * @author cyrille
 * 
 */
@SuppressWarnings("serial")
public class S57DataFieldsArray extends Vector<S57DataField> {

	private static final int DEFAULT_SIZE = 100;
	public S57FieldDSPM dspm;

	public S57DataFieldsArray() {
		super(DEFAULT_SIZE);
	}
}

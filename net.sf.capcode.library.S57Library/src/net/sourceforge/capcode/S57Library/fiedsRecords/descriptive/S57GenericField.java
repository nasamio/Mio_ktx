/**
 * 
 */
package net.sourceforge.capcode.S57Library.fiedsRecords.descriptive;

/**
 * @author cyrille
 *
 */
public class S57GenericField {
    public static final byte FIELD_TERMINATOR = 30;
    public static final byte UNIT_TERMINATOR = 31;
    protected static final byte[] TERMINATORS = new byte[]{FIELD_TERMINATOR, UNIT_TERMINATOR};
	protected String tag ="";
    protected int fieldLength = 0;
	protected int fieldPos;

}

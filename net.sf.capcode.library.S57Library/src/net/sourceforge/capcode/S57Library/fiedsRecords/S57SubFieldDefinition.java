/**
 * 
 */
package net.sourceforge.capcode.S57Library.fiedsRecords;



/**
 * @author cyrille
 *
 */
public class S57SubFieldDefinition {
	public String name = "";
	public S57SubFieldFormat format = null;

	public S57SubFieldDefinition(String aName, String aFormat){
		name = aName;
		format = S57SubFieldFormat.decode(aFormat);
	}
		
}

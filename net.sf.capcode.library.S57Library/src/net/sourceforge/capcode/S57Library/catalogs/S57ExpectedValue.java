/**
 * 
 */
package net.sourceforge.capcode.S57Library.catalogs;

/**
 * @author cyrille
 *
 */
public class S57ExpectedValue {
	public int attribute;
	public int code;
	public String meaning;
	public String abrev;
	
	public S57ExpectedValue(int attribute , int code, String value, String abrev){
		this.attribute = attribute;
		this.code = code;
		this.meaning = value;
		this.abrev = abrev;
	}
	
	public String toString(){
		return meaning;
	}

}

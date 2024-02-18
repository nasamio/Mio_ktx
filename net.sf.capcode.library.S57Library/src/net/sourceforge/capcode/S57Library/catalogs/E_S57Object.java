package net.sourceforge.capcode.S57Library.catalogs;

/**
 * Code	ObjectClass	Acronym	Attribute_A	Attribute_B	Attribute_C	Class	Primitives
 * @author cyrille
 *
 */
public class E_S57Object {
	public int code;
	public String objectClassName="";
	public String accronym="";

	public E_S57Object(int code, String className, String accr){
		this.code = code;
		this.objectClassName = className;
		this.accronym = accr;
	}

	@Override
	public String toString(){
		return objectClassName + "("+code+")";
	}

	public boolean isDefaultNotVisible() {
		return accronym.toUpperCase().startsWith("M_");
	}

	/**
	 * @return the forbiden
	 */
	public boolean isForbiden() {
		return accronym.toUpperCase().startsWith("$");
	}

}

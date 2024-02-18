package net.sourceforge.capcode.S57Library.catalogs;

/**
 * This enumeration provide a means to get the name, accronym, type and class for a given code.
 * These values are read from the file data/s57attributes.csv
 */
public class S57CatalogAttributeElement {
	/** the attribute code */
	public int code;
	/** the attribute name */
	public String name;
	/** the accronym of the attribute */
	public String accronym;
	
	public S57CatalogAttributeElement(int code, String name, String accronym){
		this.code = code;
		this.name = name;
		this.accronym = accronym;
	}
	
	public String toString(){
		return name;
	}
}

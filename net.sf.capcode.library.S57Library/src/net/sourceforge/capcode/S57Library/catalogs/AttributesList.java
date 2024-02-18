/**
 * 
 */
package net.sourceforge.capcode.S57Library.catalogs;

import java.util.ArrayList;


/**
 * the list of attributes for a S57 object.
 */
@SuppressWarnings("serial")
public class AttributesList extends ArrayList<S57Attribute> {

	public AttributesList(int size){
		super(size);
	}
	
	public boolean add(S57Attribute a){
		return super.add(a);
	}
	
	public void addAll(AttributesList otherList){
		super.addAll(otherList);
	}
}

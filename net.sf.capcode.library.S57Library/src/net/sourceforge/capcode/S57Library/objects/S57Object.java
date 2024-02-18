/**
 * 
 */
package net.sourceforge.capcode.S57Library.objects;

import java.awt.geom.Path2D;

import net.sourceforge.capcode.S57Library.catalogs.AttributesList;

/**
 * @author cyrille
 *
 */
public class S57Object{
	public int name = -1;
	public int priority = 0;//lowest priority will be drawn first
	public AttributesList attributes;

	protected final static int WINDING_NORMAL = Path2D.WIND_NON_ZERO;
	protected final static int WINDING_REVERSED = Path2D.WIND_NON_ZERO;
	
	public S57Object() {
		super();
	}
			
	public final void addAttributes(AttributesList attr) {
		if (attributes == null){
			attributes = new AttributesList(1);
		}
		attributes.addAll(attr);
	}	
	
	/**
	 * A special toString for debugging purpose.
	 * @return the short className and the object pointer (name).
	 */
	public String toStringShort(){
		return String.format("class:%s; name :%d", getClass().getSimpleName(), name);
	}
	/**
	 * For debugging purpose.
	 * @return the short className, the object pointer (name).
	 */	
	public String toString(){
		return String.format("class:%s; name :%d", getClass().getSimpleName(), name);
	}

}

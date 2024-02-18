/**
 * 
 */
package net.sourceforge.capcode.S57Library.objects;


/**
 * @author cyrille
 *
 */
public enum E_S57ObjectPrimitiveType {
	point(1),
	line(2),
	area(3),
	none(255);
	
	public int code;
	E_S57ObjectPrimitiveType(int code){
		this.code= code;
	}
	public String toString(){
		return String.format("%s(%d)", name(), code);
	}
	
	public static E_S57ObjectPrimitiveType byCode(int code){
		for(E_S57ObjectPrimitiveType o : E_S57ObjectPrimitiveType.values()){
			if (o.code == code){
				return o;
			}
		}
		return null;		
	}
}

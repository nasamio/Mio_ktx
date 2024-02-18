/**
 * 
 */
package net.sourceforge.capcode.S57Library.basics;

/**
 * @author cyrille
 *
 */
public enum E_S57IntededUsage {
	overview(1),
	general(2),
	coastal(3),
	approach(4),
	harbour(5),
	berthing(6),
	unspecified(-1);
	private int code;
	E_S57IntededUsage(int x){code=x;}
	public static E_S57IntededUsage byCode(int x){
		for (E_S57IntededUsage e:E_S57IntededUsage.values()){
			if (e.code == x) return e;
		}
		return unspecified;
	}
}

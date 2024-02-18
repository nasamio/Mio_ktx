/**
 *
 */
package net.sourceforge.capcode.S57Library.basics;

/**
 * this class establish the link between a Feature and a Spatial or
 * between a Spatial and another Spatial
 */
public class Link{
	public long name;
	public int ornt;
	public int usag;
	public int topi;
	public int mask;
	@Override
	public String toString(){
		return String.format("name %d: ornt %d, usag %d, tpoi %d, mask %d", name, ornt, usag, topi, mask);
	}
	public boolean isMasked() {
		return mask == 1;
	}

	public boolean isReversed() {
		return ornt == 2;
	}

	public boolean isInterior() {
		return usag == 2;
	}

	public boolean isExterior() {
		return usag == 1;
	}

	public boolean isTruncated() {
		return usag == 3;
	}

	public boolean isBeginingNode(){
		return topi == 1;
	}

	public boolean isEndNode(){
		return topi == 2;
	}
}

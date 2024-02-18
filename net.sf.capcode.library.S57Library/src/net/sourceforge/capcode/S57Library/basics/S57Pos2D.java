/**
 * 
 */
package net.sourceforge.capcode.S57Library.basics;

/**
 * @author cyrille
 *
 */
public class S57Pos2D {
	public double longitude = 0;
	public double latitude = 0;
	public S57Pos2D(double latitude, double longitude){
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public String toString(){
		return String.format("%f/%f", longitude, latitude);
	}
}

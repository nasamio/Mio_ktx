/**
 * 
 */
package net.sourceforge.capcode.S57Library.basics;

/**
 * @author cyrille
 *
 */
public enum E_VerticalDatum {
	unspecified,
	mean_low_water_springs,
	mean_lower_low_water_springs,
	mean_sea_level,
	lowest_low_water,
	mean_low_water,
	lowest_low_water_springs,
	approximate_mean_low_water_springs,
	indian_spring_low_water,
	low_water_springs,
	approximate_lowest_astronomical_tide,
	nearly_lowest_low_water,
	mean_lower_low_water,
	low_water,
	approximate_mean_low_water,
	approximate_mean_lower_low_water,
	mean_high_water,
	mean_high_water_springs,
	high_water,
	approximate_mean_sea_level,
	high_water_springs,
	mean_higher_high_water,
	equinoctial_spring_low_water,
	lowest_astronomical_tide,
	local_datum,
	international_Great_Lakes_Datum_1985,
	mean_water_level,
	lower_low_water_large_tide,
	higher_high_water_large_tide,
	nearly_highest_high_water,
	highest_astronomical_tide;

	public static E_VerticalDatum byCode(int code) {
		for (E_VerticalDatum d:E_VerticalDatum.values()){
			if (d.ordinal() == code){
				return d;
			}			
		}
		return null;
	}
}

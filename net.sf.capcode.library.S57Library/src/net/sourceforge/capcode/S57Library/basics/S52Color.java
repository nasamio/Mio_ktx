/**
 * 
 */
package net.sourceforge.capcode.S57Library.basics;

import java.awt.Color;

/**
 * @author cyrille
 *
 */
public class S52Color {
	public static final Color deepWaterColor = byAcronym("DEPDW");
	public static final Color mediumShallowWater = byAcronym("DEPMS");
	public static final Color veryShallowWaterColor = byAcronym("DEPVS");
	public static final Color mediumDeepWaterColor = byAcronym("DEPMD");
	public static final Color intertidalColor = byAcronym("DEPIT");
	
	public static final Color landColor = byAcronym("LANDA");
	public static final Color buildUpArea = byAcronym("CHBRN");
	public static final Color buildingColor = byAcronym("LANDF");
	
	public static final Color mainRoadColor = byAcronym("TRFCD");
	public static final Color coastLineColor = byAcronym("CSTLN");
	public static final Color depthContourColor = byAcronym("DEPCN");
	
	public static final Color greenGeneral = byAcronym("CHGRN");
	public static final Color redGeneral = byAcronym("CHRED");
	public static final Color yellowGeneral = byAcronym("CHYLW");

	public static final Color textGray = byAcronym("UINFF");
	public static final Color black = byAcronym("BKAJ1");


	//code as read in S-57 Files
	public static final int WHITE = 1; //CHWHT
	public static final int BLACK = 2;//BKAJ1
	public static final int RED = 3;//CHRED
	public static final int GREEN = 4;//CHGRN
	public static final int BLUE = 5;//
	public static final int YELLOW = 6;//CHYLW
	public static final int GRAY = 7;//CHGRD
	public static final int BROWN = 8;//CHBRN
	public static final int AMBER = 9;
	public static final int VIOLET = 10;
	public static final int ORANGE = 11;//CHCOR
	public static final int MAGENTA = 12;//CHMGF
	public static final int PINK = 13;

	public enum E_S52Codes{
		ADINF(184,172,60),//ADINF yellow mariner's and manufacturer's added information
		APLRT(230,121,56),//APLRT orange alternate planned route.
		ARPAT(82,153,60),//ARPAT blue-green ARPA target and vector.
		BKAJ1(0,0,0),//BKAJ1 black black colour of black-adjust symbol
		BKAJ2(0,0,0),//BKAJ2 grey dark grey colour of black-adjust symbol
		CHBLK(0,0,0),//CHBLK black/white general.
		CHBRN(182,157,64),//CHBRN brown built-up land areas, etc.
		CHCOR(230,121,56),//CHCOR orange manual chart corrections made by the mariner.
		CHGRD(136,152,139),//CHGRD grey, conspic general.
		CHGRF(171,192,177),//CHGRF grey, faint general.
		CHGRN(124,240,91),//CHGRN green general, including buoys.
		CHMGD(198,77,187),//CHMGD magenta,conspic
		CHMGF(212,177,221),//CHMGF magenta, faint less important magenta chart features.
		CHRED(238,90,108),//CHRED red general, including buoys.
		CHWHT(244,244,225),//CHWHT white/white general.
		CHYLW(243,229,77),//CHYLW yellow general, including buoys.
		CSTLN(95,106,96),//CSTLN grey, conspic coastline (high water line), including wharf and dock faces.
		CURSR(230,121,56), //CURSR orange cursor, also mariner's EBLs and VRMs.
		DEPCN(136,152,139),//DEPCN grey other depth contours
		DEPDW(216,244,225),//DEPDW white/black area fill colour for deep water, depth greater than the deep contour selected by the mariner.
		DEPIT(143,191,147),//DEPIT yellow-green area fill for the intertidal zone between the drying line and the high water line.
		DEPMD(192,225,214),//DEPMD blue area fill for medium-deep water, deeper than the Safety Contour.
		DEPMS(162,210,229),//DEPMS blue area fill for medium-shallow water, less than the Safety Contour.
		DEPSC(95,106,96),//DEPSC grey, conspic own-ship's Safety Contour, selected by the mariner.
		DEPVS(129,195,226),//DEPVS blue area fill for very shallow water, less than the shallow contour.
		DNGHL(238,90,108),//DNGHL red danger highlight symbol, applied by the mariner.
		ISDNG(198,77,187),//ISDNG magenta, conspic isolated danger, selected automatically by ECDIS depending on the Safety Contour selected by the mariner.
		LANDA(204,197,123),//LANDA brown area fill for land that is not built-over.
		LANDF(147,116,39),//LANDF brown, conspic contrasting brown for land features (buildings, dykes etc.)
		LITGN(124,240,91),//LITGN green light flares and sectors.
		LITRD(238,90,108),//LITRD red light flares and sectors.
		LITYW(243,229,77),//LITYW yellow light flares and sectors.
		NINFO(230,121,56),//NINFO orange navigator's information added to the chart by the mariner.
		NODTA(171,192,177), //NODTA grey area for which there is no chart information.
		OUTLL(204,197,123),//OUTLL brown outline colour to clarify overwritten symbols on land areas.
		OUTLW(0,0,0),//OUTLW black outline colour to clarify overwritten symbols on water areas.
		PLRTE(218,70,45),//PLRTE red, conspic own-ship's planned route.
		PSTRK(0,0,0),//PSTRK black/white own-ship's past track from primary positioning system.
		RADHI(82,153,60),//RADHI green, conspic high luminance radar colour for high echo intensity.
		RADLO(124,240,91),//RADLO green, faint low luminance radar colour for low echo intensity.
		RES01(171,192,177),//RES01 grey symbol, line or text colour reserved for future applications
		RES02(171,192,177),//RES02 grey symbol, line or text colour reserved for future applications
		RES03(171,192,177),//RES03 grey symbol, line or text colour reserved for future applications
		RESBL(76,135,227),//RESBL blue symbol, line or text colour reserved for AIS and VTS
		RESGR(136,152,139),//RESGR grey symbol, line or text colour reserved for future applications
		SCLBR(230,121,56),//SCLBR orange, white 1 mile vertical bar to give general impression of display scale.
		SHIPS(0,0,0),//SHIPS black/white own-ship symbol or scaled shape, with velocity vectors.
		SNDG1(136,152,139),//SNDG1 grey soundings deeper than the mariner-selected Safety Depth.
		SNDG2(0,0,0),//SNDG2 black/white dangerous soundings, equal to or less than the Safety Depth.
		SYTRK(136,152,139),//SYTRK grey own-ship's past track from secondary positioning system.
		TRFCD(198,77,187),//TRFCD magenta,conspic important traffic routeing features.
		TRFCF(212,177,221),//TRFCF magenta less important routeing features.
		UIAFD(220,220,220),//UIAFD blue area fill for use on user interface, water colour.
		UIAFF(185,185,185),//UIAFF brown area fill for use on user interface, land colour.
		UIBCK(150,150,150),//UIBCK white/black user interface background.
		UIBDR(125,125,125),//UIBDR grey border to separate user interface from ECDIS chart display.
		UINFB(54,130,244),//UINFB blue colour for symbols, lines and text on the user interface
		UINFD(0,0,3),//UINFD grey, conspic conspic colour for limited amount of important text.
		UINFF(240,240,240),//UINFF grey normal colour for user interface text.
		UINFG(75,210,86),//UINFG green colour for symbols, lines and text on the user interface
		UINFM(200,72,201),//UINFM magenta colour for symbols, lines and text on the user interface
		UINFO(255,240,70),//UINFO orange colour for symbols, lines and text on the user interface
		UINFR(251,95,99);//UINFR red colour for symbols, lines and text on the user interface
		public int red, green, blue;
		E_S52Codes(int r, int g, int b){
			red = r;
			green = g;
			blue = b;
		}
	}




	Color color;
	public S52Color(int code){
		color = byCode(code);
	}

	public static Color byAcronym(String acronym){
		E_S52Codes e = E_S52Codes.valueOf(acronym);
		if (e != null){
			return new Color(e.red, e.green, e.blue);
		}
		return null;		
	}
	
	public static Color byAcronym(String acronym, int alpha){
		E_S52Codes e = E_S52Codes.valueOf(acronym);
		if (e != null){
			return new Color(e.red, e.green, e.blue, alpha);
		}
		return null;		
	}
	
	/**
	 * returns a color given by its code
	 * WHITE = 1; //CHWHT
	 * BLACK = 2;//BKAJ1
	 * RED = 3;//CHRED
	 * GREEN = 4;//CHGRN
	 * BLUE = 5;//
	 * YELLOW = 6;//CHYLW
	 * GRAY = 7;//CHGRD
	 * BROWN = 8;//CHBRN
	 * AMBER = 9;
	 * VIOLET = 10;
	 * ORANGE = 11;//CHCOR
	 * MAGENTA= 12;//CHMGF
	 * PINK = 13;
	*
	 * @param code
	 * @return
	 */
	public static Color byCode(int code) {
		Color color = Color.black;
		switch (code){
		case WHITE: 
			color = byAcronym("CHWHT");
			break;
		case BLACK: 
			color = byAcronym("BKAJ1");
			break;
		case RED: 
			color = byAcronym("CHRED");
			break;
		case GREEN: 
			color = byAcronym("CHGRN");
			break;
		case BLUE: 
			color = Color.blue;
			break;
		case YELLOW: 
			color = byAcronym("CHYLW");
			break;
		case GRAY: 
			color = byAcronym("CHGRD");
			break;
		case BROWN: 
			color = byAcronym("CHBRN");
			break;
		case AMBER: 
			color = new Color(255,153,51);//amber
			break;
		case VIOLET: 
			color = new Color(153,0,153);//violet
			break;
		case ORANGE: 
			color = byAcronym("CHCOR");
			break;
		case MAGENTA: 
			color = byAcronym("CHMGF");
			break;
		case PINK: 
			color = new Color(255,102,153); //pink
			break;
		}	
		return color;
	}

}

/**
 * 
 */
package net.sourceforge.capcode.S57Library.fiedsRecords;

/**
 * @author cyrille
 *
 */
public class S57SubFieldFormat {
	public enum FormatType{
		noFormat,
		ascii,
		integer,
		real,
		binaryUnsignedInteger,
		binarySignedInteger,
		binaryFixedPoint,
		binaryFloatingPoint,
		binaryComplex		
	}
	FormatType type = FormatType.ascii;
	int length = 1;

	public S57SubFieldFormat(FormatType tp, int lg){
		type = tp;
		length = lg;
	}
	public static S57SubFieldFormat decode(String formatAsString){
		if (formatAsString.equals("b4")){
			formatAsString = "b24";
		}
		if (formatAsString.equals("b1")){
			formatAsString = "b12";
		}
		FormatType type = FormatType.noFormat;
		int length = -1; // default meaning for unspecified size (FT terminated)
		
		String sizeAsString = "0";
		int pos1 = formatAsString.indexOf('(');
		if (pos1 != -1){
			int pos2 = formatAsString.indexOf(')');
			sizeAsString = formatAsString.substring(pos1+1, pos2);
			length = Integer.parseInt(sizeAsString);
		}
		char code = formatAsString.charAt(0);
		switch (code){
		case 'A':
			type = FormatType.ascii;
			break;
		case 'I':
			type = FormatType.integer;
			break;
		case 'R':
			type = FormatType.real;
			break;
		case 'B':
			type = FormatType.binaryUnsignedInteger;
			length = Integer.parseInt(sizeAsString) / 8;
			break;
		case 'b':
			char binaryCode = formatAsString.charAt(1);
			switch (binaryCode){
			case '0':
				type = FormatType.noFormat;
				break;
			case '1':
				type = FormatType.binaryUnsignedInteger;
				break;
			case '2':
				type = FormatType.binarySignedInteger;
				break;
			case '3':
				type = FormatType.binaryFixedPoint;
				break;
			case '4':
				type = FormatType.binaryFloatingPoint;
				break;
			case '5':
				type = FormatType.binaryComplex;
				break;			
			default:
				type = FormatType.noFormat;
				length = 0;				
			}
			if (formatAsString.length() > 2){
				length = formatAsString.charAt(2)-48;
			}else{
				length = -1;
			}
		}
		return new S57SubFieldFormat(type, length);

	}
}

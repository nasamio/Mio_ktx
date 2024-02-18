/**
 * 
 */
package net.sourceforge.capcode.S57Library.catalogs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

import net.sourceforge.capcode.S57Library.Activator;
import net.sourceforge.capcode.S57Library.basics.SystemUtils;

/**
 * @author cyrille
 * 
 */
public class S57CatalogExpectedValues extends Vector<S57ExpectedValue> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * the static list of all possible S57 expectd values.
	 */
	public static S57CatalogExpectedValues list;
	static {
		try {
			list = new S57CatalogExpectedValues("/data/s57expectedinput.csv");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public S57CatalogExpectedValues(String fileName)
			throws FileNotFoundException {
		super(200);
		Scanner scanner = new Scanner(getClass().getResourceAsStream(fileName));
		// skip header
		scanner.nextLine();
		while (scanner.hasNextLine()) {
			String[] fields = scanner.nextLine().split(",");
			int attr = fields.length > 0 ? Integer.parseInt(fields[0]) : 0;
			int code = fields.length > 1 ? Integer.parseInt(fields[1]) : 0;
			String value = fields.length > 2 ? fields[2].replaceAll("\"", "")
					: "";
			String abrev = fields.length > 3 ? fields[3].replaceAll("\"", "")
					: "";
			S57ExpectedValue v = new S57ExpectedValue(attr, code, value, abrev);
			super.add(v);
		}
		scanner.close();
	}

	/**
	 * @param attribute
	 *            : the code of the S57 attribute(eg: 113)
	 * @param code
	 *            : the code of the expected input id for this attribute
	 * @return a String giving the meaning of the code
	 */
	public static String getByCode(int attribute, int code) {
		for (S57ExpectedValue element : list) {
			if (element.attribute == attribute && element.code == code) {
				return element.meaning;
			}
		}
		return null;
	}

	/**
	 * @param attribute
	 *            : the code of the S57 attribute(eg: 113)
	 * @param code
	 *            : the code of the expected input id for this attribute
	 * @return a String giving the meaning of the code
	 */
	public static String getAbreviationByCode(int attribute, int code) {
		for (S57ExpectedValue element : list) {
			if (element.attribute == attribute && element.code == code) {
				return element.abrev;
			}
		}
		return null;
	}

}

package net.sourceforge.capcode.S57Library.catalogs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

@SuppressWarnings("serial")
public class S57ObjectsCatalog extends Vector<E_S57Object> {
	public static S57ObjectsCatalog list;
	static {
		try {
			list = new S57ObjectsCatalog("/data/s57objectclasses.csv");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public S57ObjectsCatalog(String fileName) throws FileNotFoundException {
		super(180);
		Scanner scanner = new Scanner(getClass().getResourceAsStream(fileName));
		// skip header
		scanner.nextLine();
		while (scanner.hasNextLine()) {
			String[] fields = scanner.nextLine().split(",");
			E_S57Object o = new E_S57Object(
					Integer.parseInt(fields[0]),
					fields[1].replaceAll("\"", ""),
					fields[2].replaceAll("\"", ""));
			super.add(o);
		}
		scanner.close();
	}

	public static E_S57Object getByCode(int code) {
		for (E_S57Object o : list) {
			if (o.code == code) {
				return o;
			}
		}
		return new E_S57Object(code, "unknown", "");
	}
}

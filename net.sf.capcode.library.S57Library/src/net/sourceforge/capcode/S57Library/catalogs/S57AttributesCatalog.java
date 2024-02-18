package net.sourceforge.capcode.S57Library.catalogs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

import net.sourceforge.capcode.S57Library.Activator;
import net.sourceforge.capcode.S57Library.basics.SystemUtils;

@SuppressWarnings("serial")
/**
 * read the attributes that MUST be in data/s57attributes.csv.
 * The access to the attributes is done via the static object S57AttributesCatalog.list
 * to get a particular attribute perform a S57AttributesCatalog.list.geetByCode(int)
 */
public class S57AttributesCatalog extends Vector<S57CatalogAttributeElement>{
	/** 
	 * the static list of all possible S57 attribute. 
	 * @see S57AttributesCatalog
	 */
	public static S57AttributesCatalog list;
	static{
		try {
			list = new S57AttributesCatalog( "/data/s57attributes.csv");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
	}
	
	public S57AttributesCatalog(String fileName) throws FileNotFoundException{
		super(200);
		Scanner scanner = new Scanner(getClass().getResourceAsStream(fileName));
		//skip header
		scanner.nextLine();
		while(scanner.hasNextLine()){
			String[] fields = scanner.nextLine().split(",");	
			S57CatalogAttributeElement a = new S57CatalogAttributeElement(Integer.parseInt(fields[0]), fields[1].replaceAll("\"", ""), fields[2].replaceAll("\"", ""));
			super.add(a);
		}
		scanner.close();
	}
	/**
	 * @param code : the code of the S57 attribute to find (eg: 113)
	 * @return a S57CatalogAttributeElement enumerated item (eg: "Nature of Surface")
	 */
	public static S57CatalogAttributeElement getByCode(int code){
		for (S57CatalogAttributeElement a : list){
			if (a.code == code){
				return a;
			}
		}
		return null;
	}
}

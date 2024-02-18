S57Library.jar, a free library under a GNU GPL Licence.
Version alpha (no reference yet)

1) LICENCE

2) IMPORTANT NOTICE

3) HOWTO

1) LICENCE
a copy of the GNU GPL licence can be obtained at:
www.gnu.org/copyleft/gpl.html

2)IMPORTANT NOTICE
You acknowledge that using this software for the purpose of navigation does not
engage the authors responsabilities in case of damage, injuries or casualities due
to the use of this software.
This software MUST NOT BE USED as a means of navigation, weather prediction, 
tide and currents prediction or any other critical sailing activities as it was 
not developed for that purpose.
Use of this software is done at your own risks. 
 
Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

-Redistribution of source code must retain the above copyright notice, this
list of conditions and the following disclaimer.

-Redistribution in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

-The source code is provided in the jar

- Neither the name of "Capcode a free software suite for sailor" or the names
 of authors may be used to endorse or promote products derived from this 
 software without specific prior written permission.

- This software is provided "AS IS," without a warranty of any kind. ALL
EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
NON-INFRINGEMENT, ARE HEREBY EXCLUDED. CAPCODE AND ITS
LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A
RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.

IN NO EVENT WILL "CAPCODE A FREE SOFTWARE FOR SAILORS" OR ITS LICENSORS 
BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, 
CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS 
OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, 
EVEN IF THE AUTHORS HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.

3) HOWTO
Installation:
Copy the jar in the classpath of your application

Usage:
this library can only decode the structure of a S57 file.
To use it:
download an ENC chart from NOAA server (eg: US3EC09M.000)
use the class S57ModuleReader as follow:

		mod = new S57ModuleReader();
		mod.load("test/data/US3EC09M.000");		
or 
		mod = new S57ModuleReader("<path to your files>/US3EC09M.000");

you can access all the feature with
		mod.getFeatures();
which returns a Vector<S57Objects>
then you can:
for (S57Feature feature : mod.getFeatures()){
	//to get all the attributes
	feature.attributes;
	//to get all the linked spatial object
	feature.linkedFeatures;
	//to get the international name of the feature (may be empty))
	feature.intName;
	//to get information on the object (see E_S57Object)	
	feature.object;
	feature.objectPrimitiveType;
}		

for a catalog 
today you need to create a new class as follow:

private class S57ModuleReaderCatalog extends S57ModuleReader{

//you must override the following method
	@Override
	public void newCatEntryCallBack(S57FieldCATD catd){
	// you get the file name in catd.longFileName
	// you get the sector of the chart in the following field
	//catd.northLat, catd.westLon, catd.southLat, catd.eastLon
	//you can now add an entry in a Vector<yourCatalogEntryClass>
	}
}

module = new S57ModuleReaderCatalog("<path to your files>/CATALOG.031");
		
		
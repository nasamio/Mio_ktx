package net.sourceforge.capcode.S57Library.basics;

public enum E_S57RecordType {
	datasetGeneralInformation("DS",10),
	dataSetGeographicReference("DP",20),
	dataSetHistory("DH",30),
	dataSetAccuracy("DA",40),
	catalogueDirectory("CD", 0),
	catalogueCrossReference("CR",60),
	dataDictionaryDefinition("ID",70),
	dataDictionaryDomain("IO",80),
	dataDictionarySchema("IS",90),
	feature("FE",100),
	vectorIsolatedNode("VI", 110),
	vectorConnectedNode("VC", 120),
	vectorEdge("VE", 130),
	vectorFace("VF", 140);
	
	public String asciiCode;
	public int code;
	E_S57RecordType(String asciiCode, int code){
		this.asciiCode = asciiCode;
		this.code = code;
	}
	public String toString(){
		return String.format("%s[%s](%d)", name(), asciiCode, code);
	}
	
	public static E_S57RecordType byCode(Integer code) {
		for (E_S57RecordType tp:E_S57RecordType.values()){
			if (tp.code == code){
				return tp;
			}
		}
		return null;
	}
}

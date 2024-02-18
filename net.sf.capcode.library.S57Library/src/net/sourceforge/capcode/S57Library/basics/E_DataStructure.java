/**
 * 
 */
package net.sourceforge.capcode.S57Library.basics;

/**
 * @author cyrille
 *
 */
public enum E_DataStructure {
	unspecified,
	/**A set of isolated nodes and edges. Edges do not reference nodes. 
	 * Feature objects must not share spatial objects. 
	 * Point representations are coded as isolated nodes. 
	 * Line representations are coded as connected series of edges. 
	 * Area representations are coded as closing loops of edges. 
	 * If logical consistency is required, coincident edges must contain identical geometry.*/
	cartographic_spagetti,
	/**Data structure in which the geometry is described in terms of edges, isolated 
	 * nodes and connected nodes. Edges and connected nodes are topologically linked. 
	 * Nodes are explicitely coded in the data structure.*/
	chain_node,
	planar_graph,
	full_topology,
	not_relevant;
;

	public static E_DataStructure byCode(int code) {
		for (E_DataStructure d:E_DataStructure.values()){
			if (d.ordinal() == code){
				return d;
			}			
		}
		return null;
	}
}

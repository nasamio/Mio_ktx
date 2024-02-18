package net.sourceforge.capcode.S57Library.objects;

import java.awt.Shape;
import java.awt.geom.Path2D;
import java.util.Vector;

import net.sourceforge.capcode.S57Library.basics.Link;
import net.sourceforge.capcode.S57Library.basics.S57Pos2D;

/**
 * A one-dimensional spatial object, located by two or more coordinate pairs (or
 * two connected nodes) and optional interpolation parameters. If the parameters
 * are missing, the interpolation is defaulted to straight line segments between
 * the coordinate pairs. In the chain-node, planar graph and full topology data
 * structures, an edge must reference a connected node at both ends and must not
 * reference any other nodes
 */
public class S57Edge extends S57Spatial {
	public S57ConnectedNode begin;
	public S57ConnectedNode end;

	@Override
	public Shape getPath() {
		boolean started = false;
		int startingPoint = 0; // used in case of absence of start node
		if (path == null) {
			path = new Path2D.Float();
			// should start with a begin node
			if (begin != null) {
				// the path starts
				path.moveTo(begin.coordinate.longitude,
						begin.coordinate.latitude);
				started = true;
				// case where no starting node is defined
			} else if (positions != null) {
				S57Pos2D p = positions.elementAt(0);
				path.moveTo(p.longitude, p.latitude);
				started = true;
				startingPoint = 1;
			}

			// now iterate on the other points starting at the next point
			if (positions != null) {
				for (int i = startingPoint; i < positions.size(); i++) {
					S57Pos2D p = positions.elementAt(i);
					path.lineTo(p.longitude, p.latitude);
				}
			}
			// all edge should have an end
			if (end != null) {
				// line to only if it has started
				if (started) {
					path.lineTo(end.coordinate.longitude,
							end.coordinate.latitude);
				}
			}
		}
		return path;
	}

	@Override
	public Shape getInversedPath() {
		boolean started = false;
		int startingPoint = 0; // used in case of absence of start node
		if (inversedPath == null) {
			inversedPath = new Path2D.Float();
			// should start with a begin node
			if (end != null) {
				// the path starts
				inversedPath.moveTo(end.coordinate.longitude,
						end.coordinate.latitude);
				started = true;
				// case where no starting node is defined
			} else if (positions != null) {
				S57Pos2D p = positions.lastElement();
				inversedPath.moveTo(p.longitude, p.latitude);
				started = true;
				startingPoint = 1;
			}

			// now iterate on the other points starting at the next point
			if (positions != null) {
				for (int i = positions.size()-1; i >= startingPoint; i--) {
					S57Pos2D p = positions.elementAt(i);
					inversedPath.lineTo(p.longitude, p.latitude);
				}
			}
			// all edge should have an end
			if (begin != null) {
				// line to only if it has started
				if (started) {
					inversedPath.lineTo(begin.coordinate.longitude,
							begin.coordinate.latitude);
				}
			}
		}
		return inversedPath;
	}

	@Deprecated
	public Shape getInversedPathOld() {
		if (inversedPath == null && begin != null && end != null) {
			inversedPath = new Path2D.Float();
			inversedPath.moveTo(end.coordinate.longitude,
					end.coordinate.latitude);

			if (positions != null) {
				for (int i = positions.size() - 1; i >= 0; i--) {
					S57Pos2D p = positions.elementAt(i);
					inversedPath.lineTo(p.longitude, p.latitude);
				}
			}

			inversedPath.lineTo(begin.coordinate.longitude,
					begin.coordinate.latitude);
		}
		return inversedPath;
	}

	public void addVectors(Vector<Link> links, S57ObjectsVector vectors) {
		Link l1 = links.elementAt(0);
		Link l2 = (links.size() > 1) ? links.elementAt(1) : links.elementAt(0);
		begin = (S57ConnectedNode) (l1.isBeginingNode() ? vectors.searchByCode(l1.name) : null);
		end = (S57ConnectedNode) (l2.isEndNode() ? vectors.searchByCode(l2.name) : null);
	}

}

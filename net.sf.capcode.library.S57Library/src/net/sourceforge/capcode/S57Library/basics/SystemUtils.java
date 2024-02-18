/**
 * CVS markups
 * last update by : $Author: crosay $ 
 * $Date: 2009-12-01 22:19:46 $ 
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/common/SystemUtils.java,v 1.37 2009-12-01 22:19:46 crosay Exp $
 * $Log: not supported by cvs2svn $
 * Revision 1.36  2009/11/02 00:10:37  crosay
 * Incomplete # add AIS capability
 * http://sourceforge.net/apps/trac/capcode/ticket/42
 * Complete # required by ticket 6: install a change waypoint event mechanism
 * http://sourceforge.net/apps/trac/capcode/ticket/37
 * Complete # when importing a polar, ask also to save it under capcode format.
 * http://sourceforge.net/apps/trac/capcode/ticket/40
 * Incomplete # wordwind plugin for capcode
 * http://sourceforge.net/apps/trac/capcode/ticket/6
 *
 * Revision 1.35  2009/09/03 21:49:20  crosay
 * Incomplete # change the logging of event
 * http://sourceforge.net/apps/trac/capcode/ticket/33
 *
 * Revision 1.34  2009/08/31 22:18:08  crosay
 * Incomplete # preparation for delivery to version 0.9.3
 * http://sourceforge.net/apps/trac/capcode/ticket/17
 *
 * Revision 1.33  2009/08/29 19:24:26  crosay
 * Complete # move the display of the target VMG arrow to the polar on map plugin
 * http://sourceforge.net/apps/trac/capcode/ticket/29
 *
 * Revision 1.32  2009/08/28 23:33:30  crosay
 * Complete # add reference points to maps
 * http://sourceforge.net/apps/trac/capcode/ticket/22
 *
 * Revision 1.31  2009/08/14 12:49:09  crosay
 * Complete # add a close button on all internal frames
 * http://sourceforge.net/apps/trac/capcode/ticket/30
 *
 * Revision 1.30  2009/07/19 09:18:00  crosay
 * Incomplete - task optimisation of the display
 * added defensive code (checking null values)
 *
 * Revision 1.29  2009/03/31 07:42:07  crosay
 * Incomplete - taskSourceForge.net: CapCode-software suite for sailors: Detail: 2705654 - NMEA data mapping (FR 2605966 Input Mapping)
 * https://sourceforge.net/tracker2/index.php?func=detail&aid=2705654&group_id=100698&atid=1045270
 *
 * Revision 1.28  2009/03/16 17:13:05  crosay
 * Complete - taskexternalise strings
 *
 * Revision 1.27  2009/03/04 10:15:55  crosay
 * Complete - taskdisplay the equipments connected to PC
 *
 * Complete - taskexternalise strings
 *
 * Incomplete - tasknew plugin: bearing-speed-and log
 *
 * Revision 1.26  2009/02/22 11:41:40  crosay
 * Incomplete - task2024408 - FR 1909997 version control
 * https://sourceforge.net/tracker2/?func=detail&aid=2024408&group_id=100698&atid=1045270
 * Complete - taskimplement the possibility to change perspective
 *
 * Revision 1.25  2009/02/20 22:26:37  crosay
 * Incomplete - taskimplement the possibility to change perspective
 *
 * Revision 1.24  2009/01/29 10:31:28  crosay
 * Incomplete - task add gps hypbrid mode (in true wind computation) task 2533241 GPS speed spare mode
 * https://sourceforge.net/tracker2/?func=detail&aid=2533241&group_id=100698&atid=1045270
 * Complete - task save status and position of toolbars
 * https://sourceforge.net/tracker2/?func=detail&aid=2531530&group_id=100698&atid=1045270
 *
 * Revision 1.23  2009/01/25 15:52:00  crosay
 * Incomplete - taskpreferences for windows bug
 *
 * Revision 1.22  2009/01/24 20:52:36  crosay
 * Complete - taskRemoval of sqlite
 *
 * Complete - tasksuppression of debug display for preference saving
 *
 * Complete - tasktask 2531747 animation of the wearther
 * https://sourceforge.net/tracker2/?func=detail&aid=2024403&group_id=100698&atid=1045270
 *
 * Revision 1.21  2009/01/21 22:11:21  crosay
 * added initializeSystem (must be called by Application and test cases)
 *
 * Revision 1.20  2009/01/20 23:02:57  crosay
 * corrected the bug related to shared parameters
 *
 * Revision 1.19  2009/01/13 18:04:49  crosay
 * correction of code after unit test:
 * - SystemUtils: added exception in case of property not found
 * - SpeedPolar, SpeedPolarFile, WindandSpeedProcess: management of this exception
 *
 * Correction of a bug in Coords management (DMS function)
 *
 * Revision 1.18  2008/12/31 15:03:53  crosay
 * Complete - taskbug 2470945: tracks not saved
 *
 * Revision 1.17  2008/12/11 16:53:44  crosay
 * Complete - tasksuppression of debug display for preference saving
 *
 * Revision 1.16  2008/10/10 20:56:16  crosay
 * upgrade of preference dialog
 *
 * Revision 1.15  2008/09/17 10:58:41  crosay
 *  2081226 "preferences":
 * added the JComboBox and enum capability
 *
 * Revision 1.14  2008/09/12 19:23:21  crosay
 *  2081226 "preferences":
 * the root preference directory is an option
 *
 * Revision 1.13  2008/09/11 21:01:29  crosay
 *  2081226 "preferences":
 *  when a preference file does not exist, create it with the default values, but do not include the window size (could be 0-0)
 *
 * Revision 1.12  2008/09/11 15:23:59  crosay
 * implementation of  task 2081226 "preferences"
 *
 * Revision 1.11  2008/06/18 19:46:24  crosay
 * complementing FR  1961649  	  plugin for race tracks
 * added the capability to:
 * - insert a waypoint
 * - delete a waypoint
 * - create and save a new track
 *
 * Revision 1.10  2008/05/28 16:28:55  crosay
 * added CVS markups in source code
 *
 * $Revision: 1.37 $ 
 * $State: Exp $ 
 */
package net.sourceforge.capcode.S57Library.basics;

import java.awt.Color;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;


public class SystemUtils {
	/**
	 * initialize the system with required parameters.<br>
	 * file.encoding = UTF-8<br>
	 */
	public static void initializeSystem() {
		System.setProperty("file.encoding", "UTF-8"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * changes the path in to standard path system ("using "/" instead of "\" in some systems.
	 * @param path the original path with some potential "\"
	 * @return the path with only "/" separators
	 */
	public static String setToStandardPath(String path) {
		return path.replace(System.getProperty("file.separator"), "/"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * decode a color code given as #aarrggbb where:<br>
	 * aa is the alpha<br>
	 * rr the red<br>
	 * gg the green<br>
	 * bb the blue<br>
	 * @param s
	 * @return a Color
	 */
	public static Color decodeColorLong(String s){
		Long l = Long.decode(s);
		int alpha = (int) ((l >> 24) & 0xff);
		int red = (int) ((l>>16) & 0xff);
		int green = (int) ((l>>8) & 0xff);
		int blue = (int) (l & 0xff);
		return new Color(red, green, blue, alpha);					
	}

	public static URL getUrlLocalToPlugin(String pluginId, String localPath){
		Bundle bundle = Platform.getBundle(pluginId);
		Path path = new Path(localPath);
		URL url = FileLocator.find(bundle, path, null);
		try {
			url = FileLocator.resolve(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return url;
	}

}

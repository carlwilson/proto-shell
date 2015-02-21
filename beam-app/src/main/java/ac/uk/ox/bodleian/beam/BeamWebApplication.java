/**
 * 
 */
package ac.uk.ox.bodleian.beam;

import java.io.File;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * TODO JavaDoc for BeamWebApplication.</p> TODO Tests for
 * BeamWebApplication.</p> TODO Implementation for BeamWebApplication.</p>
 * 
 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 *          Created 21 Sep 2012:12:02:19
 */

public class BeamWebApplication {
	private static final String JETTY_HOME_KEY = "jetty.home";
	private static final File JETTY_HOME;
	static {
		String jettyPath = System.getProperty(JETTY_HOME_KEY, "./jetty");
		File jettyHome = new File(jettyPath); 
		jettyPath = FilenameUtils.normalize(jettyHome.getAbsolutePath());
		JETTY_HOME = new File(jettyPath);
		if (!JETTY_HOME.exists()) {
			if (!JETTY_HOME.mkdirs()) {
				throw new IllegalStateException("Cannot create JETTY home:" + JETTY_HOME.getAbsolutePath());
			}
		}
		System.setProperty(JETTY_HOME_KEY, JETTY_HOME.getAbsolutePath());
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		System.out.println(JETTY_HOME.getAbsolutePath());
		System.out.println(System.getProperty(JETTY_HOME_KEY));
		Server server = new Server(8080);
		WebAppContext webapp = new WebAppContext();
		String warPath = JETTY_HOME.getAbsolutePath() + "/webapp/beam-webapp-0.3.5-SNAPSHOT.war"; 
		webapp.setWar(warPath);
		server.setHandler(webapp);
		server.start();
		server.join();
	}

}

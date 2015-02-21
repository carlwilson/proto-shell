/**
 * 
 */
package uk.ac.ox.bodleian.beam;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * TODO JavaDoc for BeamMain.</p>
 * TODO Tests for BeamMain.</p>
 * TODO Implementation for BeamMain.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 11 Oct 2012:23:00:24
 */

public class BeamMain {

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
		webapp.setDescriptor(webapp+"/WEB-INF/web.xml");
        webapp.setResourceBase("../beam-webapp/src/main/webapp");
        webapp.setContextPath("/");
        webapp.setParentLoaderPriority(true);
		server.setHandler(webapp);
		server.start();
		server.join();
	}


}

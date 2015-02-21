/**
 * 
 */
package uk.ac.ox.bodleian.beam.views;

import uk.ac.ox.bodleian.beam.model.system.JvmDetails;
import uk.ac.ox.bodleian.beam.model.system.OsDetails;
import uk.ac.ox.bodleian.beam.model.system.ServerDetails;
import uk.ac.ox.bodleian.beam.services.auth.Users;
import uk.ac.ox.bodleian.beam.services.auth.User;

import com.yammer.dropwizard.views.View;

/**
 * TODO JavaDoc for ApplicationView.</p>
 * TODO Tests for ApplicationView.</p>
 * TODO Implementation for ApplicationView.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 14 Oct 2012:02:10:14
 */

public final class ApplicationView extends View {
	private final static User user = Users.getCurrentUser();
	private final static OsDetails os = OsDetails.getInstance();
	private final static JvmDetails jvm = JvmDetails.getInstance();
	private final static ServerDetails server = ServerDetails.getInstance();

	/**
	 * @param templateName the name of the Freemarker template to load
	 */
	private ApplicationView(final String templateName) {
		super(templateName);
	}
	
	/**
	 * @param templateName
	 * @return a new ApplicationView instance processing the passed template
	 */
	public static final ApplicationView getNewInstance(final String templateName) {
		return new ApplicationView(templateName);
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}
	
	/**
	 * @return the operating system details
	 */
	public OsDetails getOs() {
		return os;
	}
	
	/**
	 * @return the details of the jvm
	 */
	public JvmDetails getJvm() {
		return jvm;
	}
	
	/**
	 * @return the details of the server
	 */
	public ServerDetails getServer() {
		return server;
	}
}

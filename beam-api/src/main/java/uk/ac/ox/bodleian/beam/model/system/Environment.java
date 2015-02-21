/**
 * 
 */
package uk.ac.ox.bodleian.beam.model.system;



/**
 * TODO JavaDoc for Environment.</p>
 * TODO Tests for Environment.</p>
 * TODO Implementation for Environment.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 14 Sep 2012:10:42:22
 */
public class Environment {
	private final ServerDetails hardware = ServerDetails.getInstance();
	private final OsDetails os = OsDetails.getInstance();
	private final JvmDetails java = JvmDetails.getInstance();

	Environment() {
		// Do nothing
	}
	
	/**
	 * @return the Server details
	 */
	public ServerDetails getServer() {
		return this.hardware;
	}
	
	/**
	 * @return the os details
	 */
	public OsDetails getOS() {
		return this.os;
	}
	/**
	 * @return the Java details
	 */
	public JvmDetails getJava() {
		return this.java;
	}
}

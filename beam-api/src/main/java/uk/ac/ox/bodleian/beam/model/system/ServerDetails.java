/**
 * 
 */
package uk.ac.ox.bodleian.beam.model.system;

import org.opf_labs.spruce.utils.Environments;


/**
 * TODO JavaDoc for ServerDetails.</p>
 * TODO Tests for ServerDetails.</p>
 * TODO Implementation for ServerDetails.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 14 Sep 2012:09:58:24
 */
public class ServerDetails {
	private static final ServerDetails INSTANCE = new ServerDetails();
	private final String ipAddress;
	private final String hostName;
	private final String machAddress;
	
	private ServerDetails() {
		this.ipAddress=Environments.getHostAddress();
		this.hostName=Environments.getHostName();
		this.machAddress=Environments.getMachAddress();
	}
	
	/**
	 * @return the server details instance
	 */
	public static final ServerDetails getInstance() {
		return INSTANCE;
	}
	
	/**
	 * @return the JVM's host's IP address
	 */
	public String getIpAddress() {
		return this.ipAddress;
	}
	
	/**
	 * @return the JVM's host's name
	 */
	public String getHostName() {
		return this.hostName;
	}
	
	/**
	 * @return the JVM's host's MACH address
	 */
	public String getMachAddress() {
		return this.machAddress;
	}
}

/**
 * 
 */
package uk.ac.ox.bodleian.beam.model.system;

import org.opf_labs.spruce.utils.Environments;


/**
 * TODO JavaDoc for OsDetails.</p>
 * TODO Tests for OsDetails.</p>
 * TODO Implementation for OsDetails.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 14 Sep 2012:10:28:51
 */
public class JvmDetails {
	private final static JvmDetails INSTANCE = new JvmDetails();
	private final String vendor;
	private final String version;
	private final String architecture;
	private final String home;
	
	private JvmDetails() {
		this.vendor = Environments.getJavaVendor();
		this.version = Environments.getJavaVersion();
		this.architecture = Environments.getJavaArch();
		this.home = Environments.getJavaHome();
	}

	/**
	 * @return the JVM details instance
	 */
	public static final JvmDetails getInstance() {
		return INSTANCE;
	}

	/**
	 * @return the Java vendor
	 */
	public String getVendor() {
		return this.vendor;
	}
	
	/**
	 * @return the Java version
	 */
	public String getVersion() {
		return this.version;
	}
	
	/**
	 * @return the Java system architecture
	 */
	public String getArchitecture() {
		return this.architecture;
	}
	
	/**
	 * @return the value of JAVA_HOME
	 */
	public String getHome() {
		return this.home;
	}
}

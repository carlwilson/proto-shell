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
public class OsDetails {
	private final static OsDetails INSTANCE = new OsDetails();
	private final String name;
	private final String version;
	private final String architecture;
	
	private OsDetails() {
		this.name = Environments.getOSName();
		this.version = Environments.getOSVersion();
		this.architecture = Environments.getOSArch();
	}
	
	/**
	 * @return the OS Details instance
	 */
	public final static OsDetails getInstance() {
		return INSTANCE;
	}
	
	/**
	 * @return the os name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @return the os version
	 */
	public String getVersion() {
		return this.version;
	}
	
	/**
	 * @return the os architecture
	 */
	public String getArchitecture() {
		return this.architecture;
	}
}

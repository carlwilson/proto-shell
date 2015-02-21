/**
 * 
 */
package uk.ac.ox.bodleian.beam.services.auth;

/**
 * TODO JavaDoc for User.</p>
 * TODO Tests for User.</p>
 * TODO Implementation for User.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 14 Oct 2012:14:44:00
 */

public interface User {
	/**
	 * @return the user's id
	 */
	public int getId();
	/**
	 *  @return the user's system name
	 */
	public String getName();
	/**
	 * @return the user's first name
	 */
	public String getFirstName();
	/**
	 * @return the user's last name
	 */
	public String getLastName();
	/**
	 * @return the MD5 hash of the user's password
	 */
	public String getPasswordHash();
	/**
	 * @return the user's email address
	 */
	public String getEmail();
}

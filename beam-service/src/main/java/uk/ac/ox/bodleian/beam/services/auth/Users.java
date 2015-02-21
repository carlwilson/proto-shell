/**
 * 
 */
package uk.ac.ox.bodleian.beam.services.auth;



import org.opf_labs.spruce.utils.Environments;

import com.google.common.base.Preconditions;

/**
 * TODO JavaDoc for Users.</p>
 * TODO Tests for Users.</p>
 * TODO Implementation for Users.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 14 Oct 2012:15:32:49
 */

public class Users {
	private static final User CURRENT_USER = newUserFromName(Environments.getUserName());
	private Users() {
		Preconditions.checkState(true, "Users no arg constructor.");
	}
	
	/**
	 * @return the static default user instance
	 */
	public static final User getDefaultUser() {
		return UserImpl.getDefaultUser();
	}
	
	/**
	 * @return the current user, taken from the JVM settings
	 */
	public static final User getCurrentUser() {
		return CURRENT_USER;
	}
	
	/**
	 * @param name the system name of the user
	 * @return a new User instance created from the passed details
	 */
	public static final User newUserFromName(final String name) {
		Preconditions.checkNotNull(name, "name == null");
		Preconditions.checkArgument(!name.isEmpty(), "Argument name cannot be emppty", name);
		return UserImpl.newInstance(name);
	}
	
	/**
	 * @param id of the user
	 * @param name the system name of the user
	 * @param firstName the users first name
	 * @param lastName the users last name
	 * @param passwordHash the hash of the users password
	 * @param email the users email address
	 * @return a new User instance created from the passed details
	 */
	public static final User newUser(final int id, final String name, final String firstName, final String lastName, final String passwordHash, final String email) {
		Preconditions.checkNotNull(name, "name == null");
		Preconditions.checkArgument(!name.isEmpty(), "Argument name cannot be emppty", name);
		Preconditions.checkNotNull(firstName, "firstName == null");
		Preconditions.checkArgument(!firstName.isEmpty(), "Argument firstName cannot be emppty", firstName);
		Preconditions.checkNotNull(lastName, "lastName == null");
		Preconditions.checkNotNull(passwordHash, "passwordHash == null");
		Preconditions.checkArgument(!passwordHash.isEmpty(), "Argument passwordHash cannot be emppty", firstName);
		Preconditions.checkNotNull(email, "email == null");
		return UserImpl.newInstance(id, name, firstName, lastName, passwordHash, email);
	}
}

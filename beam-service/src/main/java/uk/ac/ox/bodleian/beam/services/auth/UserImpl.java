/**
 * 
 */
package uk.ac.ox.bodleian.beam.services.auth;

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
final class UserImpl implements User {
	private static final UserImpl DEFAULT_USER = new UserImpl();
	private final int id;
	private final String name;
	private final String firstName;
	private final String lastName;
	private final String passwordHash;
	private final String email;

	private UserImpl() {
		this(-1, "name", "first name", "last name", "password", "name@domain");
	}
	
	private UserImpl(String name) {
		this(-1, name, "first name", "last name", "password", "name@domain");
	}

	private UserImpl(final int id, final String name, final String firstName, final String lastName, final String passwordHash, final String email) {
		assert name != null : "name null";
		assert firstName != null : "firstName null";
		assert lastName != null : "lastName null";
		assert passwordHash != null : "passwordHash null";
		assert email != null : "email null";
		this.id = id;
		this.name = name;
		this.firstName = firstName;
		this.lastName = lastName;
		this.passwordHash = passwordHash;
		this.email = email;
	}

	/**
	 * @return returns the default user object
	 */
	public static final UserImpl getDefaultUser() {
		return DEFAULT_USER;
	}

	static final UserImpl newInstance(final String name) {
		assert name != null : "name null";
		return new UserImpl(name);
	}

	static final UserImpl newInstance(final int id, final String name, final String firstName, final String lastName, final String passwordHash, final String email) {
		assert name != null : "name null";
		assert firstName != null : "firstName null";
		assert lastName != null : "lastName null";
		assert passwordHash != null : "passwordHash null";
		assert email != null : "email null";
		return new UserImpl(id, name, firstName, lastName, passwordHash, email);
	}

	@Override
	public final int getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public final String getFirstName() {
		return this.firstName;
	}
	
	@Override
	public final String getLastName() {
		return this.lastName;
	}

	@Override
	public String getPasswordHash() {
		return this.passwordHash;
	}

	@Override
	public String getEmail() {
		return this.email;
	}
}

/**
 * 
 */
package uk.ac.ox.bodleian.beam.services.auth;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * TODO JavaDoc for UserDAO.</p> TODO Tests for UserDAO.</p> TODO Implementation
 * for UserDAO.</p>
 * 
 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 *          Created 14 Oct 2012:16:51:49
 */

public interface UserDAO {
	/**
	 * Create the user table
	 */
	@SqlUpdate("create table users (id int identity, name varchar(100) not null, firstName varchar(100) not null, lastName varchar(100) not null, passwordHash varchar(100) not null, email varchar(100) not null)")
	void createUserTable();

	/**
	 * @param name
	 * @param firstName
	 * @param lastName
	 * @param passwordHash
	 * @param email
	 */
	@SqlUpdate("insert into users (name, firstName, lastName, passwordHash, email) values (:firstName, :lastName, :passwordHash, :email)")
	void insert(@Bind("name") String name,
			@Bind("firstName") String firstName,
			@Bind("lastName") String lastName,
			@Bind("passwordHash") String passwordHash,
			@Bind("email") String email);
	
	/**
	 * @param id
	 * @return the user with that id
	 */
	@SqlQuery("select id, name, firstName, lastName, passwordHash, email from users where id = :id")
	@Mapper(UserMapper.class)
	User findById(@Bind("id") int id);

	/**
	 * TODO JavaDoc for UserMapper.</p> TODO Tests for UserMapper.</p> TODO
	 * Implementation for UserMapper.</p>
	 * 
	 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl
	 *         Wilson</a>.</p> <a
	 *         href="https://github.com/carlwilson">carlwilson AT
	 *         github</a>.</p>
	 * @version 0.1
	 * 
	 *          Created 14 Oct 2012:18:44:22
	 */
	public static class UserMapper implements ResultSetMapper<User> {
		@Override
		public User map(int index, ResultSet r, StatementContext ctx)
				throws SQLException {
			return Users.newUser(r.getInt("id"), r.getString("name"), r.getString("firstName"),
					r.getString("lastName"), r.getString("passwordHash"),
					r.getString("email"));
		}

	}
}

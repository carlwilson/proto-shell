/**
 * 
 */
package uk.ac.ox.bodleian.beam.services.auth;


import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import com.yammer.dropwizard.auth.basic.BasicCredentials;

/**
 * TODO JavaDoc for BeamAuthenticator.</p>
 * TODO Tests for BeamAuthenticator.</p>
 * TODO Implementation for BeamAuthenticator.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 14 Oct 2012:15:48:32
 */

public class BeamAuthenticator implements Authenticator<BasicCredentials, User>{

	@Override
	public Optional<User> authenticate(BasicCredentials credentials)
			throws AuthenticationException {
		return Optional.of(Users.getCurrentUser());
	} 

}

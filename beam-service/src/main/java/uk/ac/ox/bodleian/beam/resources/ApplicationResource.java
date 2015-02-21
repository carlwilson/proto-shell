/**
 * 
 */
package uk.ac.ox.bodleian.beam.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import uk.ac.ox.bodleian.beam.services.auth.Users;
import uk.ac.ox.bodleian.beam.services.auth.User;
import uk.ac.ox.bodleian.beam.views.ApplicationView;


/**
 * TODO JavaDoc for ApplicationResource.</p>
 * TODO Tests for ApplicationResource.</p>
 * TODO Implementation for ApplicationResource.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 14 Oct 2012:02:19:08
 */
@Path("/")
public class ApplicationResource {
	
	/**
	 * This is the BEAM application home page.
	 * 
	 * @return the home page for the BEAM application
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
	public ApplicationView homeHtml() {
		return ApplicationView.getNewInstance("home.ftl");
	}
	
	/**
	 * This is the BEAM application home page.
	 * 
	 * @return the home page for the BEAM application
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("bytestreams/")
	public ApplicationView byteStreamsHtml() {
		return ApplicationView.getNewInstance("bytestreams.ftl");
	}

	/**
	 * This is the BEAM application home page.
	 * 
	 * @return the home page for the BEAM application
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("tools/")
	public ApplicationView toolsHtml() {
		return ApplicationView.getNewInstance("tools.ftl");
	}

	/**
	 * @return the HTML view of a user
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("about/")
	public ApplicationView aboutHtml() {
		return ApplicationView.getNewInstance("about.ftl");
	}
	
	/**
	 * @return the JSON representation of the user
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("user/")
	public User userJson() {
		return Users.getCurrentUser();
	}


}

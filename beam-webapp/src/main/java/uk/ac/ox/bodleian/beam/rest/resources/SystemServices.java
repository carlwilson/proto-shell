/**
 * 
 */
package uk.ac.ox.bodleian.beam.rest.resources;

import java.io.File;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import uk.ac.ox.bodleian.beam.BeamApplication;
import uk.ac.ox.bodleian.beam.environment.Environment;
import uk.ac.ox.bodleian.beam.filesystem.roots.Roots;

/**
 * TODO JavaDoc for SystemServices.</p>
 * TODO Tests for SystemServices.</p>
 * TODO Implementation for SystemServices.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 26 Sep 2012:12:16:27
 */
@Path("sys")
public class SystemServices {
	/**
	 * @return the user details of the current user
	 */
	@Path("env")
	@GET
	@Produces({"application/xml"})
	public final Environment getEnv() {
		return BeamApplication.getBEAM().getEnv();
	}
	
	/**
	 * @param path
	 * @return true if the string path is an existing file on the server
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("path/exists/{path}")
	public final boolean pathExists(@PathParam("path") String path) {
		return new File(path).exists();
	}
	
	/**
	 * @param path
	 * @return true if the string path is an existing file on the server
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("path/dir/exists/{path}")
	public final boolean pathDirectoryExists(@PathParam("path") String path) {
		File dir = new File(path);
		return dir.isDirectory();
		
	}

	/**
	 * @return the root directories for the server
	 */
	@Path("roots")
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public final Roots roots() {
		return Roots.getInstance();
	}
}

/**
 * 
 */
package uk.ac.ox.bodleian.beam.resources;

import java.io.FileNotFoundException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import com.sun.jersey.spi.resource.Singleton;

import uk.ac.ox.bodleian.beam.model.collection.BeamAccession;
import uk.ac.ox.bodleian.beam.model.collection.BeamCollection;
import uk.ac.ox.bodleian.beam.views.AccessionsView;

/**
 * TODO JavaDoc for AcessionsResource.</p>
 * TODO Tests for AcessionsResource.</p>
 * TODO Implementation for AcessionsResource.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 16 Oct 2012:01:59:13
 */
@Path("/accessions")
@Singleton
public class AcessionsResource {

	/**
	 * @param root
	 * @return the set of basic accessions returned from the root
	 */
	@GET
	@Path("/scan/{root}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<BeamAccession> scanCollectionAccessionsJson(@PathParam("root") String root) {
		try {
			return BeamCollection.findAccessions(root);
		} catch (FileNotFoundException excep) {
			throw new WebApplicationException(excep, Status.NOT_FOUND);

		}
	}

	/**
	 * @param root
	 * @return the set of basic accessions returned from the root
	 */
	@GET
	@Path("/scan/{root}")
	@Produces(MediaType.TEXT_HTML)
	public AccessionsView scanCollectionAccessions(@PathParam("root") String root) {
		try {
			return AccessionsView.getSummaryView(BeamCollection.findAccessions(root));
		} catch (FileNotFoundException excep) {
			throw new WebApplicationException(excep, Status.NOT_FOUND);

		}
	}
}

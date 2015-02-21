/**
 * 
 */
package uk.ac.ox.bodleian.beam.rest.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import uk.ac.ox.bodleian.beam.BeamApplication;
import uk.ac.ox.bodleian.beam.model.BeamCollection;
import uk.ac.ox.bodleian.beam.registries.Registry;
import uk.ac.ox.bodleian.beam.registries.Registry.ItemNotFoundException;
import uk.ac.ox.bodleian.beam.rest.resources.ExceptionMappingProviders.ItemExistsException;

/**
 * TODO JavaDoc for CollectionServices.</p> TODO Tests for
 * CollectionServices.</p> TODO Implementation for CollectionServices.</p>
 * 
 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 *          Created 24 Sep 2012:14:46:57
 */

@Path("collections")
public class CollectionServices {

	/**
	 * @return the full set of Beam Collections
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	
	public final Set<BeamCollection> getCollections() {
		return BeamApplication.getBEAM().getCollectionRegistry().retrieveAll();
	}

	/**
	 * @param name
	 *            the name of the collection to add
	 * @param title
	 *            the title of the collection to add
	 * @param path
	 *            the root path of the collection to add
	 * @return the new Beam Collection object
	 * @throws FileNotFoundException
	 * @throws ItemExistsException
	 */
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_XML)
	@Path("add")
	public final BeamCollection addCollection(@FormParam("name") String name,
			@FormParam("title") String title, @FormParam("root") String path)
			throws FileNotFoundException, ItemExistsException {
		// Create registry and a item to get
		Registry<BeamCollection> collReg = BeamApplication.getBEAM().getCollectionRegistry();
		BeamCollection queryItem = BeamCollection.fromValues(name, title);

		// First check that the collection doesn't already exist
		try {
			collReg.retrieve(queryItem.getId());
		} catch (ItemNotFoundException excep) {

			File root = new File(path);
			if (!root.isDirectory()) {
				throw new FileNotFoundException("The collection path: " + path
						+ " is not an existing directory.");
			}

			return collReg.register(BeamCollection.fromValues(name, title,
					root.toURI()));
		}
		// OK we need to a collection exists error
		throw new ItemExistsException("A collection called " + name
				+ " already exists.");
	}

	/**
	 * @param id
	 *            the id of the collection to retrieve
	 * @return the collection retrieved
	 * @throws ItemNotFoundException
	 *             if a collection of that name couldn't be found
	 */
	@GET
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("id/{itemid}/")
	@Produces(MediaType.APPLICATION_XML)
	public final BeamCollection getById(@PathParam("itemid") String id)
			throws ItemNotFoundException {
		return BeamApplication.getBEAM().getCollectionRegistry().retrieve(id);
	}

	/**
	 * @param name
	 *            the name of the collection to retrieve
	 * @return the collection retrieved
	 * @throws ItemNotFoundException
	 *             if a collection of that name couldn't be found
	 */
	@GET
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("name/{itemname}/")
	@Produces(MediaType.APPLICATION_XML)
	public final BeamCollection getByName(@PathParam("itemname") String name)
			throws ItemNotFoundException {
		BeamCollection queryItem = BeamCollection.fromValues(name, "title");
		return BeamApplication.getBEAM().getCollectionRegistry().retrieve(queryItem.getId());
	}
}

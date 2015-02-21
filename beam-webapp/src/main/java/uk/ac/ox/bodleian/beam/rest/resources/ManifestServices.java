/**
 * 
 */
package uk.ac.ox.bodleian.beam.rest.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.codec.digest.DigestUtils;
import org.opf_labs.spruce.filesystem.EntryManifest;
import org.opf_labs.spruce.filesystem.FileSystems;

import uk.ac.ox.bodleian.beam.BeamApplication;
import uk.ac.ox.bodleian.beam.model.BeamManifest;
import uk.ac.ox.bodleian.beam.registries.Registry;
import uk.ac.ox.bodleian.beam.registries.Registry.ItemNotFoundException;
import uk.ac.ox.bodleian.beam.rest.resources.ExceptionMappingProviders.ItemExistsException;

/**
 * TODO JavaDoc for ManifestServices.</p>
 * TODO Tests for ManifestServices.</p>
 * TODO Implementation for ManifestServices.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 6 Oct 2012:17:38:42
 */
@Path("manifests")
public class ManifestServices {
	/**
	 * @return the full set of Beam Collections
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public final Set<BeamManifest> getManifests() {
		return BeamApplication.getBEAM().getManifestRegistry().retrieveAll();
	}

	/**
	 * @param path
	 *            the root path of the collection to add
	 * @return the new BeamManifest object
	 * @throws FileNotFoundException
	 * @throws ItemExistsException
	 */
	@GET
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("entries/{root}")
	public final EntryManifest getEntryManifest(@PathParam("root") String path)
			throws FileNotFoundException, ItemExistsException {
		// First check that root is an existing directory
		File rootDir = new File(path);
		if (!rootDir.isDirectory()) {
			throw new FileNotFoundException("The manifest path: " + path
					+ " is not an existing directory.");
		}

		// Now get the URI and hash it, then search in the registry
		URI root = rootDir.toURI();
		Registry<BeamManifest> manReg = BeamApplication.getBEAM().getManifestRegistry();
		
		// First check that the collection doesn't already exist
		try {
			manReg.retrieve(DigestUtils.md5Hex(root.toASCIIString()));
		} catch (ItemNotFoundException excep) {
			return FileSystems.entryManifestFromDirectory(rootDir);
		}
		// OK we need to a collection exists error
		throw new ItemExistsException("A manifest with root directory:" + path
				+ " already exists.");
	}


	/**
	 * @param path
	 *            the root path of the collection to add
	 * @return the new BeamManifest object
	 * @throws FileNotFoundException
	 * @throws ItemExistsException
	 */
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("beam")
	public final BeamManifest createBeamManifest(@FormParam("root") String path)
			throws FileNotFoundException, ItemExistsException {
		// First check that root is an existing directory
		File rootDir = new File(path);
		if (!rootDir.isDirectory()) {
			throw new FileNotFoundException("The manifest path: " + path
					+ " is not an existing directory.");
		}

		// Now get the URI and hash it, then search in the registry
		URI root = rootDir.toURI();
		Registry<BeamManifest> manReg = BeamApplication.getBEAM().getManifestRegistry();
		
		// First check that the collection doesn't already exist
		try {
			manReg.retrieve(DigestUtils.md5Hex(root.toASCIIString()));
		} catch (ItemNotFoundException excep) {
			BeamManifest manifest = BeamManifest.fromDirectory(rootDir); 
			return manReg.register(manifest);
		}
		// OK we need to a collection exists error
		throw new ItemExistsException("A manifest with root directory:" + path
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
	@Produces(MediaType.APPLICATION_JSON)
	public final BeamManifest getById(@PathParam("itemid") String id)
			throws ItemNotFoundException {
		return BeamApplication.getBEAM().getManifestRegistry().retrieve(id);
	}
}

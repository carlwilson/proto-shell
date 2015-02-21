/**
 * 
 */
package uk.ac.ox.bodleian.beam.resources;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.WriteResult;
import uk.ac.ox.bodleian.beam.model.collection.BeamAccession;
import uk.ac.ox.bodleian.beam.model.collection.BeamCollection;
import uk.ac.ox.bodleian.beam.model.collection.CollectionDetails;
import uk.ac.ox.bodleian.beam.services.mongo.MongoResourceHelper;
import uk.ac.ox.bodleian.beam.views.CollectionsView;

/**
 * TODO JavaDoc for CollectionsResource.</p>
 * TODO Tests for CollectionsResource.</p>
 * TODO Implementation for CollectionsResource.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 12 Oct 2012:16:40:57
 */
@Path("/collections")
public class CollectionsResource {

    private JacksonDBCollection<BeamCollection, String> collections;

    /**
     * @param collections
     */
    public CollectionsResource(JacksonDBCollection<BeamCollection, String> collections) {
        this.collections = collections;
    }


    /**
     * @return the List of all collections
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<BeamCollection> getJson() {
    	DBCursor<BeamCollection> cursor = this.collections.find();
    	List<BeamCollection> allCollections = new ArrayList<BeamCollection>();
    	while (cursor.hasNext()) {
    		allCollections.add(cursor.next());
    	}
		return allCollections;
    }

    /**
     * @return the List of all collections
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public CollectionsView getHtml() {
    	DBCursor<BeamCollection> cursor = this.collections.find();
    	List<BeamCollection> allCollections = new ArrayList<BeamCollection>();
    	while (cursor.hasNext()) {
    		allCollections.add(cursor.next());
    	}
    	return CollectionsView.getNewInstance("collections.ftl", getJson());
    }


    /**
     * @param root the path of the collection to retrieve
     * @return the collection with the id passed as a parameter
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/{root}")
    public CollectionsView getCollectionHtml(@PathParam("root") String root) {
        return CollectionsView.getNewInstance("collection.ftl", getCollectionJson(root));
    }

    /**
     * @param root the path of the collection to retrieve
     * @return the collection with the id passed as a parameter
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{root}")
    public BeamCollection getCollectionJson(@PathParam("root") String root) {
    	if (root.equalsIgnoreCase("new")) return BeamCollection.getDefaultInstance();
        BeamCollection collection = this.collections.findOneById(root);
        MongoResourceHelper.notFoundIfNull(collection);

        return collection;
    }

    /**
     * @param root the path of the collection to retrieve
     * @return the collection with the id passed as a parameter
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{root}/accessions")
    public List<BeamAccession> getAccessionsJson(@PathParam("root") String root) {
    	if (root.equalsIgnoreCase("new")){
    		return BeamCollection.getDefaultInstance().getAccessions();
    	}
        BeamCollection collection = this.collections.findOneById(root);
        MongoResourceHelper.notFoundIfNull(collection);

        return collection.getAccessions();
    }

    /**
     * @param root the path of the collection to retrieve
     * @return the collection with the id passed as a parameter
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/{root}/accessions")
    public CollectionsView getAccessionsHtml(@PathParam("root") String root) {
        return CollectionsView.getNewInstance("collection-accessions.ftl", getCollectionJson(root));
    }

	/**
	 * @param name
	 *            the name of the collection to add
	 * @param title
	 *            the title of the collection to add
	 * @param path
	 *            the root path of the collection to add
     * @return a HTTP Response
	 */
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @POST
    @Path("/{name}")
    public Response createCollection(@PathParam("name") String name,
			@FormParam("title") String title, @FormParam("location") String path) {
    	// Now check that there's no collection on that root directory
        BeamCollection collection = this.collections.findOneById(name);
        if (collection !=  null) {
        	// If one exists return CONFLICT response
            return Response.status(Status.CONFLICT).build();
        }
		// First check that the root path points to an existing directory
    	File rootDir = new File(path);
    	if (!rootDir.isDirectory()) {
    		// No directory, so return 404 error
            return Response.status(Status.NOT_FOUND).build();
    	}
    	// Get the URI from the directory
    	URI location = rootDir.toURI();
        // OK let's create the collection
        CollectionDetails details = CollectionDetails.newInstance(title);
        collection = BeamCollection.getNewInstance(name, location, details);
		return updateCollection(collection);
    }

	/**
	 * @param collection 
	 * @return a HTTP Response
	 */
	@Consumes(MediaType.APPLICATION_JSON)
    @PUT
    @Path("/{name}")
    public Response updateCollection(@Valid BeamCollection collection) {
		if (collection.getName().equals(BeamCollection.DEFAULT_NAME)) {
        	// If one exists return CONFLICT response
            return Response.status(Status.FORBIDDEN).build();
		}
        // OK let's create the collection
        WriteResult<BeamCollection, String> result = this.collections.save(collection);

		return Response.created(URI.create("")).build();
    }
}

/**
 * 
 */
package uk.ac.ox.bodleian.beam.services;

import net.vz.mongodb.jackson.JacksonDBCollection;
import uk.ac.ox.bodleian.beam.model.collection.BeamCollection;
import uk.ac.ox.bodleian.beam.resources.AcessionsResource;
import uk.ac.ox.bodleian.beam.resources.ApplicationResource;
import uk.ac.ox.bodleian.beam.resources.CollectionsResource;
import uk.ac.ox.bodleian.beam.resources.ManifestsResource;
import uk.ac.ox.bodleian.beam.services.auth.BeamAuthenticator;
import uk.ac.ox.bodleian.beam.services.auth.User;
import uk.ac.ox.bodleian.beam.services.mongo.MongoHealthCheck;
import uk.ac.ox.bodleian.beam.services.mongo.MongoManaged;

import com.google.common.cache.CacheBuilderSpec;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.auth.basic.BasicAuthProvider;
import com.yammer.dropwizard.bundles.AssetsBundle;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.views.ViewBundle;

/**
 * TODO JavaDoc for BeamService.</p>
 * TODO Tests for BeamService.</p>
 * TODO Implementation for BeamService.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 12 Oct 2012:15:55:15
 */

public class BeamService extends Service<BeamConfiguration> {

	/**
	 * @param args the passed arg array
	 * @throws Exception thrown by the service
	 */
	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			new BeamService().run(new String[]{"server", "src/main/resources/config.yaml"});
		} else {
			new BeamService().run(args);
		}

	}

	private BeamService() {
		super("beam-service");
	    // By default a restart will be required to pick up any changes to assets.
	    // Use the following spec to disable that behaviour, useful when developing.
		// TODO: Turn off caching
	    CacheBuilderSpec cacheSpec = CacheBuilderSpec.disableCaching();
	    // CacheBuilderSpec cacheSpec = AssetsBundle.DEFAULT_CACHE_SPEC;
	    addBundle(new AssetsBundle("/assets/", cacheSpec, "/"));
	    addBundle(new ViewBundle());
	    // TODO: for H2 BD for authorisation
	    //addBundle(new DBIExceptionsBundle());
	}

    @Override
	protected void initialize(BeamConfiguration configuration, Environment environment)
			throws Exception {
		Mongo mongo = new Mongo(configuration.getMongoHost(), configuration.getMongoPort());
		DB mongoDb = mongo.getDB(configuration.getMongoDb());
		JacksonDBCollection<BeamCollection, String> collections = 
				JacksonDBCollection.wrap(mongoDb.getCollection("collections"), BeamCollection.class, String.class);
		MongoManaged mongoManaged = MongoManaged.getMongoManaged(mongo);
		/**
		 * TODO : This to introduce user authorisation
		 */
//	    final DatabaseFactory factory = new DatabaseFactory(environment);
//	    final Database h2Db = factory.build(configuration.getDatabaseConfiguration(), "h2database");
//	    final UserDAO dao = h2Db.onDemand(UserDAO.class);
		environment.manage(mongoManaged);
		environment.addHealthCheck(MongoHealthCheck.getHealthCheck(mongo));
		environment.addProvider(new BasicAuthProvider<User>(new BeamAuthenticator(), "beam"));
		environment.addResource(new CollectionsResource(collections));
		environment.addResource(new AcessionsResource());
		environment.addResource(new ManifestsResource());
		environment.addResource(new ApplicationResource());
	}

}

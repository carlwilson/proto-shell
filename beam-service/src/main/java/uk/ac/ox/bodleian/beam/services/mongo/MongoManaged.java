/**
 * 
 */
package uk.ac.ox.bodleian.beam.services.mongo;

import com.mongodb.Mongo;
import com.yammer.dropwizard.lifecycle.Managed;

/**
 * TODO JavaDoc for MongoManaged.</p>
 * TODO Tests for MongoManaged.</p>
 * TODO Implementation for MongoManaged.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 12 Oct 2012:16:32:12
 */

public final class MongoManaged implements Managed {
	private Mongo mongo;

	private MongoManaged() {
		throw new IllegalStateException("in private no-arg constructor.");
	}

	private MongoManaged(Mongo mongo) {
		this.mongo = mongo;
	}

	/**
	 * @param mongo a mongo db Mongo instance
	 * @return a new MongoManged Managed instance
	 */
	public static final MongoManaged getMongoManaged(Mongo mongo) {
		return new MongoManaged(mongo);
	}
	/**
	 * @see com.yammer.dropwizard.lifecycle.Managed#start()
	 */
	@Override
	public void start() throws Exception {
		// Have to override but nothing to do on start()
	}

	/**
	 * @see com.yammer.dropwizard.lifecycle.Managed#stop()
	 */
	@Override
	public void stop() {
		// Close the mongo DB when the database closes
		this.mongo.close();
	}

}

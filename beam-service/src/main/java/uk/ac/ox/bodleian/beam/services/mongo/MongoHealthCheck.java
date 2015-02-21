/**
 * 
 */
package uk.ac.ox.bodleian.beam.services.mongo;

import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.yammer.metrics.core.HealthCheck;

/**
 * TODO JavaDoc for MongoHealthCheck.</p>
 * TODO Tests for MongoHealthCheck.</p>
 * TODO Implementation for MongoHealthCheck.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 12 Oct 2012:16:28:09
 */

public final class MongoHealthCheck extends HealthCheck {
	private final Mongo mongo;

	private MongoHealthCheck(final Mongo mongo) {
		super("MongoHealthCheck");
		this.mongo = mongo;
	}
	
	/**
	 * @param mongo the application mongo instance
	 * @return a new MongoHealthCheck instance
	 */
	public static final MongoHealthCheck getHealthCheck(final Mongo mongo) {
		return new MongoHealthCheck(mongo);
	}

	/**
	 * @see com.yammer.metrics.core.HealthCheck#check()
	 */
	@Override
	protected Result check() throws MongoException {
        this.mongo.getDatabaseNames();
        return Result.healthy();
	}

}

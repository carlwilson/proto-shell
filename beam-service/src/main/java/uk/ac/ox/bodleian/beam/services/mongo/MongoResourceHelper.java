/**
 * 
 */
package uk.ac.ox.bodleian.beam.services.mongo;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import net.vz.mongodb.jackson.DBCursor;

/**
 * TODO JavaDoc for MongoResourceHelper.</p> TODO Tests for
 * MongoResourceHelper.</p> TODO Implementation for MongoResourceHelper.</p>
 * 
 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 *          Created 12 Oct 2012:16:30:23
 */

public class MongoResourceHelper {
	/**
	 * @param cursor
	 */
	public static void notFoundIfNull(DBCursor<?> cursor) {
		if (!cursor.hasNext()) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
	}

	/**
	 * @param obj
	 */
	public static void notFoundIfNull(Object obj) {
		errorIfNull(obj, Status.NOT_FOUND);
	}

	/**
	 * @param obj
	 * @param status
	 */
	public static void errorIfNull(Object obj, Status status) {
		if (obj == null) {
			throw new WebApplicationException(status);
		}
	}

}

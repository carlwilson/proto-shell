/**
 * 
 */
package uk.ac.ox.bodleian.beam.services;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.db.DatabaseConfiguration;

/**
 * TODO JavaDoc for BeamConfiguration.</p>
 * TODO Tests for BeamConfiguration.</p>
 * TODO Implementation for BeamConfiguration.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 12 Oct 2012:15:50:27
 */

public final class BeamConfiguration extends Configuration {

	@JsonProperty @NotEmpty
	private String mongohost = "localhost";
	
	@Min(1)
	@Max(65535)
	@JsonProperty
	private int mongoport = 27017;
	
	@JsonProperty @NotEmpty
	private String mongodb = "beamdb";
	
	@Valid
	@NotNull
	@JsonProperty
	private DatabaseConfiguration database = new DatabaseConfiguration();
	
	/**
	 * @return the host upon which the Mongo DB instance to use is hosted
	 */
	public final String getMongoHost() {
		return this.mongohost;
	}
	
	/**
	 * @return the port which Mongo DB is listening on
	 */
	public final int getMongoPort() {
		return this.mongoport;
	}
	
	/**
	 * @return the Mongo database name
	 */
	public final String getMongoDb() {
		return this.mongodb;
	}
	
	/**
	 * @return the database configuration 
	 */
	public DatabaseConfiguration getDatabaseConfiguration() {
		return this.database;
	}
}

/**
 * 
 */
package uk.ac.ox.bodleian.beam.model.collection;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import com.google.common.base.Preconditions;

/**
 * TODO JavaDoc for AccessionDetails.</p>
 * TODO Tests for AccessionDetails.</p>
 * TODO Implementation for AccessionDetails.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 23 Oct 2012:19:09:38
 */

public class AccessionDetails {
	private static final AccessionDetails DEFAULT = new AccessionDetails();
	private final String name;
	private final String relativePath;
	private final Date created;

	private AccessionDetails() {
		this("name", "path", new Date(0L));
	}
	
	private AccessionDetails(final String name, final String relativePath,
			final Date dateAdded) {
		assert (name != null && !name.isEmpty());
		assert (relativePath != null && !relativePath.isEmpty());
		assert (dateAdded != null);
		this.name = name;
		this.relativePath = relativePath;
		this.created = dateAdded;
	}
	
	/**
	 * @return the static default instance
	 */
	public static final AccessionDetails getDefaultInstance() {
		return DEFAULT;
	}
	
	
	/**
	 * @param name the name of the accession
	 * @param relativePath the path of the accession relative to it's parent collections root
	 * @return a new AccessionDetails instance created from the passed values
	 */
	public static final AccessionDetails newInstance(final String name, final String relativePath) {
		Preconditions.checkNotNull(name, "name == null");
		Preconditions.checkNotNull(relativePath, "relativePath == null");
		return new AccessionDetails(name, relativePath, new Date());
	}

	/**
	 * @param name the name of the accession
	 * @param relativePath the path of the accession relative to it's parent collections root
	 * @param created the accession was added to the collection
	 * @return a new AccessionDetails instance created from the passed values
	 */
	@JsonCreator
	public static final AccessionDetails newInstance(@JsonProperty("name") final String name,
			@JsonProperty("relativePath") final String relativePath,
			@JsonProperty("created") final Date created) {
		Preconditions.checkNotNull(name, "name == null");
		Preconditions.checkNotNull(relativePath, "relativePath == null");
		Preconditions.checkNotNull(created, "created == null");
		return new AccessionDetails(name, relativePath, created);
	}

	/**
	 * @return the name of the accession
	 */
	@NotEmpty
	public String getName() {
		return this.name;
	}

	/**
	 * @return the relative path of the accession in relation to the path of
	 *         it's parent collection
	 */
	@NotEmpty
	public String getRelativePath() {
		return this.relativePath;
	}

	/**
	 * @return the date the accession was added to the collection
	 */
	@NotEmpty
	public Date getCreated() {
		return this.created;
	}

}

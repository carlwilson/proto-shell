/**
 * 
 */
package uk.ac.ox.bodleian.beam.model.collection;

import java.net.URI;
import java.util.Date;

import javax.validation.Valid;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import com.google.common.base.Preconditions;


/**
 * TODO JavaDoc for CollectionDetails.</p>
 * TODO Tests for CollectionDetails.</p>
 * TODO Implementation for CollectionDetails.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 12 Oct 2012:16:18:03
 */

public class CollectionDetails {
	private static CollectionDetails DEFAULT_COLLECTION = new CollectionDetails();
	private final String title;
	private final Date created;

	private CollectionDetails() {
		this("title", new Date());
	}
	
	private CollectionDetails(final String title, final Date created) {
		assert(title != null && !title.isEmpty());
		assert(created != null);
		this.title = title;
		this.created = created;
	}

	/**
	 * @return the static default instance
	 */
	public final static CollectionDetails getDefaultInstance() {
		return DEFAULT_COLLECTION;
	}

	/**
	 * @param title the title of the collection
	 * @return a new collection details object
	 */
	public final static CollectionDetails newInstance(final String title) {
		return newInstance(title, new Date());
	}

	/**
	 * @param name the name of the collection
	 * @param title the title of the collection
	 * @param root a root directory for the collection
	 * @param created the date the collection was created
	 * @return a new collection details object
	 */
	@JsonCreator
	final static CollectionDetails newInstance(@JsonProperty("title") final String title, @Valid @JsonProperty("created") final Date created) {
		Preconditions.checkNotNull(title, "title == null");
		Preconditions.checkNotNull(created, "created == null");
		return new CollectionDetails(title, created);
	}
	
	/**
	 * This didn't need to be a function except that it might prove
	 * difficult to change my mind unless I stated it somewhere
	 * @param uri the uri to transform to a string root
	 * @return the string rep
	 */
	public static final String transformUriToRoot(URI uri) {
		Preconditions.checkNotNull(uri, "uri == null");
		return uri.toASCIIString();
	}

	/**
	 * @return the collection title
	 */
	@NotEmpty
	public final String getTitle() {
		return this.title;
	}
	
	/**
	 * @return the date the collection was created
	 */
	@Valid
	public final Date getCreated() {
		return this.created;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BeamCollection[title=" + this.title
				+ "]";
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.title == null) ? 0 : this.title.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof CollectionDetails)) {
			return false;
		}
		CollectionDetails other = (CollectionDetails) obj;
		if (this.title == null) {
			if (other.title != null) {
				return false;
			}
		} else if (!this.title.equals(other.title)) {
			return false;
		}
		return true;
	}
}

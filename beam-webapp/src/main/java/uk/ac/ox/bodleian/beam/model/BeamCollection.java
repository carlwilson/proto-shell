/**
 * 
 */
package uk.ac.ox.bodleian.beam.model;

import java.io.File;
import java.net.URI;
import java.util.Date;

import uk.ac.ox.bodleian.beam.environment.Environments;
import uk.ac.ox.bodleian.beam.environment.UserDetails;

/**
 * TODO JavaDoc for BEAMCollection.</p>
 * TODO Tests for BEAMCollection.</p>
 * TODO Implementation for BEAMCollection.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 24 Sep 2012:01:20:12
 */
public class BeamCollection {
	/**	The default name for a collection. */
	public static final String DEFAULT_NAME = "Collection"; 
	/**	The default title for a collection. */
	public static final String DEFAULT_TITLE = "Collection Title"; 
	/**	The default root URI  for a collection. */
	public static final URI DEFAULT_ROOT = URI.create("beam://collection.empty");
	/**	The default creator for a collection. */
	/**	The default checked date for a collection. */
	public static final Date DEFAULT_CHECKED = new Date(0L);
	private final String name;
	private final String title;
	private final URI root;
	private final UserDetails creator;
	private final Date created;
	private Date lastChecked;
	@SuppressWarnings("unused")
	private boolean available;

	private BeamCollection() {
		this(DEFAULT_NAME, DEFAULT_TITLE, DEFAULT_ROOT, UserDetails.getDefaultUser(), null);
	}
	
	private BeamCollection(String name, String title, URI root, UserDetails creator, Date created) {
		if (name == null) throw new IllegalArgumentException("name == null");
		if (title == null) throw new IllegalArgumentException("title == null");
		if (creator == null) throw new IllegalArgumentException("creator == null");
		this.name = name;
		this.title = title;
		this.root = (root == null) ? DEFAULT_ROOT : root;
		this.creator = creator;
		this.created = (created == null) ? new Date() : created;
		this.available = this.isAvailable();
	}

	/**
	 * @param name the name of the collection
	 * @param title the title of the collection
	 * @return a new collection object assembled from the values passed
	 */
	public final static BeamCollection fromValues(String name, String title) {
		return new BeamCollection(name, title, DEFAULT_ROOT, UserDetails.getDefaultUser(), null);
	}

	/**
	 * @param name the name of the collection
	 * @param title the title of the collection
	 * @param root a root directory for the collection
	 * @return a new collection object
	 */
	public final static BeamCollection fromValues(String name, String title, URI root) {
		return new BeamCollection(name, title, root, Environments.getEnvironment().getUser(), null);
	}

	/**
	 * @return the collection name
	 */
	public final String getName() {
		return this.name;
	}
	
	/**
	 * @return the collection title
	 */
	public final String getTitle() {
		return this.title;
	}
	
	/**
	 * @return the root directory for the collection
	 */
	public final URI getRoot() {
		return this.root;
	}

	/**
	 * @return the creator of the collection
	 */
	public final UserDetails getCreator() {
		return this.creator;
	}
	
	/**
	 * @return the date the collection was created
	 */
	public final Date getCreated() {
		return this.created;
	}
	
	/**
	 * @return return true if the root location points to an existing directory.
	 */
	public final boolean isAvailable() {
		this.lastChecked = new Date();
		return this.available = (this.root == DEFAULT_ROOT) ? false : new File(this.root).isDirectory(); 
	}

	/**
	 * @return the date time the collection was last checked
	 */
	public final Date getLastChecked() {
		return this.lastChecked;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BeamCollection[name=" + this.name + ", title=" + this.title
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
				+ ((this.name == null) ? 0 : this.name.hashCode());
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
		if (!(obj instanceof BeamCollection)) {
			return false;
		}
		BeamCollection other = (BeamCollection) obj;
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
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

/**
 * 
 */
package uk.ac.ox.bodleian.beam.model.collection;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import net.vz.mongodb.jackson.Id;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import com.google.common.base.Preconditions;

/**
 * TODO JavaDoc for BeamCollection.</p> TODO Tests for BeamCollection.</p> TODO
 * Implementation for BeamCollection.</p>
 * 
 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 *          Created 12 Oct 2012:16:14:41
 */

public class BeamCollection {
	/** The default root URI for a collection. */
	public static final String DEFAULT_NAME = "new";
	/** The default URI location */
	public static final URI DEFAULT_LOCATION = URI
			.create("beam://collection/new");
	private final static BeamCollection DEFAULT_COLLECTION = new BeamCollection();
	private final String name;
	private final URI location;
	private final CollectionDetails details;
	private final List<BeamAccession> accessions;

	private BeamCollection() {
		this(DEFAULT_NAME, DEFAULT_LOCATION, CollectionDetails
				.getDefaultInstance());
	}

	private BeamCollection(final String name, URI location,
			final CollectionDetails details) {
		this(name, location, details, new ArrayList<BeamAccession>());
	}

	private BeamCollection(final String name, final URI location,
			final CollectionDetails details, List<BeamAccession> accessions) {
		assert (name != null);
		assert (location != null);
		assert (details != null);
		assert (accessions != null);
		this.name = name;
		this.location = location;
		this.details = details;
		this.accessions = accessions;
	}

	/**
	 * @return the default collection instance
	 */
	public static final BeamCollection getDefaultInstance() {
		return DEFAULT_COLLECTION;
	}

	/**
	 * @param name
	 *            the name of the collection to create
	 * @param location
	 *            the URI location of the collection
	 * @param details
	 *            the collection details
	 * @return a new BeamCollection instance created with the id and details
	 *         passed
	 */
	public static final BeamCollection getNewInstance(final String name,
			final URI location, final CollectionDetails details) {
		Preconditions.checkNotNull(name, "name == null");
		Preconditions.checkNotNull(location, "location == null");
		Preconditions.checkNotNull(details, "details == null");
		return new BeamCollection(name, location, details);
	}

	/**
	 * @param name
	 *            the name the collection to create
	 * @param location
	 *            the URI location of the collection
	 * @param details
	 *            the collection details
	 * @param accessions
	 *            the list of the accessions for the collection
	 * @return a new BeamCollection instance created with the id and details
	 *         passed
	 */
	@JsonCreator
	public static final BeamCollection getNewInstance(
			@Id  @JsonProperty("name") final String name, @Valid @JsonProperty("location") final URI location,
			@Valid @JsonProperty("details") final CollectionDetails details,
			@Valid @JsonProperty("accessions") final List<BeamAccession> accessions) {
		Preconditions.checkNotNull(name, "name == null");
		Preconditions.checkNotNull(location, "location == null");
		Preconditions.checkNotNull(details, "details == null");
		Preconditions.checkNotNull(accessions, "accessions == null");
		return new BeamCollection(name, location, details, accessions);
	}

	/**
	 * @param rootPath
	 *            the directory to recurse from looking for accession roots,
	 *            normally a collection root directory
	 * @return the list of any accessions found beneath the root or an empty
	 *         list if none found
	 * @throws FileNotFoundException
	 *             when the passed String root isn't an existing directory
	 */
	public static final List<BeamAccession> findAccessions(String rootPath)
			throws FileNotFoundException {
		Preconditions.checkNotNull(rootPath, "root == null");
		// Get past URL files
		File rootDir = getRootFile(rootPath);
		if (!rootDir.isDirectory()) {
			throw new FileNotFoundException("Directory not found:" + rootPath);
		}
		List<BeamAccession> accessions = new ArrayList<BeamAccession>();
		for (File child : rootDir.listFiles()) {
			if (child.isDirectory()
					&& child.getName().startsWith(
							BeamAccession.ACCESSION_DIR_PREFIX)) {
				BeamAccession accession = BeamAccession.newInstanceFromDir(
						child, rootDir);
				accessions.add(accession);
			}
		}
		return accessions;
	}
	
	/**
	 * @param collection
	 * @return the list of any accessions found beneath the root or an empty
	 *         list if none found
	 * @throws FileNotFoundException
	 *             when the passed String root isn't an existing directory
	 */
	public static final List<BeamAccession> findNewAccessions(BeamCollection collection)
			throws FileNotFoundException {
		Preconditions.checkNotNull(collection, "collection == null");
		if (collection.equals(DEFAULT_COLLECTION)) return DEFAULT_COLLECTION.accessions;
		// Get past URL files
		File rootDir = new File(collection.location);
		if (!rootDir.isDirectory()) {
			throw new FileNotFoundException("Directory not found:" + collection.location);
		}
		List<String> collAccessionNames = new ArrayList<String>();
		for (BeamAccession accession : collection.accessions) {
			collAccessionNames.add(accession.getDetails().getName());
		}
		List<BeamAccession> accessions = new ArrayList<BeamAccession>();
		for (File child : rootDir.listFiles()) {
			if (child.isDirectory()
					&& child.getName().startsWith(
							BeamAccession.ACCESSION_DIR_PREFIX)) {
				BeamAccession accession = BeamAccession.newInstanceFromDir(
						child, rootDir);
				if (!collAccessionNames.contains(accession.getDetails().getName()))
					accessions.add(accession);
			}
		}
		return accessions;
	}

	/**
	 * Used to mitigate the fact that File URI's don't work on windows.
	 * 
	 * @param rootPath
	 *            the path (possibly in URI form) to get a file path to.
	 * @return the actual File that this path points to
	 */
	public static final File getRootFile(String rootPath) {
		String actualPath = rootPath;
		if (rootPath.startsWith("file:/")) {
			actualPath = rootPath.replaceFirst("file:/", "");
			while (actualPath.startsWith("/")) {
				actualPath = actualPath.replaceFirst("/", "");
			}
		}
		return new File(actualPath);
	}

	/**
	 * @return the collection name
	 */
	@Id
	public final String getName() {
		return this.name;
	}

	/**
	 * @return the collection id
	 */
	@Valid
	public final URI getLocation() {
		return this.location;
	}

	/**
	 * @return the collection details
	 */
	@Valid
	public CollectionDetails getDetails() {
		return this.details;
	}

	/**
	 * @return the list of accessions
	 */
	@Valid
	public List<BeamAccession> getAccessions() {
		return this.accessions;
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
				+ ((this.location == null) ? 0 : this.location.hashCode());
		result = prime * result
				+ ((this.details == null) ? 0 : this.details.hashCode());
		result = prime * result
				+ ((this.accessions == null) ? 0 : this.accessions.hashCode());
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
		if (this.location == null) {
			if (other.location != null) {
				return false;
			}
		} else if (!this.location.equals(other.location)) {
			return false;
		}
		if (this.details == null) {
			if (other.details != null) {
				return false;
			}
		} else if (!this.details.equals(other.details)) {
			return false;
		}
		if (this.accessions == null) {
			if (other.accessions != null) {
				return false;
			}
		} else if (!this.accessions.equals(other.accessions)) {
			return false;
		}
		return true;
	}
}

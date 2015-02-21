/**
 * 
 */
package uk.ac.ox.bodleian.beam.model.collection;

import java.io.File;
import java.util.Date;

import javax.validation.Valid;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.opf_labs.spruce.filesystem.ByteStreamManifest;
import org.opf_labs.spruce.filesystem.ByteStreamManifestImpl;
import org.opf_labs.spruce.filesystem.FileSystems;

import com.google.common.base.Preconditions;

/**
 * TODO JavaDoc for BeamAccession.</p> TODO Tests for BeamAccession.</p> TODO
 * Implementation for BeamAccession.</p>
 * 
 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 *          Created 15 Oct 2012:19:40:29
 */

public final class BeamAccession {
	/** The directory name prefix for accession directories */
	public static final String ACCESSION_DIR_PREFIX = "acc_";
	/** The directory name prefix for accession directories */
	public static final String ACCESSION_DATA_ROOT = "accessiondata";
	private final static BeamAccession DEFAULT_ACCESSION = new BeamAccession();
	private final AccessionDetails details;
	private final Date checked;
	@JsonSerialize(as=ByteStreamManifestImpl.class)
	@JsonDeserialize(as=ByteStreamManifestImpl.class)
	private final ByteStreamManifest manifest;


	private BeamAccession() {
		this(AccessionDetails.getDefaultInstance());
	}
	
	private BeamAccession(final AccessionDetails details) {
		this(details, details.getCreated());
	}

	private BeamAccession(final AccessionDetails details, final Date dateChecked) {
		this(details, dateChecked, FileSystems.nullByteStreamManifest());
	}

	private BeamAccession(final AccessionDetails details, final Date checked, final ByteStreamManifest manifest) {
		assert (details != null);
		assert (checked != null);
		assert (manifest != null);
		this.details = details;
		this.checked = checked;
		this.manifest = manifest;
	}

	/**
	 * @return the default accession instance
	 */
	public static final BeamAccession defaultInstance() {
		return DEFAULT_ACCESSION;
	}

	/**
	 * @param name the name of the accession
	 * @param relativePath the path of the accession relative to its parent collection
	 * @return the new Beam Accession instance
	 */
	static final BeamAccession newInstance(final String name,
			final String relativePath) {
		Preconditions.checkNotNull(name, "name == null");
		Preconditions.checkNotNull(relativePath, "relativePath == null");
		AccessionDetails details = AccessionDetails.newInstance(name, relativePath);
		return new BeamAccession(details);
	}

	/**
	 * @param name the name of the accession
	 * @param relativePath the path of the accession relative to its parent collection
	 * @param dateAdded the date the accession was added to the collection
	 * @return the new Beam Accession instance
	 */
	static final BeamAccession newInstance(final AccessionDetails details) {
		Preconditions.checkNotNull(details, "details == null");
		return new BeamAccession(details);
	}

	/**
	 * @param name the name of the accession
	 * @param relativePath the path of the accession relative to its parent collection
	 * @param dateAdded the date the accession was added to the collection
	 * @return the new Beam Accession instance
	 */
	@JsonCreator
	static final BeamAccession newInstance(@Valid @JsonProperty("details") final AccessionDetails details, @Valid @JsonProperty("checked") final Date checked) {
		Preconditions.checkNotNull(details, "dateAdded == null");
		Preconditions.checkNotNull(checked, "checked == null");
		return new BeamAccession(details, checked);
	}
	
	static final BeamAccession newInstanceFromDir(File dir, File collRoot) {
		Preconditions.checkNotNull(dir, "dir == null");
		Preconditions.checkNotNull(collRoot, "collRoot == null");
		Preconditions.checkArgument(dir.isDirectory(), "dir parameter is not an existing directory: " + dir);
		Preconditions.checkArgument(collRoot.isDirectory(), "collRoot parameter is not an existing directory: " + collRoot);
		String name = dir.getName().replaceFirst(ACCESSION_DIR_PREFIX, "");
		String relPath = FileSystems.relatavisePaths(collRoot.getAbsolutePath(), dir.getAbsolutePath());
		AccessionDetails details = AccessionDetails.newInstance(name, relPath, new Date()); 
		return new BeamAccession(details);
	}

	/**
	 * @return the accession details for the accession
	 */
	public AccessionDetails getDetails() {
		return this.details;
	}
	
	/**
	 * @return the date the accession was last checked
	 */
	@Valid
	public Date getChecked() {
		return this.checked;
	}
}

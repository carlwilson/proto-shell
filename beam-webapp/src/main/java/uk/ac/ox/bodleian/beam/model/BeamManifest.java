/**
 * 
 */
package uk.ac.ox.bodleian.beam.model;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.opf_labs.spruce.filesystem.ByteStreamManifest;
import org.opf_labs.spruce.filesystem.ByteStreamManifestImpl;
import org.opf_labs.spruce.filesystem.FileSystems;

import uk.ac.ox.bodleian.beam.environment.Environments;
import uk.ac.ox.bodleian.beam.environment.UserDetails;
import uk.ac.ox.bodleian.beam.registries.Identifiable;

/**
 * TODO JavaDoc for BeamManifest.</p>
 * TODO Tests for BeamManifest.</p>
 * TODO Implementation for BeamManifest.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 6 Oct 2012:12:07:21
 */
public final class BeamManifest implements Identifiable {
	/**	UID for serialization */
	private static final long serialVersionUID = -3624613746184883097L;
	/**	The default root URI  for a collection. */
	public static final URI DEFAULT_ROOT = URI.create("beam://manifest.empty");
	private static BeamManifest DEFAULT_MANIFEST = new BeamManifest();
	private final String id;
	private final URI root;
	private final UserDetails creator = Environments.getEnvironment().getUser();
	private final Date dateCreated;
	private final Date dateChecked;
	@JsonSerialize(as=ByteStreamManifestImpl.class)
	@JsonDeserialize(as=ByteStreamManifestImpl.class)
	private final ByteStreamManifest manifest;
	
	private BeamManifest() {
		this(DEFAULT_ROOT, FileSystems.nullByteStreamManifest());
	}

	private BeamManifest(URI root, ByteStreamManifest manifest) {
		this.root = root;
		this.id = DigestUtils.md5Hex(root.toASCIIString());
		this.manifest = manifest;
		this.dateCreated = new Date();
		this.dateChecked = this.dateCreated;
	}

	static final BeamManifest fromValues(final URI root, final ByteStreamManifest manifest) {
		if (root == null) throw new IllegalArgumentException("root == null");
		if (manifest == null) throw new IllegalArgumentException("manifest == null");
		return new BeamManifest(root, manifest);
	}

	static final BeamManifest defaultManifest() {
		return DEFAULT_MANIFEST;
	}

	/**
	 * @param directory a root directory to create the manifest from
	 * @return a new BeamManifest instance created from the passed Manifest
	 */
	public static final BeamManifest fromDirectory(final File directory) {
		if (directory == null) throw new IllegalArgumentException("directory == null");
		if (!directory.isDirectory()) throw new IllegalArgumentException("directory.isDirectory() != true");
		ByteStreamManifest manifest = FileSystems.byteStreamManifestFromDirectory(directory);
		URI root = directory.toURI();
		return new BeamManifest(root, manifest);
	}

	@Override
	public String getId() {
		return this.id;
	}

	/**
	 * @return the root URI for the manifest
	 */
	public final URI getRoot() {
		return this.root;
	}

	/**
	 * @return the UserDetails of the creator of the Manifest
	 */
	public final UserDetails getCreator() {
		return this.creator;
	}

	/**
	 * @return the date that this manifest was created
	 */
	public final Date getDateCreated() {
		return this.dateCreated;
	}

	/**
	 * @return the date that this manifest was last checked
	 */
	public final Date getDateChecked() {
		return this.dateChecked;
	}

	/**
	 * @return the manifest 
	 */
	public final ByteStreamManifest getManifest() {
		return this.manifest;
	}
}

/**
 * 
 */
package uk.ac.ox.bodleian.beam.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.codec.digest.DigestUtils;

import uk.ac.ox.bodleian.beam.filesystem.FileSystem;
import uk.ac.ox.bodleian.beam.filesystem.FileSystems;
import uk.ac.ox.bodleian.beam.filesystem.entry.FileSystemEntry;
import uk.ac.ox.bodleian.beam.registries.Identifiable;

/**
 * TODO: JavaDoc for BeamAccession.
 * <p/>
 * TODO: Tests for BeamAccession.
 * <p/>
 * TODO: Add all BeamApplication metadata properties (see email from Susan).
 * <p/>
 * TODO: Tease out identification of contained file systems (aggregates) and
 * into a TikaFileSystem.
 * <p/>
 * 
 * @author <a href="mailto:carl.wilson@keepitdigital.eu">Carl Wilson</a> <a
 *         href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 11 Jul 2012:12:36:21
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public final class BeamAccession implements Identifiable {
	private static final long serialVersionUID = -905033500991138560L;

	//private static final Logger LOGGER = Logger.getLogger(BeamAccession.class);

	/** The directory name prefix for accession directories */
	public static final String ACCESSION_DIR_PREFIX = "acc_";
	
	/** The directory name prefix for accession directories */
	public static final String ACCESSION_DATA_ROOT = "accessiondata";

	private final String id;
	private final String name;
	private final File root;
	@XmlTransient
	private FileSystem fileSystem;
	private long dateSpanStart = new Date().getTime();
	private long dateSpanEnd = 0L;
	
	private BeamAccession() {
		this("name", new File("."));
		/** disable default constructor */
	}

	private BeamAccession(String name, File accessionRoot) {
		this.name = name;
		this.root = accessionRoot;
		this.id = DigestUtils.sha256Hex(this.name + this.root);
		this.fileSystem = FileSystems.fromDirectory(this.root);
		for (FileSystemEntry entry : this.fileSystem.getEntries()) {
			if (this.dateSpanEnd < entry.getModifiedMillis()) {
				this.dateSpanEnd = entry.getModifiedMillis();
			}
			if (this.dateSpanStart > entry.getModifiedMillis()) {
				this.dateSpanStart = entry.getModifiedMillis();
			}
		}
	}

	/**
	 * @see uk.ac.ox.bodleian.beam.registries.Identifiable#getId()
	 */
	@Override
	public final String getId() {
		return this.id;
	}

	/**
	 * @return the earliest file modified date found in the accession
	 */
	public final Date getDateSpanStart() {
		return new Date(this.dateSpanStart);
	}

	/**
	 * @return the most recent file modified date found in the accession
	 */
	public final Date getDateSpanEnd() {
		return new Date(this.dateSpanEnd);
	}

	/**
	 * @return the number of files in the accession
	 */
	public final int getFileCount() {
		return this.fileSystem.getEntryCount();
	}

	/**
	 * @return the total number of bytes for the accession
	 */
	public final long getTotalBytes() {
		return this.fileSystem.getTotalBytes();
	}
	
	/**
	 * @param root
	 *            the root directory for the accession
	 * @return a new BeamApplication accession instance
	 * @throws FileNotFoundException 
	 */
	public static BeamAccession getInstanceFromDir(File root) throws FileNotFoundException {
		return new BeamAccession(root.getName(), root);
	}
	

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder retValBuilder = new StringBuilder(
				"BeamAccession [id=" + this.getId()
						+ ", fileCount=" + this.getFileCount()
						+ ", dateSpanStart=" + new Date(this.dateSpanStart)
						+ ", dateSpanEnd=" + new Date(this.dateSpanEnd) + "]");
		retValBuilder.append(this.fileSystem.toString());
		retValBuilder.append("]");
		return retValBuilder.toString();
	}

	/**
	 * @param dirName
	 * @return the BeamApplication accession name
	 */
	public final static String getAccessionNameFromDirectoryName(String dirName) {
		if (dirName.startsWith(ACCESSION_DIR_PREFIX))
			return dirName.replaceFirst(ACCESSION_DIR_PREFIX, "");
		return "";
	}
}

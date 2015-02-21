/**
 * 
 */
package uk.ac.ox.bodleian.beam.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.tika.mime.MediaType;

import uk.ac.ox.bodleian.beam.filesystem.FileSystem;
import uk.ac.ox.bodleian.beam.filesystem.FileSystem.DamagedEntryException;
import uk.ac.ox.bodleian.beam.filesystem.FileSystem.EntryNotFoundException;
import uk.ac.ox.bodleian.beam.filesystem.FileSystems;
import uk.ac.ox.bodleian.beam.filesystem.HashedFileSystem;
import uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreamId;
import uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreamInstance;
import uk.ac.ox.bodleian.beam.filesystem.entry.FileSystemEntry;
import uk.ac.ox.bodleian.beam.tika.TikaWrapper;

/**
 * TODO: JavaDoc for BEAMAccessionOld.
 * <p/>
 * TODO: Tests for BEAMAccessionOld.
 * <p/>
 * TODO: Add all BEAM metadata properties (see email from Susan).
 * <p/>
 * TODO: Tease out identification of contained file systems (aggregates) and
 * into a TikaFileSystem.
 * <p/>
 * 
 * @author <a href="mailto:carl.wilson@keepitdigital.eu">Carl Wilson</a> <a
 *         href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 11 Jul 2012:12:36:21
 */

public final class BEAMAccessionOld {
	private static final Logger LOGGER = Logger.getLogger(BEAMAccessionOld.class);
	/** The file extension for metadata files */
	public static final String METADATA_EXT = ".meta";
	/** The directory name prefix for accession directories */
	public static final String ACCESSION_DIR_PREFIX = "acc_";
	/** The directory name prefix for accession directories */
	public static final String ACCESSION_DATA_ROOT = "accessiondata";
	private File accessionRoot;
	private FileSystem fileSystem;
	private HashedFileSystem hashedSystem;
	private Map<ByteStreamId, MediaType> formatRecord = new HashMap<ByteStreamId, MediaType>();
	private long dateSpanStart = new Date().getTime();
	private long dateSpanEnd = 0L;
	
	Map<MediaType, Integer> formats = new HashMap<MediaType, Integer>();

	private BEAMAccessionOld() {
		/** disable default constructor */
		LOGGER.fatal("In default constructor.");
		throw new AssertionError("In default constructor.");
	}

	private BEAMAccessionOld(File accessionRoot) {
		// OK dir should be called acc_collName_id something, so get the acc id
		if (!accessionRoot.getName().startsWith(ACCESSION_DIR_PREFIX))
			throw new IllegalArgumentException(
					"Acession dir should start acc_, but is "
							+ accessionRoot.getAbsolutePath());
		this.accessionRoot = accessionRoot;
		this.fileSystem = FileSystems.fromDirectory(this.accessionRoot);
		for (FileSystemEntry entry : this.fileSystem.getEntries()) {
			if (this.dateSpanEnd < entry.getModifiedMillis()) {
				this.dateSpanEnd = entry.getModifiedMillis();
			}
			if (this.dateSpanStart > entry.getModifiedMillis()) {
				this.dateSpanStart = entry.getModifiedMillis();
			}
		}
		this.hashedSystem = new HashedFileSystem(this.fileSystem);
	}

	/**
	 * @return the id for this accession
	 */
	public final String getId() {
		return BEAMAccessionOld.getAccessionNameFromDirectoryName(this.accessionRoot.getName());
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
	 * @return a java.util.Map of Tika MediaTypes and a count of how often they
	 *         occur in the accession
	 */
	public final Map<MediaType, Integer> getFormatTypes() {
		return Collections.unmodifiableMap(this.formats);
	}

	/**
	 * @return the number of files in the accession
	 */
	public final int getFileCount() {
		return this.fileSystem.getEntryCount();
	}

	/**
	 * @return the total number of bytes
	 */
	public final long getTotalBytes() {
		return this.fileSystem.getTotalBytes();
	}
	
	/**
	 * @return the number of bytes hashed
	 */
	public final long getBytesHashed() {
		return this.hashedSystem.getBytesProcessed();
	}
	/**
	 * @throws IOException 
	 * 
	 */
	public void hashCheckFileSystem() throws IOException {
		this.hashedSystem.hashCheckFileSystem();
	}
	
	/**
	 * @throws IOException 
	 * 
	 */
	@SuppressWarnings("resource")
	public void formatCheckFileSystem() throws IOException {
		TikaWrapper tika = TikaWrapper.getTika();
		FileSystemEntry entry = null;
		while ((entry = this.fileSystem.getNextEntry()) != null) {
			ByteStreamInstance byteStream = this.hashedSystem.getEntryByteStream(entry.getName());
			if (byteStream.isOK()) {
				InputStream is = null;
				MediaType mt = null;
				try {
					is = this.fileSystem.getEntryStream();
					mt = tika.getMediaType(is);
					this.formatRecord.put(byteStream.getByteStreamId(), mt);
				} catch (UnsupportedOperationException excep) {
					// TODO Auto-generated catch block
					excep.printStackTrace();
				} catch (EntryNotFoundException excep) {
					// TODO Auto-generated catch block
					excep.printStackTrace();
				} catch (DamagedEntryException excep) {
					// TODO Auto-generated catch block
					excep.printStackTrace();
				} catch (IOException excep) {
					// TODO Auto-generated catch block
					excep.printStackTrace();
				} finally {
					try {
						if (is != null)
							is.close();
					} catch (IOException excep) {
						// do nothing
					}
				}
			}
		}
		// loop through all byte stream records from hashed file system 
	}
	/**
	 * @param accessionRoot
	 *            the root directory for the accession
	 * @return a new BEAM accession instance
	 * @throws FileNotFoundException 
	 */
	public static BEAMAccessionOld getInstanceFromDir(File accessionRoot) throws FileNotFoundException {
		return new BEAMAccessionOld(accessionRoot);
	}
	

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder retValBuilder = new StringBuilder(
				"BEAMAccessionOld [id=" + this.getId()
						+ ", fileCount=" + this.getFileCount()
						+ ", dateSpanStart=" + new Date(this.dateSpanStart)
						+ ", dateSpanEnd=" + new Date(this.dateSpanEnd)
						+ ",\nformats=" + this.formats + "\n");
		retValBuilder.append(this.fileSystem.toString());
		retValBuilder.append("]");
		return retValBuilder.toString();
	}

	/**
	 * @param dirName
	 * @return the BEAM accession name
	 */
	public final static String getAccessionNameFromDirectoryName(String dirName) {
		if (dirName.startsWith(ACCESSION_DIR_PREFIX))
			return dirName.replaceFirst(ACCESSION_DIR_PREFIX, "");
		return "";
	}
	
	/**
	 * @return the JSON representation of the accession
	 */
	public final String toJSON() {
		return "{\"id\":\"" + this.getId() + "\"}";
	}
}

/**
 * 
 */
package uk.ac.ox.bodleian.beam.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.tika.mime.MediaType;

/**
 * TODO: JavaDoc for BEAMCollectionOld.
 * <p/>
 * TODO: Tests for BEAMCollectionOld.
 * <p/>
 * TODO: Add all BEAM metadata properties (see email from Susan).
 * <p/>
 * TODO: Tease out identification of contained file systems (aggregates) and
 * into a TikaFileSystem.
 * <p/>
 * 
 * @author <a href="mailto:carl.wilson@keepitdigital.eu">Carl Wilson</a> <a
 *         href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1
 * 
 *          Created 11 Jul 2012:12:25:49
 */

public class BEAMCollectionOld implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(BEAMCollectionOld.class);
	private File collectionRoot;
	private String title;
	private long totalSize = 0L;
	private int fileCount = 0;
	private Set<BEAMAccessionOld> accessions = new HashSet<BEAMAccessionOld>();
	private volatile boolean isProcessing = false;

	private BEAMCollectionOld() {
		/** Disable the default constructor */
		LOGGER.fatal("In default constructor.");
		throw new AssertionError("In default constructor.");
	}

	private BEAMCollectionOld(File collectionRoot) throws FileNotFoundException {
		this.collectionRoot = collectionRoot;
		this.findAccessions();
	}

	/**
	 * @return the BEAM Collection name
	 */
	public final String getName() {
		return BEAMCollectionOld.getCollectionNameFromFile(this.collectionRoot);
	}

	/**
	 * @return the BEAM collection title
	 */
	public final String getTitle() {
		return this.title;
	}

	/**
	 * @return a java.util.Set of the accessions that make up the collection
	 */
	public final synchronized Set<BEAMAccessionOld> getAccessions() {
		return this.accessions;
	}

	/**
	 * @return the total size of the collection
	 */
	public final long getTotalSize() {
		return this.totalSize;
	}

	/**
	 * @return the total number of items in the collection
	 */
	public final long getEntryCount() {
		return this.fileCount;
	}

	/**
	 * @return the earliest modified date found in the collections accessions
	 */
	public final Date getDateSpanStart() {
		// Get the date now, all dates should be earlier, unless the accession
		// is from the future
		long collSpanStartMillis = new Date().getTime();

		// Loop through the accessions
		for (BEAMAccessionOld accession : this.accessions) {
			// get the time, if it's less than collection time start
			long accSpanStartMillis = accession.getDateSpanStart().getTime();
			collSpanStartMillis = (accSpanStartMillis < collSpanStartMillis) ? accSpanStartMillis
					: collSpanStartMillis;
		}
		return new Date(collSpanStartMillis);
	}

	/**
	 * @return the most recent modified date found in the collections accessions
	 */
	public final Date getDateSpanEnd() {
		// Set the end date to the start of time, all dates should be sooner
		long collSpanEndMillis = 0L;

		// Loop through the accessions
		for (BEAMAccessionOld accession : this.accessions) {
			// get the time, if it's greater than collection time end
			long accSpanEndMillis = accession.getDateSpanEnd().getTime();
			collSpanEndMillis = (accSpanEndMillis > collSpanEndMillis) ? accSpanEndMillis
					: collSpanEndMillis;
		}

		return new Date(collSpanEndMillis);
	}

	/**
	 * @return a java.util.Map of Tika MediaTypes and a count of how often they
	 *         occur in the collection
	 */
	public final Map<MediaType, Integer> getFormatTypes() {
		// Create a new Map for return
		Map<MediaType, Integer> formatTypes = new HashMap<MediaType, Integer>();
		// loop through the collection accessions
		for (BEAMAccessionOld accession : this.accessions) {
			// Get the format map from the accession
			Map<MediaType, Integer> accTypes = accession.getFormatTypes();
			// loop through the MediaTypes
			for (MediaType key : accTypes.keySet()) {
				// Total if we have this media type, start a new entry from
				// total if not
				Integer formatTotal = formatTypes.containsKey(key) ? formatTypes
						.get(key) + accTypes.get(key)
						: accTypes.get(key);
				// add to the map
				formatTypes.put(key, formatTotal);
			}
		}
		// Return unmodifiable version of our Map
		return Collections.unmodifiableMap(formatTypes);
	}

	/**
	 * @return a count of the different format types present in the collection
	 */
	public final int getFormatTypeCount() {
		return this.getFormatTypes().size();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BEAMCollectionOld [name=" + this.getName()
				+ ", title=" + this.title + ", totalsize=" + this.totalSize + ", filecount=" + this.fileCount + ", datespanstart=" + this.getDateSpanStart() + ", datespanend=" + this.getDateSpanEnd() + "]";
	}

	/**
	 * @param collectionRoot
	 *            the root directory for the collections
	 * @return a new BEAMCollcetion instance
	 * @throws IOException
	 */
	public static final BEAMCollectionOld getInstanceFromDirectory(
			File collectionRoot) throws IOException {
		return new BEAMCollectionOld(collectionRoot);
	}

	/**
	 * @param file
	 * @return the collection name derived from the file
	 */
	public static final String getCollectionNameFromFile(File file) {
		return file.getName();
	}

	private void findAccessions() throws FileNotFoundException {
		for (File child : this.collectionRoot.listFiles()) {
			if (child.isDirectory()
					&& child.getName().startsWith(
							BEAMAccessionOld.ACCESSION_DIR_PREFIX)) {
				BEAMAccessionOld accession = BEAMAccessionOld.getInstanceFromDir(child);
				this.totalSize += accession.getTotalBytes();
				this.fileCount += accession.getFileCount();
				this.accessions.add(accession);
			}
		}
	}

	/**
	 * @throws IOException
	 */
	private void processAccessions() throws IOException {
		for (BEAMAccessionOld accession : this.accessions)
			accession.hashCheckFileSystem();
	}

	/**
	 * @return true is the thread is processing
	 */
	public boolean isProcessing() {
		return this.isProcessing;
	}

	@Override
	public void run() {
		this.isProcessing = true;
		try {
			this.processAccessions();
		} catch (IOException excep) {
			// TODO Auto-generated catch block
			excep.printStackTrace();
		}
		this.isProcessing = false;
	}
}

/**
 * 
 */
package org.opf_labs.spruce.filesystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.opf_labs.spruce.bytestream.ByteStreamId;
import org.opf_labs.spruce.bytestream.ByteStreams;
import org.opf_labs.spruce.filesystem.Entry.EntryStatus;

/**
 * TODO: JavaDoc for FileSystems.
 * <p/>
 * 
 * @author <a href="mailto:carl.wilson@keepitdigital.eu">Carl Wilson</a> <a
 *         href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1
 * 
 *          Created 10 Jul 2012:15:25:51
 */

public final class FileSystems {
	private FileSystems() {
		/** disable default constructor */
		throw new AssertionError("In disabled private constructor.");
	}

	/**
	 * @param parentPath
	 * @param childPath
	 * @return the relative path to use as an entry name
	 */
	public final static String relatavisePaths(final String parentPath,
			final String childPath) {
		String normParent = FilenameUtils.normalize(parentPath);
		String normChild = FilenameUtils.normalize(childPath);
		if (childPath.startsWith(normParent)) {
			return normChild.substring(normParent.length() + 1,
					normChild.length());
		}
		return "";
	}

	/**
	 * @return the null Entry, empty string name and 0L for size and modified
	 *         time
	 */
	public static final Entry nullEntry() {
		return EntryImpl.NULL_ENTRY;
	}

	/**
	 * @param name
	 *            a non null, non empty string name
	 * @param size
	 *            reported length of entry in bytes long > -1
	 * @param modified
	 *            the modified time in milliseconds since 01/01/1970, long > -1
	 * @return a new {@link Entry} created from the values.
	 */
	public static final Entry uncheckedEntryFromValues(final String name,
			final long size, final long modified) {
		if (name == null)
			throw new IllegalArgumentException("name == null");
		if (name.isEmpty())
			throw new IllegalArgumentException("name.isEmpty() == true");
		return EntryImpl.fromValues(name, size, modified);
	}

	/**
	 * @param name
	 *            the entry name, must be non null and not empty
	 * @param file
	 *            the file to create entry details from, must not be a
	 *            directory.
	 * @return an unchecked Entry populated from the file
	 */
	public static final Entry uncheckedEntryFromFile(final String name,
			final File file) {
		if (name == null)
			throw new IllegalArgumentException("name == null");
		if (name.isEmpty())
			throw new IllegalArgumentException("name.isEmpty() == true");
		if (file == null)
			throw new IllegalArgumentException("file == null");
		if (!file.isFile())
			throw new IllegalArgumentException("(file.isFile()) != true for "
					+ file);
		return EntryImpl.fromValues(name, file.length(), file.lastModified());
	}

	/**
	 * @param name
	 *            the entry name, must be non null and not empty
	 * @param file
	 *            the file to create entry details from, must not be a
	 *            directory.
	 * @return an OK Entry populated from the file
	 */
	public static final Entry okEntryFromFile(final String name, final File file) {
		if (name == null)
			throw new IllegalArgumentException("name == null");
		if (name.isEmpty())
			throw new IllegalArgumentException("name.isEmpty() == true");
		if (file == null)
			throw new IllegalArgumentException("file == null");
		if (!file.isFile())
			throw new IllegalArgumentException("(file.isFile()) != true for "
					+ file);
		return EntryImpl.fromValues(name, file.length(), file.lastModified(),
				EntryStatus.OK);
	}

	/**
	 * @param name
	 * @param size
	 * @param modified
	 * @param status
	 * @return
	 */
	public static final Entry entryFromValues(final String name,
			final long size, final long modified, EntryStatus status) {
		if (name == null)
			throw new IllegalArgumentException("name == null");
		if (name.isEmpty())
			throw new IllegalArgumentException("name.isEmpty() == true");
		return EntryImpl.fromValues(name, size, modified, status);
	}

	/**
	 * @return the null byte stream manifest
	 */
	public static EntryManifest nullEntryManifest() {
		return EntryManifestImpl.NULL_MANIFEST;
	}

	/**
	 * @param directory
	 * @return a new EntryManifest created from the file system below the
	 *         directory.
	 */
	public final static EntryManifest entryManifestFromDirectory(
			final File directory) {
		return EntryManifestImpl.fromDirectory(directory);
	}

	/**
	 * @return the null byte stream manifest
	 */
	public static ByteStreamManifest nullByteStreamManifest() {
		return ByteStreamManifestImpl.NULL_MANIFEST;
	}

	/**
	 * @param totalSize
	 * @param countedBytes
	 * @param indentifiedEntries
	 * @return
	 */
	public static ByteStreamManifest byteStreamManifestFromValues(final long totalSize,
			final long countedBytes,
			final Set<EntryTuple> indentifiedEntries) {
		if (totalSize < 0L)
			throw new IllegalArgumentException("totalSize < 0L");
		if (countedBytes < 0L)
			throw new IllegalArgumentException("entrySize < 0L");
		if (indentifiedEntries == null)
			throw new IllegalArgumentException("entryTuples == null");
		return ByteStreamManifestImpl.fromValues(totalSize, countedBytes, indentifiedEntries);
	}

	/**
	 * @param directory
	 * @return a new ByteStreamManifest created from the recursed contents of
	 *         the directory passed as an argument
	 */
	public final static ByteStreamManifest byteStreamManifestFromDirectory(
			final File directory) {
		// Get the entry set
		EntryManifest entries = entryManifestFromDirectory(directory);
		// Create a root string for creating file names
		String pathRoot = directory.getAbsolutePath() + File.separator;
		// And the map for constructing the manifest plus a counter for bytes
		// read
		Set<EntryTuple> identifiedEntries = new HashSet<EntryTuple>();
		long readBytes = 0L;
		// Iterate the entries
		for (Entry entry : entries.getEntries()) {
			// Create a new file & a byte stream from it
			File file = new File(pathRoot + entry.getName());
			ByteStreamId byteStream;
			try {
				byteStream = ByteStreams.idFromFile(file);
				identifiedEntries
						.add(EntryTupleImpl.fromValues(
								EntryImpl.fromEntry(entry, EntryStatus.OK),
								byteStream));
				readBytes += byteStream.getLength();
			} catch (FileNotFoundException excep) {
				identifiedEntries.add(EntryTupleImpl.fromValues(EntryImpl
						.fromValues(entry.getName(), entry.getSize(),
								entry.getModifiedTime(), EntryStatus.LOST),
						ByteStreams.unidentifiedByteStreamId()));
			} catch (IOException excep) {
				identifiedEntries.add(EntryTupleImpl.fromValues(EntryImpl
						.fromValues(entry.getName(), entry.getSize(),
								entry.getModifiedTime(), EntryStatus.DAMAGED),
						ByteStreams.unidentifiedByteStreamId()));
			}
			// Add them to the map and add the bytes to the count
		}
		// and create the returned instance
		return ByteStreamManifestImpl.fromValues(entries.getTotalSize(),
				readBytes, identifiedEntries);
	}
	
	/**
	 * @param entry
	 * @param byteStreamId
	 * @return
	 */
	public static final EntryTuple entryTupleFromValues(final Entry entry, final ByteStreamId byteStreamId) {
		return EntryTupleImpl.fromValues(entry, byteStreamId);
	}
	/**
	 * @param manifest
	 * @return the pathDepth
	 */
	public final static int countPathDepth(ByteStreamManifest manifest) {
		int pathDepth = 0;
		for (EntryTuple entry : manifest.getEntryTuples()) {
			String pathPart = FilenameUtils.getFullPathNoEndSeparator(entry.getEntry().getName());
			int entryPathDepth = pathPart.split("/").length;
			pathDepth = (entryPathDepth > pathDepth) ? entryPathDepth : pathDepth;
		}
		return pathDepth;
	}
}

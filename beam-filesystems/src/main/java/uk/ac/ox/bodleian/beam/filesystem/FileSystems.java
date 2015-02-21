/**
 * 
 */
package uk.ac.ox.bodleian.beam.filesystem;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.io.FilenameUtils;

import uk.ac.ox.bodleian.beam.filesystem.entry.FileSystemEntry;
import uk.ac.ox.bodleian.beam.filesystem.iso.ISOFileSystemManager;
import uk.ac.ox.bodleian.beam.filesystem.iso.ISOFileSystemManager.ISOMountException;

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
	/** The standard three character extension used for iso image files */
	public static final String ISO_EXT = "iso";
	/** The standard three character extension used for zip image files */
	public static final String ZIP_EXT = "zip";
	/** The standard three character extension used for tar files */
	public static final String TAR_EXT = "tar";
	/** The standard three character extension used for gzip files */
	public static final String GZIP_EXT = "gz";

	/**
	 * 
	 */
	public static final Set<String> SUPPORTED_EXTS;
	static {
		Set<String> exts = new HashSet<String>();
		exts.add(ISO_EXT);
		exts.add(GZIP_EXT);
		exts.add(TAR_EXT);
		exts.add(ZIP_EXT);
		SUPPORTED_EXTS = Collections.unmodifiableSet(exts);
	}

	private FileSystems() {
		/** disable default constructor */
		throw new AssertionError("In disabled private constructor.");
	}
	
	/**
	 * @param ext
	 * @return true if the extension is supported
	 */
	public static boolean isSupportedExt(String ext) {
		return SUPPORTED_EXTS.contains(ext);
	}

	/**
	 * @param firstPath
	 * @param secondPath
	 * @return the relative path to use as an entry name
	 */
	public static String relatavisePaths(String firstPath, String secondPath) {
		if (secondPath.startsWith(firstPath)) {
			return secondPath
					.substring(firstPath.length() + 1, secondPath.length())
					.replace("\\", "/").trim();
		}
		return "";
	}

	/**
	 * Build an FileSystem from a root directory, built by traversing from the root directory, and named from the directory name.
	 * @param directory the root directory to be traversed, must be a non null, existing directory
	 * @return a new {@link FileSystem} created from the directory
	 */
	public static final JavaIOFileSystem fromDirectory(File directory) {
		if (directory == null) throw new IllegalArgumentException("directory == null");
		return fromDirectory(directory.getName(), directory);
	}

	/**
	 * Build an FileSystem from a root directory, built by traversing from the root directory, and named from the directory name.
	 * @param name the name of the file system
	 * @param directory the root directory to be traversed, must be a non null, existing directory
	 * @return a new {@link FileSystem} created from the directory
	 */
	public static final JavaIOFileSystem fromDirectory(String name, File directory) {
		if (name == null) throw new IllegalArgumentException("name == null");
		if (directory == null) throw new IllegalArgumentException("directory == null");
		if (!directory.isDirectory()) {
			throw new IllegalArgumentException("directory parameter must be an existing directory");
		}
		// Create an entry set
		Map<String, FileSystemEntry> entries = new HashMap<String, FileSystemEntry>();
		// Populate the entry set and get the total size
		long totalSize = JavaIOFileSystem.populateEntriesFromDirectory(directory, entries, directory.getAbsolutePath());
		// Return the JavaIOFileSystem
		return new JavaIOFileSystem(directory.getName(), directory.getAbsolutePath(), totalSize, entries);
	}
	
	/**
	 * @param file
	 * @return the appropriate FileSystem
	 * @throws UnsupportedOperationException
	 * @throws ISOMountException
	 * @throws IOException
	 * @throws ArchiveException
	 */
	public static final FileSystem fromFile(File file) 
		throws UnsupportedOperationException, ISOMountException, IOException, ArchiveException {
		// Check the file extension
		String ext = FilenameUtils.getExtension(file.getName());
		if (ext.equalsIgnoreCase(ISO_EXT))
				return ISOFileSystemManager.getFileSystemFromISOFile(file);
		else if (ext.equalsIgnoreCase(ZIP_EXT) || ext.equalsIgnoreCase(TAR_EXT)) {
				return ArchiveFileSystem.getInstanceFromFile(file);
		}
		throw new UnsupportedOperationException();
	}
}

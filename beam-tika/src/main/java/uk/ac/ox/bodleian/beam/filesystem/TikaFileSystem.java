/**
 * 
 */
package uk.ac.ox.bodleian.beam.filesystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreams;
import uk.ac.ox.bodleian.beam.filesystem.entry.FileSystemEntries;
import uk.ac.ox.bodleian.beam.filesystem.entry.FileSystemEntry;
import uk.ac.ox.bodleian.beam.tika.TikaWrapper;


/**
 * TODO: JavaDoc for TestHashedFileSystem.<p/>
 * TODO: Tests for TestHashedFileSystem.<p/>
 *
 * @author <a href="mailto:carl.wilson@keepitdigital.eu">Carl Wilson</a> <a
 *         href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1
 * 
 *          Created 6 Jul 2012:14:18:34
 */
public final class TikaFileSystem extends AbstractFileSystem {
	private static Logger LOGGER = Logger.getLogger(TikaFileSystem.class);
	private static TikaWrapper TIKA = TikaWrapper.getTika();
	private final static String TYPE = "[Tika]";
	private final String rootPath;
	private TikaFileSystem(final File rootDir, final long totalSize, final Map<String, FileSystemEntry> entries) {
		super(rootDir.getName(), TYPE, rootDir.getAbsolutePath(), totalSize, entries);
		this.rootPath = rootDir.getName();
		this.populatedFromDir(rootDir);
	}
	
	private void populatedFromDir(File directory) {
		directory.exists();
		// do nothing
	}
	
	/**
	 * @return the root directory of the collection
	 */
	public final String getRootPath() {return this.rootPath;}

	/**
	 * @see uk.ac.ox.bodleian.beam.filesystem.FileSystem#getEntryStream(java.lang.String)
	 */
	@Override
	public InputStream getEntryStream(final String name)
			throws EntryNotFoundException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see uk.ac.ox.bodleian.beam.filesystem.FileSystem#getEntryFile(java.lang.String)
	 */
	@Override
	public final File getEntryFile(final String name) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Utility method to list the file paths beneath a directory with recursion
	 * option.
	 * 
	 * @param rootDir
	 *            the root directory to traverse
	 * @return a List of String paths to the files
	 * @throws FileNotFoundException
	 *             if the directory doesn't exist
	 * @throws IllegalArgumentException
	 *             if the rootDir arg is null, or is not a directory
	 */
	public static final TikaFileSystem getInstanceFromDirectory(final File rootDir)
			throws FileNotFoundException {
		// First defensive checks on the file arg
		if (rootDir == null)
			throw new IllegalArgumentException("rootDir == null.");
		if (!rootDir.isDirectory())
			throw new IllegalArgumentException("rootDir.isDirectory() != true");
		// New file list for the children
		Map<String, FileSystemEntry> entries = new HashMap<String, FileSystemEntry>();
		String rootPath = rootDir.getAbsolutePath();
		long totalSize = JavaIOFileSystem.populateEntriesFromDirectory(rootDir, entries, rootPath);
		Map<String, FileSystemEntry> entryMap = checkEntries(new HashSet<FileSystemEntry>(entries.values()), totalSize, rootPath);
		return new TikaFileSystem(rootDir, totalSize, entryMap);
	}
	
	/**
	 * Utility method to list the file paths beneath a directory with recursion
	 * option.
	 * 
	 * @param rootDir
	 *            the root directory to traverse
	 * @param fileSystem 
	 * @return a List of String paths to the files
	 * @throws FileNotFoundException
	 *             if the directory doesn't exist
	 * @throws IllegalArgumentException
	 *             if the rootDir arg is null, or is not a directory
	 */
	public static final TikaFileSystem getInstanceFromDirectory(final File rootDir, FileSystem fileSystem)
			throws FileNotFoundException {
		// First defensive checks on the file arg
		// First defensive checks on the file arg
		if (rootDir == null)
			throw new IllegalArgumentException("rootDir == null.");
		if (!rootDir.isDirectory())
			throw new IllegalArgumentException("rootDir.isDirectory() != true");
		if (fileSystem == null)
			throw new IllegalArgumentException("fileSystem == null");
		// New file list for the children
		String rootPath = rootDir.getAbsolutePath();
		Map<String, FileSystemEntry> entryMap = checkEntries(fileSystem.getEntries(), fileSystem.getTotalBytes(), rootPath);
		return new TikaFileSystem(rootDir, fileSystem.getTotalBytes(), entryMap);
	}
	
	
	private static final Map<String, FileSystemEntry> checkEntries(Set<FileSystemEntry> entries, long totalSize, String rootPath) {
		// new map to return
		Map<String, FileSystemEntry> entryMap = new HashMap<String, FileSystemEntry>();
		LOGGER.info("[TikaFileSystem] HASH checking " + entries.size() + " files, " + ByteStreams.humanReadableByteCount(totalSize, false) + ".");
		for (FileSystemEntry entry : entries) {
			// Create the relative name
			File entryFile =  new File(rootPath + File.separator + entry.getName());
			FileSystemEntry details = FileSystemEntries.fromFile(entry.getName(), entryFile);
			try {
				TIKA.getMediaType(entryFile);
			} catch (IOException excep) {
				// TODO Auto-generated catch block
				excep.printStackTrace();
			}
			entryMap.put(entry.getName(), details);
		//	processedBytes += entryFile.length();
		}
		return Collections.unmodifiableMap(entryMap);
	}

	/**
	 * @return the input stream
	 * @throws UnsupportedOperationException
	 * @throws FileNotFoundException
	 * @throws DamagedEntryException
	 */
	public InputStream getFileSystemStream()
			throws UnsupportedOperationException, FileNotFoundException,
			DamagedEntryException {
		throw new UnsupportedOperationException();
	}

	@Override
	public FileSystemEntry getNextEntry() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getEntryStream() throws EntryNotFoundException,
			DamagedEntryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void resetEntries() throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetEntries(InputStream stream) {
		// TODO Auto-generated method stub
		
	}
}

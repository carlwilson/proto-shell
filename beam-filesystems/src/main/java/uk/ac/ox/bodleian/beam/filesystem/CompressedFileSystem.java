/**
 * 
 */
package uk.ac.ox.bodleian.beam.filesystem;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.gzip.GzipUtils;

import uk.ac.ox.bodleian.beam.filesystem.entry.FileSystemEntries;
import uk.ac.ox.bodleian.beam.filesystem.entry.FileSystemEntry;

/**
 * TODO: JavaDoc for CompressedFileSystem.<p/>
 * TODO: Tests for CompressedFileSystem.<p/>
 * 
 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a> <a
 *         href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 24 Jul 2012:02:46:36
 */

public class CompressedFileSystem extends AbstractFileSystem {
	private static CompressorStreamFactory FACT = new CompressorStreamFactory();
	private final static String TYPE = "[Commons-Compress]";
	private CompressorInputStream cis;
	boolean isAvailable = false;
	private FileSystemEntry entry;

	/**
	 * @param name
	 * @param path
	 * @param totalSize
	 * @param entries
	 */
	private CompressedFileSystem(final String name, final String path,
			final long totalSize, final Map<String, FileSystemEntry> entries) {
		super(name, path, TYPE, totalSize, entries);
		FileSystemEntry entArray [] = null;
		this.entry = entries.values().toArray(entArray)[0];
	}

	/**
	 * @param file
	 * @param name
	 * @param path
	 * @param totalSize
	 * @param entries
	 */
	private CompressedFileSystem(final File file, final long totalSize,
			final Map<String, FileSystemEntry> entries) {
		super(file.getName(), file.getAbsolutePath(), TYPE, totalSize, entries);
	}

	/**
	 * @param file
	 * @return a new instance created from the file
	 * @throws FileNotFoundException 
	 * @throws FileSystemException 
	 */
	public static final FileSystem getInstanceFromFile(final File file) throws FileNotFoundException, FileSystemException
			{
		// First defensive checks on the file arg
		if (file == null)
			throw new IllegalArgumentException("file == null");
		if (!file.isFile())
			throw new IllegalArgumentException("file.isFile() != true for " + file);
		BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));
		CompressorInputStream cis = null;
		try {
			cis = FACT
					.createCompressorInputStream(bis);
		} catch (CompressorException excep) {
			// TODO Auto-generated catch block
			throw new FileSystemException("CompressorException opening file " + file, excep);
		} finally {
			try {
				bis.close();
			} catch (IOException excep) {
				// DO NOTHING
			}
		}
		FileSystemEntry entry = null;
		try {
			entry = FileSystemEntries.fromValues(GzipUtils.getUncompressedFilename(file.getName()), file.length(),
					file.lastModified());
		} finally {
			try {
				cis.close();
			} catch (IOException excep) {
				// DO NOTHING
			}
		}
		Map<String, FileSystemEntry> entries = new HashMap<String, FileSystemEntry>();
		entries.put(entry.getName(), entry);
		return new CompressedFileSystem(file, file.length(), entries);
	}

	@Override
	public FileSystemEntry getNextEntry() throws IOException {
		if (!this.isAvailable) return null;
		return this.entry;
	}

	@Override
	public InputStream getEntryStream() throws EntryNotFoundException,
			DamagedEntryException {
		if (!this.isAvailable) return null;
		return new BufferedInputStream(this.cis);
	}

	@Override
	public void resetEntries() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void resetEntries(InputStream stream) throws IOException {
		this.cis.close();
		try {
			this.cis = FACT.createCompressorInputStream(stream);
			this.isAvailable = true;
		} catch (CompressorException excep) {
			throw new IOException(excep);
		}		
	}
	@Override
	public InputStream getEntryStream(String name)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public File getEntryFile(String name) throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}

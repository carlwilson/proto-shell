/**
 * 
 */
package uk.ac.ox.bodleian.beam.filesystem;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;

import uk.ac.ox.bodleian.beam.filesystem.entry.FileSystemEntries;
import uk.ac.ox.bodleian.beam.filesystem.entry.FileSystemEntry;

/**
 * TODO: JavaDoc for TarFileSystem.<p/>
 * TODO: Generalise TarFileSystem for commons-compress archives.<p/>
 * TODO: Tests for TarFileSystem.<p/>
 * 
 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a> <a
 *         href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 21 Jul 2012:21:38:13
 */

public final class ArchiveFileSystem extends AbstractFileSystem {
	private final static String TYPE = "[Commons-Archive]";
	private static final String E_NULL_ARG_MSG = "ZipFileSystem constructor File arg null.";
	private static final String E_TARFILE_ISDIR_MSG = "TarFileSystem constructor File arg is not a file ";
	private static ArchiveStreamFactory FACT = new ArchiveStreamFactory();
	private ArchiveInputStream ais;

	private ArchiveFileSystem(final String name, final String path, final long totalSize,
			final Map<String, FileSystemEntry> entries) {
		super(name, path, TYPE, totalSize, entries);
	}

	/**
	 * @param archFile 
	 * @return a new TarFileSystem instance created from the parsed stream
	 * @throws IOException 
	 * @throws ArchiveException 
	 */
	public static final ArchiveFileSystem getInstanceFromFile(final File archFile) throws IOException, ArchiveException  {
		// Defensive arg checks, make sure that the arg is not null, and is an
		// existing file
		if (archFile == null)
			throw new IllegalArgumentException(E_NULL_ARG_MSG);
		if (!archFile.isFile())
			throw new IllegalArgumentException(E_TARFILE_ISDIR_MSG
					+ archFile.getAbsolutePath() + ".");
		InputStream stream = new FileInputStream(archFile);
		ArchiveFileSystem afs = getInstanceFromStream(archFile.getName(), archFile.getAbsolutePath(), stream);
		stream.close();
		return afs;
	}

	/**
	 * @param name
	 * @param path
	 * @param stream
	 * @return a new TarFileSystem instance created from the parsed stream
	 * @throws IOException
	 * @throws ArchiveException 
	 */
	public static final ArchiveFileSystem getInstanceFromStream(final String name, final String path,
			InputStream stream) throws IOException, ArchiveException {
		Map<String, FileSystemEntry> entryMap = new HashMap<String, FileSystemEntry>();
		BufferedInputStream bis = new BufferedInputStream(stream);
		long totalBytes = getEntriesFromStream(bis, entryMap);
		bis.close();
		return new ArchiveFileSystem(name, path, totalBytes, Collections.unmodifiableMap(entryMap));
	}
	
	private static final long getEntriesFromStream(InputStream stream, Map<String, FileSystemEntry> entryMap) throws IOException, ArchiveException {
		long totalBytes = 0L;
		ArchiveInputStream ais = FACT.createArchiveInputStream(stream);
		ArchiveEntry entry = null;
		try {
			while ((entry = ais.getNextEntry()) != null) {
				if (!entry.isDirectory() && ais.canReadEntryData(entry)) {
					FileSystemEntry fse = FileSystemEntries.fromArchive(entry);
					entryMap.put(entry.getName(), fse);
					totalBytes += entry.getSize();
				}
			}
		} catch (IOException excep) {
			LOGGER.warn(excep);
			throw excep;
		} finally {
			try {
				ais.close();
			} catch (IOException excep) {
				LOGGER.trace(excep);
			}
		}
		return totalBytes;
	}

	@Override
	public FileSystemEntry getNextEntry() throws IOException {
			return FileSystemEntries.fromArchive(this.ais.getNextEntry());
	}

	@Override
	public InputStream getEntryStream() throws EntryNotFoundException,
			DamagedEntryException {
		return new BufferedInputStream(this.ais);
	}

	@Override
	public void resetEntries() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void resetEntries(InputStream stream) throws IOException {
		this.ais.close();
		try {
			this.ais = FACT.createArchiveInputStream(stream);
		} catch (ArchiveException excep) {
			// TODO Auto-generated catch block
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

/**
 * 
 */
package uk.ac.ox.bodleian.beam.filesystem;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import uk.ac.ox.bodleian.beam.filesystem.FileSystem.DamagedEntryException;
import uk.ac.ox.bodleian.beam.filesystem.FileSystem.EntryNotFoundException;
import uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreamId;
import uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreamInstance;
import uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreams;
import uk.ac.ox.bodleian.beam.filesystem.entry.FileSystemEntry;

/**
 * TODO: JavaDoc for HashedFileSystem.
 * <p/>
 * TODO: Tests for HashedFileSystem.
 * <p/>
 * 
 * @author <a href="mailto:carl.wilson@keepitdigital.eu">Carl Wilson</a> <a
 *         href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1
 * 
 *          Created 6 Jul 2012:14:18:34
 */
public class HashedFileSystem {
	private final FileSystem fileSystem;
	private volatile long bytesProcessed;
	private volatile int entriesProcessed;
	private Map<String, ByteStreamInstance> entryByteStreams = new HashMap<String, ByteStreamInstance>();

	/**
	 * @param fileSystem
	 */
	public HashedFileSystem(final FileSystem fileSystem) {
		this.fileSystem = fileSystem;
	}

	/**
	 * @return the underlying file system
	 */
	public FileSystem getFileSystem() {
		return this.fileSystem;
	}
	
	/**
	 * @return the number of bytes processed so far
	 */
	public long getBytesProcessed() {
		return this.bytesProcessed;
	}
	
	/**
	 * @return the total number of bytes to process
	 */
	public long getTotalBytes() {
		return this.fileSystem.getTotalBytes();
	}
	
	/**
	 * @return the number of entries processed
	 */
	public int getEntriesProcessed() {
		return this.entriesProcessed;
	}
	
	/**
	 * @return the total number of entries
	 */
	public int getTotalEntries() {
		return this.fileSystem.getEntryCount();
	}

	/**
	 * @param entryName
	 * @return the ByteStreamInstance for the entry name
	 */
	public ByteStreamInstance getEntryByteStream(String entryName) {
		if (entryName == null) throw new IllegalArgumentException("entryName == null");
		return this.entryByteStreams.get(entryName);
	}

	/**
	 * process the underlying file system, check hashes 
	 * @throws IOException 
	 */
	@SuppressWarnings("resource")
	public void hashCheckFileSystem() throws IOException {
		this.entryByteStreams.clear();
		this.bytesProcessed = 0L;
		this.entriesProcessed = 0;
	
		FileSystemEntry entry = null;
		while ((entry = this.fileSystem.getNextEntry()) != null) {
			InputStream is = null;
			ByteStreamInstance instance = null;
			try {
				is = this.fileSystem.getEntryStream();
				ByteStreamId byteStream = ByteStreams.fromInputStream(is);
				instance = ByteStreams.getOkInstance(byteStream);
			} catch (UnsupportedOperationException excep) {
				instance = ByteStreams.getUncheckedInstance(excep);
			} catch (EntryNotFoundException excep) {
				instance = ByteStreams.getLostInstance(excep);
			} catch (DamagedEntryException excep) {
				instance = ByteStreams.getDamagedInstance(excep);
			} catch (IOException excep) {
				instance = ByteStreams.getDamagedInstance(excep);
			} finally {
				this.entryByteStreams.put(entry.getName(), instance);
				try {
					if (is != null)
						is.close();
				} catch (IOException excep) {
					// Do nothing
				}
			}
			this.bytesProcessed+=entry.getSize();
			this.entriesProcessed++;
		}
	}
}

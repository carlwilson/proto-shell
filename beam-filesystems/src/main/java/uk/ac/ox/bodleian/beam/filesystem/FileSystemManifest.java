/**
 * 
 */
package uk.ac.ox.bodleian.beam.filesystem;

import java.util.Set;

import uk.ac.ox.bodleian.beam.filesystem.entry.FileSystemEntry;

/**
 * TODO JavaDoc for EntryManifest.</p>
 * TODO Tests for EntryManifest.</p>
 * TODO Implementation for EntryManifest.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 17 Sep 2012:11:18:18
 */

public interface FileSystemManifest {
	/**
	 * @return the name given to the file system, always a non null, non empty
	 *         String.
	 */
	public String getName();

	/**
	 * @return the path to the file system "root", always a non null, non empty
	 *         String.
	 */
	public String getPath();

	/**
	 * @return the number of entries in the file system, an int >= 0.
	 */
	public int getEntryCount();

	/**
	 * @return the total size of the file system bytes, a long >= 0L.
	 */
	public long getTotalBytes();

	/**
	 * @return the full set of FileSystemEntries for the file system
	 */
	public Set<FileSystemEntry> getEntries();


}

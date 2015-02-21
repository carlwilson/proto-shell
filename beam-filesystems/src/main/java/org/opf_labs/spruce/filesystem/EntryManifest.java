/**
 * 
 */
package org.opf_labs.spruce.filesystem;

import java.util.Set;

/**
 * Interface that presents behaviour of a File System Manifest. This is defined
 * as a Set&lt;Entry&gt; that was reported back by the file system
 * implementation. This is NOT necessarily an accurate record of the contents of
 * the FileSystem, but what the FileSystem implementation believes should be
 * contained by the file system. TODO JavaDoc for EntryManifest.</p> TODO Tests
 * for EntryManifest.</p> TODO Implementation for EntryManifest.</p>
 * 
 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 *          Created 17 Sep 2012:11:18:18
 */
public interface EntryManifest {
	/**
	 * @return the total size of the file system in bytes calculated from the
	 *         length reported from the entries / file system. Due to
	 *         compression and reporting this may not be the same length as the
	 *         total number of bytes that would be read from the entries. If the
	 *         length is unknown -1L is returned otherwise a a long >= 0L.
	 */
	public long getTotalSize();

	/**
	 * @return the full set of FileSystemEntries for the file system
	 */
	public Set<? extends Entry> getEntries();

	/**
	 * @param manifest
	 *            to compare
	 * @return -1 if the item less than id, 0 if equal, 1 if greater
	 */
	public int compareTo(final EntryManifest manifest);
}

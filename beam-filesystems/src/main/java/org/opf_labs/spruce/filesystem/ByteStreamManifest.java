/**
 * 
 */
package org.opf_labs.spruce.filesystem;

import java.util.Set;

/**
 * TODO JavaDoc for ByteStreamManifest.</p> TODO Tests for
 * ByteStreamManifest.</p> TODO Implementation for ByteStreamManifest.</p>
 * 
 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 *          Created 3 Oct 2012:11:56:08
 */
public interface ByteStreamManifest {
	
	/**
	 * @return the number of bytes actually counted from reading the byte streams
	 */
	public long getTotalSize();
	
	/**
	 * @return the number of bytes reported in the file entries
	 */
	public long getEntrySize();

	/**
	 * @return all of the checked entries, with either a hash identifier or an entry status of DAMAGED, or LOST
	 */
	public Set<EntryTuple> getEntryTuples();

	/**
	 * @param manifest
	 * @return -1 if the item less than id, 0 if equal, 1 if greater
	 */
	public int compareTo(final ByteStreamManifest manifest);
}

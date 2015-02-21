/**
 * 
 */
package org.opf_labs.spruce.filesystem;

import org.opf_labs.spruce.bytestream.ByteStreamId;

/**
 * TODO JavaDoc for EntryTuple.</p>
 * TODO Tests for EntryTuple.</p>
 * TODO Implementation for EntryTuple.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 9 Oct 2012:15:31:02
 */
public interface EntryTuple {

	/**
	 * @return the FileSystem entry part
	 */
	public Entry getEntry();

	/**
	 * @return the byte stream id
	 */
	public ByteStreamId getByteStreamId();
	
	/**
	 * @param entry
	 *            the entry to compare
	 * @return -1 if the item less than id, 0 if equal, 1 if greater
	 */
	public int compareTo(final EntryTuple entry);
}

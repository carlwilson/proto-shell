/**
 * 
 */
package uk.ac.ox.bodleian.beam.model;

import java.util.Set;

import uk.ac.ox.bodleian.beam.filesystem.FileSystem;
import uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreamId;
import uk.ac.ox.bodleian.beam.tika.TikaDetails;

/**
 * TODO JavaDoc for ByteStreamRegistry.</p>
 * TODO Tests for ByteStreamRegistry.</p>
 * TODO Implementation for ByteStreamRegistry.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 17 Sep 2012:09:26:06
 */

public interface ByteStreamRegistry {
	/**
	 * @return the set of Byte Streams
	 */
	public Set<ByteStreamId> getByteStreams();
	/**
	 * @param byteStream
	 * @param tikaDetails
	 * @return the TikaDetails added
	 */
	public TikaDetails putTikaDetails(ByteStreamId byteStream, TikaDetails tikaDetails);
	/**
	 * @param byteStream
	 * @return true of the byte stream has been registered
	 */
	public boolean isByteStreamRegistered(ByteStreamId byteStream);
	/**
	 * @param byteStream
	 * @return the TikaDetails for this byte stream
	 */
	public TikaDetails getTikaDetails(ByteStreamId byteStream);
	/**
	 * @param byteStream
	 * @return true if the byte stream is an aggregate file system
	 */
	public boolean isByteStreamAggregate(ByteStreamId byteStream);
	/**
	 * @param byteStream
	 * @param fileSystem
	 * @return the FileSytem added to the registry
	 */
	public FileSystem putAggregateByteStream(ByteStreamId byteStream, FileSystem fileSystem);
	/**
	 * @param byteStream
	 * @return the File system for a particular aggregate
	 */
	public FileSystem getAggregateFileSystem(ByteStreamId byteStream);
}

/**
 * 
 */
package uk.ac.ox.bodleian.beam.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import uk.ac.ox.bodleian.beam.filesystem.FileSystem;
import uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreamId;
import uk.ac.ox.bodleian.beam.tika.TikaDetails;

/**
 * TODO JavaDoc for ByteStreamRegistryImpl.</p> TODO Tests for
 * ByteStreamRegistryImpl.</p> TODO Implementation for
 * ByteStreamRegistryImpl.</p>
 * 
 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 *          Created 17 Sep 2012:09:34:25
 */

public class ByteStreamRegistryImpl implements ByteStreamRegistry {
	private static Map<ByteStreamId, TikaDetails> TIKA_MAP = new HashMap<ByteStreamId, TikaDetails>();
	private static Map<ByteStreamId, FileSystem> AGGREGATES = new HashMap<ByteStreamId, FileSystem>();
	private static ByteStreamRegistry INSTANCE = new ByteStreamRegistryImpl();

	private ByteStreamRegistryImpl() {
		throw new AssertionError();
	}

	static ByteStreamRegistry getInstance() {
		return INSTANCE;
	}

	/**
	 * @see uk.ac.ox.bodleian.beam.model.ByteStreamRegistry#getByteStreams()
	 */
	@Override
	public Set<ByteStreamId> getByteStreams() {
		return TIKA_MAP.keySet();
	}

	/**
	 * @see uk.ac.ox.bodleian.beam.model.ByteStreamRegistry#putTikaDetails(uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreamId,
	 *      uk.ac.ox.bodleian.beam.tika.TikaDetails)
	 */
	@Override
	public TikaDetails putTikaDetails(ByteStreamId byteStream,
			TikaDetails tikaDetails) {
		// TODO Auto-generated method stub
		return TIKA_MAP.put(byteStream, tikaDetails);
	}

	/**
	 * @see uk.ac.ox.bodleian.beam.model.ByteStreamRegistry#isByteStreamRegistered(uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreamId)
	 */
	@Override
	public boolean isByteStreamRegistered(ByteStreamId byteStream) {
		return TIKA_MAP.containsKey(byteStream);
	}

	/**
	 * @see uk.ac.ox.bodleian.beam.model.ByteStreamRegistry#getTikaDetails(uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreamId)
	 */
	@Override
	public TikaDetails getTikaDetails(ByteStreamId byteStream) {
		return TIKA_MAP.get(byteStream);
	}

	/**
	 * @see uk.ac.ox.bodleian.beam.model.ByteStreamRegistry#isByteStreamAggregate(uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreamId)
	 */
	@Override
	public boolean isByteStreamAggregate(ByteStreamId byteStream) {
		return AGGREGATES.containsKey(byteStream);
	}


	@Override
	public FileSystem putAggregateByteStream(ByteStreamId byteStream,
			FileSystem fileSystem) {
		return AGGREGATES.put(byteStream, fileSystem);
	}

	@Override
	public FileSystem getAggregateFileSystem(ByteStreamId byteStream) {
		// TODO Auto-generated method stub
		return AGGREGATES.get(byteStream);
	}

}

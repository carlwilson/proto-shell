package org.opf_labs.spruce.filesystem;

import org.opf_labs.spruce.bytestream.ByteStreamId;
import org.opf_labs.spruce.bytestream.ByteStreams;

/**
 * TODO JavaDoc for EntryTupleImpl.</p>
 * TODO Tests for EntryTupleImpl.</p>
 * TODO Implementation for EntryTupleImpl.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 9 Oct 2012:15:36:06
 */
public final class EntryTupleImpl implements EntryTuple, Comparable<EntryTuple> {
	private final Entry entry;
	private final ByteStreamId byteStreamId;
	
	private EntryTupleImpl() {
		this(FileSystems.nullEntry(), ByteStreams.nullByteStreamId());
	}
	
	private EntryTupleImpl(final Entry entry, final ByteStreamId byteStreamId) {
		this.entry = entry;
		this.byteStreamId = byteStreamId;
	}

	static final EntryTupleImpl fromValues(final Entry entry, final ByteStreamId byteStreamId) {
		if (entry == null) throw new IllegalArgumentException("entry == null");
		if (byteStreamId == null) throw new IllegalArgumentException("byteStreamId == null");
		return new EntryTupleImpl(entry, byteStreamId);
	}

	/**
	 * @see org.opf_labs.spruce.filesystem.EntryTuple#getEntry()
	 */
	@Override
	public Entry getEntry() {
		return this.entry;
	}

	/**
	 * @see org.opf_labs.spruce.filesystem.EntryTuple#getByteStreamId()
	 */
	@Override
	public ByteStreamId getByteStreamId() {
		return this.byteStreamId;
	}

	@Override
	public int compareTo(EntryTuple entry) {
		// First by entry, then by byte stream
		int entryDiff = this.entry.compareTo(entry.getEntry());
		return (entryDiff != 0) ? entryDiff : this.byteStreamId.compareTo(entry.getByteStreamId());
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((this.byteStreamId == null) ? 0 : this.byteStreamId
						.hashCode());
		result = prime * result
				+ ((this.entry == null) ? 0 : this.entry.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof EntryTupleImpl)) {
			return false;
		}
		EntryTupleImpl other = (EntryTupleImpl) obj;
		if (this.byteStreamId == null) {
			if (other.byteStreamId != null) {
				return false;
			}
		} else if (!this.byteStreamId.equals(other.byteStreamId)) {
			return false;
		}
		if (this.entry == null) {
			if (other.entry != null) {
				return false;
			}
		} else if (!this.entry.equals(other.entry)) {
			return false;
		}
		return true;
	}

}

/**
 * 
 */
package org.opf_labs.spruce.filesystem;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * TODO JavaDoc for ByteStreamManifestImpl.</p> TODO Tests for
 * ByteStreamManifestImpl.</p> TODO Implementation for
 * ByteStreamManifestImpl.</p>
 * 
 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 *          Created 3 Oct 2012:14:33:40
 */
public final class ByteStreamManifestImpl implements ByteStreamManifest,
		Comparable<ByteStreamManifest> {
	static final ByteStreamManifestImpl NULL_MANIFEST = new ByteStreamManifestImpl();
	private final long totalSize;
	private final long entrySize;
	private final SortedSet<EntryTuple> entryTuples;

	private ByteStreamManifestImpl() {
		this.totalSize = 0L;
		this.entrySize = 0L;
		this.entryTuples = new TreeSet<EntryTuple>();
	}

	private ByteStreamManifestImpl(final long totalSize,
			final long countedBytes,
			final Set<EntryTuple> identifiedEntries) {
		this.totalSize = totalSize;
		this.entrySize = countedBytes;
		this.entryTuples = Collections.unmodifiableSortedSet(new TreeSet<EntryTuple>(identifiedEntries));
	}

	static ByteStreamManifestImpl fromValues(final long totalSize,
			final long countedBytes,
			final Set<EntryTuple> indentifiedEntries) {
		if (totalSize < 0L)
			throw new IllegalArgumentException("totalSize < 0L");
		if (countedBytes < 0L)
			throw new IllegalArgumentException("entrySize < 0L");
		if (indentifiedEntries == null)
			throw new IllegalArgumentException("entryTuples == null");
		return new ByteStreamManifestImpl(totalSize, countedBytes, indentifiedEntries);
	}

	/**
	 * @see org.opf_labs.spruce.filesystem.ByteStreamManifest#getTotalSize()
	 */
	@Override
	public final long getTotalSize() {
		return this.totalSize;
	}

	/**
	 * @see org.opf_labs.spruce.filesystem.ByteStreamManifest#getEntrySize()
	 */
	@Override
	public final long getEntrySize() {
		return this.entrySize;
	}

	/**
	 * @see org.opf_labs.spruce.filesystem.ByteStreamManifest#getEntryTuples()
	 */
	@Override
	public Set<EntryTuple> getEntryTuples() {
		return this.entryTuples;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		return "ByteStreamManifestImpl [entryTuples=" + this.entryTuples
				+ ", totalSize=" + this.totalSize + ", entrySize="
				+ this.entrySize + "]";
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (int) (this.entrySize ^ (this.entrySize >>> 32));
		result = prime * result
				+ ((this.entryTuples == null) ? 0 : this.entryTuples.hashCode());
		result = prime * result
				+ (int) (this.totalSize ^ (this.totalSize >>> 32));
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ByteStreamManifest)) {
			return false;
		}
		ByteStreamManifest other = (ByteStreamManifest) obj;
		if (this.entrySize != other.getEntrySize()) {
			return false;
		}

		if (this.totalSize != other.getTotalSize()) {
			return false;
		}

		if (this.entryTuples == null) {
			if (other.getEntryTuples() != null) {
				return false;
			}
		} else if (other.getEntryTuples() == null) {
			return false;
		} else if (this.entryTuples.size() != other.getEntryTuples().size()) {
			return false;
		}
		return this.entryTuples.containsAll(other.getEntryTuples());
	}

	/**
	 * @see org.opf_labs.spruce.filesystem.ByteStreamManifest#compareTo(org.opf_labs.spruce.filesystem.ByteStreamManifest)
	 */
	@Override
	public final int compareTo(final ByteStreamManifest other) {
		if (this == other) return 0;
		int entryCountDiff = this.entryTuples.size() - other.getEntryTuples().size();
		if (entryCountDiff != 0)
			return entryCountDiff;

		long sizeDiff = this.totalSize - other.getTotalSize();
		if (sizeDiff != 0)
			return (sizeDiff > 0) ? 1 : -1;

		sizeDiff = this.entrySize - other.getEntrySize();
		if (sizeDiff != 0)
			return (sizeDiff > 0) ? 1 : -1;

		int entryDiff = 0;
		Iterator<EntryTuple> thisIterator = this.getEntryTuples().iterator();
		Iterator<EntryTuple> otherIterator = other.getEntryTuples().iterator();

		while ((thisIterator.hasNext()) && (entryDiff == 0)) {
			EntryTuple thisEntry = thisIterator.next();
			EntryTuple otherEntry = otherIterator.next();
			entryDiff = thisEntry.compareTo(otherEntry);
		}
		return entryDiff;
	}
}

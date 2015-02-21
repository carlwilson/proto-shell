/**
 * 
 */
package org.opf_labs.spruce.filesystem;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * TODO JavaDoc for EntryManifestImpl.</p>
 * TODO Tests for EntryManifestImpl.</p>
 * TODO Implementation for EntryManifestImpl.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 2 Oct 2012:14:47:22
 */
public final class EntryManifestImpl implements EntryManifest, Comparable<EntryManifest> {
	static final EntryManifest NULL_MANIFEST = new EntryManifestImpl();
	
	private final long totalSize;
	private final Set<Entry> entries;

	private EntryManifestImpl() {
		this(0L, new HashSet<Entry>());
	}
	
	private EntryManifestImpl(final long totalSize, final Set<? extends Entry> entries) {
		this.totalSize = totalSize;
		this.entries = new HashSet<Entry>(entries);
	}
	
	final static EntryManifest getNullManifest() {
		return NULL_MANIFEST;
	}

	final static EntryManifestImpl fromValues(final long totalSize, final Set<? extends Entry> entries) {
		if (entries == null) throw new IllegalArgumentException("entries == null");
		if (totalSize < 0) throw new IllegalArgumentException("totalSize < 0");
		return new EntryManifestImpl(totalSize, entries);
	}

	final static EntryManifestImpl fromValues(final Set<Entry> entries) {
		if (entries == null) throw new IllegalArgumentException("entries == null");
		long totalSize = 0L;
		for (Entry entry : entries) {
			// Avoid subtracting unknown bytes
			long size = entry.getSize();
			totalSize += (size > 0) ? size : 0;
		}
		return new EntryManifestImpl(totalSize, entries);
	}

	/**
	 * @param directory
	 * @return a new EntryManifest created from the file system below the
	 *         directory.
	 */
	final static EntryManifestImpl fromDirectory(
			final File directory) {
		if (directory == null)
			throw new IllegalArgumentException("directory == null");
		if (!directory.isDirectory())
			throw new IllegalArgumentException("directory arg:" + directory
					+ " is not an existing directory.");
		Set<Entry> entries = new HashSet<Entry>();
		long totalSize = populateEntriesFromDirectory(directory, entries,
				directory.getAbsolutePath());
		return EntryManifestImpl.fromValues(totalSize, entries);
	}

	/**
	 * @see org.opf_labs.spruce.filesystem.EntryManifest#getTotalSize()
	 */
	@Override
	public final long getTotalSize() {
		return this.totalSize;
	}

	/**
	 * @see org.opf_labs.spruce.filesystem.EntryManifest#getEntries()
	 */
	@Override
	public final Set<Entry> getEntries() {
		return this.entries;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.entries == null) ? 0 : this.entries.hashCode());
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
		if (!(obj instanceof EntryManifest)) {
			return false;
		}
		EntryManifest other = (EntryManifest) obj;
		if (this.totalSize != other.getTotalSize()) {
			return false;
		}
		if (this.entries == null) {
			if (other.getEntries() != null) {
				return false;
			}
		} else if (other.getEntries() == null) {
			return false;
		} else if (this.entries.size() != other.getEntries().size()) {
			return false;
		} 
		return this.getEntries().containsAll(other.getEntries());
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		return "EntryManifest [totalSize=" + this.totalSize
				+ ", entries=" + this.entries + "]";
	}

	@Override
	public final int compareTo(final EntryManifest other) {
		int entryCountDiff = this.entries.size() - other.getEntries().size();
		if (entryCountDiff != 0) return entryCountDiff;
		
		long sizeDiff = this.totalSize - other.getTotalSize();
		if (sizeDiff != 0) return (sizeDiff > 0) ? 1 : -1;

		SortedSet<Entry> thisEntries = new TreeSet<Entry>(this.entries);
		SortedSet<Entry> otherEntries = new TreeSet<Entry>(other.getEntries());
		Iterator<Entry> thisIterator = thisEntries.iterator();
		Iterator<Entry> otherIterator = otherEntries.iterator();
		int entryDiff = 0;
		// Loop through until we find one that isn't equal, or finish when all equal
		while (thisIterator.hasNext() && (entryDiff == 0)) {
			Entry thisEntry = thisIterator.next();
			Entry otherEntry = otherIterator.next();
			entryDiff = thisEntry.compareTo(otherEntry);
		}
		return entryDiff;
	}

	static final long populateEntriesFromDirectory(final File dir,
			final Set<Entry> entries, final String rootPath) {
		long totalSize = 0L;
		File[] children = dir.listFiles();
		if (children != null) {
			// Loop through the java.io.File children
			for (File child : children) {
				if (child.isDirectory()) {
					totalSize += populateEntriesFromDirectory(child, entries,
							rootPath);
				} else if (child.isFile()) {
					String entryName = FileSystems.relatavisePaths(rootPath,
							child.getAbsolutePath());
					entries.add(FileSystems.uncheckedEntryFromFile(entryName, child));
					totalSize += child.length();
				}
			}
		}
		return totalSize;
	}
}

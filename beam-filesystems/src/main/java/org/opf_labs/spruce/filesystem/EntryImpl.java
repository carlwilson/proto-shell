/**
 * 
 */
package org.opf_labs.spruce.filesystem;

import org.apache.commons.io.FilenameUtils;
import org.opf_labs.spruce.bytestream.ByteStreams;

/**
 * TODO: Beter JavaDoc for EntryImpl.
 * <p/>
 * TODO: JavaDoc for inheritance
 * 
 * @author <a href="mailto:carl.wilson@keepitdigital.eu">Carl Wilson</a> <a
 *         href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 12 Jul 2012:00:44:17
 */
public final class EntryImpl implements Entry, Comparable<Entry> {
	private static String NULL_NAME = "";
	static final EntryImpl NULL_ENTRY = new EntryImpl();
	private final String name;
	private final long size;
	private final long modifiedTime;
	private final EntryStatus status;

	private EntryImpl() {
		this(NULL_NAME, ByteStreams.UNKNOWN_LENGTH, 0L);
	}

	private EntryImpl(final String name, final long size, final long modified) {
		this(name, size, modified, EntryStatus.UNCHECKED);
	}


	private EntryImpl(final String name, final long size, final long modified, final EntryStatus status) {
		this.name = name;
		this.size = size;
		this.modifiedTime = modified;
		this.status = status;
	}

	final static EntryImpl fromValues(final String name,
			final long size, final long modified, final EntryStatus status) {
		// OK all roads lead to this constructor so do the arg checks once here
		if (name == null)
			throw new IllegalArgumentException("name == null");
		if (status == null)
			throw new IllegalArgumentException("status == null");
		// Check consistency
		if (size < -0L)
			throw new IllegalArgumentException("(size " + size
					+ " < -0) == true");
		if (modified < 0L)
			throw new IllegalArgumentException("(modified " + modified
					+ " < 0) == true");
		return new EntryImpl(name, size, modified, status);
	}
	
	final static EntryImpl fromValues(final String name,
			final long size, final long modified) {
		return fromValues(name, size, modified, EntryStatus.UNCHECKED);
	}
	
	final static EntryImpl fromValues(final String name, final long modified) {
		// OK all roads lead to this constructor so do the arg checks once here
		return fromValues(name, ByteStreams.UNKNOWN_LENGTH, modified, EntryStatus.UNCHECKED);
	}

	final static EntryImpl fromEntry(final Entry entry, final EntryStatus status) {
		// OK all roads lead to this constructor so do the arg checks once here
		if (entry == null)
			throw new IllegalArgumentException("entry == null");
		return fromValues(entry.getName(), entry.getSize(), entry.getModifiedTime(), status);
	}

	/**
	 * @see org.opf_labs.spruce.filesystem.Entry#getName()
	 */
	@Override
	public final String getName() {
		return this.name;
	}

	/**
	 * @see org.opf_labs.spruce.filesystem.Entry#getSize()
	 */
	@Override
	public final long getSize() {
		return this.size;
	}

	/**
	 * @see org.opf_labs.spruce.filesystem.Entry#getModifiedTime()
	 */
	@Override
	public final long getModifiedTime() {
		return this.modifiedTime;
	}

	/**
	 * @see org.opf_labs.spruce.filesystem.Entry#getStatus()
	 */
	@Override
	public final EntryStatus getStatus() {
		return this.status;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "{\"Entry\": {\"name\":\"" + this.name + "\",\"size\":" + this.size
				+ ",\"modifiedTime\":" + this.modifiedTime + "}}";
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (int) (this.modifiedTime ^ (this.modifiedTime >>> 32));
		result = prime * result
				+ ((this.name == null) ? 0 : this.getName().hashCode());
		result = prime * result + (int) (this.size ^ (this.size >>> 32));
		result = prime * result + ((this.status == null) ? 0 : this.getStatus().hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Entry)) {
			return false;
		}
		Entry other = (Entry) obj;
		if (this.size != other.getSize()) {
			return false;
		}
		if (this.modifiedTime != other.getModifiedTime()) {
			return false;
		}
		if (this.status != other.getStatus()) {
			return false;
		}
		if (this.name == null) {
			if (other.getName() != null) {
				return false;
			}
		} else if (!this.name.equals(other.getName())) {
			return false;
		}
		return true;
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final Entry other) {
		// Compare the name path part first
		int pathDiff = FilenameUtils.getFullPath(this.name).compareTo(
				FilenameUtils.getFullPath(other.getName()));
		if (pathDiff != 0)
			return pathDiff;

		// Then compare the name and extension
		int nameDiff = FilenameUtils.getName(this.name).compareTo(
				FilenameUtils.getName(other.getName()));
		if (nameDiff != 0)
			return nameDiff;

		// Compare size
		long sizeDiff = this.size - other.getSize();
		if (sizeDiff != 0)
			return (sizeDiff > 0) ? 1 : -1;

		// And finally the time
		long timeDiff = this.modifiedTime - other.getModifiedTime();
		if (timeDiff != 0)
			return (timeDiff > 0) ? 1 : -1;

		return 0;
	}
}

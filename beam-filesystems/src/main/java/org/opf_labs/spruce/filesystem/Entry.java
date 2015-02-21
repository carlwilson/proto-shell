/**
 * 
 */
package org.opf_labs.spruce.filesystem;





/**
 * Encapsulates a few properties common to all Java file system entries:
 * <ul>
 * <li>
 * <p>
 * <strong>Name</strong>
 * </p>
 * <p>
 * The unique name of the entry within it's file system. Usually a relative path
 * and a name.
 * </p>
 * </li>
 * <li>
 * <p>
 * <strong>Size</strong>
 * </p>
 * <p>
 * The file system reported size of the entry in bytes, may not be identical to
 * the actual byte stream length due to compression, or damage to the
 * filesystem.
 * </p>
 * </li>
 * <li>
 * <p>
 * <strong>Modified Time</strong>
 * </p>
 * <p>
 * The reported modified data as a long containing milliseconds since
 * 01/01/1970.
 * </p>
 * </li>
 * </ul>
 * 
 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a> <a
 *         href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 11 Jul 2012:23:33:55
 */
public interface Entry {
	/**
	 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>
	 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
	 * @version 0.1 Created 12 Jul 2012:02:41:29
	 */
	public enum EntryStatus {
		/** indicates a damaged entry */
		UNCHECKED(0),
		/** indicates a damaged entry */
		DAMAGED(1),
		/** indicates a lost entry that was believed to be there */
		LOST(2),
		/** indicates all is OK */
		OK(3);
		private final int ordinal;
	
		private EntryStatus(int ordinal) {
			this.ordinal = ordinal;
		}
	
		/**
		 * @return the ordinal for the status
		 */
		public int getOrdinal() {
			return this.ordinal;
		}
	}

	/**
	 * @return the name given to the entry, often a relative path, always a non
	 *         null, non-empty string
	 */
	public String getName();

	/**
	 * @return the size of the entry in bytes, as reported by the file system,
	 *         and dependent upon implementation. This may not be the same as
	 *         the size of the ByteStream, due to compression, or a file system
	 *         error. Returns a value > -1, unless the size is unknown, then it returns -1.
	 */
	public long getSize();

	/**
	 * @return the entry's modified time as a long value counting milliseconds
	 *         since 01/01/1970. Always returns a value > -1.
	 */
	public long getModifiedTime();

	/**
	 * @return the status of the entry
	 */
	public EntryStatus getStatus();

	/**
	 * @param entry
	 *            the entry to compare
	 * @return -1 if the item less than id, 0 if equal, 1 if greater
	 */
	public int compareTo(final Entry entry);
}

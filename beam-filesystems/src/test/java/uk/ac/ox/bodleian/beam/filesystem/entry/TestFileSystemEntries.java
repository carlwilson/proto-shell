/**
 * 
 */
package uk.ac.ox.bodleian.beam.filesystem.entry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

import uk.ac.ox.bodleian.beam.filesystem.AllFileSystemTests;
import uk.ac.ox.bodleian.beam.filesystem.FileSystems;
import uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreamId;
import uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreamInstance.ByteStreamStatus;

/**
 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a> <a
 *         href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 20 Jul 2012:12:46:06
 */

public class TestFileSystemEntries {
	static final Map<Integer, FileSystemEntry> RAW_ENTRIES;
	static {
		Map<Integer, FileSystemEntry> entries = new HashMap<Integer, FileSystemEntry>();
		try {
			for (File file : AllFileSystemTests.getRawFiles()) {
				FileSystemEntry entry = FileSystemEntries.fromValues(
						FileSystems.relatavisePaths(AllFileSystemTests
								.getCanonicalRootFile().getAbsolutePath(), file
								.getAbsolutePath()), file.length(), file.lastModified());
				entries.put(entry.hashCode(), entry);
			}
		} catch (URISyntaxException excep) {
			excep.printStackTrace();
			throw new IllegalStateException(excep);
		}
		RAW_ENTRIES = Collections.unmodifiableMap(entries);
	}

	/**
	 * Test method for {@link uk.ac.ox.bodleian.beam.filesystem.entry.FileEntry#fromValues(String, long, long, long, ByteStreamId, Exception, ByteStreamStatus)}.
	 */
	@SuppressWarnings("javadoc")
	@Test
	public void testfromValuesNull() {
		// Create our own version of the null stream details
		FileSystemEntry nullEntry = FileSystemEntryImpl.fromValues("", 0L, 0L, 0L);
		// ensure it's equal to the constant value
		assertEquals("Null entry values should be equal.", nullEntry, FileSystemEntries.NULL_ENTRY);
	}
	
	/**
	 * Test method for {@link uk.ac.ox.bodleian.beam.filesystem.entry.FileEntry#fromValues(String, long, long, long, ByteStreamId, Exception, ByteStreamStatus)}.
	 */
	@SuppressWarnings("javadoc")
	@Test(expected=IllegalArgumentException.class)
	public void testFromValuesNullName() {
		// Try creating an entry with empty name
		FileSystemEntryImpl.fromValues(null, 0L, 0L, 0L);
	}

	/**
	 * {@link uk.ac.ox.bodleian.beam.filesystem.entry.FileEntry#fromValues(String, long, long, long, ByteStreamId, Exception, ByteStreamStatus)}
	 */
	@SuppressWarnings("javadoc")
	@Test(expected=IllegalArgumentException.class)
	public void testFromValuesBadCompressedSize() {
		// Try creating an entry with null name
		FileSystemEntryImpl.fromValues("", -1L, 0L, 0L);
	}

	/**
	 * Test method for {@link uk.ac.ox.bodleian.beam.filesystem.entry.FileEntry#fromValues(String, long, long, long, ByteStreamId, Exception, ByteStreamStatus)}.
	 */
	@SuppressWarnings("javadoc")
	@Test(expected=IllegalArgumentException.class)
	public void testFromValuesBadSize() {
		// Try creating an entry with null name
		FileSystemEntryImpl.fromValues("", 0L, -1L, 0L);
	}

	/**
	 * Test method for {@link uk.ac.ox.bodleian.beam.filesystem.entry.FileEntry#fromValues(String, long, long, long, ByteStreamId, Exception, ByteStreamStatus)}.
	 */
	@SuppressWarnings("javadoc")
	@Test(expected=IllegalArgumentException.class)
	public void testFromValuesCompressedGTSize() {
		// Try creating an entry with null name
		FileSystemEntryImpl.fromValues("", 10L, 1L, 1L);
	}


	/**
	 * @throws URISyntaxException
	 */
	public void testFromFile() throws URISyntaxException {
		Set<FileSystemEntry> entriesFromFile = new HashSet<FileSystemEntry>();
		for (File file : AllFileSystemTests.getRawFiles()) {
			entriesFromFile.add(FileSystemEntries.fromFile(
					FileSystems.relatavisePaths(AllFileSystemTests
							.getCanonicalRootFile().getAbsolutePath(), file
							.getAbsolutePath()), file));
		}

		// OK our set should be the same as the map values
		assertEquals("Sets should be the same size.", entriesFromFile.size(), RAW_ENTRIES.values().size());
		
		// Now lets get the details and perform a few more tests
		Iterator<FileSystemEntry> entriesIT = entriesFromFile.iterator();
		while (entriesIT.hasNext()) {
			FileSystemEntry entryFromFile = entriesIT.next();
			// Our map should contain the SHA key
			assertTrue("hashCode() not found in map.", RAW_ENTRIES.containsKey(entryFromFile.hashCode()));
			// And the retrieved object should be identical
			assertEquals("Retrieved Entry differs.", entryFromFile, RAW_ENTRIES.get(entryFromFile.hashCode()));
		}
	}
	/**
	 * Test method for
	 * {@link uk.ac.ox.bodleian.beam.filesystem.entry.FileEntry#uncheckedEntryFromFile(String, File)}
	 * .
	 * 
	 * @throws URISyntaxException
	 */
	@SuppressWarnings("javadoc")
	@Test(expected = AssertionError.class)
	public void testFromFileWithEmpties() throws URISyntaxException {
		File emptyFile = AllFileSystemTests.getEmptyFile();
		// Create null file from values, and the file
		FileSystemEntry emptyFromValues = FileSystemEntryImpl.fromValues(
				emptyFile.getName(), emptyFile.length(),
				emptyFile.lastModified());
		FileSystemEntry emptyFromFile = FileSystemEntries.fromFile(emptyFile.getName(), emptyFile);
		// They should be equal, but not to the empty stream
		assertEquals("emptyFromValues != emptyFromFile", emptyFromFile,
				emptyFromValues);
		assertFalse("emptyFromValues != emptyFromFile",
				(emptyFromValues == FileSystemEntries.NULL_ENTRY));

		File emptyDir = AllFileSystemTests.getEmptyDir();
		assertTrue(emptyDir.isDirectory());
		FileSystemEntries.fromFile("name", emptyDir);
	}

	/**
	 * Test method for
	 * {@link uk.ac.ox.bodleian.beam.filesystem.entry.FileEntry#uncheckedEntryFromFile(String, File)}
	 * .
	 */
	@SuppressWarnings("javadoc")
	@Test(expected = IllegalArgumentException.class)
	public void testFromFileNullFile() {
		FileSystemEntries.fromFile("name", null);
	}
	/**
	 * Test the hash and equals contract for the class using EqualsVerifier
	 */
	@Test
	public void testEqualsContract() {
		EqualsVerifier.forClass(FileSystemEntryImpl.class).verify();
	}
}

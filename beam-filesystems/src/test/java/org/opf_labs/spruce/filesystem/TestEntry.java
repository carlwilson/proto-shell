/**
 * 
 */
package org.opf_labs.spruce.filesystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

/**
 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a> <a
 *         href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 20 Jul 2012:12:46:06
 */

public class TestEntry {
	static final Set<Entry> RAW_ENTRIES;
	static {
		Set<Entry> entries = new HashSet<Entry>();
		try {
			for (File file : AllFileSystemTests.getRawFiles()) {
				Entry entry = FileSystems.uncheckedEntryFromValues(
						FileSystems.relatavisePaths(AllFileSystemTests
								.getJavaIORootFile().getAbsolutePath(), file
								.getAbsolutePath()), file.length(), file.lastModified());
				entries.add(entry);
			}
		} catch (URISyntaxException excep) {
			excep.printStackTrace();
			throw new IllegalStateException(excep);
		}
		RAW_ENTRIES = Collections.unmodifiableSet(entries);
	}

	/**
	 * Test method for {@link org.opf_labs.spruce.filesystem.FileSystems#uncheckedEntryFromValues(String, long, long)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testFromValuesNullName() {
		// Try creating an entry with empty name
		EntryImpl.fromValues(null, 0L, 0L);
	}

	/**
	 * Test method for {@link org.opf_labs.spruce.filesystem.FileSystems#uncheckedEntryFromValues(String, long, long)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testFromValuesBadSize() {
		// Try creating an entry with null name
		EntryImpl.fromValues("", -2L, 0L);
	}

	/**
	 * Test method for {@link org.opf_labs.spruce.filesystem.FileSystems#uncheckedEntryFromValues(String, long, long)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testFromValuesBadModified() {
		// Try creating an entry with null name
		EntryImpl.fromValues("", 0L, -1L);
	}

	/**
	 * Test method for
	 * {@link org.opf_labs.spruce.filesystem.FileSystems#uncheckedEntryFromFile(String, File)}
	 * .
	 * 
	 * @throws URISyntaxException
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testFromFileWithEmpties() throws URISyntaxException {
		File emptyFile = AllFileSystemTests.getEmptyFile();
		// Create null file from values, and the file
		Entry emptyFromValues = EntryImpl.fromValues(
				emptyFile.getName(), emptyFile.length(),
				emptyFile.lastModified());
		Entry emptyFromFile = FileSystems.uncheckedEntryFromFile(emptyFile.getName(), emptyFile);
		// They should be equal, but not to the empty stream
		assertEquals("emptyFromValues != emptyFromFile", emptyFromFile,
				emptyFromValues);
		assertFalse("emptyFromValues != emptyFromFile",
				(emptyFromValues == FileSystems.nullEntry()));

		File emptyDir = AllFileSystemTests.getEmptyDir();
		FileSystems.uncheckedEntryFromFile("name", emptyDir);
	}

	/**
	 * Test method for
	 * {@link org.opf_labs.spruce.filesystem.FileSystems#uncheckedEntryFromFile(String, File)}
	 * .
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testFromFileNullFile() {
		FileSystems.uncheckedEntryFromFile("name", null);
	}

	/**
	 * Test the hash and equals contract for the class using EqualsVerifier
	 */
	@Test
	public void testEqualsContract() {
		EqualsVerifier.forClass(EntryImpl.class).verify();
	}
	

	/**
	 * Test compareTo method
	 */
	@Test
	public void TestCompareTo() {
		// Create from value entries from the current map and fill a set
		Set<Entry> entries = new HashSet<Entry>();
		for (Entry entry : RAW_ENTRIES) {
			entries.add(EntryImpl.fromValues(entry.getName(), entry.getSize(), entry.getModifiedTime(), entry.getStatus()));
		}
		// OK our set should be the same as the map values
		assertEquals("Sets should be the same size.", entries.size(), RAW_ENTRIES.size());
		
		// Iterate through set and test
		for (Entry entry : entries) {
			assertTrue(RAW_ENTRIES.contains(entry));
			// Now test against the other ids
			for (Entry rawEntry : RAW_ENTRIES) {
				// if they're equal test consistency with compareTo
				if (entry.equals(rawEntry)) {
					assertTrue(rawEntry.equals(entry));
					assertTrue(entry.compareTo(rawEntry) == 0);
					assertTrue(rawEntry.compareTo(entry) == 0);
				} else if ((entry.compareTo(rawEntry) == 0) || (rawEntry.equals(entry)) || (rawEntry.compareTo(entry) == 0)) {
					// This shouldn't happen as should be caught by initial equals test
					fail(entry + " and " + rawEntry + ", fail equals compareTo consistency.");
				} else if (entry.compareTo(rawEntry) < 0) {
					// tests for less than
					assertTrue(rawEntry.compareTo(entry) > 0);
				} else {
					assertTrue(entry.compareTo(rawEntry) > 0);
				}
			}
		}
		// Some trivial tests
		EntryImpl entry = EntryImpl.fromValues("a", 0L, 0L);
		EntryImpl largerEntry = EntryImpl.fromValues("b", 0L, 0L);
		assertTrue(entry.compareTo(largerEntry) < 0);
		assertTrue(largerEntry.compareTo(entry) > 0);
		largerEntry = EntryImpl.fromValues("a", 1L, 0L);
		assertTrue(entry.compareTo(largerEntry) < 0);
		assertTrue(largerEntry.compareTo(entry) > 0);
		largerEntry = EntryImpl.fromValues("a", 0L, 1L);
		assertTrue(entry.compareTo(largerEntry) < 0);
		assertTrue(largerEntry.compareTo(entry) > 0);
	}
}

/**
 * 
 */
package uk.ac.ox.bodleian.beam.filesystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.apache.log4j.Logger;
import org.junit.Test;

import uk.ac.ox.bodleian.beam.filesystem.entry.FileSystemEntries;
import uk.ac.ox.bodleian.beam.filesystem.entry.FileSystemEntry;

/**
 * TODO JavaDoc for TestJavaIOFileSystem.</p> TODO Tests for
 * TestJavaIOFileSystem.</p> TODO Implementation for TestJavaIOFileSystem.</p>
 * 
 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a> <a
 *         href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1
 * 
 *          Created 28 Jul 2012:04:20:48
 */

public class TestJavaIOFileSystem {
	// Set up a static set of unchecked entries derived from the JavaIO
	// FileSystem Root
	static final Set<FileSystemEntry> ENTRIES;
	static final long TOTAL_SIZE;
	static {
		long totalSize = 0L;
		Set<FileSystemEntry> entries = new HashSet<FileSystemEntry>();
		for (File file : AllFileSystemTests.getRawFiles()) {
			// Clunky name trick to get the relative path
			String name = FileSystems.relatavisePaths(file.getParentFile()
					.getParentFile().getParentFile().getAbsolutePath(),
					file.getAbsolutePath());
			FileSystemEntry uncheckedFromFile = FileSystemEntries
					.fromFile(name, file);
			totalSize += file.length();
			assertTrue("entries.add(uncheckedFromFile) != true",
					entries.add(uncheckedFromFile));
		}
		assertTrue("(entries.size() > 0) != true", (entries.size() > 0));
		assertTrue(
				"(entries.size() == AllFileSystemTests.getRawFiles().size()) != true",
				(entries.size() == AllFileSystemTests.getRawFiles().size()));
		TOTAL_SIZE = totalSize;
		ENTRIES = Collections.unmodifiableSet(entries);
	}

	/**
	 * Test method for
	 * {@link uk.ac.ox.bodleian.beam.filesystem.FileSystems#fromDirectory(java.lang.String, java.io.File)}
	 * .
	 */
	@SuppressWarnings("static-method")
	@Test
	public final void testFromDirectory() {
		// Get an instance from the java IO fs root, same as the static set
		FileSystem uncheckedFromDir = getBasicFileSystem();
		assertNotNull("uncheckedFromDir == null", uncheckedFromDir);
		// It should equal the static set
		assertEquals("uncheckedFromDir.getEntries() != ENTRIES",
				uncheckedFromDir.getEntries(), ENTRIES);
	}

	/**
	 * Test method for
	 * {@link uk.ac.ox.bodleian.beam.filesystem.JavaIOFileSystem#getName()}.
	 */
	@SuppressWarnings("static-method")
	@Test
	public final void testGetName() {
		// Get an instance from the java IO fs root, same as the static set
		FileSystem uncheckedFromDir = getBasicFileSystem();
		assertNotNull("uncheckedFromDir.getName() == null",
				uncheckedFromDir.getName());
		assertFalse("uncheckedFromDir.getName().isEmpty() == null",
				uncheckedFromDir.getName().isEmpty());
	}

	/**
	 * Test method for
	 * {@link uk.ac.ox.bodleian.beam.filesystem.JavaIOFileSystem#getPath()}.
	 */
	@SuppressWarnings("static-method")
	@Test
	public final void testGetPath() {
		// Get an instance from the java IO fs root, same as the static set
		FileSystem uncheckedFromDir = getBasicFileSystem();
		assertNotNull("uncheckedFromDir.getPath() == null",
				uncheckedFromDir.getPath());
		assertFalse("uncheckedFromDir.getPath().isEmpty() == null",
				uncheckedFromDir.getPath().isEmpty());
	}

	/**
	 * Test method for
	 * {@link uk.ac.ox.bodleian.beam.filesystem.JavaIOFileSystem#getEntryCount()}
	 * .
	 */
	@SuppressWarnings("static-method")
	@Test
	public final void testGetEntryCount() {
		// Get an instance from the java IO fs root, same as the static set
		FileSystem uncheckedFromDir = getBasicFileSystem();
		assertTrue("uncheckedFromDir.getEntryCount() != ENTRIES.size()",
				uncheckedFromDir.getEntryCount() == ENTRIES.size());
	}

	/**
	 * Test method for
	 * {@link uk.ac.ox.bodleian.beam.filesystem.JavaIOFileSystem#getTotalBytes()}.
	 */
	@SuppressWarnings("static-method")
	@Test
	public final void testGetTotalSize() {
		FileSystem uncheckedFromDir = getBasicFileSystem();
		assertTrue("uncheckedFromDir.getEntryCount() != ENTRIES.size()",
				uncheckedFromDir.getEntryCount() == ENTRIES.size());
	}

	/**
	 * TODO: look at equals test
	 * Test the hash and equals contract for the {@link JavaIOFileSystem} using
	 * {@link EqualsVerifier}.
	 */
	//@Test
	public void testEqualsContract() {
		EqualsVerifier
				.forClass(JavaIOFileSystem.class)
				.withPrefabValues(Logger.class, Logger.getLogger("1"),
						Logger.getLogger("2")).verify();
	}

	// Helper to retrieve FS or fail test
	private static FileSystem getBasicFileSystem() {
		// Create a File System from the JavaIOFS root
		FileSystem fileSys = null;
		try {
			fileSys = FileSystems.fromDirectory(AllFileSystemTests.getJavaIORootFile());
		} catch (URISyntaxException excep) {
			// Fail if we don't find test data
			excep.printStackTrace();
			fail(excep.getMessage());
		}
		return fileSys;
	}
}

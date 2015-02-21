/**
 * 
 */
package org.opf_labs.spruce.filesystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

/**
 * TODO JavaDoc for TestEntryManifest.</p> TODO Tests for TestEntryManifest.</p>
 * TODO Implementation for TestEntryManifest.</p>
 * 
 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 *          Created 2 Oct 2012:15:54:15
 */

public class TestEntryManifest {

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		AllFileSystemTests.removeEmptyFileFromJavaiofs();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		AllFileSystemTests.removeEmptyFileFromJavaiofs();
	}

	/**
	 * Test of package protected constructor arg testing
	 * org.opf_labs.spruce.filesystem.EntryManifestImpl
	 * #EntryManifestImpl(java.util.Set, long)
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testFileSystemManifestImplNullEntries() {
		EntryManifestImpl.fromValues(0L, null);
	}

	/**
	 * Test of package protected constructor arg testing
	 * org.opf_labs.spruce.filesystem.EntryManifestImpl
	 * #EntryManifestImpl(java.util.Set, long)
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testFileSystemManifestImplBadSize() {
		EntryManifestImpl.fromValues(-1L, new HashSet<Entry>());
	}

	/**
	 * Test method for
	 * {@link org.opf_labs.spruce.filesystem.EntryManifest#getTotalSize()}.
	 * 
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	@Test
	public final void testGetTotalBytes() throws URISyntaxException,
			IOException {
		// Create manifest from the root java directory
		EntryManifest originalManifest = FileSystems
				.entryManifestFromDirectory(AllFileSystemTests
						.getJavaIORootFile());
		// count the bytes on a per-entry basis
		long totalBytes = 0L;
		for (Entry entry : originalManifest.getEntries()) {
			totalBytes += entry.getSize();
		}
		// and compare to the manifests title
		assertEquals(originalManifest.getTotalSize(), totalBytes);
		// Now add the empty file and create another manifest
		AllFileSystemTests.addEmptyFileToJavaiofs();
		EntryManifest manifestWithEmpty = FileSystems
				.entryManifestFromDirectory(AllFileSystemTests
						.getJavaIORootFile());
		// this should have more elements but the same size manifest
		assertTrue(manifestWithEmpty.getEntries().size() > originalManifest
				.getEntries().size());
		assertEquals(manifestWithEmpty.getTotalSize(),
				originalManifest.getTotalSize());
	}

	/**
	 * Test the hash and equals contract for the class using EqualsVerifier
	 */
	@Test
	public void testEqualsContract() {
		EqualsVerifier.forClass(EntryManifestImpl.class).suppress(Warning.NULL_FIELDS).verify();
	}

	/**
	 * @throws URISyntaxException
	 * @throws IOException
	 * 
	 */
	@Test
	public void testManifestComparison() throws URISyntaxException, IOException {
		// Create manifest from the root java directory, and then a copy
		EntryManifest originalManifest = FileSystems
				.entryManifestFromDirectory(AllFileSystemTests
						.getJavaIORootFile());
		EntryManifest origCopyManifest = FileSystems
				.entryManifestFromDirectory(AllFileSystemTests
						.getJavaIORootFile());
		// these should be equal
		assertTrue(originalManifest.compareTo(origCopyManifest) == 0);
		// OK one less than the original, one greater than
		EntryManifest ltOriginal = FileSystems
				.entryManifestFromDirectory(AllFileSystemTests
						.getLessThanJavaIORootFile());
		EntryManifest gtOriginal = FileSystems
				.entryManifestFromDirectory(AllFileSystemTests
						.getGreaterThanJavaIORootFile());
		// Now some tests
		assertTrue(ltOriginal.compareTo(gtOriginal) < 0);
		assertTrue(ltOriginal.compareTo(originalManifest) < 0);
		assertTrue(ltOriginal.compareTo(ltOriginal) == 0);
		assertTrue(originalManifest.compareTo(gtOriginal) < 0);
		assertTrue(originalManifest.compareTo(originalManifest) == 0);
		assertTrue(originalManifest.compareTo(ltOriginal) > 0);
		assertTrue(gtOriginal.compareTo(gtOriginal) == 0);
		assertTrue(gtOriginal.compareTo(originalManifest) > 0);
		assertTrue(gtOriginal.compareTo(ltOriginal) > 0);
	}
}

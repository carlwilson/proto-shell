/**
 * 
 */
package org.opf_labs.spruce.filesystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.opf_labs.spruce.bytestream.ByteStreams;
import org.opf_labs.spruce.filesystem.Entry.EntryStatus;

/**
 * TODO JavaDoc for TestByteStreamManifest.</p> TODO Tests for
 * TestByteStreamManifest.</p> TODO Implementation for
 * TestByteStreamManifest.</p>
 * 
 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 *          Created 3 Oct 2012:18:21:34
 */

public class TestByteStreamManifest {

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
	 * Test method for
	 * {@link org.opf_labs.spruce.filesystem.FileSystems#nullByteStreamManifest()}
	 * .
	 */
	@Test
	public final void testGetNullManifest() {
		ByteStreamManifest manifest = FileSystems.nullByteStreamManifest();
		assertTrue(manifest.getEntryTuples().size() == 0);
		assertTrue(manifest.getTotalSize() == 0L);
		assertTrue(manifest.getEntrySize() == 0L);
	}

	/**
	 * Test method for
	 * {@link org.opf_labs.spruce.filesystem.ByteStreamManifest#getTotalSize()}
	 * .
	 * @throws URISyntaxException 
	 * @throws IOException 
	 */
	@Test
	public final void testGetTotalSize() throws IOException, URISyntaxException {
		// Create manifest from the root java directory 
		ByteStreamManifest manifest = FileSystems
				.byteStreamManifestFromDirectory(AllFileSystemTests.getJavaIORootFile());
		// count the bytes on a per-entry basis
		long totalBytes = 0L;
		for (EntryTuple identifiedEntry : manifest.getEntryTuples()) {
			long size = identifiedEntry.getEntry().getSize();
			totalBytes += (size > 0) ? size : 0;
		}
		// and compare to the manifests title
		assertEquals(manifest.getTotalSize(), totalBytes);
		// Now add the empty file and create another manifest
		AllFileSystemTests.addEmptyFileToJavaiofs();
		ByteStreamManifest manifestWithEmpty = FileSystems.byteStreamManifestFromDirectory(AllFileSystemTests.getJavaIORootFile());
		// this should have more elements but the same size manifest
		assertTrue(manifestWithEmpty.getEntryTuples().size() > manifest.getEntryTuples().size());
		assertEquals(manifestWithEmpty.getTotalSize(), manifest.getTotalSize());
	}

	/**
	 * Test method for
	 * {@link org.opf_labs.spruce.filesystem.ByteStreamManifest#getEntryTuples()}
	 * .
	 * @throws URISyntaxException 
	 */
	@Test
	public final void testGetIdentifiedEntries() throws URISyntaxException {
		// Create manifest from the root java directory 
		ByteStreamManifest manifest = FileSystems
				.byteStreamManifestFromDirectory(AllFileSystemTests.getJavaIORootFile());
		int entryCount = 0;
		for (EntryTuple identifiedEntry : manifest.getEntryTuples()) {
			// Make sure we have an OK byte stream for every entry
			entryCount++;
			assertTrue(identifiedEntry.getEntry().getStatus() == EntryStatus.OK);
			assertTrue(identifiedEntry.getByteStreamId().getLength() > 0L);
		}
		assertTrue(manifest.getEntryTuples().size() == entryCount);
	}

	/**
	 * Test method for
	 * {@link org.opf_labs.spruce.filesystem.ByteStreamManifest#getEntrySize()}
	 * .
	 * @throws URISyntaxException 
	 * @throws IOException 
	 */
	@Test
	public final void testGetEntrySize() throws IOException, URISyntaxException {
		// Create manifest from the root java directory 
		ByteStreamManifest manifest = FileSystems
				.byteStreamManifestFromDirectory(AllFileSystemTests.getJavaIORootFile());
		// count the bytes on a per-entry basis
		long countedBytes = 0L;
		for (EntryTuple identifiedEntry : manifest.getEntryTuples()) {
			assertFalse(identifiedEntry.getByteStreamId().equals(ByteStreams.nullByteStreamId()));
			countedBytes += identifiedEntry.getByteStreamId().getLength();
		}
		// and compare to the manifests title
		assertEquals(manifest.getEntrySize(), countedBytes);
		// Now add the empty file and create another manifest
		AllFileSystemTests.addEmptyFileToJavaiofs();
		ByteStreamManifest manifestWithEmpty = FileSystems.byteStreamManifestFromDirectory(AllFileSystemTests.getJavaIORootFile());
		// this should have more elements but the same size manifest
		assertTrue(manifestWithEmpty.getEntryTuples().size() > manifest.getEntryTuples().size());
		assertEquals(manifestWithEmpty.getEntrySize(), manifest.getEntrySize());
	}

	/**
	 * Test the hash and equals contract for the class using EqualsVerifier
	 */
	//@Test
	public void testEqualsContract() {
		EqualsVerifier.forClass(ByteStreamManifestImpl.class).suppress(Warning.NULL_FIELDS).verify();
	}

	/**
	 * Test method for
	 * {@link org.opf_labs.spruce.filesystem.ByteStreamManifest#compareTo(org.opf_labs.spruce.filesystem.ByteStreamManifest)}
	 * .
	 * 
	 * @throws URISyntaxException
	 */
	@Test
	public final void testCompareToByteStreamManifest()
			throws URISyntaxException {
		// Create manifest from the root java directory, and then a copy
		ByteStreamManifest originalManifest = FileSystems
				.byteStreamManifestFromDirectory(AllFileSystemTests
						.getJavaIORootFile());
		// OK one less than the original, one greater than
		ByteStreamManifest ltOriginal = FileSystems
				.byteStreamManifestFromDirectory(AllFileSystemTests
						.getLessThanJavaIORootFile());
		ByteStreamManifest gtOriginal = FileSystems
				.byteStreamManifestFromDirectory(AllFileSystemTests
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

/**
 * 
 */
package uk.ac.ox.bodleian.beam.filesystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.apache.log4j.Logger;
import org.junit.Test;

import uk.ac.ox.bodleian.beam.filesystem.FileSystem.DamagedEntryException;
import uk.ac.ox.bodleian.beam.filesystem.FileSystem.EntryNotFoundException;
import uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreamId;
import uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreamInstance;
import uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreams;
import uk.ac.ox.bodleian.beam.filesystem.entry.FileSystemEntry;

/**
 * TODO JavaDoc for TestHashedFileSystem<p/>
 * TODO Implementation for TestHashedFileSystem
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1
 * 
 * Created 27 Jul 2012:02:33:36
 */

public class TestHashedFileSystem {
	// Set up a static map of entries derived from the JavaIO FileSystem Root
	static final Map<String, FileSystemEntry> ENTRY_MAP;
	static {
		Map<String, FileSystemEntry> entryMap = new HashMap<String, FileSystemEntry>();
		try {
			for (File file : AllFileSystemTests.getRawFiles()) {
				FileSystemEntry fromCC = AllFileSystemTests.createFileSystemEntryUsingCommonsCodec(FileSystems.relatavisePaths(AllFileSystemTests.getJavaIORootFile().getAbsolutePath(), file.getAbsolutePath()), file);
				entryMap.put(FileSystems.relatavisePaths(AllFileSystemTests.getJavaIORootFile().getAbsolutePath(), file.getAbsolutePath()), fromCC);
			}
		} catch (URISyntaxException excep) {
			// Fail if we don't find test data
			excep.printStackTrace();
			fail(excep.getMessage());
		} catch (FileNotFoundException excep) {
			// Fail if we don't find test data
			excep.printStackTrace();
			fail(excep.getMessage());
		} catch (IOException excep) {
			// Fail if we can't read data
			excep.printStackTrace();
			fail(excep.getMessage());
		}
		ENTRY_MAP = Collections.unmodifiableMap(entryMap);
	}

	/**
	 * Creates an {@link TestHashedFileSystem} instance populated from the javaiofs test
	 * directory and tests that it's equivalent to the static map populated ENTRY_MAP from
	 * first principles.
	 * @throws IOException 
	 */
	@Test
	public void testGetInstanceFromDirectory() throws IOException {
		// Create a File System from the JavaIOFS root
		FileSystem fileSys = getJavaIOFSFileSystem();
		HashedFileSystem hashedFileSys = new HashedFileSystem(fileSys);
		hashedFileSys.hashCheckFileSystem();
		// Loop through the entry names
		FileSystemEntry entry = null;
		while ((entry = fileSys.getNextEntry()) != null) {
			// Check we have the name in the ENTRY_MAP and they're the same entry
			try {
				// Check that the main map contains the entry
				assertEquals("fileSys != ENTRY_MAP.containsKey(name)", entry, ENTRY_MAP.get(entry.getName()));
				// Get the Byte sequence identifier for the entry, and the one for the hashed system
				ByteStreamId fsBsid = ByteStreams.fromInputStream(fileSys.getEntryStream());
				ByteStreamInstance bsi = hashedFileSys.getEntryByteStream(entry.getName());
				// assert that these are equal
				assertEquals("Expected the byte sequence ids to be equal", fsBsid, bsi.getByteStreamId());
			} catch (EntryNotFoundException excep) {
				// Fail if we don't find an entry for the name
				excep.printStackTrace();
				fail("Couldn't find entry " + entry + ".\n" + excep.getMessage());
			} catch (IOException excep) {
				excep.printStackTrace();
				fail("IOException reading entry " + entry + ".\n" + excep.getMessage());
			} catch (DamagedEntryException excep) {
				excep.printStackTrace();
				fail("DamagedEntryException for entry " + entry + ".\n" + excep.getMessage());
			}
		}
	}

	/**
	 * TODO: look at equals test
	 * Test the hash and equals contract for the class using EqualsVerifier
	 */
	//@Test
	public void testEqualsContract() {
		EqualsVerifier.forClass(HashedFileSystem.class).withPrefabValues(Logger.class, Logger.getLogger("1"), Logger.getLogger("2")).verify();
	}

	// Helper to retrieve FS or fail test
	private static FileSystem getJavaIOFSFileSystem() {
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

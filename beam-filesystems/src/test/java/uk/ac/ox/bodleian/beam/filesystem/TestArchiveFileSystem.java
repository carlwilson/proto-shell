/**
 * 
 */
package uk.ac.ox.bodleian.beam.filesystem;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.compress.archivers.ArchiveException;
import org.junit.Test;

import uk.ac.ox.bodleian.beam.filesystem.entry.FileSystemEntry;

/**
 * TODO JavaDoc for TestArchiveFileSystem.</p>
 * TODO Tests for TestArchiveFileSystem.</p>
 * TODO Implementation for TestArchiveFileSystem.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 17 Sep 2012:02:05:29
 */

public class TestArchiveFileSystem {

	/**
	 * Test method for {@link uk.ac.ox.bodleian.beam.filesystem.ArchiveFileSystem#getInstanceFromFile(java.io.File)}.
	 * @throws ArchiveException 
	 * @throws IOException 
	 */
	@SuppressWarnings("static-method")
	@Test
	public final void testGetInstanceFromTarFile() throws IOException, ArchiveException {
		try {
			File tarArchive = AllFileSystemTests.getTarRootFile();
			assertTrue("TAR Archive test file must exist", tarArchive.exists());
			FileSystem filesys = ArchiveFileSystem.getInstanceFromFile(tarArchive);
			for (FileSystemEntry entry : filesys.getEntries()) {
				System.out.println(entry);
			}
		} catch (URISyntaxException excep) {
			excep.printStackTrace();
			fail("URI Syntax exception getting tar file " + excep.getMessage());
		}
	}

	/**
	 * Test method for {@link uk.ac.ox.bodleian.beam.filesystem.ArchiveFileSystem#getInstanceFromStream(java.lang.String, java.lang.String, java.io.InputStream)}.
	 * @throws ArchiveException 
	 * @throws IOException 
	 */
	@SuppressWarnings("static-method")
	@Test
	public final void testGetInstanceFromZipFile() throws IOException, ArchiveException {
		try {
			File zipArchive = AllFileSystemTests.getZipRootFile();
			assertTrue("ZIP Archive test file must exist", zipArchive.exists());
			FileSystem filesys = ArchiveFileSystem.getInstanceFromFile(zipArchive);
			for (FileSystemEntry entry : filesys.getEntries()) {
				System.out.println(entry);
			}
		} catch (URISyntaxException excep) {
			excep.printStackTrace();
			fail("URI Syntax exception getting tar file " + excep.getMessage());
		}
	}
}

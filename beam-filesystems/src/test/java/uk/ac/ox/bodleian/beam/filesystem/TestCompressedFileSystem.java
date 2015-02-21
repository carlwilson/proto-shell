package uk.ac.ox.bodleian.beam.filesystem;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

import org.junit.Test;

import uk.ac.ox.bodleian.beam.filesystem.FileSystem.FileSystemException;
import uk.ac.ox.bodleian.beam.filesystem.entry.FileSystemEntry;

/**
 * TODO JavaDoc for TestCompressedFileSystem.</p>
 * TODO Tests for TestCompressedFileSystem.</p>
 * TODO Implementation for TestCompressedFileSystem.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 17 Sep 2012:02:20:25
 */
public class TestCompressedFileSystem {

	/**
	 * @throws FileNotFoundException
	 * @throws FileSystemException
	 */
	@SuppressWarnings("static-method")
	@Test
	public final void testGetInstanceFromFile() throws FileNotFoundException, FileSystemException {
		try {
			File gzipFile = AllFileSystemTests.getGzTarRootFile();
			assertTrue("TAR Archive test file must exist", gzipFile.exists());
			FileSystem filesys = CompressedFileSystem.getInstanceFromFile(gzipFile);
			for (FileSystemEntry entry : filesys.getEntries()) {
				System.out.println(entry);
			}
		} catch (URISyntaxException excep) {
			excep.printStackTrace();
			fail("URI Syntax exception getting tar file " + excep.getMessage());
		}
	}

}

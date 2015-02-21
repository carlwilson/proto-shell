/**
 * 
 */
package uk.ac.ox.bodleian.beam.filesystem.iso;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import uk.ac.ox.bodleian.beam.filesystem.AllFileSystemTests;
import uk.ac.ox.bodleian.beam.filesystem.FileSystem;
import uk.ac.ox.bodleian.beam.filesystem.iso.ISOFileSystemManager.ISOMountException;
// TODO: Reinstate testing here

/**
 * TODO JavaDoc for TestIsoFileSystem.</p>
 * TODO Tests for TestIsoFileSystem.</p>
 * TODO Implementation for TestIsoFileSystem.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 16 Sep 2012:23:43:24
 */

public class TestIsoFileSystem {
	/**
	 * 
	 * @throws ISOMountException
	 * @throws URISyntaxException
	 * @throws IOException 
	 */
	@SuppressWarnings("static-method")
	public final void testIsoMounting() throws ISOMountException, URISyntaxException, IOException {
		File isoFile = AllFileSystemTests.getISORootFile();
		assertTrue("Test ISO file:" + isoFile.getAbsolutePath() + " doesn't exist.", isoFile.exists());
		FileSystem isoFs = null;
		isoFs = ISOFileSystemManager.getFileSystemFromISOFile(isoFile);
		if (isoFs != null) {
			for (String entryName : isoFs.getEntryNames()) {
				System.out.println(entryName);
			}
		}
		ISOFileSystemManager.unmountFileSystems();
	}
}

/**
 * 
 */
package uk.ac.ox.bodleian.beam.filesystem;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * TODO JavaDoc for TestFileSystems.</p>
 * TODO Tests for TestFileSystems.</p>
 * TODO Implementation for TestFileSystems.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 13 Sep 2012:08:29:21
 */

public class TestFileSystems {
	/**
	 * 
	 */
	@SuppressWarnings("static-method")
	@Test
	public final void testIsSupportedExt() {
		// Assert that a known bad string is negative
		String ext = "garbage";
		assertFalse("Garbage file extension flagged as supported.", FileSystems.isSupportedExt(ext));
		// Now test some known ones
		ext = FileSystems.GZIP_EXT;
		assertTrue("GZIP not flagged as supported", FileSystems.isSupportedExt(ext));
		ext = FileSystems.ISO_EXT;
		assertTrue("ISO not flagged as supported", FileSystems.isSupportedExt(ext));
		ext = FileSystems.TAR_EXT;
		assertTrue("TAR not flagged as supported", FileSystems.isSupportedExt(ext));
		ext = FileSystems.ZIP_EXT;
		assertTrue("ZIP not flagged as supported", FileSystems.isSupportedExt(ext));
	}
}

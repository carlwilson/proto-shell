/**
 * 
 */
package uk.ac.ox.bodleian.beam.filesystem.bytestream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreamId;
import uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreamIdImpl;
import uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreams;

/**
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1
 * 
 * Created 20 Jul 2012:03:29:11
 */

public class TestByteStreams {
	// Create a set of ByteStreams from the raw test data
	private static final Map<String, ByteStreamId> RAW_DETAILS_MAP;
	static {
		Map<String, ByteStreamId> detailsMap = new HashMap<String, ByteStreamId>();
		try {
			for (File file : AllFileSystemTests.getRawFiles()) {
				ByteStreamId fromCC = AllFileSystemTests.createByteStreamUsingCommonsCodec(file);
				detailsMap.put(fromCC.getHexSHA256(), fromCC);
			}
		} catch (FileNotFoundException excep) {
			excep.printStackTrace();
			fail(excep.getMessage());
		} catch (IOException excep) {
			excep.printStackTrace();
			fail(excep.getMessage());
		}
		RAW_DETAILS_MAP = Collections.unmodifiableMap(detailsMap);
	}

	/**
	 * Test method for {@link uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreams#fromValues(long, String, String)}.
	 */
	@Test
	public void testfromValuesNullStream() {
		// Create our own version of the null stream details
		ByteStreamId nullStreamDetails = ByteStreams.fromValues(0L, ByteStreams.NULL_SHA256, ByteStreams.NULL_MD5);
		// ensure it's equal to the constant value
		assertEquals("Null stream values should be equal.", nullStreamDetails, ByteStreams.NULL_STREAM);
	}
	
	/**
	 * Test method for {@link uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreams#fromValues(long, String, String)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testFromValuesBadLength() {
		// Try creating a byte stream with a length less than zero, use the valid empty hash values
		ByteStreams.fromValues(-1L, ByteStreams.NULL_SHA256, ByteStreams.NULL_MD5);
	}
	
	/**
	 * Test method for {@link uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreams#fromValues(long, String, String)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testFromValuesEmptySHA256() {
		// Try creating a byte stream with an empty sha value
		ByteStreams.fromValues(0L, "", ByteStreams.NULL_MD5);
	}
	
	/**
	 * Test method for {@link uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreams#fromValues(long, String, String)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testFromValuesEmptyMD5() {
		// Try creating a byte stream with an empty md5 value
		ByteStreams.fromValues(0L, ByteStreams.NULL_SHA256, "");
	}
	
	/**
	 * Test method for {@link uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreams#fromValues(long, String, String)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testFromValuesNullSHA256() {
		// Try creating a byte stream with a null sha value
		ByteStreams.fromValues(0L, null, ByteStreams.NULL_MD5);
	}
	
	/**
	 * Test method for {@link uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreams#fromValues(long, String, String)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testFromValuesNullMD5() {
		// Try creating a byte stream with a null md5 value
		ByteStreams.fromValues(0L, ByteStreams.NULL_SHA256, null);
	}
	
	/**
	 * Test method for {@link uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreams#fromValues(long, String, String)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testFromValuesWrongLengthSHA256() {
		// Try creating a byte stream with an over long md5 value
		ByteStreams.fromValues(0L, ByteStreams.NULL_MD5, ByteStreams.NULL_MD5);
	}
	
	/**
	 * Test method for {@link uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreams#fromValues(long, String, String)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testFromValuesWrongLengthMD5() {
		// Try creating a byte stream with an over long md5 value
		ByteStreams.fromValues(0L, ByteStreams.NULL_SHA256, ByteStreams.NULL_SHA256);
	}
	
	/**
	 * Test method for {@link uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreams#fromValues(long, String, String)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testFromValuesNonHexSHA256() {
		// Try creating a byte stream with a non hex sha value
		ByteStreams.fromValues(0L, "1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdeX", ByteStreams.NULL_MD5);
	}
	
	/**
	 * Test method for {@link uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreams#fromValues(long, String, String)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testFromValuesNonHexMD5() {
		// Try creating a byte stream with an over long md5 value
		ByteStreams.fromValues(0L, ByteStreams.NULL_SHA256, "1234567890abcdef1234567890abcdeX");
	}

	/**
	 * Test method for {@link uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreams#fromValues(long, String, String)}.
	 */
	@Test
	public void TestFromValues() {
		// Create from value entries from the current map and fill a set
		Set<ByteStreamId> details = new HashSet<ByteStreamId>();
		for (ByteStreamId detail : RAW_DETAILS_MAP.values()) {
			details.add(ByteStreams.fromValues(detail.getLength(), detail.getHexSHA256(), detail.getHexMD5()));
		}
		// OK our set should be the same as the map values
		assertEquals("Sets should be the same size.", details.size(), RAW_DETAILS_MAP.values().size());
		
		// Now lets get the details and perform a few more tests
		Iterator<ByteStreamId> detailsIT = details.iterator();
		while (detailsIT.hasNext()) {
			ByteStreamId detail = detailsIT.next();
			// Our map should contain the SHA key
			assertTrue("SHA KEY not found in map.", RAW_DETAILS_MAP.containsKey(detail.getHexSHA256()));
			// And the retrieved object should be identical
			assertEquals("Retrieved byte stream differs.", detail, RAW_DETAILS_MAP.get(detail.getHexSHA256()));
		}
	}

	/**
	 * Test method for {@link uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreams#fromFile(File)}.
	 */
	@Test
	public void testFromFile() {
		// Create a null item from an empty file
		ByteStreamId nullStream = null;
		try {
			nullStream = ByteStreams.fromFile(AllFileSystemTests.getEmptyFile());
		} catch (URISyntaxException excep) {
			fail(excep.getMessage());
		} catch (FileNotFoundException excep) {
			fail(excep.getMessage());
		} catch (IOException excep) {
			fail(excep.getMessage());
		}
		assertEquals("nullStream != NULL_STREAM", nullStream, ByteStreams.NULL_STREAM);
		Set<ByteStreamId> details = new HashSet<ByteStreamId>();
		try {
			for (File rawFile : AllFileSystemTests.getRawFiles()) {
				details.add(ByteStreams.fromFile(rawFile));
			}
		} catch (FileNotFoundException excep) {
			fail(excep.getMessage());
		} catch (IOException excep) {
			fail(excep.getMessage());
		}
		// OK our set should be the same as the map values
		assertEquals("Sets should be the same size.", details.size(), RAW_DETAILS_MAP.values().size());
		
		// Now lets get the details and perform a few more tests
		Iterator<ByteStreamId> detailsIT = details.iterator();
		while (detailsIT.hasNext()) {
			ByteStreamId detail = detailsIT.next();
			// Our map should contain the SHA key
			assertTrue("SHA KEY not found in map.", RAW_DETAILS_MAP.containsKey(detail.getHexSHA256()));
			// And the retrieved object should be identical
			assertEquals("Retrieved byte stream differs.", detail, RAW_DETAILS_MAP.get(detail.getHexSHA256()));
		}
	}
	
	/**
	 * Test method for {@link uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreams#fromFile(File)}.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testFromFileNullFile() throws FileNotFoundException, IOException {
		// OK try a null file for the exception
		ByteStreams.fromFile(null);
	}

	/**
	 * Test method for {@link uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreams#fromInputStream(java.io.InputStream)}.
	 */
	@SuppressWarnings("null")
	@Test
	public void testFromInputStream() {
		// Create a null item from an empty file
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(AllFileSystemTests.getEmptyFile());
		} catch (FileNotFoundException excep) {
			fail("Can't find empty file.");
		} catch (URISyntaxException excep) {
			fail("URI Exception for empty file resource.");
		}
		ByteStreamId nullStream = null;
		try {
			nullStream = ByteStreams.fromInputStream(fis);
		} catch (IOException excep) {
			fail("Can't  open empty file");
		} finally {
			try {
				fis.close();
			} catch (IOException excep) {
				// DO NOTHING
			}
		}
		assertEquals("nullStream != NULL_STREAM", nullStream, ByteStreams.NULL_STREAM);
		Set<ByteStreamId> details = new HashSet<ByteStreamId>();
		for (File rawFile : AllFileSystemTests.getRawFiles()) {
			try {
				details.add(ByteStreams.fromInputStream(new FileInputStream(rawFile)));
			} catch (IOException excep) {
				fail("Can't  open raw file " + rawFile);
			}
		}
		// OK our set should be the same as the map values
		assertEquals("Sets should be the same size.", details.size(), RAW_DETAILS_MAP.values().size());
		
		// Now lets get the details and perform a few more tests
		Iterator<ByteStreamId> detailsIT = details.iterator();
		while (detailsIT.hasNext()) {
			ByteStreamId detail = detailsIT.next();
			// Our map should contain the SHA key
			assertTrue("SHA KEY not found in map.", RAW_DETAILS_MAP.containsKey(detail.getHexSHA256()));
			// And the retrieved object should be identical
			assertEquals("Retrieved byte stream differs.", detail, RAW_DETAILS_MAP.get(detail.getHexSHA256()));
		}
	}

	/**
	 * Test method for {@link uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreams#fromInputStream(java.io.InputStream)}.
	 * @throws IOException 
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testFromInputStreamNullStream() throws IOException {
		// OK try a null file for the exception
		ByteStreams.fromInputStream(null);
	}

	/**
	 * Test the hash and equals contract for the class using EqualsVerifier
	 */
	@Test
	public void testEqualsContract() {
		EqualsVerifier.forClass(ByteStreamIdImpl.class).verify();
	}
}

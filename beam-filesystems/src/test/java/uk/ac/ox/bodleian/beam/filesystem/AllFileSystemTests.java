package uk.ac.ox.bodleian.beam.filesystem;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreamId;
import uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreams;
import uk.ac.ox.bodleian.beam.filesystem.bytestream.TestByteStreams;
import uk.ac.ox.bodleian.beam.filesystem.entry.FileSystemEntries;
import uk.ac.ox.bodleian.beam.filesystem.entry.FileSystemEntry;
import uk.ac.ox.bodleian.beam.filesystem.entry.TestFileSystemEntries;
import uk.ac.ox.bodleian.beam.filesystem.iso.TestIsoFileSystem;

/**
 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a> <a
 *         href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 20 Jul 2012:03:33:34
 */
@RunWith(Suite.class)
@SuiteClasses({ TestByteStreams.class, TestFileSystemEntries.class,
		TestJavaIOFileSystem.class, TestHashedFileSystem.class, TestFileSystems.class, TestIsoFileSystem.class })
public class AllFileSystemTests {
	/** Root test */
	private final static String TEST_ROOT = "uk/ac/bodleian/beam";
	/** Root data */
	public final static String DATA_ROOT = TEST_ROOT + "/data";
	/** Root javaio fs */
	public final static String JAVAIO_ROOT = DATA_ROOT + "/javaiofs";
	/** Root javaio fs */
	public final static String ISO_ROOT = DATA_ROOT + "/javaiofs.iso";
	/** Root javaio fs */
	public final static String ZIP_ROOT = DATA_ROOT + "/javaiofs.zip";
	/** Root javaio fs */
	public final static String TAR_ROOT = DATA_ROOT + "/javaiofs.tar";
	/** Root javaio fs */
	public final static String GZTAR_ROOT = DATA_ROOT + "/javaiofs.tar.gz";
	/** Empty directory */
	public final static String EMPTY_DIR = DATA_ROOT + "/empty.dir";
	/** Empty file */
	public final static String EMPTY_FILE = DATA_ROOT + "/empty.file";
	// A list of raw test files
	private static final List<File> RAW_FILES;
	static {
		try {
			RAW_FILES = AllFileSystemTests.getResourceFiles(JAVAIO_ROOT, true);
		} catch (URISyntaxException excep) {
			excep.printStackTrace();
			fail(excep.getMessage());
			throw new AssertionError();
		}
	}

	/**
	 * @return the list of files in the RAW ROOT dir
	 */
	public final static List<File> getRawFiles() {
		return RAW_FILES;
	}

	private final static List<File> getResourceFiles(String resName,
			boolean recurse) throws URISyntaxException {
		File root = getResourceAsFile(resName);
		return getFileChildren(root, recurse);
	}

	/**
	 * @return the empty directory
	 * @throws URISyntaxException
	 */
	public final static File getEmptyDir() throws URISyntaxException {
		return new File(getResourceAsFile(EMPTY_FILE).getParentFile()
				.getAbsolutePath() + EMPTY_DIR);
	}

	/**
	 * @return the empty file
	 * @throws URISyntaxException
	 */
	public final static File getEmptyFile() throws URISyntaxException {
		return getResourceAsFile(EMPTY_FILE);
	}

	/**
	 * @return the java.io.File for the canonical File System root
	 * @throws URISyntaxException
	 */
	public final static File getCanonicalRootFile() throws URISyntaxException {
		return getResourceAsFile(JAVAIO_ROOT);
	}

	/**
	 * @return the java.io.File for the java io File System root
	 * @throws URISyntaxException
	 */
	public final static File getJavaIORootFile() throws URISyntaxException {
		return getResourceAsFile(JAVAIO_ROOT);
	}

	/**
	 * @return the java.io.File for the ISO File System root
	 * @throws URISyntaxException
	 */
	public final static File getISORootFile() throws URISyntaxException {
		return getResourceAsFile(ISO_ROOT);
	}

	/**
	 * @return the java.io.File for the Zip File System root
	 * @throws URISyntaxException
	 */
	public final static File getZipRootFile() throws URISyntaxException {
		return getResourceAsFile(ZIP_ROOT);
	}

	/**
	 * @return the java.io.File for the Zip File System root
	 * @throws URISyntaxException
	 */
	public final static File getTarRootFile() throws URISyntaxException {
		return getResourceAsFile(TAR_ROOT);
	}

	/**
	 * @return the java.io.File for the Zip File System root
	 * @throws URISyntaxException
	 */
	public final static File getGzTarRootFile() throws URISyntaxException {
		return getResourceAsFile(GZTAR_ROOT);
	}

	/**
	 * @param resName
	 *            the name of the resource to retrieve a file for
	 * @return the java.io.File for the named resource
	 * @throws URISyntaxException
	 *             if the named resource can't be converted to a URI
	 */
	public final static File getResourceAsFile(String resName)
			throws URISyntaxException {
		return new File(ClassLoader.getSystemResource(resName).toURI());
	}

	/**
	 * @param file
	 * @return a
	 *         {@link uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreamId}
	 *         created using values as commons-codec for testing.
	 * @throws IOException
	 */
	public static final ByteStreamId createByteStreamUsingCommonsCodec(
			File file) throws IOException {
		long length = file.length();
		FileInputStream fis = new FileInputStream(file);
		String hexMD5 = null;
		try {
			hexMD5 = DigestUtils.md5Hex(fis);
		} finally {
			try {
				fis.close();
			} catch (IOException excep) {
				// DO NOTHING
			}
		}
		fis = new FileInputStream(file);
		String hexSHA256 = null;
		try {
			hexSHA256 = DigestUtils.sha256Hex(fis);
		} finally {
			try {
				fis.close();
			} catch (IOException excep) {
				// DO NOTHING
			}
		}
		return ByteStreams.fromValues(length, hexSHA256, hexMD5);
	}

	/**
	 * @param name
	 * @param file
	 * @return a
	 *         {@link uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreamId}
	 *         created using values as commons-codec for testing.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public final static FileSystemEntry createFileSystemEntryUsingCommonsCodec(
			String name, File file) throws FileNotFoundException, IOException {
		return FileSystemEntries.fromValues(name, file.length(),
				file.lastModified());
	}
	private final static List<File> getFileChildren(File root, boolean recurse) {
		List<File> children = new ArrayList<File>();
		for (File file : root.listFiles()) {
			if (file.isDirectory() && !file.isHidden() && recurse) {
				children.addAll(getFileChildren(file, recurse));
			} else if (file.isFile() && !file.isHidden()) {
				children.add(file);
			}
		}
		return children;
	}
}

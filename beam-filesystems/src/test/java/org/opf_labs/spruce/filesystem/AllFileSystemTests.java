package org.opf_labs.spruce.filesystem;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a> <a
 *         href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 20 Jul 2012:03:33:34
 */
@RunWith(Suite.class)
@SuiteClasses({TestEntry.class, TestEntryManifest.class, TestByteStreamManifest.class})
public class AllFileSystemTests {
	/** Root test */
	private final static String TEST_ROOT = "uk/ac/bodleian/beam";
	/** Root data */
	public final static String DATA_ROOT = TEST_ROOT + "/data";
	/** Root javaio fs */
	public final static String JAVAIO_ROOT = DATA_ROOT + "/javaiofs";
	/** Root javaio_dupes */
	public final static String JAVAIO_DUPES_ROOT = DATA_ROOT + "/javaiofs_dupes";
	/** Root greater than javaio fs */
	public final static String GT_JAVAIO_ROOT = DATA_ROOT + "/gt_javaiofs";
	/** Root less than javaio fs */
	public final static String LT_JAVAIO_ROOT = DATA_ROOT + "/lt_javaiofs";
	/** Root iso javaio fs */
	public final static String ISO_ROOT = DATA_ROOT + "/javaiofs.iso";
	/** Root zip javaio fs */
	public final static String ZIP_ROOT = DATA_ROOT + "/javaiofs.zip";
	/** Root tar javaio fs */
	public final static String TAR_ROOT = DATA_ROOT + "/javaiofs.tar";
	/** Root tar gzip javaio fs */
	public final static String GZTAR_ROOT = DATA_ROOT + "/javaiofs.tar.gz";
	/** Empty directory */
	public final static String EMPTY_DIR = DATA_ROOT + "/empty.dir";
	/** Empty file */
	public final static String EMPTY_FILE = DATA_ROOT + "/empty.file";
	/** Java IOFS Empty file */
	public final static String JAVAIO_EMPTY_FILE = JAVAIO_ROOT + "/empty.file";

	/**
	 * @return the list of files in the RAW ROOT dir
	 */
	public final static List<File> getRawFiles() {
		try {
			return Collections.unmodifiableList(AllFileSystemTests.getResourceFiles(JAVAIO_ROOT, true));
		} catch (URISyntaxException excep) {
			// TODO Auto-generated catch block
			excep.printStackTrace();
			fail("URI Exception getting test data: " + excep);
		}
		// this will never happen as fail condition thrown first
		return null;
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
	 * @return the java.io.File for the javaio File System root
	 * @throws URISyntaxException
	 */
	public final static File getJavaIORootFile() throws URISyntaxException {
		return getResourceAsFile(JAVAIO_ROOT);
	}

	/**
	 * @return the java.io.File for the javaio dupes File System root
	 * @throws URISyntaxException
	 */
	public final static File getJavaIODupesRootFile() throws URISyntaxException {
		return getResourceAsFile(JAVAIO_DUPES_ROOT);
	}

	/**
	 * @return the java.io.File for the greater than javaio File System root
	 * @throws URISyntaxException
	 */
	public final static File getGreaterThanJavaIORootFile() throws URISyntaxException {
		return getResourceAsFile(GT_JAVAIO_ROOT);
	}

	/**
	 * @return the java.io.File for the less than javaio File System root
	 * @throws URISyntaxException
	 */
	public final static File getLessThanJavaIORootFile() throws URISyntaxException {
		return getResourceAsFile(LT_JAVAIO_ROOT);
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
	 * @return the java.io.File for the Tar File System root
	 * @throws URISyntaxException
	 */
	public final static File getTarRootFile() throws URISyntaxException {
		return getResourceAsFile(TAR_ROOT);
	}

	/**
	 * @return the java.io.File for the TarGzip File System root
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
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public final static void addEmptyFileToJavaiofs() throws IOException, URISyntaxException {
		File javaIoRoot = getResourceAsFile(JAVAIO_ROOT);
		File emptyCopy = new File(javaIoRoot.getAbsolutePath() + "/empty.file"); 
		FileUtils.copyFile(getResourceAsFile(EMPTY_FILE), emptyCopy);
	}

	/**
	 * @throws URISyntaxException 
	 */
	public final static void removeEmptyFileFromJavaiofs() throws URISyntaxException {
		File javaIoRoot = getResourceAsFile(JAVAIO_ROOT);
		File javaIoEmpty = new File(javaIoRoot.getAbsolutePath() + "/empty.file"); 
		if (javaIoEmpty.exists()) javaIoEmpty.delete();
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

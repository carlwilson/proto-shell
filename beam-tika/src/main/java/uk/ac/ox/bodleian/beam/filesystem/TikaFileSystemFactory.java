/**
 * 
 */
package uk.ac.ox.bodleian.beam.filesystem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.tika.mime.MediaType;

import uk.ac.ox.bodleian.beam.tika.TikaWrapper;

/**
 * TODO: JavaDoc for TikaFileSystemFactory.<p/>
 * TODO: Tests for TikaFileSystemFactory.<p/>
 * TODO: Full implementation for TikaFileSystemFactory.<p/>
 *
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1
 * 
 * Created 24 Jul 2012:10:48:52
 */

public final class TikaFileSystemFactory {
	private static final TikaWrapper TIKA = TikaWrapper.getTika();
	private static final Set<MediaType> SUPPORTED_TYPES = new HashSet<MediaType>();

	private TikaFileSystemFactory() {
		throw new AssertionError("[TikaFileSystemFactory] should never be in default constructor");
	}

	/**
	 * @param mediaType is this media type a supported file system type?
	 * @return true if the media type a supported file system type
	 */
	public static boolean isFileSystem(MediaType mediaType) {
		return(SUPPORTED_TYPES.contains(mediaType));
	}

	/**
	 * @param stream
	 * @return true if the stream a supported file system type
	 */
	public static boolean isFileSystem(InputStream stream) {
		MediaType mediaType;
		try {
			mediaType = TIKA.getMediaType(stream);
		} catch (IOException excep) {
			return false;
		}
		return(SUPPORTED_TYPES.contains(mediaType));
	}

	/**
	 * @param file
	 * @return true if the stream a supported file system type
	 */
	public static boolean isFileSystem(File file) {
		MediaType mediaType;
		try {
			mediaType = TIKA.getMediaType(file);
		} catch (IOException excep) {
			return false;
		}
		return(SUPPORTED_TYPES.contains(mediaType));
	}
}

/**
 * 
 */
package uk.ac.ox.bodleian.beam.filesystem.iso;

import java.io.File;
import java.io.FileNotFoundException;

import uk.ac.ox.bodleian.beam.filesystem.FileSystem;

/**
 * TODO JavaDoc for ISOFileSystemManager.</p>
 * TODO Tests for ISOFileSystemManager.</p>
 * TODO Implementation for ISOFileSystemManager.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 17 Sep 2012:01:08:56
 */

public class ISOFileSystemManager {
	
	
	/**
	 * @param isoFile
	 *            the iso file to create a file system for
	 * @return a FileSystem based on the ISO file contents
	 * @throws ISOMountException
	 * @throws FileNotFoundException
	 * @throws UnsupportedOperationException 
	 */
	public static FileSystem getFileSystemFromISOFile(File isoFile)
			throws FileNotFoundException, UnsupportedOperationException, ISOMountException {
		if (isoFile == null) throw new IllegalArgumentException("isoFile == null");
		if (!isoFile.isFile()) {
			throw new IllegalArgumentException("isoFile must be an existing file.");
		}
		if (CdEmuFileSystem.isAvailable()) {
			return CdEmuFileSystem.getInstance(isoFile);
		}
		if (FuseIsoFileSystem.isAvailable()) {
			try {
				return FuseIsoFileSystem.getInstance(isoFile);
			} catch (ISOMountException excep) {
				if (LinuxImageFileSystem.isAvailable()) {
					return LinuxImageFileSystem.getInstance(isoFile);
				}
			}
		} else if  (LinuxImageFileSystem.isAvailable()) {
			return LinuxImageFileSystem.getInstance(isoFile);
		}
		
		throw new UnsupportedOperationException();
	}

	/**
	 * Unmount any resources
	 */
	public static void unmountFileSystems() {
		try {
			if (CdEmuFileSystem.isAvailable()) {
				CdEmuFileSystem.unmountAll();
			}
			if (FuseIsoFileSystem.isAvailable()) {
				FuseIsoFileSystem.unmountAll();
			}
			if (LinuxImageFileSystem.isAvailable()) {
				LinuxImageFileSystem.unmountAll();
			}
		} catch (ISOMountException excep) {
			// TODO Auto-generated catch block
			excep.printStackTrace();
		}
	}

	/**
	 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>
	 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
	 * @version 0.1
	 * 
	 *          Created 20 Jul 2012:16:53:25
	 */
	public static class ISOMountException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = -2976945485710275245L;

		/**
		 * 
		 */
		public ISOMountException() {
			super();
		}

		/**
		 * @param message
		 */
		public ISOMountException(String message) {
			super(message);
		}

		/**
		 * @param message
		 * @param cause
		 */
		public ISOMountException(String message, Exception cause) {
			super(message, cause);
		}
	}
}

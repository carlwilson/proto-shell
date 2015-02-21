/**
 * 
 */
package uk.ac.ox.bodleian.beam.filesystem.iso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.opf_labs.spruce.utils.Environments;

import uk.ac.ox.bodleian.beam.filesystem.FileSystem;
import uk.ac.ox.bodleian.beam.filesystem.FileSystems;
import uk.ac.ox.bodleian.beam.filesystem.JavaIOFileSystem;
import uk.ac.ox.bodleian.beam.filesystem.entry.FileSystemEntry;
import uk.ac.ox.bodleian.beam.filesystem.iso.ISOFileSystemManager.ISOMountException;

/**
 * TODO JavaDoc for FuseIsoFileSystem.</p> TODO Tests for FuseIsoFileSystem.</p>
 * TODO Implementation for FuseIsoFileSystem.</p>
 * 
 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 *          Created 4 Sep 2012:08:37:02
 */

public class FuseIsoFileSystem implements FileSystem {
	private final static String TYPE = "[FuseISO]";
	private final static String FUSEISO_CMD = "fuseiso";
	private final static String FUSEISO_MNT_FLG = "-p";
	private final static String FUSEISO_DSMNT_CMD = "fusermount";
	private final static String FUSEISO_DSMNT_FLG = "-u";
	private final static String FUSEISO_HELP_FLG = "-h";
	private final static String FUSEISO_MNT_HOME = System
			.getProperty("user.home") + "/mounts/";
	private static final int FUSEISO_SUCCESS = 0;
	static {
		File MOUNT_HOME = new File(FUSEISO_MNT_HOME);
		if (!MOUNT_HOME.exists()) {
			if (!MOUNT_HOME.mkdirs()) {
				throw new IllegalStateException("Couldn't create MOUNT_HOME "
						+ MOUNT_HOME.getAbsolutePath());
			}
		}
	}
	private static final boolean AVAILABLE;
	static {
		boolean available = false;
		if (Environments.isUnix()) {
			String[] commands = new String[] { FUSEISO_CMD, FUSEISO_HELP_FLG};
			Process proc = null;
			@SuppressWarnings("unused")
			String processOut = "";
			try {
				proc = new ProcessBuilder(commands).start();
				BufferedReader input = new BufferedReader(new InputStreamReader(
						proc.getInputStream()));
				String line;
				while ((line = input.readLine()) != null) {
					processOut += (line + "\n");
				}
				input.close();
				available = true;
			} catch (IOException excep) {
				// Do nothing
			}
		}
		AVAILABLE = available;
	}
	/**
	 * @return true if the fuse iso system is available
	 */
	public static final boolean isAvailable() {return AVAILABLE;}
	private static Map<String, FuseIsoFileSystem> MOUNTED_ISOS = new HashMap<String, FuseIsoFileSystem>();
	private File isoFile;
	private String mountPoint;
	private JavaIOFileSystem javaFileSystem;
	private String name;

	private FuseIsoFileSystem(File isoFile, String mountPoint)
			throws FileNotFoundException {
		if (isoFile == null)
			throw new IllegalArgumentException(
					"ISO file argument can not be null");
		if (!isoFile.exists())
			throw new FileNotFoundException("Can't find file "
					+ isoFile.getAbsolutePath());
		if (!isoFile.isFile())
			throw new IllegalArgumentException(isoFile.getAbsolutePath()
					+ " is not a file.");
		this.name = isoFile.getName();
		this.isoFile = isoFile;
		this.mountPoint = mountPoint;
		this.javaFileSystem = FileSystems.fromDirectory(isoFile.getName(), new File(mountPoint));
	}

	/**
	 * @see uk.ac.ox.bodleian.beam.filesystem.FileSystem#getType()
	 */
	@Override
	public final String getType() {
		return TYPE;
	}

	/**
	 * @see uk.ac.ox.bodleian.beam.filesystem.FileSystem#getPath()
	 */
	@Override
	public final String getPath() {
		return this.isoFile.getAbsolutePath();
	}

	/**
	 * @see uk.ac.ox.bodleian.beam.filesystem.FileSystem#getEntryNames()
	 */
	@Override
	public final Set<String> getEntryNames() {
		return this.javaFileSystem.getEntryNames();
	}

	/**
	 * @see uk.ac.ox.bodleian.beam.filesystem.FileSystem#getEntryStream(java.lang.String)
	 */
	@Override
	public InputStream getEntryStream(final String name)
			throws EntryNotFoundException, DamagedEntryException {
		try {
			return new FileInputStream(this.getEntryFile(name));
		} catch (FileNotFoundException excep) {
			throw new EntryNotFoundException(name + " entry not found.", excep);
		}
	}

	/**
	 * @see uk.ac.ox.bodleian.beam.filesystem.FileSystem#getEntryCount()
	 */
	@Override
	public final int getEntryCount() {
		return this.javaFileSystem.getEntryCount();
	}

	/**
	 * @see uk.ac.ox.bodleian.beam.filesystem.FileSystem#getTotalBytes()
	 */
	@Override
	public final long getTotalBytes() {
		return this.javaFileSystem.getTotalBytes();
	}

	/**
	 * @see uk.ac.ox.bodleian.beam.filesystem.FileSystem#getEntry(java.lang.String)
	 */
	@Override
	public final FileSystemEntry getEntry(final String name)
			throws EntryNotFoundException {
		return this.javaFileSystem.getEntry(name);
	}

	/**
	 * @see uk.ac.ox.bodleian.beam.filesystem.FileSystem#getEntryFile(java.lang.String)
	 */
	@Override
	public final File getEntryFile(final String name)
			throws UnsupportedOperationException, EntryNotFoundException,
			DamagedEntryException {
		return this.javaFileSystem.getEntryFile(name);
	}

	@Override
	public final String getName() {
		return this.name;
	}

	@Override
	public final Set<FileSystemEntry> getEntries() {
		return this.javaFileSystem.getEntries();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		return this.javaFileSystem.toString();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((this.javaFileSystem == null) ? 0 : this.javaFileSystem
						.hashCode());
		result = prime * result
				+ ((this.name == null) ? 0 : this.name.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof FileSystem))
			return false;
		FileSystem other = (FileSystem) obj;
		if (this.javaFileSystem == null) {
			return false;
		} else if (!this.javaFileSystem.equals(other))
			return false;
		if (this.name == null) {
			if (other.getName() != null)
				return false;
		} else if (!this.name.equals(other.getName()))
			return false;
		return true;
	}

	/**
	 * @return the stream upon which the file system is based
	 * @throws FileNotFoundException
	 */
	public InputStream getFileSystemStream() throws FileNotFoundException {
		return new FileInputStream(this.isoFile);
	}

	/**
	 * @return the mount point for the file system
	 */
	public final String getMountPoint() {
		return this.mountPoint;
	}

	/**
	 * @return the map of the currently mounted isos
	 */
	public static final Map<String, FuseIsoFileSystem> getMountedISOs() {
		return Collections.unmodifiableMap(MOUNTED_ISOS);
	}

	/**
	 * @param isoFile
	 * @return a new ISO file based file system mounted using fuseiso
	 * @throws FileNotFoundException
	 * @throws ISOMountException
	 */
	static final FileSystem getInstance(final File isoFile)
			throws FileNotFoundException, ISOMountException {
		if (MOUNTED_ISOS.containsKey(isoFile.getAbsolutePath()))
			return MOUNTED_ISOS.get(isoFile.getAbsolutePath());
		return mountISOFile(isoFile);
	}

	/**
	 * @param ifs
	 * @throws ISOMountException
	 */
	public static final void deleteInstance(final FuseIsoFileSystem ifs)
			throws ISOMountException {
		unmountISOFile(ifs);
	}

	/**
	 * @throws ISOMountException
	 */
	public static void unmountAll() throws ISOMountException {
		for (String isoPath : MOUNTED_ISOS.keySet()) {
			unmountISOFile(MOUNTED_ISOS.get(isoPath));
		}
	}

	// Static method to mount an ISO file using WinCDEmu.
	private static FuseIsoFileSystem mountISOFile(final File isoFile)
			throws ISOMountException, FileNotFoundException {
		String mountPoint = FUSEISO_MNT_HOME
				+ FilenameUtils.getBaseName(isoFile.getName());
		String[] commands = new String[] { FUSEISO_CMD, FUSEISO_MNT_FLG,
				isoFile.getAbsolutePath(), mountPoint };
		Process proc = null;
		String processOut = "";
		try {
			proc = new ProcessBuilder(commands).start();
			BufferedReader input = new BufferedReader(new InputStreamReader(
					proc.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				processOut += (line + "\n");
			}
			input.close();
		} catch (IOException excep) {
			throw new ISOMountException("IOException mounting "
					+ isoFile.getName() + " at mount point " + mountPoint,
					excep);
		}
		try {
			int result = proc.waitFor();
			if (result != FUSEISO_SUCCESS) {
				throw new ISOMountException("Bad FuseISO result:" + result
						+ " mounting" + isoFile.getName() + " at mount point "
						+ mountPoint + "\nProcessOutput:" + processOut);
			}
		} catch (InterruptedException excep) {
			throw new ISOMountException("InterruptedException mounting "
					+ isoFile.getName() + " at mount point " + mountPoint,
					excep);
		}

		FuseIsoFileSystem ifs = new FuseIsoFileSystem(isoFile, mountPoint);
		MOUNTED_ISOS.put(isoFile.getAbsolutePath(), ifs);
		return ifs;
	}

	private static void unmountISOFile(final FuseIsoFileSystem ifs)
			throws ISOMountException {
		String[] commands = new String[] { FUSEISO_DSMNT_CMD,
				FUSEISO_DSMNT_FLG, ifs.getMountPoint() };
		Process proc = null;
		@SuppressWarnings("unused")
		String processOut = "";
		try {
			proc = new ProcessBuilder(commands).start();
			BufferedReader input = new BufferedReader(new InputStreamReader(
					proc.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				processOut += (line + "/n");
			}
			input.close();
		} catch (IOException excep) {
			throw new ISOMountException("IOException unmounting "
					+ ifs.getName() + " at mount point " + ifs.mountPoint,
					excep);
		}
		try {
			@SuppressWarnings("unused")
			int result = proc.waitFor();
		} catch (InterruptedException excep) {
			throw new ISOMountException("InterruptedException mounting "
					+ ifs.getName() + " at mount point " + ifs.mountPoint,
					excep);
		}
		MOUNTED_ISOS.remove(ifs.isoFile.getAbsolutePath());
	}


	@Override
	public FileSystemEntry getNextEntry() {
		// TODO Auto-generated method stub
		return this.javaFileSystem.getNextEntry();
	}

	@Override
	public InputStream getEntryStream() throws EntryNotFoundException,
			DamagedEntryException {
		// TODO Auto-generated method stub
		return this.javaFileSystem.getEntryStream();
	}

	@Override
	public void resetEntries() {
		this.javaFileSystem.resetEntries();
		
	}

	@Override
	public void resetEntries(InputStream stream) {
		this.javaFileSystem.resetEntries(stream);
	}
}

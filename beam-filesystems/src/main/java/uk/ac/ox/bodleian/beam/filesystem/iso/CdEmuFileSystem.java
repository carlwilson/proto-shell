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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.opf_labs.spruce.utils.Environments;

import uk.ac.ox.bodleian.beam.filesystem.FileSystem;
import uk.ac.ox.bodleian.beam.filesystem.FileSystems;
import uk.ac.ox.bodleian.beam.filesystem.JavaIOFileSystem;
import uk.ac.ox.bodleian.beam.filesystem.entry.FileSystemEntry;
import uk.ac.ox.bodleian.beam.filesystem.iso.ISOFileSystemManager.ISOMountException;

/**
 * TODO: JavaDoc for CdEmuFileSystem.
 * <p/>
 * TODO: Refactor to incorporate OSFMount.
 * </p>
 * TODO: Add support for linux mount.</p>
 * 
 * @author <a href="mailto:carl.wilson@keepitdigital.eu">Carl Wilson</a> <a
 *         href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1
 * 
 *          Created 9 Jul 2012:00:52:16
 */

public final class CdEmuFileSystem implements FileSystem {
	private final static String TYPE = "[WinCDEMU ISO]";
	private static final String[] WINCDEMU_PATHS = {
			"C:\\Program Files (x86)\\WinCDEmu\\batchmnt.exe",
			"C:\\Program Files\\WinCDEmu\\batchmnt.exe" };
	private static String WINCDEMU_PATH;
	static {
		String winPath = "";
		for (String path : WINCDEMU_PATHS) {
			File file = new File(path);
			if (file.isFile()) {
				winPath = path;
			}
		}
		WINCDEMU_PATH = winPath;
	}
	private static final boolean AVAILABLE = (Environments.isWindows() && !WINCDEMU_PATH.isEmpty());
	/**
	 * @return true is the WinCDEmu file system is available
	 */
	public static final boolean isAvailable() {return AVAILABLE;}
	private static final String WINCDEMU_WAIT_FLAG = "/wait";
	private static final String WINCDEMU_UNMOUNT_FLAG = "/unmount";
	private static final int WINCDEMU_SUCCESS = 0;
	private static final String[] POSSIBLE_MOUNT_POINTS = { "d:", "e:", "f:",
			"g:", "h:", "i:", "j:", "k:", "l:", "m:", "n:", "o:", "p:", "q:",
			"s:", "t:", "u:", "v:", "w:", "x:", "y:", "z:" };
	private static Set<String> AVAILABLE_MOUNTS = new HashSet<String>();
	static {
		Set<String> usedMounts = new HashSet<String>();
		File[] roots = File.listRoots();
		for (File root : roots) {
			String rootName = root.getAbsolutePath();
			if (!rootName.endsWith(":") && rootName.length() > 0) {
				rootName = rootName.substring(0, rootName.length() - 1);
			}
			usedMounts.add(rootName.toLowerCase());
		}

		for (String possRoot : POSSIBLE_MOUNT_POINTS) {
			if (!usedMounts.contains(possRoot)) {
				AVAILABLE_MOUNTS.add(possRoot);
			}
		}
	}
	private static Map<String, CdEmuFileSystem> MOUNTED_ISOS = new HashMap<String, CdEmuFileSystem>();
	private File isoFile;
	private String mountPoint;
	private JavaIOFileSystem javaFileSystem;
	private String name;

	private CdEmuFileSystem(File isoFile, String mountPoint) {
		if (!isoFile.isFile())
			throw new IllegalArgumentException(isoFile.getAbsolutePath()
					+ " is not a file.");
		this.name = isoFile.getName();
		this.isoFile = isoFile;
		this.mountPoint = mountPoint;
		this.javaFileSystem = FileSystems
				.fromDirectory(isoFile.getName(), new File(mountPoint));
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
	public static final Map<String, CdEmuFileSystem> getMountedISOs() {
		return Collections.unmodifiableMap(MOUNTED_ISOS);
	}

	/**
	 * @return the set of available mount points
	 */
	public static final Set<String> getAvailableMountPoints() {
		return Collections.unmodifiableSet(AVAILABLE_MOUNTS);
	}

	/**
	 * @param isoFile
	 * @return a new ISO file based file system mounted using WinCDEmu
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
	public static final void deleteInstance(final CdEmuFileSystem ifs)
			throws ISOMountException {
		unmountISOFile(ifs);
		AVAILABLE_MOUNTS.add(ifs.getMountPoint());
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
	private static CdEmuFileSystem mountISOFile(final File isoFile)
			throws ISOMountException {
		if (AVAILABLE_MOUNTS.size() < 1)
			throw new IllegalStateException();
		String mountPoint = AVAILABLE_MOUNTS.iterator().next();
		String[] commands = new String[] { WINCDEMU_PATH,
				isoFile.getAbsolutePath(), mountPoint, WINCDEMU_WAIT_FLAG };
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
			if (result != WINCDEMU_SUCCESS) {
				throw new ISOMountException("Bad WinCDEmu result:" + result
						+ " mounting" + isoFile.getName() + " at mount point "
						+ mountPoint + "\nProcessOutput:" + processOut);
			}

		} catch (InterruptedException excep) {
			throw new ISOMountException("InterruptedException mounting "
					+ isoFile.getName() + " at mount point " + mountPoint,
					excep);
		}

		CdEmuFileSystem ifs = new CdEmuFileSystem(isoFile, mountPoint);
		AVAILABLE_MOUNTS.remove(mountPoint);
		MOUNTED_ISOS.put(isoFile.getAbsolutePath(), ifs);
		return ifs;
	}

	private static void unmountISOFile(final CdEmuFileSystem ifs)
			throws ISOMountException {
		String[] commands = new String[] { WINCDEMU_PATH,
				WINCDEMU_UNMOUNT_FLAG, ifs.mountPoint };
		Process proc = null;
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
			int result = proc.waitFor();
			if (result != WINCDEMU_SUCCESS) {
				throw new ISOMountException("Bad WinCDEmu result:" + result
						+ " mounting" + ifs.getName() + " at mount point "
						+ ifs.mountPoint + "\nProcessOutput:" + processOut);
			}
		} catch (InterruptedException excep) {
			throw new ISOMountException("InterruptedException mounting "
					+ ifs.getName() + " at mount point " + ifs.mountPoint,
					excep);
		}
		MOUNTED_ISOS.remove(ifs.isoFile.getAbsolutePath());
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

/**
 * 
 */
package uk.ac.ox.bodleian.beam.filesys;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import net.didion.loopy.iso9660.ISO9660FileEntry;
import net.didion.loopy.iso9660.ISO9660FileSystem;
import uk.ac.ox.bodleian.beam.filesys.entry.FileSystemEntries;
import uk.ac.ox.bodleian.beam.filesys.entry.FileSystemEntry;

/**
 * TODO: Decide if Loopy is worth the hassle.<p/>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1
 * 
 * Created 24 Jul 2012:11:36:06
 */

public final class LoopyISOFileSystem extends AbstractFileSystem {
	@SuppressWarnings("unused")
	private ISO9660FileSystem loopyFs;

	/**
	 * @param name
	 * @param path
	 * @param totalSize
	 * @param entries
	 */
	private LoopyISOFileSystem(ISO9660FileSystem loopyFs, String name, String path, long totalSize,
			Map<String, FileSystemEntry> entries) {
		super(name, path, totalSize, entries);
		this.loopyFs = loopyFs;
	}

	/**
	 * @param source
	 * @param entries
	 * @throws IOException 
	 */
	private LoopyISOFileSystem(File source, Map<String, FileSystemEntry> entries) throws IOException {
		super(source, entries);
		this.loopyFs = new ISO9660FileSystem(source, true);
	}

	/**
	 * @param source
	 * @param totalSize
	 * @param entries
	 * @throws IOException 
	 */
	private LoopyISOFileSystem(File source, long totalSize,
			Map<String, FileSystemEntry> entries) throws IOException {
		super(source, totalSize, entries);
		this.loopyFs = new ISO9660FileSystem(source, true);
	}

	/**
	 * @param parent
	 * @param name
	 * @param path
	 * @param totalSize
	 * @param entries
	 */
	private LoopyISOFileSystem(FileSystem parent, ISO9660FileSystem loopyFs, String name, String path,
			long totalSize, Map<String, FileSystemEntry> entries) {
		super(parent, name, path, totalSize, entries);
		this.loopyFs = loopyFs;
	}

	/**
	 * @see uk.ac.ox.bodleian.beam.filesys.FileSystem#getEntryStream(java.lang.String)
	 */
	@Override
	public InputStream getEntryStream(String name)
			throws EntryNotFoundException, DamagedEntryException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see uk.ac.ox.bodleian.beam.filesys.FileSystem#getEntryFile(java.lang.String)
	 */
	@Override
	public File getEntryFile(String name) throws UnsupportedOperationException,
			EntryNotFoundException, DamagedEntryException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param isoFile
	 * @return LoopyLoo
	 * @throws IOException
	 */
	public static final LoopyISOFileSystem createFromFile(File isoFile) throws IOException {
		System.out.println("ISO9660 - Pre-scan found file named: "+  isoFile.getName());
		ISO9660FileSystem loopyFs = new ISO9660FileSystem(isoFile, true);
		 Map<String, FileSystemEntry> entryMap = new HashMap<String, FileSystemEntry>();
		 if (loopyFs != null) {

			try {
				/* ISO9660 entries (files and directories) are not necessarily in
				 * hierarchical order.  Also, directories may be implicit, that
				 * is, referred to in the pathnames of files or directories but
				 * not explicitly present in the form of a directory entry.
				 * 
				 * Since all files and directories need to be associated with 
				 * the correct parent directory in order for aggregate
				 * characterization to work properly, we there are three stages
				 * of processing:
				 * 
				 * (1) Identify all explicit directory entries, creating
				 *     Directory sources and putting them into a map keyed to
				 *     the directory pathname.
				 *     
				 * (2) Identify all implicit directories (by extracting
				 *     directories from pathnames and checking to see if they
				 *     are not already in the map), creating Directory sources
				 *     and putting them into the map.  Also characterize any
				 *     top-level file entries (children of the ISO9660 file) that
				 *     are found.
				 *     
				 * (3) Directly characterize all top-level directories, that
				 *     is, those whose parent is the ISO9660 file.  This will
				 *     implicitly characterize all child files and directories.
				 */
				@SuppressWarnings("unchecked")
				Enumeration<ISO9660FileEntry> en = loopyFs.getEntries();

				/* (1) Identify all directories that are explicit entries. */ 
				while (en.hasMoreElements()) {
					ISO9660FileEntry isoEntry = en.nextElement();
					System.out.println("ISO9660 - Pre-scan found directory named: "+  isoEntry.getName());
					// Now parse it...
					if( isoEntry.isDirectory() ) {
						// FIXME What to do with directories?
						System.out.println("ISO9660 - Found directory named: "+ isoEntry.getName() +" "+isoEntry.getPath());
					} else {
						// FIXME Parse the embedded file: 
						System.out.println("ISO9660 - Found file named: "+ isoEntry.getName() +" "+isoEntry.getPath());
						InputStream entryStream = loopyFs.getInputStream(isoEntry);
						FileSystemEntry fse = FileSystemEntries.fromLoopyISO(isoEntry, entryStream);
						entryMap.put(isoEntry.getName(), fse);
						/* Get the entry-specific properties. */
						/*
	    	            		long crc = entry.getCrc();
	    	            		Digest crc32 = new Digest(AbstractArrayDigester.toHexString(crc),
	    	            				CRC32Digester.ALGORITHM);
	    	            		ISO9660EntryProperties properties =
	    	            			new ISO9660EntryProperties(name, entry.getCompressedSize(), crc32,
	    	            					entry.getComment(),
	    	            					new Date(entry.getTime()));
						 */
						// Setup
					}
				}

			}  
			finally {
				loopyFs.close();
			}
		}
		 return new LoopyISOFileSystem(isoFile, entryMap);

	}
}

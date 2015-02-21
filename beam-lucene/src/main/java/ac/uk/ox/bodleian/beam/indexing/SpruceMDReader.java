/**
 * 
 */
package ac.uk.ox.bodleian.beam.indexing;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import argo.jdom.JdomParser;
import argo.jdom.JsonRootNode;
import argo.saj.InvalidSyntaxException;

import uk.ac.ox.bodleian.beam.filesystem.FileSystem.DamagedEntryException;
import uk.ac.ox.bodleian.beam.filesystem.FileSystem.EntryNotFoundException;
import uk.ac.ox.bodleian.beam.filesystem.FileSystems;
import uk.ac.ox.bodleian.beam.filesystem.JavaIOFileSystem;
import uk.ac.ox.bodleian.beam.filesystem.entry.FileSystemEntry;

/**
 * @author  <a href="mailto:carl.wilson@keepitdigital.eu">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1
 * 
 * Created 20 Sep 2012:10:54:39
 */

public class SpruceMDReader {
	private static LuceneIndexer INDEXER;
	private static final JdomParser JDOM_PARSER = new JdomParser();
	/**
	 * @param args
	 * @throws DamagedEntryException 
	 * @throws EntryNotFoundException 
	 * @throws IOException 
	 * @throws UnsupportedOperationException 
	 * @throws InvalidSyntaxException 
	 */
	public static void main(String[] args) throws UnsupportedOperationException, IOException, EntryNotFoundException, DamagedEntryException, InvalidSyntaxException {
		String parseRootPath = args[0];
		File parseRootDir = new File(parseRootPath);
		if (!parseRootDir.isDirectory()) {
			throw new IllegalArgumentException("parse dir not existing dir");
		}
		String idxPath = args[1];
		File indexDir = new File(idxPath);
		INDEXER = LuceneIndexer.createIndex(indexDir);
		indexMD(parseRootDir);
	}

	private static void indexMD(File parseRoot) throws UnsupportedOperationException, IOException, EntryNotFoundException, DamagedEntryException {
		JavaIOFileSystem jiofs = FileSystems.fromDirectory(parseRoot);
		for (FileSystemEntry entry : jiofs.getEntries()) {
			// Do we have the JSON file?
			if (FilenameUtils.getExtension(entry.getName()).equalsIgnoreCase("json")) {
				// Parse the sha-256 id
				String jsonText = FileUtils.readFileToString(jiofs.getEntryFile(entry.getName()));
				JsonRootNode json;
				try {
					json = JDOM_PARSER.parse(jsonText);
					String SHA256 = json.getStringValue("SHA256");
					String path = json.getStringValue("Original-File-Path");
					String textName = entry.getName().replace("tikaextract.json", "textextract.txt");
					String fullText = FileUtils.readFileToString(jiofs.getEntryFile(textName));
					String normShaName = entry.getName().replace("tikaextract.json", "textnormsha256.txt");
					String normText256 = FileUtils.readFileToString(jiofs.getEntryFile(normShaName));
					INDEXER.addDocument(SHA256, path, fullText, normText256);
				} catch (InvalidSyntaxException excep) {
					System.err.println("Failed to parse JSON file:" + entry.getName());
					System.err.println(excep.getMessage());
				}
			}
		}
		INDEXER.closeIndex();
	}
}

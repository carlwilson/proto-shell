/**
 * 
 */
package ac.uk.ox.bodleian.beam.indexing;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LimitTokenCountAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

/**
 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a> <a
 *         href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1
 * 
 *          Created 20 Sep 2012:17:39:30
 */
public class LuceneIndexer {
	private static Analyzer STANDARD_ANALYZER = new StandardAnalyzer(
			Version.LUCENE_36);
	private static Analyzer LIMITED_TOKEN_ANALYZER = new LimitTokenCountAnalyzer(
			STANDARD_ANALYZER, Integer.MAX_VALUE);
	private static IndexWriterConfig IW_CONFIG = new IndexWriterConfig(
			Version.LUCENE_36, LIMITED_TOKEN_ANALYZER);
	static {
		IW_CONFIG.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
	}
	private Directory indexDir;
	private IndexWriter indexWriter;

	private LuceneIndexer(File idxDir) {
		try {
			this.indexDir = FSDirectory.open(idxDir);
			this.indexWriter = new IndexWriter(this.indexDir, IW_CONFIG);
		} catch (CorruptIndexException excep) {
			throw new IllegalStateException("Lucene index is corrupt.", excep);
		} catch (LockObtainFailedException excep) {
			throw new IllegalStateException(
					"Couldn't obtain lock on Lucene index.", excep);
		} catch (IOException excep) {
			throw new IllegalStateException(
					"IOException preparing Lucene index writer.", excep);
		}
	}

	/**
	 * Factory method that creates a new Lucene index in the specified
	 * directory. The method will create the index directory if it doesn't
	 * already exist.
	 * 
	 * @param indexDir
	 *            the directory location for the Lucene index. This can be an
	 *            existing directory / index.
	 * @return a new LuceneIndexer instance
	 * @throws IllegalArgumentException
	 *             if the indexDir parameter is null or if the parameter points
	 *             to an existing file, rather than a directory.
	 * @throws IllegalStateException
	 *             if the indexDir parameter points to a non existant file AND
	 *             the creation of a directory fails.
	 */
	public static LuceneIndexer createIndex(File indexDir) {
		if (indexDir == null)
			throw new IllegalArgumentException("indexDir == null");
		if (!indexDir.isFile())
			throw new IllegalArgumentException(
					"indexDir must be a directory NOT a file");
		if (!indexDir.exists()) {
			if (!indexDir.mkdirs()) {
				throw new IllegalStateException("Couldn't create indexDir:"
						+ indexDir);
			}
		}
		return new LuceneIndexer(indexDir);
	}

	/**
	 * @return the number of documents that exist in the index
	 * @throws IOException
	 *             if there's a problem returning the document count, indicative
	 *             of a storage error.
	 */
	public int getIndexedDocumentCount() throws IOException {
		return this.indexWriter.numDocs();
	}

	/**
	 * @param normText256
	 * @param path
	 * @param sha256
	 * @param fullText
	 * @throws IOException
	 */
	public void addDocument(String sha256, String path, String fullText,
			String normText256) throws IOException {
		Document doc = new Document();
		doc.add(new Field("sha256", sha256, Store.YES, Index.NOT_ANALYZED));
		doc.add(new Field("path", path, Store.YES, Index.NOT_ANALYZED));
		doc.add(new Field("normText256", normText256, Store.YES,
				Index.NOT_ANALYZED));
		doc.add(new Field("fullText", fullText, Store.YES, Index.ANALYZED));
		try {
			this.indexWriter.addDocument(doc);
		} catch (CorruptIndexException excep) {
			throw new IllegalStateException("Lucene index is corrupt.", excep);
		}
	}

	/**
	 * Merge commit and close the Lucene Index
	 * 
	 * @throws IOException
	 */
	public void closeIndex() throws IOException {
		try {
			this.indexWriter.forceMerge(1);
			this.indexWriter.commit();
			this.indexWriter.close();
			this.indexDir.close();
		} catch (CorruptIndexException excep) {
			throw new IllegalStateException("Lucene index is corrupt.", excep);
		}
	}
}

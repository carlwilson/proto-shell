/**
 * 
 */
package uk.ac.ox.bodleian.beam;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import uk.ac.ox.bodleian.beam.audit.BEAMReporter;
import uk.ac.ox.bodleian.beam.audit.Project;
import uk.ac.ox.bodleian.beam.audit.SimpleReporter;
import uk.ac.ox.bodleian.beam.filesystem.iso.CdEmuFileSystem;
import uk.ac.ox.bodleian.beam.filesystem.iso.ISOFileSystemManager.ISOMountException;
import uk.ac.ox.bodleian.beam.model.BEAMAccessionOld;
import uk.ac.ox.bodleian.beam.model.BEAMCollectionOld;

/**
 * TODO: JavaDoc for AuditWorkflow.<p/>
 * TODO: Tests for AuditWorkflow.<p/>
 * TODO: Add all BEAM metadata properties (see email from Susan).<p/>
 * TODO: Command Line Interface commons-cli.<p/>
 * TODO: CLI Reporting to be apt-get like.</p> 
 * TODO: Generalise reporting to a helper class (progress, summary, etc.).</p>
 *  
 * @author <a href="mailto:carl.wilson@keepitdigital.eu">Carl Wilson</a> <a
 *         href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1
 * 
 *          Created 3 Jul 2012:02:31:43
 */

public class AuditWorkflow {
	private static Logger LOGGER = Logger.getLogger(AuditWorkflow.class);
	{
		DOMConfigurator.configure("src/main/resources/log4j.xml");
	}
	
	private static BEAMReporter REPORTER = SimpleReporter.getBEAMReporter(); 
	private static final String ACC_DIR_NAME = "accessiondata";
	private File rootDir;
	private Map<String, BEAMCollectionOld> BEAMCollections = new HashMap<String, BEAMCollectionOld>();
	private List<File> BEAMCollectionRoots = new ArrayList<File>();
	private int collectionsProcessing = 0;
	private Date start = new Date();
	private Date end = this.start;

	@SuppressWarnings("unused")
	private AuditWorkflow() {
		LOGGER.fatal("In default constructor.");
		throw new AssertionError("In default constructor.");
	}

	/**
	 * @param dir
	 * @param outputDir
	 * @throws IOException
	 */
	AuditWorkflow(File dir, File outputDir) throws IOException {
		// Check that the directory to be scanned is not null and exists
		if (dir == null) {
			LOGGER.fatal("dir == null");
			throw new IllegalArgumentException("dir == null");
		}
		if (!dir.isDirectory()) {
			LOGGER.fatal("dir.isDirectory() != true");
			throw new IllegalArgumentException("dir.isDirectory() != true");
		}

		this.rootDir = dir;

		// Check the output dir is an existing directory
		if (outputDir == null) {
			LOGGER.fatal("outputDir == null");
			throw new IllegalArgumentException("outputDir == null");
		}
		if (!outputDir.isDirectory()) {
			LOGGER.fatal("outputDir.isDirectory() != true");
			throw new IllegalArgumentException("outputDir.isDirectory() != true");
		}

		if (!outputDir.exists()) {
			// Create the output directory
			if (!outputDir.mkdirs()) {
				LOGGER.fatal("outputDir.mkdirs() != true");
				throw new AssertionError("outputDir.mkdirs() != true");
			}
		}

		// Now process the collections
		this.findCollectionRoots();
		LOGGER.info("Found " + this.BEAMCollectionRoots.size() + " collections.");
		for (File collectionRoot : this.BEAMCollectionRoots) {
			LOGGER.info("Assessing collection " + collectionRoot.getName());
			BEAMCollectionOld collection = BEAMCollectionOld
					.getInstanceFromDirectory(
							collectionRoot);
			this.BEAMCollections.put(collection.getName(), collection);
			REPORTER.addCollection(collection);
			new Thread(collection).start();
			this.collectionsProcessing++;
		}

		while (this.collectionsProcessing > 0) {
			REPORTER.reportProgress();
			try {
				Thread.sleep(SimpleReporter.WAIT_PERIOD);
			} catch (InterruptedException excep) {
				// TODO Auto-generated catch block
				excep.printStackTrace();
			}
			this.collectionsProcessing = 0;
			for (BEAMCollectionOld collection : this.BEAMCollections.values()) {
				if (collection.isProcessing())
					this.collectionsProcessing++;
			}
		}
		this.end = new Date();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LOGGER.info(Project.NAME + " v." + Project.VERSION);
		File directory = new File(args[0]);
		File outputDir = new File(args[1]);
		try {
			AuditWorkflow workflow = new AuditWorkflow(directory, outputDir);
			workflow.outputSummary();
		} catch (IOException excep) {
			LOGGER.fatal(excep);
		} finally {
			try {
				CdEmuFileSystem.unmountAll();
			} catch (ISOMountException excep) {
				LOGGER.trace(excep);
			}
		}
	}

	private void outputSummary() {
		long periodInMillisecs = (this.end.getTime() - this.start.getTime());
		float avgMsPerFile = Float.valueOf(periodInMillisecs)
				/ Float.valueOf(this.BEAMCollections.size());
		LOGGER.info("Avg Time/Collection:"
				+ Math.round(avgMsPerFile / 1000) + " s.");
		LOGGER.info("processed "
				+ this.BEAMCollections.size() + " collections "
				+ Math.round((periodInMillisecs) / 1000) + " seconds.");
		//LOGGER.info(gson.toJson(Environments.getEnvironment()));
		for (BEAMCollectionOld collection : this.BEAMCollections.values()) {
			LOGGER.info(collection.toString());
			for (BEAMAccessionOld accession : collection.getAccessions()) {
				LOGGER.info(accession.toString());
			}
		}

	}
	

	private void findCollectionRoots() {
		// OK let's find the root collection dirs.
		// Iterate through directories below root
		for (File dir : this.getDirs(this.rootDir)) {
			 // Look for accessiondata directories
			if (dir.getName().equals(ACC_DIR_NAME)) {
				// Check that the parent starts with the accession prefix
				if (dir.getParentFile().getName().toLowerCase().startsWith(BEAMAccessionOld.ACCESSION_DIR_PREFIX)) {
					// If so then get the parent's parent as a collection root
					this.BEAMCollectionRoots.add(dir.getParentFile().getParentFile());
				}
			}
		}
	}

	private Set<File> getDirs(File root) {
		// Helper function to get all of the directories below the current root
		// and return as a set
		Set<File> dirs = new HashSet<File>();
		// Loop through the files below root
		for (File file : root.listFiles()) {
			// If it's a directory
			if (file.isDirectory()) {
				// add to the return set
				dirs.add(file);
				// and dig recursively for more dirs
				dirs.addAll(getDirs(file));
			}
		}
		return dirs;
	}
	
	
}

/**
 * 
 */
package uk.ac.ox.bodleian.beam.audit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import uk.ac.ox.bodleian.beam.filesystem.bytestream.ByteStreams;
import uk.ac.ox.bodleian.beam.model.BEAMAccessionOld;
import uk.ac.ox.bodleian.beam.model.BEAMCollectionOld;

/**
 * TODO JavaDoc for SimpleReporter.</p>
 * TODO Tests for SimpleReporter.</p>
 * TODO Implementation for SimpleReporter.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 5 Aug 2012:21:40:45
 */

public class SimpleReporter implements BEAMReporter {
	/** The default reporter wait period */
	public static final long WAIT_PERIOD = 10000L;
	private static final Date START = new Date();
	private static Logger LOGGER = Logger.getLogger(BEAMReporter.class);
	private static BEAMReporter INSTANCE = new SimpleReporter();
	private static List<BEAMCollectionOld> COLLECTIONS = new ArrayList<BEAMCollectionOld>();
	private static long TOTAL_BYTES = 0L;
	private static double NEXT_REPORT = 0.0;
	
	/**
	 * @return the reporter instance
	 */
	public static BEAMReporter getBEAMReporter() {
		return INSTANCE;
	}
	
	private SimpleReporter() {
		/** Disable no-arg constructor */
	}

	/**
	 * @see uk.ac.ox.bodleian.beam.audit.BEAMReporter#addCollection(uk.ac.ox.bodleian.beam.model.BEAMCollectionOld)
	 */
	@Override
	public void addCollection(BEAMCollectionOld collection) {
		TOTAL_BYTES+=collection.getTotalSize();
		COLLECTIONS.add(collection);
	}

	/**
	 * @see uk.ac.ox.bodleian.beam.audit.BEAMReporter#reportProgress()
	 */
	@Override
	public void reportProgress() {
		long bytesProcessed = 0L;
		for (BEAMCollectionOld collection : COLLECTIONS) {
			for (BEAMAccessionOld accession : collection.getAccessions()) {
				bytesProcessed += accession.getBytesHashed();
			}
		}
		reportProgressByBytes(bytesProcessed);
	}

	protected static final void reportProgressByBytes(long processed) {
		BigDecimal repInc = BigDecimal.valueOf(0.05);
		double progress = Double.valueOf(processed) / Double.valueOf(TOTAL_BYTES);
		long periodMillis = new Date().getTime() - START.getTime();
		double avgMillis = Double.valueOf(periodMillis)
				/ Double.valueOf(processed);
		long expectedSecs = Math
				.round(avgMillis * (TOTAL_BYTES - processed) / 1000.0);

		if ((progress > NEXT_REPORT)) {
			LOGGER.info(ByteStreams.humanReadableByteCount(processed, true) + "/"
					+ ByteStreams.humanReadableByteCount(TOTAL_BYTES, true) + " "
					+ Math.round(progress * 100) + "% finished in "
					+ expectedSecs + " secs.\r");
			NEXT_REPORT = BigDecimal.valueOf(NEXT_REPORT).add(repInc).doubleValue();
		}
	}
}

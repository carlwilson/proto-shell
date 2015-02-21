/**
 * 
 */
package uk.ac.ox.bodleian.beam.model;

/**
 * TODO JavaDoc for ProgressReporter.</p>
 * TODO Tests for ProgressReporter.</p>
 * TODO Implementation for ProgressReporter.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 23 Oct 2012:22:33:30
 */

public interface ProgressReporter {
	/**
	 * @return the time in millisecs that the operation started, or 0L if not started
	 */
	public long getStartTime();
	/**
	 * @return the time the operation finished, or 0L if not yet finished
	 */
	public long getFinishTime();
	/**
	 * @return the estimated number of milliseconds remaining
	 */
	public long getEstimatedMillis();
	/**
	 * @return the number of units processed
	 */
	public long getProcessedCount();
	/**
	 * @return the total number of units to process
	 */
	public long getTotalCount();
	/**
	 * @return the progress of the operation expressed as a number between 0 and 1 which are the extreme states, 0 meaning not started, 1 meaning complete
	 */
	public double getProgress();
	/**
	 * @return a text description of the units
	 */
	public String getUnits();
}

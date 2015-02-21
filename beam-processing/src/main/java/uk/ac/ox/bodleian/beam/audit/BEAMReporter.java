/**
 * 
 */
package uk.ac.ox.bodleian.beam.audit;

import uk.ac.ox.bodleian.beam.model.BEAMCollectionOld;


/**
 * TODO: JavaDoc for BEAMReporter.<p/>
 * TODO: Tests for BEAMReporter.<p/>
 * TODO: Generalise reporting to this interface (progress, summary, etc.).</p> 
 *
 * Interface to define reporting behaviour
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson-bl">carlwilson-bl AT github</a>
 * @version 0.1
 * 
 * Created 17 Jul 2012:13:39:45
 */

public interface BEAMReporter {
	/**
	 * @param collection
	 */
	public void addCollection(BEAMCollectionOld collection);
	
	/**
	 * 
	 */
	public void reportProgress();
}

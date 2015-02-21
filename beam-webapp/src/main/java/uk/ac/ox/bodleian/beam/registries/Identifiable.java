/**
 * 
 */
package uk.ac.ox.bodleian.beam.registries;

import java.io.Serializable;

/**
 * TODO JavaDoc for Identifiable.</p>
 * TODO Tests for Identifiable.</p>
 * TODO Implementation for Identifiable.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 24 Sep 2012:00:36:53
 */

public interface Identifiable extends Serializable {
	/**
	 * @return the items id
	 */
	public String getId();
}

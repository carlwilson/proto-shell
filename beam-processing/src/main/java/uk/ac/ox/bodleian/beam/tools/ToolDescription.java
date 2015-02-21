/**
 * 
 */
package uk.ac.ox.bodleian.beam.tools;

/**
 * TODO JavaDoc for ToolDescription.</p>
 * TODO Tests for ToolDescription.</p>
 * TODO Implementation for ToolDescription.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 14 Sep 2012:09:40:27
 */

public interface ToolDescription {
	/**
	 * @return the id of the tool
	 */
	public String getId();
	/**
	 * @return the name of the tool
	 */
	public String getName();
	/**
	 * @return the version of the tool
	 */
	public String getVersion();
	/**
	 * @return a text description of the tool
	 */
	public String getDescription();
}

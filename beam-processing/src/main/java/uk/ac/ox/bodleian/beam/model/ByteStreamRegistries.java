/**
 * 
 */
package uk.ac.ox.bodleian.beam.model;

/**
 * TODO JavaDoc for ByteStreamRegistries.</p>
 * TODO Tests for ByteStreamRegistries.</p>
 * TODO Implementation for ByteStreamRegistries.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 17 Sep 2012:09:38:03
 */

public class ByteStreamRegistries {
	private ByteStreamRegistries() {
		throw new AssertionError();
	}
	/**
	 * @return the ByteStreamRRegistry instance
	 */
	public static ByteStreamRegistry getInstance() {
		return ByteStreamRegistryImpl.getInstance();
	}
}

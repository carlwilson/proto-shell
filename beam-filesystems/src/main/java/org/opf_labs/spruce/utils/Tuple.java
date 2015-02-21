/**
 * 
 */
package org.opf_labs.spruce.utils;

/**
 * TODO JavaDoc for Tuple.</p>
 * TODO Tests for Tuple.</p>
 * TODO Implementation for Tuple.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 15 Oct 2012:20:10:54
 * @param <X> 
 * @param <Y> 
 */
public final class Tuple<X, Y> {
	/**
	 * 
	 */
	public final X x;
	/**
	 * 
	 */
	public final Y y;
	/**
	 * @param x
	 * @param y
	 */
	public Tuple(X x, Y y) {
		this.x = x;
		this.y = y;
	}
}

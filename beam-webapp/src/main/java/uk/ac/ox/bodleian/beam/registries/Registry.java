/**
 * 
 */
package uk.ac.ox.bodleian.beam.registries;

import java.util.Set;

/**
 * TODO JavaDoc for CacheRegistry.</p>
 * TODO Tests for CacheRegistry.</p>
 * TODO Implementation for CacheRegistry.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 24 Sep 2012:00:35:41
 * @param <I> 
 */
public interface Registry<I> {
	/**
	 * @return the registry name
	 */
	public String getName();

	/**
	 * @param item the item to add to the registry
	 * @return the item now registered for this id
	 */
	public I register(I item);

	/**
	 * @param id the string key for the item to be retrieved
	 * @return the retrieved item
	 * @throws ItemNotFoundException if the item couldn't be found 
	 */
	public I retrieve(String id) throws ItemNotFoundException;
	
	/**
	 * @return the set of all the items in the registry
	 */
	public Set<I> retrieveAll();
	
	/**
	 * @return a count of the number of items in the registry
	 */
	public int count();

	/**
	 * @param item the item to remove from the registry
	 * @return the item removed or null if no item found
	 */
	public I remove(I item);
	
	/**
	 * clears all registry contents
	 */
	public void clear();
	
	/**
	 * TODO JavaDoc for ItemNotFoundException.</p>
	 * TODO Tests for ItemNotFoundException.</p>
	 * TODO Implementation for ItemNotFoundException.</p>
	 * 
	 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
	 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
	 * @version 0.1
	 * 
	 * Created 26 Sep 2012:15:03:46
	 */
	public static class ItemNotFoundException extends Exception {
		private static final long serialVersionUID = -6132987508563249986L;
		/**
		 * Default no-arg constructor
		 */
		public ItemNotFoundException() {
			super();
		}
		/**
		 * @param message string message for the exception
		 */
		public ItemNotFoundException(String message) {
			super(message);
		}
		/**
		 * @param message the string message for the exception
		 * @param cause the exception that caused the exception
		 */
		public ItemNotFoundException(String message, Exception cause) {
			super(message, cause);
		}
	}
}

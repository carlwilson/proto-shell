/**
 * 
 */
package uk.ac.ox.bodleian.beam.registries;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
class CacheRegistry<I extends Identifiable> implements Registry<I> {
	private final String name;
	private Map<String, I> items; 

	private CacheRegistry() {
		throw new AssertionError("CacheRegistry in no arg Constructor");
	}
	
	/**
	 * @param root
	 * @param clazz
	 */
	private CacheRegistry(String name) {
		this.name= name;
		this.items = new HashMap<String, I>();
	}

	static <I extends Identifiable> CacheRegistry<I> createCacheRegistry(String name) {
		if (name == null) throw new IllegalArgumentException("name == null");
		return new CacheRegistry<I>(name);
	}

	/**
	 * 
	 * @see uk.ac.ox.bodleian.beam.registries.Registry#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * 
	 * @see uk.ac.ox.bodleian.beam.registries.Registry#retrieve(java.lang.String)
	 */
	@Override
	public final I retrieve(String key) throws ItemNotFoundException {
		I retVal = this.items.get(key);
		if (retVal == null) {
			throw new ItemNotFoundException("Item with key:" + key + " not found.");
		}
		return retVal;
	}
	
	
	/**
	 * 
	 * @see uk.ac.ox.bodleian.beam.registries.Registry#retrieveAll()
	 */
	@Override
	public final Set<I> retrieveAll() {
		Set<I> all = new HashSet<I>(this.items.values());
		return Collections.unmodifiableSet(all);
	}

	/**
	 * @see uk.ac.ox.bodleian.beam.registries.Registry#register(Object)
	 */
	@Override
	public I register(I item) {
		this.items.put(item.getId(), item);
		return item;
	}
	
	/**
	 * 
	 * @see uk.ac.ox.bodleian.beam.registries.Registry#count()
	 */
	@Override
	public int count() {
		return this.items.size();
	}
	
	/**
	 * @see uk.ac.ox.bodleian.beam.registries.Registry#remove(Object)
	 */
	@Override
	public I remove(I item) {
		return this.items.remove(item.getId());
	}
	
	/**
	 * @see uk.ac.ox.bodleian.beam.registries.Registry#clear()
	 */
	@Override
	public void clear() {
		this.items.clear();
	}
}

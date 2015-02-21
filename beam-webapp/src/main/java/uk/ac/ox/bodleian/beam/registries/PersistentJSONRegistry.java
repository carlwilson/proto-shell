/**
 * 
 */
package uk.ac.ox.bodleian.beam.registries;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * TODO JavaDoc for PersistentJSONRegistry.</p>
 * TODO Tests for PersistentJSONRegistry.</p>
 * TODO Implementation for PersistentJSONRegistry.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 24 Sep 2012:02:45:28
 * @param <I> 
 */
class PersistentJSONRegistry<I extends Identifiable> implements JSONRegistry<I> { 
	private static final String SUFFIX = ".xml";
	private ObjectMapper mapper;
	private File root;
	private final Class<I> clazz;
	private Registry<I> cache;

	private PersistentJSONRegistry() {
		throw new AssertionError();
	}
	
	private PersistentJSONRegistry(File root, Class<I> clazz) throws JsonParseException, JsonMappingException, IOException {
		this(root, root.getName(), clazz);
	}

	private PersistentJSONRegistry(File root, String name, Class<I> clazz) throws JsonParseException, JsonMappingException, IOException {
		this(root, name, clazz, new ObjectMapper());
	}

	private PersistentJSONRegistry(File root, String name, Class<I> clazz, ObjectMapper mapper) throws JsonParseException, JsonMappingException, IOException {
		this.root = root;
		if (!root.exists() && !root.mkdirs()) {
			throw new IllegalStateException("Couldn't initialise Registry at:" + this.root);
		}
		this.cache = CacheRegistry.createCacheRegistry(name);
		this.clazz = clazz;
		this.mapper = mapper;
		this.loadCache();
	}

	public static <E extends Identifiable> PersistentJSONRegistry<E> createRegistry(File root, Class<E> clazz) throws JsonParseException, JsonMappingException, IOException {
		if (root == null) throw new IllegalArgumentException("root == null");
		if (clazz == null) throw new IllegalArgumentException("clazz == null");
		if (!root.isDirectory()) {
			throw new FileNotFoundException("");
		}
		return new PersistentJSONRegistry<E>(root, clazz);
	}
	
	public static <E extends Identifiable> PersistentJSONRegistry<E> createRegistry(File root, String name, Class<E> clazz) throws JsonParseException, JsonMappingException, IOException {
		if (root == null) throw new IllegalArgumentException("root == null");
		if (name == null) throw new IllegalArgumentException("name == null");
		if (clazz == null) throw new IllegalArgumentException("clazz == null");
		if (!root.isDirectory()) {
			throw new FileNotFoundException("");
		}
		return new PersistentJSONRegistry<E>(root, name, clazz);
	}
	
	public static <E extends Identifiable> PersistentJSONRegistry<E> createRegistry(File root, String name, Class<E> clazz, ObjectMapper mapper) throws JsonParseException, JsonMappingException, IOException {
		if (root == null) throw new IllegalArgumentException("root == null");
		if (name == null) throw new IllegalArgumentException("name == null");
		if (clazz == null) throw new IllegalArgumentException("clazz == null");
		if (mapper == null) throw new IllegalArgumentException("mapper == null");
		if (!root.isDirectory()) {
			throw new FileNotFoundException("");
		}
		return new PersistentJSONRegistry<E>(root, name, clazz, mapper);
	}
	
	@Override
	public String getName() {
		return this.cache.getName();
	}
	@Override
	public I register(I item) {
		I prevItem = this.cache.register(item);
		try {
			File f = new File(this.root, filename(item));
			this.mapper.writeValue(f, item);
		} catch (JsonGenerationException excep) {
			// TODO Auto-generated catch block
			excep.printStackTrace();
		} catch (JsonMappingException excep) {
			// TODO Auto-generated catch block
			excep.printStackTrace();
		} catch (IOException excep) {
			// TODO Auto-generated catch block
			excep.printStackTrace();
		}
		return prevItem;
	}
	@Override
	public I retrieve(String key) throws ItemNotFoundException {
		return this.cache.retrieve(key);
	}
	@Override
	public Set<I> retrieveAll() {
		return this.cache.retrieveAll();
	}
	@Override
	public int count() {
		return this.cache.count();
	}
	@Override
	public I remove(I item) {
		try {
			this.cache.retrieve(item.getId());
			File file = new File(this.root, filename(item));
			if (file.exists()) file.delete();
			return this.cache.remove(item);
		} catch (uk.ac.ox.bodleian.beam.registries.Registry.ItemNotFoundException excep) {
			// Do nothing
		}
		return null;
	}
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		this.cache.clear();
		for (File file : this.root.listFiles()) {
			if (!file.isDirectory() && !file.isHidden() && file.getName().endsWith(SUFFIX)) {
				file.delete();
			}
		}
	}

	/**
	 * Initialize the cache from disk.
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	private void loadCache() throws JsonParseException, JsonMappingException, IOException {
		this.cache.clear();
		File[] list = this.root.listFiles();
		for (File file : list) {
			if (!file.isHidden() && file.getName().endsWith(SUFFIX)) {
					this.cache.register(this.mapper.readValue(file, this.clazz));
			}
		}
	}

	private String filename(I item) {
		String prefix = item.getId() == null ? "null" : item.getId();
		return prefix + SUFFIX;
	}

	@Override
	public I fromJSON(String json) throws JsonParseException, JsonMappingException, IOException {
		return this.mapper.readValue(json, this.clazz);
	}

	@Override
	public String toJSON(I toSerialise) throws JsonGenerationException, JsonMappingException, IOException {
		return this.mapper.writeValueAsString(toSerialise);
	}
}

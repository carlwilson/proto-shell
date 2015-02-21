/**
 * 
 */
package uk.ac.ox.bodleian.beam.registries;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

/**
 * TODO JavaDoc for Registries.</p>
 * TODO Tests for Registries.</p>
 * TODO Implementation for Registries.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 9 Oct 2012:20:00:40
 */

public class Registries {
	private Registries() {
		throw new AssertionError("in no arg constructor");
	}
	
	/**
	 * @param name a name for the temp registry
	 * @return a new temporary cache registry backed by a map
	 */
	public static final <I extends Identifiable>Registry <I> createCacheRegistry(String name) {
		if (name == null) throw new IllegalArgumentException("name == null");
		return CacheRegistry.createCacheRegistry(name);
	}
	
	/**
	 * @param root a root directory for the registry
	 * @param clazz the class to be held in the registry
	 * @return a new temporary cache registry backed by a map
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public static final <I extends Identifiable>JSONRegistry<I> createPersistentJSONRegistry(File root, Class<I> clazz) throws JsonParseException, JsonMappingException, IOException {
		return PersistentJSONRegistry.createRegistry(root, root.getName(), clazz, BeamJacksonObjectMapper.getBeamObjectMapper());
	}
}

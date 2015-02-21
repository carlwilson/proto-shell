/**
 * 
 */
package uk.ac.ox.bodleian.beam.registries;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

/**
 * TODO JavaDoc for JSONRegistry.</p>
 * TODO Tests for JSONRegistry.</p>
 * TODO Implementation for JSONRegistry.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 9 Oct 2012:23:54:37
 * @param <I> 
 */

public interface JSONRegistry<I extends Identifiable> extends Registry<I> {
	/**
	 * @param json the JSON form of the object
	 * @return the Object deserialized from JSON
	 * @throws JsonParseException 
	 * @throws JsonMappingException 
	 * @throws IOException 
	 */
	public I fromJSON(String json) throws JsonParseException, JsonMappingException, IOException ;
	/**
	 * @param toSerialise the object to serialize
	 * @return the JSPM form of the object
	 * @throws JsonGenerationException 
	 * @throws JsonMappingException 
	 * @throws IOException 
	 */
	public String toJSON(I toSerialise) throws JsonGenerationException, JsonMappingException, IOException;
}

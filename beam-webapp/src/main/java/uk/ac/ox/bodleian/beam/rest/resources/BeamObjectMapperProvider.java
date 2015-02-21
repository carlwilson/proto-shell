/**
 * 
 */
package uk.ac.ox.bodleian.beam.rest.resources;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.ObjectMapper;

import uk.ac.ox.bodleian.beam.registries.BeamJacksonObjectMapper;

/**
 * TODO JavaDoc for BeamObjectMapperProvider.</p>
 * TODO Tests for BeamObjectMapperProvider.</p>
 * TODO Implementation for BeamObjectMapperProvider.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 10 Oct 2012:12:17:49
 */
@Provider
public class BeamObjectMapperProvider implements ContextResolver<ObjectMapper> {

	@Override
	public ObjectMapper getContext(Class<?> type) {
		// TODO Auto-generated method stub
		return BeamJacksonObjectMapper.getBeamObjectMapper();
	}

}

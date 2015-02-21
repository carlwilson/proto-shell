/**
 * 
 */
package uk.ac.ox.bodleian.beam.registries;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.opf_labs.spruce.bytestream.ByteStreamId;
import org.opf_labs.spruce.bytestream.ByteStreamIdImpl;
import org.opf_labs.spruce.filesystem.ByteStreamManifest;
import org.opf_labs.spruce.filesystem.ByteStreamManifestImpl;
import org.opf_labs.spruce.filesystem.Entry;
import org.opf_labs.spruce.filesystem.EntryImpl;
import org.opf_labs.spruce.filesystem.EntryManifest;
import org.opf_labs.spruce.filesystem.EntryManifestImpl;
import org.opf_labs.spruce.filesystem.EntryTuple;
import org.opf_labs.spruce.filesystem.EntryTupleImpl;

/**
 * TODO JavaDoc for BeamJacksonObjectMapper.</p>
 * TODO Tests for BeamJacksonObjectMapper.</p>
 * TODO Implementation for BeamJacksonObjectMapper.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 10 Oct 2012:12:23:39
 */

public class BeamJacksonObjectMapper {
	private final static ObjectMapper MAPPER;
	static {
		SimpleModule module = new SimpleModule("beam-webapp", new Version(0, 3, 5, "SNAPSHOT"));
		module.addAbstractTypeMapping(ByteStreamId.class, ByteStreamIdImpl.class);
		module.addAbstractTypeMapping(ByteStreamManifest.class, ByteStreamManifestImpl.class);
		module.addAbstractTypeMapping(Entry.class, EntryImpl.class);
		module.addAbstractTypeMapping(EntryManifest.class, EntryManifestImpl.class);
		module.addAbstractTypeMapping(EntryTuple.class, EntryTupleImpl.class);
		MAPPER = new ObjectMapper();
		MAPPER.registerModule(module);
	}

	private BeamJacksonObjectMapper() {
		
	}
	
	/**
	 * @return the BEAM Object mapper
	 */
	public static ObjectMapper getBeamObjectMapper() {
		return MAPPER;
	}
}

/**
 * 
 */
package uk.ac.ox.bodleian.beam.rest.resources;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

/**
 * TODO JavaDoc for ByteStreamServices.</p>
 * TODO Tests for ByteStreamServices.</p>
 * TODO Implementation for ByteStreamServices.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 22 Sep 2012:15:13:58
 */
@Path("bytestream")
public class ByteStreamServices {
	/**
	 * @param upStream the input stream to identify
	 * @param fdcd the form data
	 * @return the identity of the byte stream
	 */
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Path("identify")
	@POST
	public String identify(@FormDataParam("file") InputStream upStream, @FormDataParam("file") FormDataContentDisposition fdcd) {
		return "ByteStreamId";
	}
}

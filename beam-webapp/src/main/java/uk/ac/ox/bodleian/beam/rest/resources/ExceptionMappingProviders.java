/**
 * 
 */
package uk.ac.ox.bodleian.beam.rest.resources;

import java.io.FileNotFoundException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import uk.ac.ox.bodleian.beam.registries.Registry.ItemNotFoundException;

/**
 * TODO JavaDoc for ExceptionMappingProviders.</p> TODO Tests for
 * ExceptionMappingProviders.</p> TODO Implementation for
 * ExceptionMappingProviders.</p>
 * 
 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 *          Created 6 Oct 2012:14:31:27
 */

public class ExceptionMappingProviders {

	/**
	 * TODO JavaDoc for ItemNotFoundExceptionMapper.</p> TODO Tests for
	 * ItemNotFoundExceptionMapper.</p> TODO Implementation for
	 * ItemNotFoundExceptionMapper.</p>
	 * 
	 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl
	 *         Wilson</a>.</p> <a
	 *         href="https://github.com/carlwilson">carlwilson AT
	 *         github</a>.</p>
	 * @version 0.1
	 * 
	 *          Created 26 Sep 2012:15:08:19
	 */
	@Provider
	public static class ItemNotFoundExceptionMapper implements
			ExceptionMapper<ItemNotFoundException> {
		@Override
		public Response toResponse(ItemNotFoundException ex) {
			return Response.status(404).entity(ex.getMessage())
					.type(MediaType.TEXT_PLAIN).build();
		}
	}

	/**
	 * TODO JavaDoc for ItemNotFoundExceptionMapper.</p> TODO Tests for
	 * ItemNotFoundExceptionMapper.</p> TODO Implementation for
	 * ItemNotFoundExceptionMapper.</p>
	 * 
	 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl
	 *         Wilson</a>.</p> <a
	 *         href="https://github.com/carlwilson">carlwilson AT
	 *         github</a>.</p>
	 * @version 0.1
	 * 
	 *          Created 26 Sep 2012:15:08:19
	 */
	@Provider
	public static class IllegalStateExceptionMapper implements
			ExceptionMapper<IllegalStateException> {
		@Override
		public Response toResponse(IllegalStateException ex) {
			return Response.status(500).entity(ex.getMessage())
					.type(MediaType.TEXT_PLAIN).build();
		}
	}

	/**
	 * TODO JavaDoc for FileNotFoundExceptionMapper.</p> TODO Tests for
	 * FileNotFoundExceptionMapper.</p> TODO Implementation for
	 * FileNotFoundExceptionMapper.</p>
	 * 
	 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl
	 *         Wilson</a>.</p> <a
	 *         href="https://github.com/carlwilson">carlwilson AT
	 *         github</a>.</p>
	 * @version 0.1
	 * 
	 *          Created 26 Sep 2012:16:22:02
	 */
	@Provider
	public static class FileNotFoundExceptionMapper implements
			ExceptionMapper<FileNotFoundException> {
		@Override
		public Response toResponse(FileNotFoundException ex) {
			return Response.status(404).entity(ex.getMessage())
					.type(MediaType.TEXT_PLAIN).build();
		}
	}

	/**
	 * TODO JavaDoc for CollectionExistsExceptionMapper.</p> TODO Tests for
	 * CollectionExistsExceptionMapper.</p> TODO Implementation for
	 * CollectionExistsExceptionMapper.</p>
	 * 
	 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl
	 *         Wilson</a>.</p> <a
	 *         href="https://github.com/carlwilson">carlwilson AT
	 *         github</a>.</p>
	 * @version 0.1
	 * 
	 *          Created 26 Sep 2012:18:05:16
	 */
	@Provider
	public static class CollectionExistsExceptionMapper implements
			ExceptionMapper<ItemExistsException> {
		@Override
		public Response toResponse(ItemExistsException ex) {
			return Response.status(409).entity(ex.getMessage())
					.type(MediaType.TEXT_PLAIN).build();
		}
	}

	/**
	 * TODO JavaDoc for ItemExistsException.</p> TODO Tests for
	 * ItemExistsException.</p> TODO Implementation for
	 * ItemExistsException.</p>
	 * 
	 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl
	 *         Wilson</a>.</p> <a
	 *         href="https://github.com/carlwilson">carlwilson AT
	 *         github</a>.</p>
	 * @version 0.1
	 * 
	 *          Created 26 Sep 2012:18:03:16
	 */
	public static class ItemExistsException extends Exception {
		private static final long serialVersionUID = 2831219063745490589L;

		/**
		 * Default no-arg constructor
		 */
		public ItemExistsException() {
			super();
		}

		/**
		 * @param message
		 *            string message for the exception
		 */
		public ItemExistsException(String message) {
			super(message);
		}

		/**
		 * @param message
		 *            the string message for the exception
		 * @param cause
		 *            the exception that caused the exception
		 */
		public ItemExistsException(String message, Exception cause) {
			super(message, cause);
		}
	}

}

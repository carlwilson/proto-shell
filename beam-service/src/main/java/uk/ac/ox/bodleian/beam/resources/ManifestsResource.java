/**
 * 
 */
package uk.ac.ox.bodleian.beam.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;

import org.opf_labs.spruce.bytestream.ByteStreamId;
import org.opf_labs.spruce.bytestream.ByteStreams;
import org.opf_labs.spruce.filesystem.ByteStreamManifest;
import org.opf_labs.spruce.filesystem.Entry;
import org.opf_labs.spruce.filesystem.Entry.EntryStatus;
import org.opf_labs.spruce.filesystem.EntryManifest;
import org.opf_labs.spruce.filesystem.EntryTuple;
import org.opf_labs.spruce.filesystem.FileSystems;

import uk.ac.ox.bodleian.beam.model.ProgressReporter;
import uk.ac.ox.bodleian.beam.model.collection.BeamCollection;
import uk.ac.ox.bodleian.beam.views.ApplicationView;
import uk.ac.ox.bodleian.beam.views.ManifestsView;

import com.sun.jersey.spi.resource.Singleton;

/**
 * TODO JavaDoc for ManifestsResource.</p> TODO Tests for ManifestsResource.</p>
 * TODO Implementation for ManifestsResource.</p>
 * 
 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 *          Created 23 Oct 2012:15:04:25
 */
@Singleton
@Path("/manifests")
public class ManifestsResource {
	private static final Map<URI, ByteStreamManifestCreator> CACHE = new HashMap<URI, ByteStreamManifestCreator>();

	/**
	 * the list page for all manifests in the cache
	 * 
	 * @return the home page for the BEAM application
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
	public ManifestsView getHtml() {
		return ManifestsView.getNewInstance("manifests.ftl", CACHE);
	}

	/**
	 * the list page for all manifests in the cache
	 * 
	 * @return the home page for the BEAM application
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Map<URI, ByteStreamManifestCreator> getJson() {
		return CACHE;
	}

	/**
	 * @param root
	 * @return
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("/{root}")
	public ManifestsView getManifestHtml(@PathParam("root") final URI root) {
		System.out.println("GET html" + root);
		return ManifestsView.getNewInstance("manifests.ftl",
				getManifestJson(root));
	}

	/**
	 * @param root
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{root}")
	public ByteStreamManifestCreator getManifestJson(
			@PathParam("root") final URI root) {
		System.out.println("GET json" + root);
		if (CACHE.containsKey(root))
			return CACHE.get(root);
		throw new WebApplicationException(Status.NOT_FOUND);
	}

	/**
	 * @param root
	 * @return
	 */
	@PUT
	@Path("/{root}")
	public Response createByteStreamManifest(@PathParam("root") final URI root) {
		System.out.println("PUT" + root);
		// First check that the root path points to an existing directory
		File rootDir = new File(root);
		if (!rootDir.isDirectory()) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		ByteStreamManifestCreator creator = new ByteStreamManifestCreator(root);
		new Thread(creator).start();
		CACHE.put(root, creator);
		try {
			return Response
					.created(
							UriBuilder.fromPath(
									"/manifests/"
											+ URLEncoder.encode(
													root.toASCIIString(),
													"UTF-8")).build())
					.status(Status.ACCEPTED).build();
		} catch (IllegalArgumentException excep) {
			throw new WebApplicationException(excep, Status.BAD_REQUEST);
		} catch (UriBuilderException excep) {
			throw new WebApplicationException(excep, Status.BAD_REQUEST);
		} catch (UnsupportedEncodingException excep) {
			throw new WebApplicationException(excep,
					Status.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * TODO JavaDoc for ByteStreamManifestCreator.</p> TODO Tests for
	 * ByteStreamManifestCreator.</p> TODO Implementation for
	 * ByteStreamManifestCreator.</p>
	 * 
	 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl
	 *         Wilson</a>.</p> <a
	 *         href="https://github.com/carlwilson">carlwilson AT
	 *         github</a>.</p>
	 * @version 0.1
	 * 
	 *          Created 26 Oct 2012:05:35:37
	 */
	public static class ByteStreamManifestCreator implements ProgressReporter,
			Runnable {
		private final URI location;
		private final EntryManifest entryManifest;
		private ByteStreamManifest byteManifest;
		private volatile long startMillis = 0L;
		private volatile long finishedMillis = 0L;
		private volatile long bytesProcessed = 0L;

		/**
		 * @param root
		 */
		public ByteStreamManifestCreator(URI location) {
			this.location = location;
			this.entryManifest = FileSystems
					.entryManifestFromDirectory(new File(location));
		}

		/**
		 * @return
		 */
		public ByteStreamManifest getByteManifest() {
			return this.byteManifest;
		}

		/**
		 * @return
		 */
		public EntryManifest getEntryManifest() {
			return this.entryManifest;
		}

		/**
		 * @return
		 */
		public URI getLocation() {
			return this.location;
		}

		@Override
		public long getProcessedCount() {
			return this.bytesProcessed;
		}

		@Override
		public long getTotalCount() {
			return this.entryManifest.getTotalSize();
		}

		@Override
		public double getProgress() {
			return Double.valueOf(this.getProcessedCount())
					/ Double.valueOf(this.getTotalCount());
		}

		@Override
		public String getUnits() {
			return "bytes";
		}

		@Override
		public long getEstimatedMillis() {
			long periodMillis = new Date().getTime() - this.startMillis;
			double avgMillis = Double.valueOf(periodMillis)
					/ this.getProcessedCount();
			return Math.round(avgMillis
					* (this.getTotalCount() - this.getProcessedCount()));
		}

		@Override
		public long getStartTime() {
			// TODO Auto-generated method stub
			return this.startMillis;
		}

		@Override
		public long getFinishTime() {
			// TODO Auto-generated method stub
			return this.finishedMillis;
		}

		@Override
		public void run() {
			this.startMillis = new Date().getTime();
			this.processDirectory();
			this.finishedMillis = new Date().getTime();
		}

		private void processDirectory() {
			File root = new File(this.location);
			String pathRoot = root.getAbsolutePath() + File.separator;
			// And the map for constructing the manifest plus a counter for
			// bytes
			// read
			Set<EntryTuple> identifiedEntries = new HashSet<EntryTuple>();
			// Iterate the entries
			for (Entry entry : this.entryManifest.getEntries()) {
				// Create a new file & a byte stream from it
				File file = new File(pathRoot + entry.getName());
				ByteStreamId byteStream;
				try {
					byteStream = ByteStreams.idFromFile(file);
					identifiedEntries.add(FileSystems.entryTupleFromValues(
							FileSystems.okEntryFromFile(entry.getName(), file),
							byteStream));
					this.bytesProcessed += byteStream.getLength();
				} catch (FileNotFoundException excep) {
					identifiedEntries.add(FileSystems.entryTupleFromValues(
							FileSystems.entryFromValues(entry.getName(),
									entry.getSize(), entry.getModifiedTime(),
									EntryStatus.LOST), ByteStreams
									.unidentifiedByteStreamId()));
				} catch (IOException excep) {
					identifiedEntries.add(FileSystems.entryTupleFromValues(
							FileSystems.entryFromValues(entry.getName(),
									entry.getSize(), entry.getModifiedTime(),
									EntryStatus.DAMAGED), ByteStreams
									.unidentifiedByteStreamId()));
				}
				// Add them to the map and add the bytes to the count
			}
			// and create the returned instance
			this.byteManifest = FileSystems.byteStreamManifestFromValues(
					this.entryManifest.getTotalSize(), this.bytesProcessed,
					identifiedEntries);
		}
	}
}

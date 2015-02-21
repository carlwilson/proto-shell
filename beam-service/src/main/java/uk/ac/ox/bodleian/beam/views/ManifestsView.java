/**
 * 
 */
package uk.ac.ox.bodleian.beam.views;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.ox.bodleian.beam.resources.ManifestsResource.ByteStreamManifestCreator;

import com.yammer.dropwizard.views.View;

/**
 * TODO JavaDoc for CollectionsView.</p>
 * TODO Tests for CollectionsView.</p>
 * TODO Implementation for CollectionsView.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 14 Oct 2012:22:12:00
 */

public final class ManifestsView extends View {
	private final Collection<ByteStreamManifestCreator> cache;

	private ManifestsView(final String templateName, final Collection<ByteStreamManifestCreator> cache) {
		super(templateName);
		this.cache = cache;
	}

	/**
	 * @param templateName
	 * @param cache 
	 * @return a new CollectionsView instance processing the passed template
	 */
	public static final ManifestsView getNewInstance(final String templateName, final Map<URI, ByteStreamManifestCreator> cache) {
		return new ManifestsView(templateName, cache.values());
	}

	/**
	 * @param templateName
	 * @param cache 
	 * @return a new CollectionsView instance processing the passed template
	 */
	public static final ManifestsView getNewInstance(final String templateName, final ByteStreamManifestCreator manifest) {
		Map<URI, ByteStreamManifestCreator> cache = new HashMap<URI, ByteStreamManifestCreator>(); 
		cache.put(manifest.getLocation(), manifest);
		return new ManifestsView(templateName, cache.values());
	}

	/**
	 * @return the list of collections
	 */
	public final Collection<ByteStreamManifestCreator> getCache() {
		return this.cache;
	}
	

	/**
	 * @return
	 */
	public final ByteStreamManifestCreator getManifest() {
		return this.cache.iterator().next();
	}
}

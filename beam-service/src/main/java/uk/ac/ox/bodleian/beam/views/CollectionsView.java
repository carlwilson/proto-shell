/**
 * 
 */
package uk.ac.ox.bodleian.beam.views;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.ac.ox.bodleian.beam.model.collection.BeamAccession;
import uk.ac.ox.bodleian.beam.model.collection.BeamCollection;

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

public final class CollectionsView extends View {
	private final List<BeamCollection> collections;

	private CollectionsView(final String templateName, final List<BeamCollection> collections) {
		super(templateName);
		this.collections = collections;
	}

	/**
	 * @param templateName
	 * @param collections 
	 * @return a new CollectionsView instance processing the passed template
	 */
	public static final CollectionsView getNewInstance(final String templateName, final List<BeamCollection> collections) {
		return new CollectionsView(templateName, collections);
	}


	/**
	 * @param templateName
	 * @param collection
	 * @return a new CollectionsView instance processing the passed template
	 */
	public static final CollectionsView getNewInstance(String templateName,
			BeamCollection collection) {
		List<BeamCollection> collections = new ArrayList<BeamCollection>();
		collections.add(collection);
		return new CollectionsView(templateName, collections);
	}

	/**
	 * @return the list of collections
	 */
	public final List<BeamCollection> getCollections() {
		return Collections.unmodifiableList(this.collections);
	}

	/**
	 * @return the first collection or the default if the list is empty
	 */
	public final BeamCollection getCollection() {
		return (this.collections.size() > 0) ? this.collections.get(0) : BeamCollection.getDefaultInstance();
		
	}
	
	/**
	 * @return
	 */
	public final List<BeamAccession> getNewAccessions() {
		try {
			return BeamCollection.findNewAccessions(this.getCollection());
		} catch (FileNotFoundException excep) {
			return BeamCollection.getDefaultInstance().getAccessions();
		}
	}
}

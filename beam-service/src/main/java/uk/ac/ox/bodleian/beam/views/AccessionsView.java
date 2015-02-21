/**
 * 
 */
package uk.ac.ox.bodleian.beam.views;

import java.util.List;

import uk.ac.ox.bodleian.beam.model.collection.BeamAccession;

import com.yammer.dropwizard.views.View;

/**
 * TODO JavaDoc for AcessionsView.</p>
 * TODO Tests for AcessionsView.</p>
 * TODO Implementation for AcessionsView.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 16 Oct 2012:09:17:35
 */

public class AccessionsView extends View {
	private final List<BeamAccession> accessions;

	protected AccessionsView(String templateName, List<BeamAccession> accessions) {
		super(templateName);
		this.accessions = accessions;
	}

	/**
	 * @param accessions
	 * @return the summary version of the view
	 */
	public static final AccessionsView getSummaryView(List<BeamAccession> accessions) {
		return new AccessionsView("acc-summ.ftl", accessions);
	}
	
	/**
	 * @return the list of accessions
	 */
	public List<BeamAccession> getAccessions() {
		return this.accessions;
	}
}

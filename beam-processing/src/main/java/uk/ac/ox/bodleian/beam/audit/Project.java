/**
 * 
 */
package uk.ac.ox.bodleian.beam.audit;


/**
 * TODO JavaDoc for Project
 * TODO Tests for Project
 * TODO Implementation for Project
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1
 * 
 * Created 27 Jul 2012:17:09:27
 */

public class Project {
	/** The project version */
	public static final String VERSION = Messages.getString("Project.version"); //$NON-NLS-1$
	/** The project name */
	public static final String NAME = Messages.getString("Project.name"); //$NON-NLS-1$
	/** The project Maven artifact */
	public static final String MAVEN_ARTIFACT_ID = Messages.getString("Project.artifact"); //$NON-NLS-1$
	private Project() {
		throw new AssertionError(Messages.getString("Project.defaut.constructor")); //$NON-NLS-1$
	}
}

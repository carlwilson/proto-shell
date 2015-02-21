package uk.ac.ox.bodleian.beam.audit;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * TODO JavaDoc for TestProject
 * TODO Tests for TestProject
 * TODO Implementation for TestProject
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1
 * 
 * Created 27 Jul 2012:17:53:31
 */
public class TestProject {

	/**
	 * 
	 */
	@Test
	public void testVersion() {
		assertFalse(Project.VERSION.equals("${version}"));
	}

	/**
	 * 
	 */
	@Test
	public void testName() {
		assertFalse(Project.NAME.equals("${name}"));
	}

	/**
	 * 
	 */
	@Test
	public void testMavenArtifactId() {
		assertFalse(Project.MAVEN_ARTIFACT_ID.equals("${artifact}"));
	}
}

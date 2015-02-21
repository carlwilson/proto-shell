package uk.ac.ox.bodleian.beam.audit;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * TODO JavaDoc for Messages.</p>
 * TODO Tests for Messages.</p>
 * TODO Implementation for Messages.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 6 Oct 2012:13:29:38
 */
public class Messages {
	private static final String BUNDLE_NAME = "uk.ac.ox.bodleian.beam.audit.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private Messages() {
	}

	/**
	 * @param key the key to return a message for
	 * @return the message associated with the key
	 */
	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}

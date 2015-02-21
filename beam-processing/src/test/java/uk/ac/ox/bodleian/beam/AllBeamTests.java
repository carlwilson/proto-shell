package uk.ac.ox.bodleian.beam;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author  <a href="mailto:carl.wilson@keepitdigital.eu">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1
 * 
 * Created 29 Jun 2012:14:17:07
 */
@RunWith(Suite.class)
@SuiteClasses({})
public class AllBeamTests {
    /** Base directory for all test resources */
    final public static String TEST_BASE = "src/test/resources/";
    /** Base directory for test data */
    final public static String TEST_DATA_BASE = TEST_BASE + "uk/ac/ox/bodleian/test/";
}

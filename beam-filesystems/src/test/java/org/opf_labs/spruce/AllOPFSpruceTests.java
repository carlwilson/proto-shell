package org.opf_labs.spruce;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.opf_labs.spruce.bytestream.AllByteStreamTests;
import org.opf_labs.spruce.filesystem.AllFileSystemTests;

/**
 * Test Suite for all OPF SPRUCE tests
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 3 Oct 2012:10:00:30
 */
@RunWith(Suite.class)
@SuiteClasses({ AllFileSystemTests.class, AllByteStreamTests.class })
public class AllOPFSpruceTests {
	// Just a runner, intentionally blank
}

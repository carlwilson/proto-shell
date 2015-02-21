/**
 * 
 */
package org.opf_labs.spruce.utils;

import static org.junit.Assert.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Test;
import org.opf_labs.spruce.utils.Environments;


/**
 * TODO JavaDoc for TestEnvironments
 * TODO Tests for TestEnvironments
 * TODO Implementation for TestEnvironments
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1
 * 
 * Created 27 Jul 2012:13:21:26
 */

public class TestEnvironments {

	/**
	 * Just compare to the property for now
	 * Test method for {@link org.opf_labs.spruce.utils.Environments#getHostName()}.
	 */
	@Test
	public void testGetHostName() {
		try {
			assertEquals("Host name inconsistent.", Environments.getHostName(), InetAddress.getLocalHost().getHostName());
			assertFalse("Environments.getHostName().isEmpty() == true", Environments.getHostName().isEmpty());
		} catch (UnknownHostException excep) {
			excep.printStackTrace();
			fail("Illegal length IP address, check your setup: " + excep.getMessage());
			throw new IllegalStateException();
		}
	}

	/**
	 * Test method for {@link org.opf_labs.spruce.utils.Environments#getCPUIsa()}.
	 */
	@Test
	public void testGetCPUIsa() {
		assertEquals("Inconsistent CPU Details", System.getProperty(Environments.JAVA_CPU_ISA_PROP), Environments.getCPUIsa());
	}

	/**
	 * Test method for {@link org.opf_labs.spruce.utils.Environments#getHostSummary()}.
	 */
	@Test
	public void testGetHostSummary() {
		assertFalse("HostSummary().isEmpty() == true", Environments.getHostSummary().isEmpty());
	}

	/**
	 * Test method for {@link org.opf_labs.spruce.utils.Environments#getJavaArch()}.
	 */
	@Test
	public void testGetJavaArch() {
		assertEquals("Inconsistent Java Arch", "x" + System.getProperty(Environments.JAVA_ARCH_PROP), Environments.getJavaArch());
		assertFalse("Environments.getJavaArch().isEmpty() == true", Environments.getJavaArch().isEmpty());
	}

	/**
	 * Test method for {@link org.opf_labs.spruce.utils.Environments#getJavaVendor()}.
	 */
	@Test
	public void testGetJavaVendor() {
		assertEquals("Inconsistent Java Vendor", System.getProperty(Environments.JAVA_VM_VENDOR_PROP), Environments.getJavaVendor());
		assertFalse("Environments.getJavaVendor().isEmpty() == true", Environments.getJavaVendor().isEmpty());
	}

	/**
	 * Test method for {@link org.opf_labs.spruce.utils.Environments#getJavaVersion()}.
	 */
	@Test
	public void testGetJavaVersion() {
		assertEquals("Inconsistent Java Version", System.getProperty(Environments.JAVA_VERSION_PROP), Environments.getJavaVersion());
		assertFalse("Environments.getJavaVersion().isEmpty() == true", Environments.getJavaVersion().isEmpty());
	}

	/**
	 * Test method for {@link org.opf_labs.spruce.utils.Environments#getJavaHome()}.
	 */
	@Test
	public void testGetJavaHome() {
		assertEquals("Inconsistent Java Home", System.getProperty(Environments.JAVA_HOME_PROP), Environments.getJavaHome());
		assertFalse("Environments.getJavaHome().isEmpty() == true", Environments.getJavaHome().isEmpty());
	}

	/**
	 * Test method for {@link org.opf_labs.spruce.utils.Environments#getJavaSummary()}.
	 */
	@Test
	public void testGetJavaSummary() {
		assertFalse("getJavaSummary().isEmpty() == true", Environments.getJavaSummary().isEmpty());
	}

	/**
	 * Test method for {@link org.opf_labs.spruce.utils.Environments#getUserName()}.
	 */
	@Test
	public void testGetUserName() {
		assertEquals("Inconsistent User Name", System.getProperty(Environments.USER_NAME_PROP), Environments.getUserName());
		assertFalse("Environments.getUserName().isEmpty() == true", Environments.getUserName().isEmpty());
	}

	/**
	 * Test method for {@link org.opf_labs.spruce.utils.Environments#getUserHome()}.
	 */
	@Test
	public void testGetUserHome() {
		assertEquals("Inconsistent User home", System.getProperty(Environments.USER_HOME_PROP), Environments.getUserHome());
		assertFalse("Environments.getUserHome().isEmpty() == true", Environments.getUserHome().isEmpty());
	}

	/**
	 * Test method for {@link org.opf_labs.spruce.utils.Environments#getUserCountry()}.
	 */
	@Test
	public void testGetUserCountry() {
		assertEquals("Inconsistent User country", System.getProperty(Environments.USER_COUNTRY_PROP), Environments.getUserCountry());
		assertFalse("Environments.getUserCountry().isEmpty() == true", Environments.getUserCountry().isEmpty());
	}

	/**
	 * Test method for {@link org.opf_labs.spruce.utils.Environments#getUserLanguage()}.
	 */
	@Test
	public void testGetUserLanguage() {
		assertEquals("Inconsistent User lang", System.getProperty(Environments.USER_LANGUAGE_PROP), Environments.getUserLanguage());
		assertFalse("Environments.getUserLanguage().isEmpty() == true", Environments.getUserLanguage().isEmpty());
	}

	/**
	 * Test method for {@link org.opf_labs.spruce.utils.Environments#getUserSummary()}.
	 */
	@Test
	public void testGetUserSummary() {
		assertFalse("getUserSummary().isEmpty() == true", Environments.getUserSummary().isEmpty());
	}

	/**
	 * Test method for {@link org.opf_labs.spruce.utils.Environments#getOSName()}.
	 */
	@Test
	public void testGetOSName() {
		assertEquals("Inconsistent OS Name", System.getProperty(Environments.OS_NAME_PROP), Environments.getOSName());
		assertFalse("Environments.getOSName().isEmpty() == true", Environments.getOSName().isEmpty());
	}

	/**
	 * Test method for {@link org.opf_labs.spruce.utils.Environments#getOSVersion()}.
	 */
	@Test
	public void testGetOSVersion() {
		assertEquals("Inconsistent OS Version", System.getProperty(Environments.OS_VERSION_PROP), Environments.getOSVersion());
		assertFalse("Environments.getOSVersion().isEmpty() == true", Environments.getOSVersion().isEmpty());
	}

	/**
	 * Test method for {@link org.opf_labs.spruce.utils.Environments#getOSArch()}.
	 */
	@Test
	public void testGetOSArch() {
		assertEquals("Inconsistent OS Arch", System.getProperty(Environments.OS_ARCH_PROP), Environments.getOSArch());
		assertFalse("Environments.getOSArch().isEmpty() == true", Environments.getOSArch().isEmpty());
	}

	/**
	 * Test method for {@link org.opf_labs.spruce.utils.Environments#getOSSummary()}.
	 */
	@Test
	public void testGetOSSummary() {
		assertFalse("getOSSummary().isEmpty() == true", Environments.getOSSummary().isEmpty());
	}
}

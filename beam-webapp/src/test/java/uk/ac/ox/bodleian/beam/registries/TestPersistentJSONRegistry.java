/**
 * 
 */
package uk.ac.ox.bodleian.beam.registries;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.ox.bodleian.beam.AllWebAppTests;
import uk.ac.ox.bodleian.beam.filesystem.bytestream.BeamByteStream;
import uk.ac.ox.bodleian.beam.registries.Registry.ItemNotFoundException;

/**
 * TODO JavaDoc for TestMapBackedRegistry.</p>
 * TODO Tests for TestMapBackedRegistry.</p>
 * TODO Implementation for TestMapBackedRegistry.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 24 Sep 2012:10:36:54
 */

public class TestPersistentJSONRegistry {
	private static final String NAME = "bytestream";
	private static File ROOT;
	static {
		try {
			ROOT = AllWebAppTests.getRegistryRoot();
		} catch (URISyntaxException excep) {
			excep.printStackTrace();
			throw new IllegalStateException("Can't get test resources.", excep);
		}
	}
	private static PersistentJSONRegistry<BeamByteStream> REGISTRY;

	/**
	 * 
	 */
	@BeforeClass
	@AfterClass
	public static void cleanRoot() {
		// loop through all file in directory
		for (File file : ROOT.listFiles()) {
			// if an unhidden file
			if (file.isFile() && !file.isHidden()) {
				file.delete();
			}
		}

	}
	
	private static void initialiseRegistry() {
		try {
			REGISTRY = PersistentJSONRegistry.createRegistry(ROOT, NAME, BeamByteStream.class);
			for (File file : AllWebAppTests.getRawFiles()) {
				BeamByteStream item = AllWebAppTests.createByteStreamUsingCommonsCodec(file);
				REGISTRY.register(item);
			}
		} catch (IOException excep) {
			excep.printStackTrace();
			throw new IllegalStateException("Can't read test file.", excep);
		}
	}
	
	/**
	 * 
	 */
	@Before
	public void setUp() {
		initialiseRegistry();
	}
	
	/**
	 * 
	 */
	@After
	public void tearDown() {
		cleanRoot();
	}
	
	/**
	 * Test method for {@link uk.ac.ox.bodleian.beam.registries.Registry#getName()}.
	 */
	@Test
	public final void testGetName() {
		// Create registry with name and assert name is correct
		assertEquals(NAME, REGISTRY.getName());
	}

	/**
	 * Test method for {@link uk.ac.ox.bodleian.beam.registries.Registry#retrieve(java.lang.String)}.
	 * @throws URISyntaxException 
	 * @throws IOException 
	 * @throws ItemNotFoundException 
	 */
	@Test
	public final void testRetrieve() throws IOException, URISyntaxException, ItemNotFoundException {
		// OK loop through the items
		for (File file : AllWebAppTests.getRawFiles()) {
			BeamByteStream item = AllWebAppTests.createByteStreamUsingCommonsCodec(file);
			BeamByteStream retItem = REGISTRY.retrieve(item.getId());
			assertEquals(retItem, item);
		}
	}

	/**
	 * Test method for {@link uk.ac.ox.bodleian.beam.registries.Registry#retrieveAll()}.
	 * @throws IOException 
	 */
	@Test
	public final void testRetrieveAll() throws IOException {
		// Create registry
		Set<BeamByteStream> allItems = REGISTRY.retrieveAll();
		for (File file : AllWebAppTests.getRawFiles()) {
			BeamByteStream item = AllWebAppTests.createByteStreamUsingCommonsCodec(file);
			assertTrue(allItems.contains(item));
		}
	}

	/**
	 * Test method for {@link uk.ac.ox.bodleian.beam.registries.Registry#remove(Object)}.
	 * @throws IOException 
	 * @throws URISyntaxException 
	 * @throws ItemNotFoundException 
	 */
	@Test(expected=ItemNotFoundException.class)
	public final void testRemove() throws IOException, URISyntaxException, ItemNotFoundException {
		// Add the empty file to the registry
		BeamByteStream item = AllWebAppTests.createByteStreamUsingCommonsCodec(AllWebAppTests.getEmptyFile());
		REGISTRY.register(item);
		// Make sure it's there
		BeamByteStream retItem;
		try {
			retItem = REGISTRY.retrieve(item.getId());
			assertEquals(item, retItem);
		} catch (ItemNotFoundException excep) {
			fail("Item should be in registry.");
		}
		REGISTRY.remove(item);
		REGISTRY.retrieve(item.getId());
	}

	/**
	 * Test method for {@link uk.ac.ox.bodleian.beam.registries.Registry#clear()}.
	 * @throws IOException 
	 */
	@Test
	public final void testClear() throws IOException {
		assertTrue(REGISTRY.count() > 0);
		REGISTRY.clear();
		assertTrue("count " + REGISTRY.count() + " != 0", REGISTRY.count() == 0);
	}

}

/**
 * 
 */
package uk.ac.ox.bodleian.beam.registries;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

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

public class TestMapBackedRegistry {
	private static final String NAME = "bytestream";
	/**
	 * Test method for {@link uk.ac.ox.bodleian.beam.registries.Registry#getName()}.
	 */
	@Test
	public final void testGetName() {
		// Create registry with name and assert name is correct
		CacheRegistry<BeamByteStream> reg = CacheRegistry.createCacheRegistry(NAME);
		assertEquals(NAME, reg.getName());
	}

	/**
	 * Test method for {@link uk.ac.ox.bodleian.beam.registries.Registry#retrieve(java.lang.String)}.
	 * @throws URISyntaxException 
	 * @throws IOException 
	 * @throws ItemNotFoundException 
	 */
	@Test
	public final void testRetrieve() throws IOException, URISyntaxException, ItemNotFoundException {
		// Create registry
		CacheRegistry<BeamByteStream> reg = CacheRegistry.createCacheRegistry(NAME);
		// Insert item and check count is 1
		BeamByteStream item = AllWebAppTests.createByteStreamUsingCommonsCodec(AllWebAppTests.getEmptyFile());
		reg.register(item);
		BeamByteStream retItem = reg.retrieve(item.getId());
		assertEquals(retItem, item);
	}

	/**
	 * Test method for {@link uk.ac.ox.bodleian.beam.registries.Registry#retrieveAll()}.
	 * @throws IOException 
	 */
	@Test
	public final void testRetrieveAll() throws IOException {
		// Create registry
		CacheRegistry<BeamByteStream> reg = CacheRegistry.createCacheRegistry(NAME);
		for (File file : AllWebAppTests.getRawFiles()) {
			BeamByteStream item = AllWebAppTests.createByteStreamUsingCommonsCodec(file);
			reg.register(item);
		}
		assertEquals(AllWebAppTests.getRawFiles().size(), reg.count());
		Set<BeamByteStream> allItems = reg.retrieveAll();
		for (File file : AllWebAppTests.getRawFiles()) {
			BeamByteStream item = AllWebAppTests.createByteStreamUsingCommonsCodec(file);
			assertTrue(allItems.contains(item));
		}
	}

	/**
	 * Test method for {@link uk.ac.ox.bodleian.beam.registries.Registry#register(Object)}.
	 * @throws IOException 
	 */
	@Test
	public final void testRegister() throws IOException {
		// Create registry
		CacheRegistry<BeamByteStream> reg = CacheRegistry.createCacheRegistry(NAME);
		for (File file : AllWebAppTests.getRawFiles()) {
			BeamByteStream frmStrm = AllWebAppTests.createByteStreamUsingCommonsCodec(file);
			BeamByteStream frmReg = reg.register(frmStrm);
			assertEquals(frmReg, frmStrm);
		}
	}

	/**
	 * Test method for {@link uk.ac.ox.bodleian.beam.registries.Registry#count()}.
	 * @throws URISyntaxException 
	 * @throws IOException 
	 */
	@Test
	public final void testCount() throws IOException, URISyntaxException {
		// Create registry and check count is zero
		CacheRegistry<BeamByteStream> reg = CacheRegistry.createCacheRegistry(NAME);
		assertTrue(reg.count() == 0);
		// Insert item and check count is 1
		BeamByteStream item = AllWebAppTests.createByteStreamUsingCommonsCodec(AllWebAppTests.getEmptyFile());
		reg.register(item);
		assertTrue(reg.count() == 1);
	}

	/**
	 * Test method for {@link uk.ac.ox.bodleian.beam.registries.Registry#remove(Object)}.
	 * @throws IOException 
	 */
	@Test
	public final void testRemove() throws IOException {
		// Create registry
		CacheRegistry<BeamByteStream> reg = CacheRegistry.createCacheRegistry(NAME);
		for (File file : AllWebAppTests.getRawFiles()) {
			BeamByteStream item = AllWebAppTests.createByteStreamUsingCommonsCodec(file);
			reg.register(item);
			BeamByteStream prevItem = reg.remove(item);
			assertEquals(item, prevItem);
			prevItem = reg.remove(item);
			assertFalse(item.equals(prevItem));
		}
	}

	/**
	 * Test method for {@link uk.ac.ox.bodleian.beam.registries.Registry#clear()}.
	 * @throws IOException 
	 */
	@Test
	public final void testClear() throws IOException {
		CacheRegistry<BeamByteStream> reg = CacheRegistry.createCacheRegistry(NAME);
		for (File file : AllWebAppTests.getRawFiles()) {
			BeamByteStream item = AllWebAppTests.createByteStreamUsingCommonsCodec(file);
			reg.register(item);
		}
		assertFalse(reg.count() == 0);
		reg.clear();
		assertTrue(reg.count() == 0);
	}

}

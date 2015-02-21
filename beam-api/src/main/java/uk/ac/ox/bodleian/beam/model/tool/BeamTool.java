/**
 * 
 */
package uk.ac.ox.bodleian.beam.model.tool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.common.base.Preconditions;

/**
 * TODO JavaDoc for BeamTool.</p>
 * TODO Tests for BeamTool.</p>
 * TODO Implementation for BeamTool.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 22 Oct 2012:21:30:03
 */

public final class BeamTool {
	private final static BeamTool DEFAULT = new BeamTool();
	private final String id;
	private final String name;
	private final String version;
	private final List<String> resources;
	
	/**
	 * @return the default instance
	 */
	public final static BeamTool getDefaultInstance() {return DEFAULT;}

	/**
	 * @param name
	 * @param version
	 * @return a new instance from the passed values
	 */
	public final static BeamTool newInstance(final String name, final String version) {
		Preconditions.checkNotNull(name, "name == null");
		Preconditions.checkNotNull(version, "version == null");
		return new BeamTool(name, version, new ArrayList<String>());
	}

	/**
	 * @param name
	 * @param version
	 * @param resources
	 * @return a new instance from the passed values
	 */
	public final static BeamTool newInstance(final String name, final String version, List<String> resources) {
		Preconditions.checkNotNull(name, "name == null");
		Preconditions.checkNotNull(version, "version == null");
		Preconditions.checkNotNull(resources, "resources == null");
		return new BeamTool(name, version, resources);
	}

	/**
	 * @param name
	 * @param version
	 * @param resources
	 * @return a new instance from the passed values
	 */
	final static BeamTool newInstance(final String id, final String name, final String version, List<String> resources) {
		Preconditions.checkNotNull(name, "name == null");
		Preconditions.checkNotNull(version, "version == null");
		Preconditions.checkNotNull(resources, "resources == null");
		return new BeamTool(name, version, resources);
	}

	private BeamTool() {
		this("name", "0", new ArrayList<String>());
	}

	private BeamTool(final String name, final String version, List<String> resources) {
		assert(name != null);
		assert(!name.isEmpty());
		this.name = name;
		assert(version != null);
		assert(!version.isEmpty());
		this.version = version;
		assert(resources != null);
		this.resources = Collections.unmodifiableList(resources);
		this.id = getId(name, version, resources);
	}

	private static String getId(final String name, final String version, List<String> resources) {
		StringBuilder builder = new StringBuilder(name);
		builder.append(version);
		builder.append(resources);
		return DigestUtils.md5Hex(builder.toString());
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("id:" + this.id);
		builder.append(",name:" + this.name);
		builder.append(",version:" + this.version);
		builder.append(",resources" + this.resources);
		return builder.toString();
	}

	/**
	 * Dummy tester main to create an example instance, takes args but faciley falls back to defaults
	 * @param args array of strings interpreted as name, version, resources....
	 */
	public static void main(String[] args) {
		String name = (args.length > 0) ? args[0] : "toolName";
		String version = (args.length > 1) ? args[1] : "0.0.0";
		List<String> resources = new ArrayList<String>();
		if (args.length > 2) {
			for (int index = 2; index < args.length; index++) {
				resources.add(args[index]);
			}
		} else {
			resources.add("ResourceName:vers");
		}
		BeamTool tool = new BeamTool(name, version, resources);
		System.out.println(tool.toString());
	}
}

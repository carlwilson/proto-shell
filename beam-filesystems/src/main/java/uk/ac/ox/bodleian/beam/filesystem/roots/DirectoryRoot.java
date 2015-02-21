/**
 * 
 */
package uk.ac.ox.bodleian.beam.filesystem.roots;

import java.io.File;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * TODO JavaDoc for DirectoryRoot.</p>
 * TODO Tests for DirectoryRoot.</p>
 * TODO Implementation for DirectoryRoot.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 23 Sep 2012:22:34:30
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DirectoryRoot {
	private final String host;
	@XmlElement(name="path")
	private final File root;
	@SuppressWarnings("unused")
	private DirectoryRoot() {
		throw new AssertionError("In DirectoryRoot no arg constructor.");
	}
	
	DirectoryRoot(String host, File root) {
		this.root = root;
		this.host = host;
	}
	
	/**
	 * @return the path for the root
	 */
	public String getHost() {return this.host;}

	/**
	 * @return the path for the root
	 */
	public File getRoot() {return this.root;}

	/**
	 * @param host 
	 * @param root
	 * @return a new Directorr Root instance
	 */
	public static final DirectoryRoot getInstance(String host, File root) {
		if (root == null) throw new IllegalArgumentException("root == null");
		if (!root.isDirectory()) throw new IllegalArgumentException("!root.isDirectory()");
		return new DirectoryRoot(host, root);
	}
}

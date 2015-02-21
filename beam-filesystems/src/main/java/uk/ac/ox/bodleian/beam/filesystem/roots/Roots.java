package uk.ac.ox.bodleian.beam.filesystem.roots;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.opf_labs.spruce.utils.Environments;

/**
 * TODO JavaDoc for Roots.</p> TODO Tests for Roots.</p> TODO Implementation for
 * Roots.</p>
 * 
 * @author <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 *          Created 23 Sep 2012:23:13:43
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Roots {
	private static final Roots INSTANCE = new Roots();
	/**
	 * A set of the machines directory roots
	 */
	@XmlElement(name="root")
	private final Set<DirectoryRoot> roots;

	private Roots() {
		Set<DirectoryRoot> rootsTmp = new HashSet<DirectoryRoot>();
		for (File root : File.listRoots()) {
			rootsTmp.add(new DirectoryRoot(Environments.getHostName(), root));
		}
		this.roots = Collections.unmodifiableSet(rootsTmp);
	}

	/**
	 * @return the set of root directories
	 */
	public Set<DirectoryRoot> getRoots() {
		return this.roots;
	}

	/**
	 * @return the static instance
	 */
	public static Roots getInstance() {
		return INSTANCE;
	}
}

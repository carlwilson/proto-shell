/**
 * 
 */
package uk.ac.ox.bodleian.beam.filesystem.bytestream;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import uk.ac.ox.bodleian.beam.registries.Identifiable;

/**
 * TODO JavaDoc for BeamByteStream.</p>
 * TODO Tests for BeamByteStream.</p>
 * TODO Implementation for BeamByteStream.</p>
 * 
 * @author  <a href="mailto:carl@openplanetsfoundation.org">Carl Wilson</a>.</p>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>.</p>
 * @version 0.1
 * 
 * Created 24 Sep 2012:10:44:30
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BeamByteStream extends ByteStreamIdImpl implements Identifiable {
	private static final long serialVersionUID = -6322523502087241408L;
	BeamByteStream() {
		super();
	}
	
	BeamByteStream(final long length,
			final String sha256, final String md5) {
		super(length, sha256, md5);
	}
	
	/**
	 * @param length
	 * @param sha256
	 * @param md5 
	 * @return a byte stream constructed from the values
	 */
	public static final BeamByteStream beamBsFromValues(final long length,
			final String sha256, final String md5) {
		return new BeamByteStream(length, sha256, md5);
	}
	/**
	 * @see uk.ac.ox.bodleian.beam.registries.Identifiable#getId()
	 */
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return this.getHexSHA256();
	}
}

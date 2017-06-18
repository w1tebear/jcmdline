package jcmdline;

import jcmdline.AbstractParameter;
import jcmdline.CmdLineException;

/**
 * A Parameter class that accepts string parameters of a specified len.
 */
public class SampleParam extends AbstractParameter<String> {
	private int len;

	public SampleParam(String tag, String desc, int length) {
		setTag(tag);
		setDesc(desc);
		this.len = length;
		setOptionLabel("<s>");
	}

	/**
	 * @see jcmdline.AbstractParameter#convertValue(java.lang.String)
	 */
	@Override
	public String convertValue(String strVal) throws CmdLineException {
		return strVal;
	}

	public void validateValue(String val) throws CmdLineException {
		super.validateValue(val);
		if (val.length() != len) {
			throw new CmdLineException(getTag() + " must be a string of " + len
					+ " characters in len");
		}

	}
}

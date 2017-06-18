/*
 * BooleanParam.java
 *
 * Classes:
 *   public   BooleanParam
 *   
 * ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is the Java jcmdline (command line management) package.
 *
 * The Initial Developer of the Original Code is Lynne Lawrence.
 * 
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):  Lynne Lawrence <lynneglawrence02@gmail.com>
 *
 * ***** END LICENSE BLOCK *****
 */

package jcmdline;

/**
 * Encapsulate a boolean command line parameter. This parameter defaults to
 * "false" if not set by the user.
 * <p>
 * Sample usage:
 * 
 * <pre>
 *    BooleanParam deleteOpt =
 *        new BooleanParam(&quot;delete&quot;, &quot;delete original file&quot;);
 *    FileParam outfileOpt =
 *        new FileParam(&quot;outfile&quot;, &quot;the outfile file - defaults to stdout&quot;,
 *                      FileParam.DOESNT_EXIST);
 *    FileParam infileArg =
 *        new FileParam(&quot;infile&quot;, &quot;the input file - defaults to stdin&quot;,
 *                      FileParam.IS_READABLE &amp; FileParam.IS_FILE);
 *    CmdLineHandler clh = new DefaultCmdLineHandler(
 *        &quot;filter&quot;,
 *        &quot;filters files for obscenities&quot;,
 *        new Parameter[] { deleteOpt, outfileOpt },
 *        new Parameter[] { infileArg }
 *    );
 *    clh.parse(args);
 *    if (deleteOpt.isTrue()) {
 *        ....
 *    }
 * </pre>
 * 
 * @author Lynne Lawrence
 * @version jcmdline Rel. @VERSION@ $Id: BooleanParam.java,v 1.2 2002/12/07
 *          14:22:06 lglawrence Exp $
 * @see CmdLineParser
 */
public class BooleanParam extends AbstractParameter<Boolean> implements
		OptionTakesNoValue {

	/**
	 * constructor - creates a public boolean parameter
	 * 
	 * @param tag
	 *            a unique identifier for this parameter
	 * @param desc
	 *            a description of the parameter, suitable for display in a
	 *            usage statement
	 * @throws IllegalArgumentException
	 *             if any specified parameter is invalid.
	 * @see AbstractParameter#setTag(String) setTag()
	 * @see AbstractParameter#setDesc(String) setDesc()
	 */
	public BooleanParam(String tag, String desc) {
		this(tag, desc, PUBLIC);
	}

	/**
	 * constructor - creates a boolean parameter that is public or hidden, as
	 * specified
	 * 
	 * @param tag
	 *            a unique identifier for this parameter
	 * @param desc
	 *            a description of the parameter, suitable for display in a
	 *            usage statement
	 * @param hidden
	 *            {@link Parameter#HIDDEN HIDDEN} if parameter is not to be
	 *            listed in the usage, {@link Parameter#PUBLIC PUBLIC}
	 *            otherwise.
	 * @throws IllegalArgumentException
	 *             if any specified parameter is invalid.
	 * @see AbstractParameter#setTag(String) setTag()
	 * @see AbstractParameter#setDesc(String) setDesc()
	 * @see Parameter#HIDDEN HIDDEN
	 * @see Parameter#PUBLIC PUBLIC
	 */
	public BooleanParam(String tag, String desc, boolean hidden) {
		this.setTag(tag);
		this.setDesc(desc);
		this.setOptional(optional);
		this.setMultiValued(SINGLE_VALUED);
		this.setHidden(hidden);
		this.setOptionLabel("");
		// these should only come into play if the value is being set
		// progammatically because this Parameter implements OptionTakesNoValue
		this.setAcceptableValues(new Boolean[] { Boolean.TRUE, Boolean.FALSE });
		try {
			this.setValue(Boolean.FALSE); // defaults to false
			this.set = false;
		} catch (Exception e) {
			// Should never get here
			throw new RuntimeException(Strings
					.get("BooleanParam.setValueProgError"));
		}
	}
	
	/**
	 * Replaces any current value with that specified.
	 * @see jcmdline.AbstractParameter#addValue(java.lang.Object)
	 */
	@Override
	public void addValue(Boolean value) throws CmdLineException {
		values.clear();
		super.addValue(value);
	}

	/**
	 * @see jcmdline.AbstractParameter#convertValue(java.lang.String)
	 */
	public Boolean convertValue(String strVal) throws CmdLineException {
		Boolean retval = Boolean.FALSE;
		if (strVal.equals(Strings.get("BooleanParam.true"))
				|| strVal.equals(Strings.get("BooleanParam.yes"))) {
			retval = Boolean.TRUE;
		} else if (!(strVal.equals(Strings.get("BooleanParam.false")) || strVal
				.equals(Strings.get("BooleanParam.no")))) {
			String validValues = Strings.join("\n    ", new String[] {
					Strings.get("BooleanParam.true"),
					Strings.get("BooleanParam.yes"),
					Strings.get("BooleanParam.false"),
					Strings.get("BooleanParam.no") });
			throw new CmdLineException(Strings.get(
					"BooleanParameter.conversionErr", new Object[] { strVal,
							tag, validValues }));
		}
		return retval;
	}

	/**
	 * Gets the default value of this Parameter when used as a command line
	 * option, and specified just by its tag.
	 * 
	 * @return the String value of {@link Boolean#TRUE}
	 * @see OptionTakesNoValue
	 */
	public String getDefaultValue() {
		return Boolean.TRUE.toString();
	}

	/**
	 * Returns the value of the parameter as a boolean.
	 * 
	 * @return the parameter value as a boolean
	 */
	public boolean isTrue() {
		if (isSet()) {
			return values.get(0);
		} else {
			return false;
		}
	}

}

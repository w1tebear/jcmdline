/*
 * AbstractParameter.java
 *
 * Classes:
 *   public   AbstractParameter
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Base class for command line parameters.
 * <P>
 * To implement a concrete Parameter class by subclassing this class, the
 * following should be done:
 * <ul>
 * <li>Implement a {@link #convertValue(String)} method.</li>
 * <li>If the {@link #validateValue(Object)} is not adequate (see the javadoc
 * for this method), override it, perhaps calling super.validateValue() to also
 * invoke the functionality of this class.</li>
 * <li>Implement constructors applicable for the new type of Parameter.
 * <li>Call {@link #setOptionLabel(String) setOptionLabel()} from the
 * constructors to set a reasonable option label for the new type of parameter.
 * </ul>
 * <p>
 * A simple Parameter class that accepts only strings of a specified length
 * might look as follows:
 * 
 * <pre>
 * public class FixedLenParam extends AbstractParameter&lt;String&gt; {
 * 
 * 	private int length;
 * 
 * 	public FixedLenParam(String tag, String desc, int length) {
 * 		setTag(tag);
 * 		setDesc(desc);
 * 		this.length = length;
 * 		setOptionLabel(&quot;&lt;s&gt;&quot;);
 * 	}
 * 
 * 	public String convertValue(String strVal) {
 * 		return strVal;
 * 	}
 * 
 * 	public void validateValue(String val) throws CmdLineException {
 * 		super.validateValue(val); // check acceptable values, etc..
 * 		if (val.length() != length) {
 * 			throw new CmdLineException(getTag() + &quot; must be a string of &quot;
 * 					+ length + &quot; characters in length&quot;);
 * 		}
 * 
 * 	}
 * }
 * </pre>
 * 
 * @author Lynne Lawrence
 * @version $Id: AbstractParameter.java,v 1.4 2009/08/07 16:13:28 lglawrence Exp $
 */
public abstract class AbstractParameter<T> implements Parameter<T> {

	/**
	 * a set of restricted values the Parameter may take
	 * 
	 * @see #setAcceptableValues(Collection) setAcceptableValues()
	 * @see #getAcceptableValues()
	 */
	protected List<T> acceptableValues;

	/**
	 * a description of the parameter to be displayed in the usage
	 */
	protected String desc;

	/**
	 * indicates that the parameter is hidden and will not be displayed in the
	 * normal usage - default is <code>false</code>
	 */
	protected boolean hidden = false;

	/**
	 * During parse, ignore missing required Parameters if this Parameter is
	 * set. Typically used by Parameters that cause an action then call
	 * System.exit(), like "-help".
	 * 
	 * @see #setIgnoreRequired(boolean) setIgnoreRequired()
	 * @see #getIgnoreRequired()
	 */
	protected boolean ignoreRequired;

	/**
	 * Indicates whether the parameter can have multiple values. The default is
	 * false, indicating that the parameter can only accept a single value.
	 */
	protected boolean multiValued = false;

	/**
	 * Indicates whether or not the parameter is optional. The default is
	 * <code>true</code>, indicating that the parameter is optional.
	 */
	protected boolean optional = true;

	/**
	 * The label that should be used for a Parameter option's value in the usage
	 * 
	 * @see #setOptionLabel(String) setOptionLabel()
	 * @see #getOptionLabel()
	 */
	protected String optionLabel = null;

	/**
	 * indicates that the value of the parameter has been set
	 */
	protected boolean set;

	/**
	 * the tag which uniquely identifies the parameter, and will be used to
	 * identify the parameter on the command line if the parameter is used as an
	 * option
	 */
	protected String tag;

	/**
	 * the value(s) of the entity
	 */
	protected ArrayList<T> values = new ArrayList<T>();

	/**
	 * Add a value to this Parameter. This implementation calls
	 * {@link #convertValue(String)} to convert the String, then
	 * {@link #addValue(Object)}.
	 * 
	 * @see jcmdline.Parameter#addStringValue(java.lang.String)
	 */
	public void addStringValue(String value) throws CmdLineException {
		T obj = convertValue(value);
		addValue(obj);
	}

	/**
	 * Converts a String value to the type associated with the Parameter. All
	 * non-abstract subclasses must implement this method.
	 * 
	 * @param strVal
	 *            the String value of the Parameter
	 * @return the parameter value converted to the Object type with which the
	 *         Parameter is associated
	 * @throws CmdLineException
	 *             if the conversion cannot be made
	 */
	public abstract T convertValue(String strVal) throws CmdLineException;

	/**
	 * @see jcmdline.Parameter#addValue(Object)
	 */
	public void addValue(T value) throws CmdLineException {
		if (values.size() >= 1 && !multiValued) {
			throw new CmdLineException(Strings.get(
					"AbstractParameter.specifiedMoreThanOnce",
					new Object[] { tag }));
		}
		validateValue(value); // throws CmdLineException
		values.add(value);
		set = true;
	}

	/**
	 * @see jcmdline.Parameter#getAcceptableValues()
	 */
	public List<T> getAcceptableValues() {
		return acceptableValues;
	}

	/**
	 * @see jcmdline.Parameter#getDesc()
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @see jcmdline.Parameter#getIgnoreRequired()
	 */
	public boolean getIgnoreRequired() {
		return ignoreRequired;
	}

	/**
	 * @see jcmdline.Parameter#getOptionLabel()
	 */
	public String getOptionLabel() {
		return ((optionLabel == null) ? "" : optionLabel);
	}

	/**
	 * @see jcmdline.Parameter#getTag()
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * @see jcmdline.Parameter#getValue()
	 */
	public T getValue() {
		if (values.size() == 0) {
			return null;
		}
		return values.get(0);
	}

	/**
	 * @see jcmdline.Parameter#getValues()
	 */
	public List<T> getValues() {
		return values;
	}

	/**
	 * @see jcmdline.Parameter#isHidden()
	 */
	public boolean isHidden() {
		return hidden;
	}

	/**
	 * @see jcmdline.Parameter#isMultiValued()
	 */
	public boolean isMultiValued() {
		return multiValued;
	}

	/**
	 * @see jcmdline.Parameter#isOptional()
	 */
	public boolean isOptional() {
		return optional;
	}

	/**
	 * @see jcmdline.Parameter#isSet()
	 */
	public boolean isSet() {
		return set;
	}

	/**
	 * @see jcmdline.Parameter#setAcceptableValues(java.util.Collection)
	 */
	public void setAcceptableValues(Collection<T> vals) {
		if (vals == null || vals.size() == 0) {
			acceptableValues = null;
		} else {
			acceptableValues = new ArrayList<T>(vals.size());
			for (T val : vals) {
				acceptableValues.add(val);
			}
		}
	}

	/**
	 * @see jcmdline.Parameter#setAcceptableValues(Collection)
	 */
	public void setAcceptableValues(T[] vals) {
		if (vals == null || vals.length == 0) {
			acceptableValues = null;
		} else {
			acceptableValues = new ArrayList<T>(vals.length);
			for (T val : vals) {
				acceptableValues.add(val);
			}
		}
	}

	/**
	 * @see jcmdline.Parameter#setDesc(java.lang.String)
	 */
	public void setDesc(String desc) throws IllegalArgumentException {
		int minDescLen = 5;
		if (desc.length() < minDescLen) {
			throw new IllegalArgumentException(Strings.get(
					"AbstractParameter.descTooShort", new Object[] { tag }));
		}
		this.desc = desc;
	}

	/**
	 * @see jcmdline.Parameter#setHidden(boolean)
	 */
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	/**
	 * @see jcmdline.Parameter#setIgnoreRequired(boolean)
	 */
	public void setIgnoreRequired(boolean ignoreRequired) {
		this.ignoreRequired = ignoreRequired;
	}

	/**
	 * @see jcmdline.Parameter#setMultiValued(boolean)
	 */
	public void setMultiValued(boolean multiValued) {
		this.multiValued = multiValued;
	}

	/**
	 * @see jcmdline.Parameter#setOptional(boolean)
	 */
	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	/**
	 * @see jcmdline.Parameter#setOptionLabel(java.lang.String)
	 */
	public void setOptionLabel(String optionLabel) {
		this.optionLabel = optionLabel;
	}

	/**
	 * @see jcmdline.Parameter#setTag(java.lang.String)
	 */
	public void setTag(String tag) throws IllegalArgumentException {
		if (tag == null || tag.length() < 1) {
			throw new IllegalArgumentException(Strings
					.get("AbstractParameter.emptyTag"));
		}
		if (tag.indexOf("=") != -1) {
			throw new IllegalArgumentException(Strings.get(
					"AbstractParameter.illegalCharInTag", new Object[] { tag,
							"=" }));
		}
		this.tag = tag;
	}

	/**
	 * @see jcmdline.Parameter#setValue(java.lang.Object)
	 */
	public void setValue(T value) throws CmdLineException {
		values.clear();
		addValue(value); // Let addValue() validate
	}

	/**
	 * @see jcmdline.Parameter#setValues(List)
	 */
	public void setValues(List<T> values) throws CmdLineException {
		this.values.clear();
		for (T val : values) {
			addValue(val); // let addValue() validate
		}
	}

	/**
	 * @see jcmdline.Parameter#setValues(Object[])
	 */
	public void setValues(T[] values) throws CmdLineException {
		this.values.clear();
		for (T val : values) {
			addValue(val); // let addValue() validate
		}
	}

	/**
	 * This implementation compares the value to the acceptable values if any
	 * have been defined.
	 * 
	 * @param value
	 *            the value to be validated
	 * @throws CmdLineException
	 *             if there are acceptable values defined and the value is not
	 *             one of them.
	 */
	public void validateValue(T value) throws CmdLineException {
		if (acceptableValues != null) {
			for (T accVal : acceptableValues) {
				if (accVal.equals(value)) {
					return;
				}
			}
			int maxExpectedAVLen = 200;
			StringBuffer b = new StringBuffer(maxExpectedAVLen);
			for (T accVal : acceptableValues) {
				b.append("\n   " + accVal);
			}
			throw new CmdLineException(Strings.get(
					"Parameter.valNotAcceptableVal", new Object[] { value, tag,
							b.toString() }));
		}
	}
}

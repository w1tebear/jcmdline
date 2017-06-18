/*
 * Parameter.java
 *
 * Interface:
 *   public   Parameter
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

import java.util.Collection;
import java.util.List;

/**
 * Interface for command line parameters.
 * 
 * @author Lynne Lawrence
 * @version jcmdline Rel. @VERSION@ $Id: Parameter.java,v 1.2 2002/12/07
 *          14:22:06 lglawrence Exp $
 */
public interface Parameter<T> {

	/**
	 * when used as a value for the <code>hidden</code> indicator, indicates
	 * that a parameter is hidden, and its description will <b>not</b> be listed
	 * in the usage.
	 */
	public static final boolean HIDDEN = true;

	/**
	 * when used as a value for the <code>multiValued</code> indicator,
	 * specifies that an parameter accepts mulitiple values
	 */
	public static final boolean MULTI_VALUED = true;

	/**
	 * when used as a value for the <code>optional</code> indicator, specifies
	 * that an parameter is optional
	 */
	public static final boolean OPTIONAL = true;

	/**
	 * when used as a value for the <code>hidden</code> indicator, indicates
	 * that a parameter is public, and its description will be listed in the
	 * usage.
	 */
	public static final boolean PUBLIC = false;

	/**
	 * when used as a value for the <code>optional</code> indicator, specifies
	 * that an parameter is required
	 */
	public static final boolean REQUIRED = false;

	/**
	 * when used as a value for the <code>multiValued</code> indicator,
	 * specifies that a parameter accepts only one value
	 */
	public static final boolean SINGLE_VALUED = false;

	/**
	 * Adds the value, specified as a String. The String will be converted to
	 * the correct type and validated.
	 * 
	 * @param value
	 *            the value to be added
	 * @throws CmdLineException
	 *             if the value of the entity has already been set and
	 *             <code>multiValued</code> is not <code>true</code>, or if the
	 *             value is not valid.
	 */
	public void addStringValue(String value) throws CmdLineException;

	/**
	 * Adds the specified Object as a value for this entity - the Object will be
	 * validated with respect to the constraints of the Parameter.
	 * 
	 * @param value
	 *            the value to be added
	 * @throws CmdLineException
	 *             if the value of the entity has already been set and
	 *             <code>multiValued</code> is not <code>true</code>, or if the
	 *             validation provided by the implementing class fails.
	 */
	public void addValue(T value) throws CmdLineException;

	/**
	 * Gets the values that are acceptable for this parameter, if a restricted
	 * set exists. If there is no restricted set of acceptable values, null is
	 * returned.
	 * 
	 * @return a set of acceptable values for the Parameter, or null if there is
	 *         none.
	 */
	public List<T> getAcceptableValues();

	/**
	 * gets the value of the parameter's description
	 * 
	 * @return this parameter's description
	 */
	public String getDesc();

	/**
	 * Gets the flag indicating that during parse, missing required Parameters
	 * are ignored if this Parameter is set. Typically used by Parameters that
	 * cause an action then call System.exit(), like "-help".
	 * 
	 * @return <code>true</code> if missing required Parameters will be ignored
	 *         when this Parameter is set.
	 */
	public boolean getIgnoreRequired();

	/**
	 * gets the value of optionLabel
	 * 
	 * @return the string used as a label for the parameter's value
	 */
	public String getOptionLabel();

	/**
	 * gets the value of tag
	 * 
	 * @return a unique identifier for this parameter
	 */
	public String getTag();

	/**
	 * The value of the parameter, in the case where the parameter is not
	 * multi-valued. For a multi-valued parameter, the first value specified is
	 * returned.
	 * 
	 * @return The value of the parameter, or null if the parameter has not been
	 *         set.
	 * @see #getValues()
	 */
	public T getValue();

	/**
	 * gets the values associated with this Parameter - the List will be in the
	 * order the values were listed on the command line.
	 * 
	 * @return The values associated with this Parameter. Note that this might
	 *         be an empty List if the Parameter has not been set.
	 * @see #isSet()
	 */
	public List<T> getValues();

	/**
	 * gets the value of the hidden indicator
	 * 
	 * @return true ({@link #HIDDEN}) if the parameter is a hidden parameter
	 */
	public boolean isHidden();

	/**
	 * gets the value of multiValued indicator
	 * 
	 * @return true if the parameter can have multiple values
	 */
	public boolean isMultiValued();

	/**
	 * returns the value of the optional indicator
	 * 
	 * @return true if the parameter is optional
	 */
	public boolean isOptional();

	/**
	 * gets an indicator that the parameter's value has been set
	 * 
	 * @return true if the parameter's value has been set, false otherwise
	 */
	public boolean isSet();

	/**
	 * Sets the values that are acceptable for this parameter, if a restricted
	 * set exists. A null <code>vals</code> value, or an empty <code>vals</code>
	 * Collection, will result in any previously set acceptable values being
	 * cleared.
	 * <P>
	 * The <code>toString()</code> values of the Objects in <code>vals</code>
	 * will be used for the acceptable values.
	 * 
	 * @param vals
	 *            the new acceptable values
	 */
	public void setAcceptableValues(Collection<T> vals);

	/**
	 * Sets the values that are acceptable for this parameter, if a restricted
	 * set exists. A null <code>vals</code> value, or an empty <code>vals</code>
	 * array, will result in any previously set acceptable values being cleared.
	 * 
	 * @param vals
	 *            the new acceptable values
	 * @see #getAcceptableValues()
	 */
	public void setAcceptableValues(T[] vals);

	/**
	 * sets the value of this parameter's description
	 * 
	 * @param desc
	 *            a description of the parameter, suitable for display in the
	 *            command's usage
	 * @throws IllegalArgumentException
	 *             if <code>desc</code> is fewer than 5 charaters.
	 */
	public void setDesc(String desc) throws IllegalArgumentException;

	/**
	 * sets the value of the hidden indicator
	 * 
	 * @param hidden
	 *            true ({@link #HIDDEN}) if the parameter is a hidden parameter
	 */
	public void setHidden(boolean hidden);

	/**
	 * Sets a flag such that during parse, missing required Parameters are
	 * ignored if this Parameter is set. Typically used by Parameters that cause
	 * an action then call System.exit(), like "-help".
	 * 
	 * @param ignoreRequired
	 *            set to <code>true</code> to ignore missing required Parameters
	 *            if this Parameter is set
	 * @see #getIgnoreRequired()
	 */
	public void setIgnoreRequired(boolean ignoreRequired);

	/**
	 * sets the value of the multiValued indicator
	 * 
	 * @param multiValued
	 *            true if the parameter can have multiple values
	 */
	public void setMultiValued(boolean multiValued);

	/**
	 * indicates whether or not the parameter is optional
	 * 
	 * @param optional
	 *            true if the parameter is optional
	 */
	public void setOptional(boolean optional);

	/**
	 * Sets the value of optionLabel. This label will be used when the usage for
	 * the command is displayed. For instance, a date parameter might use
	 * "&lt;mm/dd/yy&gt;". This could then be displayed as in the following
	 * usage.
	 * 
	 * <PRE>
	 * st_date &lt;mm/dd/yy&gt;  the start date of the report
	 * </PRE>
	 * 
	 * The default is the empty string.
	 * 
	 * @param optionLabel
	 *            The string used as a label for the parameter's value. If null,
	 *            an empty string is used.
	 * @see #getOptionLabel()
	 */
	public void setOptionLabel(String optionLabel);

	/**
	 * sets the value of tag
	 * 
	 * @param tag
	 *            a unique identifier for this parameter. If the parameter is
	 *            used as an option, it will be used to identify the option on
	 *            the command line. In the case where the parameter is used as
	 *            an argument, it will only be used to identify the argument in
	 *            the usage statement. Tags must be made up of any character but
	 *            '='.
	 * @throws IllegalArgumentException
	 *             if the length of <code>tag
     *                  </code> is less than 1, or
	 *             <code>tag</code> contains an invalid character.
	 */
	public void setTag(String tag) throws IllegalArgumentException;

	/**
	 * Sets the value of the parameter to the specified string.
	 * 
	 * @param value
	 *            the new value of the parameter
	 * @throws CmdLineException
	 *             if the validation provided by the implementing class fails.
	 */
	public void setValue(T value) throws CmdLineException;

	/**
	 * Sets the values of the parameter to those specified.
	 * 
	 * @param values
	 *            A List of objects to be used as the parameter's values.
	 * @throws CmdLineException
	 *             if more than one value is specified and
	 *             <code>multiValued</code> is not <code>true</code>, or if the
	 *             validation provided by the implementing class fails.
	 */
	public void setValues(List<T> values) throws CmdLineException;

	/**
	 * Sets the values of the parameter to those specified.
	 * 
	 * @param values
	 *            The objects to be used as the parameter's values.
	 * @throws CmdLineException
	 *             if more than one value is specified and
	 *             <code>multiValued</code> is not <code>true</code>, or if the
	 *             validation provided by the implementing class fails.
	 */
	public void setValues(T[] values) throws CmdLineException;

	/**
	 * verifies that <code>value</code> is valid for this Parameter
	 * 
	 * @param value
	 *            the value to be validated
	 * @throws CmdLineException
	 *             if <code>value</code> is not valid.
	 */
	// public void validateValue(T value) throws CmdLineException;
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
	// public T convertValue(String strVal) throws CmdLineException;
}

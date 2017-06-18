/*
 * TimeParam.java
 *
 * jcmdline Rel. @VERSION@ $Id: TimeParam.java,v 1.4 2009/08/07 16:13:28 lglawrence Exp $
 *
 * Classes:
 *   public   TimeParam
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A parameter that accepts a time as its value.
 * <p>
 * The format for the time is "HH:mm:ss:SSS", where the seconds and/or
 * milliseconds portion may be left off by the user, in which case they will be
 * defaulted.
 * <P>
 * Sample Usage:
 * 
 * <pre>
 *     TimeParam startTimeParam = 
 *         new TimeParam(&quot;startTime&quot;, 
 *                       &quot;start time of report&quot;, 
 *                       TimeParam.REQUIRED);
 *     TimeParam endTimeParam = 
 *         new TimeParam(&quot;endTime&quot;, 
 *                       &quot;end time of report&quot;, 
 *                       TimeParam.REQUIRED);
 * 
 *     // Seconds and millis for startTime will both be 0 by default.
 *     // Set the seconds and millis for the end of the report to be the end 
 *     // of a minute.  The date portion will be the current day, which is 
 *     // the default.
 *     endTimeParam.setDefaultSeconds(59);
 *     endTimeParam.setDefaultMilliSeconds(999);
 * 
 *     CmdLineHandler cl = new DefaultCmdLineHandler(
 *         &quot;myreport&quot;, &quot;generate current activity report&quot;,
 *         new Parameter[] {}, 
 *         new Parameter[] { startTimeParam, endTimeParam });
 *     
 *     cl.parse();
 * 
 *     // Don't need to check isSet() because params are REQUIRED
 *     Date stTime = startTimeParam.getValue();
 *     Date enTime = endTimeParam.getValue();
 *     .
 *     .
 * </pre>
 * 
 * This will result in a command line that may be executed as:
 * 
 * <pre>
 *   myreport &quot;10:12&quot; &quot;23:34&quot;
 * </pre>
 * 
 * or
 * 
 * <pre>
 *   myreport &quot;10:12:34:567&quot; &quot;23:34:34:567&quot;
 * </pre>
 * 
 * @author Lynne Lawrence
 * @version jcmdline Rel. @VERSION@ $Id: TimeParam.java,v 1.2 2002/12/07
 *          14:22:06 lglawrence Exp $
 * @see DateParam
 * @see TimeParam
 */
public class TimeParam extends AbstractParameter<Date> {

	private static final String sTimeFmt = "HH:mm:ss:SSS";
	private static final String sTimeFmtDisplay = "HH:mm[:ss[:SSS]]";

	/**
	 * The date, whose month, day, and year will be used to form the full Date
	 * associated with values of this Parameter.
	 */
	private Date datePortion = new Date();

	/**
	 * The default millisecond value to use if not specified by the user. This
	 * will default to 0 if never specified for a TimeParam object.
	 * 
	 * @see #setDefaultMilliSeconds(int) setDefaultMilliSeconds()
	 * @see #getDefaultMilliSeconds()
	 */
	private int defaultMilliSeconds = 0;

	/**
	 * The seconds default to use if not specified by the user. This will
	 * default to 0 if never specified for a TimeParam object.
	 * 
	 * @see #setDefaultSeconds(int) setDefaultSeconds()
	 * @see #getDefaultSeconds()
	 */
	private int defaultSeconds = 0;

	/**
	 * constructor - creates single-valued, optional, public parameter, with a
	 * default datePortion of the current day
	 * 
	 * @param tag
	 *            a unique identifier for this parameter
	 * @param desc
	 *            a description of the parameter, suitable for display in a
	 *            usage statement
	 * @throws IllegalArgumentException
	 *             if <code>tag</code> or <desc> are invalid.
	 * @see AbstractParameter#setTag(String) setTag()
	 * @see AbstractParameter#setDesc(String) setDesc()
	 */
	public TimeParam(String tag, String desc) {
		this(tag, desc, OPTIONAL, SINGLE_VALUED, PUBLIC);
	}

	/**
	 * constructor - creates single-valued, public parameter which will will be
	 * either optional or required, as specified, with a default datePortion of
	 * the current day
	 * 
	 * @param tag
	 *            a unique identifier for this parameter
	 * @param desc
	 *            a description of the parameter, suitable for display in a
	 *            usage statement
	 * @param optional
	 *            {@link Parameter#OPTIONAL OPTIONAL} if optional,
	 *            {@link Parameter#REQUIRED REQUIRED} if required
	 * @throws IllegalArgumentException
	 *             if any of the specified parameters are invalid.
	 * @see AbstractParameter#setTag(String) setTag()
	 * @see AbstractParameter#setDesc(String) setDesc()
	 */
	public TimeParam(String tag, String desc, boolean optional) {
		this(tag, desc, optional, SINGLE_VALUED, PUBLIC);
	}

	/**
	 * constructor - creates a public parameter which will will be either
	 * optional or required, and/or multi-valued, as specified, with a default
	 * datePortion of the current day
	 * 
	 * @param tag
	 *            a unique identifier for this parameter
	 * @param desc
	 *            a description of the parameter, suitable for display in a
	 *            usage statement
	 * @param optional
	 *            {@link Parameter#OPTIONAL OPTIONAL} if optional,
	 *            {@link Parameter#REQUIRED REQUIRED} if required
	 * @param multiValued
	 *            {@link Parameter#MULTI_VALUED MULTI_VALUED} if the parameter
	 *            can accept multiple values, {@link Parameter#SINGLE_VALUED
	 *            SINGLE_VALUED} if the parameter can contain only a single
	 *            value
	 * @throws IllegalArgumentException
	 *             if any of the specified parameters are invalid.
	 * @see AbstractParameter#setTag(String) setTag()
	 * @see AbstractParameter#setDesc(String) setDesc()
	 * @see Parameter#SINGLE_VALUED SINGLE_VALUED
	 * @see Parameter#MULTI_VALUED MULTI_VALUED
	 */
	public TimeParam(String tag, String desc, boolean optional,
			boolean multiValued) {
		this(tag, desc, optional, multiValued, PUBLIC);
	}

	/**
	 * constructor - creates a parameter which will will be either optional or
	 * required, single or multi-valued, and hidden or public as specified, with
	 * a default datePortion of the current day
	 * 
	 * @param tag
	 *            a unique identifier for this parameter
	 * @param desc
	 *            a description of the parameter, suitable for display in a
	 *            usage statement
	 * @param optional
	 *            {@link Parameter#OPTIONAL OPTIONAL} if optional,
	 *            {@link Parameter#REQUIRED REQUIRED} if required
	 * @param multiValued
	 *            {@link Parameter#MULTI_VALUED MULTI_VALUED} if the parameter
	 *            can accept multiple values, {@link Parameter#SINGLE_VALUED
	 *            SINGLE_VALUED} if the parameter can contain only a single
	 *            value
	 * @param hidden
	 *            {@link Parameter#HIDDEN HIDDEN} if parameter is not to be
	 *            listed in the usage, {@link Parameter#PUBLIC PUBLIC}
	 *            otherwise.
	 * @throws IllegalArgumentException
	 *             if any of the specified parameters are invalid.
	 * @see AbstractParameter#setTag(String) setTag()
	 * @see AbstractParameter#setDesc(String) setDesc()
	 * @see Parameter#SINGLE_VALUED SINGLE_VALUED
	 * @see Parameter#MULTI_VALUED MULTI_VALUED
	 * @see Parameter#HIDDEN HIDDEN
	 * @see Parameter#PUBLIC PUBLIC
	 */
	public TimeParam(String tag, String desc, boolean optional,
			boolean multiValued, boolean hidden) {

		this.setTag(tag);
		this.setDesc(desc);
		this.setOptional(optional);
		this.setMultiValued(multiValued);
		this.setHidden(hidden);
		this.setOptionLabel(sTimeFmtDisplay);
	}

	/**
	 * constructor - creates a single-valued, optional, public, number parameter
	 * whose value must be one of the specified values, with a default
	 * datePortion of the current day
	 * 
	 * @param tag
	 *            the tag associated with this parameter
	 * @param desc
	 *            a description of the parameter, suitable for display in a
	 *            usage statement
	 * @param acceptableValues
	 *            the acceptable values for the parameter
	 * @throws IllegalArgumentException
	 *             if any parameter is invalid.
	 * @see AbstractParameter#setTag(String) setTag()
	 * @see AbstractParameter#setDesc(String) setDesc()
	 * @see AbstractParameter#setAcceptableValues(Object[]) setAcceptableValues()
	 */
	public TimeParam(String tag, String desc, Date[] acceptableValues) {
		this(tag, desc, acceptableValues, OPTIONAL, SINGLE_VALUED, PUBLIC);
	}

	/**
	 * constructor - creates a single-valued, public, number parameter whose
	 * value must be one of the specified values, and which is required or
	 * optional, as specified, with a default datePortion of the current day
	 * 
	 * @param tag
	 *            the tag associated with this parameter
	 * @param desc
	 *            a description of the parameter, suitable for display in a
	 *            usage statement
	 * @param acceptableValues
	 *            the acceptable values for the parameter
	 * @param optional
	 *            {@link Parameter#OPTIONAL OPTIONAL} if optional,
	 *            {@link Parameter#REQUIRED REQUIRED} if required
	 * @throws IllegalArgumentException
	 *             if any parameter is invalid.
	 * @see AbstractParameter#setTag(String) setTag()
	 * @see AbstractParameter#setDesc(String) setDesc()
	 * @see AbstractParameter#setAcceptableValues(Object[]) setAcceptableValues()
	 * @see Parameter#OPTIONAL OPTIONAL
	 * @see Parameter#REQUIRED REQUIRED
	 */
	public TimeParam(String tag, String desc, Date[] acceptableValues,
			boolean optional) {
		this(tag, desc, acceptableValues, optional, SINGLE_VALUED, PUBLIC);
	}

	/**
	 * constructor - creates a public number parameter whose value must be one
	 * of the specified values, and which is required or optional and/or
	 * multi-valued, as specified, with a default datePortion of the current day
	 * 
	 * @param tag
	 *            the tag associated with this parameter
	 * @param desc
	 *            a description of the parameter, suitable for display in a
	 *            usage statement
	 * @param acceptableValues
	 *            the acceptable values for the parameter
	 * @param optional
	 *            {@link Parameter#OPTIONAL OPTIONAL} if optional,
	 *            {@link Parameter#REQUIRED REQUIRED} if required
	 * @param multiValued
	 *            {@link Parameter#MULTI_VALUED MULTI_VALUED} if the parameter
	 *            can accept multiple values, {@link Parameter#SINGLE_VALUED
	 *            SINGLE_VALUED} if the parameter can contain only a single
	 *            value
	 * @throws IllegalArgumentException
	 *             if any parameter is invalid.
	 * @see AbstractParameter#setTag(String) setTag()
	 * @see AbstractParameter#setDesc(String) setDesc()
	 * @see AbstractParameter#setAcceptableValues(Object[]) setAcceptableValues()
	 * @see Parameter#OPTIONAL OPTIONAL
	 * @see Parameter#REQUIRED REQUIRED
	 * @see Parameter#SINGLE_VALUED SINGLE_VALUED
	 * @see Parameter#MULTI_VALUED MULTI_VALUED
	 */
	public TimeParam(String tag, String desc, Date[] acceptableValues,
			boolean optional, boolean multiValued) {
		this(tag, desc, acceptableValues, optional, multiValued, PUBLIC);
	}

	/**
	 * constructor - creates a Parameter, all of whose options are specified,
	 * with a default datePortion of the current day
	 * 
	 * @param tag
	 *            the tag associated with this parameter
	 * @param desc
	 *            a description of the parameter, suitable for display in a
	 *            usage statement
	 * @param acceptableValues
	 *            the acceptable values for the parameter
	 * @param optional
	 *            {@link Parameter#OPTIONAL OPTIONAL} if optional,
	 *            {@link Parameter#REQUIRED REQUIRED} if required
	 * @param multiValued
	 *            {@link Parameter#MULTI_VALUED MULTI_VALUED} if the parameter
	 *            can accept multiple values, {@link Parameter#SINGLE_VALUED
	 *            SINGLE_VALUED} if the parameter can contain only a single
	 *            value
	 * @param hidden
	 *            {@link Parameter#HIDDEN HIDDEN} if parameter is not to be
	 *            listed in the usage, {@link Parameter#PUBLIC PUBLIC}
	 *            otherwise.
	 * @throws IllegalArgumentException
	 *             if any parameter is invalid.
	 * @see AbstractParameter#setTag(String) setTag()
	 * @see AbstractParameter#setDesc(String) setDesc()
	 * @see AbstractParameter#setAcceptableValues(Object[]) setAcceptableValues()
	 * @see Parameter#OPTIONAL OPTIONAL
	 * @see Parameter#REQUIRED REQUIRED
	 * @see Parameter#SINGLE_VALUED SINGLE_VALUED
	 * @see Parameter#MULTI_VALUED MULTI_VALUED
	 * @see Parameter#HIDDEN HIDDEN
	 * @see Parameter#PUBLIC PUBLIC
	 */
	public TimeParam(String tag, String desc, Date[] acceptableValues,
			boolean optional, boolean multiValued, boolean hidden) {
		this.setTag(tag);
		this.setAcceptableValues(acceptableValues);
		this.setDesc(desc);
		this.setOptional(optional);
		this.setMultiValued(multiValued);
		this.setHidden(hidden);
		this.setOptionLabel(sTimeFmtDisplay);
	}

	/**
	 * @see jcmdline.AbstractParameter#convertValue(java.lang.String)
	 */
	@Override
	public Date convertValue(String strVal) throws CmdLineException {
		String fullval = fullTime(strVal);
		try {
			if (fullval.length() > 12) {
				throw new Exception();
			}
			int n = Integer.parseInt(fullval.substring(0, 2));
			if (n < 0 || n > 23) {
				throw new Exception();
			}
			n = Integer.parseInt(fullval.substring(3, 5));
			if (n < 0 || n > 59) {
				throw new Exception();
			}
			n = Integer.parseInt(fullval.substring(6, 8));
			if (n < 0 || n > 59) {
				throw new Exception();
			}
			n = Integer.parseInt(fullval.substring(9, 12));
			if (n < 0 || n > 999) {
				throw new Exception();
			}
		} catch (Exception e) {
			throw new CmdLineException(Strings.get(
					"TimeParam.invalidTimeFormat", new Object[] { strVal,
							sTimeFmtDisplay }));
		}

		// Combine with date portion to make up a full Date
		String dtfmt = "MM/dd/yy";
		SimpleDateFormat fmt = new SimpleDateFormat(dtfmt);
		String sDate = fmt.format(datePortion);
		Date ret;
		try {
			fmt = new SimpleDateFormat(dtfmt + " " + sTimeFmt);
			ret = fmt.parse(sDate + " " + fullval);
		} catch (ParseException e) {
			// should never happen!!
			throw new RuntimeException(e);
		}
		return ret;
	}

	/**
	 * @return the datePortion
	 */
	public Date getDatePortion() {
		return datePortion;
	}

	/**
	 * Gets the default millisecond value to use if not specified by the user.
	 * This will default to 0 if never specified for a TimeParam object.
	 * 
	 * @return the default millisecond value to use if not specified by the user
	 * @see #setDefaultMilliSeconds(int) setDefaultMilliSeconds()
	 */
	public int getDefaultMilliSeconds() {
		return defaultMilliSeconds;
	}

	/**
	 * Gets the seconds default to use if not specified by the user. This will
	 * default to 0 if never specified for a TimeParam object.
	 * 
	 * @return the seconds default to use if not specified by the user
	 * @see #setDefaultSeconds(int) setDefaultSeconds()
	 */
	public int getDefaultSeconds() {
		return defaultSeconds;
	}

	/**
	 * @param datePortion
	 *            the datePortion to set
	 */
	public void setDatePortion(Date datePortion) {
		this.datePortion = datePortion;
	}

	/**
	 * Sets the default millisecond value to use if not specified by the user.
	 * This will default to 0 if never specified for a TimeParam object.
	 * 
	 * @param defaultMilliSeconds
	 *            the default millisecond value to use if not specified by the
	 *            user
	 * @see #getDefaultMilliSeconds()
	 */
	public void setDefaultMilliSeconds(int defaultMilliSeconds) {
		if (defaultMilliSeconds < 0 || defaultMilliSeconds > 999) {
			throw new IllegalArgumentException(Strings.get(
					"TimeParam.invalidMilliSeconds",
					new Object[] { new Integer(defaultMilliSeconds) }));
		}
		this.defaultMilliSeconds = defaultMilliSeconds;
	}

	/**
	 * Sets the seconds default to use if not specified by the user. This will
	 * default to 0 if never specified for a TimeParam object.
	 * 
	 * @param defaultSeconds
	 *            the seconds default to use if not specified by the user
	 * @see #getDefaultSeconds()
	 */
	public void setDefaultSeconds(int defaultSeconds) {
		if (defaultSeconds < 0 || defaultSeconds > 59) {
			throw new IllegalArgumentException(Strings.get(
					"TimeParam.invalidSeconds", new Object[] { new Integer(
							defaultSeconds) }));
		}
		this.defaultSeconds = defaultSeconds;
	}

	/**
	 * @see jcmdline.AbstractParameter#validateValue(java.lang.Object)
	 */
	public void validateValue(Date val) throws CmdLineException {
		super.validateValue(val);
	}

	/**
	 * Converts a String value to its Date equivalent, filling in default
	 * seconds and milliseconds as necessary.
	 * 
	 * @param val
	 *            the String to be converted
	 * @return the Date object represented by <code>val</code>
	 */
	private String fullTime(String val) {
		if (val.length() == 5) {
			val = String.format("%s:%02d", val, defaultSeconds);
		}
		if (val.length() == 8) {
			val = String.format("%s:%03d", val, defaultMilliSeconds);
		}
		return val;
	}
}

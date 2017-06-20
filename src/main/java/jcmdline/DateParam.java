/*
 * DateParam.java
 *
 * jcmdline Rel. @VERSION@ $Id: DateParam.java,v 1.4 2009/08/07 16:13:28 lglawrence Exp $
 *
 * Classes:
 *   public   DateParam
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
 * A parameter that accepts a date as its value.
 * <p>
 * The format for the date is taken from the <code>strings</code>
 * ResourceBundle.
 * <p>
 * Sample Usage:
 * 
 * <pre>
 *     DateParam startDateParam = 
 *         new DateParam(&quot;startDate&quot;, 
 *                       &quot;start date of report&quot;, 
 *                       DateParam.REQUIRED);
 *     DateParam endDateParam = 
 *         new DateParam(&quot;endDate&quot;, 
 *                       &quot;end date of report&quot;, 
 *                       DateParam.REQUIRED);
 * 
 *     // Time for startDate will be the beginning of the day by default.
 *     // Set the time for the end of the report to be the end of the day.
 *     endDateParam.setDefaultTime(23, 59, 58, 999);
 * 
 *     CmdLineHandler cl = new DefaultCmdLineHandler(
 *         &quot;myreport&quot;, &quot;report of activity over days&quot;,
 *         new Parameter[] {}, 
 *         new Parameter[] { startDateParam, endDateParam });
 *     
 *     cl.parse();
 * 
 *     // Don't need to check isSet() because params are REQUIRED
 *     Date stDate = startDateParam.getValue();
 *     Date enDate = endDateParam.getValue();
 *     .
 *     .
 * </pre>
 * 
 * @author Lynne Lawrence
 * @version jcmdline Rel. @VERSION@ $Id: DateParam.java,v 1.2 2002/12/07
 *          14:22:06 lglawrence Exp $
 * @see DateTimeParam
 * @see TimeParam
 */
public class DateParam extends AbstractParameter<Date> {

	private static final String sDateFmt = Strings.get("DateParam.dateFormat");
	private static final String sTimeFmt = "HH:mm:ss:SSS";
	private static final SimpleDateFormat dateFmt = new SimpleDateFormat(
			sDateFmt);
	private static final SimpleDateFormat dateFmtWTime = new SimpleDateFormat(
			sDateFmt + " " + sTimeFmt);

	/**
	 * The default hours to be added to the date - defaults to 0
	 * 
	 * @see #setDefaultTime(int,int,int,int) setDefaultTime()
	 * @see #getDefaultTime()
	 */
	private int defaultHours = 0;

	/**
	 * The default milliseconds to be added to the date - defaults to 0
	 * 
	 * @see #setDefaultTime(int,int,int,int) setDefaultTime()
	 * @see #getDefaultTime()
	 */
	private int defaultMilliSeconds = 0;

	/**
	 * The default minutes to be added to the date - defaults to 0
	 * 
	 * @see #setDefaultTime(int,int,int,int) setDefaultTime()
	 * @see #getDefaultTime()
	 */
	private int defaultMinutes = 0;

	/**
	 * The default seconds to be added to the date - defaults to 0
	 * 
	 * @see #setDefaultTime(int,int,int,int) setDefaultTime()
	 * @see #getDefaultTime()
	 */
	private int defaultSeconds = 0;

	/**
	 * constructor - creates single-valued, optional, public parameter
	 * 
	 * @param tag
	 *            a unique identifier for this parameter
	 * @param desc
	 *            a description of the parameter, suitable for display in a
	 *            usage statement
	 * @throws IllegalArgumentException
	 *             if <code>tag</code> or <code>desc</code> are invalid.
	 * @see AbstractParameter#setTag(String) setTag()
	 * @see AbstractParameter#setDesc(String) setDesc()
	 */
	public DateParam(String tag, String desc) {
		this(tag, desc, OPTIONAL, SINGLE_VALUED, PUBLIC);
	}

	/**
	 * constructor - creates single-valued, public parameter which will will be
	 * either optional or required, as specified.
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
	public DateParam(String tag, String desc, boolean optional) {
		this(tag, desc, optional, SINGLE_VALUED, PUBLIC);
	}

	/**
	 * constructor - creates a public parameter which will will be either
	 * optional or required, and/or multi-valued, as specified.
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
	public DateParam(String tag, String desc, boolean optional,
			boolean multiValued) {
		this(tag, desc, optional, multiValued, PUBLIC);
	}

	/**
	 * constructor - creates a parameter which will will be either optional or
	 * required, single or multi-valued, and hidden or public as specified.
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
	public DateParam(String tag, String desc, boolean optional,
			boolean multiValued, boolean hidden) {

		this.setTag(tag);
		this.setDesc(desc);
		this.setOptional(optional);
		this.setMultiValued(multiValued);
		this.setHidden(hidden);
		this.setOptionLabel(sDateFmt);
	}

	/**
	 * constructor - creates a single-valued, optional, public, number parameter
	 * whose value must be one of the specified values.
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
	public DateParam(String tag, String desc, Date[] acceptableValues) {
		this(tag, desc, acceptableValues, OPTIONAL, SINGLE_VALUED, PUBLIC);
	}

	/**
	 * constructor - creates a single-valued, public, number parameter whose
	 * value must be one of the specified values, and which is required or
	 * optional, as specified.
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
	public DateParam(String tag, String desc, Date[] acceptableValues,
			boolean optional) {
		this(tag, desc, acceptableValues, optional, SINGLE_VALUED, PUBLIC);
	}

	/**
	 * constructor - creates a public number parameter whose value must be one
	 * of the specified values, and which is required or optional and/or
	 * multi-valued, as specified.
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
	public DateParam(String tag, String desc, Date[] acceptableValues,
			boolean optional, boolean multiValued) {
		this(tag, desc, acceptableValues, optional, multiValued, PUBLIC);
	}

	/**
	 * constructor - creates a Parameter, all of whose options are specified.
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
	public DateParam(String tag, String desc, Date[] acceptableValues,
			boolean optional, boolean multiValued, boolean hidden) {
		this.setTag(tag);
		this.setAcceptableValues(acceptableValues);
		this.setDesc(desc);
		this.setOptional(optional);
		this.setMultiValued(multiValued);
		this.setHidden(hidden);
		this.setOptionLabel(sDateFmt);
	}

	/**
	 * Gets the format used to parse the date/time values.
	 * 
	 * @return the format used to parse the date/time values
	 */
	static String getParseFormat() {
		return dateFmt.toLocalizedPattern();
	}

	/**
	 * @see jcmdline.AbstractParameter#convertValue(java.lang.String)
	 */
	public Date convertValue(String val) throws CmdLineException {
		String sTime = String.format("%02d:%02d:%02d:%03d", defaultHours,
				defaultMinutes, defaultSeconds, defaultMilliSeconds);
		try {
			return dateFmtWTime.parse(val + " " + sTime);
		} catch (ParseException e) {
			throw new CmdLineException(Strings.get("DateParam.invalidDate",
					new Object[] { getTag(), sDateFmt }));
		}
	}

	/**
	 * Gets default values for the time component used to generate the Date
	 * value.
	 * 
	 * @return a 4 element <code>int</code> array, where the elements are the
	 *         default hours, minutes, seconds, and milliseconds, in that order
	 */
	public int[] getDefaultTime() {
		return new int[] { defaultHours, defaultMinutes, defaultSeconds,
				defaultMilliSeconds };
	}

	/**
	 * Sets default values for the time component used to generate the Date
	 * value.
	 * 
	 * @param h
	 *            the hours - 0-23 - defaults to 0
	 * @param m
	 *            the minutes - 0-59 - defaults to 0
	 * @param s
	 *            the seconds - 0-59 - defaults to 0
	 * @param ms
	 *            the milliseconds - 0-999 - defaults to 0
	 * @throws IllegalArgumentException
	 *             if any of the parameters are in error.
	 */
	public void setDefaultTime(int h, int m, int s, int ms) {
		if (h < 0 || h > 23) {
			throw new IllegalArgumentException(Strings.get(
					"DateParam.invalidHours", new Object[] { new Integer(h) }));
		}
		if (m < 0 || m > 59) {
			throw new IllegalArgumentException(Strings
					.get("DateParam.invalidMinutes",
							new Object[] { new Integer(m) }));
		}
		if (s < 0 || s > 59) {
			throw new IllegalArgumentException(Strings
					.get("DateParam.invalidSeconds",
							new Object[] { new Integer(s) }));
		}
		if (ms < 0 || ms > 999) {
			throw new IllegalArgumentException(Strings.get(
					"DateParam.invalidMilliSeconds",
					new Object[] { new Integer(ms) }));
		}
		defaultHours = h;
		defaultMinutes = m;
		defaultSeconds = s;
		defaultMilliSeconds = ms;
	}
}

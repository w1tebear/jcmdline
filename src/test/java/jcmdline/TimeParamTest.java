/*
 * TimeParam_UT.java
 *
 * jcmdline Rel. @VERSION@ $Id: TimeParam_UT.java,v 1.3 2009/08/06 14:31:35 lglawrence Exp $
 *
 * Classes:
 *   public   TimeParam_UT
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import jcmdline.CmdLineException;
import jcmdline.TimeParam;

/**
 * Unit test code for TimeParam.
 * <P>
 * Usage:
 * 
 * <pre>
 *   java TimeParam_UT [-log &lt;level&gt;] [testname [,testname...]]
 *   java TimeParam_UT -help
 * </pre>
 * 
 * @author Lynne Lawrence
 * @version jcmdline Rel. @VERSION@ $Id: TimeParam_UT.java,v 1.2 2002/12/07
 *          14:30:49 lglawrence Exp $
 */
public class TimeParamTest extends BetterTestCase {

	private static List<Date> acceptVals;
	private static final String desc = "the report's start time";
	private static final String tag = "startTime";
	/**
	 * a DateFormat for converting strings code in the UT
	 */
	private static final SimpleDateFormat utDateFmt = new SimpleDateFormat(
			"MM/dd/yy HH:mm:ss:SSS");

	/**
	 * constructor takes name of test method
	 * 
	 * @param name
	 *            The name of the test method to be run.
	 */
	public TimeParamTest(String name) {
		super(name);
	}

	/**
	 * Runs all tests using junit.textui.TestRunner
	 */
	public static void main(String[] args) {
		doMain(args, TimeParamTest.class);
	}

	/**
	 * Sets up data for the test
	 */
	public void setUp() throws Exception {
		acceptVals = new ArrayList<Date>();
		acceptVals.add(utDateFmt.parse("9/23/59 01:03:04:005"));
		acceptVals.add(utDateFmt.parse("9/26/89 10:20:30:400"));
	}

	/**
	 * Undoes all that was done in setUp, clean up after test
	 */
	public void tearDown() {
	}

	/**
	 * Tests setValue() with invalid times. The following times are checked:
	 * 
	 * <pre>
	 *      100:45
	 *      24:45
	 *      23:60
	 *      23:591
	 *      23:50:60
	 *      23:50:598
	 *      23:50:49:1000
	 *      23:50:49:9991
	 * </pre>
	 */
	public void testConvertValueBad() {
		TimeParam p = new TimeParam(tag, desc);
		verifyBadTime(p, "100:45");
		verifyBadTime(p, "24:45");
		verifyBadTime(p, "23:60");
		verifyBadTime(p, "23:591");
		verifyBadTime(p, "23:50:60");
		verifyBadTime(p, "23:50:598");
		verifyBadTime(p, "23:50:49:1000");
		verifyBadTime(p, "23:50:49:9991");
	}

	/**
	 * Tests the convertValue() method
	 */
	public void testConvertValueGood() throws Exception {
		TimeParam p = new TimeParam(tag, desc);
		Date beginToday = getBeginningOfToday();

		String sTime = "20:55:34:021";
		Date val = p.convertValue(sTime);
		assertEquals("convertValue() returned wrong Date for '" + sTime + "'",
				beginToday.getTime() + calcMillis(sTime, 0, 0), val.getTime());

		sTime = "20:55";
		val = p.convertValue(sTime);
		assertEquals("convertValue() returned wrong Date for '" + sTime + "'",
				beginToday.getTime() + calcMillis(sTime, 0, 0), val.getTime());

		// check when datePortion is set
		Date datePortion = utDateFmt.parse("9/23/59 01:03:04:005");
		Date baseDatePortion = utDateFmt.parse("9/23/59 00:00:00:000");
		p.setDatePortion(datePortion);
		sTime = "20:55:34:021";
		val = p.convertValue(sTime);
		assertEquals("convertValue() returned wrong Date for '" + sTime + "'",
				baseDatePortion.getTime() + calcMillis(sTime, 0, 0), val
						.getTime());
		sTime = "20:55";
		val = p.convertValue(sTime);
		assertEquals("convertValue() returned wrong Date for '" + sTime + "'",
				baseDatePortion.getTime() + calcMillis(sTime, 0, 0), val
						.getTime());

		// check when default secs and millis are set
		p = new TimeParam(tag, desc);
		p.setDefaultMilliSeconds(345);
		p.setDefaultSeconds(23);

		sTime = "20:55:34:021";
		val = p.convertValue(sTime);
		assertEquals("convertValue() returned wrong Date for '" + sTime + "'",
				beginToday.getTime() + calcMillis(sTime, 23, 345), val
						.getTime());

		sTime = "20:55:12";
		val = p.convertValue(sTime);
		assertEquals("convertValue() returned wrong Date for '" + sTime + "'",
				beginToday.getTime() + calcMillis(sTime, 23, 345), val
						.getTime());

		sTime = "20:55";
		val = p.convertValue(sTime);
		assertEquals("convertValue() returned wrong Date for '" + sTime + "'",
				beginToday.getTime() + calcMillis(sTime, 23, 345), val
						.getTime());

	}

	/**
	 * Test ctor accepting tag, desc
	 */
	public void testCtor1() {
		TimeParam tp = new TimeParam(tag, desc);
		assertEquals("tag set wrong", tag, tp.getTag());
		assertEquals("desc set wrong", desc, tp.getDesc());
		assertTrue("optional flag not set true by default", tp.isOptional());
		assertTrue("multiValued flag not set false by default", !tp
				.isMultiValued());
		assertTrue("hidden flag not set false by default", !tp.isHidden());
	}

	/**
	 * Test ctor accepting tag, desc, optional
	 */
	public void testCtor2() {
		TimeParam tp = new TimeParam(tag, desc, TimeParam.OPTIONAL);
		assertEquals("tag set wrong", tag, tp.getTag());
		assertEquals("desc set wrong", desc, tp.getDesc());
		assertTrue("multiValued flag not set false by default", !tp
				.isMultiValued());
		assertTrue("hidden flag not set false by default", !tp.isHidden());
		assertTrue("optional flag not set to true", tp.isOptional());

		tp = new TimeParam(tag, desc, TimeParam.REQUIRED);
		assertTrue("optional flag not set to false", !tp.isOptional());
	}

	/**
	 * Test ctor accepting tag, desc, optional, multi-valued
	 */
	public void testCtor3() {
		TimeParam tp = new TimeParam(tag, desc, TimeParam.OPTIONAL,
				TimeParam.MULTI_VALUED);
		assertEquals("tag set wrong", tag, tp.getTag());
		assertEquals("desc set wrong", desc, tp.getDesc());
		assertTrue("optional flag not set to true", tp.isOptional());
		assertTrue("multiValued flag not set true", tp.isMultiValued());
		assertTrue("hidden flag not set false by default", !tp.isHidden());

		tp = new TimeParam(tag, desc, TimeParam.REQUIRED,
				TimeParam.SINGLE_VALUED);
		assertTrue("optional flag not set to false", !tp.isOptional());
		assertTrue("multiValued flag not set false", !tp.isMultiValued());
	}

	/**
	 * Test ctor accepting tag, desc, optional, multi-valued, hidden
	 */
	public void testCtor4() {
		TimeParam tp = new TimeParam(tag, desc, TimeParam.OPTIONAL,
				TimeParam.MULTI_VALUED, TimeParam.HIDDEN);
		assertEquals("tag set wrong", tag, tp.getTag());
		assertEquals("desc set wrong", desc, tp.getDesc());
		assertTrue("optional flag not set to true", tp.isOptional());
		assertTrue("multiValued flag not set true", tp.isMultiValued());
		assertTrue("hidden flag not set true", tp.isHidden());

		tp = new TimeParam(tag, desc, TimeParam.REQUIRED,
				TimeParam.SINGLE_VALUED, TimeParam.PUBLIC);
		assertTrue("optional flag not set to false", !tp.isOptional());
		assertTrue("multiValued flag not set false", !tp.isMultiValued());
		assertTrue("hidden flag not set false", !tp.isHidden());
	}

	/**
	 * Test ctor accepting tag, desc, acceptableValues
	 */
	public void testCtor5() {
		TimeParam tp = new TimeParam(tag, desc, acceptVals
				.toArray(new Date[] {}));
		assertEquals("tag set wrong", tag, tp.getTag());
		assertEquals("desc set wrong", desc, tp.getDesc());
		assertEquals("acceptableValues set wrong", acceptVals, tp
				.getAcceptableValues());
		assertTrue("optional flag not set true by default", tp.isOptional());
		assertTrue("multiValued flag not set false by default", !tp
				.isMultiValued());
		assertTrue("hidden flag not set false by default", !tp.isHidden());
	}

	/**
	 * Test ctor accepting tag, desc, acceptableValues, optional
	 */
	public void testCtor6() {
		TimeParam tp = new TimeParam(tag, desc, acceptVals
				.toArray(new Date[] {}), TimeParam.OPTIONAL);
		assertEquals("tag set wrong", tag, tp.getTag());
		assertEquals("desc set wrong", desc, tp.getDesc());
		assertTrue("optional flag not set to true", tp.isOptional());
		assertEquals("acceptableValues set wrong", acceptVals, tp
				.getAcceptableValues());
		assertTrue("multiValued flag not set false by default", !tp
				.isMultiValued());
		assertTrue("hidden flag not set false by default", !tp.isHidden());

		tp = new TimeParam(tag, desc, acceptVals.toArray(new Date[] {}),
				TimeParam.REQUIRED);
		assertTrue("optional flag not set to false", !tp.isOptional());
	}

	/**
	 * Test ctor accepting tag, desc, acceptableValues, optional, multiValued
	 */
	public void testCtor7() {
		TimeParam tp = new TimeParam(tag, desc, acceptVals
				.toArray(new Date[] {}), TimeParam.OPTIONAL,
				TimeParam.MULTI_VALUED);
		assertEquals("tag set wrong", tag, tp.getTag());
		assertEquals("desc set wrong", desc, tp.getDesc());
		assertTrue("optional flag not set to true", tp.isOptional());
		assertTrue("multiValued flag not set to true", tp.isMultiValued());
		assertEquals("acceptableValues set wrong", acceptVals, tp
				.getAcceptableValues());
		assertTrue("hidden flag not set false by default", !tp.isHidden());

		tp = new TimeParam(tag, desc, acceptVals.toArray(new Date[] {}),
				TimeParam.REQUIRED, TimeParam.SINGLE_VALUED);
		assertTrue("optional flag not set to false", !tp.isOptional());
		assertTrue("multiValued flag not set to false", !tp.isMultiValued());
	}

	/**
	 * Test ctor accepting tag, desc, acceptableValues, optional, multiValued,
	 * hidden
	 */
	public void testCtor8() {
		TimeParam tp = new TimeParam(tag, desc, acceptVals
				.toArray(new Date[] {}), TimeParam.OPTIONAL,
				TimeParam.MULTI_VALUED, TimeParam.HIDDEN);
		assertEquals("tag set wrong", tag, tp.getTag());
		assertEquals("desc set wrong", desc, tp.getDesc());
		assertTrue("optional flag not set to true", tp.isOptional());
		assertTrue("multiValued flag not set to true", tp.isMultiValued());
		assertTrue("hidden flag not set to true", tp.isHidden());
		assertEquals("acceptableValues set wrong", acceptVals, tp
				.getAcceptableValues());

		tp = new TimeParam(tag, desc, acceptVals.toArray(new Date[] {}),
				TimeParam.REQUIRED, TimeParam.SINGLE_VALUED, TimeParam.PUBLIC);
		assertTrue("optional flag not set to false", !tp.isOptional());
		assertTrue("multiValued flag not set to false", !tp.isMultiValued());
		assertTrue("hidden flag not set to false", !tp.isHidden());
	}

	/**
	 * Tests getValue()
	 */
	public void testGetValue() throws Exception {
		TimeParam p = new TimeParam(tag, desc);
		Date val = new Date();
		p.setValue(val);
		Date date = p.getValue();
		assertEquals("getValue() returned wrong date", val, date);
	}

	private long calcMillis(String tm, int dfltSecs, int dfltMillis) {
		String[] parts = tm.split(":");
		int hours = Integer.parseInt(parts[0]);
		int mins = Integer.parseInt(parts[1]);
		int secs = dfltSecs;
		if (parts.length > 2) {
			secs = Integer.parseInt(parts[2]);
		}
		int millis = dfltMillis;
		if (parts.length > 3) {
			millis = Integer.parseInt(parts[3]);
		}
		return (hours * 1000 * 60 * 60) + (mins * 1000 * 60) + (secs * 1000)
				+ millis;
	}

	private Date getBeginningOfToday() {
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * Verifies that the passed time value is <b>not</b> accepted by
	 * convertValue(). If the time is accepted by convertValue(), fail() will be
	 * called and the method will not return.
	 * 
	 * @param time
	 *            the time to be checked out
	 */
	private void verifyBadTime(TimeParam p, String time) {
		try {
			debug("Verifying that time '" + time + "' will fail");
			p.convertValue(time);
			fail("convertValue() did not fail with '" + time + "'");
		} catch (CmdLineException e) {
			checkForMissingString(e.getMessage());
		}
	}
}

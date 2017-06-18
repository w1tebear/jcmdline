/*
 * DateParam_UT.java
 *
 * jcmdline Rel. @VERSION@ $Id: DateParam_UT.java,v 1.3 2009/08/06 14:31:35 lglawrence Exp $
 *
 * Classes:
 *   public   DateParam_UT
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
import java.util.Date;
import java.util.List;

import jcmdline.CmdLineException;
import jcmdline.DateParam;

/**
 * Unit test code for DateParam.
 * <P>
 * Usage:
 * 
 * <pre>
 *   java DateParam_UT [-debug &lt;n&gt;] [testname [,testname...]]
 *   java DateParam_UT -help
 * </pre>
 * 
 * By default, all tests are run, and debug mode is disabled.
 * 
 * @author Lynne Lawrence
 * @version jcmdline Rel. @VERSION@ $Id: DateParam_UT.java,v 1.2 2002/12/07
 *          14:30:49 lglawrence Exp $
 */
public class DateParamTest extends BetterTestCase {

	private static List<Date> acceptVals;
	/**
	 * the same parse format as the class, in this locale
	 */
	private static final SimpleDateFormat dateFmt = new SimpleDateFormat(
			DateParam.getParseFormat());
	private static final String desc = "the report's start date";

	private static final String tag = "startDate";

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
	public DateParamTest(String name) {
		super(name);
	}

	/**
	 * Runs all tests using junit.textui.TestRunner
	 */
	public static void main(String[] args) {
		doMain(args, DateParamTest.class);
	}

	/**
	 * Sets up data for the test
	 */
	public void setUp() throws Exception {
		acceptVals = new ArrayList<Date>();
		acceptVals.add(utDateFmt.parse("9/23/59 00:00:00:000"));
		acceptVals.add(utDateFmt.parse("9/26/89 00:00:00:000"));
	}

	/**
	 * Undoes all that was done in setUp, clean up after test
	 */
	public void tearDown() {
	}

	/**
	 * Tests set/getAcceptableValues()
	 */
	public void testAcceptableValues() throws Exception {
		DateParam p = new DateParam(tag, desc);
		Date date1 = utDateFmt.parse("9/23/59 00:00:00:000");
		Date date2 = utDateFmt.parse("9/26/89 00:00:00:000");
		p.setAcceptableValues(new Date[] { date1, date2 });

		String sParamDate = dateFmt.format(date1);
		debug("set/getAcceptableValues: testing with good date " + sParamDate);
		p.setValue(date1); // should not be a problem
		assertEquals("getValue() returned wrong value", date1, p.getValue());

		Date badDate = utDateFmt.parse("9/11/01 00:00:00:000");
		sParamDate = dateFmt.format(badDate);
		debug("set/getAcceptableValues: testing with bad date " + sParamDate);
		boolean gotException = false;
		try {
			p.setValue(badDate);
		} catch (CmdLineException e) {
			checkForMissingString(e.getMessage());
			gotException = true;
		}
		assertTrue("setValue() did not fail with non-acceptable date",
				gotException);
	}

	/**
	 * Test bad default hours passed to setDefaultTime()
	 */
	public void testBadDefaultHours() {
		DateParam p = new DateParam(tag, desc);
		boolean gotException = false;
		try {
			p.setDefaultTime(24, 59, 59, 999);
		} catch (IllegalArgumentException e) {
			checkForMissingString(e.getMessage());
			gotException = true;
		}
		assertTrue("setDefaultTime() did not fail with hour set to 24",
				gotException);
		gotException = false;
		try {
			p.setDefaultTime(-1, 59, 59, 999);
		} catch (IllegalArgumentException e) {
			checkForMissingString(e.getMessage());
			gotException = true;
		}
		assertTrue("setDefaultTime() did not fail with hour set to -1",
				gotException);
	}

	/**
	 * Test bad default milliseconds passed to setDefaultTime()
	 */
	public void testBadDefaultMilliSeconds() {
		DateParam p = new DateParam(tag, desc);
		boolean gotException = false;
		try {
			p.setDefaultTime(23, 59, 59, 1000);
		} catch (IllegalArgumentException e) {
			checkForMissingString(e.getMessage());
			gotException = true;
		}
		assertTrue(
				"setDefaultTime() did not fail with millisecond set to 1000",
				gotException);
		gotException = false;
		try {
			p.setDefaultTime(23, 59, 59, -1);
		} catch (IllegalArgumentException e) {
			checkForMissingString(e.getMessage());
			gotException = true;
		}
		assertTrue("setDefaultTime() did not fail with millisecond set to -1",
				gotException);
	}

	/**
	 * Test bad default minutes passed to setDefaultTime()
	 */
	public void testBadDefaultMinutes() {
		DateParam p = new DateParam(tag, desc);
		boolean gotException = false;
		try {
			p.setDefaultTime(23, 60, 59, 999);
		} catch (IllegalArgumentException e) {
			checkForMissingString(e.getMessage());
			gotException = true;
		}
		assertTrue("setDefaultTime() did not fail with minute set to 60",
				gotException);
		gotException = false;
		try {
			p.setDefaultTime(23, -1, 59, 999);
		} catch (IllegalArgumentException e) {
			checkForMissingString(e.getMessage());
			gotException = true;
		}
		assertTrue("setDefaultTime() did not fail with minute set to -1",
				gotException);
	}

	/**
	 * Test bad default seconds passed to setDefaultTime()
	 */
	public void testBadDefaultSeconds() {
		DateParam p = new DateParam(tag, desc);
		boolean gotException = false;
		try {
			p.setDefaultTime(23, 59, 60, 999);
		} catch (IllegalArgumentException e) {
			checkForMissingString(e.getMessage());
			gotException = true;
		}
		assertTrue("setDefaultTime() did not fail with second set to 60",
				gotException);
		gotException = false;
		try {
			p.setDefaultTime(23, 59, -1, 999);
		} catch (IllegalArgumentException e) {
			checkForMissingString(e.getMessage());
			gotException = true;
		}
		assertTrue("setDefaultTime() did not fail with second set to -1",
				gotException);
	}

	public void testConvertValue() throws Exception {
		DateParam p = new DateParam(tag, desc);

		String sDate = "9/23/59";
		Date expectedDate = utDateFmt.parse(sDate + " 00:00:00:000");
		Date convertedDate = p.convertValue(sDate);
		assertEquals("Conversion of '" + sDate + "' failed", expectedDate,
				convertedDate);

		p = new DateParam(tag, desc);
		p.setDefaultTime(22, 59, 59, 999);
		expectedDate = utDateFmt.parse(sDate + " 22:59:59:999");
		convertedDate = p.convertValue(sDate);
		assertEquals("Conversion of '" + sDate
				+ "' failed with default time '22:59:59:999'", expectedDate,
				convertedDate);
	}

	/**
	 * Test ctor accepting tag, desc
	 */
	public void testCtor1() {
		DateParam dp = new DateParam(tag, desc);
		assertEquals("tag set wrong", tag, dp.getTag());
		assertEquals("desc set wrong", desc, dp.getDesc());
		assertTrue("optional flag not set true by default", dp.isOptional());
		assertTrue("multiValued flag not set false by default", !dp
				.isMultiValued());
		assertTrue("hidden flag not set false by default", !dp.isHidden());
	}

	/**
	 * Test ctor accepting tag, desc, optional
	 */
	public void testCtor2() {
		DateParam dp = new DateParam(tag, desc, DateParam.OPTIONAL);
		assertEquals("tag set wrong", tag, dp.getTag());
		assertEquals("desc set wrong", desc, dp.getDesc());
		assertTrue("multiValued flag not set false by default", !dp
				.isMultiValued());
		assertTrue("hidden flag not set false by default", !dp.isHidden());
		assertTrue("optional flag not set to true", dp.isOptional());

		dp = new DateParam(tag, desc, DateParam.REQUIRED);
		assertTrue("optional flag not set to false", !dp.isOptional());
	}

	/**
	 * Test ctor accepting tag, desc, optional, multi-valued
	 */
	public void testCtor3() {
		DateParam dp = new DateParam(tag, desc, DateParam.OPTIONAL,
				DateParam.MULTI_VALUED);
		assertEquals("tag set wrong", tag, dp.getTag());
		assertEquals("desc set wrong", desc, dp.getDesc());
		assertTrue("optional flag not set to true", dp.isOptional());
		assertTrue("multiValued flag not set true", dp.isMultiValued());
		assertTrue("hidden flag not set false by default", !dp.isHidden());

		dp = new DateParam(tag, desc, DateParam.REQUIRED,
				DateParam.SINGLE_VALUED);
		assertTrue("optional flag not set to false", !dp.isOptional());
		assertTrue("multiValued flag not set false", !dp.isMultiValued());
	}

	/**
	 * Test ctor accepting tag, desc, optional, multi-valued, hidden
	 */
	public void testCtor4() {
		DateParam dp = new DateParam(tag, desc, DateParam.OPTIONAL,
				DateParam.MULTI_VALUED, DateParam.HIDDEN);
		assertEquals("tag set wrong", tag, dp.getTag());
		assertEquals("desc set wrong", desc, dp.getDesc());
		assertTrue("optional flag not set to true", dp.isOptional());
		assertTrue("multiValued flag not set true", dp.isMultiValued());
		assertTrue("hidden flag not set true", dp.isHidden());

		dp = new DateParam(tag, desc, DateParam.REQUIRED,
				DateParam.SINGLE_VALUED, DateParam.PUBLIC);
		assertTrue("optional flag not set to false", !dp.isOptional());
		assertTrue("multiValued flag not set false", !dp.isMultiValued());
		assertTrue("hidden flag not set false", !dp.isHidden());
	}

	/**
	 * Test ctor accepting tag, desc, acceptableValues
	 */
	public void testCtor5() {
		DateParam dp = new DateParam(tag, desc, acceptVals.toArray(new Date[] {}));
		assertEquals("tag set wrong", tag, dp.getTag());
		assertEquals("desc set wrong", desc, dp.getDesc());
		List<Date> paramAccVals = dp.getAcceptableValues();
		assertEquals("acceptableValues set wrong", acceptVals, paramAccVals);
		assertTrue("optional flag not set true by default", dp.isOptional());
		assertTrue("multiValued flag not set false by default", !dp
				.isMultiValued());
		assertTrue("hidden flag not set false by default", !dp.isHidden());
	}

	/**
	 * Test ctor accepting tag, desc, acceptableValues, optional
	 */
	public void testCtor6() {
		DateParam dp = new DateParam(tag, desc, acceptVals
				.toArray(new Date[] {}), DateParam.OPTIONAL);
		assertEquals("tag set wrong", tag, dp.getTag());
		assertEquals("desc set wrong", desc, dp.getDesc());
		assertTrue("optional flag not set to true", dp.isOptional());
		assertEquals("acceptableValues set wrong", acceptVals, dp
				.getAcceptableValues());
		assertTrue("multiValued flag not set false by default", !dp
				.isMultiValued());
		assertTrue("hidden flag not set false by default", !dp.isHidden());

		dp = new DateParam(tag, desc, acceptVals.toArray(new Date[] {}),
				DateParam.REQUIRED);
		assertTrue("optional flag not set to false", !dp.isOptional());
	}

	/**
	 * Test ctor accepting tag, desc, acceptableValues, optional, multiValued
	 */
	public void testCtor7() {
		DateParam dp = new DateParam(tag, desc, acceptVals
				.toArray(new Date[] {}), DateParam.OPTIONAL,
				DateParam.MULTI_VALUED);
		assertEquals("tag set wrong", tag, dp.getTag());
		assertEquals("desc set wrong", desc, dp.getDesc());
		assertTrue("optional flag not set to true", dp.isOptional());
		assertTrue("multiValued flag not set to true", dp.isMultiValued());
		assertEquals("acceptableValues set wrong", acceptVals, dp
				.getAcceptableValues());
		assertTrue("hidden flag not set false by default", !dp.isHidden());

		dp = new DateParam(tag, desc, acceptVals.toArray(new Date[] {}),
				DateParam.REQUIRED, DateParam.SINGLE_VALUED);
		assertTrue("optional flag not set to false", !dp.isOptional());
		assertTrue("multiValued flag not set to false", !dp.isMultiValued());
	}

	/**
	 * Test ctor accepting tag, desc, acceptableValues, optional, multiValued,
	 * hidden
	 */
	public void testCtor8() {
		DateParam dp = new DateParam(tag, desc, acceptVals
				.toArray(new Date[] {}), DateParam.OPTIONAL,
				DateParam.MULTI_VALUED, DateParam.HIDDEN);
		assertEquals("tag set wrong", tag, dp.getTag());
		assertEquals("desc set wrong", desc, dp.getDesc());
		assertTrue("optional flag not set to true", dp.isOptional());
		assertTrue("multiValued flag not set to true", dp.isMultiValued());
		assertTrue("hidden flag not set to true", dp.isHidden());
		assertEquals("acceptableValues set wrong", acceptVals, dp
				.getAcceptableValues());

		dp = new DateParam(tag, desc, acceptVals.toArray(new Date[] {}),
				DateParam.REQUIRED, DateParam.SINGLE_VALUED, DateParam.PUBLIC);
		assertTrue("optional flag not set to false", !dp.isOptional());
		assertTrue("multiValued flag not set to false", !dp.isMultiValued());
		assertTrue("hidden flag not set to false", !dp.isHidden());
	}

	/**
	 * Test set/getDefaultTime()
	 */
	public void testDefaultTime() throws Exception {
		DateParam p = new DateParam(tag, desc);
		p.setDefaultTime(23, 59, 58, 999);
		int[] a = p.getDefaultTime();
		assertEquals("getDefaultTime() returned wrong hours", 23, a[0]);
		assertEquals("getDefaultTime() returned wrong minutes", 59, a[1]);
		assertEquals("getDefaultTime() returned wrong seconds", 58, a[2]);
		assertEquals("getDefaultTime() returned wrong milliseconds", 999, a[3]);

		Date date = utDateFmt.parse("9/23/59 23:59:58:999");
		String sParamDate = dateFmt.format(date); // should pick up just date
		// portion
		debug("testDefaultTime: testing with " + sParamDate);

		p.setValue(date);
		assertEquals("getValue() returned wrong value", date, p.getValue());
	}

	/**
	 * Test constructor and get/set Values
	 */
	public void testNormal() throws Exception {
		DateParam p = new DateParam(tag, desc);
		Date date = utDateFmt.parse("9/23/59 00:00:00:000");
		String sParamDate = dateFmt.format(date);
		debug("testNormal: testing with " + sParamDate);
		p.setValue(date);
		assertEquals("getValue() returned wrong value", date, p.getValue());
	}
}

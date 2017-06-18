/*
 * StringParam_UT.java
 *
 * jcmdline Rel. @VERSION@ $Id: StringParam_UT.java,v 1.4 2009/08/06 14:31:35 lglawrence Exp $
 *
 * Classes:
 *   public   StringParam_UT
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
import java.util.List;

import jcmdline.CmdLineException;
import jcmdline.StringParam;

/**
 * Unit test code for StringParam
 * 
 * @author Lynne Lawrence
 * @version jcmdline Rel. @VERSION@ $Id: StringParam_UT.java,v 1.3 2005/02/06
 *          23:41:14 lglawrence Exp $
 */
public class StringParamTest extends BetterTestCase {

	private List<String> acceptVals;
	private static final String desc = "this is the desc";
	private static final String tag = "mytag";
	
	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		acceptVals = new ArrayList<String>();
		acceptVals.add("value 1");
		acceptVals.add("value 2");
	}

	/**
	 * constructor takes name of test method
	 * 
	 * @param name
	 *            The name of the test method to be run.
	 */
	public StringParamTest(String name) {
		super(name);
	}

	/**
	 * Runs all tests using junit.textui.TestRunner
	 */
	public static void main(String[] args) {
		doMain(args, StringParamTest.class);
	}

	/**
	 * Test a StringParam with acceptableValues
	 */
	public void testAcceptableValues() throws CmdLineException {
		debug("Starting testAcceptableValues()");
		StringParam sp = new StringParam("myTag", "myDesc", new String[] {
				"val1", "val2" }, StringParam.OPTIONAL,
				StringParam.MULTI_VALUED);
		sp.addValue("val1");
		sp.addValue("val2");
		try {
			sp.addValue("");
			fail("addValue(\"\") did not fail");
		} catch (CmdLineException e) {
		}
		try {
			sp.addValue("val3");
			fail("addValue(\"val3\") did not fail");
		} catch (CmdLineException e) {
			debug("Bad value error message: " + e.getMessage());
		}
		List<String> vals = sp.getValues();
		assertEquals("StringParam contains wrong # of values", 2, vals.size());
	}

	/**
	 * Test ctor w/params tag, desc
	 */
	public void testCtor1() {
		StringParam sp = new StringParam(tag, desc);
		assertEquals("tag set wrong", tag, sp.getTag());
		assertEquals("desc set wrong", desc, sp.getDesc());
		assertTrue("optional flag not set true by default", sp.isOptional());
		assertTrue("multiValued flag not set false by default", !sp
				.isMultiValued());
		assertTrue("hidden flag not set false by default", !sp.isHidden());
	}

	/**
	 * Test ctor w/params tag, desc, acceptableValues, optional, multiValued,
	 * hidden
	 */
	public void testCtor10() {
		StringParam sp = new StringParam(tag, desc, acceptVals.toArray(new String[] {}),
				StringParam.OPTIONAL, StringParam.MULTI_VALUED,
				StringParam.HIDDEN);
		assertEquals("tag set wrong", tag, sp.getTag());
		assertEquals("desc set wrong", desc, sp.getDesc());
		assertTrue("optional flag not set to true", sp.isOptional());
		assertTrue("multiValued flag not set to true", sp.isMultiValued());
		assertTrue("hidden flag not set to true", sp.isHidden());
		assertEquals("acceptableValues set wrong", acceptVals, sp
				.getAcceptableValues());

		sp = new StringParam(tag, desc, acceptVals.toArray(new String[] {}), StringParam.REQUIRED,
				StringParam.SINGLE_VALUED, StringParam.PUBLIC);
		assertTrue("optional flag not set to false", !sp.isOptional());
		assertTrue("multiValued flag not set to false", !sp.isMultiValued());
		assertTrue("hidden flag not set to false", !sp.isHidden());
	}

	/**
	 * Test ctor w/params tag, desc, optional
	 */
	public void testCtor2() {
		StringParam sp = new StringParam(tag, desc, StringParam.OPTIONAL);
		assertEquals("tag set wrong", tag, sp.getTag());
		assertEquals("desc set wrong", desc, sp.getDesc());
		assertTrue("multiValued flag not set false by default", !sp
				.isMultiValued());
		assertTrue("hidden flag not set false by default", !sp.isHidden());
		assertTrue("optional flag not set to true", sp.isOptional());

		sp = new StringParam(tag, desc, StringParam.REQUIRED);
		assertTrue("optional flag not set to false", !sp.isOptional());
	}

	/**
	 * Test ctor w/params tag, desc, minValLen, maxValLen
	 */
	public void testCtor3() {
		int min = 1;
		int max = 100;
		StringParam sp = new StringParam(tag, desc, min, max);
		assertEquals("tag set wrong", tag, sp.getTag());
		assertEquals("desc set wrong", desc, sp.getDesc());
		assertEquals("minValLen set wrong", min, sp.getMinValLen());
		assertEquals("maxValLen set wrong", max, sp.getMaxValLen());
		assertTrue("optional flag not set true by default", sp.isOptional());
		assertTrue("multiValued flag not set false by default", !sp
				.isMultiValued());
		assertTrue("hidden flag not set false by default", !sp.isHidden());
	}

	/**
	 * Test ctor w/params tag, desc, minValLen, maxValLen, optional
	 */
	public void testCtor4() {
		int min = 1;
		int max = 100;
		StringParam sp = new StringParam(tag, desc, min, max,
				StringParam.OPTIONAL);
		assertEquals("tag set wrong", tag, sp.getTag());
		assertEquals("desc not set to true", desc, sp.getDesc());
		assertTrue("optional flag set wrong", sp.isOptional());
		assertEquals("minValLen set wrong", min, sp.getMinValLen());
		assertEquals("maxValLen set wrong", max, sp.getMaxValLen());
		assertTrue("multiValued flag not set false by default", !sp
				.isMultiValued());
		assertTrue("hidden flag not set false by default", !sp.isHidden());

		sp = new StringParam(tag, desc, min, max, StringParam.REQUIRED);
		assertTrue("optional flag not set to false", !sp.isOptional());
	}

	/**
	 * Test ctor w/params tag, desc, minValLen, maxValLen, optional, and
	 * multi-valued
	 */
	public void testCtor5() {
		int min = 1;
		int max = 100;
		StringParam sp = new StringParam(tag, desc, min, max,
				StringParam.OPTIONAL, StringParam.MULTI_VALUED);
		assertEquals("tag set wrong", tag, sp.getTag());
		assertEquals("desc set wrong", desc, sp.getDesc());
		assertTrue("optional flag not set to true", sp.isOptional());
		assertTrue("multiValued flag not set to true", sp.isMultiValued());
		assertEquals("minValLen set wrong", min, sp.getMinValLen());
		assertEquals("maxValLen set wrong", max, sp.getMaxValLen());
		assertTrue("hidden flag not set false by default", !sp.isHidden());

		sp = new StringParam(tag, desc, min, max, StringParam.REQUIRED,
				StringParam.SINGLE_VALUED);
		assertTrue("optional flag not set to false", !sp.isOptional());
		assertTrue("multiValued flag not set to false", !sp.isMultiValued());
	}

	/**
	 * Test ctor w/params tag, desc, minValLen, maxValLen, optional multiValued,
	 * and hidden
	 */
	public void testCtor6() {
		int min = 1;
		int max = 100;
		StringParam sp = new StringParam(tag, desc, min, max,
				StringParam.OPTIONAL, StringParam.MULTI_VALUED,
				StringParam.HIDDEN);
		assertEquals("tag set wrong", tag, sp.getTag());
		assertEquals("desc set wrong", desc, sp.getDesc());
		assertTrue("optional flag not set to true", sp.isOptional());
		assertTrue("multiValued flag not set to true", sp.isMultiValued());
		assertTrue("hidden flag not set to true", sp.isHidden());
		assertEquals("minValLen set wrong", min, sp.getMinValLen());
		assertEquals("maxValLen set wrong", max, sp.getMaxValLen());

		sp = new StringParam(tag, desc, min, max, StringParam.REQUIRED,
				StringParam.SINGLE_VALUED, StringParam.PUBLIC);
		assertTrue("optional flag not set to false", !sp.isOptional());
		assertTrue("multiValued flag not set to false", !sp.isMultiValued());
		assertTrue("hidden flag not set to false", !sp.isHidden());
	}

	/**
	 * Test ctor w/params tag, desc, acceptableValues
	 */
	public void testCtor7() {
		StringParam sp = new StringParam(tag, desc, acceptVals.toArray(new String[] {}));
		assertEquals("tag set wrong", tag, sp.getTag());
		assertEquals("desc set wrong", desc, sp.getDesc());
		assertEquals("acceptableValues set wrong", acceptVals, sp
				.getAcceptableValues());
		assertTrue("optional flag not set true by default", sp.isOptional());
		assertTrue("multiValued flag not set false by default", !sp
				.isMultiValued());
		assertTrue("hidden flag not set false by default", !sp.isHidden());
	}

	/**
	 * Test ctor w/params tag, desc, acceptableValues, optional
	 */
	public void testCtor8() {
		StringParam sp = new StringParam(tag, desc, acceptVals.toArray(new String[] {}),
				StringParam.OPTIONAL);
		assertEquals("tag set wrong", tag, sp.getTag());
		assertEquals("desc set wrong", desc, sp.getDesc());
		assertTrue("optional flag not set to true", sp.isOptional());
		assertEquals("acceptableValues set wrong", acceptVals, sp
				.getAcceptableValues());
		assertTrue("multiValued flag not set false by default", !sp
				.isMultiValued());
		assertTrue("hidden flag not set false by default", !sp.isHidden());

		sp = new StringParam(tag, desc, acceptVals.toArray(new String[] {}), StringParam.REQUIRED);
		assertTrue("optional flag not set to false", !sp.isOptional());
	}

	/**
	 * Test ctor w/params tag, desc, acceptableValues, optional, multiValued
	 */
	public void testCtor9() {
		StringParam sp = new StringParam(tag, desc, acceptVals.toArray(new String[] {}),
				StringParam.OPTIONAL, StringParam.MULTI_VALUED);
		assertEquals("tag set wrong", tag, sp.getTag());
		assertEquals("desc set wrong", desc, sp.getDesc());
		assertTrue("optional flag not set to true", sp.isOptional());
		assertTrue("multiValued flag not set to true", sp.isMultiValued());
		assertEquals("acceptableValues set wrong", acceptVals, sp
				.getAcceptableValues());
		assertTrue("hidden flag not set false by default", !sp.isHidden());

		sp = new StringParam(tag, desc, acceptVals.toArray(new String[] {}), StringParam.REQUIRED,
				StringParam.SINGLE_VALUED);
		assertTrue("optional flag not set to false", !sp.isOptional());
		assertTrue("multiValued flag not set to false", !sp.isMultiValued());
	}

	/**
	 * Test a StringParam with specified value length more than maxValLen
	 */
	public void testMaxLength() {
		StringParam sp = new StringParam("mytag", "mydesc", 2, 10,
				StringParam.OPTIONAL, StringParam.MULTI_VALUED);
		try {
			sp.addValue("12345678901");
			fail("addValue() did not fail with string of length > max");
		} catch (CmdLineException e) {
			checkForMissingString(e.getMessage());
		}
	}

	/**
	 * Test a StringParam with specified value length less than minValLen
	 */
	public void testMinLength() {
		StringParam sp = new StringParam("mytag", "mydesc", 2, 10,
				StringParam.OPTIONAL, StringParam.MULTI_VALUED);
		try {
			sp.addValue("1");
			fail("addValue() did not fail with string of length < min");
		} catch (CmdLineException e) {
			checkForMissingString(e.getMessage());
		}
	}

	/**
	 * Test a StringParam with min/max values set and specified values in range
	 */
	public void testMinMaxLength() throws CmdLineException {
		StringParam sp = new StringParam("mytag", "mydesc", 2, 10,
				StringParam.OPTIONAL, StringParam.MULTI_VALUED);
		sp.addValue("12");
		sp.addValue("1234567890");
	}

	/**
	 * Test testMinMaxUnspec() - tests that getMinValLen() and getMaxVlLen()
	 * return 0 and StringParam.UNSPECIFIED_LENGTH, respectively, if they had
	 * not been set.
	 */
	public void testMinMaxUnspec() {
		StringParam sp = new StringParam("mytag", "mydesc");
		assertEquals("getMinValLen() did not return 0 when not set", 0, sp
				.getMinValLen());
		assertEquals(
				"getMaxValLen() did not return UNSPECIFIED_LENGTH when not set",
				StringParam.UNSPECIFIED_LENGTH, sp.getMaxValLen());
	}

	/**
	 * Tests that a multivalued StringParam contains the correct values
	 */
	public void testMultiValued() throws CmdLineException {
		StringParam sp = new StringParam("mytag", "mydesc", 1, -1,
				StringParam.OPTIONAL, StringParam.MULTI_VALUED);
		String s1 = "value 1";
		String s2 = "value 2";
		sp.addValue(s1);
		sp.addValue(s2);
		List<String> vals = sp.getValues();
		assertEquals("Multi-valued StringParam contains wrong # of values", 2,
				vals.size());
		assertEquals("First value is not correct", s1, vals.get(0));
		assertEquals("Second value is not correct", s2, vals.get(1));
	}

	/**
	 * Test setMaxValLen(). Tests that:
	 * <ul>
	 * <li>getMinValLen() returns the correct value after setMaxValLen().</li>
	 * <li>setMaxValLen() works when max value set same as min.</li>
	 * <li>setMaxValLen() throws an IllegalArgumentException when value set less
	 * than min.</li>
	 * <li>setMaxValLen() throws an IllegalArgumentException when value is
	 * negative.</li>
	 * </ul>
	 */
	public void testSetMaxValLen() {
		StringParam sp = new StringParam("mytag", "mydesc", 2, 10,
				StringParam.OPTIONAL, StringParam.MULTI_VALUED);
		sp.setMaxValLen(5);
		assertEquals("Max value not set correctly after call", 5, sp
				.getMaxValLen());
		sp.setMaxValLen(2);
		assertEquals("Max value not set correctly when set same as min", 2, sp
				.getMaxValLen());
		try {
			sp.setMaxValLen(1);
			fail("setMaxValLen() did not fail with val < min");
		} catch (IllegalArgumentException e) {
			checkForMissingString(e.getMessage());
		}
		try {
			sp.setMaxValLen(-1);
			fail("setMaxValLen() did not fail with val = -1");
		} catch (IllegalArgumentException e) {
			checkForMissingString(e.getMessage());
		}
	}

	/**
	 * Test setMinValLen(). Tests that:
	 * <ul>
	 * <li>getMinValLen() returns the correct value after setMinValLen().</li>
	 * <li>setMinValLen() works when min value set same as max.</li>
	 * <li>setMinValLen() throws an IllegalArgumentException when value set
	 * greater than max.</li>
	 * <li>setMinValLen() throws an IllegalArgumentException when value is
	 * negative.</li>
	 * </ul>
	 */
	public void testSetMinValLen() {
		StringParam sp = new StringParam("mytag", "mydesc", 2, 10,
				StringParam.OPTIONAL, StringParam.MULTI_VALUED);
		sp.setMinValLen(5);
		assertEquals("Min value not set correctly after call", 5, sp
				.getMinValLen());
		sp.setMinValLen(10);
		assertEquals("Min value not set correctly when set same as max", 10, sp
				.getMinValLen());
		try {
			sp.setMinValLen(11);
			fail("setMinValLen() did not fail with val > max");
		} catch (IllegalArgumentException e) {
			checkForMissingString(e.getMessage());
		}
		try {
			sp.setMinValLen(-1);
			fail("setMinValLen() did not fail with val = -1");
		} catch (IllegalArgumentException e) {
			checkForMissingString(e.getMessage());
		}
	}
}

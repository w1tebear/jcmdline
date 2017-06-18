/*
 * IntParam_UT.java
 *
 * jcmdline Rel. @VERSION@ $Id: IntParam_UT.java,v 1.3 2009/08/06 14:31:35 lglawrence Exp $
 *
 * Classes:
 *   public   IntParam_UT
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

import java.util.Arrays;
import java.util.List;

import jcmdline.CmdLineException;
import jcmdline.IntParam;
import jcmdline.Parameter;

/**
 * Unit test code for IntParam
 * 
 * @author Lynne Lawrence
 * @version jcmdline Rel. @VERSION@ $Id: IntParam_UT.java,v 1.2 2002/12/07
 *          14:30:49 lglawrence Exp $
 */
public class IntParamTest extends BetterTestCase {

	/**
	 * constructor takes name of test method
	 * 
	 * @param name
	 *            The name of the test method to be run.
	 */
	public IntParamTest(String name) {
		super(name);
	}

	/**
	 * Runs all tests using junit.textui.TestRunner
	 */
	public static void main(String[] args) {
		doMain(args, IntParamTest.class);
	}

	/**
	 * Test a IntParam with acceptableValues where specified values valid
	 */
	public void testAcceptableValues1() throws CmdLineException {
		debug("Starting testAcceptableValues1()");
		IntParam p = new IntParam("myTag", "myDesc", new Integer[] { 2, 10 },
				true, true);
		p.addValue(new Integer("2"));
		p.addValue(new Integer("10"));

		try {
			p.addValue(new Integer("5"));
		} catch (CmdLineException e) {
			// expected
			checkForMissingString(e.getMessage());
		}
	}

	public void testConvertValue() throws CmdLineException {
		IntParam p = new IntParam("myTag", "myDesc");
		String sVal = "10";
		Integer exepected = new Integer("10");
		assertEquals("convertValue() with '" + sVal + "' returned wrong value",
				exepected, p.convertValue(sVal));

		sVal = "-10";
		exepected = new Integer("-10");
		assertEquals("convertValue() with '" + sVal + "' returned wrong value",
				exepected, p.convertValue(sVal));

		sVal = "";
		try {
			p.convertValue(sVal);
		} catch (CmdLineException e) {
			// expected
			checkForMissingString(e.getMessage());
		}

		sVal = "cat";
		try {
			p.convertValue(sVal);
		} catch (CmdLineException e) {
			// expected
			checkForMissingString(e.getMessage());
		}
	}

	/**
	 * Test constructor taking tag, desc
	 */
	public void testCtor1() {
		String tag = "count";
		String desc = "the number of times to iterate";
		IntParam p = new IntParam(tag, desc);
		assertEquals("tag not set correctly", tag, p.getTag());
		assertEquals("desc not set correctly", desc, p.getDesc());
		assertTrue("optional flag not set correctly", p.isOptional());
		assertTrue("multiValued flag not set correctly", !p.isMultiValued());
		assertTrue("hidden flag not set correctly", !p.isHidden());
	}

	/**
	 * Test constructor taking tag, desc, acceptableValues, optional,
	 * multiValued, hidden
	 */
	public void testCtor10() {
		String tag = "count";
		String desc = "the number of times to iterate";
		Integer[] av = new Integer[] { 3, 4, 5, 6 };
		IntParam p = new IntParam(tag, desc, av, true, true, true);
		assertEquals("tag not set correctly", tag, p.getTag());
		assertEquals("desc not set correctly", desc, p.getDesc());
		assertTrue("optional flag not set to true correctly", p.isOptional());
		assertTrue("multiValued flag not set to true correctly", p
				.isMultiValued());
		assertTrue("hidden flag not set to true correctly", p.isHidden());
		assertEquals("acceptableValues not set correctly", Arrays.asList(av), p
				.getAcceptableValues());
		p = new IntParam(tag, desc, av, false, false, false);
		assertTrue("optional flag not set to false correctly", !p.isOptional());
		assertTrue("multiValued flag not set to false correctly", !p
				.isMultiValued());
		assertTrue("hidden flag not set to false correctly", !p.isHidden());
	}

	/**
	 * Test constructor taking tag, desc, optional
	 */
	public void testCtor2() {
		String tag = "count";
		String desc = "the number of times to iterate";
		IntParam p = new IntParam(tag, desc, IntParam.OPTIONAL);
		assertEquals("tag not set correctly", tag, p.getTag());
		assertEquals("desc not set correctly", desc, p.getDesc());
		assertTrue("multiValued flag not set correctly", !p.isMultiValued());
		assertTrue("hidden flag not set correctly", !p.isHidden());
		assertTrue("optional flag not set correctly to IntParam.OPTIONAL", p
				.isOptional());
		p = new IntParam(tag, desc, IntParam.REQUIRED);
		assertTrue("optional flag not set correctly to IntParam.REQUIRED", !p
				.isOptional());
	}

	/**
	 * Test constructor taking tag, desc, min, max
	 */
	public void testCtor3() {
		String tag = "count";
		String desc = "the number of times to iterate";
		int min = 2;
		int max = 100;
		IntParam p = new IntParam(tag, desc, min, max);
		assertEquals("tag not set correctly", tag, p.getTag());
		assertEquals("desc not set correctly", desc, p.getDesc());
		assertTrue("optional flag not set correctly", p.isOptional());
		assertTrue("multiValued flag not set correctly", !p.isMultiValued());
		assertTrue("hidden flag not set correctly", !p.isHidden());
		assertEquals("min not set correctly", min, p.getMin());
		assertEquals("max not set correctly", max, p.getMax());
	}

	/**
	 * Test constructor taking tag, desc, min, max, optional ind.
	 */
	public void testCtor4() {
		String tag = "count";
		String desc = "the number of times to iterate";
		int min = 2;
		int max = 100;
		IntParam p = new IntParam(tag, desc, min, max, true);
		assertEquals("tag not set correctly", tag, p.getTag());
		assertEquals("desc not set to true correctly", desc, p.getDesc());
		assertTrue("optional flag not set correctly", p.isOptional());
		assertTrue("multiValued flag not set correctly", !p.isMultiValued());
		assertTrue("hidden flag not set correctly", !p.isHidden());
		assertEquals("min not set correctly", min, p.getMin());
		assertEquals("max not set correctly", max, p.getMax());
		p = new IntParam(tag, desc, min, max, false);
		assertTrue("optional flag not set to false correctly", !p.isOptional());
	}

	/**
	 * Test constructor taking tag, desc, min, max, optional, and multi-valued
	 */
	public void testCtor5() {
		String tag = "count";
		String desc = "the number of times to iterate";
		int min = 2;
		int max = 100;
		IntParam p = new IntParam(tag, desc, min, max, true, true);
		assertEquals("tag not set correctly", tag, p.getTag());
		assertEquals("desc not set correctly", desc, p.getDesc());
		assertTrue("optional flag not set to true correctly", p.isOptional());
		assertTrue("multiValued flag not set to true correctly", p
				.isMultiValued());
		assertTrue("hidden flag not set correctly", !p.isHidden());
		assertEquals("min not set correctly", min, p.getMin());
		assertEquals("max not set correctly", max, p.getMax());
		p = new IntParam(tag, desc, min, max, false, false);
		assertTrue("optional flag not set to false correctly", !p.isOptional());
		assertTrue("multiValued flag not set to false correctly", !p
				.isMultiValued());
	}

	/**
	 * Test constructor taking tag, desc, min, max, optional multiValued, and
	 * hidden
	 */
	public void testCtor6() {
		String tag = "count";
		String desc = "the number of times to iterate";
		int min = 1;
		int max = 100;
		IntParam p = new IntParam(tag, desc, min, max, true, true, true);
		assertEquals("tag not set correctly", tag, p.getTag());
		assertEquals("desc not set correctly", desc, p.getDesc());
		assertTrue("optional flag not set to true correctly", p.isOptional());
		assertTrue("multiValued flag not set to true correctly", p
				.isMultiValued());
		assertTrue("hidden flag not set to true correctly", p.isHidden());
		assertEquals("min not set correctly", min, p.getMin());
		assertEquals("max not set correctly", max, p.getMax());
		p = new IntParam(tag, desc, min, max, false, false, false);
		assertTrue("optional flag not set to false correctly", !p.isOptional());
		assertTrue("multiValued flag not set to false correctly", !p
				.isMultiValued());
		assertTrue("hidden flag not set to false correctly", !p.isHidden());
	}

	/**
	 * Test constructor taking tag, desc, acceptableValues
	 */
	public void testCtor7() {
		String tag = "count";
		String desc = "the number of times to iterate";
		Integer[] av = new Integer[] { 3, 4, 5, 6 };
		IntParam p = new IntParam(tag, desc, av);
		assertEquals("tag not set correctly", tag, p.getTag());
		assertEquals("desc not set correctly", desc, p.getDesc());
		assertTrue("optional flag not set correctly", p.isOptional());
		assertTrue("multiValued flag not set correctly", !p.isMultiValued());
		assertTrue("hidden flag not set correctly", !p.isHidden());
		assertEquals("acceptableValues not set correctly", Arrays.asList(av), p
				.getAcceptableValues());
	}

	/**
	 * Test constructor taking tag, desc, acceptableValues, optional
	 */
	public void testCtor8() {
		String tag = "count";
		String desc = "the number of times to iterate";
		Integer[] av = new Integer[] { 3, 4, 5, 6 };
		IntParam p = new IntParam(tag, desc, av, true);
		assertEquals("tag not set correctly", tag, p.getTag());
		assertEquals("desc not set correctly", desc, p.getDesc());
		assertTrue("optional flag not set to true correctly", p.isOptional());
		assertTrue("multiValued flag not set correctly", !p.isMultiValued());
		assertTrue("hidden flag not set correctly", !p.isHidden());
		assertEquals("acceptableValues not set correctly", Arrays.asList(av), p
				.getAcceptableValues());
		p = new IntParam(tag, desc, av, false);
		assertTrue("optional flag not set to false correctly", !p.isOptional());
	}

	/**
	 * Test constructor taking tag, desc, acceptableValues, optional,
	 * multiValued
	 */
	public void testCtor9() {
		String tag = "count";
		String desc = "the number of times to iterate";
		Integer[] av = new Integer[] { 3, 4, 5, 6 };
		IntParam p = new IntParam(tag, desc, av, true, true);
		assertEquals("tag not set correctly", tag, p.getTag());
		assertEquals("desc not set correctly", desc, p.getDesc());
		assertTrue("optional flag not set to true correctly", p.isOptional());
		assertTrue("multiValued flag not set to true correctly", p
				.isMultiValued());
		assertTrue("hidden flag not set correctly", !p.isHidden());
		assertEquals("acceptableValues not set correctly", Arrays.asList(av), p
				.getAcceptableValues());
		p = new IntParam(tag, desc, av, false, false);
		assertTrue("optional flag not set to false correctly", !p.isOptional());
		assertTrue("multiValued flag not set to false correctly", !p
				.isMultiValued());
	}

	/**
	 * Tests getValue() with single valued param
	 */
	public void testGetValue() throws CmdLineException {
		IntParam p = new IntParam("count", "myDesc");
		Integer val = new Integer("92359");
		p.addValue(val);
		assertEquals("getValue() returned wrong value", val, p.getValue());
	}

	/**
	 * Tests getValue() with multi valued param
	 */
	public void testGetValueMulti() throws CmdLineException {
		IntParam p = new IntParam("count", "myDesc");
		p.setMultiValued(Parameter.MULTI_VALUED);
		Integer val1 = new Integer("92359");
		Integer val2 = new Integer("10286");
		p.addValue(val1);
		p.addValue(val2);
		assertEquals("getValue() returned wrong value for multi-valued", val1,
				p.getValue());
	}

	/**
	 * Tests getValues()
	 */
	public void testGetValues() throws CmdLineException {
		IntParam p = new IntParam("count", "myDesc");
		p.setMultiValued(Parameter.MULTI_VALUED);
		Integer val1 = new Integer("92359");
		Integer val2 = new Integer("10286");
		p.addValue(val1);
		p.addValue(val2);
		List<Integer> vals = p.getValues();
		assertEquals("getValues() returned array of wrong size", 2, vals.size());
		assertEquals("first value returned by getValues() is wrong", val1, vals
				.get(0));
		assertEquals("second value returned by getValues() is wrong", val2,
				vals.get(1));
	}

	/**
	 * Test a IntParam with Min/Max values where specified value in range
	 */
	public void testMinMax() throws Exception {
		debug("Starting testMinMax()");
		IntParam p = new IntParam("count", "mydesc", 2, 99, true, true);
		p.addValue(new Integer("2"));
		p.addValue(new Integer("99"));
		p.addValue(new Integer("50"));

		try {
			p.addValue(new Integer("100"));
			fail("addValue() did not throw CmdLineException with value > max");
		} catch (CmdLineException e) {
			// expected
			checkForMissingString(e.getMessage());
		}
		try {
			p.addValue(new Integer("1"));
			fail("addValue() did not throw CmdLineException with value < min");
		} catch (CmdLineException e) {
			// expected
			checkForMissingString(e.getMessage());
		}
	}
}

/*
 * PosixCmdLineParser_UT.java
 *
 * jcmdline Rel. @VERSION@ $Id: PosixCmdLineParser_UT.java,v 1.3 2009/08/06 14:31:35 lglawrence Exp $
 *
 * Classes:
 *   public   PosixCmdLineParser_UT
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
import java.util.HashMap;
import java.util.List;

import jcmdline.Parameter;
import jcmdline.PosixCmdLineParser;
import jcmdline.StringParam;

/**
 * Unit test code for PosixCmdLineParser.
 * <P>
 * Usage:
 * 
 * <pre>
 *   java PosixCmdLineParser_UT [-debug &lt;n&gt;] [testname [,testname...]]
 *   java PosixCmdLineParser_UT -help
 * </pre>
 * 
 * By default, all tests are run, and debug mode is disabled.
 * 
 * @author Lynne Lawrence
 * @version jcmdline Rel. @VERSION@ $Id: PosixCmdLineParser_UT.java,v 1.2
 *          2002/12/07 14:30:49 lglawrence Exp $
 */
public class PosixCmdLineParserTest extends BetterTestCase {

	ArrayList<Parameter<?>> args;
	HashMap<String, Parameter<?>> opts;
	private StringParam param1 = new StringParam("param1", "this is param1");
	private StringParam param2 = new StringParam("param2", "this is param2");
	private StringParam param3 = new StringParam("param3", "this is param3");
	private StringParam param4 = new StringParam("param4", "this is param4");
	// variables new for each test
	private PosixCmdLineParser parser;

	/**
	 * constructor takes name of test method
	 * 
	 * @param name
	 *            The name of the test method to be run.
	 */
	public PosixCmdLineParserTest(String name) {
		super(name);
	}

	/**
	 * Runs all tests using junit.textui.TestRunner
	 */
	public static void main(String[] args) {
		doMain(args, PosixCmdLineParserTest.class);
	}

	/**
	 * Sets up data for the test
	 */
	public void setUp() {
		parser = new PosixCmdLineParser();
		opts = new HashMap<String, Parameter<?>>();
		opts.put(param1.getTag(), param1);
		opts.put(param2.getTag(), param2);
		args = new ArrayList<Parameter<?>>();
		args.add(param3);
		args.add(param4);
	}

	/**
	 * Undoes all that was done in setUp, clean up after test
	 */
	public void tearDown() {
	}

	/**
	 * Tests with no options and no arguments
	 */
	public void testNoOptNoArg() throws Exception {
		HashMap<String, Parameter<?>> emptyOpts = new HashMap<String, Parameter<?>>();
		ArrayList<Parameter<?>> emptyArgs = new ArrayList<Parameter<?>>();
		parser.parse(new String[] {}, emptyOpts, emptyArgs);
		// try with invalid opt
		try {
			parser.parse(new String[] { "-opt", "opt value" }, emptyOpts,
					emptyArgs);
		} catch (Exception e) {
			checkForMissingString(e.getMessage());
		}
	}

	/**
	 * Tests parse() with 2 options and 2 arguments set
	 */
	public void testParse2Opt2ArgSet() throws Exception {
		param4.setMultiValued(Parameter.MULTI_VALUED);
		String val1 = "param1 value";
		String val2 = "param2 value";
		String val3 = "param3 value";
		String val4 = "param4 value";
		String val5 = "param4 value";
		parser.parse(new String[] { "--param2", val2, "-param1", val1, val3,
				val4, val5 }, opts, args);
		assertEquals("param1 did not get set correctly", val1, param1
				.getValue());
		assertEquals("param2 did not get set correctly", val2, param2
				.getValue());
		assertEquals("param3 did not get set correctly", val3, param3
				.getValue());
		List<String> param4Vals = param4.getValues();
		assertEquals("1st value of multi-valued not set correctly", val4,
				param4Vals.get(0));
		assertEquals("2nd value of multi-valued not set correctly", val5,
				param4Vals.get(1));
	}

	/**
	 * Tests parse() with an argument set
	 */
	public void testParseArgSet() throws Exception {
		String val = "param3 value";
		parser.parse(new String[] { val }, opts, args);
		assertEquals("Argument parameter did not get set correctly", val,
				param3.getValue());
	}

	/**
	 * Tests parse() with a multi-valued argument set
	 */
	public void testParseMultiArgSet() throws Exception {
		param3.setMultiValued(Parameter.MULTI_VALUED);
		String val1 = "param3 value";
		String val2 = "second value";
		parser.parse(new String[] { val1, val2 }, opts, args);
		List<String> argVals = param3.getValues();
		assertEquals("1st value of multi-valued argument did not get set "
				+ "correctly", val1, argVals.get(0));
		assertEquals("2nd value of multi-valued argument did not get set "
				+ "correctly", val2, argVals.get(1));
	}

	/**
	 * Tests parse() with a multi-valued option set
	 */
	public void testParseMultiOptSet() throws Exception {
		param1.setMultiValued(Parameter.MULTI_VALUED);
		String val1 = "param1 value";
		String val2 = "second value";
		parser.parse(new String[] { "-param1", val1, "-param1", val2 }, opts,
				args);
		List<String> argVals = param1.getValues();
		assertEquals("1st value of multi-valued option did not get set "
				+ "correctly", val1, argVals.get(0));
		assertEquals("2nd value of multi-valued option did not get set "
				+ "correctly", val2, argVals.get(1));
	}

	/**
	 * Tests parse() empty cmd line and all optional parameters - should pass
	 */
	public void testParseOptionalParams() throws Exception {
		parser.parse(new String[] {}, opts, args);
	}

	/**
	 * Tests parse() with an option set
	 */
	public void testParseOptSet() throws Exception {
		String val = "param1 value";
		parser.parse(new String[] { "-param1", val }, opts, args);
		assertEquals("Option did not get set correctly", val, param1.getValue());
	}

	/**
	 * Tests parse() with an option set with a double dash
	 */
	public void testParseOptSet2Dash() throws Exception {
		String val = "param1 value";
		parser.parse(new String[] { "--param1", val }, opts, args);
		assertEquals("Option did not get set correctly", val, param1.getValue());
	}
}

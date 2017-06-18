/*
 * BasicCmdLineHandler_UT.java
 *
 * jcmdline Rel. @VERSION@ $Id: BasicCmdLineHandler_UT.java,v 1.6 2009/08/06 14:31:35 lglawrence Exp $
 *
 * Classes:
 *   public   BasicCmdLineHandler_UT
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

import jcmdline.BasicCmdLineHandler;
import jcmdline.CmdLineHandler;
import jcmdline.Parameter;
import jcmdline.StringParam;

/**
 * Unit test code for BasicCmdLineHandler
 * 
 * @author Lynne Lawrence
 * @version jcmdline Rel. @VERSION@ $Id: BasicCmdLineHandler_UT.java,v 1.5
 *          2005/02/07 00:21:06 lglawrence Exp $
 */
public class BasicCmdLineHandlerTest extends BetterTestCase {

	// variables new for each test
	private StringParam param1 = new StringParam("param1", "this is param1");
	private StringParam param2 = new StringParam("param2", "this is param2");
	private StringParam param3 = new StringParam("param3", "this is param3");
	private StringParam param4 = new StringParam("param4", "this is param4");

	/**
	 * constructor takes name of test method
	 * 
	 * @param name
	 *            The name of the test method to be run.
	 */
	public BasicCmdLineHandlerTest(String name) {
		super(name);
	}

	/**
	 * Runs all tests using junit.textui.TestRunner
	 */
	public static void main(String[] args) {
		doMain(args, BasicCmdLineHandlerTest.class);
	}

	/**
	 * Sets up data for the test
	 */
	public void setUp() {
	}

	/**
	 * Undoes all that was done in setUp, clean up after test
	 */
	public void tearDown() {
	}

	/**
	 * Tests constructor with 2 required args passed in Array
	 */
	public void testCtor2ReqArgsArray() {
		param3.setOptional(StringParam.REQUIRED);
		param4.setOptional(StringParam.REQUIRED);
		@SuppressWarnings("unused")
		CmdLineHandler cl = new BasicCmdLineHandler("MyCmd",
				"copies one file to another", new Parameter[] {},
				new Parameter[] { param3, param4 });
	}

	/**
	 * Tests constructor with 2 required args passed in Collection
	 */
	public void testCtor2ReqArgsColl() {
		param3.setOptional(StringParam.REQUIRED);
		param4.setOptional(StringParam.REQUIRED);
		ArrayList<Parameter<?>> a = new ArrayList<Parameter<?>>();
		a.add(param3);
		a.add(param4);
		@SuppressWarnings("unused")
		CmdLineHandler cl = new BasicCmdLineHandler("MyCmd",
				"copies one file to another", new ArrayList<Parameter<?>>(), a);
	}

	/**
	 * Tests constructor with 2 required args
	 */
	public void testCtorMultiArg1() {
		param4.setMultiValued(StringParam.MULTI_VALUED);
		@SuppressWarnings("unused")
		CmdLineHandler cl = new BasicCmdLineHandler("MyCmd",
				"copies one file to another", new Parameter[] {},
				new Parameter[] { param3, param4 });
	}

	/**
	 * Tests constructor with arg following multi-valued arg (should throw
	 * exception)
	 */
	public void testCtorMultiArg2() {
		param3.setMultiValued(StringParam.MULTI_VALUED);
		try {
			@SuppressWarnings("unused")
			CmdLineHandler cl = new BasicCmdLineHandler("MyCmd",
					"copies one file to another", new Parameter[] {},
					new Parameter[] { param3, param4 });
		} catch (Exception e) {
			// should get exception
			checkForMissingString(e.getMessage());
			return;
		}
		fail("constructor with arg following multivalued arg " + "did not fail");
	}

	/**
	 * Tests constructor with no options and no arguments
	 */
	public void testCtorNoOptNoArg() {
		@SuppressWarnings("unused")
		CmdLineHandler cl = new BasicCmdLineHandler("MyCmd",
				"copies one file to another", new Parameter[] {},
				new Parameter[] {});
	}

	/**
	 * Tests constructor with required arg following optional arg (should throw
	 * exception) - args passed as Array
	 */
	public void testCtorReqArgArray() {
		param4.setOptional(StringParam.REQUIRED);
		try {
			@SuppressWarnings("unused")
			CmdLineHandler cl = new BasicCmdLineHandler("MyCmd",
					"copies one file to another", new Parameter[] {},
					new Parameter[] { param3, param4 });
		} catch (Exception e) {
			// should get exception
			checkForMissingString(e.getMessage());
			return;
		}
		fail("constructor with required arg following optional arg "
				+ "did not fail - args passed as Array");
	}

	/**
	 * Tests constructor with required arg following optional arg (should throw
	 * exception) - args passed as Collection
	 */
	public void testCtorReqArgColl() {
		param4.setOptional(StringParam.REQUIRED);
		ArrayList<Parameter<?>> a = new ArrayList<Parameter<?>>();
		a.add(param3);
		a.add(param4);
		try {
			@SuppressWarnings("unused")
			CmdLineHandler cl = new BasicCmdLineHandler("MyCmd",
					"copies one file to another",
					new ArrayList<Parameter<?>>(), a);
		} catch (Exception e) {
			// should get exception
			checkForMissingString(e.getMessage());
			return;
		}
		fail("constructor with required arg following optional arg "
				+ "did not fail - args passed as Collection");
	}

	/**
	 * Tests constructor with optional arg following required arg - args passed
	 * as Array.
	 */
	public void testCtorReqArgOKArray() {
		param3.setOptional(StringParam.REQUIRED);
		@SuppressWarnings("unused")
		CmdLineHandler cl = new BasicCmdLineHandler("MyCmd",
				"copies one file to another", new Parameter[] {},
				new Parameter[] { param3, param4 });
	}

	/**
	 * Tests constructor with optional arg following required arg - args passed
	 * as Collection.
	 */
	public void testCtorReqArgOKColl() {
		param3.setOptional(StringParam.REQUIRED);
		ArrayList<Parameter<?>> a = new ArrayList<Parameter<?>>();
		a.add(param3);
		a.add(param4);
		@SuppressWarnings("unused")
		CmdLineHandler cl = new BasicCmdLineHandler("MyCmd",
				"copies one file to another", new ArrayList<Parameter<?>>(), a);
	}

	/**
	 * Tests parse() with all optional options and arguments
	 */
	public void testParseOptionalParams() {
		CmdLineHandler cl = new BasicCmdLineHandler("MyCmd",
				"copies one file to another",
				new Parameter[] { param1, param2 }, new Parameter[] { param3 });
		cl.setDieOnParseError(false);
		assertTrue("parse() with all optional params failed", cl
				.parse(new String[] {}));
	}

	/**
	 * Tests parse() with a required argument
	 */
	public void testParseReqArg() {
		CmdLineHandler cl = new BasicCmdLineHandler("MyCmd",
				"copies one file to another", new Parameter[] { param1 },
				new Parameter[] { param3, param4 });
		cl.setDieOnParseError(false);
		param3.setOptional(StringParam.REQUIRED);
		assertTrue("parse() did not fail when reqed arg missing", !cl
				.parse(new String[] {}));
		checkForMissingString(cl.getParseError());
		boolean result = cl.parse(new String[] { "arg value" });
		assertTrue("parse() failed when reqed arg specified correctly, error: "
				+ cl.getParseError(), result);
	}

	/**
	 * Tests parse() with a required option
	 */
	public void testParseReqOpt() {
		CmdLineHandler cl = new BasicCmdLineHandler("MyCmd",
				"copies one file to another",
				new Parameter[] { param1, param2 }, new Parameter[] { param3 });
		cl.setDieOnParseError(false);
		param1.setOptional(StringParam.REQUIRED);
		assertTrue("parse() did not fail when reqed option missing", !cl
				.parse(new String[] {}));
		checkForMissingString(cl.getParseError());
		boolean result = cl.parse(new String[] { "-param1", "param value" });
		assertTrue(
				"parse() failed when reqed option specified correctly, error: "
						+ cl.getParseError(), result);
	}

	/**
	 * Tests setArgs(), passing a null for args. Ref sourceforge bug 1038722.
	 */
	public void testSetArgsWNull() {
		CmdLineHandler cl = new BasicCmdLineHandler("MyCmd",
				"copies one file to another",
				new Parameter[] { param1, param2 }, new Parameter[] { param3,
						param4 });
		cl.setArgs(null);
		Collection<Parameter<?>> args = cl.getArgs();
		assertEquals("Wrong number of args returned after setArgs()", 0, args
				.size());
	}

	/**
	 * Tests setOptions()
	 */
	public void testSetOptions() {
		CmdLineHandler cl = new BasicCmdLineHandler("MyCmd",
				"copies one file to another",
				new Parameter[] { param1, param2 }, new Parameter[] {});
		cl.setOptions(new Parameter[] { param3 });
		Collection<Parameter<?>> opts = cl.getOptions();
		assertEquals("Wrong number of options returned after setOptions()", 1,
				opts.size());
		assertTrue("Options don't contain correct option after setOptions()",
				opts.contains(param3));
	}

	/**
	 * Tests setOptions(), passing a null for options Ref sourceforge bug
	 * 1038722.
	 */
	public void testSetOptionsWNull() {
		CmdLineHandler cl = new BasicCmdLineHandler("MyCmd",
				"copies one file to another",
				new Parameter[] { param1, param2 }, new Parameter[] {});
		cl.setOptions(null);
		Collection<Parameter<?>> opts = cl.getOptions();
		assertEquals("Wrong number of options returned after setOptions()", 0,
				opts.size());
	}
}

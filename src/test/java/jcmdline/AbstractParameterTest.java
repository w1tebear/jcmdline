/*
 * AbstractParameter_UT.java
 *
 * Classes:
 *   public   AbstractParameter_UT
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

import jcmdline.AbstractParameter;
import jcmdline.CmdLineException;
import jcmdline.Parameter;

/**
 * Unit test code for AbstractParameter
 * 
 * @author Lynne Lawrence
 * @version jcmdline Rel. @VERSION@ $Id: AbstractParameter_UT.java,v 1.2
 *          2002/12/07 14:30:49 lglawrence Exp $
 */
public class AbstractParameterTest extends BetterTestCase {

	/**
	 * a Parameter to use for testing
	 */
	private UnitTestParam p;

	/**
	 * constructor takes name of test method
	 * 
	 * @param name
	 *            The name of the test method to be run.
	 */
	public AbstractParameterTest(String name) {
		super(name);
	}

	/**
	 * Runs all tests using junit.textui.TestRunner
	 */
	public static void main(String[] args) {
		doMain(args, AbstractParameterTest.class);
	}

	/**
	 * Sets up data for the test
	 */
	public void setUp() {
		p = new UnitTestParam();
	}

	/**
	 * Tests addValue() with multi valued Parameter
	 */
	public void testAddValueMulti() {
		p.setMultiValued(Parameter.MULTI_VALUED);
		assertTrue("isSet() returns true before value set", !p.isSet());
		String val1 = "First value";
		String val2 = "Second value";

		try {
			p.addValue(val1);
		} catch (Exception e) {
			fail("Unable to add first value to MULTI_VALUED Parameter: "
					+ e.getMessage());
		}
		try {
			p.addValue(val2);
		} catch (Exception e) {
			fail("Unable to add second value to MULTI_VALUED Parameter: "
					+ e.getMessage());
		}
		assertEquals("addValue() did not store first value correctly", val1, p
				.getValue());
		ArrayList<String> vals = new ArrayList<String>(p.getValues());
		assertEquals("getValues() returned wrong nbr of values after adds", 2,
				vals.size());
		assertEquals("getValues() did not store first value correctly", val1,
				vals.get(0));
		assertEquals("getValues() did not store second value correctly", val2,
				vals.get(1));
		assertTrue("isSet() returns false after value set", p.isSet());
	}

	/**
	 * Tests addValue() with single valued Parameter
	 */
	public void testAddValueSingle() {
		p.setMultiValued(Parameter.SINGLE_VALUED);
		assertTrue("isSet() returns true before value set", !p.isSet());
		String val1 = "First value";
		String val2 = "Second value";

		try {
			p.addValue(val1);
		} catch (Exception e) {
			fail("Unable to add first value to SINGLE_VALUED Parameter: "
					+ e.getMessage());
		}
		assertEquals("addValue() did not store first value correctly", val1, p
				.getValue());
		ArrayList<String> vals = new ArrayList<String>(p.getValues());
		assertEquals(
				"getValues() returned wrong nbr of values after first add", 1,
				vals.size());
		assertEquals("getValues() did not store first value correctly", val1,
				vals.get(0));
		assertTrue("isSet() returns false after value set", p.isSet());

		try {
			p.addValue(val2);
			fail("addValue() accepted second value when SINGLE_VALUED");
		} catch (Exception e) {
		}
	}

	/**
	 * Tests setDesc()/getDesc()
	 */
	public void testDesc() {
		String s = "This is the description";
		p.setDesc(s);
		assertEquals("getDesc() returns wrong string", s, p.getDesc());
	}

	/**
	 * Tests setHidden()/getHidden()
	 */
	public void testHidden() {
		p.setHidden(Parameter.HIDDEN);
		assertTrue("getHidden(Parameter.HIDDEN) failed", p.isHidden());
		p.setHidden(Parameter.PUBLIC);
		assertTrue("setHidden(Parameter.PUBLIC) failed", !p.isHidden());
	}

	/**
	 * Tests setMultiValued()/getMultiValued()
	 */
	public void testMultiValued() {
		p.setMultiValued(Parameter.MULTI_VALUED);
		assertTrue("getMultiValued(Parameter.MULTI_VALUED) failed", p
				.isMultiValued());
		p.setMultiValued(Parameter.SINGLE_VALUED);
		assertTrue("setMultiValued(Parameter.SINGLE_VALUED) failed", !p
				.isMultiValued());
	}

	/**
	 * Tests setOptional()/getOptional()
	 */
	public void testOptional() {
		p.setOptional(Parameter.REQUIRED);
		assertTrue("getOptional(Parameter.REQUIRED) failed", !p.isOptional());
		p.setOptional(Parameter.OPTIONAL);
		assertTrue("setOptional(Parameter.OPTIONAL) failed", p.isOptional());
	}

	/**
	 * Tests setValue()
	 */
	public void testSetValue() throws CmdLineException {
		String v = "val1";
		p.setValue(v);
		ArrayList<String> vals = new ArrayList<String>(p.getValues());
		assertEquals("wrong # values after first set of value", 1, vals.size());
		assertEquals("Wrong value set after first try", v, (String) vals.get(0));

		v = "new value";
		p.setValue(v);
		vals = new ArrayList<String>(p.getValues());
		assertEquals("wrong # values after second set of value", 1, vals.size());
		assertEquals("Wrong value set after second try", v, (String) vals
				.get(0));
	}

	/**
	 * Tests setValues() - Array
	 */
	public void testSetValuesArray() throws CmdLineException {
		// Check multivalued
		p.setMultiValued(Parameter.MULTI_VALUED);
		p.addValue("oldval1");
		p.addValue("oldval2");

		String[] vals = new String[] { "val1", "val2" };
		p.setValues(vals);
		ArrayList<String> currVals = new ArrayList<String>(p.getValues());
		assertEquals("Parameter does not contain the right # of values", 2,
				currVals.size());
		assertTrue("Parameter does not contain val1", currVals.contains("val1"));
		assertTrue("Parameter does not contain val2", currVals.contains("val2"));

		// Check single valued
		p = new UnitTestParam();
		try {
			p.setValues(vals);
			fail("setValues() of multiple values worked for single-valued "
					+ "Parameter");
		} catch (Exception e) {
		}
	}

	/**
	 * Tests setValues() - Collection
	 */
	public void testSetValuesCollection() throws CmdLineException {
		// Check multivalued
		p.setMultiValued(Parameter.MULTI_VALUED);
		p.addValue("oldval1");
		p.addValue("oldval2");

		ArrayList<String> vals = new ArrayList<String>();
		vals.add("val1");
		vals.add("val2");
		p.setValues(vals);
		ArrayList<String> currVals = new ArrayList<String>(p.getValues());
		assertEquals("Parameter does not contain the right # of values", 2,
				currVals.size());
		assertTrue("Parameter does not contain val1", currVals.contains("val1"));
		assertTrue("Parameter does not contain val2", currVals.contains("val2"));

		p = new UnitTestParam();
		try {
			p.setValues(vals);
			fail("setValues() of multiple values worked for single-valued "
					+ "Parameter");
		} catch (Exception e) {
		}
	}

	/**
	 * Tests setTag()/getTag()
	 */
	public void testTag() {
		String s = "my_tag-23";
		p.setTag(s);
		assertEquals("getTag() returns wrong string", s, p.getTag());

		try {
			p.setTag("tag with =");
			fail("setTag() accepted a tag containing an = sign");
		} catch (Exception e) {
		}
	}

	/**
	 * A subclass of AbstractParameter for use during testing
	 * 
	 * @author Lynne Lawrence
	 * @version jcmdline Rel. @VERSION@ $Id: AbstractParameter_UT.java,v 1.2
	 *          2002/12/07 14:30:49 lglawrence Exp $
	 */
	class UnitTestParam extends AbstractParameter<String> {
		public UnitTestParam() {
		}

		/**
		 * @see jcmdline.AbstractParameter#convertValue(java.lang.String)
		 */
		@Override
		public String convertValue(String strVal) throws CmdLineException {
			return strVal;
		}
	}
}

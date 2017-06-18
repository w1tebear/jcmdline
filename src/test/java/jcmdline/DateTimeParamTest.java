/*
 * dATEtImeParam_UT.java
 *
 * jcmdline Rel. @VERSION@ $Id: DateTimeParam_UT.java,v 1.3 2009/08/06 14:31:35 lglawrence Exp $
 *
 * Classes:
 *   public   DateTimeParam_UT
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
import jcmdline.DateTimeParam;

/**
 * Unit test code for DateTimeParam.
 * <P>
 * Usage:
 * 
 * <pre>
 *   java DateTimeParam_UT [-debug &lt;n&gt;] [testname [,testname...]]
 *   java DateTimeParam_UT -help
 * </pre>
 * 
 * By default, all tests are run, and debug mode is disabled.
 * 
 * @author Lynne Lawrence
 * @version jcmdline Rel. @VERSION@ $Id: DateTimeParam_UT.java,v 1.2 2002/12/07 14:30:49 lglawrence Exp $
 */
public class DateTimeParamTest extends BetterTestCase {
    private static List<Date> acceptVals;
    private static String sDateFormat = DateTimeParam.getParseFormat();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(sDateFormat);
    private static final String desc = "the date on which the report is to start";
    private static final String tag = "startDate";

    /**
     * constructor takes name of test method
     * 
     * @param name
     *            The name of the test method to be run.
     */
    public DateTimeParamTest(String name) {
        super(name);
    }

    /**
     * Runs all tests using junit.textui.TestRunner
     */
    public static void main(String[] args) {
        doMain(args, DateTimeParamTest.class);
    }

    /**
     * Sets up data for the test
     */
    public void setUp() {
        acceptVals = new ArrayList<Date>();
        Date date1 = new Date();
        acceptVals.add(date1);
        acceptVals.add(new Date(date1.getTime() + (1000l * 5)));
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

        DateTimeParam p = new DateTimeParam(tag, desc);
        p.setAcceptableValues(acceptVals);
        assertEquals("getAcceptableValues() returned wrong values", acceptVals, p.getAcceptableValues());

        p.setValue(acceptVals.get(0)); // should work ok

        boolean gotException = false;
        try {
            p.setValue(new Date(System.currentTimeMillis() - (1000l * 5)));
        } catch (CmdLineException e) {
            gotException = true;
        }
        assertTrue("Did not get exception when setting non-acceptable value", gotException);
    }

    public void testConvertValue() throws Exception {
        DateTimeParam p = new DateTimeParam(tag, desc);

        Date expectedDate = new Date();
        String sDate = dateFormat.format(expectedDate);
        assertEquals("convertValue() returns wrong Date with fully specified date/time", expectedDate,
                p.convertValue(sDate));

        // cut off the millis
        p = new DateTimeParam(tag, desc);
        sDate = sDate.substring(0, sDate.length() - 4);
        expectedDate = dateFormat.parse(sDate + ":000");
        assertEquals("convertValue() returns wrong Date with date/time missing millis", expectedDate,
                p.convertValue(sDate));

        // cut off the secs and millis
        p = new DateTimeParam(tag, desc);
        sDate = sDate.substring(0, sDate.length() - 3);
        expectedDate = dateFormat.parse(sDate + ":00:000");
        assertEquals("convertValue() returns wrong Date with date/time missing secs and millis", expectedDate,
                p.convertValue(sDate));

        try {
            p = new DateTimeParam(tag, desc);
            p.convertValue("some junk");
            fail("convertValue() did not throw CmdLineException with param='some junk'");
        } catch (CmdLineException e) {
            // expected
        }

        p = new DateTimeParam(tag, desc);
        p.setDefaultSeconds(30);
        p.setDefaultMilliSeconds(123);
        expectedDate = dateFormat.parse(sDate + ":30:123");
        assertEquals(
                "convertValue() returns wrong Date with date/time missing secs and millis and " + "default values set",
                expectedDate, p.convertValue(sDate));

    }

    /**
     * Test ctor accepting tag, desc
     */
    public void testCtor1() {
        DateTimeParam dp = new DateTimeParam(tag, desc);
        assertEquals("tag set wrong", tag, dp.getTag());
        assertEquals("desc set wrong", desc, dp.getDesc());
        assertTrue("optional flag not set true by default", dp.isOptional());
        assertTrue("multiValued flag not set false by default", !dp.isMultiValued());
        assertTrue("hidden flag not set false by default", !dp.isHidden());
    }

    /**
     * Test ctor accepting tag, desc, optional
     */
    public void testCtor2() {
        DateTimeParam dp = new DateTimeParam(tag, desc, DateTimeParam.OPTIONAL);
        assertEquals("tag set wrong", tag, dp.getTag());
        assertEquals("desc set wrong", desc, dp.getDesc());
        assertTrue("multiValued flag not set false by default", !dp.isMultiValued());
        assertTrue("hidden flag not set false by default", !dp.isHidden());
        assertTrue("optional flag not set to true", dp.isOptional());

        dp = new DateTimeParam(tag, desc, DateTimeParam.REQUIRED);
        assertTrue("optional flag not set to false", !dp.isOptional());
    }

    /**
     * Test ctor accepting tag, desc, optional, multi-valued
     */
    public void testCtor3() {
        DateTimeParam dp = new DateTimeParam(tag, desc, DateTimeParam.OPTIONAL, DateTimeParam.MULTI_VALUED);
        assertEquals("tag set wrong", tag, dp.getTag());
        assertEquals("desc set wrong", desc, dp.getDesc());
        assertTrue("optional flag not set to true", dp.isOptional());
        assertTrue("multiValued flag not set true", dp.isMultiValued());
        assertTrue("hidden flag not set false by default", !dp.isHidden());

        dp = new DateTimeParam(tag, desc, DateTimeParam.REQUIRED, DateTimeParam.SINGLE_VALUED);
        assertTrue("optional flag not set to false", !dp.isOptional());
        assertTrue("multiValued flag not set false", !dp.isMultiValued());
    }

    /**
     * Test ctor accepting tag, desc, optional, multi-valued, hidden
     */
    public void testCtor4() {
        DateTimeParam dp = new DateTimeParam(tag, desc, DateTimeParam.OPTIONAL, DateTimeParam.MULTI_VALUED,
                DateTimeParam.HIDDEN);
        assertEquals("tag set wrong", tag, dp.getTag());
        assertEquals("desc set wrong", desc, dp.getDesc());
        assertTrue("optional flag not set to true", dp.isOptional());
        assertTrue("multiValued flag not set true", dp.isMultiValued());
        assertTrue("hidden flag not set true", dp.isHidden());

        dp = new DateTimeParam(tag, desc, DateTimeParam.REQUIRED, DateTimeParam.SINGLE_VALUED, DateTimeParam.PUBLIC);
        assertTrue("optional flag not set to false", !dp.isOptional());
        assertTrue("multiValued flag not set false", !dp.isMultiValued());
        assertTrue("hidden flag not set false", !dp.isHidden());
    }

    /**
     * Test ctor accepting tag, desc, acceptableValues
     */
    public void testCtor5() {
        DateTimeParam dp = new DateTimeParam(tag, desc, acceptVals.toArray(new Date[] {}));
        assertEquals("tag set wrong", tag, dp.getTag());
        assertEquals("desc set wrong", desc, dp.getDesc());
        assertEquals("acceptableValues set wrong", acceptVals, dp.getAcceptableValues());
        assertTrue("optional flag not set true by default", dp.isOptional());
        assertTrue("multiValued flag not set false by default", !dp.isMultiValued());
        assertTrue("hidden flag not set false by default", !dp.isHidden());
    }

    /**
     * Test ctor accepting tag, desc, acceptableValues, optional
     */
    public void testCtor6() {
        DateTimeParam dp = new DateTimeParam(tag, desc, acceptVals.toArray(new Date[] {}), DateTimeParam.OPTIONAL);
        assertEquals("tag set wrong", tag, dp.getTag());
        assertEquals("desc set wrong", desc, dp.getDesc());
        assertTrue("optional flag not set to true", dp.isOptional());
        assertEquals("acceptableValues set wrong", acceptVals, dp.getAcceptableValues());
        assertTrue("multiValued flag not set false by default", !dp.isMultiValued());
        assertTrue("hidden flag not set false by default", !dp.isHidden());

        dp = new DateTimeParam(tag, desc, acceptVals.toArray(new Date[] {}), DateTimeParam.REQUIRED);
        assertTrue("optional flag not set to false", !dp.isOptional());
    }

    /**
     * Test ctor accepting tag, desc, acceptableValues, optional, multiValued
     */
    public void testCtor7() {
        DateTimeParam dp = new DateTimeParam(tag, desc, acceptVals.toArray(new Date[] {}), DateTimeParam.OPTIONAL,
                DateTimeParam.MULTI_VALUED);
        assertEquals("tag set wrong", tag, dp.getTag());
        assertEquals("desc set wrong", desc, dp.getDesc());
        assertTrue("optional flag not set to true", dp.isOptional());
        assertTrue("multiValued flag not set to true", dp.isMultiValued());
        assertEquals("acceptableValues set wrong", acceptVals, dp.getAcceptableValues());
        assertTrue("hidden flag not set false by default", !dp.isHidden());

        dp = new DateTimeParam(tag, desc, acceptVals.toArray(new Date[] {}), DateTimeParam.REQUIRED,
                DateTimeParam.SINGLE_VALUED);
        assertTrue("optional flag not set to false", !dp.isOptional());
        assertTrue("multiValued flag not set to false", !dp.isMultiValued());
    }

    /**
     * Test ctor accepting tag, desc, acceptableValues, optional, multiValued, hidden
     */
    public void testCtor8() {
        DateTimeParam dp = new DateTimeParam(tag, desc, acceptVals.toArray(new Date[] {}), DateTimeParam.OPTIONAL,
                DateTimeParam.MULTI_VALUED, DateTimeParam.HIDDEN);
        assertEquals("tag set wrong", tag, dp.getTag());
        assertEquals("desc set wrong", desc, dp.getDesc());
        assertTrue("optional flag not set to true", dp.isOptional());
        assertTrue("multiValued flag not set to true", dp.isMultiValued());
        assertTrue("hidden flag not set to true", dp.isHidden());
        assertEquals("acceptableValues set wrong", acceptVals, dp.getAcceptableValues());

        dp = new DateTimeParam(tag, desc, acceptVals.toArray(new Date[] {}), DateTimeParam.REQUIRED,
                DateTimeParam.SINGLE_VALUED, DateTimeParam.PUBLIC);
        assertTrue("optional flag not set to false", !dp.isOptional());
        assertTrue("multiValued flag not set to false", !dp.isMultiValued());
        assertTrue("hidden flag not set to false", !dp.isHidden());
    }

    /**
     * Tests set/getDefaultMilliSeconds()
     */
    public void testSetDefaultMilliSeconds() throws Exception {
        DateTimeParam p = new DateTimeParam(tag, desc);
        p.setDefaultMilliSeconds(30);
        assertEquals("getDefaultMilliSeconds() returned wrong value", 30, p.getDefaultMilliSeconds());
    }

    /**
     * Tests set/getDefaultSeconds()
     */
    public void testSetDefaultSeconds() throws Exception {
        DateTimeParam p = new DateTimeParam(tag, desc);
        p.setDefaultSeconds(30);
        assertEquals("getDefaultSeconds() returned wrong value", 30, p.getDefaultSeconds());
    }
}

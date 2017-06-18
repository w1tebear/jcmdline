/*
 * StringFormatHelper_UT.java
 *
 * jcmdline Rel. @VERSION@ $Id: StringFormatHelper_UT.java,v 1.3 2009/08/06 14:31:35 lglawrence Exp $
 *
 * Classes:
 *   public   StringFormatHelper_UT
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

import jcmdline.StringFormatHelper;

/**
 * Unit test code for StringFormatHelper.
 * <P>
 * Usage:
 * 
 * <pre>
 *   java StringFormatHelper_UT [-debug &lt;n&gt;] [testname [,testname...]]
 *   java StringFormatHelper_UT -help
 * </pre>
 * 
 * By default, all tests are run, and debug mode is disabled.
 * 
 * @author Lynne Lawrence
 * @version jcmdline Rel. @VERSION@ $Id: StringFormatHelper_UT.java,v 1.2 2002/12/07 14:30:49 lglawrence Exp $
 */
public class StringFormatHelperTest extends BetterTestCase {

    /**
     * a StringFormatHelper to use during tests
     */
    private static final StringFormatHelper sHelper = StringFormatHelper.getHelper();

    /**
     * constructor takes name of test method
     * 
     * @param name
     *            The name of the test method to be run.
     */
    public StringFormatHelperTest(String name) {
        super(name);
    }

    /**
     * Runs all tests using junit.textui.TestRunner
     */
    public static void main(String[] args) {
        doMain(args, StringFormatHelperTest.class);
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
     * Test format() of String with no breaks in it
     */
    public void testBlockedText() {
        String s = "This is the String to be used for my test";
        String expected = "     This is\n     the String\n     to be used\n" + "     for my\n     test";
        assertEquals("formatBlockedText(s, 5, 15) failed", expected, sHelper.formatBlockedText(s, 5, 15));
    }

    /**
     * Tests breakString() on a String that is empty
     */
    public void testBreakStringEmpty() {
        String s = "";
        String[] a = sHelper.breakString(s, 10);
        assertEquals("breakString returned wrong first String", "", a[0]);
        assertNull("breakString returned wrong remainder String", a[1]);
    }

    /**
     * Tests breakString() with a newline past the maxLen
     */
    public void testBreakStringLongNL() {
        String s = "This is my test\n String";
        String[] a = sHelper.breakString(s, 10);
        assertEquals("breakString returned wrong first String", "This is my\n", a[0]);
        assertEquals("breakString returned wrong remainder String", "test\n String", a[1]);
    }

    /**
     * Tests breakString() on a String with no newline
     */
    public void testBreakStringNoNewLine() {
        String s = "This is my test String";
        String[] a = sHelper.breakString(s, 10);
        assertEquals("breakString returned wrong first String", "This is my\n", a[0]);
        assertEquals("breakString returned wrong remainder String", "test String", a[1]);
        s = "This is my      test String";
        a = sHelper.breakString(s, 10);
        assertEquals("breakString returned wrong first String " + "when multiple spaces at break point", "This is my\n",
                a[0]);
        assertEquals("breakString returned wrong remainder String " + "when multiple spaces at break point",
                "test String", a[1]);
    }

    /**
     * Tests breakString() on a String that is null
     */
    public void testBreakStringNull() {
        String s = null;
        String[] a = sHelper.breakString(s, 10);
        assertEquals("breakString returned wrong first String", "", a[0]);
        assertNull("breakString returned wrong remainder String", a[1]);
    }

    /**
     * Tests breakString() on a String shorter than maxLen
     */
    public void testBreakStringShort() {
        String s = "Hi there!";
        String[] a = sHelper.breakString(s, 10);
        assertEquals("breakString returned wrong first String", "Hi there!", a[0]);
        assertNull("breakString returned wrong remainder String", a[1]);
    }

    /**
     * Tests breakString() on a String shorter than maxLen, ending with a newline
     */
    public void testBreakStringShortNewLine() {
        String s = "Hi there!\n";
        String[] a = sHelper.breakString(s, 10);
        assertEquals("breakString returned wrong first String", "Hi there!\n", a[0]);
        assertNull("breakString returned wrong remainder String", a[1]);
    }

    /**
     * Tests breakString() on a String with multiple spaces at the break point
     */
    public void testBreakStringWMultSpaces() {
        String s = "This is my      test String";
        String[] a = sHelper.breakString(s, 10);
        assertEquals("breakString returned wrong first String", "This is my\n", a[0]);
        assertEquals("breakString returned wrong remainder String", "test String", a[1]);
    }

    /**
     * Tests breakString() on a String with a newline before maxLen
     */
    public void testBreakStringWNewLine() {
        String s = "This is\n  my test String";
        String[] a = sHelper.breakString(s, 10);
        assertEquals("breakString returned wrong first String", "This is\n", a[0]);
        assertEquals("breakString returned wrong remainder String", "  my test String", a[1]);
    }

    /**
     * Tests formatLabeledList()
     */
    public void testFormatLabeledList() {
        String[] labels = new String[] { "label1", "label2" };
        String[] texts = new String[] { "This is the description for label1",
                "This is the longer and more beautiful description for label2" };
        String expected = "label1 : This is the description for label1\n"
                + "label2 : This is the longer and more beautiful description\n" + "         for label2\n";
        assertEquals("formatLabeledList() returned wrong string", expected,
                sHelper.formatLabeledList(labels, texts, " : ", 10, 60));
    }

    /**
     * Tests formatLabeledList() with a long label
     */
    public void testFormatLabeledList2() {
        String[] labels = new String[] { "label1", "label2IsaLongLabel" };
        String[] texts = new String[] { "This is the description for label1",
                "This is the longer and more beautiful description for label2" };
        String expected = "label1 : This is the description for label1\n" + "label2IsaLongLabel : \n"
                + "         This is the longer and more beautiful description\n" + "         for label2\n";
        assertEquals("formatLabeledList() returned wrong string", expected,
                sHelper.formatLabeledList(labels, texts, " : ", 10, 60));
    }

    /**
     * Test formatHangingIndent() of String with exactly maxLength characters
     */
    public void testFormatMaxLen() {
        String s = "this is a string";
        String newS = sHelper.formatHangingIndent(s, 5, s.length());
        assertEquals("format of String of length = maxLength failed", s, newS);
    }

    /**
     * Test formatHangingIndent() of String with exactly maxLength characters, followed by a newline
     */
    public void testFormatMaxLenWNL() {
        String s = "this is a string\n";
        String newS = sHelper.formatHangingIndent(s, 5, s.length() - 1);
        assertEquals("format of String of length = maxLength failed", s, newS);
    }

    /**
     * Test format() of String with no breaks in it
     */
    public void testFormatNoBreak() {
        String s = "this_is_a_string_next_line";
        String newS = sHelper.formatHangingIndent(s, 5, 16);
        assertEquals("format of String of length = maxLength failed", "this_is_a_string\n     _next_line", newS);
    }

    /**
     * Test formatHangingIndent() of String with a word ending at maxLength
     */
    public void testFormatWordAtMaxLen() {
        String s = "this is a string next line";
        String newS = sHelper.formatHangingIndent(s, 5, 16);
        assertEquals("format of String of length = maxLength failed", "this is a string\n     next line", newS);
    }

    /**
     * Test formatHangingIndent() of String with newlines in the final line
     */
    public void testNLinFinalLine() {
        String s = "These are the options:\n  opt1\n  opt2";
        // Returned string should have the indent # spaces(3) + the number of
        // specified spaces before each option
        String expS = "These are the options:\n     opt1\n     opt2";
        String newS = sHelper.formatHangingIndent(s, 3, 80);
        assertEquals("wrong string returned", expS, newS);
    }
}

/*
 * TextUsageFormatter_UT.java
 *
 * jcmdline Rel. @VERSION@ $Id: TextUsageFormatter_UT.java,v 1.3 2009/08/06 14:31:35 lglawrence Exp $
 *
 * Classes:
 *   public   TextUsageFormatter_UT
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
import java.util.StringTokenizer;

import jcmdline.Parameter;
import jcmdline.StringParam;
import jcmdline.TextUsageFormatter;

/**
 * Unit test code for TextUsageFormatter.
 * <P>
 * Usage:
 * 
 * <pre>
 *   java TextUsageFormatter_UT [-log &lt;level&gt;] [testname [,testname...]]
 *   java TextUsageFormatter_UT -help
 * </pre>
 * 
 * By default, all tests are run, and debug mode is disabled.
 * 
 * @author Lynne Lawrence
 * @version jcmdline Rel. @VERSION@ $Id: TextUsageFormatter_UT.java,v 1.2 2002/12/07 14:30:49 lglawrence Exp $
 */
public class TextUsageFormatterTest extends BetterTestCase {

    private ArrayList<Parameter<?>> args;
    private TextUsageFormatter formatter;
    private HashMap<String, Parameter<?>> opts;
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
    public TextUsageFormatterTest(String name) {
        super(name);
    }

    /**
     * Runs all tests using junit.textui.TestRunner
     */
    public static void main(String[] args) {
        doMain(args, TextUsageFormatterTest.class);
    }

    /**
     * Sets up data for the test
     */
    public void setUp() {
        opts = new HashMap<String, Parameter<?>>(2);
        opts.put(param1.getTag(), param1);
        opts.put(param2.getTag(), param2);
        args = new ArrayList<Parameter<?>>();
        args.add(param3);
        args.add(param4);
        formatter = new TextUsageFormatter();
    }

    /**
     * Undoes all that was done in setUp, clean up after test
     */
    public void tearDown() {
    }

    /**
     * Tests formatText().
     */
    public void testFormatText() {

        // 1 2 3 4 5
        // 12345678901234567890123456789012345678901234567890
        String s = "This is a test line to use in testing formatText()";

        String ret = formatter.formatText(s, 0, 80);
        assertEquals("formatText(s, 0, 80) failed", s, ret);
        ret = formatter.formatText(s, 0, 20);
        assertEquals("formatText(s, 0, 20) failed", "This is a test line\nto use in testing\nformatText()", ret);
        ret = formatter.formatText(s, 10, 20);
        assertEquals("formatText(s, 10, 20) failed", "          This is a\n          test line\n"
                + "          to use in\n          testing\n" + "          formatText\n          ()", ret);
    }

    /**
     * Tests formatUsage with a long name to ensure the wrap works.
     */
    public void testFormatUsageLongName() {
        HashMap<String, Parameter<?>> emptyOpts = new HashMap<String, Parameter<?>>();
        String s = formatter.formatUsage("java -jar mof2rdf-0.0.1-SNAPSHOT.jar", //
                "converts a UML file to an OWL RDF ontology", emptyOpts, args, false);
        debug("Usage with long name:\n" + s);
    }

    /**
     * Tests formatUsage on when the list of arguments is empty. Just tests that it won't bomb off. To see the actual
     * message, run with "-log FINE".
     */
    public void testFormatUsageNoArgs() {
        ArrayList<Parameter<?>> emptyArgs = new ArrayList<Parameter<?>>();
        String s = formatter.formatUsage("name", "cmd desc", opts, emptyArgs, false);
        debug("Usage with no arguments:\n" + s);
    }

    /**
     * Tests formatUsage on when the list of options is empty. Just tests that it won't bomb off. To see the actual
     * message, run with "-log FINE".
     */
    public void testFormatUsageNoOpts() {
        HashMap<String, Parameter<?>> emptyOpts = new HashMap<String, Parameter<?>>();
        String s = formatter.formatUsage("name", "cmd desc", emptyOpts, args, false);
        debug("Usage with no options:\n" + s);
    }

    /**
     * Tests formatUsage on when the list of options and the list of arguments are empty. Just tests that it won't bomb
     * off. To see the actual message, run with "-log FINE".
     */
    public void testFormatUsageNoOptsNoArgs() {
        HashMap<String, Parameter<?>> emptyOpts = new HashMap<String, Parameter<?>>();
        ArrayList<Parameter<?>> emptyArgs = new ArrayList<Parameter<?>>();
        String s = formatter.formatUsage("name", "cmd desc", emptyOpts, emptyArgs, false);
        debug("Usage with no options or args:\n" + s);
    }

    /**
     * Tests set/getLineLength()
     */
    public void testSetLineLength() {
        int len = 50;
        formatter.setLineLength(len);
        assertEquals("getLineLength() did not return correct value", len, formatter.getLineLength());
        String s = formatter.formatUsage("name", "cmd desc", opts, args, false);
        debug("Usage with max line length = " + len + ":\n" + s);

        StringTokenizer st = new StringTokenizer(s, "\n");
        String usageLine;
        while (st.hasMoreTokens()) {
            usageLine = st.nextToken();
            if (usageLine.length() > len) {
                fail("Usage line contains more than " + len + " chars:\n" + usageLine);
            }
        }
    }
}

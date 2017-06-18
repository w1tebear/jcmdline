/*
 * BooleanParam_UT.java
 *
 * Classes:
 *   public   BooleanParam_UT
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

import jcmdline.BooleanParam;
import jcmdline.CmdLineException;

/**
 * Unit test code for BooleanParam
 *
 * @author          Lynne Lawrence
 * @version         jcmdline Rel. @VERSION@ $Id: BooleanParam_UT.java,v 1.3 2009/08/06 14:31:35 lglawrence Exp $
 */
public class BooleanParamTest extends BetterTestCase {

    /**
     * constructor takes name of test method
     *
     * @param name          The name of the test method to be run.
     */
    public BooleanParamTest(String name) {
        super(name);
    }

    /**
     * Runs all tests using junit.textui.TestRunner
     */
    public static void main (String[] args) {
        doMain(args, BooleanParamTest.class);
    }

    /**
     * Tests addValue()
     */
    public void testAddValue() throws CmdLineException {
        BooleanParam p = new BooleanParam("mytag", "mydesc");
        assertTrue("BooleanParam not initialized to 'false'",
               !p.isTrue());
        p.addValue(Boolean.TRUE);
        assertTrue("setValue(Boolean.TRUE) failed",
               p.isTrue());

        p = new BooleanParam("mytag", "mydesc");
        try {
            p.addValue(null);
            fail("setValue(null) did not fail");
        } catch (CmdLineException e) {
            debug("addValue(null) error: " + e.getMessage());
        }
    }

    /**
     * Test constructor taking tag, desc
     */
    public void testConst1() {
        String tag = "mytag";
        String desc = "this is the desc";
        BooleanParam p = new BooleanParam(tag, desc);
        assertEquals("tag not set correctly",
                     tag, p.getTag());
        assertEquals("desc not set correctly",
                     desc, p.getDesc());
        assertTrue("optional flag not set correctly",
               p.isOptional());
        assertTrue("multiValued flag not set correctly",
               !p.isMultiValued());
        assertTrue("hidden flag not set correctly",
               !p.isHidden());
    }
    
    /**
     * Test constructor taking tag, desc, hidden
     */
    public void testConst2() {
        String tag = "mytag";
        String desc = "this is the desc";
        BooleanParam p = new BooleanParam(tag, desc, BooleanParam.HIDDEN);
        assertEquals("tag not set correctly",
                     tag, p.getTag());
        assertEquals("desc not set correctly",
                     desc, p.getDesc());
        assertTrue("multiValued flag not set correctly",
               !p.isMultiValued());
        assertTrue("optional flag not set correctly",
               p.isOptional());
        assertTrue("hidden flag not set correctly to BooleanParam.HIDDEN",
               p.isHidden());
        p = new BooleanParam(tag, desc, BooleanParam.PUBLIC);
        assertTrue("hidden flag not set correctly to BooleanParam.PUBLIC",
               !p.isHidden());
    }

    /**
     * Tests setValue()
     */
    public void testSetValue() throws CmdLineException {
        BooleanParam p = new BooleanParam("mytag", "mydesc");
        assertTrue("BooleanParam not initialized to 'false'",
               !p.isTrue());
        p.setValue(Boolean.TRUE);
        assertTrue("setValue(Boolean.TRUE) failed",
               p.isTrue());

        p = new BooleanParam("mytag", "mydesc");
        try {
            p.setValue(null);
            fail("setValue(null) did not fail");
        } catch (CmdLineException e) {
            debug("addValue(null) error: " + e.getMessage());
        }
    }
}

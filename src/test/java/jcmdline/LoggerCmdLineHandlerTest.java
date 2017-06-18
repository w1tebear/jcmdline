/*
 * LoggerCmdLineHandler_UT.java
 *
 * jcmdline Rel. @VERSION@ $Id: LoggerCmdLineHandler_UT.java,v 1.3 2009/08/06 14:31:35 lglawrence Exp $
 *
 * Classes:
 *   public   LoggerCmdLineHandler_UT
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

import java.io.ByteArrayOutputStream;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import jcmdline.LoggerCmdLineHandler;
import jcmdline.Parameter;
import jcmdline.StringParam;

/**
 * Unit test code for LoggerCmdLineHandler.
 * <P>
 * Usage:
 * <pre>
 *   java LoggerCmdLineHandler_UT [-debug <n>] [testname [,testname...]]
 *   java LoggerCmdLineHandler_UT -help
 * </pre>
 * By default, all tests are run, and debug mode is disabled.
 *
 * @author          Lynne Lawrence
 * @version         jcmdline Rel. @VERSION@ $Id: LoggerCmdLineHandler_UT.java,v 1.3 2009/08/06 14:31:35 lglawrence Exp $
 */
public class LoggerCmdLineHandlerTest extends BetterTestCase {

    private static final String cmdname = "mycmd";
    private static final String cmddesc = "this is the cmd desc";

    // variables new for each test
    private StringParam param1 = 
        new StringParam("param1", "this is param1");
    private StringParam param2 = 
        new StringParam("param2", "this is param2");

    /**
     * constructor takes name of test method
     *
     * @param name          The name of the test method to be run.
     */
    public LoggerCmdLineHandlerTest(String name) {
        super(name);
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
     * Tests the constructor and setting a logging level
     */

    public void testCtor() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        LoggerCmdLineHandler h = new LoggerCmdLineHandler(
            stream, cmdname, cmddesc, 
            new Parameter[] { param1 },
            new Parameter[] { param2 });

        Level level = Level.FINE;
        Level lesserLevel = Level.FINER;
        Level greaterLevel = Level.CONFIG;

        h.parse(new String[] { "-log", level.getLocalizedName() });

        Logger logger = Logger.getLogger(""); 
        logger.log(level, "log message at set level");
        assertEquals("The Logger level did not get set correctly",
                     level, logger.getLevel());
        flushHandlers(logger);
        assertTrue("log message at set level did not come through",
                   stream.size() > 0);
        flushHandlers(logger);
        stream.reset();

        // Try message of lesser level - should not go through
        logger.log(lesserLevel, "log message at lesser level");
        flushHandlers(logger);
        assertTrue("log message at lesser level came through",
                   stream.size() == 0);

        // Try message of greater level - should go through
        logger.log(greaterLevel, "log message at greater level");
        flushHandlers(logger);
        assertTrue("log message at greater level did not come through",
                   stream.size() > 0);
        flushHandlers(logger);
        stream.reset();
    }

    /**
     * Flushes all Handlers associated with the specified Logger.
     *
     * @param logger        the Logger whose Handlers are to be flushed
     */
    private static void flushHandlers(Logger logger) {
        Handler[] h = logger.getHandlers();
        for (int i = 0; i < h.length; i++) {
            h[i].flush();
        }
    }

    /**
     * Runs all tests using junit.textui.TestRunner
     */
    public static void main (String[] args) {
        doMain(args, LoggerCmdLineHandlerTest.class);
    }
}

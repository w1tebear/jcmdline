/*
 * BetterTestCase.java
 *
 * Classes:
 *   public   BetterTestCase
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

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Formatter;
import java.util.logging.Logger;

import jcmdline.HelpCmdLineHandler;
import jcmdline.LoggerCmdLineHandler;
import jcmdline.Parameter;
import jcmdline.StringParam;
import jcmdline.VersionCmdLineHandler;

import java.util.logging.LogRecord;
import java.util.ResourceBundle;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public abstract class BetterTestCase extends TestCase {

	/**
	 * Temporary data root dir. Intended to be used by UT to store all test data
	 * under test/tmp
	 */
	public static String TEST_DATA_DIR = "test" + File.separator + "tmp";

	/**
	 * indicates whether verbose debug information should be printed
	 */
	protected static boolean debugMode = false;

	/**
	 * Named Logger to use for debug messages
	 */
	private static final Logger debugLogger = Logger.getLogger("UT");

	/**
	 * the beginning of the string returned when a key is not defined in the
	 * strings.properties resource file
	 */
	private static final String missingMsg = ResourceBundle.getBundle(
			"strings").getString("Strings.missingKey");

	/**
	 * constructor takes name of test method
	 * 
	 * @param name
	 *            The name of the test method to be run.
	 */
	public BetterTestCase(String name) {
		super(name);
	}

	protected static void checkForMissingString(String s) {
		debug("checkForMissingString starting: " + s);
		if (s.startsWith(missingMsg)) {
			fail("String missing from strings.properties file:\n" + s);
		}
	}

	/**
	 * Logs a String with Level FINE using the root Logger
	 * 
	 * @param msg
	 *            the String to be printed.
	 */
	protected static void debug(String msg) {
		debugLogger.fine(msg);
	}

	/**
	 * A default main() implementation. This implementation uses CmdLineParser
	 * to accept debug levels and an optional list of test names. If no test
	 * names are passed as arguments, all tests in the class will be run.
	 * <P>
	 * The '-debug' command line option sets the root logging level to the
	 * corresponding level, where 1 = FINEST ... 7 = SEVERE. If not specified, 7
	 * (SEVERE) is the default. A ConsoleHandler is installed so that log
	 * records of the appropriate level will be written to stderr.
	 * <P>
	 * This method is intended to be called from a subclass's main() method as
	 * follows (for class JobAssignment_UT):
	 * 
	 * <pre>
	 * public static void main(String[] args) {
	 * 	doMain(args, JobAssignment_UT.class);
	 * }
	 * </pre>
	 * 
	 * @param args
	 *            the command line arguments passed to main()
	 * @param c
	 *            a class object representing the implementing class
	 */
	protected static void doMain(String[] args, Class<?> c) {
		String cName = baseClassName(c.getName());
		String classUnderTest = cName.endsWith("_UT") ? cName.substring(0,
				cName.length() - 3) : cName;

		Method[] methods = c.getMethods();
		Method m;
		ArrayList<String> testNames = new ArrayList<String>();
		for (int i = 0; i < methods.length; i++) {
			m = (Method) methods[i];
			if (m.getName().startsWith("test")) {
				testNames.add(m.getName());
			}
		}
		StringParam pTests = new StringParam("test",
				"the name(s) of a specific test(s) to be run",
				(String[]) testNames.toArray(new String[0]),
				StringParam.OPTIONAL, StringParam.MULTI_VALUED);
		StringBuffer helpText = new StringBuffer();
		helpText.append("Runs the unit test for class ").append(classUnderTest)
				.append(".\n\n").append(
						"If desired, specific tests can be run by specifying ")
				.append("them by name on the command line.  Valid test names ")
				.append("are:");
		for (String testName : testNames) {
			helpText.append("\n     ").append(testName);
		}
		helpText.append("\n\nSpecification of '-log' will generate more ")
				.append("verbose output by setting the Logger level.  ")
				.append("Log messages will ").append(
						"be written to stdout.  The unit tests themselves ")
				.append("log trace type information at log level FINE.");
		LoggerCmdLineHandler clp = new LoggerCmdLineHandler(
				System.out,
				new HelpCmdLineHandler(
						helpText.toString(),
						new VersionCmdLineHandler(
								"jcmdline Rel. @VERSION@ $Id: BetterTestCase.java,v 1.3 2009/08/06 14:31:35 lglawrence Exp $",
								cName, "tests class " + classUnderTest,
								new Parameter[] {}, new Parameter[] { pTests })));
		clp.setLogFormatter(new BetterLogFormatter());
		clp.parse(args);

		TestSuite suite = new TestSuite();
		if (pTests.isSet()) {
			try {
				Constructor<?> ctor = c
						.getConstructor(new Class[] { String.class });
				for (Iterator<String> itr = pTests.getValues().iterator(); itr
						.hasNext();) {
					suite.addTest((TestCase) ctor
							.newInstance(new Object[] { itr.next() }));
				}
			} catch (Exception e) {
				e.printStackTrace();
				fail("Exception creating test cases");
			}
		} else {
			suite.addTest(new TestSuite(c));
		}
		junit.textui.TestRunner.run(suite);
	}

	/**
	 * A convenience method that returns the base portion (that which follows
	 * the last '.', or '$' in case of a nested class) of this object's class
	 * name.
	 * 
	 * @return the base class name
	 */
	private static String baseClassName(String cName) {
		String name = cName.substring(cName.lastIndexOf(".") + 1);
		name = name.substring(name.lastIndexOf("$") + 1);
		return name;
	}

	/**
	 * Called by a subclass to create an temporary directory in which to hold
	 * data. This is typically called in the setUp() method, as in:
	 * 
	 * <code>
	 *    public void setUp() {
	 *        // tmpDir being a static variable in class MyClassName
	 *        tmpDir = createTempDir(MyClass_UT.class);
	 *    }
     * </code> Should this method be used in setUp(), a complementary call
	 * should appear in tearDown:
	 * 
	 * 
	 * A temporary directory is created in "test" + File.separator + "tmp", with
	 * the name of MyClass_UT.
	 * 
	 * @param myClass
	 *            the subclass
	 * @throws Exceptions
	 * @see #deleteDir(File) deleteDir
	 */
	protected File createTempDir(Class<?> myClass) {
		File tmpDir = new File(TEST_DATA_DIR, baseClassName(myClass.getName()));
		if (tmpDir.exists()) {
			deleteDir(tmpDir);
		}
		if (!tmpDir.mkdirs()) {
			fail("Unable to create temp dir '" + tmpDir.getAbsolutePath()
					+ "': ");
		}
		return tmpDir;
	}

	/**
	 * Deletes a directory, and all of its contents, unless
	 * <code>debugMode</code> is set.
	 * 
	 * @param dir
	 *            the directory to be deleted. line of desc is indented on next
	 *            line.
	 * @return Description of return values.
	 */
	protected void deleteDir(File dir) {
		if (debugMode) {
			debug("Leaving temp files in " + dir.getAbsolutePath());
		} else {
			if (dir == null || !dir.exists()) {
				return;
			}
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDir(files[i]);
				} else {
					files[i].delete();
				}
			}
			dir.delete();
		}
	}

	/**
	 * A Logger Formatter class that always prints the record on a single line
	 * (unless the message itself contains a newline).
	 * 
	 * @author Lynne Lawrence
	 * @version jcmdline Rel. @VERSION@ $Id: BetterTestCase.java,v 1.2
	 *          2002/12/07 14:30:49 lglawrence Exp $
	 * @see Image
	 */
	static class BetterLogFormatter extends Formatter {
		public String format(LogRecord rec) {
			SimpleDateFormat dateFmt = new SimpleDateFormat(
					"MM/dd/yy HH:mm:ss:SSS");
			return dateFmt.format(new Date(rec.getMillis())) + "|"
					+ rec.getLoggerName()
					+ "|"
					+
					// strip off the package name to shorten message
					rec.getSourceClassName().substring(8) + "|"
					+ rec.getSourceMethodName() + "|" + rec.getThreadID() + "|"
					+ rec.getLevel() + "|" + rec.getMessage() + "\n";
		}
	}
}

/*
 * Sample2.java
 *
 * jcmdline Rel. @VERSION@ $Id: Sample2.java,v 1.3 2009/08/06 14:31:35 lglawrence Exp $
 *
 * Classes:
 *   public   Sample2
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

import java.util.Collection;
import java.util.List;

import jcmdline.BooleanParam;
import jcmdline.CmdLineHandler;
import jcmdline.DateParam;
import jcmdline.DateTimeParam;
import jcmdline.FileParam;
import jcmdline.HelpCmdLineHandler;
import jcmdline.IntParam;
import jcmdline.Parameter;
import jcmdline.StringParam;
import jcmdline.TimeParam;
import jcmdline.VersionCmdLineHandler;

/**
 * A "junk class" used to test CmdLineParser.
 * 
 * @author Lynne Lawrence
 * @version jcmdline Rel. @VERSION@ $Id: Sample2.java,v 1.2 2002/12/07 14:30:49
 *          lglawrence Exp $
 */
public class Sample2 {

	/**
	 * The arguments this program takes
	 */
	private static final Parameter<?>[] arguments = new Parameter<?>[] {
			new FileParam("old_file",
					"the name of the file to copy - this file"
							+ " must already exist, be readable, and end "
							+ "with '.html'", FileParam.IS_FILE
							& FileParam.IS_READABLE, FileParam.REQUIRED),
			new FileParam("new_file", "the name of the file to copy to",
					FileParam.REQUIRED),
			new FileParam("copies",
					"optional copies to be made of old_file, in addition "
							+ "to new_file; files may not already exist",
					FileParam.DOESNT_EXIST, FileParam.OPTIONAL,
					FileParam.MULTI_VALUED) };

	/**
	 * The help text for this program
	 */
	private static final String helpText = "This command is used to copy an html file, old_file, to a new file, "
			+ "new_file.\n\n"
			+ "Optionally, if the '-delete' option is specified, the original file"
			+ " will be deleted.  It is also possible to preserve the contents of "
			+ "the destination file.  To preserve the destination file, if it "
			+ "exists, use the '-save' option\n\n"
			+ "Additional copies of old_file can be made by specifying <copies>. "
			+ "Files associated with filenames specified as copies must not "
			+ "exist\n\n"
			+ "Permissions for the new file may be specified using the '-perms' "
			+ "option.  If '-perms' is not specified, the permissions will be set "
			+ "to the same as the source file.\n\nIn order to test the "
			+ "functionality of lots of different options, the following options "
			+ "are supported:"
			+ "\n  -int1"
			+ "\n  -int2"
			+ "\n  -int3"
			+ "\n  -int4"
			+ "\n  -int5"
			+ "\n  -bool1"
			+ "\n  -bool2"
			+ "\n  -string1"
			+ "\n  -string2"
			+ "\n  -string3"
			+ "\n  -file1"
			+ "\n  -file2" + "\n  -file3";

	/**
	 * The options available for this program
	 */
	private static final Parameter<?>[] opts = new Parameter<?>[] {
			new IntParam("int1", "an optional int param"),
			new IntParam("int2",
					"an optional int param with min and max values"
							+ " of -3 and 50, respectively", -3, 50),
			new IntParam("int3", "an optional, multivalued int param",
					IntParam.OPTIONAL, IntParam.MULTI_VALUED),
			new IntParam("int4", "an optional, multivalued, hidden int param",
					IntParam.OPTIONAL, IntParam.MULTI_VALUED, IntParam.HIDDEN),
			new IntParam("int5", "a required int param", FileParam.REQUIRED),
			new BooleanParam("bool1", "an optional boolean parameter"),
			new BooleanParam("bool2", "an optional, hidden, boolean parameter",
					BooleanParam.HIDDEN),
			new StringParam("string1", "an optional string option"),
			new StringParam("string2",
					"a required string option with a set of acceptable "
							+ "values that include str1, str2, and str3",
					new String[] { "str1", "str2", "str3" },
					StringParam.REQUIRED),
			new StringParam("string3",
					"an optional string option that wants a string with "
							+ "min and max lengths of 3 and 5, respectively",
					3, 5),
			new FileParam("file1", "an optional file parameter"),
			new FileParam("file2",
					"an optional file parameter that must be a directory",
					FileParam.IS_DIR),
			new FileParam("file3", "an optional, hidden file parameter",
					FileParam.NO_ATTRIBUTES, FileParam.OPTIONAL,
					FileParam.SINGLE_VALUED, FileParam.HIDDEN),
			new DateParam("startDate", "the start date for the report"),
			new DateTimeParam("dateTime", "the report date and time"),
			new TimeParam("time", "the time of the report"), };

	/**
	 * main - doesn't actually do anything but process command line paramters
	 * and print them out.
	 * 
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		/*
		 * CmdLineParser cl = new CmdLineParser( "Sample2",
		 * "a class to test command line parameters", "V 5.2", helpText, opts,
		 * arguments);
		 */
		CmdLineHandler cl = new VersionCmdLineHandler("V 5.2",
				new HelpCmdLineHandler(helpText, "Sample2",
						"a class to test command line parameters", opts,
						arguments));
		cl.parse(args);
		FileParam oldfile = (FileParam) cl.getArg("old_file");
		if (!oldfile.getValue().getPath().endsWith(".html")) {
			cl.exitUsageError("<old_file> must end with '.html'");
		}

		Collection<Parameter<?>> nopts = cl.getOptions();
		Collection<Parameter<?>> nargs = cl.getArgs();
		String valString;

		System.out.println("\n\nArgs:\n");
		for (Parameter<?> p : nargs) {
			List<?> vals = p.getValues();
			valString = "";
			for (Object o : vals) {
				valString += " '" + o.toString() + "'";
			}
			System.out.println("  " + p.getTag() + ": " + valString);
		}
		System.out.println("\n\nOptions:\n");
		for (Parameter<?> p : nopts) {
			List<?> vals = p.getValues();
			valString = "";
			for (Object o : vals) {
				valString += " '" + o.toString() + "'";
			}
			System.out.println("  " + p.getTag() + ": " + valString);
		}
	}
}

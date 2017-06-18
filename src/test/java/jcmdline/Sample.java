package jcmdline;

import java.io.File;
import java.io.InputStream;

import jcmdline.BooleanParam;
import jcmdline.CmdLineHandler;
import jcmdline.FileParam;
import jcmdline.HelpCmdLineHandler;
import jcmdline.Parameter;
import jcmdline.StringParam;
import jcmdline.VersionCmdLineHandler;

public class Sample {
	public static void main(String[] args) {

		// command line arguments
		StringParam patternArg = new StringParam("pattern",
				"the pattern to match", StringParam.REQUIRED);
		FileParam filesArg = new FileParam("file",
				"a file to be processed - defaults to stdin", FileParam.IS_FILE
						& FileParam.IS_READABLE, FileParam.OPTIONAL,
				FileParam.MULTI_VALUED);

		// command line options
		BooleanParam ignorecaseOpt = new BooleanParam("ignoreCase",
				"ignore case while matching");
		BooleanParam listfilesOpt = new BooleanParam("listFiles",
				"list filenames containing pattern");

		// a help text because we will use a HelpCmdLineHandler so our
		// command will display verbose help with the -help option
		String helpText = "This command prints to stdout all lines within "
				+ "the specified files that contain the specified "
				+ "pattern.\n\n"
				+ "Optionally, the matching may be done without "
				+ "regard to case (using the -ignorecase option).\n\n"
				+ "If the -listFiles option is specified, only the "
				+ "names of the files containing the pattern will be "
				+ "listed.  In this case, files to process must be "
				+ "specified on the command line";

		CmdLineHandler cl = new VersionCmdLineHandler("V 5.2",
				new HelpCmdLineHandler(helpText, "kindagrep",
						"find lines in files containing a specified pattern",
						new Parameter[] { ignorecaseOpt, listfilesOpt },
						new Parameter[] { patternArg, filesArg }));
		cl.parse(args);

		// can't be check by CmdLineHandler
		if (listfilesOpt.isTrue() && !filesArg.isSet()) {
			cl
					.exitUsageError("filename(s) must be specified with -listFiles option");
		}

		String pattern = patternArg.getValue();

		if (filesArg.isSet()) {
			for (File f : filesArg.getValues()) {
				findPattern(pattern, f, listfilesOpt.isTrue());
			}
		} else {
			findPattern(pattern, System.in);
		}
	}

	private static void findPattern(String pattern, File f, boolean listFiles) {
		System.out.println("Looking for '" + pattern + "' in file "
				+ f.getName() + ", listFiles = " + listFiles);
	}

	private static void findPattern(String pattern, InputStream is) {
		System.out.println("Looking for '" + pattern + "' in stdin");
	}
}

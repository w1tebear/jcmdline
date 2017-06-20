The jcmdline package is a Java package with the following goals: 

* Facilitate parsing/handling of command line parameters.

* Add consistency to command line parameter parsing and command usage display through all executables of a Java application.

* Automatically generate a command usage based upon defined command line parameters..

Features of the package include: 

* Parses command line options and arguments.

* Supports frequently used parameter types - booleans, strings, integers, dates, file names...

* Performs common validation tasks - checks for number ranges, file/directory attributes, values that must be from a specified set, etc..

* Displays neatly formatted usage and error message in response to parameter specification errors.

* Provides support for commonly used command line options (such as -help and -version) and a means to standardize command line options across all executables of a project.

* Supports hidden options.

# Example

```java
    public static void main(String[] args) {

        // command line arguments
        StringParam patternArg =
            new StringParam("pattern", "the pattern to match",
                            StringParam.REQUIRED);
        FileParam filesArg =
            new FileParam("file",
                          "a file to be processed - defaults to stdin",
                          FileParam.IS_FILE &amp; FileParam.IS_READABLE,
                          FileParam.OPTIONAL,
                          FileParam.MULTI_VALUED);

        // command line options
        BooleanParam ignorecaseOpt =
            new BooleanParam("ignoreCase", "ignore case while matching");
        BooleanParam listfilesOpt = 
            new BooleanParam("listFiles", "list filenames containing pattern");

        // a help text because we will use a HelpCmdLineHandler so our
        // command will display verbose help with the -help option
        String helpText = "This command prints to stdout all lines within " +
                          "the specified files that contain the specified " +
                          "pattern.\n\n" + 
                          "Optionally, the matching may be done without " +
                          "regard to case (using the -ignorecase option).\n\n" +                          
                          "If the -listFiles option is specified, only the " + 
                          "names of the files containing the pattern will be " +                 
                          "listed.  In this case, files to process must be " +
                          "specified on the command line";
                         
                        
        CmdLineHandler cl =
            new VersionCmdLineHandler("V 5.2",
            new HelpCmdLineHandler(helpText,
                "kindagrep",
                "find lines in files containing a specified pattern",
                new Parameter[] { ignorecaseOpt, listfilesOpt },
                new Parameter[] { patternArg, filesArg } ));
                
        cl.parse(args);
```

Produces the following usage when run with a '-help' option:

```
kindagrep - find lines in files containing a specified pattern

Usage: kindagrep [options] pattern [file],[file]...

where:

pattern = the pattern to match (required)
file    = a file to be processed - defaults to stdin (optional)

and options are:

-?           prints usage to stdout; exits (optional)
-h           prints usage to stdout; exits (optional)
-help        displays verbose help information (optional)
-ignoreCase  ignore case while matching (optional)
-listFiles   list filenames containing pattern (optional)
-version     displays command's version (optional)

Option tags are not case sensitive, and may be truncated as long as they remain
unambiguous.  Option tags must be separated from their corresponding values by
whitespace, or by an equal sign.  Boolean options (options that require no
associated value) may be specified alone (=true), or as 'tag=value' where value
is 'true' or 'false'.

This command prints to stdout all lines within the specified files that contain
the specified pattern.

Optionally, the matching may be done without regard to case (using the
-ignorecase option).

If the -listFiles option is specified, only the names of the files containing
the pattern will be listed.  In this case, files to process must be specified on
the command line
```

For more information, see the [User Guide](docs/apidocs/jcmdline/doc-files/userguide.html).

# Development Status

This package is stable and production worthy.

At present it supports only the English language. All strings
are handled via ResourceBundle, though, and any assistance with
translation to other languages would be greatly appreciated!

# Licence

This software is open source and is provided under the [Mozilla Public License V1.1](http://www.mozilla.org/MPL/).

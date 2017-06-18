package jcmdline;

import jcmdline.AbstractHandlerDecorator;
import jcmdline.HelpCmdLineHandler;
import jcmdline.Parameter;
import jcmdline.VersionCmdLineHandler;

public class MyAppCmdLineHandler extends AbstractHandlerDecorator {
    public MyAppCmdLineHandler(String cmdName,
                               String cmdDesc,
                               String version,
                               String helpText,
                               String hiddenHelpText,
                               Parameter<?>[] options,
                               Parameter<?>[] args) {
        super(new VersionCmdLineHandler(version,
              new HelpCmdLineHandler(helpText, hiddenHelpText,
                                     cmdName, cmdDesc, options, args)));
    }
    public boolean processParsedOptions(boolean parseStatus) {
        return parseStatus;
    }
}

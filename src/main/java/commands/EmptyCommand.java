package commands;

import utils.SessionRequestContent;
import utils.CommandResult;

public class EmptyCommand implements ActionCommand {

    private static EmptyCommand instance;

    public static EmptyCommand getInstance() {
        if (instance == null) {
            synchronized (EmptyCommand.class) {
                if (instance == null)
                    instance = new EmptyCommand();
            }
        }
        return instance;
    }
    
    private EmptyCommand() {
        
    }

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        return new CommandResult("/error.jsp");
    }
}

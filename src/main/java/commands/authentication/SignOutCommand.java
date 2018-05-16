package commands.authentication;

import commands.ActionCommand;
import resource_managers.PageManager;
import utils.SessionRequestContent;
import utils.CommandResult;


public class SignOutCommand implements ActionCommand {

    private static SignOutCommand instance;

    public static SignOutCommand getInstance() {
        if (instance == null) {
            synchronized (SignOutCommand.class) {
                if (instance == null)
                    instance = new SignOutCommand();
            }
        }
        return instance;
    }


    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult(PageManager.getProperty("page.sign_in"), true);
        sessionRequestContent.invalidateSession();
        return commandResult;
    }
}

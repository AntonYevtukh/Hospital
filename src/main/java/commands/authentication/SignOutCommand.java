package commands.authentication;

import commands.ActionCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import resource_managers.PageManager;
import utils.CommandResult;
import utils.SessionRequestContent;


public class SignOutCommand implements ActionCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignOutCommand.class);
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

    private SignOutCommand() {
        LOGGER.info(getClass().getSimpleName() + " instance created!");
    }

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        LOGGER.trace("Entering the method");
        CommandResult commandResult = new CommandResult(PageManager.getProperty("page.sign_in"), true);
        sessionRequestContent.invalidateSession();
        LOGGER.trace("Leaving the method");
        return commandResult;
    }
}

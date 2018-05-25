package commands.authentication;


import commands.ActionDbCommand;
import exceptions.ErrorMessageKeysContainedException;
import model.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import resource_managers.PageManager;
import services.UserService;
import utils.CommandResult;
import utils.SessionRequestContent;

import java.util.ArrayList;
import java.util.List;


public class SignInCommand implements ActionDbCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignInCommand.class);
    private static SignInCommand instance;

    public static SignInCommand getInstance() {
        if (instance == null) {
            synchronized (SignInCommand.class) {
                if (instance == null)
                    instance = new SignInCommand();
            }
        }
        return instance;
    }

    private SignInCommand() {
        LOGGER.info(getClass().getSimpleName() + " instance created!");
    }

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        LOGGER.trace("Entering the method");
        CommandResult commandResult;
        String login = sessionRequestContent.getSingleRequestParameter("login");
        String password = sessionRequestContent.getSingleRequestParameter("password");
        List<String> loginFails = new ArrayList<>(2);

        try {
            long userId = UserService.signIn(login, password);
            User currentUser = UserService.getUserById(userId);
            sessionRequestContent.addSessionAttribute("current_user", currentUser);
            LOGGER.trace("Leaving the method");
            commandResult = new CommandResult("/serv?action=view_user&id=" + userId, true);

        } catch (ErrorMessageKeysContainedException e) {
            LOGGER.error("Error caught while executing the method:", e);
            loginFails.addAll(e.getErrorMesageKeys());
            sessionRequestContent.addRequestAttribute("sign_in_fails", loginFails);
            sessionRequestContent.addRequestAttribute("login", login);
            LOGGER.trace("Leaving the method");
            commandResult = new CommandResult(PageManager.getProperty("page.sign_in"));
        }
        return commandResult;
    }
}

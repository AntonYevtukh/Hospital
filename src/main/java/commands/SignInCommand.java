package commands;


import exceptions.ErrorMessageKeysContainedException;
import model.database.ConnectionProvider;

import model.entities.User;
import resource_managers.PageManager;
import services.UserService;

import utils.SessionRequestContent;
import utils.CommandResult;
import java.util.ArrayList;
import java.util.List;


public class SignInCommand implements ActionDbCommand {

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


    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult;
        String login = sessionRequestContent.getSingleRequestParameter("login");
        String password = sessionRequestContent.getSingleRequestParameter("password");
        List<String> loginFails = new ArrayList<>(2);

        try {
            long userId = UserService.signIn(login, password);
            User currentUser = UserService.getUserById(userId);
            sessionRequestContent.addSessionAttribute("current_user", currentUser);
            commandResult = new CommandResult("/serv?action=view_current_user", true);

        } catch (ErrorMessageKeysContainedException e) {
            loginFails.addAll(e.getErrorMesageKeys());
            sessionRequestContent.addRequestAttribute("sign_in_fails", loginFails);
            sessionRequestContent.addRequestAttribute("login", login);
            commandResult = new CommandResult(PageManager.getProperty("page.sign_in"));
        }
        return commandResult;
    }
}

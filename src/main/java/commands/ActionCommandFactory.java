package commands;

import model.database.ConnectionProvider;
import utils.SessionRequestContent;

import java.util.HashMap;
import java.util.Map;

public class ActionCommandFactory {

    private static Map<String, ActionCommand> actionCommandMap = new HashMap<>();

    static {
        actionCommandMap.put("sign_up", SignUpCommand.getInstance());
        actionCommandMap.put("sign_in", SignInCommand.getInstance());
        actionCommandMap.put("sign_out", SignOutCommand.getInstance());
        actionCommandMap.put("empty_command", EmptyCommand.getInstance());
        actionCommandMap.put("view_user", ViewUserCommand.getInstance());
        actionCommandMap.put("view_current_user", ViewCurrentUserCommand.getInstance());
        actionCommandMap.put("update_user", UpdateUserCommand.getInstance());
        actionCommandMap.put("edit_user", EditUserCommand.getInstance());
        actionCommandMap.put("view_diagnoses", ViewDiagnosesCommand.getInstance());
        actionCommandMap.put("update_diagnose", UpdateDiagnoseCommand.getInstance());
        actionCommandMap.put("add_diagnose", AddDiagnoseCommand.getInstance());
    }

    public static ActionCommand defineCommand(SessionRequestContent sessionRequestContent) {

        try {
            ActionCommand actionCommand = actionCommandMap.getOrDefault(
                    sessionRequestContent.getSingleRequestParameter("action"),
                    actionCommandMap.get("empty_command"));
            if (actionCommand instanceof ActionDbCommand) {
                return (SessionRequestContent sessionRequestContentArg) -> {
                    try {
                        ConnectionProvider.bindConnection();
                        return actionCommand.execute(sessionRequestContentArg);
                    } finally {
                        ConnectionProvider.unbindConnection();
                    }
                };
            } else {
                return actionCommand;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            return actionCommandMap.get("empty_command");
        }
    }
}

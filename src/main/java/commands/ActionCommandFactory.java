package commands;

import commands.authentication.SignInCommand;
import commands.authentication.SignOutCommand;
import commands.authentication.SignUpCommand;
import commands.diagnose.AddDiagnoseCommand;
import commands.diagnose.DeleteDiagnoseCommand;
import commands.diagnose.UpdateDiagnoseCommand;
import commands.diagnose.ViewDiagnosesCommand;
import commands.settings.*;
import commands.user.EditUserCommand;
import commands.user.UpdateUserCommand;
import commands.user.ViewCurrentUserCommand;
import commands.user.ViewUserCommand;
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
        actionCommandMap.put("delete_diagnose", DeleteDiagnoseCommand.getInstance());
        actionCommandMap.put("view_settings", ViewSettingsCommand.getInstance());
        actionCommandMap.put("update_role", UpdateRoleCommand.getInstance());
        actionCommandMap.put("add_role", AddRoleCommand.getInstance());
        actionCommandMap.put("delete_role", DeleteRoleCommand.getInstance());
        actionCommandMap.put("update_assignment_type", UpdateAssignmentTypeCommand.getInstance());
        actionCommandMap.put("add_assignment_type", AddAssignmentTypeCommand.getInstance());
        actionCommandMap.put("delete_assignment_type", DeleteAssignmentTypeCommand.getInstance());
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

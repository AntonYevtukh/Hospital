package commands.settings;

import commands.ActionDbCommand;
import exceptions.ErrorMessageKeysContainedException;
import model.entities.User;
import resource_managers.MessageManager;
import services.RoleService;
import utils.CommandResult;
import utils.SessionRequestContent;
import utils.json.ErrorResponseCreator;
import utils.json.JsonSerializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DeleteRoleCommand implements ActionDbCommand {

    private static DeleteRoleCommand instance;

    public static DeleteRoleCommand getInstance() {
        if (instance == null) {
            synchronized (DeleteRoleCommand.class) {
                if (instance == null)
                    instance = new DeleteRoleCommand();
            }
        }
        return instance;
    }


    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
        long roleId = Long.parseLong(sessionRequestContent.getSingleRequestParameter("id"));
        List<String> validationFails = new ArrayList<>();
        String ajaxString = null;

        try {
            RoleService.deleteRole(roleId);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", MessageManager.getProperty("role.deleted", currentUser.getLanguage()));
            ajaxString = JsonSerializer.serialize(responseMap);
            return new CommandResult("", true, ajaxString, false);
        } catch (ErrorMessageKeysContainedException e) {
            String generalError = e.getErrorMesageKeys().get(0);
            ajaxString = ErrorResponseCreator.createResponseWithErrors(validationFails,
                    generalError, currentUser.getLanguage());
            return new CommandResult("", true, ajaxString, false);
        }
    }
}

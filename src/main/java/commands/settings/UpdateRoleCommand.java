package commands.settings;

import commands.ActionDbCommand;
import exceptions.ErrorMessageKeysContainedException;
import model.entities.Role;
import model.entities.User;
import resource_managers.MessageManager;
import services.RoleService;
import utils.CommandResult;
import utils.SessionRequestContent;
import utils.json.ErrorResponseCreator;
import utils.json.JsonSerializer;
import utils.parsers.RoleParser;
import validation.EntityValidatorFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UpdateRoleCommand implements ActionDbCommand {

    private static UpdateRoleCommand instance;

    public static UpdateRoleCommand getInstance() {
        if (instance == null) {
            synchronized (UpdateRoleCommand.class) {
                if (instance == null)
                    instance = new UpdateRoleCommand();
            }
        }
        return instance;
    }


    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
        Role role = RoleParser.parseRole(sessionRequestContent);
        List<String> validationFails = EntityValidatorFactory.getValidatorFor(Role.class).validate(role);
        String ajaxString = null;

        if (!validationFails.isEmpty()) {
            ajaxString = ErrorResponseCreator.createResponseWithErrors(validationFails, null, currentUser.getLanguage());
            return new CommandResult("", true, ajaxString, false);
        }

        try {
            RoleService.updateRole(role);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", MessageManager.getProperty("role.updated", currentUser.getLanguage()));
            ajaxString = JsonSerializer.serialize(responseMap);
            return new CommandResult("", true, ajaxString, false);
        } catch (ErrorMessageKeysContainedException e) {
            validationFails.addAll(e.getErrorMesageKeys());
            ajaxString = ErrorResponseCreator.createResponseWithErrors(validationFails, null, currentUser.getLanguage());
            return new CommandResult("", true, ajaxString, false);
        }
    }


}

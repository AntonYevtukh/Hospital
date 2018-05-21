package commands.assignment;

import commands.ActionDbCommand;
import exceptions.ErrorMessageKeysContainedException;
import model.entities.User;
import services.ExaminationService;
import utils.CommandResult;
import utils.SessionRequestContent;
import utils.json.ErrorResponseCreator;
import utils.json.JsonSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class LoadDropDownsDataCommand implements ActionDbCommand {

    private static LoadDropDownsDataCommand instance;

    public static LoadDropDownsDataCommand getInstance() {
        if (instance == null) {
            synchronized (LoadDropDownsDataCommand.class) {
                if (instance == null)
                    instance = new LoadDropDownsDataCommand();
            }
        }
        return instance;
    }


    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
        String ajaxString = null;

        try {
            Map<String, Object> responseMap = ExaminationService.getDropDownsData();
            responseMap.put("success", "data loaded");
            ajaxString = JsonSerializer.serialize(responseMap);
            return new CommandResult("", true, ajaxString, false);
        } catch (ErrorMessageKeysContainedException e) {
            List<String> validationFails = new ArrayList<>();
            validationFails.addAll(e.getErrorMesageKeys());
            ajaxString = ErrorResponseCreator.createResponseWithErrors(validationFails, null, currentUser.getLanguage());
            return new CommandResult("", true, ajaxString, false);
        }
    }


}

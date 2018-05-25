package commands.assignment;

import commands.ActionDbCommand;
import exceptions.ErrorMessageKeysContainedException;
import model.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import resource_managers.PageManager;
import services.ExaminationService;
import utils.CommandResult;
import utils.SessionRequestContent;
import utils.json.ErrorResponseCreator;
import utils.json.JsonSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class LoadDropDownsDataCommand implements ActionDbCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadDropDownsDataCommand.class);
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

    private LoadDropDownsDataCommand() {
        LOGGER.info(getClass().getSimpleName() + " instance created!");
    }

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        LOGGER.trace("Entering the method");
        User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
        String jsonString = null;

        try {
            Map<String, Object> responseMap = ExaminationService.getDropDownsData();
            responseMap.put("success", "data loaded");
            jsonString = JsonSerializer.serialize(responseMap);
            sessionRequestContent.addRequestAttribute("jsonString", jsonString);
            LOGGER.trace("Leaving the method");
            return new CommandResult(PageManager.getProperty("page.json"));
        } catch (ErrorMessageKeysContainedException e) {
            LOGGER.error("Error caught while executing the method:", e);
            List<String> validationFails = new ArrayList<>();
            validationFails.addAll(e.getErrorMesageKeys());
            jsonString = ErrorResponseCreator.createResponseWithErrors(validationFails, null, currentUser.getLanguage());
            sessionRequestContent.addRequestAttribute("jsonString", jsonString);
            LOGGER.trace("Leaving the method");
            return new CommandResult(PageManager.getProperty("page.json"));
        }
    }


}

package commands.diagnose;

import commands.ActionDbCommand;
import exceptions.ErrorMessageKeysContainedException;
import model.entities.Diagnose;
import model.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import resource_managers.MessageManager;
import resource_managers.PageManager;
import services.DiagnoseService;
import utils.CommandResult;
import utils.SessionRequestContent;
import utils.json.ErrorResponseCreator;
import utils.json.JsonSerializer;
import utils.parsers.DiagnoseParser;
import validation.EntityValidatorFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UpdateDiagnoseCommand implements ActionDbCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateDiagnoseCommand.class);
    private static UpdateDiagnoseCommand instance;

    public static UpdateDiagnoseCommand getInstance() {
        if (instance == null) {
            synchronized (UpdateDiagnoseCommand.class) {
                if (instance == null)
                    instance = new UpdateDiagnoseCommand();
            }
        }
        return instance;
    }

    public UpdateDiagnoseCommand() {
        LOGGER.info(getClass().getSimpleName() + " instance created!");
    }

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        LOGGER.trace("Entering the method");
        User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
        Diagnose diagnose = DiagnoseParser.parseDiagnose(sessionRequestContent);
        List<String> validationFails = EntityValidatorFactory.getValidatorFor(Diagnose.class).validate(diagnose);
        String jsonString = null;

        if (!validationFails.isEmpty()) {
            jsonString = ErrorResponseCreator.createResponseWithErrors(validationFails, null, currentUser.getLanguage());
            sessionRequestContent.addRequestAttribute("jsonString", jsonString);
            LOGGER.trace("Leaving the method");
            return new CommandResult(PageManager.getProperty("page.json"));
        }

        try {
            DiagnoseService.updateDiagnose(diagnose);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", MessageManager.getProperty("diagnose.updated", currentUser.getLanguage()));
            jsonString = JsonSerializer.serialize(responseMap);
            sessionRequestContent.addRequestAttribute("jsonString", jsonString);
            LOGGER.trace("Leaving the method");
            return new CommandResult(PageManager.getProperty("page.json"));
        } catch (ErrorMessageKeysContainedException e) {
            LOGGER.error("Error caught while executing the method:", e);
            validationFails.addAll(e.getErrorMesageKeys());
            jsonString = ErrorResponseCreator.createResponseWithErrors(validationFails, null, currentUser.getLanguage());
            sessionRequestContent.addRequestAttribute("jsonString", jsonString);
            LOGGER.trace("Leaving the method");
            return new CommandResult(PageManager.getProperty("page.json"));
        }
    }


}

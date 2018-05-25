package commands.diagnose;

import commands.ActionDbCommand;
import model.entities.Diagnose;
import model.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import resource_managers.PageManager;
import services.DiagnoseService;
import utils.CommandResult;
import utils.PageContent;
import utils.SessionRequestContent;

public class ViewDiagnosesCommand implements ActionDbCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(ViewDiagnosesCommand.class);
    private static ViewDiagnosesCommand instance;

    public static ViewDiagnosesCommand getInstance() {
        if (instance == null) {
            synchronized (ViewDiagnosesCommand.class) {
                if (instance == null)
                    instance = new ViewDiagnosesCommand();
            }
        }
        return instance;
    }

    private ViewDiagnosesCommand() {
        LOGGER.info(getClass().getSimpleName() + " instance created!");
    }

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        LOGGER.trace("Entering the method");
        int page = Integer.parseInt(sessionRequestContent.getSingleRequestParameter("page"));
        try {
            User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
            if (!currentUser.getRoleMap().containsKey(3L)) {
                sessionRequestContent.addRequestAttribute("error_message", "error.access");
                LOGGER.trace("Leaving the method");
                return new CommandResult(PageManager.getProperty("page.error"));
            }
            PageContent<Diagnose> pageContent = DiagnoseService.getDiagnosesForPage(page, currentUser.getItemsPerPage());
            sessionRequestContent.addRequestAttribute("page_content", pageContent);
            LOGGER.trace("Leaving the method");
            return new CommandResult(PageManager.getProperty("page.view_diagnoses"));
        } catch (RuntimeException e) {
            LOGGER.error("Error caught while executing the method:", e);
            LOGGER.trace("Leaving the method");
            return new CommandResult(PageManager.getProperty("page.error"));
        }
    }
}

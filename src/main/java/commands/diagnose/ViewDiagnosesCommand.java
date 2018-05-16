package commands.diagnose;

import commands.ActionDbCommand;
import model.entities.Diagnose;
import model.entities.User;
import resource_managers.PageManager;
import services.DiagnoseService;
import utils.CommandResult;
import utils.PageContent;
import utils.SessionRequestContent;

public class ViewDiagnosesCommand implements ActionDbCommand {
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
    }

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        int page = Integer.parseInt(sessionRequestContent.getSingleRequestParameter("page"));
        try {
            User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
            if (!currentUser.getRoleMap().containsKey(3L)) {
                sessionRequestContent.addRequestAttribute("error_message", "error.access");
                return new CommandResult(PageManager.getProperty("page.error"));
            }
            PageContent<Diagnose> pageContent = DiagnoseService.getDiagnosesForPage(page, currentUser.getItemsPerPage());
            sessionRequestContent.addRequestAttribute("page_content", pageContent);
            return new CommandResult(PageManager.getProperty("page.view_diagnoses"));
        } catch (RuntimeException e) {
            e.getMessage();
            e.printStackTrace();
            return new CommandResult(PageManager.getProperty("page.error"));
        }
    }
}

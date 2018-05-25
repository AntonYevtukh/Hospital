package commands.hospitalization;

import commands.ActionDbCommand;
import model.entities.Hospitalization;
import model.entities.User;
import resource_managers.PageManager;
import services.HospitalizationService;
import utils.CommandResult;
import utils.PageContent;
import utils.SessionRequestContent;

public class ViewHospitalizationsCommand implements ActionDbCommand {
    private static ViewHospitalizationsCommand instance;

    public static ViewHospitalizationsCommand getInstance() {
        if (instance == null) {
            synchronized (ViewHospitalizationsCommand.class) {
                if (instance == null)
                    instance = new ViewHospitalizationsCommand();
            }
        }
        return instance;
    }

    private ViewHospitalizationsCommand() {
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
            PageContent<Hospitalization> pageContent =
                    HospitalizationService.getHospitalizationsForPage(page, currentUser.getItemsPerPage());
            sessionRequestContent.addRequestAttribute("title", "hospitalization.hospitalizations");
            sessionRequestContent.addRequestAttribute("page_content", pageContent);
            sessionRequestContent.addRequestAttribute("url_pattern",
                    "/serv?action=view_hospitalizations&page=");
            return new CommandResult(PageManager.getProperty("page.view_hospitalizations"));
        } catch (RuntimeException e) {
            e.getMessage();
            e.printStackTrace();
            return new CommandResult(PageManager.getProperty("page.error"));
        }
    }
}

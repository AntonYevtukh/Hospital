package commands.hospitalization;

import commands.ActionDbCommand;
import model.entities.Examination;
import model.entities.Hospitalization;
import model.entities.User;
import resource_managers.PageManager;
import services.ExaminationService;
import services.HospitalizationService;
import utils.CommandResult;
import utils.PageContent;
import utils.SessionRequestContent;

public class ViewHospitalizationCommand implements ActionDbCommand {
    private static ViewHospitalizationCommand instance;

    public static ViewHospitalizationCommand getInstance() {
        if (instance == null) {
            synchronized (ViewHospitalizationCommand.class) {
                if (instance == null)
                    instance = new ViewHospitalizationCommand();
            }
        }
        return instance;
    }

    private ViewHospitalizationCommand() {
    }

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        int page = Integer.parseInt(sessionRequestContent.getSingleRequestParameter("examinations_page"));
        int hospitalizationId = Integer.parseInt(sessionRequestContent.getSingleRequestParameter("hospitalization_id"));
        try {
            User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
            Hospitalization hospitalization =
                    HospitalizationService.getHospitalizationById(hospitalizationId);
            if (!currentUser.getRoleMap().containsKey(3L) && currentUser.getId() != hospitalization.getPatient().getId()) {
                sessionRequestContent.addRequestAttribute("error_message", "error.access");
                return new CommandResult(PageManager.getProperty("page.error"));
            }
            PageContent<Examination> pageContent = ExaminationService.getIntermediateExaminationsForPageByHospitalizationId(
                            hospitalizationId, page, currentUser.getItemsPerPage());
            sessionRequestContent.addRequestAttribute("title", "hospitalization.hospitalization");
            sessionRequestContent.addRequestAttribute("hospitalization", hospitalization);
            sessionRequestContent.addRequestAttribute("page_content", pageContent);
            sessionRequestContent.addRequestAttribute("url_pattern",
                    "/serv?action=view_hospitalization&examinations_page=");
            return new CommandResult(PageManager.getProperty("page.view_hospitalization"));
        } catch (RuntimeException e) {
            e.getMessage();
            e.printStackTrace();
            return new CommandResult(PageManager.getProperty("page.error"));
        }
    }
}

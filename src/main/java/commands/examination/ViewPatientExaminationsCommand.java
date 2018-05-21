package commands.examination;

import commands.ActionDbCommand;
import model.entities.Examination;
import model.entities.User;
import resource_managers.PageManager;
import services.ExaminationService;
import services.UserService;
import utils.CommandResult;
import utils.PageContent;
import utils.SessionRequestContent;

public class ViewPatientExaminationsCommand implements ActionDbCommand {
    private static ViewPatientExaminationsCommand instance;

    public static ViewPatientExaminationsCommand getInstance() {
        if (instance == null) {
            synchronized (ViewPatientExaminationsCommand.class) {
                if (instance == null)
                    instance = new ViewPatientExaminationsCommand();
            }
        }
        return instance;
    }

    private ViewPatientExaminationsCommand() {
    }

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        int page = Integer.parseInt(sessionRequestContent.getSingleRequestParameter("page"));
        int patientId = Integer.parseInt(sessionRequestContent.getSingleRequestParameter("patient_id"));
        User patient = UserService.getUserById(patientId);
        try {
            User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
            if (!currentUser.getRoleMap().containsKey(3L)) {
                sessionRequestContent.addRequestAttribute("error_message", "error.access");
                return new CommandResult(PageManager.getProperty("page.error"));
            }
            PageContent<Examination> pageContent =
                    ExaminationService.getExaminationsForPageByPatientId(patientId, page, currentUser.getItemsPerPage());
            sessionRequestContent.addRequestAttribute("title", "examinations.examinations_patient");
            sessionRequestContent.addRequestAttribute("page_content", pageContent);
            sessionRequestContent.addRequestAttribute("url_pattern",
                    "/serv?action=view_patient_examinations&patient_id=" + patientId + "&page=");
            sessionRequestContent.addRequestAttribute("user", patient);
            return new CommandResult(PageManager.getProperty("page.view_examinations"));
        } catch (RuntimeException e) {
            e.getMessage();
            e.printStackTrace();
            return new CommandResult(PageManager.getProperty("page.error"));
        }
    }
}

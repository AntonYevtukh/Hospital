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

public class ViewDoctorExaminationsCommand implements ActionDbCommand {
    private static ViewDoctorExaminationsCommand instance;

    public static ViewDoctorExaminationsCommand getInstance() {
        if (instance == null) {
            synchronized (ViewDoctorExaminationsCommand.class) {
                if (instance == null)
                    instance = new ViewDoctorExaminationsCommand();
            }
        }
        return instance;
    }

    private ViewDoctorExaminationsCommand() {
    }

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        int page = Integer.parseInt(sessionRequestContent.getSingleRequestParameter("page"));
        int doctorId = Integer.parseInt(sessionRequestContent.getSingleRequestParameter("doctor_id"));
        User doctor = UserService.getUserById(doctorId);
        try {
            User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
            if (!currentUser.getRoleMap().containsKey(3L)) {
                sessionRequestContent.addRequestAttribute("error_message", "error.access");
                return new CommandResult(PageManager.getProperty("page.error"));
            }
            PageContent<Examination> pageContent =
                    ExaminationService.getExaminationsForPageByDoctorId(doctorId, page, currentUser.getItemsPerPage());
            sessionRequestContent.addRequestAttribute("title", "examinations.examinations_doctor");
            sessionRequestContent.addRequestAttribute("page_content", pageContent);
            sessionRequestContent.addRequestAttribute("url_pattern",
                    "/serv?action=view_doctor_examinations&doctor_id=" + doctorId + "&page=");
            sessionRequestContent.addRequestAttribute("user", doctor);
            return new CommandResult(PageManager.getProperty("page.view_examinations"));
        } catch (RuntimeException e) {
            e.getMessage();
            e.printStackTrace();
            return new CommandResult(PageManager.getProperty("page.error"));
        }
    }
}

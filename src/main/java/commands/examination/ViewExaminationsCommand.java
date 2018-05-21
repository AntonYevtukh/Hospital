package commands.examination;

import commands.ActionDbCommand;
import model.entities.Examination;
import model.entities.User;
import resource_managers.PageManager;
import services.ExaminationService;
import utils.CommandResult;
import utils.PageContent;
import utils.SessionRequestContent;

public class ViewExaminationsCommand implements ActionDbCommand {
    private static ViewExaminationsCommand instance;

    public static ViewExaminationsCommand getInstance() {
        if (instance == null) {
            synchronized (ViewExaminationsCommand.class) {
                if (instance == null)
                    instance = new ViewExaminationsCommand();
            }
        }
        return instance;
    }

    private ViewExaminationsCommand() {
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
            PageContent<Examination> pageContent = ExaminationService.getExaminationsForPage(page, currentUser.getItemsPerPage());
            sessionRequestContent.addRequestAttribute("title", "examinations.examinations");
            sessionRequestContent.addRequestAttribute("page_content", pageContent);
            sessionRequestContent.addRequestAttribute("url_pattern", "/serv?action=view_examinations&page=");
            return new CommandResult(PageManager.getProperty("page.view_examinations"));
        } catch (RuntimeException e) {
            e.getMessage();
            e.printStackTrace();
            return new CommandResult(PageManager.getProperty("page.error"));
        }
    }
}

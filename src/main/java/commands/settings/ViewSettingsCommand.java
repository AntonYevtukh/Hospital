package commands.settings;

import commands.ActionDbCommand;
import model.entities.AssignmentType;
import model.entities.Diagnose;
import model.entities.Role;
import model.entities.User;
import resource_managers.PageManager;
import services.AssignmentTypeService;
import services.DiagnoseService;
import services.RoleService;
import utils.CommandResult;
import utils.PageContent;
import utils.SessionRequestContent;

import java.util.List;

public class ViewSettingsCommand implements ActionDbCommand {
    private static ViewSettingsCommand instance;

    public static ViewSettingsCommand getInstance() {
        if (instance == null) {
            synchronized (ViewSettingsCommand.class) {
                if (instance == null)
                    instance = new ViewSettingsCommand();
            }
        }
        return instance;
    }

    private ViewSettingsCommand() {
    }

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        try {
            User currentUser = (User)sessionRequestContent.getSessionAttribute("current_user");
            if (!currentUser.getRoleMap().containsKey(4L)) {
                sessionRequestContent.addRequestAttribute("error_message", "error.access");
                return new CommandResult(PageManager.getProperty("page.error"));
            }
            List<Role> roles = RoleService.getRoles();
            List<AssignmentType> assignmentTypes = AssignmentTypeService.getAssignmentTypes();
            sessionRequestContent.addRequestAttribute("roles", roles);
            sessionRequestContent.addRequestAttribute("assignment_types", assignmentTypes);
            return new CommandResult(PageManager.getProperty("page.view_settings"));
        } catch (RuntimeException e) {
            e.getMessage();
            e.printStackTrace();
            return new CommandResult(PageManager.getProperty("page.error"));
        }
    }
}

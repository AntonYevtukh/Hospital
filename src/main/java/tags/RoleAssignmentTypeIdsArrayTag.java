package tags;

import model.entities.AssignmentType;
import model.entities.Role;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.StringJoiner;

public class RoleAssignmentTypeIdsArrayTag extends SimpleTagSupport {

    private Role role;

    public RoleAssignmentTypeIdsArrayTag() {
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public void doTag() throws JspException, IOException {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");
        role.getAllowedAssignmentTypes().forEach(
                (AssignmentType assignmentType) -> joiner.add(Long.toString(assignmentType.getId())));
        getJspContext().getOut().write(joiner.toString());
    }
}

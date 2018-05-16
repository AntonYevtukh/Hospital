package tags;

import model.entities.AssignmentType;
import model.entities.Role;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Base64;
import java.util.StringJoiner;

public class RoleAssignmentTypeNamesTag extends SimpleTagSupport {

    private Role role;

    public RoleAssignmentTypeNamesTag() {
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public void doTag() throws JspException, IOException {
        StringJoiner joiner = new StringJoiner(", ");
        role.getAllowedAssignmentTypes().forEach((AssignmentType assignmentType) -> joiner.add(assignmentType.getName()));
        getJspContext().getOut().write(joiner.toString());
    }
}

package tags;

import model.entities.User;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.StringJoiner;

public class UserShortInfoTag extends SimpleTagSupport {
    
    private User user;
    private Boolean showPassportNumber;

    public UserShortInfoTag() {
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setShowPassportNumber(Boolean showPassportNumber) {
        this.showPassportNumber = showPassportNumber;
    }

    @Override
    public void doTag() throws JspException, IOException {

        StringJoiner userShortInfoJoiner = new StringJoiner(" ");
        System.out.println("user: " + user);
        userShortInfoJoiner.add(user.getLastName());
        userShortInfoJoiner.add(user.getFirstName());
        if (user.getMiddleName() != null && !user.getMiddleName().isEmpty())
            userShortInfoJoiner.add(user.getMiddleName());
        if (showPassportNumber) {
            userShortInfoJoiner.add("/");
            userShortInfoJoiner.add(user.getPassportNumber());
        }
        getJspContext().getOut().write(userShortInfoJoiner.toString());
    }
}

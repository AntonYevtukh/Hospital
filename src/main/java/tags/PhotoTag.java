package tags;

import model.entities.Photo;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Base64;

public class PhotoTag extends SimpleTagSupport {

    private Photo photo;
    private String cssClass;
    private String htmlId;

    public PhotoTag() {
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public void setHtmlId(String htmlId) {
        this.htmlId = htmlId;
    }

    @Override
    public void doTag() throws JspException, IOException {

        String encodedImageBody = (photo.getContent() != null) ?
                Base64.getEncoder().encodeToString(photo.getContent()) : "";
        StringBuilder imageTagString = new StringBuilder();
        imageTagString.append("<img src=\"data:image/png;base64,");
        imageTagString.append(encodedImageBody + "\" ");
        imageTagString.append("class=\"").append(cssClass).append("\" ");
        imageTagString.append("alt=\"").append(photo.getName()).append("\" ");
        imageTagString.append("id=\"").append(htmlId).append("\"/>");
        getJspContext().getOut().write(imageTagString.toString());
    }
}

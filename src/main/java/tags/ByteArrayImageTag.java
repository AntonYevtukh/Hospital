package tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Base64;

public class ByteArrayImageTag extends SimpleTagSupport {

    private byte[] content;
    private String cssClass;
    private String alt;
    private String id;

    public ByteArrayImageTag() {
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void doTag() throws JspException, IOException {

        String encodedImageBody = (content != null) ?
                Base64.getEncoder().encodeToString(content) : "";
        StringBuilder imageTagString = new StringBuilder();
        imageTagString.append("<img src=\"data:image/png;base64,");
        imageTagString.append(encodedImageBody + "\" ");
        imageTagString.append("class=\"").append(cssClass).append("\" ");
        imageTagString.append("alt=\"").append(alt).append("\" ");
        imageTagString.append("id=\"").append(id).append("\"/>");
        getJspContext().getOut().write(imageTagString.toString());
    }
}

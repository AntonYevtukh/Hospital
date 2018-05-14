package tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Base64;

public class BootstrapPaginationTag extends SimpleTagSupport {

    private String urlPattern;
    private long page;
    private long totalPages;

    public BootstrapPaginationTag() {
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    @Override
    public void doTag() throws JspException, IOException {
        StringBuilder bootstrapPaginationTagString = new StringBuilder();
        bootstrapPaginationTagString.append("<nav>\n<ul class=\"pagination pagination-sm mb-0\">");
        String disabled = (page == 1) ? "disabled" : "";
        bootstrapPaginationTagString.append(String.format("<li class=\"page-item %s\">\n" +
                "<a class=\"page-link\" href=\"%s%s\" >|&lt;</a>\n</li>", disabled, urlPattern, 1));
        bootstrapPaginationTagString.append(String.format("<li class=\"page-item %s\">\n" +
                "<a class=\"page-link\" href=\"%s%s\" >&lt;</a>\n</li>", disabled, urlPattern, page - 1));
        for (int i = 1; i <= totalPages; i++) {
            String active = (page == i) ? "active" : "";
            bootstrapPaginationTagString.append(String.format("<li class=\"page-item %s\">\n" +
                    "<a class=\"page-link\" href=\"%s%s\" >%s</a>\n</li>", active, urlPattern, i, i));
        }
        disabled = (page == totalPages) ? "disabled" : "";
        bootstrapPaginationTagString.append(String.format("<li class=\"page-item %s\">\n" +
                "<a class=\"page-link\" href=\"%s%s\" >&gt;</a>\n</li>", disabled, urlPattern, page + 1));
        bootstrapPaginationTagString.append(String.format("<li class=\"page-item %s\">\n" +
                "<a class=\"page-link\" href=\"%s%s\" >&gt;|</a>\n</li>", disabled, urlPattern, totalPages));
        bootstrapPaginationTagString.append("</ul>\n</nav>\n");
        getJspContext().getOut().write(bootstrapPaginationTagString.toString());
    }
}

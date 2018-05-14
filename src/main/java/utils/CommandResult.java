package utils;

public class CommandResult {

    private String url;
    private boolean ajax;
    private String ajaxContent;
    private boolean redirect;

    public CommandResult() {
    }

    public CommandResult(String url, boolean ajax, String ajaxContent, boolean redirect) {
        this.url = url;
        this.ajax = ajax;
        this.ajaxContent = ajaxContent;
        this.redirect = redirect;
    }

    public CommandResult(String page) {
        this(page, false, null, false);
    }

    public CommandResult(String page, boolean redirect) {
        this(page, false, null, redirect);
    }

    public String getUrl() {
        return url;
    }

    public boolean isAjax() {
        return ajax;
    }

    public String getAjaxContent() {
        return ajaxContent;
    }

    public boolean isRedirect() {
        return redirect;
    }
}

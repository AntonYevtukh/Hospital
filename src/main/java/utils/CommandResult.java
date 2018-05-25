package utils;

public class CommandResult {

    private String url;
    private boolean redirect;

    public CommandResult() {
    }

    public CommandResult(String url, boolean redirect) {
        this.url = url;
        this.redirect = redirect;
    }

    public CommandResult(String url) {
        this(url, false);
    }

    public String getUrl() {
        return url;
    }

    public boolean isRedirect() {
        return redirect;
    }
}

package utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SessionRequestContent {
    private HttpServletRequest request;
    private HttpSession session;
    private String requestContentString;

    public SessionRequestContent(HttpServletRequest request) {
        this.request = request;
        this.session = request.getSession();
    }

    public String[] getRequestParameter(String parameterName) {
        return request.getParameterValues(parameterName);
    }

    public String getSingleRequestParameter(String parameterName) {
        return request.getParameterValues(parameterName)[0];
    }

    public Object getRequestAttribute(String attributeName) {
        return request.getAttribute(attributeName);
    }

    public void addRequestAttribute(String attributeName, Object attribute) {
        request.setAttribute(attributeName, attribute);
    }

    public void removeRequestAttribute(String attributeName) {
        request.removeAttribute(attributeName);
    }

    public Object getSessionAttribute(String attributeName) {
        return session.getAttribute(attributeName);
    }

    public void addSessionAttribute(String attributeName, Object attribute) {
        session.setAttribute(attributeName, attribute);
    }

    public void removeSessionAttribute(String attributeName) {
        session.removeAttribute(attributeName);
    }

    public void invalidateSession() {
        session.invalidate();
    }

    public Part getRequestPart(String partName) {
        try {
            return request.getPart(partName);
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getRequestContentType() {
        return request.getContentType();
    }

    public String getRequestBodyString() {
        if (requestContentString == null) {
            StringBuilder requestBodyBuilder = new StringBuilder("");
            String line = null;
            try {
                BufferedReader reader = request.getReader();
                while ((line = reader.readLine()) != null)
                    requestBodyBuilder.append(line);
            } catch (Exception e) {
                e.printStackTrace();
            }
            requestContentString = requestBodyBuilder.toString();
        }
        return requestContentString;
    }
}

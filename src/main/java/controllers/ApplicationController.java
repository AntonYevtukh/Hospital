package controllers;

import commands.ActionCommand;
import commands.ActionCommandFactory;
import utils.CommandResult;
import utils.SessionRequestContent;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ApplicationController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        SessionRequestContent sessionRequestContent = new SessionRequestContent(req);
        ActionCommand actionCommand = ActionCommandFactory.defineCommand(sessionRequestContent);
        CommandResult commandResult = actionCommand.execute(sessionRequestContent);
        if (!commandResult.isRedirect()) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(commandResult.getUrl());
            dispatcher.forward(req, resp);
        } else {
            resp.sendRedirect(commandResult.getUrl());
        }
    }
}

package com.pharmacy.traning.controller;

import java.io.*;

import com.pharmacy.traning.controller.command.Command;
import com.pharmacy.traning.controller.command.CommandFactory;
import com.pharmacy.traning.controller.command.PathToPage;
import com.pharmacy.traning.controller.command.Router;
import com.pharmacy.traning.exception.CommandException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.pharmacy.traning.controller.command.RequestAttribute.ERROR;

/**
 * @author Besarab Victor
 * The type Main servlet.
 */
@WebServlet(name = "helloServlet", urlPatterns = "/controller")
public class MainServlet extends UploadServlet {

private static final Logger logger = LogManager.getLogger();

    public void init() {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
       processRequest(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Command command = CommandFactory.getInstance().createCommand(request);
        Router router;
        try {
            router = command.execute(request);
        } catch (CommandException e) {
            logger.error("Command exception in main servlet. " + e);
            request.setAttribute(ERROR, e);
            router = new Router(PathToPage.ERROR_404, Router.RouterType.FORWARD);
        }
        switch (router.getRouterType()) {
            case FORWARD -> request.getRequestDispatcher(router.getPagePath()).forward(request, response);
            case REDIRECT -> response.sendRedirect(request.getContextPath() + router.getPagePath());
            default -> response.sendError(500, "Unknown type");
        }
    }

    public void destroy() {
    }
}
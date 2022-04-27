package com.pharmacy.traning.controller.command.impl.admin.go;

import com.pharmacy.traning.controller.command.Command;
import com.pharmacy.traning.controller.command.PathToPage;
import com.pharmacy.traning.controller.command.Router;
import com.pharmacy.traning.exception.CommandException;
import jakarta.servlet.http.HttpServletRequest;

/**
 * The type Go to admin menu command.
 */
public class GoToAdminMenuCommand implements Command {

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        return new Router(PathToPage.ADMIN_MENU, Router.RouterType.FORWARD);
    }
}

package com.pharmacy.traning.controller.command.impl.user;

import com.pharmacy.traning.controller.command.Command;
import com.pharmacy.traning.controller.command.Message;
import com.pharmacy.traning.controller.command.PathToPage;
import com.pharmacy.traning.controller.command.Router;
import com.pharmacy.traning.exception.CommandException;
import com.pharmacy.traning.exception.ServiceException;
import com.pharmacy.traning.model.entity.Product;
import com.pharmacy.traning.model.entity.User;
import com.pharmacy.traning.model.service.ServiceOrder;
import com.pharmacy.traning.model.service.ServiceProduct;
import com.pharmacy.traning.model.service.impl.ServiceOrderImpl;
import com.pharmacy.traning.model.service.impl.ServiceProductImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;

import static com.pharmacy.traning.controller.command.RequestAttribute.ERROR;
import static com.pharmacy.traning.controller.command.RequestAttribute.PRODUCT_LIST;
import static com.pharmacy.traning.controller.command.RequestParameter.PRODUCT_ID;
import static com.pharmacy.traning.controller.command.RequestParameter.QUANTITY;
import static com.pharmacy.traning.controller.command.SessionAttribute.*;

/**
 * The type Add product in order command.
 */
public class AddProductInOrderCommand implements Command {

    private static final ServiceOrder serviceOrder = ServiceOrderImpl.getInstance();
    private static final ServiceProduct serviceProduct = ServiceProductImpl.getInstance();

    // TODO: 21.04.2022 testing
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();
        long productId = Long.parseLong(request.getParameter(PRODUCT_ID));
        long userId = ((User) session.getAttribute(USER)).getId();
        String quantity = request.getParameter(QUANTITY);
        try {
            if (serviceOrder.addOrder(productId, userId, quantity)){
                List<Product> productList = serviceProduct.findAllProduct();
                request.setAttribute(PRODUCT_LIST, productList);
                return new Router(PathToPage.USER_PRODUCT_LIST, Router.RouterType.FORWARD);
            }
        } catch (ServiceException e) {
            throw new CommandException("CommandException in AddProductInOrderCommand. " + e);
        }
        request.setAttribute(ERROR, Message.ERROR_INPUT_DATA);
        return new Router(PathToPage.ERROR_404, Router.RouterType.FORWARD);
    }
}

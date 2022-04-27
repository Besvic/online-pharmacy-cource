package com.pharmacy.traning.controller.command.impl.user.go;

import com.pharmacy.traning.controller.command.Command;
import com.pharmacy.traning.controller.command.PathToPage;
import com.pharmacy.traning.controller.command.Router;
import com.pharmacy.traning.exception.CommandException;
import com.pharmacy.traning.exception.ServiceException;
import com.pharmacy.traning.model.entity.Product;
import com.pharmacy.traning.model.service.impl.ServiceProductImpl;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import static com.pharmacy.traning.controller.command.RequestAttribute.PRODUCT_LIST;

/**
 * The type Go to product list for purchase command.
 */
public class GoToProductListForPurchaseCommand implements Command{

    private static final ServiceProductImpl serviceProduct = ServiceProductImpl.getInstance();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        try {
            List<Product> productList = serviceProduct.findAllProduct();
            request.setAttribute(PRODUCT_LIST, productList);
            return new Router(PathToPage.USER_PRODUCT_LIST, Router.RouterType.FORWARD);
        } catch (ServiceException e) {
            throw new CommandException("CommandException in GoToProductListForPurchaseCommand. " + e);
        }
    }
}

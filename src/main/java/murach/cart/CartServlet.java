package murach.cart;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sound.sampled.Line;

import murach.data.*;
import murach.business.*;

public class CartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        ServletContext sc = getServletContext();

        String action = request.getParameter("action");
        if (action == null) {
            action = "cart";
        }

        String url = "/index.jsp";
        if (action.equals("shop")) {
            url = "/index.jsp";
        }
        else if (action.equals("cart"))
        {

            String productCode = request.getParameter("productCode");
            String quantityString = request.getParameter("quantity");
            HttpSession session = request.getSession();

            Cart cart = (Cart) session.getAttribute("cart");

            if (cart == null) {
                cart = new Cart();
            }
            String path = sc.getRealPath("/WEB-INF/products.txt");
            Product product = ProductIO.getProduct(productCode, path);
            int quantity;
            try {
                quantity = Integer.parseInt(quantityString);
                if (quantity < 0) {
                    quantity = 1;
                }

                LineItem lineItem = new LineItem(product, quantity);
                if (quantity > 0 )
                {
                    cart.updateItem(lineItem);
                } else if (quantity == 0) {
                    cart.removeItem(lineItem);
                }

            }
            catch (NumberFormatException nfe) {
                LineItem lineItem = new LineItem(product, 1);
                cart.addItem(lineItem);
            }

            session.setAttribute("cart", cart);
            url = "/cart.jsp";
        }
        else if (action.equals("checkout")) {
            HttpSession session = request.getSession();
            session.setAttribute("cart", new Cart());
            url = "/checkout.jsp";
        }

        sc.getRequestDispatcher(url)
                .forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

}
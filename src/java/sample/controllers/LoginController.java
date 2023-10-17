package sample.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import sample.users.UserDAO;
import sample.users.UserDTO;

public class LoginController extends HttpServlet {

    private static final String ERROR = "login.jsp";

    private static final String MANAGER_PAGE = "manager.jsp";
    private static final String CUSTOMER_PAGE = "customer.jsp";

    private static final String MANAGER = "QL";
    private static final String CUSTOMER = "KH";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String url = ERROR;

        try {

            // get parameters from form request
            String userName = request.getParameter("userName");
            String passWord = request.getParameter("passWord");

            // create an data user object to store current User login
            UserDAO dao = new UserDAO();
            UserDTO loginUser = dao.checkLogin(userName, passWord);

            // checking authentication
            if (loginUser != null) {
                String roleID = loginUser.getRoleID();

                HttpSession session = request.getSession();
                session.setAttribute("LOGIN_USER", loginUser);

                // phan quyen o day
                if (MANAGER.equals(roleID)) {
                    url = MANAGER_PAGE;
                } else if (CUSTOMER.equals(roleID)) {
                    url = CUSTOMER_PAGE;
                } else {
                    request.setAttribute("ERROR", "Your role is not supported");
                }
            } else {
                request.setAttribute("ERROR", "Incorrect username or password");
            }

        } catch (Exception e) {
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}

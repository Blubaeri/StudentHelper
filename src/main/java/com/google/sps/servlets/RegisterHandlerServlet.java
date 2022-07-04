package main.java.com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register-handler")
public class RegisterHandlerServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    // Get the values entered in the form:
    String names = request.getParameter("names");
    String lastNames = request.getParameter("lastNames");
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    // Print the value so you can see it in the server logs:
    System.out.println("Your name(s): " + names);
    System.out.println("Your last name(s): " + lastNames);
    System.out.println("Your username: " + username);
    System.out.println("Your password: " + password);

    // Write the value to the response so the user can see it.
    response.getWriter().println("Your name(s): " + names
        + "\nYour last name(s): " + lastNames
        + "\nYour username: " + username
        + "\nYour password: " + password);

  }

}
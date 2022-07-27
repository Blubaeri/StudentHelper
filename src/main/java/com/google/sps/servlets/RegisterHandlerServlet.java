package main.java.com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import java.util.ArrayList;
import java.util.List;
import main.java.Task;
import com.google.gson.Gson;
import java.util.Base64;
import javax.servlet.ServletException;

@WebServlet("/register-handler")
public class RegisterHandlerServlet extends HttpServlet {
  boolean alex = true;
  boolean newUsername = true;
  boolean validUsername = true;
  boolean samePassword = true;

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // Get the values entered in the form:
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    String confirmPassword = request.getParameter("confirmPassword");
    long timestamp = System.currentTimeMillis();
    String encodedPassword = Base64.getEncoder().encodeToString(password.getBytes());
    //boolean nullProperty = false;

    newUsername = true;
    samePassword = true;
    validUsername = true;

    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    KeyFactory keyFactory = datastore.newKeyFactory().setKind("Task");

    Query<Entity> query =
        Query.newEntityQueryBuilder().setKind("Task").setOrderBy(OrderBy.desc("timestamp")).build();
    QueryResults<Entity> results = datastore.run(query);

    while (results.hasNext()) {
        Entity entity = results.next();

        String previousUsername = entity.getString("username");

        if (previousUsername.equals(username)) {
            newUsername = false;
            break;
        }
    }
    
    if (!password.equals(confirmPassword)) {
        samePassword = false;
    }

    if ((username.charAt(0) < 65 || username.charAt(0) > 90)
    && (username.charAt(0) < 97 || username.charAt(0) > 122))
            validUsername = false;

    for (int i = 1; i < username.length(); i++) {
        if ((username.charAt(i) < 48 || username.charAt(i) > 57)
        && (username.charAt(i) < 65 || username.charAt(i) > 90)
        && (username.charAt(i) < 97 || username.charAt(i) > 122) && username.charAt(i) != 95) {
            validUsername = false; 
            break;
        }
    }

    if (newUsername && samePassword && validUsername) {
        FullEntity taskEntity =
            Entity.newBuilder(keyFactory.newKey())
                .set("username", username)
                .set("password", encodedPassword)
                .set("timestamp", timestamp)
                .build();
        datastore.put(taskEntity);

        // Print the value so you can see it in the server logs:
        System.out.println("Your username: " + username);
        System.out.println("Your password: " + encodedPassword);
        System.out.println("Your timestamp: " + timestamp);

        response.sendRedirect("rightRegister.html");
    }
    else if (!validUsername) {
        response.setContentType("text/html;");
        response.getWriter().write("The username you entered is not valid.");
        request.getRequestDispatcher("register.html").include(request, response);
    }
    else if (!newUsername) {
        response.setContentType("text/html;");
        response.getWriter().write("The username you entered already exists.");
        request.getRequestDispatcher("register.html").include(request, response);
    }
    else if (!samePassword) {
        response.setContentType("text/html;");
        response.getWriter().write("Both passwords have to match.");
        request.getRequestDispatcher("register.html").include(request, response);
    }
  }
}
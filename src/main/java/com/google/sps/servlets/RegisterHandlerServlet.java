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
import org.apache.commons.validator.routines.EmailValidator;

@WebServlet("/register-handler")
public class RegisterHandlerServlet extends HttpServlet {
  private static String names;
  private static String lastNames;
    
  /*
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html;");
    if (!newUsername) {
        response.getWriter().println("<p>The username you entered already exists.</p>");
    }
    else if (!newEmail) {
        response.getWriter().println("<p>The email you entered already exists.</p>");
    }
  }
  */

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the values entered in the form:
    names = request.getParameter("names");
    lastNames = request.getParameter("lastNames");
    String email = request.getParameter("email");
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    long timestamp = System.currentTimeMillis();
    String encodedPassword = Base64.getEncoder().encodeToString(password.getBytes());
    boolean newUsername = true;
    boolean newEmail = true;
    //boolean validUsername = true;
    boolean validEmail = true;
    boolean nullProperty = false;

    // Print the value so you can see it in the server logs:
    System.out.println("Your name(s): " + names);
    System.out.println("Your last name(s): " + lastNames);
    System.out.println("Your username: " + username);
    System.out.println("Your email: " + email);
    System.out.println("Your password: " + encodedPassword);
    System.out.println("Your timestamp: " + timestamp);

    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    KeyFactory keyFactory = datastore.newKeyFactory().setKind("Task");

    Query<Entity> query =
        Query.newEntityQueryBuilder().setKind("Task").setOrderBy(OrderBy.desc("timestamp")).build();
    QueryResults<Entity> results = datastore.run(query);

    //List<Task> tasks = new ArrayList<>();
    while (results.hasNext()) {
        Entity entity = results.next();

        /*
        long id = entity.getKey().getId();
        String previousNames = entity.getString("names");
        String previousLastNames = entity.getString("lastNames");
        */
        String previousEmail = entity.getString("email");
        String previousUsername = entity.getString("username");
        //long previousTimestamp = entity.getLong("timestamp");

        /*
        Task task = new Task(id, previousNames, previousLastNames,
            previousUsername, previousTimestamp);
        tasks.add(task);
        */

        if (previousUsername.equals(username)) {
            newUsername = false;
            break;
        }

        if (previousEmail.equals(email)) {
            newEmail = false;
            break;
        }
    }

    /*
    for (int i = 0; i < tasks.size(); i++) {
        if (tasks.get(i).getUsername().equals(username)) {
            newUsername = false;
            break;
        }
    }
    */

    //Create the EmailValidator instance:
    EmailValidator validator = EmailValidator.getInstance();
    //Check for valid email addresses using isValid method:
    validEmail = validator.isValid(email);

    if (!newUsername) {
        response.sendRedirect("wrongUsername.html");
    }
    else if (!newEmail) {
        response.sendRedirect("wrongEmail.html");
    }
    else if (!validEmail) {
        response.sendRedirect("invalidEmail.html");
    }
    else if (names.isEmpty() || lastNames.isEmpty() || username.isEmpty()
    || email.isEmpty() || password.isEmpty()) {
        response.sendRedirect("emptyField.html");
    }
    else{
        FullEntity taskEntity =
            Entity.newBuilder(keyFactory.newKey())
                .set("names", names)
                .set("lastNames", lastNames)
                .set("email", email)
                .set("username", username)
                .set("password", encodedPassword)
                .set("timestamp", timestamp)
                .build();
        datastore.put(taskEntity);

        response.sendRedirect("rightRegister.html");
    }
  }
}
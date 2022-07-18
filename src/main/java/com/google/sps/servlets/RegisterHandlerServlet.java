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

@WebServlet("/register-handler")
public class RegisterHandlerServlet extends HttpServlet {
  private static String names;
  private static String lastNames;    

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    doPost(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the values entered in the form:
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    String confirmPassword = request.getParameter("confirmPassword");
    long timestamp = System.currentTimeMillis();
    String encodedPassword = Base64.getEncoder().encodeToString(password.getBytes());
    boolean newUsername = true;
    boolean validUsername = true;
    boolean validEmail = true;
    boolean nullProperty = false;

    // Print the value so you can see it in the server logs:
    System.out.println("Your username: " + username);
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
    }

    /*
    for (int i = 0; i < tasks.size(); i++) {
        if (tasks.get(i).getUsername().equals(username)) {
            newUsername = false;
            break;
        }
    }
    */

    if (!newUsername) {
        response.setContentType("text/html;");
        response.getWriter().println("The username you entered already exists.");
        response.sendRedirect("register.html");
    }
    else if (username.isEmpty() || password.isEmpty()
    || confirmPassword.isEmpty()) {
        response.sendRedirect("emptyField.html");
    }
    else{
        FullEntity taskEntity =
            Entity.newBuilder(keyFactory.newKey())
                .set("username", username)
                .set("password", encodedPassword)
                .set("timestamp", timestamp)
                .build();
        datastore.put(taskEntity);

        response.sendRedirect("rightRegister.html");
    }
  }
}
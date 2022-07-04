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

@WebServlet("/register-handler")
public class RegisterHandlerServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    // Get the values entered in the form:
    String names = request.getParameter("names");
    String lastNames = request.getParameter("lastNames");
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    long timestamp = System.currentTimeMillis();
    boolean newUsername = true;

    // Print the value so you can see it in the server logs:
    System.out.println("Your name(s): " + names);
    System.out.println("Your last name(s): " + lastNames);
    System.out.println("Your username: " + username);
    System.out.println("Your password: " + password);
    System.out.println("Your timestamp: " + timestamp);

    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    KeyFactory keyFactory = datastore.newKeyFactory().setKind("Task");

    Query<Entity> query =
        Query.newEntityQueryBuilder().setKind("Task").setOrderBy(OrderBy.desc("timestamp")).build();
    QueryResults<Entity> results = datastore.run(query);

    List<Task> tasks = new ArrayList<>();
    while (results.hasNext()) {

        Entity entity = results.next();

        long id = entity.getKey().getId();
        String previousNames = entity.getString("names");
        String previousLastNames = entity.getString("lastNames");
        String previousUsername = entity.getString("username");
        long previousTimestamp = entity.getLong("timestamp");

        Task task = new Task(id, previousNames, previousLastNames,
            previousUsername, previousTimestamp);
        tasks.add(task);

    }

    for (int i = 0; i < tasks.size(); i++) {

        if (tasks.get(i).getUsername().equals(username)) {
            newUsername = false;
            break;
        }

    }
    
    /*
    Gson gson = new Gson();
    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(tasks));
    */
    
    /*
    for (int j = 0; j < usernames.length; j++) {

        if (usernames[j] == username) {

            newUsername = false;
            break;

        }

    }
*/

    if (newUsername) {

        FullEntity taskEntity =
            Entity.newBuilder(keyFactory.newKey())
                .set("names", names)
                .set("lastNames", lastNames)
                .set("username", username)
                .set("password", password)
                .set("timestamp", timestamp)
                .build();
        datastore.put(taskEntity);

        response.sendRedirect("rightRegister.html");

    }
    else {

        response.sendRedirect("wrongRegister.html");

    }

    /*
    Gson gson = new Gson();
    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(tasks));
    */

    /*
    // Write the value to the response so the user can see it.
    response.getWriter().println("Your name(s): " + names
        + "\nYour last name(s): " + lastNames
        + "\nYour username: " + username
        + "\nYour password: " + password
        + "\nYour timestamp: " + timestamp);
    */

  }

}
package main.java.com.google.sps.servlets;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

@WebServlet("/login-handler")
public class LoginHandlerServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the value entered in the form.
    String clientName = request.getParameter("clientName");
    String clientPassword = request.getParameter("clientPassword");
    String encodedPassword = Base64.getEncoder().encodeToString(clientPassword.getBytes());
    boolean existUsername = false;
    boolean existRegister = false;

    // Creating variable to interact with Datastore
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    KeyFactory keyFactory = datastore.newKeyFactory().setKind("Task");

    // Print the value so you can see it in the server logs.
    System.out.println(clientName + ": " + encodedPassword);

    Query<Entity> query =
        Query.newEntityQueryBuilder().setKind("Task").setOrderBy(OrderBy.desc("timestamp")).build();
    QueryResults<Entity> results = datastore.run(query);

    //List<task> tasks = new ArrayList<>();
    while(results.hasNext()){
      Entity entity = results.next();

      //long id = entity.getKey().getID();
      String name = entity.getString("username");
      String password = entity.getString("password");
      //long timestamp = entity.getLong("timestamp");

      //Task task = new Task(id, clientName, clientPassword, timestamp);
      //tasks.add(task);

      // Check if the user exists in the database:
      if (name.equals(clientName))
        existUsername = true;

      // Check if the full register exists in the database:
      if (name.equals(clientName) && password.equals(encodedPassword)) {
        existRegister = true;
        break;
      }
    }

    /*
    for (int i = 0; i < tasks.size(); i++){
      if (!tasks.get(i).getUsername().equals(username)){ // sets the variable 'existUsername' to false if the username does not exist in the database
        existUsername = false;
      }
    }
    */

    // what happens after the check
    if (existRegister) {
        response.sendRedirect("rightLogin.html");
    }
    else if (existUsername && !existRegister){
      // true - direct it to their respective page
      response.sendRedirect("incorrectPassword.html");
    }
    else{
      // false - direct to the 'newUser' page which directs to the register page
      response.sendRedirect("wrongLogin.html");
    }
  }
}
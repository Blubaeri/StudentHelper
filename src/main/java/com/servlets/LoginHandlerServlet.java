package com.google.sps.servlets;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login-handler")
public class LoginHandlerServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the value entered in the form.
    String clientName = request.getParameter("clientName");
    String clientPassword = request.getParameter("clientPassword");
    
    // Creating variable to interact with Datastore
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    KeyFactory keyFactory = datastore.newKeyFactory().setKind("Task");

    // Print the value so you can see it in the server logs.
    System.out.println(clientName + ": " + clientPassword);


    Query<Entity> query = Query.newEntityQueryBuilder().setKind("Task").setOrderBy(OrderBY.desc("timestamp")).build();
    QueryResults<Entity> results = datastore.run(query);

    List<task> tasks = new ArrayList<>();
    while(results.hasnext()){
      Entity entity = results.next();

      long id = entity.getKey().getID();
      String name = entity.getString("clientName");
      String password = entity.getString("clientPassword");
      long timestamp = entity.getLong("timestamp");

      Task task = new Task(id, clientName, clientPassword, timestamp);
      tasks.add(task);
    }

    // check if the username exists in the database
    boolean existUsername = true;
    for (int i = 0; i < tasks.size(); i++){
      if (!tasks.get(i).getUsername().equals(username)){ // sets the variable 'existUsername' to false if the username does not exist in the database
        existUsername = false;
      }
    }

    // what happens after the check
    if (existUsername){
      // true - direct it to their respective page
    }
    else{
      // false - direct to the 'newUser' page which directs to the register page
    }


  }
}
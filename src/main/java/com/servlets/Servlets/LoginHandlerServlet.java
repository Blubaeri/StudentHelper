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

    FullEntity taskEntity =
        Entity.newBuilder(keyFactory.newKey())
            .set("clientName", clientName)
            .set("clientPassword", clientPassword)
            .build();
    datastore.put(taskEntity);

    // Redirecting the user to web application's login page.
    response.sendRedirect("/index.html");
  }
}
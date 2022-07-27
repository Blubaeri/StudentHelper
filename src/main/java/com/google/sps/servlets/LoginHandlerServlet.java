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
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession; 

@WebServlet("/login-handler")
public class LoginHandlerServlet extends HttpServlet {
  boolean existUsername = false;
  boolean existRegister = false;

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // Get the value entered in the form.
    String clientName = request.getParameter("clientName");
    String clientPassword = request.getParameter("clientPassword");
    String encodedPassword = Base64.getEncoder().encodeToString(clientPassword.getBytes());

    existUsername = false;
    existRegister = false;

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

      String name = entity.getString("username");
      String password = entity.getString("password");

      // Check if the user exists in the database:
      if (name.equals(clientName))
        existUsername = true;

      // Check if the full register exists in the database:
      if (name.equals(clientName) && password.equals(encodedPassword)) {
        existRegister = true;
        break;
      }
    }

    // what happens after the check
    if (existRegister) {
        response.getWriter().write(clientName);  
        HttpSession session=request.getSession();  
        session.setAttribute("username", clientName);  
        request.getRequestDispatcher("rightLogin.html").include(request, response);
    }
    else if (existUsername && !existRegister){
        response.setContentType("text/html;");
        response.getWriter().write("The password you entered is incorrect.");
        request.getRequestDispatcher("login.html").include(request, response);
    }
    else{
        response.setContentType("text/html;");
        response.getWriter().write("The username you entered does not exist.");
        request.getRequestDispatcher("login.html").include(request, response);
    }
  }
}
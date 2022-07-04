package main.java.com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

/** Handles requests sent to the /hello URL. Try running a server and navigating to /hello! */
@WebServlet("/hello")
public class HelloWorldServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    // Converting to JSON:
    String[] messages = {"Team 11", "Hello T11!", "SPS11"};
    String json = convertToJsonUsingGson(messages);
    
    // Send the JSON as the response:
    //response.setContentType("text/html;");
    //response.getWriter().println("Hello Team 11!");
    response.setContentType("application/json;");
    response.getWriter().println(json);

  }

  /**
   * Converts an instance into a JSON string using the Gson library. Note: We first added
   * the Gson library dependency to pom.xml.
   */
  private String convertToJsonUsingGson(String[] messages) {
    Gson gson = new Gson();
    String json = gson.toJson(messages);
    return json;
  }
}
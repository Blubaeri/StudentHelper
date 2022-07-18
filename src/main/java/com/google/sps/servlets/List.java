// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.Month;

/** Servlet responsible for listing time breakdown. */
@WebServlet("/time-breakdown")
public class ListRecordsServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    
    // TODO: add userid as a filter to fetch data 
    String userId;

    // get today's date as int
    LocalDate currentdate = LocalDate.now();
    int currentDay = currentdate.getDayOfMonth();
    int currentMonth = currentdate.getMonth().getValue();
    int currentYear = currentdate.getYear();
    Query<Entity> query = Query.newEntityQueryBuilder().setKind("Record")
        .setFilter(CompositeFilter.and(
            PropertyFilter.eq("userId", userId), PropertyFilter.eq("year", currentYear),
            PropertyFilter.eq("month", currentMonth), PropertyFilter.eq("day", currentDay)))
        .build();
    QueryResults<Entity> results = datastore.run(query);

    // Calculate the sum of time spent for each catagory
    HashMap<String, String> recordSum = new HashMap<String, Double>();
    while (results.hasNext()) {
      Entity entity = results.next();
      // each Record entity should at least have the following properties: 
      // catagory(String), time(Double), userId(String), date(String)
      String catagory = entity.getString("catagory");
      long time = entity.getDouble("time");
      if (recordSum.containsKey(catagory)) {
        recordSum.replace(catagory, recordSum.get(catagory) + time);
      } else {
        recordSum.put(catagory, time);
      }
    }
    // pass the result as JSON back to client
    Gson gson = new Gson();
    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(recordSum));
  }
}

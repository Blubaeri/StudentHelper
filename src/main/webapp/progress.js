// Load the Visualization API and the corechart package.
google.charts.load('current', {'packages':['corechart']});

// Set a callback to run when the Google Visualization API is loaded.
google.charts.setOnLoadCallback(drawPieChart);

// Callback that creates and populates a data table,
// instantiates the pie chart, passes in the data and
// draws it.
function drawPieChart() {

  // Create the data table. 
  var data = new google.visualization.DataTable();
  data.addColumn('string', 'Catagories');
  data.addColumn('number', 'Time');

  // fetch data(JSON) from server, iterate through each catagory 
  // and add it to the DataTable for the chart
  fetch('/time-breakdown').then(response => response.json()).then((records) => {
    Object.keys(records).forEach(function(key) {
      data.addRows(key, records[key]); //
    })
  });
  
  // Set chart options
  var options = {'title':'How you spent your time today',
                  'is3D':true,
                 'width':1000,
                 'height':750};

  // Instantiate and draw our chart, passing in some options.
  var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
  chart.draw(data, options);
}
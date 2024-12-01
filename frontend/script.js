var data = [];
const colors = [
  'rgba(244, 67, 54, 0.7)',    // Soft Red
  'rgba(76, 175, 80, 0.7)',    // Soft Green
  'rgba(33, 150, 243, 0.7)',   // Soft Blue
  'rgba(255, 193, 7, 0.7)',    // Soft Yellow
  'rgba(156, 39, 176, 0.7)',   // Soft Purple
  'rgba(0, 188, 212, 0.7)',    // Soft Cyan
  'rgba(121, 85, 72, 0.7)',    // Soft Brown
  'rgba(158, 158, 158, 0.7)',  // Soft Gray
  'rgba(96, 125, 139, 0.7)',   // Cool Gray
  'rgba(255, 87, 34, 0.7)',    // Soft Orange
  'rgba(205, 220, 57, 0.7)',   // Lime Green
  'rgba(255, 235, 59, 0.7)',   // Soft Lemon Yellow
  'rgba(103, 58, 183, 0.7)',   // Soft Indigo
  'rgba(63, 81, 181, 0.7)',    // Soft Royal Blue
  'rgba(3, 169, 244, 0.7)',    // Light Sky Blue
  'rgba(0, 150, 136, 0.7)',    // Teal
  'rgba(255, 64, 129, 0.7)',   // Soft Pink
  'rgba(239, 108, 0, 0.7)',    // Deep Orange
  'rgba(191, 54, 12, 0.7)',    // Reddish Brown
  'rgba(124, 179, 66, 0.7)',   // Olive Green
];


const legendsElement = document.getElementById("legends");

const fetchData = async () => {
  try {
    const response = await fetch("../backend/fetchData.php");
    if (!response.ok) {
      throw new Error("Failed to fetch");
    }
    const results = await response.json();
    console.log(results);
    return results;
  } catch (error) {
    console.error("Error:", error);
  }
};

function addYearsOption(data) {
  const filterYear = document.getElementById("yearFilter");

  var years = [];
  data.forEach((item) => {
    if (!years.includes(item.year)) {
      years.push(item.year);
    }
  });

  years.sort(function(a, b) {
    return b - a;
  });

  years.forEach((year) => {
    var option = document.createElement("option");
    option.value = year;
    option.text = year;
    filterYear.appendChild(option);
  });
}

function addLegends(years) {
  for (i = 0; i < years.length; i++) {
    // Create a new div that will contain the assigned color for the label
    var coloredBox = document.createElement("div");
    coloredBox.style.width = "20px"; 
    coloredBox.style.height = "20px"; 
    coloredBox.style.backgroundColor = colors[i]; 
    coloredBox.style.border = "1px solid black";
    coloredBox.style.display = "inline-block";

    // Create a new span element for the label
    var labelSpan = document.createElement("span");
    labelSpan.textContent = years[i]; 
    labelSpan.style.margin = "3px";

    // Create a container div to hold the box and label
    var containerDiv = document.createElement("div");
    containerDiv.style.display = "flex"; 
    containerDiv.style.alignItems = "center"; 
    containerDiv.appendChild(coloredBox); 
    containerDiv.appendChild(labelSpan); 

    // Add the container to the page
    legendsElement.appendChild(containerDiv);
  }
}
const initializeChart = async () => {
  data = await fetchData();

  addYearsOption(data);

  var scoreFilter = document.getElementById("scoreFilter");
  var yearFilter = document.getElementById("yearFilter");
  var avgCtx = document.getElementById("scoreGraph").getContext("2d");
  var scoreChart;

  const filterDataByYears = (data, years) => {
    if (years.includes("allYears")) return data;
    return data.filter((item) => years.includes(item.year));
  };

  const updateChart = (selectedYears, topScores) => {
    var filteredData = filterDataByYears(data, selectedYears);

    filteredData.sort(function (a, b) {
      return b.score - a.score;
    });

    if (topScores >= 0) {
      filteredData = filteredData.slice(0, topScores);
    }

    var usernames = filteredData.map((item) => item.publicName);
    var scores = filteredData.map((item) => item.score);
    var years = filteredData.map((item) => item.year);
    var lastUpdates = filteredData.map((item) => item.last_update);

    var semesterColors = [];

    var uniqueYears = new Set(years);
    var uniqueYearsArray = Array.from(uniqueYears);
    var numOfYears = uniqueYears.size;

    legendsElement.innerHTML = "";

    addLegends(uniqueYearsArray);

    years.forEach((semester) => {
      for (i = 0; i < numOfYears; i++) {
        if (semester == uniqueYearsArray[i]) {
          semesterColors.push(colors[i]);
        }
      }
    });

    if (scoreChart) {
      scoreChart.destroy();
    }

    scoreChart = new Chart(avgCtx, {
      type: "bar",
      data: {
        labels: usernames,
        datasets: [
          {
            label: "Score",
            data: scores,
            backgroundColor: semesterColors,
            borderColor: "black",
            borderWidth: 1,
          },
        ],
      },
      options: {
        indexAxis: "y",
        scales: {
          x: {
            beginAtZero: true,
          },
          y: {
            beginAtZero: true,
            ticks: {
                autoSkip: false
            }
          }
        },
        plugins: {
          tooltip: {
            callbacks: {
              label: function (context) {
                const dataIndex = context.dataIndex;
                const score = scores[dataIndex];
                const lastdate = lastUpdates[dataIndex];
                return ["Score: " + score, "Updated at: " + lastdate];
              },
            },
          },
          legend: {
            display: false,
          }
        },
      },
    });
  };

  updateChart(yearFilter.value, scoreFilter.value);

  scoreFilter.addEventListener("change", function () {
    updateChart(yearFilter.value, scoreFilter.value);
  });

  yearFilter.addEventListener("change", function () {
    const selectedOptions = Array.from(yearFilter.selectedOptions).map(
      (option) => option.value
    );
    updateChart(selectedOptions, scoreFilter.value);
  });
};

initializeChart();

var data = [];
const colors = [
  'rgba(255, 0, 0, 0.7)',
  'rgba(0, 255, 0, 0.7)',
  'rgba(0, 0, 255, 0.7)',
  'rgba(255, 255, 0, 0.7)',
  'rgba(255, 0, 255, 0.7)',
  'rgba(0, 255, 255, 0.7)',
  'rgba(128, 0, 0, 0.7)',
  'rgba(0, 128, 0, 0.7)',
  'rgba(0, 0, 128, 0.7)',
  'rgba(128, 128, 0, 0.7)',
  'rgba(128, 0, 128, 0.7)',
  'rgba(0, 128, 128, 0.7)',
  'rgba(64, 0, 0, 0.7)',
  'rgba(0, 64, 0, 0.7)',
  'rgba(0, 0, 64, 0.7)',
  'rgba(64, 64, 0, 0.7)',
  'rgba(64, 0, 64, 0.7)',
  'rgba(0, 64, 64, 0.7)',
  'rgba(192, 192, 192, 0.7)',
  'rgba(128, 128, 128, 0.7)',
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

function addSemesterOption(data) {
  var selectElement = document.getElementById("semesterFilter");

  var uniqueSemesters = [];
  data.forEach((element) => {
    if (!uniqueSemesters.includes(element.semester)) {
      uniqueSemesters.push(element.semester);
      var newOption = document.createElement("option");
      newOption.value = element.semester;
      newOption.text = element.semester;
      selectElement.appendChild(newOption);
    }
  });
}

function addLegends(semesters) {
  for (i = 0; i < semesters.length; i++) {
    var coloredBox = document.createElement("div");
    coloredBox.style.width = "20px"; // Set the width of the box
    coloredBox.style.height = "20px"; // Set the height of the box
    coloredBox.style.backgroundColor = colors[i]; // Set the background color
    coloredBox.style.display = "inline-block"; // Display the box inline

    // Create a new span element for the label
    var labelSpan = document.createElement("span");
    labelSpan.textContent = semesters[i]; // Set the label text

    // Create a container div to hold the box and label
    var containerDiv = document.createElement("div");
    containerDiv.appendChild(coloredBox); // Add the colored box to the container
    containerDiv.appendChild(labelSpan); // Add the label to the container

    // Add the container to the page
    legendsElement.appendChild(containerDiv);
  }
}
const initializeChart = async () => {
  data = await fetchData();

  addSemesterOption(data);

  var scoreFilter = document.getElementById("scoreFilter");
  var semesterFilter = document.getElementById("semesterFilter");
  var avgCtx = document.getElementById("scoreGraph").getContext("2d");
  var scoreChart;

  const filterDataBySemester = (data, semesters) => {
    if (semesters.includes("allSemesters")) return data;
    return data.filter((item) => semesters.includes(item.semester));
  };

  const updateChart = (selectedSemesters, topScores) => {
    var filteredData = filterDataBySemester(data, selectedSemesters);

    filteredData.sort(function (a, b) {
      return b.score - a.score;
    });

    if (topScores >= 0) {
      filteredData = filteredData.slice(0, topScores);
    }

    var usernames = filteredData.map((item) => item.publicName);
    var scores = filteredData.map((item) => item.score);
    var semesters = filteredData.map((item) => item.semester);
    var lastDates = filteredData.map((item) => item.lastDate);

    var semesterColors = [];

    var uniqueSemesters = new Set(semesters);
    var uniqueSemestersArray = Array.from(uniqueSemesters);
    var numOfSemesters = uniqueSemesters.size;

    legendsElement.innerHTML = "";

    addLegends(uniqueSemestersArray);

    semesters.forEach((semester) => {
      for (i = 0; i < numOfSemesters; i++) {
        if (semester == uniqueSemestersArray[i]) {
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
        },
        plugins: {
          tooltip: {
            callbacks: {
              label: function (context) {
                const dataIndex = context.dataIndex;
                const score = scores[dataIndex];
                const lastdate = lastDates[dataIndex];
                return ["Score: " + score, "Updated at: " + lastdate];
              },
            },
          },
          legend: {
            display: false,
          },
        },
      },
    });
  };

  updateChart(semesterFilter.value, scoreFilter.value);

  scoreFilter.addEventListener("change", function () {
    updateChart(semesterFilter.value, scoreFilter.value);
  });

  semesterFilter.addEventListener("change", function () {
    const selectedOptions = Array.from(semesterFilter.selectedOptions).map(
      (option) => option.value
    );
    updateChart(selectedOptions, scoreFilter.value);
  });
};

initializeChart();

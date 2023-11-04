<?php
    include 'db.php';

    if($_SERVER['REQUEST_METHOD'] !== 'GET'){
        die();
    } else {
        $query = "SELECT publicName, score, semester, lastDate FROM students";
        $result = $conn->query($query);

        if ($result) {
            $data = [];
            if ($result->num_rows > 0) {
                while ($row = $result->fetch_assoc()) {
                    array_push($data, $row);
                }
                echo json_encode($data);
            } else {
                echo json_encode(["message" => "No records found in the 'students' table."]);
            }
        } else {
            echo json_encode(["message" => "Error executing the SQL query: " . $conn->error]);
        }
    }

    $conn->close();
?>
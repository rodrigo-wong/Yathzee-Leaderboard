<?php

    include 'db.php';

    $headers = getallheaders();

    if(empty($headers["API_KEY"])) {
        echo "Missing API KEY";
        die();
    }

    if ($_SERVER['REQUEST_METHOD'] === 'PUT') {

        $hashedPassword = '$2y$10$DUcoQHMuikvydlMmsuyac.DBbeYhT.aNxlyCHWC0EqfiApaE0cwW.'; // Store this in an environment variable ?
        if(!password_verify($headers["API_KEY"], $hashedPassword)) {
            echo "Invalid API KEY";
            die();
        }
        $data = file_get_contents("php://input");
        $putData = json_decode($data, true);

        $student = [];

        if (empty($putData['publicName']) || empty($putData['score'])) {
            echo "Missing information";
            die();
        } else {
            $publicName = filter_var($putData['publicName'], FILTER_SANITIZE_STRING);
            $score = filter_var($putData['score'],FILTER_SANITIZE_NUMBER_FLOAT, FILTER_FLAG_ALLOW_FRACTION);

            $query = "SELECT * FROM students WHERE publicName = ?";
            $stmt = $conn->prepare($query);

            $stmt->bind_param('s', $publicName);
            $stmt->execute();
            $result = $stmt->get_result();
            $student = $result->fetch_assoc();

            if ($student) {
                $currentScore = $student['score'];

                if ($score > $currentScore) {
                    $updateQuery = "UPDATE students SET score = ?, lastDate = CURRENT_TIMESTAMP() WHERE publicName = ?";
                    $updateStmt = $conn->prepare($updateQuery);
                    $updateStmt->bind_param('ds', $score, $publicName);

                    if ($updateStmt->execute()) {
                        $student['score'] = $score;
                        echo "Score updated\n";
                        echo json_encode($student);
                    } else {
                        echo "Error updating the score: " . $updateStmt->error;
                    }

                    $updateStmt->close();
                } else {
                    echo "No score update required\n";
                }
            } else {
                echo "Student not on record\n";
            }

            $stmt->close();
        }
    }

    $conn->close();

?>

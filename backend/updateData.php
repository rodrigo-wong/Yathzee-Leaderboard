<?php

    include 'db.php';

    $student = [];

    if ($_SERVER['REQUEST_METHOD'] === 'PUT') {

        $data = file_get_contents("php://input");

        $putData = json_decode($data, true);

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

<?php
    $conn = new mysqli("localhost", "root", "", "project1");

    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }
?>

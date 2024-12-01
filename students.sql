-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Dec 07, 2023 at 09:39 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `project1`
--

-- --------------------------------------------------------

--
-- Table structure for table `students`
--

CREATE TABLE `students` (
  `student_id` int(11) NOT NULL,
  `publicName` varchar(255) NOT NULL,
  `year` int(11) NOT NULL DEFAULT year(curdate()),
  `score` float DEFAULT NULL,
  `last_update` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `students`
--

INSERT INTO `students` (`student_id`, `publicName`, `year`, `score`, `last_update`) VALUES
(1, 's1', 2020, 151.65, '2023-11-05 21:06:39'),
(2, 's2', 2023, 81, '2023-10-26 08:00:00'),
(3, 's3', 2019, 93, '2023-10-25 08:00:00'),
(4, 's4', 2019, 151.68, '2023-11-05 21:18:41'),
(5, 's5', 2023, 77, '2023-10-25 08:00:00'),
(6, 's6', 2022, 88, '2023-10-25 08:00:00'),
(7, 's7', 2021, 65, '2023-10-25 08:00:00'),
(8, 's8', 2023, 77, '2023-10-25 08:00:00'),
(9, 's9', 2018, 151.75, '2023-11-07 00:16:41'),
(10, 's10', 2022, 95, '2023-10-25 08:00:00'),
(11, 's11', 2019, 74, '2023-10-26 08:00:00'),
(12, 's12', 2022, 100, '2023-10-27 08:00:00'),
(13, 's13', 2018, 80, '2023-10-25 08:00:00'),
(14, 's14', 2018, 78, '2023-10-25 08:00:00'),
(15, 's15', 2020, 84, '2023-10-25 08:00:00'),
(16, 's16', 2020, 92, '2023-10-25 08:00:00'),
(17, 's17', 2018, 75, '2023-10-26 08:00:00'),
(18, 's18', 2020, 86, '2023-10-25 08:00:00'),
(19, 's19', 2021, 62, '2023-10-25 08:00:00'),
(20, 's20', 2021, 91, '2023-10-25 08:00:00'),
(21, 's21', 2019, 75, '2023-10-25 08:00:00'),
(22, 's22', 2022, 80, '2023-10-25 08:00:00'),
(23, 's23', 2021, 68, '2023-10-25 08:00:00'),
(24, 's24', 2019, 79, '2023-10-25 08:00:00'),
(25, 's25', 2023, 83, '2023-10-25 08:00:00'),
(26, 's26', 2022, 70, '2023-10-25 08:00:00'),
(27, 's27', 2020, 88, '2023-10-25 08:00:00'),
(28, 's28', 2022, 100, '2023-10-26 08:00:00'),
(29, 's29', 2019, 73, '2023-10-25 08:00:00'),
(30, 's30', 2018, 87, '2023-10-25 08:00:00'),
(31, 's31', 2022, 85, '2023-10-29 08:00:00'),
(32, 's32', 2020, 72, '2023-10-29 08:00:00'),
(33, 's33', 2023, 93, '2023-10-29 08:00:00'),
(34, 's34', 2020, 68, '2023-10-29 08:00:00'),
(35, 's35', 2021, 77, '2023-10-29 08:00:00'),
(36, 's36', 2018, 92, '2023-10-29 08:00:00'),
(37, 's37', 2019, 78, '2023-10-29 08:00:00'),
(38, 's38', 2022, 85, '2023-10-29 08:00:00'),
(39, 's39', 2022, 70, '2023-10-29 08:00:00'),
(40, 's40', 2023, 64, '2023-10-29 08:00:00'),
(41, 's41', 2019, 82, '2023-10-29 08:00:00'),
(42, 's42', 2018, 75, '2023-10-29 08:00:00'),
(43, 's43', 2019, 91, '2023-10-29 08:00:00'),
(44, 's44', 2022, 87, '2023-10-29 08:00:00'),
(45, 's45', 2018, 86, '2023-11-05 20:43:36'),
(46, 's46', 2022, 73, '2023-10-29 08:00:00'),
(47, 's47', 2023, 96, '2023-10-29 08:00:00');
COMMIT;

-- Indexes for table `students`
--
ALTER TABLE `students`
  ADD PRIMARY KEY (`student_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `students`
--
ALTER TABLE `students`
  MODIFY `student_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=48;
COMMIT;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

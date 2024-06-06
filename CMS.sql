-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Feb 12, 2024 at 10:44 AM
-- Server version: 10.4.27-MariaDB
-- PHP Version: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `CMS`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `AdminID` int(11) NOT NULL,
  `FullName` varchar(100) DEFAULT NULL,
  `Contact` varchar(20) DEFAULT NULL,
  `Password` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`AdminID`, `FullName`, `Contact`, `Password`) VALUES
(211, 'Pra Dip', '987623563', 'pra123'),
(1001, 'Admin Bahadur', '9876543210', 'admin123');

-- --------------------------------------------------------

--
-- Table structure for table `Courses`
--

CREATE TABLE `Courses` (
  `CourseID` varchar(10) NOT NULL,
  `CourseName` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `Courses`
--

INSERT INTO `Courses` (`CourseID`, `CourseName`) VALUES
('BBA000', 'Bachelor in Business Administration'),
('CS000', 'Bachelor in Computer Science (Hons)'),
('IMBA000', 'International Masters Of Business Administration');

-- --------------------------------------------------------

--
-- Table structure for table `Marks`
--

CREATE TABLE `Marks` (
  `MarkID` int(11) NOT NULL,
  `StudentID` varchar(10) DEFAULT NULL,
  `CourseID` varchar(10) DEFAULT NULL,
  `ModuleID` varchar(10) DEFAULT NULL,
  `Marks` float DEFAULT NULL,
  `Approve` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `Marks`
--

INSERT INTO `Marks` (`MarkID`, `StudentID`, `CourseID`, `ModuleID`, `Marks`, `Approve`) VALUES
(1, '1002', 'CS000', '4CS001', 90, 'Yes'),
(2, '1001', 'CS000', '4CS001', 95, 'Yes'),
(3, '1004', 'BBA000', '3BU002', 70, NULL),
(4, '1001', 'CS000', '4CS015', 90, 'Yes');

-- --------------------------------------------------------

--
-- Table structure for table `Modules`
--

CREATE TABLE `Modules` (
  `ModuleID` varchar(10) NOT NULL,
  `ModuleName` varchar(255) NOT NULL,
  `CourseID` varchar(10) DEFAULT NULL,
  `Level` varchar(50) NOT NULL,
  `TeacherID` varchar(10) DEFAULT NULL,
  `ModuleChoice` varchar(255) DEFAULT NULL,
  `Enroll` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `Modules`
--

INSERT INTO `Modules` (`ModuleID`, `ModuleName`, `CourseID`, `Level`, `TeacherID`, `ModuleChoice`, `Enroll`) VALUES
('3BU002', '21st Century Management', 'BBA000', '4', '1111', 'Compulsory', 'Yes'),
('3BU003', 'Principles of Business', 'BBA000', '4', NULL, 'Compulsory', 'Yes'),
('3GK012', 'Preparing for Sucess at University', 'BBA000', '4', NULL, 'Compulsory', 'Yes'),
('4CS001', 'Introduction to Programming and Problem Solving', 'CS000', '4', '0000', 'Compulsory', 'Yes'),
('4CS015', 'Fundamentals of Computing', 'CS000', '4', NULL, 'Compulsory', 'Yes'),
('4CS017', 'Internet Software Architecture', 'CS000', '4', '12345', 'Compulsory', 'Yes'),
('4CS020', 'Introduction to Games Technology for Serious Applications', 'CS000', '4', NULL, 'Compulsory', 'Yes'),
('4CS021', 'Introduction to Object-Oriented Programming', 'CS000', '4', NULL, 'Compulsory', 'Yes'),
('4MM013', 'Computational Mathematics', 'CS000', '4', NULL, 'Compulsory', 'Yes'),
('5CS019', 'Object-Oriented Design and Programming', 'CS000', '5', NULL, 'Compulsory', 'Yes'),
('5CS021', 'Numerical Method and Concurrency', 'CS000', '5', NULL, 'Compulsory', 'Yes'),
('5CS022', 'Distributed and Cloud System Programmin', 'CS000', '5', NULL, 'Compulsory', 'Yes'),
('5CS024', 'Collaborative Development', 'CS000', '5', NULL, 'Compulsory', 'Yes'),
('6CS014 ', 'Complex Systems', 'CS000', '6', NULL, 'Optional', 'Yes'),
('6CS030', 'Big Data', 'CS000', '6', NULL, 'Compulsory', 'Yes');

-- --------------------------------------------------------

--
-- Table structure for table `Student`
--

CREATE TABLE `Student` (
  `StudentID` varchar(10) NOT NULL,
  `StudentName` varchar(100) DEFAULT NULL,
  `Level` varchar(50) DEFAULT NULL,
  `Contact` varchar(20) DEFAULT NULL,
  `Password` varchar(50) DEFAULT NULL,
  `CourseID` varchar(10) DEFAULT NULL,
  `MarkID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `Student`
--

INSERT INTO `Student` (`StudentID`, `StudentName`, `Level`, `Contact`, `Password`, `CourseID`, `MarkID`) VALUES
('1001', 'Pragya Aryal', '4', '9761873901', 'pragya123', 'CS000', 1),
('1002', 'Kalu Pandey', '4', '9854278933', 'kalu123', 'CS000', 2),
('1003', 'Diparshan Chauhan', '5', '9860444190', 'dipu123', 'CS000', NULL),
('1004', 'Pranav Aryal', '4', '9876543210', 'pranav123', 'BBA000', NULL),
('1005', 'Shyam Bahadur', '6', '9876534262', 'shyam123', 'CS000', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `studentModules`
--

CREATE TABLE `studentModules` (
  `StudentID` varchar(50) NOT NULL,
  `ModuleID` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `studentModules`
--

INSERT INTO `studentModules` (`StudentID`, `ModuleID`) VALUES
('1005', '6CS014 ');

-- --------------------------------------------------------

--
-- Table structure for table `Teachers`
--

CREATE TABLE `Teachers` (
  `TeacherName` varchar(255) NOT NULL,
  `Contact` varchar(20) NOT NULL,
  `Password` varchar(255) NOT NULL,
  `ModuleID` varchar(10) DEFAULT NULL,
  `CourseID` varchar(10) DEFAULT NULL,
  `TeacherID` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `Teachers`
--

INSERT INTO `Teachers` (`TeacherName`, `Contact`, `Password`, `ModuleID`, `CourseID`, `TeacherID`) VALUES
('Ram Lal', '9876535652', 'ram123', '4CS001', 'CS000', '0000'),
('Sam Thapa', '987662379', 'sam123', '3BU002', 'BBA000', '1111'),
('Hari Bahadur', '9876543210', 'hari123', '4CS015', 'CS000', '12345'),
('Pradip Pandey', '9876527122', 'pradip123', '6CS030', 'CS000', '2222');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`AdminID`);

--
-- Indexes for table `Courses`
--
ALTER TABLE `Courses`
  ADD PRIMARY KEY (`CourseID`);

--
-- Indexes for table `Marks`
--
ALTER TABLE `Marks`
  ADD PRIMARY KEY (`MarkID`),
  ADD KEY `StudentID` (`StudentID`),
  ADD KEY `CourseID` (`CourseID`),
  ADD KEY `ModuleID` (`ModuleID`);

--
-- Indexes for table `Modules`
--
ALTER TABLE `Modules`
  ADD PRIMARY KEY (`ModuleID`),
  ADD KEY `CourseID` (`CourseID`),
  ADD KEY `fk_TeacherID` (`TeacherID`);

--
-- Indexes for table `Student`
--
ALTER TABLE `Student`
  ADD PRIMARY KEY (`StudentID`),
  ADD KEY `FK_student_Course` (`CourseID`),
  ADD KEY `MarkID` (`MarkID`);

--
-- Indexes for table `studentModules`
--
ALTER TABLE `studentModules`
  ADD PRIMARY KEY (`StudentID`,`ModuleID`),
  ADD KEY `ModuleID` (`ModuleID`);

--
-- Indexes for table `Teachers`
--
ALTER TABLE `Teachers`
  ADD PRIMARY KEY (`TeacherID`),
  ADD KEY `ModuleID` (`ModuleID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Marks`
--
ALTER TABLE `Marks`
  MODIFY `MarkID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `Marks`
--
ALTER TABLE `Marks`
  ADD CONSTRAINT `marks_ibfk_1` FOREIGN KEY (`StudentID`) REFERENCES `student` (`StudentID`),
  ADD CONSTRAINT `marks_ibfk_2` FOREIGN KEY (`CourseID`) REFERENCES `Courses` (`CourseID`),
  ADD CONSTRAINT `marks_ibfk_3` FOREIGN KEY (`ModuleID`) REFERENCES `Modules` (`ModuleID`);

--
-- Constraints for table `Modules`
--
ALTER TABLE `Modules`
  ADD CONSTRAINT `fk_TeacherID` FOREIGN KEY (`TeacherID`) REFERENCES `Teachers` (`TeacherID`),
  ADD CONSTRAINT `modules_ibfk_1` FOREIGN KEY (`CourseID`) REFERENCES `Courses` (`CourseID`);

--
-- Constraints for table `Student`
--
ALTER TABLE `Student`
  ADD CONSTRAINT `FK_student_Course` FOREIGN KEY (`CourseID`) REFERENCES `Courses` (`CourseID`),
  ADD CONSTRAINT `student_ibfk_1` FOREIGN KEY (`MarkID`) REFERENCES `Marks` (`MarkID`);

--
-- Constraints for table `studentModules`
--
ALTER TABLE `studentModules`
  ADD CONSTRAINT `studentmodules_ibfk_1` FOREIGN KEY (`StudentID`) REFERENCES `student` (`StudentID`),
  ADD CONSTRAINT `studentmodules_ibfk_2` FOREIGN KEY (`ModuleID`) REFERENCES `Modules` (`ModuleID`);

--
-- Constraints for table `Teachers`
--
ALTER TABLE `Teachers`
  ADD CONSTRAINT `teachers_ibfk_1` FOREIGN KEY (`ModuleID`) REFERENCES `Modules` (`ModuleID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

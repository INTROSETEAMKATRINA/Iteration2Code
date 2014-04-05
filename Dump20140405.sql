CREATE DATABASE  IF NOT EXISTS `payroll system` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `payroll system`;
-- MySQL dump 10.13  Distrib 5.6.12, for Win64 (x86_64)
--
-- Host: localhost    Database: payroll system
-- ------------------------------------------------------
-- Server version	5.6.12

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `adjustmentsanddeductions`
--

DROP TABLE IF EXISTS `adjustmentsanddeductions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `adjustmentsanddeductions` (
  `amount` decimal(10,2) NOT NULL,
  `type` varchar(30) NOT NULL,
  `PeriodStartDate` date NOT NULL,
  `TIN` varchar(20) NOT NULL,
  PRIMARY KEY (`PeriodStartDate`,`TIN`,`type`),
  KEY `PersonnelAdjustmentsAndDeductions_idx` (`TIN`),
  CONSTRAINT `TINAdjustmentAndDeductions` FOREIGN KEY (`TIN`) REFERENCES `personnel` (`TIN`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `adjustmentsanddeductions`
--

LOCK TABLES `adjustmentsanddeductions` WRITE;
/*!40000 ALTER TABLE `adjustmentsanddeductions` DISABLE KEYS */;
/*!40000 ALTER TABLE `adjustmentsanddeductions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `client`
--

DROP TABLE IF EXISTS `client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `client` (
  `Name` varchar(30) NOT NULL,
  `rotVar` decimal(10,2) DEFAULT NULL,
  `rnsdVar` decimal(10,2) DEFAULT NULL,
  `lhRate` decimal(10,2) DEFAULT NULL,
  `lhVar` decimal(10,2) DEFAULT NULL,
  `lhOTVar` decimal(10,2) DEFAULT NULL,
  `lhNSDVar` decimal(10,2) DEFAULT NULL,
  `lhRDVar` decimal(10,2) DEFAULT NULL,
  `shRate` decimal(10,2) DEFAULT NULL,
  `shVar` decimal(10,2) DEFAULT NULL,
  `shOTVar` decimal(10,2) DEFAULT NULL,
  `shNSDVar` decimal(10,2) DEFAULT NULL,
  `shRDVar` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`Name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client`
--

LOCK TABLES `client` WRITE;
/*!40000 ALTER TABLE `client` DISABLE KEYS */;
/*!40000 ALTER TABLE `client` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dtr`
--

DROP TABLE IF EXISTS `dtr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dtr` (
  `RDW` decimal(10,2) DEFAULT NULL,
  `ROT` decimal(10,2) DEFAULT NULL,
  `RNSD` decimal(10,2) DEFAULT NULL,
  `SH` decimal(10,2) DEFAULT NULL,
  `SHOT` decimal(10,2) DEFAULT NULL,
  `SHNSD` decimal(10,2) DEFAULT NULL,
  `SHRD` decimal(10,2) DEFAULT NULL,
  `LH` decimal(10,2) DEFAULT NULL,
  `LHOT` decimal(10,2) DEFAULT NULL,
  `LHNSD` decimal(10,2) DEFAULT NULL,
  `LHRD` decimal(10,2) DEFAULT NULL,
  `late` decimal(10,2) DEFAULT NULL,
  `PeriodStartDate` date NOT NULL,
  `TIN` varchar(20) NOT NULL,
  PRIMARY KEY (`PeriodStartDate`,`TIN`),
  KEY `TINDTR_idx` (`TIN`),
  CONSTRAINT `TINDTR` FOREIGN KEY (`TIN`) REFERENCES `personnel` (`TIN`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dtr`
--

LOCK TABLES `dtr` WRITE;
/*!40000 ALTER TABLE `dtr` DISABLE KEYS */;
/*!40000 ALTER TABLE `dtr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `password`
--

DROP TABLE IF EXISTS `password`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `password` (
  `Password` varchar(20) NOT NULL,
  PRIMARY KEY (`Password`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `password`
--

LOCK TABLES `password` WRITE;
/*!40000 ALTER TABLE `password` DISABLE KEYS */;
INSERT INTO `password` VALUES ('gallant2010');
/*!40000 ALTER TABLE `password` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payslip`
--

DROP TABLE IF EXISTS `payslip`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payslip` (
  `Assignment` varchar(30) NOT NULL,
  `Name` varchar(50) NOT NULL,
  `PeriodStartDate` date NOT NULL,
  `TIN` varchar(20) NOT NULL,
  `Position` varchar(30) DEFAULT NULL,
  `RDW` decimal(10,2) unsigned DEFAULT NULL,
  `DailyRate` decimal(10,2) DEFAULT NULL,
  `GrossPay` decimal(10,2) DEFAULT NULL,
  `Late` int(11) DEFAULT NULL,
  `RegularPay` decimal(10,2) DEFAULT NULL,
  `ROT` decimal(10,2) DEFAULT NULL,
  `ROTPay` decimal(10,2) DEFAULT NULL,
  `RNSD` decimal(10,2) DEFAULT NULL,
  `RNSDPay` decimal(10,2) DEFAULT NULL,
  `LH` decimal(10,2) DEFAULT NULL,
  `LHPay` decimal(10,2) DEFAULT NULL,
  `LHOT` decimal(10,2) DEFAULT NULL,
  `LHOTPay` decimal(10,2) DEFAULT NULL,
  `LHNSD` decimal(10,2) DEFAULT NULL,
  `LHNSDPay` decimal(10,2) DEFAULT NULL,
  `LHRD` decimal(10,2) DEFAULT NULL,
  `LHRDPay` decimal(10,2) DEFAULT NULL,
  `SH` decimal(10,2) DEFAULT NULL,
  `SHPay` decimal(10,2) DEFAULT NULL,
  `SHOT` decimal(10,2) DEFAULT NULL,
  `SHOTPay` decimal(10,2) DEFAULT NULL,
  `SHNSD` decimal(10,2) DEFAULT NULL,
  `SHNSDPay` decimal(10,2) DEFAULT NULL,
  `SHRD` decimal(10,2) DEFAULT NULL,
  `SHRDPay` decimal(10,2) DEFAULT NULL,
  `TranspoAllow` decimal(10,2) DEFAULT NULL,
  `Adjustments` decimal(10,2) DEFAULT NULL,
  `WTax` decimal(10,2) DEFAULT NULL,
  `SSS` decimal(10,2) DEFAULT NULL,
  `PHIC` decimal(10,2) DEFAULT NULL,
  `HDMF` decimal(10,2) DEFAULT NULL,
  `SSSLoan` decimal(10,2) DEFAULT NULL,
  `Savings` decimal(10,2) DEFAULT NULL,
  `PayrollAdvance` decimal(10,2) DEFAULT NULL,
  `HouseRental` decimal(10,2) DEFAULT NULL,
  `UniformAndOthers` decimal(10,2) DEFAULT NULL,
  `NetPay` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`TIN`,`PeriodStartDate`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payslip`
--

LOCK TABLES `payslip` WRITE;
/*!40000 ALTER TABLE `payslip` DISABLE KEYS */;
/*!40000 ALTER TABLE `payslip` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `personnel`
--

DROP TABLE IF EXISTS `personnel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `personnel` (
  `Name` varchar(50) DEFAULT NULL,
  `Assignment` varchar(30) DEFAULT NULL,
  `Position` varchar(30) DEFAULT NULL,
  `EmployeeStatus` varchar(20) DEFAULT NULL,
  `DailyRate` decimal(10,2) DEFAULT NULL,
  `ColaRate` decimal(10,2) DEFAULT NULL,
  `MonthlyRate` decimal(10,2) DEFAULT NULL,
  `TIN` varchar(20) NOT NULL,
  `TaxStatus` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`TIN`),
  KEY `ClientPersonnel_idx` (`Assignment`),
  CONSTRAINT `TINClient` FOREIGN KEY (`Assignment`) REFERENCES `client` (`Name`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `personnel`
--

LOCK TABLES `personnel` WRITE;
/*!40000 ALTER TABLE `personnel` DISABLE KEYS */;
/*!40000 ALTER TABLE `personnel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `taxtable`
--

DROP TABLE IF EXISTS `taxtable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `taxtable` (
  `Length` varchar(20) DEFAULT NULL,
  `TaxStatus` varchar(20) DEFAULT NULL,
  `Bracket1` decimal(10,2) DEFAULT NULL,
  `Bracket2` decimal(10,2) DEFAULT NULL,
  `Bracket3` decimal(10,2) DEFAULT NULL,
  `Bracket4` decimal(10,2) DEFAULT NULL,
  `Bracket5` decimal(10,2) DEFAULT NULL,
  `Bracket6` decimal(10,2) DEFAULT NULL,
  `Bracket7` decimal(10,2) DEFAULT NULL,
  `Bracket8` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `taxtable`
--

LOCK TABLES `taxtable` WRITE;
/*!40000 ALTER TABLE `taxtable` DISABLE KEYS */;
/*!40000 ALTER TABLE `taxtable` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-04-05 19:35:17

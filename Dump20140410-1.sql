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
  `Bracket` tinyint(4) NOT NULL,
  `Tax` decimal(10,2) NOT NULL,
  `TaxPercentOver` decimal(10,2) NOT NULL,
  `Z` decimal(10,2) NOT NULL,
  `SME` decimal(10,2) NOT NULL,
  `S1ME1` decimal(10,2) NOT NULL,
  `S2ME2` decimal(10,2) NOT NULL,
  `S3ME3` decimal(10,2) NOT NULL,
  `S4ME4` decimal(10,2) NOT NULL,
  PRIMARY KEY (`Bracket`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `taxtable`
--

LOCK TABLES `taxtable` WRITE;
/*!40000 ALTER TABLE `taxtable` DISABLE KEYS */;
INSERT INTO `taxtable` VALUES (1,0.00,0.00,1.00,1.00,1.00,1.00,1.00,1.00),(2,0.00,5.00,0.00,2083.00,3125.00,4167.00,5208.00,6250.00),(3,20.83,10.00,417.00,2500.00,3542.00,4583.00,5625.00,6667.00),(4,104.17,15.00,1250.00,3333.00,4375.00,5417.00,6458.00,7500.00),(5,354.17,20.00,2917.00,5000.00,6042.00,7083.00,8125.00,9167.00),(6,937.50,25.00,5833.00,7917.00,8958.00,10000.00,11042.00,12083.00),(7,2083.33,30.00,10417.00,12500.00,13542.00,14583.00,15625.00,16667.00),(8,5208.33,32.00,20833.00,22917.00,23958.00,25000.00,26042.00,27083.00);
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

-- Dump completed on 2014-04-10 21:37:58

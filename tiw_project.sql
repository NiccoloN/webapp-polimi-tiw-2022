CREATE DATABASE  IF NOT EXISTS `tiw_project` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `tiw_project`;
-- MySQL dump 10.13  Distrib 8.0.28, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: tiw_project
-- ------------------------------------------------------
-- Server version	8.0.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `contatto`
--

DROP TABLE IF EXISTS `contatto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contatto` (
  `Utente` varchar(20) NOT NULL,
  `ContoContatto` int NOT NULL,
  `UsernameContatto` varchar(20) NOT NULL,
  PRIMARY KEY (`Utente`,`ContoContatto`),
  KEY `ContoContatto_idx` (`ContoContatto`),
  KEY `UsernameContatto_idx` (`UsernameContatto`),
  CONSTRAINT `ContoContatto` FOREIGN KEY (`ContoContatto`) REFERENCES `conto` (`Codice`) ON UPDATE CASCADE,
  CONSTRAINT `UsernameContatto` FOREIGN KEY (`UsernameContatto`) REFERENCES `conto` (`Utente`) ON UPDATE CASCADE,
  CONSTRAINT `UtenteContatto` FOREIGN KEY (`Utente`) REFERENCES `utente` (`Username`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contatto`
--

LOCK TABLES `contatto` WRITE;
/*!40000 ALTER TABLE `contatto` DISABLE KEYS */;
/*!40000 ALTER TABLE `contatto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `conto`
--

DROP TABLE IF EXISTS `conto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `conto` (
  `Codice` int NOT NULL AUTO_INCREMENT,
  `Saldo` decimal(12,2) NOT NULL,
  `Utente` varchar(20) NOT NULL,
  PRIMARY KEY (`Codice`),
  KEY `Utente` (`Utente`),
  CONSTRAINT `Utente` FOREIGN KEY (`Utente`) REFERENCES `utente` (`Username`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conto`
--

LOCK TABLES `conto` WRITE;
/*!40000 ALTER TABLE `conto` DISABLE KEYS */;
INSERT INTO `conto` VALUES (1,199.00,'nume'),(2,15.00,'user2'),(3,-25.00,'user2'),(4,999998860.57,'nume'),(5,400.00,'nick'),(6,46.15,'user1'),(7,1038.98,'user1'),(8,0.00,'nume'),(9,0.00,'nick');
/*!40000 ALTER TABLE `conto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trasferimento`
--

DROP TABLE IF EXISTS `trasferimento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trasferimento` (
  `Codice` int unsigned NOT NULL AUTO_INCREMENT,
  `Data` date NOT NULL,
  `Importo` decimal(12,2) unsigned NOT NULL,
  `ContoOrigine` int NOT NULL,
  `ContoDestinazione` int NOT NULL,
  `Causale` varchar(150) DEFAULT 'No causal',
  PRIMARY KEY (`Codice`),
  KEY `ContoOrigine_idx` (`ContoOrigine`),
  KEY `ContoDestinazione_idx` (`ContoDestinazione`),
  CONSTRAINT `ContoDestinazione` FOREIGN KEY (`ContoDestinazione`) REFERENCES `conto` (`Codice`) ON UPDATE CASCADE,
  CONSTRAINT `ContoOrigine` FOREIGN KEY (`ContoOrigine`) REFERENCES `conto` (`Codice`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trasferimento`
--

LOCK TABLES `trasferimento` WRITE;
/*!40000 ALTER TABLE `trasferimento` DISABLE KEYS */;
INSERT INTO `trasferimento` VALUES (1,'2022-05-24',200.45,4,1,'Paghetta'),(4,'2022-05-23',15.00,4,2,'Regalo'),(5,'2022-05-25',10.00,4,3,'Regalo'),(6,'2022-05-26',35.00,3,6,'Tassa universitaria'),(7,'2022-05-20',10.25,4,6,'No causal'),(8,'2022-05-15',300.00,4,5,'No causal'),(9,'2022-06-28',25.46,4,5,'Birretta'),(10,'2022-06-28',25.46,4,5,'Birretta'),(11,'2022-06-28',25.46,4,5,'Birretta'),(12,'2022-06-28',12.00,4,9,'Pizza'),(13,'2022-06-28',11.00,9,4,'Resto della pizza'),(14,'2022-06-28',1.00,9,4,'No causal'),(15,'2022-06-28',11.00,4,5,'Hai sbagliato'),(16,'2022-06-28',11.62,4,5,'No causal'),(17,'2022-06-28',1.00,4,5,'No causal'),(18,'2022-06-28',1.00,5,4,'Smettila di darmi soldi a caso'),(19,'2022-06-28',1.00,4,5,'Ma devo debuggare'),(20,'2022-06-28',1.00,5,4,'Non mi interessa!'),(21,'2022-06-28',1.00,4,5,'Ma altrimenti non passo l\'esame...'),(22,'2022-06-28',1.00,5,4,'Va bene allora...'),(23,'2022-06-28',0.45,1,6,'test'),(24,'2022-06-28',0.45,1,6,'test'),(25,'2022-06-28',1.00,4,5,'Che poi è anche il tuo esame'),(26,'2022-06-28',2.00,4,7,'Sei in arresto!'),(27,'2022-06-28',0.55,1,7,'Non voglio i centesimi'),(28,'2022-07-02',3.56,4,7,'debug'),(29,'2022-07-02',1032.87,4,7,'Ci sto rimettendo...');
/*!40000 ALTER TABLE `trasferimento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utente`
--

DROP TABLE IF EXISTS `utente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utente` (
  `Username` varchar(20) NOT NULL,
  `Nome` varchar(20) NOT NULL,
  `Cognome` varchar(20) NOT NULL,
  `Password` varchar(20) NOT NULL,
  `Mail` varchar(20) NOT NULL,
  PRIMARY KEY (`Username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utente`
--

LOCK TABLES `utente` WRITE;
/*!40000 ALTER TABLE `utente` DISABLE KEYS */;
INSERT INTO `utente` VALUES ('nick','Niccolò','Nicolosi','ammaccabanane','nick@gmail.com'),('nume','Emanuele','Musto','ciaone','emanuele@gmail.com'),('user1','Antonio','Conte','allenatore','contino@libero.it'),('user2','Maria','Filippi','conduttrice','unicorno@yahoo.it');
/*!40000 ALTER TABLE `utente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'tiw_project'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-07-02 16:42:55

-- phpMyAdmin SQL Dump
-- version 3.4.10.1deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 30. Mrz 2015 um 20:46
-- Server Version: 5.5.41
-- PHP-Version: 5.3.10-1ubuntu3.15

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT=0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `db228201x2224606`
--
CREATE DATABASE `db228201x2224606` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `db228201x2224606`;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `MS_BB_DI_spiele`
--

CREATE TABLE IF NOT EXISTS `MS_BB_DI_spiele` (
  `TimeID` int(11) NOT NULL,
  `Feld` int(11) NOT NULL DEFAULT '0',
  `Paarung` varchar(25) DEFAULT NULL,
  `Schiri` int(30) DEFAULT NULL,
  PRIMARY KEY (`TimeID`,`Feld`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `MS_BB_DI_zeiten`
--

CREATE TABLE IF NOT EXISTS `MS_BB_DI_zeiten` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Spielbeginn` time NOT NULL,
  `Spielende` time NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `MS_BB_MO_spiele`
--

CREATE TABLE IF NOT EXISTS `MS_BB_MO_spiele` (
  `TimeID` int(11) NOT NULL,
  `Feld` int(11) NOT NULL DEFAULT '0',
  `Paarung` varchar(25) DEFAULT NULL,
  `Schiri` int(30) DEFAULT NULL,
  PRIMARY KEY (`TimeID`,`Feld`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `MS_BB_MO_zeiten`
--

CREATE TABLE IF NOT EXISTS `MS_BB_MO_zeiten` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Spielbeginn` time NOT NULL,
  `Spielende` time NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `MS_BM_DI_spiele`
--

CREATE TABLE IF NOT EXISTS `MS_BM_DI_spiele` (
  `TimeID` int(11) NOT NULL,
  `Feld` int(11) NOT NULL DEFAULT '0',
  `Paarung` varchar(25) DEFAULT NULL,
  `Schiri` int(30) DEFAULT NULL,
  PRIMARY KEY (`TimeID`,`Feld`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `MS_BM_DI_zeiten`
--

CREATE TABLE IF NOT EXISTS `MS_BM_DI_zeiten` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Spielbeginn` time NOT NULL,
  `Spielende` time NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `MS_BM_MO_spiele`
--

CREATE TABLE IF NOT EXISTS `MS_BM_MO_spiele` (
  `TimeID` int(11) NOT NULL,
  `Feld` int(11) NOT NULL DEFAULT '0',
  `Paarung` varchar(25) DEFAULT NULL,
  `Schiri` int(30) DEFAULT NULL,
  PRIMARY KEY (`TimeID`,`Feld`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Tabellenstruktur für Tabelle `MS_BM_MO_zeiten`
--

CREATE TABLE IF NOT EXISTS `MS_BM_MO_zeiten` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Spielbeginn` time NOT NULL,
  `Spielende` time NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `Spielbeginn` (`Spielbeginn`),
  UNIQUE KEY `Spielende` (`Spielende`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=31 ;

--
-- Tabellenstruktur für Tabelle `MS_FB_DI_spiele`
--

CREATE TABLE IF NOT EXISTS `MS_FB_DI_spiele` (
  `TimeID` int(11) NOT NULL,
  `Feld` int(11) NOT NULL DEFAULT '0',
  `Paarung` varchar(25) DEFAULT NULL,
  `Schiri` int(30) DEFAULT NULL,
  PRIMARY KEY (`TimeID`,`Feld`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `MS_FB_DI_zeiten`
--

CREATE TABLE IF NOT EXISTS `MS_FB_DI_zeiten` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Spielbeginn` time NOT NULL,
  `Spielende` time NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `MS_FB_MO_spiele`
--

CREATE TABLE IF NOT EXISTS `MS_FB_MO_spiele` (
  `TimeID` int(11) NOT NULL,
  `Feld` int(11) NOT NULL DEFAULT '0',
  `Paarung` varchar(25) DEFAULT NULL,
  `Schiri` int(30) DEFAULT NULL,
  PRIMARY KEY (`TimeID`,`Feld`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Tabellenstruktur für Tabelle `MS_FB_MO_zeiten`
--

CREATE TABLE IF NOT EXISTS `MS_FB_MO_zeiten` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Spielbeginn` time NOT NULL,
  `Spielende` time NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=31 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `MS_ST_DI_spiele`
--

CREATE TABLE IF NOT EXISTS `MS_ST_DI_spiele` (
  `TimeID` int(11) NOT NULL,
  `Feld` int(11) NOT NULL DEFAULT '0',
  `Paarung` varchar(25) DEFAULT NULL,
  `Schiri` int(30) DEFAULT NULL,
  PRIMARY KEY (`TimeID`,`Feld`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `MS_ST_DI_zeiten`
--

CREATE TABLE IF NOT EXISTS `MS_ST_DI_zeiten` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Spielbeginn` time NOT NULL,
  `Spielende` time NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `MS_ST_MO_spiele`
--

CREATE TABLE IF NOT EXISTS `MS_ST_MO_spiele` (
  `TimeID` int(11) NOT NULL,
  `Feld` int(11) NOT NULL DEFAULT '0',
  `Paarung` varchar(25) DEFAULT NULL,
  `Schiri` int(30) DEFAULT NULL,
  PRIMARY KEY (`TimeID`,`Feld`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `MS_ST_MO_zeiten`
--

CREATE TABLE IF NOT EXISTS `MS_ST_MO_zeiten` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Spielbeginn` time NOT NULL,
  `Spielende` time NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `MS_TT_DI_spiele`
--

CREATE TABLE IF NOT EXISTS `MS_TT_DI_spiele` (
  `TimeID` int(11) NOT NULL,
  `Feld` int(11) NOT NULL DEFAULT '0',
  `Paarung` varchar(25) DEFAULT NULL,
  `Schiri` int(30) DEFAULT NULL,
  PRIMARY KEY (`TimeID`,`Feld`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `MS_TT_DI_zeiten`
--

CREATE TABLE IF NOT EXISTS `MS_TT_DI_zeiten` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Spielbeginn` time NOT NULL,
  `Spielende` time NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `MS_TT_MO_spiele`
--

CREATE TABLE IF NOT EXISTS `MS_TT_MO_spiele` (
  `TimeID` int(11) NOT NULL,
  `Feld` int(11) NOT NULL DEFAULT '0',
  `Paarung` varchar(25) DEFAULT NULL,
  `Schiri` int(30) DEFAULT NULL,
  PRIMARY KEY (`TimeID`,`Feld`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `MS_TT_MO_zeiten`
--

CREATE TABLE IF NOT EXISTS `MS_TT_MO_zeiten` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Spielbeginn` time NOT NULL,
  `Spielende` time NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `MS_VB_DI_spiele`
--

CREATE TABLE IF NOT EXISTS `MS_VB_DI_spiele` (
  `TimeID` int(11) NOT NULL,
  `Feld` int(11) NOT NULL DEFAULT '0',
  `Paarung` varchar(25) DEFAULT NULL,
  `Schiri` int(30) DEFAULT NULL,
  PRIMARY KEY (`TimeID`,`Feld`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `MS_VB_DI_zeiten`
--

CREATE TABLE IF NOT EXISTS `MS_VB_DI_zeiten` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Spielbeginn` time NOT NULL,
  `Spielende` time NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `MS_VB_MO_spiele`
--

CREATE TABLE IF NOT EXISTS `MS_VB_MO_spiele` (
  `TimeID` int(11) NOT NULL,
  `Feld` int(11) NOT NULL DEFAULT '0',
  `Paarung` varchar(25) DEFAULT NULL,
  `Schiri` int(30) DEFAULT NULL,
  PRIMARY KEY (`TimeID`,`Feld`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `MS_VB_MO_zeiten`
--

CREATE TABLE IF NOT EXISTS `MS_VB_MO_zeiten` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Spielbeginn` time NOT NULL,
  `Spielende` time NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Mannschaften_MS`
--

CREATE TABLE IF NOT EXISTS `Mannschaften_MS` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci NOT NULL,
  `Vorname` varchar(20) CHARACTER SET latin1 COLLATE latin1_german2_ci NOT NULL,
  `Klasse` varchar(5) CHARACTER SET latin1 COLLATE latin1_german2_ci NOT NULL,
  `BM` varchar(5) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT '',
  `FB` varchar(5) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT '',
  `BB` varchar(5) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT '',
  `VB` varchar(5) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT '',
  `TT` varchar(5) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT '',
  `ST` varchar(5) CHARACTER SET latin1 COLLATE latin1_german2_ci DEFAULT '',
  `FT` varchar(5) DEFAULT '',
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf16 AUTO_INCREMENT=235 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Mannschaften_OS`
--

CREATE TABLE IF NOT EXISTS `Mannschaften_OS` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(20) COLLATE latin1_german2_ci NOT NULL,
  `Vorname` varchar(20) COLLATE latin1_german2_ci NOT NULL,
  `Klasse` varchar(5) COLLATE latin1_german2_ci NOT NULL,
  `BM` varchar(5) COLLATE latin1_german2_ci DEFAULT '',
  `FB` varchar(5) COLLATE latin1_german2_ci DEFAULT '',
  `BB` varchar(5) COLLATE latin1_german2_ci DEFAULT '',
  `VB` varchar(5) COLLATE latin1_german2_ci DEFAULT '',
  `TT` varchar(5) COLLATE latin1_german2_ci DEFAULT '',
  `ST` varchar(5) COLLATE latin1_german2_ci DEFAULT '',
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=42 ;


-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Mannschaften_US`
--

CREATE TABLE IF NOT EXISTS `Mannschaften_US` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(20) COLLATE latin1_german2_ci NOT NULL,
  `Vorname` varchar(20) COLLATE latin1_german2_ci NOT NULL,
  `Klasse` varchar(5) COLLATE latin1_german2_ci NOT NULL,
  `BM` varchar(5) COLLATE latin1_german2_ci DEFAULT '',
  `FB` varchar(5) COLLATE latin1_german2_ci DEFAULT '',
  `BB` varchar(5) COLLATE latin1_german2_ci DEFAULT '',
  `TT` varchar(5) COLLATE latin1_german2_ci DEFAULT '',
  `ST` varchar(5) COLLATE latin1_german2_ci DEFAULT '',
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=32 ;


-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `OS_BB_DI_spiele`
--

CREATE TABLE IF NOT EXISTS `OS_BB_DI_spiele` (
  `TimeID` int(11) NOT NULL,
  `Feld` int(11) NOT NULL DEFAULT '0',
  `Paarung` varchar(25) DEFAULT NULL,
  `Schiri` int(30) DEFAULT NULL,
  PRIMARY KEY (`TimeID`,`Feld`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `OS_BB_DI_zeiten`
--

CREATE TABLE IF NOT EXISTS `OS_BB_DI_zeiten` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Spielbeginn` time NOT NULL,
  `Spielende` time NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `OS_BB_MO_spiele`
--

CREATE TABLE IF NOT EXISTS `OS_BB_MO_spiele` (
  `TimeID` int(11) NOT NULL,
  `Feld` int(11) NOT NULL DEFAULT '0',
  `Paarung` varchar(25) DEFAULT NULL,
  `Schiri` int(30) DEFAULT NULL,
  PRIMARY KEY (`TimeID`,`Feld`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `OS_BB_MO_zeiten`
--

CREATE TABLE IF NOT EXISTS `OS_BB_MO_zeiten` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Spielbeginn` time NOT NULL,
  `Spielende` time NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `OS_BM_DI_spiele`
--

CREATE TABLE IF NOT EXISTS `OS_BM_DI_spiele` (
  `TimeID` int(11) NOT NULL,
  `Feld` int(11) NOT NULL DEFAULT '0',
  `Paarung` varchar(25) DEFAULT NULL,
  `Schiri` int(30) DEFAULT NULL,
  PRIMARY KEY (`TimeID`,`Feld`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `OS_BM_DI_zeiten`
--

CREATE TABLE IF NOT EXISTS `OS_BM_DI_zeiten` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Spielbeginn` time NOT NULL,
  `Spielende` time NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `OS_BM_MO_spiele`
--

CREATE TABLE IF NOT EXISTS `OS_BM_MO_spiele` (
  `TimeID` int(11) NOT NULL,
  `Feld` int(11) NOT NULL DEFAULT '0',
  `Paarung` varchar(25) DEFAULT NULL,
  `Schiri` int(30) DEFAULT NULL,
  PRIMARY KEY (`TimeID`,`Feld`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `OS_BM_MO_zeiten`
--

CREATE TABLE IF NOT EXISTS `OS_BM_MO_zeiten` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Spielbeginn` time NOT NULL,
  `Spielende` time NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `OS_FB_DI_spiele`
--

CREATE TABLE IF NOT EXISTS `OS_FB_DI_spiele` (
  `TimeID` int(11) NOT NULL,
  `Feld` int(11) NOT NULL DEFAULT '0',
  `Paarung` varchar(25) DEFAULT NULL,
  `Schiri` int(30) DEFAULT NULL,
  PRIMARY KEY (`TimeID`,`Feld`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `OS_FB_DI_zeiten`
--

CREATE TABLE IF NOT EXISTS `OS_FB_DI_zeiten` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Spielbeginn` time NOT NULL,
  `Spielende` time NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `OS_FB_MO_spiele`
--

CREATE TABLE IF NOT EXISTS `OS_FB_MO_spiele` (
  `TimeID` int(11) NOT NULL,
  `Feld` int(11) NOT NULL DEFAULT '0',
  `Paarung` varchar(25) DEFAULT NULL,
  `Schiri` int(30) DEFAULT NULL,
  PRIMARY KEY (`TimeID`,`Feld`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `OS_FB_MO_zeiten`
--

CREATE TABLE IF NOT EXISTS `OS_FB_MO_zeiten` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Spielbeginn` time NOT NULL,
  `Spielende` time NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `OS_ST_DI_spiele`
--

CREATE TABLE IF NOT EXISTS `OS_ST_DI_spiele` (
  `TimeID` int(11) NOT NULL,
  `Feld` int(11) NOT NULL DEFAULT '0',
  `Paarung` varchar(25) DEFAULT NULL,
  `Schiri` int(30) DEFAULT NULL,
  PRIMARY KEY (`TimeID`,`Feld`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `OS_ST_MO_spiele`
--

CREATE TABLE IF NOT EXISTS `OS_ST_MO_spiele` (
  `TimeID` int(11) NOT NULL,
  `Feld` int(11) NOT NULL DEFAULT '0',
  `Paarung` varchar(25) DEFAULT NULL,
  `Schiri` int(30) DEFAULT NULL,
  PRIMARY KEY (`TimeID`,`Feld`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `OS_ST_MO_zeiten`
--

CREATE TABLE IF NOT EXISTS `OS_ST_MO_zeiten` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Spielbeginn` time NOT NULL,
  `Spielende` time NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `OS_TT_DI_spiele`
--

CREATE TABLE IF NOT EXISTS `OS_TT_DI_spiele` (
  `TimeID` int(11) NOT NULL,
  `Feld` int(11) NOT NULL DEFAULT '0',
  `Paarung` varchar(25) DEFAULT NULL,
  `Schiri` int(30) DEFAULT NULL,
  PRIMARY KEY (`TimeID`,`Feld`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `OS_TT_DI_zeiten`
--

CREATE TABLE IF NOT EXISTS `OS_TT_DI_zeiten` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Spielbeginn` time NOT NULL,
  `Spielende` time NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `OS_TT_MO_spiele`
--

CREATE TABLE IF NOT EXISTS `OS_TT_MO_spiele` (
  `TimeID` int(11) NOT NULL,
  `Feld` int(11) NOT NULL DEFAULT '0',
  `Paarung` varchar(25) DEFAULT NULL,
  `Schiri` int(30) DEFAULT NULL,
  PRIMARY KEY (`TimeID`,`Feld`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `OS_TT_MO_zeiten`
--

CREATE TABLE IF NOT EXISTS `OS_TT_MO_zeiten` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Spielbeginn` time NOT NULL,
  `Spielende` time NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `OS_VB_DI_spiele`
--

CREATE TABLE IF NOT EXISTS `OS_VB_DI_spiele` (
  `TimeID` int(11) NOT NULL,
  `Feld` int(11) NOT NULL DEFAULT '0',
  `Paarung` varchar(25) DEFAULT NULL,
  `Schiri` int(30) DEFAULT NULL,
  PRIMARY KEY (`TimeID`,`Feld`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `OS_VB_DI_zeiten`
--

CREATE TABLE IF NOT EXISTS `OS_VB_DI_zeiten` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Spielbeginn` time NOT NULL,
  `Spielende` time NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `OS_VB_MO_spiele`
--

CREATE TABLE IF NOT EXISTS `OS_VB_MO_spiele` (
  `TimeID` int(11) NOT NULL,
  `Feld` int(11) NOT NULL DEFAULT '0',
  `Paarung` varchar(25) DEFAULT NULL,
  `Schiri` int(30) DEFAULT NULL,
  PRIMARY KEY (`TimeID`,`Feld`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `OS_VB_MO_zeiten`
--

CREATE TABLE IF NOT EXISTS `OS_VB_MO_zeiten` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Spielbeginn` time NOT NULL,
  `Spielende` time NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `US_BB_DI_spiele`
--

CREATE TABLE IF NOT EXISTS `US_BB_DI_spiele` (
  `TimeID` int(11) NOT NULL,
  `Feld` int(11) NOT NULL DEFAULT '0',
  `Paarung` varchar(25) DEFAULT NULL,
  `Schiri` int(30) DEFAULT NULL,
  PRIMARY KEY (`TimeID`,`Feld`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `US_BB_DI_zeiten`
--

CREATE TABLE IF NOT EXISTS `US_BB_DI_zeiten` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Spielbeginn` time NOT NULL,
  `Spielende` time NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `US_BB_MO_spiele`
--

CREATE TABLE IF NOT EXISTS `US_BB_MO_spiele` (
  `TimeID` int(11) NOT NULL,
  `Feld` int(11) NOT NULL DEFAULT '0',
  `Paarung` varchar(25) DEFAULT NULL,
  `Schiri` int(30) DEFAULT NULL,
  PRIMARY KEY (`TimeID`,`Feld`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `US_BB_MO_zeiten`
--

CREATE TABLE IF NOT EXISTS `US_BB_MO_zeiten` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Spielbeginn` time NOT NULL,
  `Spielende` time NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `US_BM_DI_spiele`
--

CREATE TABLE IF NOT EXISTS `US_BM_DI_spiele` (
  `TimeID` int(11) NOT NULL,
  `Feld` int(11) NOT NULL DEFAULT '0',
  `Paarung` varchar(25) DEFAULT NULL,
  `Schiri` int(30) DEFAULT NULL,
  PRIMARY KEY (`TimeID`,`Feld`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `US_BM_DI_zeiten`
--

CREATE TABLE IF NOT EXISTS `US_BM_DI_zeiten` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Spielbeginn` time NOT NULL,
  `Spielende` time NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `US_BM_MO_spiele`
--

CREATE TABLE IF NOT EXISTS `US_BM_MO_spiele` (
  `TimeID` int(11) NOT NULL,
  `Feld` int(11) NOT NULL DEFAULT '0',
  `Paarung` varchar(25) DEFAULT NULL,
  `Schiri` int(30) DEFAULT NULL,
  PRIMARY KEY (`TimeID`,`Feld`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `US_BM_MO_zeiten`
--

CREATE TABLE IF NOT EXISTS `US_BM_MO_zeiten` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Spielbeginn` time NOT NULL,
  `Spielende` time NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `US_FB_DI_spiele`
--

CREATE TABLE IF NOT EXISTS `US_FB_DI_spiele` (
  `TimeID` int(11) NOT NULL,
  `Feld` int(11) NOT NULL DEFAULT '0',
  `Paarung` varchar(25) DEFAULT NULL,
  `Schiri` int(30) DEFAULT NULL,
  PRIMARY KEY (`TimeID`,`Feld`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `US_FB_DI_zeiten`
--

CREATE TABLE IF NOT EXISTS `US_FB_DI_zeiten` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Spielbeginn` time NOT NULL,
  `Spielende` time NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `US_FB_MO_spiele`
--

CREATE TABLE IF NOT EXISTS `US_FB_MO_spiele` (
  `TimeID` int(11) NOT NULL,
  `Feld` int(11) NOT NULL DEFAULT '0',
  `Paarung` varchar(25) DEFAULT NULL,
  `Schiri` int(30) DEFAULT NULL,
  PRIMARY KEY (`TimeID`,`Feld`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `US_FB_MO_zeiten`
--

CREATE TABLE IF NOT EXISTS `US_FB_MO_zeiten` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Spielbeginn` time NOT NULL,
  `Spielende` time NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `US_ST_DI_spiele`
--

CREATE TABLE IF NOT EXISTS `US_ST_DI_spiele` (
  `TimeID` int(11) NOT NULL,
  `Feld` int(11) NOT NULL DEFAULT '0',
  `Paarung` varchar(25) DEFAULT NULL,
  `Schiri` int(30) DEFAULT NULL,
  PRIMARY KEY (`TimeID`,`Feld`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `US_ST_DI_zeiten`
--

CREATE TABLE IF NOT EXISTS `US_ST_DI_zeiten` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Spielbeginn` time NOT NULL,
  `Spielende` time NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `US_ST_MO_spiele`
--

CREATE TABLE IF NOT EXISTS `US_ST_MO_spiele` (
  `TimeID` int(11) NOT NULL,
  `Feld` int(11) NOT NULL DEFAULT '0',
  `Paarung` varchar(25) DEFAULT NULL,
  `Schiri` int(30) DEFAULT NULL,
  PRIMARY KEY (`TimeID`,`Feld`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `US_ST_MO_zeiten`
--

CREATE TABLE IF NOT EXISTS `US_ST_MO_zeiten` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Spielbeginn` time NOT NULL,
  `Spielende` time NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `US_TT_DI_spiele`
--

CREATE TABLE IF NOT EXISTS `US_TT_DI_spiele` (
  `TimeID` int(11) NOT NULL,
  `Feld` int(11) NOT NULL DEFAULT '0',
  `Paarung` varchar(25) DEFAULT NULL,
  `Schiri` int(30) DEFAULT NULL,
  PRIMARY KEY (`TimeID`,`Feld`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `US_TT_DI_zeiten`
--

CREATE TABLE IF NOT EXISTS `US_TT_DI_zeiten` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Spielbeginn` time NOT NULL,
  `Spielende` time NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `US_TT_MO_spiele`
--

CREATE TABLE IF NOT EXISTS `US_TT_MO_spiele` (
  `TimeID` int(11) NOT NULL,
  `Feld` int(11) NOT NULL DEFAULT '0',
  `Paarung` varchar(25) DEFAULT NULL,
  `Schiri` int(30) DEFAULT NULL,
  PRIMARY KEY (`TimeID`,`Feld`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `US_TT_MO_zeiten`
--

CREATE TABLE IF NOT EXISTS `US_TT_MO_zeiten` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Spielbeginn` time NOT NULL,
  `Spielende` time NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci AUTO_INCREMENT=1 ;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

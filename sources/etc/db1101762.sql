-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.5.25a - MySQL Community Server (GPL)
-- Server OS:                    Win64
-- HeidiSQL version:             7.0.0.4053
-- Date/time:                    2013-04-27 19:34:30
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET FOREIGN_KEY_CHECKS=0 */;

-- Dumping database structure for db1101762
CREATE DATABASE IF NOT EXISTS `db1101762` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `db1101762`;


-- Dumping structure for table db1101762.association
CREATE TABLE IF NOT EXISTS `association` (
  `associ_no` varchar(8) NOT NULL DEFAULT '',
  `associ_name` varchar(40) NOT NULL,
  `associ_kana` varchar(80) NOT NULL,
  PRIMARY KEY (`associ_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table db1101762.association: ~7 rows (approximately)
/*!40000 ALTER TABLE `association` DISABLE KEYS */;
INSERT INTO `association` (`associ_no`, `associ_name`, `associ_kana`) VALUES
	('00000001', '経済産業省', 'ケイザイサンギョウショウ'),
	('00000002', 'サーティファイ', 'サーティファイ'),
	('00000003', '専修学校教育振興会', 'センシュウガッコウキョウイクシンコウカイ'),
	('00000004', 'マイクロソフト', 'マイクロソフト'),
	('00000005', 'シスコシステムズ', 'シスコシステムズ'),
	('00000006', 'オラクル主催', 'オラクルシュサイ'),
	('00000007', 'Oracle認定', 'オラクルニンテイ');
/*!40000 ALTER TABLE `association` ENABLE KEYS */;


-- Dumping structure for table db1101762.class
CREATE TABLE IF NOT EXISTS `class` (
  `class_no` varchar(8) NOT NULL DEFAULT '',
  `class_name` varchar(40) NOT NULL,
  PRIMARY KEY (`class_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table db1101762.class: ~5 rows (approximately)
/*!40000 ALTER TABLE `class` DISABLE KEYS */;
INSERT INTO `class` (`class_no`, `class_name`) VALUES
	('00000001', '情報システム専攻科'),
	('00000002', '情報システム科'),
	('00000003', '情報工学科'),
	('00000004', 'コンピュータネットワーク科'),
	('00000005', '組み込みシステム科');
/*!40000 ALTER TABLE `class` ENABLE KEYS */;


-- Dumping structure for table db1101762.gettest
CREATE TABLE IF NOT EXISTS `gettest` (
  `st_no` varchar(8) NOT NULL DEFAULT '',
  `test_no` varchar(8) NOT NULL DEFAULT '',
  `test_perform_date` date NOT NULL DEFAULT '0000-00-00',
  PRIMARY KEY (`st_no`,`test_no`),
  KEY `test_no` (`test_no`,`test_perform_date`),
  CONSTRAINT `gettest_ibfk_1` FOREIGN KEY (`st_no`) REFERENCES `student` (`st_no`),
  CONSTRAINT `gettest_ibfk_2` FOREIGN KEY (`test_no`, `test_perform_date`) REFERENCES `testdate` (`test_no`, `test_perform_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table db1101762.gettest: ~3 rows (approximately)
/*!40000 ALTER TABLE `gettest` DISABLE KEYS */;
/*!40000 ALTER TABLE `gettest` ENABLE KEYS */;


-- Dumping structure for table db1101762.manager
CREATE TABLE IF NOT EXISTS `manager` (
  `mg_no` varchar(8) NOT NULL DEFAULT '',
  `mg_name` varchar(20) NOT NULL,
  `mg_kana` varchar(40) NOT NULL,
  `mg_pass` varchar(41) NOT NULL,
  PRIMARY KEY (`mg_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table db1101762.manager: ~1 rows (approximately)
/*!40000 ALTER TABLE `manager` DISABLE KEYS */;
INSERT INTO `manager` (`mg_no`, `mg_name`, `mg_kana`, `mg_pass`) VALUES
	('manager', 'テスト管理者', 'カンリシャ', '*1E6BD308243660263AB153D91B8DCDEBBB892B13');
/*!40000 ALTER TABLE `manager` ENABLE KEYS */;


-- Dumping structure for table db1101762.student
CREATE TABLE IF NOT EXISTS `student` (
  `st_no` varchar(8) NOT NULL DEFAULT '',
  `st_name` varchar(20) NOT NULL,
  `st_kana` varchar(40) NOT NULL,
  `class_no` varchar(8) NOT NULL,
  `year` char(1) NOT NULL,
  `st_pass` varchar(41) NOT NULL,
  `birthday` date NOT NULL DEFAULT '1900-01-01',
  PRIMARY KEY (`st_no`),
  KEY `class_no` (`class_no`),
  CONSTRAINT `student_ibfk_1` FOREIGN KEY (`class_no`) REFERENCES `class` (`class_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table db1101762.student: ~40 rows (approximately)
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` (`st_no`, `st_name`, `st_kana`, `class_no`, `year`, `st_pass`, `birthday`) VALUES
	('00000001', 'テスト学生1', 'テストガクセイ', '00000002', '2', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000002', 'テスト学生2', 'テストガクセイ', '00000001', '1', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000003', 'テスト学生3', 'テストガクセイ', '00000001', '1', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000004', 'テスト学生4', 'テストガクセイ', '00000002', '1', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000005', 'テスト学生5', 'テストガクセイ', '00000002', '1', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000006', 'テスト学生6', 'テストガクセイ', '00000002', '1', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000007', 'テスト学生7', 'テストガクセイ', '00000003', '1', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000008', 'テスト学生8', 'テストガクセイ', '00000003', '1', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000009', 'テスト学生9', 'テストガクセイ', '00000003', '1', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000010', 'テスト学生10', 'テストガクセイ', '00000004', '1', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000011', 'テスト学生11', 'テストガクセイ', '00000004', '1', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000012', 'テスト学生12', 'テストガクセイ', '00000004', '1', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000013', 'テスト学生13', 'テストガクセイ', '00000005', '1', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000014', 'テスト学生14', 'テストガクセイ', '00000005', '1', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000015', 'テスト学生15', 'テストガクセイ', '00000005', '1', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000016', 'テスト学生16', 'テストガクセイ', '00000001', '2', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000017', 'テスト学生17', 'テストガクセイ', '00000001', '2', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000018', 'テスト学生18', 'テストガクセイ', '00000001', '2', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000019', 'テスト学生19', 'テストガクセイ', '00000002', '2', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000020', 'テスト学生20', 'テストガクセイ', '00000002', '2', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000021', 'テスト学生21', 'テストガクセイ', '00000002', '2', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000022', 'テスト学生22', 'テストガクセイ', '00000003', '2', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000023', 'テスト学生23', 'テストガクセイ', '00000003', '2', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000024', 'テスト学生24', 'テストガクセイ', '00000003', '2', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000025', 'テスト学生25', 'テストガクセイ', '00000004', '2', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000026', 'テスト学生26', 'テストガクセイ', '00000004', '2', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000027', 'テスト学生27', 'テストガクセイ', '00000004', '2', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000028', 'テスト学生28', 'テストガクセイ', '00000005', '2', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000029', 'テスト学生29', 'テストガクセイ', '00000005', '2', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000030', 'テスト学生30', 'テストガクセイ', '00000005', '2', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000031', 'テスト学生31', 'テストガクセイ', '00000001', '3', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000032', 'テスト学生32', 'テストガクセイ', '00000001', '3', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000033', 'テスト学生33', 'テストガクセイ', '00000001', '3', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000034', 'テスト学生34', 'テストガクセイ', '00000002', '3', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000035', 'テスト学生35', 'テストガクセイ', '00000002', '3', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000036', 'テスト学生36', 'テストガクセイ', '00000002', '3', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000037', 'テスト学生37', 'テストガクセイ', '00000003', '3', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000038', 'テスト学生38', 'テストガクセイ', '00000003', '3', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000039', 'テスト学生39', 'テストガクセイ', '00000003', '3', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01'),
	('00000040', 'テスト学生40', 'テストガクセイ', '00000004', '3', '*B097B909950AEAA03AE136B6532D25803514C2C8', '1990-01-01');
/*!40000 ALTER TABLE `student` ENABLE KEYS */;


-- Dumping structure for table db1101762.test
CREATE TABLE IF NOT EXISTS `test` (
  `test_no` varchar(8) NOT NULL DEFAULT '',
  `test_name` varchar(50) NOT NULL,
  `associ_no` varchar(8) NOT NULL,
  PRIMARY KEY (`test_no`),
  KEY `associ_no` (`associ_no`),
  CONSTRAINT `test_ibfk_1` FOREIGN KEY (`associ_no`) REFERENCES `association` (`associ_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table db1101762.test: ~19 rows (approximately)
/*!40000 ALTER TABLE `test` DISABLE KEYS */;
INSERT INTO `test` (`test_no`, `test_name`, `associ_no`) VALUES
	('00000001', 'ITパスポート試験', '00000001'),
	('00000002', '基本情報技術者試験', '00000001'),
	('00000003', '応用情報技術者試験', '00000001'),
	('00000004', 'ネットワークスペシャリスト試験', '00000001'),
	('00000005', 'データベーススペシャリスト試験', '00000001'),
	('00000006', 'エンベデッドシステムスペシャリスト試験', '00000001'),
	('00000007', '情報セキュリティスペシャリスト', '00000001'),
	('00000008', '情報処理技術者能力認定試験 1級', '00000002'),
	('00000009', '情報処理技術者能力認定試験 2級', '00000002'),
	('00000010', '情報処理技術者能力認定試験 3級', '00000002'),
	('00000011', '情報検定 1級', '00000003'),
	('00000012', '情報検定 2級', '00000003'),
	('00000013', '情報検定 3級', '00000003'),
	('00000014', 'CCENT', '00000005'),
	('00000015', 'CCNA', '00000005'),
	('00000016', 'CCNP', '00000005'),
	('00000017', 'Oracle Database 11g SQL 基礎I', '00000006'),
	('00000018', 'Oracle Database Bronze DBA 11g', '00000006'),
	('00000019', 'Oracle Master Bronze Oracle Database', '00000007');
/*!40000 ALTER TABLE `test` ENABLE KEYS */;


-- Dumping structure for table db1101762.testdate
CREATE TABLE IF NOT EXISTS `testdate` (
  `test_no` varchar(8) NOT NULL DEFAULT '',
  `test_perform_date` date NOT NULL DEFAULT '0000-00-00',
  `test_get_date` date NOT NULL,
  PRIMARY KEY (`test_no`,`test_perform_date`),
  CONSTRAINT `testdate_ibfk_1` FOREIGN KEY (`test_no`) REFERENCES `test` (`test_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table db1101762.testdate: ~23 rows (approximately)
/*!40000 ALTER TABLE `testdate` DISABLE KEYS */;
/*!40000 ALTER TABLE `testdate` ENABLE KEYS */;


-- Dumping database structure for demodb
CREATE DATABASE IF NOT EXISTS `demodb` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `demodb`;


-- Dumping structure for table demodb.association
CREATE TABLE IF NOT EXISTS `association` (
  `associ_no` varchar(8) NOT NULL DEFAULT '',
  `associ_name` varchar(40) NOT NULL,
  `associ_kana` varchar(80) NOT NULL,
  PRIMARY KEY (`associ_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table demodb.association: ~7 rows (approximately)
/*!40000 ALTER TABLE `association` DISABLE KEYS */;
INSERT INTO `association` (`associ_no`, `associ_name`, `associ_kana`) VALUES
	('00000001', '経済産業省', 'ケイザイサンギョウショウ'),
	('00000002', 'サーティファイ', 'サーティファイ'),
	('00000003', '専修学校教育振興会', 'センシュウガッコウキョウイクシンコウカイ'),
	('00000004', 'マイクロソフト', 'マイクロソフト'),
	('00000005', 'シスコシステムズ', 'シスコシステムズ'),
	('00000006', 'オラクル主催', 'オラクルシュサイ'),
	('00000007', 'Oracle認定', 'オラクルニンテイ');
/*!40000 ALTER TABLE `association` ENABLE KEYS */;


-- Dumping structure for table demodb.class
CREATE TABLE IF NOT EXISTS `class` (
  `class_no` varchar(8) NOT NULL DEFAULT '',
  `class_name` varchar(40) NOT NULL,
  PRIMARY KEY (`class_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table demodb.class: ~5 rows (approximately)
/*!40000 ALTER TABLE `class` DISABLE KEYS */;
INSERT INTO `class` (`class_no`, `class_name`) VALUES
	('00000001', '情報システム専攻科'),
	('00000002', '情報システム科'),
	('00000003', '情報工学科'),
	('00000004', 'コンピュータネットワーク科'),
	('00000005', '組み込みシステム科');
/*!40000 ALTER TABLE `class` ENABLE KEYS */;


-- Dumping structure for table demodb.gettest
CREATE TABLE IF NOT EXISTS `gettest` (
  `st_no` varchar(8) NOT NULL DEFAULT '',
  `test_no` varchar(8) NOT NULL DEFAULT '',
  `test_perform_date` date NOT NULL DEFAULT '0000-00-00',
  PRIMARY KEY (`st_no`,`test_no`),
  KEY `test_no` (`test_no`,`test_perform_date`),
  CONSTRAINT `gettest_ibfk_1` FOREIGN KEY (`st_no`) REFERENCES `student` (`st_no`),
  CONSTRAINT `gettest_ibfk_2` FOREIGN KEY (`test_no`, `test_perform_date`) REFERENCES `testdate` (`test_no`, `test_perform_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table demodb.gettest: ~2 rows (approximately)
/*!40000 ALTER TABLE `gettest` DISABLE KEYS */;
/*!40000 ALTER TABLE `gettest` ENABLE KEYS */;


-- Dumping structure for table demodb.manager
CREATE TABLE IF NOT EXISTS `manager` (
  `mg_no` varchar(8) NOT NULL DEFAULT '',
  `mg_name` varchar(20) NOT NULL,
  `mg_kana` varchar(40) NOT NULL,
  `mg_pass` varchar(41) NOT NULL,
  PRIMARY KEY (`mg_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table demodb.manager: ~1 rows (approximately)
/*!40000 ALTER TABLE `manager` DISABLE KEYS */;
INSERT INTO `manager` (`mg_no`, `mg_name`, `mg_kana`, `mg_pass`) VALUES
	('manager', 'テスト管理者', 'カンリシャ', '*7D2ABFF56C15D67445082FBB4ACD2DCD26C0ED57');
/*!40000 ALTER TABLE `manager` ENABLE KEYS */;


-- Dumping structure for table demodb.student
CREATE TABLE IF NOT EXISTS `student` (
  `st_no` varchar(8) NOT NULL DEFAULT '',
  `st_name` varchar(20) NOT NULL,
  `st_kana` varchar(40) NOT NULL,
  `class_no` varchar(8) NOT NULL,
  `year` char(1) NOT NULL,
  `st_pass` varchar(41) NOT NULL,
  `birthday` date NOT NULL DEFAULT '1900-01-01',
  PRIMARY KEY (`st_no`),
  KEY `class_no` (`class_no`),
  CONSTRAINT `student_ibfk_1` FOREIGN KEY (`class_no`) REFERENCES `class` (`class_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table demodb.student: ~40 rows (approximately)
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` (`st_no`, `st_name`, `st_kana`, `class_no`, `year`, `st_pass`, `birthday`) VALUES
	('00000001', 'テスト学生1', 'テストガクセイ', '00000002', '2', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000002', 'テスト学生2', 'テストガクセイ', '00000001', '1', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000003', 'テスト学生3', 'テストガクセイ', '00000001', '1', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000004', 'テスト学生4', 'テストガクセイ', '00000002', '1', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000005', 'テスト学生5', 'テストガクセイ', '00000002', '1', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000006', 'テスト学生6', 'テストガクセイ', '00000002', '1', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000007', 'テスト学生7', 'テストガクセイ', '00000003', '1', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000008', 'テスト学生8', 'テストガクセイ', '00000003', '1', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000009', 'テスト学生9', 'テストガクセイ', '00000003', '1', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000010', 'テスト学生10', 'テストガクセイ', '00000004', '1', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000011', 'テスト学生11', 'テストガクセイ', '00000004', '1', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000012', 'テスト学生12', 'テストガクセイ', '00000004', '1', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000013', 'テスト学生13', 'テストガクセイ', '00000005', '1', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000014', 'テスト学生14', 'テストガクセイ', '00000005', '1', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000015', 'テスト学生15', 'テストガクセイ', '00000005', '1', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000016', 'テスト学生16', 'テストガクセイ', '00000001', '2', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000017', 'テスト学生17', 'テストガクセイ', '00000001', '2', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000018', 'テスト学生18', 'テストガクセイ', '00000001', '2', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000019', 'テスト学生19', 'テストガクセイ', '00000002', '2', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000020', 'テスト学生20', 'テストガクセイ', '00000002', '2', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000021', 'テスト学生21', 'テストガクセイ', '00000002', '2', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000022', 'テスト学生22', 'テストガクセイ', '00000003', '2', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000023', 'テスト学生23', 'テストガクセイ', '00000003', '2', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000024', 'テスト学生24', 'テストガクセイ', '00000003', '2', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000025', 'テスト学生25', 'テストガクセイ', '00000004', '2', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000026', 'テスト学生26', 'テストガクセイ', '00000004', '2', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000027', 'テスト学生27', 'テストガクセイ', '00000004', '2', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000028', 'テスト学生28', 'テストガクセイ', '00000005', '2', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000029', 'テスト学生29', 'テストガクセイ', '00000005', '2', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000030', 'テスト学生30', 'テストガクセイ', '00000005', '2', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000031', 'テスト学生31', 'テストガクセイ', '00000001', '3', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000032', 'テスト学生32', 'テストガクセイ', '00000001', '3', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000033', 'テスト学生33', 'テストガクセイ', '00000001', '3', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000034', 'テスト学生34', 'テストガクセイ', '00000002', '3', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000035', 'テスト学生35', 'テストガクセイ', '00000002', '3', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000036', 'テスト学生36', 'テストガクセイ', '00000002', '3', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000037', 'テスト学生37', 'テストガクセイ', '00000003', '3', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000038', 'テスト学生38', 'テストガクセイ', '00000003', '3', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000039', 'テスト学生39', 'テストガクセイ', '00000003', '3', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01'),
	('00000040', 'テスト学生40', 'テストガクセイ', '00000004', '3', '*1308E0FCD43112F8D948AB093F54892CB7B220AA', '1990-01-01');
/*!40000 ALTER TABLE `student` ENABLE KEYS */;


-- Dumping structure for table demodb.test
CREATE TABLE IF NOT EXISTS `test` (
  `test_no` varchar(8) NOT NULL DEFAULT '',
  `test_name` varchar(50) NOT NULL,
  `associ_no` varchar(8) NOT NULL,
  PRIMARY KEY (`test_no`),
  KEY `associ_no` (`associ_no`),
  CONSTRAINT `test_ibfk_1` FOREIGN KEY (`associ_no`) REFERENCES `association` (`associ_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table demodb.test: ~19 rows (approximately)
/*!40000 ALTER TABLE `test` DISABLE KEYS */;
INSERT INTO `test` (`test_no`, `test_name`, `associ_no`) VALUES
	('00000001', 'ITパスポート試験', '00000001'),
	('00000002', '基本情報技術者試験', '00000001'),
	('00000003', '応用情報技術者試験', '00000001'),
	('00000004', 'ネットワークスペシャリスト試験', '00000001'),
	('00000005', 'データベーススペシャリスト試験', '00000001'),
	('00000006', 'エンベデッドシステムスペシャリスト試験', '00000001'),
	('00000007', '情報セキュリティスペシャリスト', '00000001'),
	('00000008', '情報処理技術者能力認定試験 1級', '00000002'),
	('00000009', '情報処理技術者能力認定試験 2級', '00000002'),
	('00000010', '情報処理技術者能力認定試験 3級', '00000002'),
	('00000011', '情報検定 1級', '00000003'),
	('00000012', '情報検定 2級', '00000003'),
	('00000013', '情報検定 3級', '00000003'),
	('00000014', 'CCENT', '00000005'),
	('00000015', 'CCNA', '00000005'),
	('00000016', 'CCNP', '00000005'),
	('00000017', 'Oracle Database 11g SQL 基礎I', '00000006'),
	('00000018', 'Oracle Database Bronze DBA 11g', '00000006'),
	('00000019', 'Oracle Master Bronze Oracle Database', '00000007');
/*!40000 ALTER TABLE `test` ENABLE KEYS */;


-- Dumping structure for table demodb.testdate
CREATE TABLE IF NOT EXISTS `testdate` (
  `test_no` varchar(8) NOT NULL DEFAULT '',
  `test_perform_date` date NOT NULL DEFAULT '0000-00-00',
  `test_get_date` date NOT NULL,
  PRIMARY KEY (`test_no`,`test_perform_date`),
  CONSTRAINT `testdate_ibfk_1` FOREIGN KEY (`test_no`) REFERENCES `test` (`test_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table demodb.testdate: ~23 rows (approximately)
/*!40000 ALTER TABLE `testdate` DISABLE KEYS */;
/*!40000 ALTER TABLE `testdate` ENABLE KEYS */;
/*!40014 SET FOREIGN_KEY_CHECKS=1 */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;

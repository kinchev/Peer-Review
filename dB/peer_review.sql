-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.6.4-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for peer_review
CREATE DATABASE IF NOT EXISTS `peer_review` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `peer_review`;

-- Dumping structure for table peer_review.attachments
CREATE TABLE IF NOT EXISTS `attachments` (
  `attachment_id` int(11) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(100) DEFAULT NULL,
  `workitem_id` int(11) NOT NULL,
  PRIMARY KEY (`attachment_id`),
  KEY `attachments_workitems_fk` (`workitem_id`),
  CONSTRAINT `attachments_workitems_fk` FOREIGN KEY (`workitem_id`) REFERENCES `workitems` (`workitem_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- Dumping data for table peer_review.attachments: ~3 rows (approximately)
DELETE FROM `attachments`;
/*!40000 ALTER TABLE `attachments` DISABLE KEYS */;
INSERT INTO `attachments` (`attachment_id`, `file_name`, `workitem_id`) VALUES
	(1, 'test.pdf', 10),
	(2, 'test.jpg', 10),
	(3, 'AD PMP Certificate 2024.pdf', 19);
/*!40000 ALTER TABLE `attachments` ENABLE KEYS */;

-- Dumping structure for table peer_review.comments
CREATE TABLE IF NOT EXISTS `comments` (
  `comment_id` int(11) NOT NULL AUTO_INCREMENT,
  `author_id` int(11) NOT NULL,
  `comment_text` varchar(256) DEFAULT NULL,
  `workitem_id` int(11) NOT NULL,
  PRIMARY KEY (`comment_id`),
  KEY `comments_reviewer_fk` (`author_id`),
  KEY `comments_workitem_fk` (`workitem_id`),
  CONSTRAINT `comments_reviewer_fk` FOREIGN KEY (`author_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `comments_workitem_fk` FOREIGN KEY (`workitem_id`) REFERENCES `workitems` (`workitem_id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=latin1;

-- Dumping data for table peer_review.comments: ~3 rows (approximately)
DELETE FROM `comments`;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
INSERT INTO `comments` (`comment_id`, `author_id`, `comment_text`, `workitem_id`) VALUES
	(35, 11, 'The description is not serious', 10),
	(36, 11, 'Not perfect but well enough !', 10),
	(37, 10, 'Thank you !', 10);
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;

-- Dumping structure for table peer_review.review_requests
CREATE TABLE IF NOT EXISTS `review_requests` (
  `review_request_id` int(11) NOT NULL AUTO_INCREMENT,
  `workitem_id` int(11) NOT NULL,
  `creator_id` int(11) NOT NULL,
  `reviewer_id` int(11) NOT NULL,
  PRIMARY KEY (`review_request_id`),
  KEY `review_requests_fk` (`workitem_id`),
  KEY `review_requests_creator_fk` (`creator_id`),
  KEY `review_requests_reviewer_fk` (`reviewer_id`),
  CONSTRAINT `review_requests_creator_fk` FOREIGN KEY (`creator_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `review_requests_fk` FOREIGN KEY (`workitem_id`) REFERENCES `workitems` (`workitem_id`),
  CONSTRAINT `review_requests_reviewer_fk` FOREIGN KEY (`reviewer_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

-- Dumping data for table peer_review.review_requests: ~3 rows (approximately)
DELETE FROM `review_requests`;
/*!40000 ALTER TABLE `review_requests` DISABLE KEYS */;
INSERT INTO `review_requests` (`review_request_id`, `workitem_id`, `creator_id`, `reviewer_id`) VALUES
	(2, 1, 6, 7),
	(3, 19, 10, 11),
	(4, 20, 10, 11);
/*!40000 ALTER TABLE `review_requests` ENABLE KEYS */;

-- Dumping structure for table peer_review.statuses
CREATE TABLE IF NOT EXISTS `statuses` (
  `status_id` int(11) NOT NULL AUTO_INCREMENT,
  `status` varchar(50) NOT NULL,
  PRIMARY KEY (`status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

-- Dumping data for table peer_review.statuses: ~5 rows (approximately)
DELETE FROM `statuses`;
/*!40000 ALTER TABLE `statuses` DISABLE KEYS */;
INSERT INTO `statuses` (`status_id`, `status`) VALUES
	(1, 'Pending'),
	(2, 'Under Review '),
	(3, 'Change Requested'),
	(4, 'Accepted'),
	(5, 'Rejected');
/*!40000 ALTER TABLE `statuses` ENABLE KEYS */;

-- Dumping structure for table peer_review.teams
CREATE TABLE IF NOT EXISTS `teams` (
  `team_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `owner_id` int(11) NOT NULL,
  PRIMARY KEY (`team_id`),
  KEY `teams_users_fk` (`owner_id`),
  CONSTRAINT `teams_users_fk` FOREIGN KEY (`owner_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

-- Dumping data for table peer_review.teams: ~6 rows (approximately)
DELETE FROM `teams`;
/*!40000 ALTER TABLE `teams` DISABLE KEYS */;
INSERT INTO `teams` (`team_id`, `name`, `owner_id`) VALUES
	(1, 'Team1', 6),
	(2, 'Team2', 6),
	(3, 'Awesome Team3', 8),
	(4, 'Awesome Team4', 9),
	(5, 'Team5', 10),
	(6, 'New Team with owner-member', 11);
/*!40000 ALTER TABLE `teams` ENABLE KEYS */;

-- Dumping structure for table peer_review.teams_users
CREATE TABLE IF NOT EXISTS `teams_users` (
  `team_id` int(11) NOT NULL,
  `member_id` int(11) NOT NULL,
  KEY `teams_users_teams_fk` (`team_id`),
  KEY `teams_users_users_fk` (`member_id`),
  CONSTRAINT `teams_users_teams_fk` FOREIGN KEY (`team_id`) REFERENCES `teams` (`team_id`),
  CONSTRAINT `teams_users_users_fk` FOREIGN KEY (`member_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table peer_review.teams_users: ~8 rows (approximately)
DELETE FROM `teams_users`;
/*!40000 ALTER TABLE `teams_users` DISABLE KEYS */;
INSERT INTO `teams_users` (`team_id`, `member_id`) VALUES
	(1, 7),
	(1, 8),
	(1, 9),
	(2, 10),
	(2, 11),
	(3, 11),
	(6, 11),
	(6, 10);
/*!40000 ALTER TABLE `teams_users` ENABLE KEYS */;

-- Dumping structure for table peer_review.teams_workitems
CREATE TABLE IF NOT EXISTS `teams_workitems` (
  `team_id` int(11) NOT NULL,
  `workitem_id` int(11) NOT NULL,
  KEY `teams_workitems_teams_fk` (`team_id`),
  KEY `teams_workitems_workitems_fk` (`workitem_id`),
  CONSTRAINT `teams_workitems_teams_fk` FOREIGN KEY (`team_id`) REFERENCES `teams` (`team_id`),
  CONSTRAINT `teams_workitems_workitems_fk` FOREIGN KEY (`workitem_id`) REFERENCES `workitems` (`workitem_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Dumping data for table peer_review.teams_workitems: ~7 rows (approximately)
DELETE FROM `teams_workitems`;
/*!40000 ALTER TABLE `teams_workitems` DISABLE KEYS */;
INSERT INTO `teams_workitems` (`team_id`, `workitem_id`) VALUES
	(1, 1),
	(2, 15),
	(2, 16),
	(1, 17),
	(1, 18),
	(2, 19),
	(2, 20);
/*!40000 ALTER TABLE `teams_workitems` ENABLE KEYS */;

-- Dumping structure for table peer_review.users
CREATE TABLE IF NOT EXISTS `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `password` varchar(1000) NOT NULL,
  `email` varchar(50) NOT NULL,
  `phone_number` varchar(10) NOT NULL,
  `photo_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  KEY `users_images_fk` (`photo_name`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;

-- Dumping data for table peer_review.users: ~19 rows (approximately)
DELETE FROM `users`;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`user_id`, `username`, `password`, `email`, `phone_number`, `photo_name`) VALUES
	(1, 'Ianko', 'qwerty', 'hev@yahoo.com', '0879654565', 'celebrities-before-famous-1588175098.jpg'),
	(2, 'Nikol', '!27dsdjasaa', 'hev232@yahoo.com', '0875456525', 'celebrities-before-famous-1588175098 (1).jpg'),
	(3, 'Eva', 'azsxdcfvb', 'evaaa@yahoo.com', '0854789653', 'jennifer-aniston-1588167141.jpg'),
	(4, 'Elica', '321312313', 'el@yahoo.com', '0876546523', 'jenniferlopez-1588167242.jpg'),
	(5, 'Adelina', 'ivanmivandigivan', 'adi@yahoo.com', '0874854653', 'ariana-grande-1588167315.jpg'),
	(6, 'Gosho', 'e3234248', 'hev@yahoo.com', '0889654525', 'blake-lively-1588167416.jpg'),
	(7, 'BBB', '3255245252', 'kinchev@yahoo.com', '0896000035', 'gettyimages-51506153-1588176289.jpg'),
	(8, 'Monica', '$2a$10$1qBAEH2AdUcVWuVYJ/Q8WutKn0gQYXKWEQJtrK5/8O7gqDTET29vS', 'moni@abv.bg', '0898384663', 'gigi-hadid-1588167487.jpg'),
	(9, 'Katrin', '$2a$10$8o4pdO6a7./a7cUNUL.evONekds/KShk28dpLnfI41TtzgqCqAD46', 'kati2@abv.bg', '0889223600', 'meghan-markle-before-royal-1588168531.jpg'),
	(10, 'Ivan', '$2a$10$4wSEZ5QWk1.LIEbPmT/AT.kXW5ampq7b2FEXy8vcqDuvLFmj2BM5e', 'vanio@abv.bg', '0889223600', 'angelina-jolie-1588166967.jpg'),
	(11, 'Georgi', '$2a$10$fTxsC5c6d2pYESq0dOXyvupOKG7MI5xBedkK1nSzsCifntdVuy7o2', 'gosho@abv.bg', '0889223600', 'margot-robbie-1588168612.jpg'),
	(12, 'Simona', '$2a$10$Xg7NKXm/d9Y//SZSknXIT.5sAs5xxkoIGNQapKtAkwU7614gNVNv2', 'simonkata@abv.bg', '0879654525', 'kim-kardashian-1588170307.jpg'),
	(13, 'Gergana', '$2a$10$jxYtjTGiG3i/jC/15n3FHOqiDppEf1uCeMJwaiKJ4vuOd6gbmBoYm', 'geri@abv.bg', '0898384661', 'julia-roberts-1588168040.jpg'),
	(14, 'Luchiq', '$2a$10$zYxFg.0Vddmi0tmgLj.CIe6WI6MEwB2p/NO2ysInkz/E5fs6N7KPy', 'lulu@abv.bg', '0879654123', 'meryl-streep-1588168280.jpg'),
	(15, 'Sitniq', '$2a$10$yWr5cmOUCkzq/BvkLdRlYOaZzu90daq47G.hUth.g6lcxyT4QJzI6', 'sitniq@abv.bg', '0898384661', 'bella-hadid-1588170435.jpg'),
	(16, 'Ivo', '$2a$10$Ot3FHNHN/f3xynMCSyK7xutdEDgmbNK26jz0Ftg/8DVe35gHJiiZe', 'iivvoo@abv.bg', '0898384661', 'prah-winfrey-1588168908.jpg'),
	(17, 'Viktoriq', '$2a$10$bcU.PtayZ9mg/43AUYqdGehyMkabHaiwHN8nPD0CNcB227I32.18y', 'vki@abv.bg', '0879546525', 'hailey-baldwin-1588168448.jpg'),
	(18, 'Snejina', '$2a$10$HXTkpcNLHHNWxJFRwCeOzOAV9OukJI3dmkR/bbWSNDZSndPp2j2.K', 'snej@abv.bg', '0898384661', 'jennifer-lawrence-1588169369.jpg'),
	(21, 'Mariq', '$2a$10$MJZW4OwM9jvUC2nnoIpXjOJti4RjFciuWdCbfgwqI5gAA76SLRy0C', 'mariq@abv.bg', '0885654525', 'emma-watson-1588169821.jpg');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;

-- Dumping structure for table peer_review.workitems
CREATE TABLE IF NOT EXISTS `workitems` (
  `workitem_id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(80) DEFAULT NULL,
  `description` longtext NOT NULL,
  `status_id` int(11) NOT NULL,
  `creator_id` int(11) DEFAULT NULL,
  `reviewer_id` int(11) DEFAULT NULL,
  `team_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`workitem_id`),
  KEY `workitems_statuses_fk` (`status_id`),
  KEY `workitems_reviewer_fk` (`reviewer_id`),
  KEY `workitems_creators_fk` (`creator_id`),
  KEY `workitems_teams_fk` (`team_id`),
  CONSTRAINT `workitems_creators_fk` FOREIGN KEY (`creator_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `workitems_reviewer_fk` FOREIGN KEY (`reviewer_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `workitems_statuses_fk` FOREIGN KEY (`status_id`) REFERENCES `statuses` (`status_id`),
  CONSTRAINT `workitems_teams_fk` FOREIGN KEY (`team_id`) REFERENCES `teams` (`team_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;

-- Dumping data for table peer_review.workitems: ~15 rows (approximately)
DELETE FROM `workitems`;
/*!40000 ALTER TABLE `workitems` DISABLE KEYS */;
INSERT INTO `workitems` (`workitem_id`, `title`, `description`, `status_id`, `creator_id`, `reviewer_id`, `team_id`) VALUES
	(1, 'Title_1', 'Description_1', 4, 6, 7, 1),
	(2, 'Title_2', '2222', 2, 6, 7, 1),
	(8, 'Title_3', 'Description_1', 1, 10, 11, 2),
	(9, 'Title_3', 'Description_2', 1, 10, 6, 2),
	(10, 'Title_4', 'Workitem for update purposes only, check update !!!', 4, 10, 11, 2),
	(11, 'Title_5', 'Description_3', 1, 10, 11, 2),
	(12, 'Title_6', 'Description_4', 1, 10, 11, 2),
	(13, 'Title_7', 'Description_5', 1, 10, 11, 2),
	(14, 'Title_8', 'Description_6', 1, 10, 11, 2),
	(15, 'Title_9', 'Description_7', 1, 10, 11, 2),
	(16, 'Title_10', 'Description_8', 1, 10, 11, 2),
	(17, 'Title_11', 'Description_9', 1, 7, 9, 1),
	(18, 'Title_12', 'Workitem for test purposes only!', 1, 7, 8, 1),
	(19, 'workitem_with_request', 'Workitem for test purposes only!', 1, 10, 11, 2),
	(20, 'Test Item Web', 'Test Item Web', 1, 10, 11, 2);
/*!40000 ALTER TABLE `workitems` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

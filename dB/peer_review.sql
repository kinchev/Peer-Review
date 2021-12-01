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
DROP DATABASE IF EXISTS `peer_review`;
CREATE DATABASE IF NOT EXISTS `peer_review` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `peer_review`;

-- Dumping structure for table peer_review.attachments
CREATE TABLE IF NOT EXISTS `attachments` (
                                             `attachment_id` int(11) NOT NULL AUTO_INCREMENT,
                                             `file_name` varchar(100) NOT NULL,
                                             `workitem_id` int(11) NOT NULL,
                                             PRIMARY KEY (`attachment_id`),
                                             KEY `attachments_workitems_fk` (`workitem_id`),
                                             CONSTRAINT `attachments_workitems_fk` FOREIGN KEY (`workitem_id`) REFERENCES `workitems` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table peer_review.comments
CREATE TABLE IF NOT EXISTS `comments` (
                                          `comment_id` int(11) NOT NULL AUTO_INCREMENT,
                                          `reviewer_id` int(11) NOT NULL,
                                          `comment_text` varchar(256) NOT NULL,
                                          `workitem_id` int(11) NOT NULL,
                                          PRIMARY KEY (`comment_id`),
                                          KEY `comments_reviewer_fk` (`reviewer_id`),
                                          KEY `comments_workitems_fk` (`workitem_id`),
                                          CONSTRAINT `comments_reviewer_fk` FOREIGN KEY (`reviewer_id`) REFERENCES `users` (`user_id`),
                                          CONSTRAINT `comments_workitems_fk` FOREIGN KEY (`workitem_id`) REFERENCES `workitems` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

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
                                                 CONSTRAINT `review_requests_fk` FOREIGN KEY (`workitem_id`) REFERENCES `workitems` (`id`),
                                                 CONSTRAINT `review_requests_reviewer_fk` FOREIGN KEY (`reviewer_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table peer_review.statuses
CREATE TABLE IF NOT EXISTS `statuses` (
                                          `status_id` int(11) NOT NULL AUTO_INCREMENT,
                                          `status` varchar(50) NOT NULL,
                                          PRIMARY KEY (`status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table peer_review.teams
CREATE TABLE IF NOT EXISTS `teams` (
                                       `team_id` int(11) NOT NULL AUTO_INCREMENT,
                                       `name` varchar(30) NOT NULL,
                                       `owner_id` int(11) NOT NULL,
                                       PRIMARY KEY (`team_id`),
                                       KEY `teams_users_fk` (`owner_id`),
                                       CONSTRAINT `teams_users_fk` FOREIGN KEY (`owner_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table peer_review.teams_users
CREATE TABLE IF NOT EXISTS `teams_users` (
                                             `team_id` int(11) NOT NULL,
                                             `member_id` int(11) NOT NULL,
                                             KEY `teams_users_teams_fk` (`team_id`),
                                             KEY `teams_users_users_fk` (`member_id`),
                                             CONSTRAINT `teams_users_teams_fk` FOREIGN KEY (`team_id`) REFERENCES `teams` (`team_id`),
                                             CONSTRAINT `teams_users_users_fk` FOREIGN KEY (`member_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table peer_review.teams_workitems
CREATE TABLE IF NOT EXISTS `teams_workitems` (
                                                 `team_id` int(11) NOT NULL,
                                                 `workitem_id` int(11) NOT NULL,
                                                 KEY `teams_workitems_teams_fk` (`team_id`),
                                                 KEY `teams_workitems_workitems_fk` (`workitem_id`),
                                                 CONSTRAINT `teams_workitems_teams_fk` FOREIGN KEY (`team_id`) REFERENCES `teams` (`team_id`),
                                                 CONSTRAINT `teams_workitems_workitems_fk` FOREIGN KEY (`workitem_id`) REFERENCES `workitems` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table peer_review.users
CREATE TABLE IF NOT EXISTS `users` (
                                       `user_id` int(11) NOT NULL AUTO_INCREMENT,
                                       `username` varchar(20) NOT NULL,
                                       `password` varchar(50) NOT NULL,
                                       `email` varchar(50) NOT NULL,
                                       `phone_number` varchar(10) NOT NULL,
                                       `photo_name` varchar(100) DEFAULT NULL,
                                       PRIMARY KEY (`user_id`),
                                       KEY `users_images_fk` (`photo_name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

-- Dumping structure for table peer_review.workitems
CREATE TABLE IF NOT EXISTS `workitems` (
                                           `id` int(11) NOT NULL AUTO_INCREMENT,
                                           `title` varchar(80) NOT NULL,
                                           `description` longtext NOT NULL,
                                           `status_id` int(11) NOT NULL,
                                           PRIMARY KEY (`id`),
                                           KEY `workitems_statuses_fk` (`status_id`),
                                           CONSTRAINT `workitems_statuses_fk` FOREIGN KEY (`status_id`) REFERENCES `statuses` (`status_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

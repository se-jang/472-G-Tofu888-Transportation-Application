-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.30 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.1.0.6537
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Dumping structure for table transport_application.history
CREATE TABLE IF NOT EXISTS `history` (
  `history_id` varchar(36) COLLATE utf8mb4_general_ci NOT NULL,
  `amount` double NOT NULL,
  `payment_date` datetime(6) DEFAULT NULL,
  `order_id` varchar(36) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`history_id`),
  UNIQUE KEY `UKorvr1b42qqxyl8v4rixyii9ma` (`order_id`),
  CONSTRAINT `FKloisvnff08fitcwktyqgbcx4d` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table transport_application.history: ~0 rows (approximately)
DELETE FROM `history`;
INSERT INTO `history` (`history_id`, `amount`, `payment_date`, `order_id`) VALUES
	('292f8215-2410-41c5-8a2c-c45ca59b790e', 1000, '2025-02-13 13:12:10.822939', 'cf005636-eace-4f20-928e-17396cf46177');

-- Dumping structure for table transport_application.orders
CREATE TABLE IF NOT EXISTS `orders` (
  `id` varchar(36) COLLATE utf8mb4_general_ci NOT NULL,
  `customer_address` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `customer_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `date` datetime(6) DEFAULT NULL,
  `delivered_date` datetime(6) DEFAULT NULL,
  `payment_link` text COLLATE utf8mb4_general_ci,
  `shipment_doc_dir` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `status` tinyint DEFAULT NULL,
  `total` double NOT NULL,
  `user_id` varchar(36) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `worker_id` varchar(36) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKel9kyl84ego2otj2accfd8mr7` (`user_id`),
  KEY `FKe1wuebvlqtrkl7ma2li5vnrcp` (`worker_id`),
  CONSTRAINT `FKe1wuebvlqtrkl7ma2li5vnrcp` FOREIGN KEY (`worker_id`) REFERENCES `transportation_worker` (`id`),
  CONSTRAINT `FKel9kyl84ego2otj2accfd8mr7` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table transport_application.orders: ~0 rows (approximately)
DELETE FROM `orders`;
INSERT INTO `orders` (`id`, `customer_address`, `customer_name`, `date`, `delivered_date`, `payment_link`, `shipment_doc_dir`, `status`, `total`, `user_id`, `worker_id`) VALUES
	('1e265109-7b9e-4d8f-a51f-47e7811f2f90', 'Bangkok ', 'Konvy', '2025-02-13 13:11:01.265726', NULL, 'https://checkout.stripe.com/c/pay/cs_test_a17RArBjc7WS6VDQodcdxYXcPt8hOfoTvOBZnlfsDqfnszT0qPQM5WCgrt#fidkdWxOYHwnPyd1blpxYHZxWjA0VGowTmExQktqMmNGPWdcUD1Ud2JkXzVPV309ME1sVWRqUXZLf0dEf3BvZjwxSVR9YDxxaDZgU29AcnJzUDxtcXVxPW1xNGBvUl9RM09DNFdLRmcyVUpmNTVzSzBXQTBIZCcpJ2N3amhWYHdzYHcnP3F3cGApJ2lkfGpwcVF8dWAnPyd2bGtiaWBabHFgaCcpJ2BrZGdpYFVpZGZgbWppYWB3dic%2FcXdwYHgl', NULL, 0, 1500, 'b34a5bc4-a803-4b7d-bc44-6084d3fed71d', NULL),
	('cf005636-eace-4f20-928e-17396cf46177', 'Bangkok ', 'Konvy', '2025-02-13 13:11:41.558742', NULL, 'https://checkout.stripe.com/c/pay/cs_test_a15qMfMy3yboc1XVf1pz9txdMZPlEc188sXgjm9niYOsoYpXvZhzj66RSx#fidkdWxOYHwnPyd1blpxYHZxWjA0VGowTmExQktqMmNGPWdcUD1Ud2JkXzVPV309ME1sVWRqUXZLf0dEf3BvZjwxSVR9YDxxaDZgU29AcnJzUDxtcXVxPW1xNGBvUl9RM09DNFdLRmcyVUpmNTVzSzBXQTBIZCcpJ2N3amhWYHdzYHcnP3F3cGApJ2lkfGpwcVF8dWAnPyd2bGtiaWBabHFgaCcpJ2BrZGdpYFVpZGZgbWppYWB3dic%2FcXdwYHgl', 'src\\main\\resources\\static\\images\\uploads\\1739427761806_GAI_SE.pdf', 6, 1000, 'b34a5bc4-a803-4b7d-bc44-6084d3fed71d', '9702bb1c-460c-42d3-97f3-183efb7294ca');

-- Dumping structure for table transport_application.order_line
CREATE TABLE IF NOT EXISTS `order_line` (
  `quantity` int NOT NULL,
  `order_id` varchar(36) COLLATE utf8mb4_general_ci NOT NULL,
  `product_id` varchar(36) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`order_id`,`product_id`),
  KEY `FKpf904tci8garypkvm32cqupye` (`product_id`),
  CONSTRAINT `FKk9f9t1tmkbq5w27u8rrjbxxg6` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FKpf904tci8garypkvm32cqupye` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table transport_application.order_line: ~0 rows (approximately)
DELETE FROM `order_line`;
INSERT INTO `order_line` (`quantity`, `order_id`, `product_id`) VALUES
	(10, '1e265109-7b9e-4d8f-a51f-47e7811f2f90', '0b34fcae-fcc4-4a25-90c9-30f571499015'),
	(10, '1e265109-7b9e-4d8f-a51f-47e7811f2f90', '88294c36-b280-400c-868d-3211152afec2'),
	(20, 'cf005636-eace-4f20-928e-17396cf46177', '4026a253-28f9-443b-aae3-5952d210695c');

-- Dumping structure for table transport_application.product
CREATE TABLE IF NOT EXISTS `product` (
  `id` varchar(36) COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `type` tinyint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table transport_application.product: ~0 rows (approximately)
DELETE FROM `product`;
INSERT INTO `product` (`id`, `name`, `type`) VALUES
	('0b34fcae-fcc4-4a25-90c9-30f571499015', 'Supplements', 2),
	('4026a253-28f9-443b-aae3-5952d210695c', 'Masks', 0),
	('88294c36-b280-400c-868d-3211152afec2', 'Sunscreens', 0);

-- Dumping structure for table transport_application.receipt
CREATE TABLE IF NOT EXISTS `receipt` (
  `receipt_id` varchar(36) COLLATE utf8mb4_general_ci NOT NULL,
  `create_at` datetime(6) DEFAULT NULL,
  `order_id` varchar(36) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`receipt_id`),
  UNIQUE KEY `UK50q8oumreljv08ygfygu75kbt` (`order_id`),
  CONSTRAINT `FKo8fi6dx59tstuoahtrp0dpnom` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table transport_application.receipt: ~0 rows (approximately)
DELETE FROM `receipt`;
INSERT INTO `receipt` (`receipt_id`, `create_at`, `order_id`) VALUES
	('8df49b1f-f7b6-4a28-b1b1-c4dcba62a01b', '2025-02-13 13:11:02.084970', '1e265109-7b9e-4d8f-a51f-47e7811f2f90'),
	('d0ffd693-74ef-4309-a021-26d3f808c0a6', '2025-02-13 13:11:42.175950', 'cf005636-eace-4f20-928e-17396cf46177');

-- Dumping structure for table transport_application.transportation_worker
CREATE TABLE IF NOT EXISTS `transportation_worker` (
  `id` varchar(36) COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `phone_number` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `profile_picture` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `status` tinyint DEFAULT NULL,
  `username` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table transport_application.transportation_worker: ~0 rows (approximately)
DELETE FROM `transportation_worker`;
INSERT INTO `transportation_worker` (`id`, `email`, `name`, `password`, `phone_number`, `profile_picture`, `status`, `username`) VALUES
	('9702bb1c-460c-42d3-97f3-183efb7294ca', 'worker@example.com', 'worker worker', '$2a$12$tfzHxYx3excr4/Sujp38mu.o4Bo4xFpDsTZiMBzy2jcaTB0fQy5om', '0998887764', '/images/default-profile.png', 0, 'worker');

-- Dumping structure for table transport_application.user
CREATE TABLE IF NOT EXISTS `user` (
  `id` varchar(36) COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `phone_number` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `profile_picture` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `role` tinyint DEFAULT NULL,
  `username` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table transport_application.user: ~0 rows (approximately)
DELETE FROM `user`;
INSERT INTO `user` (`id`, `email`, `name`, `password`, `phone_number`, `profile_picture`, `role`, `username`) VALUES
	('1a2090a7-0255-4ad6-8ea6-f77af89e8eb4', 'admin@example.com', 'admin admin', '$2a$12$yeG5qyWWwvhZWpThrkMxF.BfRlmWwWpfms6aUxcBvwVP1Mc0XT1Bi', '0123456789', '/images/default-profile.png', 0, 'admin'),
	('262143c7-a579-42de-b872-c520467b2719', 'user2@example.com', 'new user', '$2a$12$GUfHsU2SgN2gksBkolKBW.G4MRGrX5aCCU104JKqeNQTLKhsC0Th2', '0112223345', '/images/default-profile.png', 1, 'user2'),
	('b34a5bc4-a803-4b7d-bc44-6084d3fed71d', 'customer@example.com', 'customer customer', '$2a$12$bvupl5LAR/hxlKoBv5ArV.O1xzeCMbYY8ZrlZhychs80sVIqZQ.Te', '0998887765', '/images/default-profile.png', 1, 'customer');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

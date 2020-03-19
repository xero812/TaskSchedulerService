CREATE TABLE `task` (
  `id` binary(16) NOT NULL,
  `message` varchar(64) DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_timestamp` (`timestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
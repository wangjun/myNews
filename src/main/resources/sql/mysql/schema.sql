drop table if exists news;
drop table if exists news_user;

CREATE TABLE `news` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `link` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `claw_time` datetime DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `description` text COMMENT '内容',
  `hash` varchar(255) DEFAULT NULL,
  `pub_date` datetime DEFAULT NULL,
  `source` varchar(255) DEFAULT NULL COMMENT '来源',
  `catalog` varchar(255) DEFAULT NULL,
  `syn_qshp` tinyint(1) DEFAULT '0',
  `syn_weibo` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4297 DEFAULT CHARSET=utf8


CREATE TABLE `news_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `register_date` datetime DEFAULT NULL,
  `roles` varchar(255) DEFAULT NULL,
  `salt` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8
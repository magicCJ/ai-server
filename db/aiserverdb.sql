-- MySQL dump 10.13  Distrib 5.7.26, for Win64 (x86_64)
--
-- Host: 124.222.169.184    Database: aiserverdb
-- ------------------------------------------------------
-- Server version	8.0.32

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
-- Table structure for table `conversation_history`
--

DROP TABLE IF EXISTS `conversation_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `conversation_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `version` int NOT NULL DEFAULT '1' COMMENT '版本',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `shop_type` tinyint NOT NULL COMMENT '商品类型，1-文字会员，2-画图会员，3-GPT4.0会员',
  `batch_no` varchar(128) DEFAULT NULL COMMENT '批次号',
  `task_id` varchar(256) DEFAULT NULL COMMENT '任务ID',
  `history_info` json NOT NULL COMMENT ' 历史聊天信息',
  `feature` json DEFAULT NULL COMMENT '扩展信息',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_batch_no` (`batch_no`) USING BTREE,
  KEY `idx_user_shop_scene` (`user_id`,`shop_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100152 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='会话历史信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `conversation_title`
--

DROP TABLE IF EXISTS `conversation_title`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `conversation_title` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `version` int NOT NULL DEFAULT '1' COMMENT '版本',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `shop_type` tinyint NOT NULL COMMENT '商品类型，1-文字会员，2-画图会员，3-GPT4.0会员',
  `batch_no` varchar(128) DEFAULT NULL COMMENT '批次号',
  `history_title` varchar(256) DEFAULT NULL COMMENT '历史标题',
  `feature` json DEFAULT NULL COMMENT '扩展信息',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_shop_scene` (`user_id`,`shop_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100061 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='会话标题历史信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `purchase_record`
--

DROP TABLE IF EXISTS `purchase_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `purchase_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `version` int NOT NULL DEFAULT '1' COMMENT '版本',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `pay_type` tinyint NOT NULL COMMENT '支付渠道 1-支付宝，2-微信',
  `shop_type` tinyint NOT NULL COMMENT '商品类型，1-文字会员，2-画图会员，3-GPT4.0会员',
  `shop_title` varchar(256) NOT NULL COMMENT '商品名称',
  `effective_days` tinyint NOT NULL COMMENT '有效天数',
  `price` decimal(8,2) NOT NULL COMMENT '价格，单位：元',
  `feature` json DEFAULT NULL COMMENT '扩展信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='购买记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rebate_record`
--

DROP TABLE IF EXISTS `rebate_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rebate_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `version` int NOT NULL DEFAULT '1' COMMENT '版本',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `inviter_id` bigint NOT NULL COMMENT '邀请用户ID',
  `inviter_name` bigint NOT NULL COMMENT '邀请用户昵称',
  `shop_type` tinyint NOT NULL COMMENT '商品类型，1-文字会员，2-画图会员，3-GPT4.0会员',
  `shop_title` varchar(256) NOT NULL COMMENT '商品名称',
  `price` decimal(10,2) NOT NULL COMMENT '价格，单位：元',
  `rebate_level` tinyint DEFAULT '1' COMMENT '返利级别',
  `rebate_ratio` decimal(10,2) NOT NULL COMMENT '返利比例',
  `rebate_amount` decimal(10,2) NOT NULL COMMENT '返利金额',
  `feature` json DEFAULT NULL COMMENT '扩展信息',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_rebate_amount` (`user_id`,`rebate_amount`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='返利记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `scene_info`
--

DROP TABLE IF EXISTS `scene_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scene_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `version` int NOT NULL DEFAULT '1' COMMENT '版本',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态，0-不可用，1-可用',
  `scene_type` tinyint NOT NULL COMMENT '场景类型，如：情感、职业等',
  `scene_icon` text NOT NULL COMMENT '场景ICON',
  `scene_title` varchar(64) NOT NULL COMMENT '场景标题',
  `scene_desc` varchar(1024) NOT NULL COMMENT '场景描述',
  `scene_prompt` varchar(2048) NOT NULL COMMENT '场景提示词',
  `fill_info` json DEFAULT NULL COMMENT '填充信息',
  `feature` json DEFAULT NULL COMMENT '扩展信息',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `scene_title` (`scene_title`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100029 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='场景提示词信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `shop_info`
--

DROP TABLE IF EXISTS `shop_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shop_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `version` int NOT NULL DEFAULT '1' COMMENT '版本',
  `type` tinyint NOT NULL COMMENT '商品类型，1-文字会员，2-画图会员，3-GPT4.0会员',
  `shop_title` varchar(256) NOT NULL COMMENT '商品名称',
  `shop_describe` varchar(256) NOT NULL COMMENT '商品描述',
  `effective_days` int NOT NULL COMMENT '有效天数',
  `price` decimal(8,2) NOT NULL COMMENT '价格，单位：元',
  `feature` json DEFAULT NULL COMMENT '扩展信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100002 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_info`
--

DROP TABLE IF EXISTS `user_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `version` int NOT NULL DEFAULT '1' COMMENT '版本',
  `nick_name` varchar(32) NOT NULL COMMENT '昵称',
  `open_id` varchar(256) NOT NULL COMMENT '普通用户的标识',
  `union_id` varchar(256) DEFAULT NULL COMMENT '用户统一标识',
  `head_img` varchar(256) DEFAULT NULL COMMENT '头像照片',
  `sex` tinyint DEFAULT NULL COMMENT '普通用户性别，1为男性，2为女性',
  `country` varchar(32) DEFAULT NULL COMMENT '国家，如中国为CN',
  `province` varchar(32) DEFAULT NULL COMMENT '普通用户个人资料填写的省份',
  `city` varchar(32) DEFAULT NULL COMMENT '普通用户个人资料填写的城市',
  `inviter_id` bigint DEFAULT NULL COMMENT '邀请人ID',
  `withdrawal_balance` decimal(10,2) DEFAULT '0.00' COMMENT '提现余额，单位：元',
  `feature` json DEFAULT NULL COMMENT '扩展信息',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_open_id` (`open_id`) USING BTREE,
  KEY `idx_inviter_id` (`inviter_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100004 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_vip_info`
--

DROP TABLE IF EXISTS `user_vip_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_vip_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `version` int NOT NULL DEFAULT '1' COMMENT '版本',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `type` tinyint NOT NULL COMMENT '类型，1-文字，2-画图，3-GPT4.0',
  `package_type` tinyint NOT NULL COMMENT '套餐类型，1-体验卡，2-会员',
  `total_number` int NOT NULL DEFAULT '0' COMMENT '总次数, -99表示不限次数',
  `text_remain_number` int NOT NULL DEFAULT '0' COMMENT '文字剩余次数, -99表示不限次数',
  `speed_remain_number` int NOT NULL DEFAULT '0' COMMENT '极速模式剩余次数, -99表示不限次数',
  `relax_remain_number` int NOT NULL DEFAULT '0' COMMENT '放松模式剩余次数, -99表示不限次数',
  `expired_time` datetime NOT NULL COMMENT '过期时间',
  `feature` json DEFAULT NULL COMMENT '扩展信息',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uni_user_id_type` (`user_id`,`type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100005 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户会员信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `withdrawal_record`
--

DROP TABLE IF EXISTS `withdrawal_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `withdrawal_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `version` int NOT NULL DEFAULT '1' COMMENT '版本',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `withdrawal_amount` decimal(10,2) NOT NULL COMMENT '提现金额，单位：元',
  `withdrawal_type` tinyint NOT NULL COMMENT '提现方式，1：支付宝，2：微信',
  `withdrawal_time` datetime NOT NULL COMMENT '提现时间',
  `status` tinyint NOT NULL COMMENT '状态',
  `feature` json DEFAULT NULL COMMENT '扩展信息',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='提现记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `withdrawal_way`
--

DROP TABLE IF EXISTS `withdrawal_way`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `withdrawal_way` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `version` int NOT NULL DEFAULT '1' COMMENT '版本',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `withdrawal_type` tinyint NOT NULL COMMENT '提现方式，1：支付宝，2：微信',
  `name` varchar(32) NOT NULL COMMENT '姓名',
  `phone` varchar(32) DEFAULT NULL COMMENT '手机号',
  `id_card` varchar(32) NOT NULL COMMENT '身份证',
  `bank_number` varchar(32) DEFAULT NULL COMMENT '银行卡号',
  `bank_name` varchar(128) DEFAULT NULL COMMENT '开户支行',
  `alipay_account` varchar(32) DEFAULT NULL COMMENT '支付宝账号',
  `wechat_account` varchar(32) DEFAULT NULL COMMENT '微信账号',
  `feature` json DEFAULT NULL COMMENT '扩展信息',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100003 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='提现方式';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-05-28 11:45:38

/*
SQLyog Ultimate v12.5.1 (64 bit)
MySQL - 8.0.12 : Database - automain
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`automain` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */;

USE `automain`;

/*Table structure for table `tb_config` */

DROP TABLE IF EXISTS `tb_config`;

CREATE TABLE `tb_config` (
  `config_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_key` varchar(64) NOT NULL COMMENT '配置key',
  `config_value` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置value',
  `config_comment` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置描述',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `is_delete` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除(0:否,1:是)',
  PRIMARY KEY (`config_id`),
  KEY `idx_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_config` */

/*Table structure for table `tb_dictionary` */

DROP TABLE IF EXISTS `tb_dictionary`;

CREATE TABLE `tb_dictionary` (
  `dictionary_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '字典表ID',
  `dict_table_name` varchar(64) NOT NULL COMMENT '表名',
  `dict_column_name` varchar(64) NOT NULL COMMENT '字段名',
  `dictionary_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典名',
  `dictionary_value` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典值',
  `sequence_number` smallint(5) unsigned NOT NULL DEFAULT '1' COMMENT '排序标识',
  `parent_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '父级ID',
  `is_leaf` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '是否是叶子节点(0:否,1:是)',
  `is_delete` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除(0:否,1:是)',
  PRIMARY KEY (`dictionary_id`),
  KEY `idx_order_label` (`sequence_number`),
  KEY `idx_table_column_parent` (`dict_table_name`,`dict_column_name`,`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_dictionary` */

/*Table structure for table `tb_menu` */

DROP TABLE IF EXISTS `tb_menu`;

CREATE TABLE `tb_menu` (
  `menu_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `request_url` varchar(256) DEFAULT NULL COMMENT '请求路径',
  `menu_name` varchar(32) NOT NULL COMMENT '菜单名称',
  `menu_icon` varchar(32) DEFAULT NULL COMMENT '菜单图标',
  `parent_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '父级ID',
  `top_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '顶级ID',
  `sequence_number` smallint(5) unsigned NOT NULL DEFAULT '1' COMMENT '菜单排序',
  `is_spread` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否默认展开(0:否,1:是)',
  `is_leaf` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '是否是叶子节点(0:否,1:是)',
  `is_delete` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除(0:否,1:是)',
  PRIMARY KEY (`menu_id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_menu_name` (`menu_name`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_menu` */

insert  into `tb_menu`(`menu_id`,`request_url`,`menu_name`,`menu_icon`,`parent_id`,`top_id`,`sequence_number`,`is_spread`,`is_leaf`,`is_delete`) values
(1,NULL,'系统管理','gears',0,0,1,1,0,0),
(2,'/dictionary/forward','字典管理','book',1,1,1,0,1,0),
(3,'/user/forward','用户管理','user',1,1,2,0,1,0),
(4,'/role/forward','角色管理','user-secret',1,1,3,0,1,0),
(5,'/menu/forward','菜单管理','navicon',1,1,4,0,1,0),
(6,'/config/forward','全局配置','cog',1,1,5,0,1,0),
(7,'/reload/cache/forward','刷新缓存','refresh',1,1,6,0,1,0),
(8,'/notice/forward','上线公告','arrow-circle-o-up',1,1,7,0,1,0),
(9,'/schedule/forward','定时任务','tasks',1,1,8,0,1,0);

/*Table structure for table `tb_role` */

DROP TABLE IF EXISTS `tb_role`;

CREATE TABLE `tb_role` (
  `role_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `role_label` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色标识',
  `is_delete` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除(0:否,1:是)',
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `uniq_role_label` (`role_label`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_role` */

insert  into `tb_role`(`role_id`,`role_name`,`role_label`,`is_delete`) values
(1,'管理员','admin',0);

/*Table structure for table `tb_role_menu` */

DROP TABLE IF EXISTS `tb_role_menu`;

CREATE TABLE `tb_role_menu` (
  `role_menu_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '角色菜单ID',
  `role_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '角色ID',
  `menu_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '菜单ID',
  `is_delete` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除(0:否,1;是)',
  PRIMARY KEY (`role_menu_id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_role_menu` */

/*Table structure for table `tb_role_request_mapping` */

DROP TABLE IF EXISTS `tb_role_request_mapping`;

CREATE TABLE `tb_role_request_mapping` (
  `role_request_mapping_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '角色路径关系ID',
  `role_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '角色ID',
  `request_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '请求路径',
  `is_delete` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除(0:否,1;是)',
  PRIMARY KEY (`role_request_mapping_id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_request_url` (`request_url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_role_request_mapping` */

/*Table structure for table `tb_schedule` */

DROP TABLE IF EXISTS `tb_schedule`;

CREATE TABLE `tb_schedule` (
  `schedule_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `schedule_name` varchar(32) NOT NULL COMMENT '任务名称',
  `schedule_url` varchar(256) NOT NULL COMMENT '任务请求url',
  `start_execute_time` timestamp NULL DEFAULT NULL COMMENT '开始执行时间',
  `delay_time` bigint(20) unsigned NOT NULL COMMENT '间隔时间长度(秒)',
  `last_execute_time` timestamp NULL DEFAULT NULL COMMENT '上次执行时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `is_delete` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否关闭(0:否,1:是)',
  PRIMARY KEY (`schedule_id`),
  UNIQUE KEY `uniq_schedule_url` (`schedule_url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_schedule` */

/*Table structure for table `tb_upload_file` */

DROP TABLE IF EXISTS `tb_upload_file`;

CREATE TABLE `tb_upload_file` (
  `upload_file_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '文件ID',
  `file_extension` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件扩展名',
  `file_path` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件相对路径',
  `file_size` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '文件大小(字节)',
  `upload_time` timestamp NOT NULL COMMENT '上传日期',
  `file_md5` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件MD5值',
  `image_width` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '图片文件宽度',
  `image_height` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '图片文件高度',
  PRIMARY KEY (`upload_file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_upload_file` */

/*Table structure for table `tb_upload_relation` */

DROP TABLE IF EXISTS `tb_upload_relation`;

CREATE TABLE `tb_upload_relation` (
  `upload_relation_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '关系ID',
  `upload_file_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '文件ID',
  `record_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '记录ID',
  `record_table_name` varchar(64) NOT NULL COMMENT '记录表名',
  `record_label` varchar(32) DEFAULT NULL COMMENT '记录标记',
  `sequence_number` smallint(5) unsigned NOT NULL DEFAULT '1' COMMENT '展示顺序',
  `is_delete` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除(0:否,1:是)',
  PRIMARY KEY (`upload_relation_id`),
  KEY `idx_id_name_label` (`record_id`,`record_table_name`,`record_label`),
  KEY `idx_sequence_number` (`sequence_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_upload_relation` */

/*Table structure for table `tb_user` */

DROP TABLE IF EXISTS `tb_user`;

CREATE TABLE `tb_user` (
  `user_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password_md5` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码MD5值',
  `cellphone` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
  `is_delete` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除(0:否,1:是)',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uniq_user_name` (`user_name`),
  KEY `idx_cellphone` (`cellphone`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_user` */

insert  into `tb_user`(`user_id`,`user_name`,`password_md5`,`cellphone`,`create_time`,`email`,`is_delete`) values
(1,'admin','e10adc3949ba59abbe56e057f20f883e','13111111111','2018-03-07 00:00:00','zhangyu13393@sina.com',0);

/*Table structure for table `tb_user_role` */

DROP TABLE IF EXISTS `tb_user_role`;

CREATE TABLE `tb_user_role` (
  `user_role_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户角色ID',
  `user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '用户ID',
  `role_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '角色ID',
  `is_delete` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除(0:否,1:是)',
  PRIMARY KEY (`user_role_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_user_role` */

insert  into `tb_user_role`(`user_role_id`,`user_id`,`role_id`,`is_delete`) values
(1,1,1,0);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

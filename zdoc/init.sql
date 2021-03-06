/*
SQLyog Ultimate v13.1.1 (64 bit)
MySQL - 8.0.18 : Database - automain
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`automain` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `automain`;

/*Table structure for table `sys_config` */

DROP TABLE IF EXISTS `sys_config`;

CREATE TABLE `sys_config` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` int(10) unsigned NOT NULL COMMENT '创建时间',
  `update_time` int(10) unsigned NOT NULL COMMENT '更新时间',
  `is_valid` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '是否有效(0:否,1:是)',
  `config_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置项键',
  `config_value` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置项值',
  `config_remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '配置项描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_congig_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `sys_config` */

/*Table structure for table `sys_dictionary` */

DROP TABLE IF EXISTS `sys_dictionary`;

CREATE TABLE `sys_dictionary` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` int(10) unsigned NOT NULL COMMENT '创建时间',
  `update_time` int(10) unsigned NOT NULL COMMENT '更新时间',
  `table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '表名',
  `column_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字段名',
  `dictionary_key` int(11) NOT NULL COMMENT '字典键',
  `dictionary_value` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典值',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_table_column_key` (`table_name`,`column_name`,`dictionary_key`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `sys_dictionary` */

insert  into `sys_dictionary`(`id`,`create_time`,`update_time`,`table_name`,`column_name`,`dictionary_key`,`dictionary_value`) values (1,0,0,'test','test_dictionary',0,'字典0'),(2,0,0,'test','test_dictionary',1,'字典1'),(3,0,0,'test','test_dictionary',2,'字典2');

/*Table structure for table `sys_file` */

DROP TABLE IF EXISTS `sys_file`;

CREATE TABLE `sys_file` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件GID',
  `create_time` int(10) unsigned NOT NULL COMMENT '创建时间',
  `update_time` int(10) unsigned NOT NULL COMMENT '更新时间',
  `is_valid` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '是否有效(0:否,1:是)',
  `file_extension` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件扩展名',
  `file_path` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件相对路径',
  `file_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '文件大小(字节)',
  `file_md5` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件MD5值',
  `image_width` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '图片宽度',
  `image_height` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '图片高度',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_gid` (`gid`),
  KEY `idx_file_md5` (`file_md5`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `sys_file` */

/*Table structure for table `sys_menu` */

DROP TABLE IF EXISTS `sys_menu`;

CREATE TABLE `sys_menu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` int(10) unsigned NOT NULL COMMENT '创建时间',
  `update_time` int(10) unsigned NOT NULL COMMENT '更新时间',
  `menu_path` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '菜单路径',
  `menu_name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `menu_icon` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单图标',
  `parent_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '父级ID',
  `sequence_number` smallint(5) unsigned NOT NULL DEFAULT '1' COMMENT '菜单排序',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_sequence_number` (`sequence_number`),
  KEY `idx_menu_name` (`menu_name`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `sys_menu` */

insert  into `sys_menu`(`id`,`create_time`,`update_time`,`menu_path`,`menu_name`,`menu_icon`,`parent_id`,`sequence_number`) values (1,0,0,NULL,'开发工具','el-icon-s-cooperation',0,1),(2,0,0,NULL,'系统管理','el-icon-s-tools',0,2),(3,0,0,'/dev/generator','生成器','el-icon-s-platform',1,1),(4,0,0,'/dev/test','测试','el-icon-s-flag',1,2),(5,0,0,'/system/dictionary','字典管理','el-icon-notebook-2',2,1),(6,0,0,'/system/menu','菜单管理','el-icon-menu',2,2),(7,0,0,'/system/user','人员管理','el-icon-user',2,3),(8,0,0,'/system/role','角色管理','el-icon-s-custom',2,4),(9,0,0,'/system/privilege','权限管理','el-icon-s-claim',2,5),(10,0,0,'/system/schedule','任务管理','el-icon-timer',2,6),(11,0,0,'/system/config','配置管理','el-icon-setting',2,7),(12,0,0,'/system/file','文件管理','el-icon-files',2,8);

/*Table structure for table `sys_privilege` */

DROP TABLE IF EXISTS `sys_privilege`;

CREATE TABLE `sys_privilege` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` int(10) unsigned NOT NULL COMMENT '创建时间',
  `update_time` int(10) unsigned NOT NULL COMMENT '更新时间',
  `privilege_label` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限标识',
  `privilege_name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限名称',
  `parent_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '父级ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_privilege_label` (`privilege_label`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_privilege_name` (`privilege_name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `sys_privilege` */

insert  into `sys_privilege`(`id`,`create_time`,`update_time`,`privilege_label`,`privilege_name`,`parent_id`) values (1,0,0,'admin','所有权限',0);

/*Table structure for table `sys_role` */

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` int(10) unsigned NOT NULL COMMENT '创建时间',
  `update_time` int(10) unsigned NOT NULL COMMENT '更新时间',
  `role_name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `role_label` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色标识',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_label` (`role_label`),
  KEY `idx_role_name` (`role_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `sys_role` */

insert  into `sys_role`(`id`,`create_time`,`update_time`,`role_name`,`role_label`) values (1,0,0,'admin','admin'),(2,0,0,'guest','guest');

/*Table structure for table `sys_role_menu` */

DROP TABLE IF EXISTS `sys_role_menu`;

CREATE TABLE `sys_role_menu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` int(10) unsigned NOT NULL COMMENT '创建时间',
  `update_time` int(10) unsigned NOT NULL COMMENT '更新时间',
  `is_valid` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '是否有效(0:否,1:是)',
  `role_id` int(10) unsigned NOT NULL COMMENT 'sys_role表主键',
  `menu_id` int(10) unsigned NOT NULL COMMENT 'sys_menu表主键',
  PRIMARY KEY (`id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `sys_role_menu` */

insert  into `sys_role_menu`(`id`,`create_time`,`update_time`,`is_valid`,`role_id`,`menu_id`) values (1,0,0,1,1,1),(2,0,0,1,1,2),(3,0,0,1,1,3),(4,0,0,1,1,4),(5,0,0,1,1,5),(6,0,0,1,1,6),(7,0,0,1,1,7),(8,0,0,1,1,8),(9,0,0,1,1,9),(10,0,0,1,1,10),(11,0,0,1,1,11),(12,0,0,1,1,12);

/*Table structure for table `sys_role_privilege` */

DROP TABLE IF EXISTS `sys_role_privilege`;

CREATE TABLE `sys_role_privilege` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` int(10) unsigned NOT NULL COMMENT '创建时间',
  `update_time` int(10) unsigned NOT NULL COMMENT '更新时间',
  `is_valid` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '是否有效(0:否,1:是)',
  `role_id` int(10) unsigned NOT NULL COMMENT 'sys_role表主键',
  `privilege_id` int(10) unsigned NOT NULL COMMENT 'sys_privilege表主键',
  PRIMARY KEY (`id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_privilege_id` (`privilege_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `sys_role_privilege` */

insert  into `sys_role_privilege`(`id`,`create_time`,`update_time`,`is_valid`,`role_id`,`privilege_id`) values (1,0,0,1,1,1);

/*Table structure for table `sys_schedule` */

DROP TABLE IF EXISTS `sys_schedule`;

CREATE TABLE `sys_schedule` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` int(10) unsigned NOT NULL COMMENT '创建时间',
  `update_time` int(10) unsigned NOT NULL COMMENT '更新时间',
  `is_valid` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '是否有效(0:否,1:是)',
  `schedule_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务名称',
  `schedule_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务地址',
  `start_execute_time` int(10) unsigned NOT NULL COMMENT '开始执行时间',
  `period` int(10) unsigned NOT NULL COMMENT '间隔时间(秒)',
  `last_execute_time` int(10) unsigned DEFAULT NULL COMMENT '上次执行时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_schedule_url` (`schedule_url`),
  KEY `idx_schedule_name` (`schedule_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `sys_schedule` */

/*Table structure for table `sys_user` */

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户GID',
  `create_time` int(10) unsigned NOT NULL COMMENT '创建时间',
  `update_time` int(10) unsigned NOT NULL COMMENT '更新时间',
  `is_valid` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '是否有效(0:无效,1:有效)',
  `user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password_md5` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码MD5值',
  `real_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '真实姓名',
  `phone` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号',
  `email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱',
  `head_img_gid` char(36) DEFAULT NULL COMMENT '头像文件GID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_gid` (`gid`),
  KEY `idx_user_name` (`user_name`),
  KEY `idx_phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `sys_user` */

insert  into `sys_user`(`id`,`gid`,`create_time`,`update_time`,`is_valid`,`user_name`,`password_md5`,`real_name`,`phone`,`email`,`head_img_gid`) values (1,'1',0,0,1,'admin','e10adc3949ba59abbe56e057f20f883e','管理员','1','1',NULL),(2,'2',0,0,1,'guest','e10adc3949ba59abbe56e057f20f883e','访客','1','1',NULL);

/*Table structure for table `sys_user_role` */

DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` int(10) unsigned NOT NULL COMMENT '创建时间',
  `update_time` int(10) unsigned NOT NULL COMMENT '更新时间',
  `is_valid` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '是否有效(0:否,1:是)',
  `user_gid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'sys_user表gid',
  `role_id` int(10) unsigned NOT NULL COMMENT 'sys_role表主键',
  PRIMARY KEY (`id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_user_gid` (`user_gid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `sys_user_role` */

insert  into `sys_user_role`(`id`,`create_time`,`update_time`,`is_valid`,`user_gid`,`role_id`) values (1,0,0,1,'1',1);

/*Table structure for table `test` */

DROP TABLE IF EXISTS `test`;

CREATE TABLE `test` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '测试GID',
  `create_time` int(10) unsigned NOT NULL COMMENT '创建时间',
  `update_time` int(10) unsigned NOT NULL COMMENT '更新时间',
  `is_valid` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '是否有效(0:否,1:是)',
  `money` decimal(10,2) unsigned DEFAULT NULL COMMENT '金额',
  `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `test_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '测试名称',
  `test_dictionary` int(11) NOT NULL DEFAULT '0' COMMENT '测试字典(0:字典0,1:字典1,2:字典2)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_gid` (`gid`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `test` */

insert  into `test`(`id`,`gid`,`create_time`,`update_time`,`is_valid`,`money`,`remark`,`test_name`,`test_dictionary`) values (1,'0c83496e-4262-4c48-9867-570ace797206',1572779383,1572779383,1,10.00,'updateByIdForNotNullColumn remark','insertOne testName',0),(2,'cbd8e84f-eee3-40be-be0f-0a1670f77fd2',1572779383,1572779383,1,10.00,'updateByGidForNotNullColumn remark','insertOneReturnId testName',1),(3,'9f3239b4-e9dd-49a6-9b97-8aaf0fe9652c',1572779392,1572779392,1,1.00,'updateByIdForAllColumn remark','updateByIdForAllColumn testName',1),(4,'3ffa5db6-9172-4363-9b13-eb97fe101917',1572779392,1572779392,1,1.00,'updateByGidForAllColumn remark','updateByGidForAllColumn testName',1),(5,'f0353c79-7f6e-4292-b0ed-3d1cf24466fc',1572783383,1572779383,1,10.00,'updateByIdList remark','batchInsertTable2',0),(6,'d14a7a09-f3d7-4c2b-b83c-88614adcf8d6',1572785383,1572779383,1,10.00,'updateByIdList remark','batchInsertTable3',0),(7,'7608cacc-eb8b-4365-8378-34e6a4df0f31',1572787383,1572779383,1,10.00,'updateByIdList remark','batchInsertTable4',0),(8,'8e2e1dcb-06eb-4073-a2f3-486d7a644401',1572789383,1572779383,1,10.00,'updateByGidList remark','batchInsertTable5',1),(9,'146375ae-b20a-42a2-9f7f-dc8e7a576b5d',1572791383,1572779383,1,10.00,'updateByGidList remark','batchInsertTable6',1),(10,'8c6487aa-94d9-42eb-81db-b8ffa075dba3',1572793383,1572779383,1,10.00,'updateByGidList remark','batchInsertTable7',1),(11,'0bb4a8bb-006a-4355-a031-2771f82c4db2',1572795383,1572779383,1,10.00,'updateByParamMulti remark','batchInsertTable8',2),(12,'d386d617-9a88-4b46-9e84-c691f01e4d30',1572797383,1572779383,1,10.00,'updateByParamMulti remark','batchInsertTable9',2),(13,'5467332a-6889-42b2-9d46-18516bce8edc',1572779392,1572779392,0,0.00,'updateByParamInsertWhenNotExist remark','updateByParamInsertWhenNotExist testName',0);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

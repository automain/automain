/*
SQLyog Ultimate v13.1.1 (64 bit)
MySQL - 8.0.17 : Database - automain
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE=''NO_AUTO_VALUE_ON_ZERO'' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`automain` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION=''N'' */;

USE `automain`;

/*Table structure for table `sys_config` */

DROP TABLE IF EXISTS `sys_config`;

CREATE TABLE `sys_config` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT ''主键'',
  `create_time` int(10) unsigned NOT NULL COMMENT ''创建时间'',
  `update_time` int(10) unsigned NOT NULL COMMENT ''更新时间'',
  `is_valid` tinyint(3) unsigned NOT NULL DEFAULT ''1'' COMMENT ''是否有效(0:否,1:是)'',
  `config_key` varchar(32) NOT NULL COMMENT ''配置项键'',
  `config_value` varchar(256) NOT NULL COMMENT ''配置项值'',
  `config_remark` varchar(256) DEFAULT NULL COMMENT ''配置项描述'',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_congig_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `sys_config` */

/*Table structure for table `sys_dictionary` */

DROP TABLE IF EXISTS `sys_dictionary`;

CREATE TABLE `sys_dictionary` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT ''主键'',
  `create_time` int(10) unsigned NOT NULL COMMENT ''创建时间'',
  `update_time` int(10) unsigned NOT NULL COMMENT ''更新时间'',
  `is_valid` tinyint(3) unsigned NOT NULL DEFAULT ''1'' COMMENT ''是否有效(0:否,1:是)'',
  `table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT ''表名'',
  `column_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT ''字段名'',
  `dictionary_key` int(11) NOT NULL COMMENT ''字典键'',
  `dictionary_value` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT ''字典值'',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_table_column_key` (`table_name`,`column_name`,`dictionary_key`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `sys_dictionary` */

insert  into `sys_dictionary`(`id`,`create_time`,`update_time`,`is_valid`,`table_name`,`column_name`,`dictionary_key`,`dictionary_value`) values
(1,1567839156,1567839156,1,''test'',''test_dictionary'',0,''字典0''),
(2,1567839156,1567839156,1,''test'',''test_dictionary'',1,''字典1''),
(3,1567839156,1567839156,1,''test'',''test_dictionary'',2,''字典2'');

/*Table structure for table `sys_file` */

DROP TABLE IF EXISTS `sys_file`;

CREATE TABLE `sys_file` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT ''主键'',
  `gid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT ''文件GID'',
  `create_time` int(10) unsigned NOT NULL COMMENT ''创建时间'',
  `file_extension` varchar(16) NOT NULL COMMENT ''文件扩展名'',
  `file_path` varchar(64) NOT NULL COMMENT ''文件相对路径'',
  `file_size` int(10) unsigned NOT NULL DEFAULT ''0'' COMMENT ''文件大小(字节)'',
  `file_md5` char(32) NOT NULL COMMENT ''文件MD5值'',
  `image_width` smallint(5) unsigned NOT NULL DEFAULT ''0'' COMMENT ''图片宽度'',
  `image_height` smallint(5) unsigned NOT NULL DEFAULT ''0'' COMMENT ''图片高度'',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_gid` (`gid`),
  UNIQUE KEY `uk_file_md5` (`file_md5`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `sys_file` */

/*Table structure for table `sys_file_relation` */

DROP TABLE IF EXISTS `sys_file_relation`;

CREATE TABLE `sys_file_relation` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT ''主键'',
  `create_time` int(10) unsigned NOT NULL COMMENT ''创建时间'',
  `update_time` int(10) unsigned NOT NULL COMMENT ''更新时间'',
  `is_valid` tinyint(3) unsigned NOT NULL DEFAULT ''1'' COMMENT ''是否有效(0:否,1:是)'',
  `file_gid` char(36) NOT NULL COMMENT ''sys_file表gid'',
  `record_table_name` varchar(64) NOT NULL COMMENT ''记录表名'',
  `record_id` int(11) DEFAULT NULL COMMENT ''记录ID'',
  `record_gid` char(36) DEFAULT NULL COMMENT ''记录GID'',
  `record_label` varchar(32) DEFAULT NULL COMMENT ''记录标识'',
  `sequence_number` smallint(5) unsigned NOT NULL DEFAULT ''1'' COMMENT ''展示顺序'',
  `file_table_name` varchar(64) NOT NULL COMMENT ''关联的文件表表名'',
  PRIMARY KEY (`id`),
  KEY `idx_record_table_name` (`record_table_name`),
  KEY `idx_record_id` (`record_id`),
  KEY `idx_record_gid` (`record_gid`),
  KEY `idx_sequence_number` (`sequence_number`),
  KEY `idx_file_gid` (`file_gid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `sys_file_relation` */

/*Table structure for table `sys_menu` */

DROP TABLE IF EXISTS `sys_menu`;

CREATE TABLE `sys_menu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT ''主键'',
  `create_time` int(10) unsigned NOT NULL COMMENT ''创建时间'',
  `update_time` int(10) unsigned NOT NULL COMMENT ''更新时间'',
  `is_valid` tinyint(3) unsigned NOT NULL DEFAULT ''1'' COMMENT ''是否有效(0:否,1:是)'',
  `menu_path` varchar(64) DEFAULT NULL COMMENT ''菜单路径'',
  `menu_name` varchar(16) NOT NULL COMMENT ''菜单名称'',
  `menu_icon` varchar(16) NOT NULL COMMENT ''菜单图标'',
  `parent_id` int(10) unsigned NOT NULL DEFAULT ''0'' COMMENT ''父级ID'',
  `top_id` int(10) unsigned NOT NULL DEFAULT ''0'' COMMENT ''顶级ID'',
  `sequence_number` smallint(5) unsigned NOT NULL DEFAULT ''1'' COMMENT ''菜单排序'',
  `is_leaf` tinyint(3) unsigned NOT NULL DEFAULT ''1'' COMMENT ''是否是叶子节点(0:否,1:是)'',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_sequence_number` (`sequence_number`),
  KEY `idx_menu_name` (`menu_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `sys_menu` */

/*Table structure for table `sys_privilege` */

DROP TABLE IF EXISTS `sys_privilege`;

CREATE TABLE `sys_privilege` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT ''主键'',
  `create_time` int(10) unsigned NOT NULL COMMENT ''创建时间'',
  `update_time` int(10) unsigned NOT NULL COMMENT ''更新时间'',
  `is_valid` tinyint(3) unsigned NOT NULL DEFAULT ''1'' COMMENT ''是否有效(0:否,1:是)'',
  `privilege_label` varchar(16) NOT NULL COMMENT ''权限标识'',
  `privilege_name` varchar(16) NOT NULL COMMENT ''权限名称'',
  `parent_id` int(10) unsigned NOT NULL DEFAULT ''0'' COMMENT ''父级ID'',
  `top_id` int(10) unsigned NOT NULL DEFAULT ''0'' COMMENT ''顶级ID'',
  `is_leaf` tinyint(3) unsigned NOT NULL DEFAULT ''1'' COMMENT ''是否是叶子节点(0:否,1:是)'',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_privilege_label` (`privilege_label`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_privilege_name` (`privilege_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `sys_privilege` */

/*Table structure for table `sys_role` */

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT ''主键'',
  `create_time` int(10) unsigned NOT NULL COMMENT ''创建时间'',
  `update_time` int(10) unsigned NOT NULL COMMENT ''更新时间'',
  `is_valid` tinyint(3) unsigned NOT NULL DEFAULT ''1'' COMMENT ''是否有效(0:否,1:是)'',
  `role_name` varchar(16) NOT NULL COMMENT ''角色名称'',
  `role_label` varchar(16) NOT NULL COMMENT ''角色标识'',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_label` (`role_label`),
  KEY `idx_role_name` (`role_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `sys_role` */

/*Table structure for table `sys_role_menu` */

DROP TABLE IF EXISTS `sys_role_menu`;

CREATE TABLE `sys_role_menu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT ''主键'',
  `create_time` int(10) unsigned NOT NULL COMMENT ''创建时间'',
  `update_time` int(10) unsigned NOT NULL COMMENT ''更新时间'',
  `is_valid` tinyint(3) unsigned NOT NULL DEFAULT ''1'' COMMENT ''是否有效(0:否,1:是)'',
  `role_id` int(10) unsigned NOT NULL COMMENT ''sys_role表主键'',
  `menu_id` int(10) unsigned NOT NULL COMMENT ''sys_menu表主键'',
  PRIMARY KEY (`id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `sys_role_menu` */

/*Table structure for table `sys_role_privilege` */

DROP TABLE IF EXISTS `sys_role_privilege`;

CREATE TABLE `sys_role_privilege` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT ''主键'',
  `create_time` int(10) unsigned NOT NULL COMMENT ''创建时间'',
  `update_time` int(10) unsigned NOT NULL COMMENT ''更新时间'',
  `is_valid` tinyint(3) unsigned NOT NULL DEFAULT ''1'' COMMENT ''是否有效(0:否,1:是)'',
  `role_id` int(10) unsigned NOT NULL COMMENT ''sys_role表主键'',
  `privilege_id` int(10) unsigned NOT NULL COMMENT ''sys_privilege表主键'',
  PRIMARY KEY (`id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_privilege_id` (`privilege_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `sys_role_privilege` */

/*Table structure for table `sys_schedule` */

DROP TABLE IF EXISTS `sys_schedule`;

CREATE TABLE `sys_schedule` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT ''主键'',
  `create_time` int(10) unsigned NOT NULL COMMENT ''创建时间'',
  `update_time` int(10) unsigned NOT NULL COMMENT ''更新时间'',
  `is_valid` tinyint(3) unsigned NOT NULL DEFAULT ''1'' COMMENT ''是否有效(0:否,1:是)'',
  `schedule_name` varchar(32) NOT NULL COMMENT ''任务名称'',
  `schedule_url` varchar(256) NOT NULL COMMENT ''任务地址'',
  `start_execute_time` int(10) unsigned NOT NULL COMMENT ''开始执行时间'',
  `delay_time` int(10) unsigned NOT NULL COMMENT ''间隔时间(秒)'',
  `last_execute_time` int(10) unsigned DEFAULT NULL COMMENT ''上次执行时间'',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `sys_schedule` */

/*Table structure for table `sys_user` */

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT ''主键'',
  `gid` char(36) NOT NULL COMMENT ''用户GID'',
  `create_time` int(10) unsigned NOT NULL COMMENT ''创建时间'',
  `update_time` int(10) unsigned NOT NULL COMMENT ''更新时间'',
  `is_valid` tinyint(3) unsigned NOT NULL DEFAULT ''1'' COMMENT ''是否有效(0:无效,1:有效)'',
  `user_name` varchar(32) NOT NULL COMMENT ''用户名'',
  `password_md5` char(32) NOT NULL COMMENT ''密码MD5值'',
  `phone` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT ''手机号'',
  `email` varchar(128) NOT NULL COMMENT ''邮箱'',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_gid` (`gid`),
  KEY `idx_user_name` (`user_name`),
  KEY `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `sys_user` */

/*Table structure for table `sys_user_role` */

DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT ''主键'',
  `create_time` int(10) unsigned NOT NULL COMMENT ''创建时间'',
  `update_time` int(10) unsigned NOT NULL COMMENT ''更新时间'',
  `is_valid` tinyint(3) unsigned NOT NULL DEFAULT ''1'' COMMENT ''是否有效(0:否,1:是)'',
  `user_id` int(10) unsigned NOT NULL COMMENT ''sys_user表主键'',
  `role_id` int(10) unsigned NOT NULL COMMENT ''sys_role表主键'',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `sys_user_role` */

/*Table structure for table `tb_config` */

DROP TABLE IF EXISTS `tb_config`;

CREATE TABLE `tb_config` (
  `config_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT ''配置ID'',
  `config_key` varchar(64) NOT NULL COMMENT ''配置key'',
  `config_value` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT ''配置value'',
  `config_comment` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT ''配置描述'',
  `create_time` timestamp NOT NULL COMMENT ''创建时间'',
  `update_time` timestamp NULL DEFAULT NULL COMMENT ''更新时间'',
  `is_delete` tinyint(3) unsigned NOT NULL DEFAULT ''0'' COMMENT ''是否删除(0:否,1:是)'',
  PRIMARY KEY (`config_id`),
  KEY `idx_config_key` (`config_key`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_config` */

insert  into `tb_config`(`config_id`,`config_key`,`config_value`,`config_comment`,`create_time`,`update_time`,`is_delete`) values
(1,''staticVersion'',''1'',''静态资源版本'',''2018-10-07 14:48:05'',''2018-10-07 14:48:05'',0);

/*Table structure for table `tb_dictionary` */

DROP TABLE IF EXISTS `tb_dictionary`;

CREATE TABLE `tb_dictionary` (
  `dictionary_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT ''字典表ID'',
  `dict_table_name` varchar(64) NOT NULL COMMENT ''表名'',
  `dict_column_name` varchar(64) NOT NULL COMMENT ''字段名'',
  `dictionary_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT ''字典名'',
  `dictionary_value` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT ''字典值'',
  `sequence_number` smallint(5) unsigned NOT NULL DEFAULT ''1'' COMMENT ''排序标识'',
  `parent_id` bigint(20) unsigned NOT NULL DEFAULT ''0'' COMMENT ''父级ID'',
  `is_leaf` tinyint(3) unsigned NOT NULL DEFAULT ''1'' COMMENT ''是否是叶子节点(0:否,1:是)'',
  `is_delete` tinyint(3) unsigned NOT NULL DEFAULT ''0'' COMMENT ''是否删除(0:否,1:是)'',
  PRIMARY KEY (`dictionary_id`),
  KEY `idx_order_label` (`sequence_number`),
  KEY `idx_table_column_parent` (`dict_table_name`,`dict_column_name`,`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_dictionary` */

/*Table structure for table `tb_menu` */

DROP TABLE IF EXISTS `tb_menu`;

CREATE TABLE `tb_menu` (
  `menu_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT ''菜单ID'',
  `request_url` varchar(256) DEFAULT NULL COMMENT ''请求路径'',
  `menu_name` varchar(32) NOT NULL COMMENT ''菜单名称'',
  `menu_icon` varchar(32) DEFAULT NULL COMMENT ''菜单图标'',
  `parent_id` bigint(20) unsigned NOT NULL DEFAULT ''0'' COMMENT ''父级ID'',
  `top_id` bigint(20) unsigned NOT NULL DEFAULT ''0'' COMMENT ''顶级ID'',
  `sequence_number` smallint(5) unsigned NOT NULL DEFAULT ''1'' COMMENT ''菜单排序'',
  `is_spread` tinyint(3) unsigned NOT NULL DEFAULT ''0'' COMMENT ''是否默认展开(0:否,1:是)'',
  `is_leaf` tinyint(3) unsigned NOT NULL DEFAULT ''1'' COMMENT ''是否是叶子节点(0:否,1:是)'',
  `is_delete` tinyint(3) unsigned NOT NULL DEFAULT ''0'' COMMENT ''是否删除(0:否,1:是)'',
  PRIMARY KEY (`menu_id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_menu_name` (`menu_name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_menu` */

insert  into `tb_menu`(`menu_id`,`request_url`,`menu_name`,`menu_icon`,`parent_id`,`top_id`,`sequence_number`,`is_spread`,`is_leaf`,`is_delete`) values
(1,NULL,''系统管理'',''gears'',0,0,1,1,0,0),
(2,''/dictionary/forward'',''字典管理'',''book'',1,1,1,0,1,0),
(3,''/user/forward'',''用户管理'',''user'',1,1,2,0,1,0),
(4,''/role/forward'',''角色管理'',''user-secret'',1,1,3,0,1,0),
(5,''/privilege/forward'',''权限管理'',''shield'',1,1,4,0,1,0),
(6,''/menu/forward'',''菜单管理'',''navicon'',1,1,5,0,1,0),
(7,''/config/forward'',''全局配置'',''cog'',1,1,6,0,1,0),
(8,''/reload/cache/forward'',''刷新缓存'',''refresh'',1,1,7,0,1,0),
(9,''/notice/forward'',''上线公告'',''arrow-circle-o-up'',1,1,8,0,1,0),
(10,''/schedule/forward'',''定时任务'',''tasks'',1,1,9,0,1,0);

/*Table structure for table `tb_privilege` */

DROP TABLE IF EXISTS `tb_privilege`;

CREATE TABLE `tb_privilege` (
  `privilege_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT ''权限ID'',
  `privilege_label` varchar(128) NOT NULL COMMENT ''权限标识'',
  `privilege_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT ''权限名称'',
  `parent_id` bigint(20) unsigned NOT NULL DEFAULT ''0'' COMMENT ''父级ID'',
  `top_id` bigint(20) unsigned NOT NULL DEFAULT ''0'' COMMENT ''顶级ID'',
  `is_leaf` tinyint(3) unsigned NOT NULL DEFAULT ''1'' COMMENT ''是否是最后一级(0:否,1;是)'',
  `is_delete` tinyint(3) unsigned NOT NULL DEFAULT ''0'' COMMENT ''是否删除(0:否,1:是)'',
  PRIMARY KEY (`privilege_id`),
  UNIQUE KEY `uniq_privilege_label` (`privilege_label`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_privilege_name` (`privilege_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_privilege` */

/*Table structure for table `tb_role` */

DROP TABLE IF EXISTS `tb_role`;

CREATE TABLE `tb_role` (
  `role_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT ''角色ID'',
  `role_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT ''角色名称'',
  `role_label` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT ''角色标识'',
  `is_delete` tinyint(3) unsigned NOT NULL DEFAULT ''0'' COMMENT ''是否删除(0:否,1:是)'',
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `uniq_role_label` (`role_label`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_role` */

insert  into `tb_role`(`role_id`,`role_name`,`role_label`,`is_delete`) values
(1,''管理员'',''admin'',0);

/*Table structure for table `tb_role_menu` */

DROP TABLE IF EXISTS `tb_role_menu`;

CREATE TABLE `tb_role_menu` (
  `role_menu_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT ''角色菜单ID'',
  `role_id` bigint(20) unsigned NOT NULL DEFAULT ''0'' COMMENT ''角色ID'',
  `menu_id` bigint(20) unsigned NOT NULL DEFAULT ''0'' COMMENT ''菜单ID'',
  `is_delete` tinyint(3) unsigned NOT NULL DEFAULT ''0'' COMMENT ''是否删除(0:否,1;是)'',
  PRIMARY KEY (`role_menu_id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_role_menu` */

/*Table structure for table `tb_role_privilege` */

DROP TABLE IF EXISTS `tb_role_privilege`;

CREATE TABLE `tb_role_privilege` (
  `role_privilege_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT ''角色权限ID'',
  `role_id` bigint(20) unsigned NOT NULL COMMENT ''角色ID'',
  `privilege_id` bigint(20) unsigned NOT NULL COMMENT ''权限ID'',
  `is_delete` tinyint(3) unsigned NOT NULL DEFAULT ''0'' COMMENT ''是否删除(0:否,1:是)'',
  PRIMARY KEY (`role_privilege_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_role_privilege` */

/*Table structure for table `tb_schedule` */

DROP TABLE IF EXISTS `tb_schedule`;

CREATE TABLE `tb_schedule` (
  `schedule_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT ''任务ID'',
  `schedule_name` varchar(32) NOT NULL COMMENT ''任务名称'',
  `schedule_url` varchar(256) NOT NULL COMMENT ''任务请求url'',
  `start_execute_time` int(10) unsigned NOT NULL COMMENT ''开始执行时间'',
  `delay_time` bigint(20) unsigned NOT NULL COMMENT ''间隔时间长度(秒)'',
  `last_execute_time` int(10) unsigned DEFAULT NULL COMMENT ''上次执行时间'',
  `update_time` int(10) unsigned NOT NULL COMMENT ''修改时间'',
  `is_delete` tinyint(3) unsigned NOT NULL DEFAULT ''0'' COMMENT ''是否关闭(0:否,1:是)'',
  PRIMARY KEY (`schedule_id`),
  UNIQUE KEY `uniq_schedule_url` (`schedule_url`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_schedule` */

insert  into `tb_schedule`(`schedule_id`,`schedule_name`,`schedule_url`,`start_execute_time`,`delay_time`,`last_execute_time`,`update_time`,`is_delete`) values
(1,''1'',''1'',1546963200,11,NULL,1544349063,1);

/*Table structure for table `tb_upload_file` */

DROP TABLE IF EXISTS `tb_upload_file`;

CREATE TABLE `tb_upload_file` (
  `upload_file_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT ''文件ID'',
  `file_extension` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT ''文件扩展名'',
  `file_path` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT ''文件相对路径'',
  `file_size` bigint(20) unsigned NOT NULL DEFAULT ''0'' COMMENT ''文件大小(字节)'',
  `upload_time` timestamp NOT NULL COMMENT ''上传日期'',
  `file_md5` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT ''文件MD5值'',
  `image_width` int(10) unsigned NOT NULL DEFAULT ''0'' COMMENT ''图片文件宽度'',
  `image_height` int(10) unsigned NOT NULL DEFAULT ''0'' COMMENT ''图片文件高度'',
  PRIMARY KEY (`upload_file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_upload_file` */

/*Table structure for table `tb_upload_relation` */

DROP TABLE IF EXISTS `tb_upload_relation`;

CREATE TABLE `tb_upload_relation` (
  `upload_relation_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT ''关系ID'',
  `upload_file_id` bigint(20) unsigned NOT NULL DEFAULT ''0'' COMMENT ''文件ID'',
  `record_id` bigint(20) unsigned NOT NULL DEFAULT ''0'' COMMENT ''记录ID'',
  `record_table_name` varchar(64) NOT NULL COMMENT ''记录表名'',
  `record_label` varchar(32) DEFAULT NULL COMMENT ''记录标记'',
  `sequence_number` smallint(5) unsigned NOT NULL DEFAULT ''1'' COMMENT ''展示顺序'',
  `is_delete` tinyint(3) unsigned NOT NULL DEFAULT ''0'' COMMENT ''是否删除(0:否,1:是)'',
  PRIMARY KEY (`upload_relation_id`),
  KEY `idx_id_name_label` (`record_id`,`record_table_name`,`record_label`),
  KEY `idx_sequence_number` (`sequence_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_upload_relation` */

/*Table structure for table `tb_user` */

DROP TABLE IF EXISTS `tb_user`;

CREATE TABLE `tb_user` (
  `user_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT ''用户ID'',
  `user_name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT ''用户名'',
  `password_md5` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT ''密码MD5值'',
  `cellphone` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT ''手机号'',
  `create_time` int(10) unsigned NOT NULL COMMENT ''创建时间'',
  `email` varchar(128) DEFAULT NULL COMMENT ''邮箱'',
  `is_delete` tinyint(3) unsigned NOT NULL DEFAULT ''0'' COMMENT ''是否删除(0:否,1:是)'',
  PRIMARY KEY (`user_id`),
  KEY `idx_cellphone` (`cellphone`),
  KEY `uniq_user_name` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_user` */

insert  into `tb_user`(`user_id`,`user_name`,`password_md5`,`cellphone`,`create_time`,`email`,`is_delete`) values
(1,''admin'',''e10adc3949ba59abbe56e057f20f883e'',''13111111111'',1544264477,''zhangyu13393@sina.com'',0);

/*Table structure for table `tb_user_role` */

DROP TABLE IF EXISTS `tb_user_role`;

CREATE TABLE `tb_user_role` (
  `user_role_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT ''用户角色ID'',
  `user_id` bigint(20) unsigned NOT NULL DEFAULT ''0'' COMMENT ''用户ID'',
  `role_id` bigint(20) unsigned NOT NULL DEFAULT ''0'' COMMENT ''角色ID'',
  `is_delete` tinyint(3) unsigned NOT NULL DEFAULT ''0'' COMMENT ''是否删除(0:否,1:是)'',
  PRIMARY KEY (`user_role_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_user_role` */

insert  into `tb_user_role`(`user_role_id`,`user_id`,`role_id`,`is_delete`) values
(1,1,1,0);

/*Table structure for table `test` */

DROP TABLE IF EXISTS `test`;

CREATE TABLE `test` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT ''主键'',
  `gid` char(36) NOT NULL COMMENT ''测试GID'',
  `create_time` int(10) unsigned NOT NULL COMMENT ''创建时间'',
  `update_time` int(10) unsigned NOT NULL COMMENT ''更新时间'',
  `is_valid` tinyint(3) unsigned NOT NULL DEFAULT ''1'' COMMENT ''是否有效(0:否,1:是)'',
  `money` decimal(10,2) unsigned DEFAULT NULL COMMENT ''金额'',
  `remark` varchar(256) DEFAULT NULL COMMENT ''备注'',
  `test_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT ''测试名称'',
  `test_dictionary` int(11) NOT NULL DEFAULT ''0'' COMMENT ''测试字典(0:字典0,1:字典1,2:字典2)'',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_gid` (`gid`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `test` */

insert  into `test`(`id`,`gid`,`create_time`,`update_time`,`is_valid`,`money`,`remark`,`test_name`,`test_dictionary`) values
(1,''183fe16d-e34b-46ed-93d7-60edfd531a8b'',1566314651,1569141495,1,10.00,''updateByIdForNotNullColumn remark'',''insertOne testName'',2),
(2,''2277b0c5-b810-461d-9324-52d772aa3e79'',1566314652,1566314652,1,10.00,''updateByGidForNotNullColumn remark'',''insertOneReturnId testName'',1),
(3,''2795e63f-3c9f-47bf-9f81-72f98350f1be'',1566314688,1566314688,1,1.00,''updateByIdForAllColumn remark'',''updateByIdForAllColumn testName'',1),
(4,''30f0adac-5835-44b6-a8cd-f97e646fca68'',1566314688,1566314688,1,1.00,''updateByGidForAllColumn remark'',''updateByGidForAllColumn testName'',1),
(5,''b9d3f75e-25bf-4c1e-a031-8d883869892c'',1566318652,1566314652,1,10.00,''updateByIdList remark'',''batchInsertTable2'',2),
(6,''9768621c-83be-48fe-97f0-85ca4a0d1ec8'',1566320652,1566314652,1,10.00,''updateByIdList remark'',''batchInsertTable3'',2),
(13,''463a4af1-269b-4ffa-b8b9-79dd5f04ad07'',1566314688,1566314688,1,0.00,''updateByParamInsertWhenNotExist remark'',''updateByParamInsertWhenNotExist testName'',2),
(14,''3fce4feb-ac8b-489b-b5db-9eaaa0c1628a'',1567436495,1567436568,1,334455.00,''3344gg'',''334455'',0),
(15,''68c46e89-c861-4e34-af0a-4ef2498ba596'',1567523287,1567827201,1,33.00,''任天堂'',''22'',0),
(16,''4d0b52a3-6a17-4345-8e3e-2739942a9e0a'',1567830308,1567830308,1,11.00,''11'',''11'',0),
(17,''f3d67cc6-3e24-443e-8b44-1175cf9158c4'',1567830317,1567830317,1,233.00,''333'',''333'',0),
(18,''5b97bb0f-6dad-472e-8802-ad650314fd92'',1567914882,1567914910,1,333.00,''ddd'',''ddd'',0);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

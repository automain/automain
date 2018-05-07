/*
SQLyog Trial
MySQL - 8.0.11 : Database - automain
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

/*Table structure for table `db_slow_log` */

DROP TABLE IF EXISTS `db_slow_log`;

CREATE TABLE `db_slow_log` (
  `slow_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `pool_name` varchar(64) NOT NULL COMMENT '连接池名称',
  `slow_db` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '慢查询库',
  `slow_time` int(10) unsigned NOT NULL COMMENT '慢查询用时',
  `slow_state` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '慢查询状态',
  `slow_sql` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '慢查询sql',
  PRIMARY KEY (`slow_id`),
  KEY `idx_create_time_time` (`create_time`,`slow_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `db_slow_log` */

/*Table structure for table `db_status` */

DROP TABLE IF EXISTS `db_status`;

CREATE TABLE `db_status` (
  `status_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `pool_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '连接池名称',
  `com_select` bigint(20) unsigned NOT NULL COMMENT '查询数',
  `com_insert` bigint(20) unsigned NOT NULL COMMENT '添加数',
  `com_delete` bigint(20) unsigned NOT NULL COMMENT '删除数',
  `com_update` bigint(20) unsigned NOT NULL COMMENT '编辑数',
  `com_commit` bigint(20) unsigned NOT NULL COMMENT '提交数',
  `com_rollback` bigint(20) unsigned NOT NULL COMMENT '回滚数',
  `threads_free` bigint(20) unsigned NOT NULL COMMENT '空闲线程数',
  `threads_running` bigint(20) unsigned NOT NULL COMMENT '运行中线程数',
  `pages_data` bigint(20) unsigned NOT NULL COMMENT '已用页大小',
  `pages_free` bigint(20) unsigned NOT NULL COMMENT '空闲页大小',
  `pages_misc` bigint(20) unsigned NOT NULL COMMENT '忙碌页大小',
  PRIMARY KEY (`status_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

/*Data for the table `db_status` */

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_config` */

insert  into `tb_config`(`config_id`,`config_key`,`config_value`,`config_comment`,`create_time`,`update_time`,`is_delete`) values
(1,'staticVersion','0','静态资源版本','2018-04-22 18:59:14','2018-04-22 18:59:17',0);

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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_menu` */

insert  into `tb_menu`(`menu_id`,`request_url`,`menu_name`,`menu_icon`,`parent_id`,`top_id`,`sequence_number`,`is_spread`,`is_leaf`,`is_delete`) values
(1,NULL,'系统管理','gears',0,0,1,0,0,0),
(2,'/request/forward','请求路径映射','random',1,0,1,0,1,0),
(3,'/dictionary/forward','字典管理','book',1,0,2,0,1,0),
(4,'/user/forward','用户管理','user',1,0,3,0,1,0),
(5,'/role/forward','角色管理','user-secret',1,0,4,0,1,0),
(6,'/menu/forward','菜单管理','navicon',1,0,5,0,1,0),
(7,'/config/forward','全局配置','cog',1,1,6,0,1,0),
(8,'/reload/cache/forward','刷新缓存','refresh',1,0,7,0,1,0),
(9,'/notice/forward','上线公告','arrow-circle-o-up',1,0,8,0,1,0),
(10,'/monitor/slowlog/forward','数据库慢查询','database',1,1,9,0,1,0);

/*Table structure for table `tb_request_mapping` */

DROP TABLE IF EXISTS `tb_request_mapping`;

CREATE TABLE `tb_request_mapping` (
  `request_mapping_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '请求映射ID',
  `request_url` varchar(256) NOT NULL COMMENT '请求相对路径',
  `operation_class` varchar(256) NOT NULL COMMENT '请求处理类的全路径',
  `url_comment` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '注释',
  PRIMARY KEY (`request_mapping_id`),
  UNIQUE KEY `uniq_request_url` (`request_url`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_request_mapping` */

insert  into `tb_request_mapping`(`request_mapping_id`,`request_url`,`operation_class`,`url_comment`) values
(1,'/dictionary/forward','com.github.automain.common.view.DictionaryForwardExecutor','字典跳转'),
(2,'/dictionary/list','com.github.automain.common.view.DictionaryListExecutor','字典列表'),
(3,'/dictionary/add','com.github.automain.common.action.DictionaryAddExecutor','字典添加'),
(4,'/dictionary/update','com.github.automain.common.action.DictionaryUpdateExecutor','字典编辑'),
(5,'/dictionary/delete','com.github.automain.common.action.DictionaryDeleteExecutor','字典删除'),
(6,'/dictionary/column/list','com.github.automain.common.action.ColumnListExecutor','字典查询获取字段名'),
(7,'/config/forward','com.github.automain.common.view.ConfigForwardExecutor','配置跳转'),
(8,'/config/list','com.github.automain.common.view.ConfigListExecutor','配置列表'),
(9,'/config/add','com.github.automain.common.action.ConfigAddExecutor','配置添加'),
(10,'/config/update','com.github.automain.common.action.ConfigUpdateExecutor','配置编辑'),
(11,'/config/delete','com.github.automain.common.action.ConfigDeleteExecutor','配置删除'),
(12,'/reload/cache/forward','com.github.automain.common.view.ForwardReloadCacheExecutor','清除缓存跳转'),
(13,'/reload/cache','com.github.automain.common.action.ReloadCacheExecutor','重新加载缓存'),
(14,'/upload/forward','com.github.automain.common.view.UploadForwardExecutor','上传文件跳转'),
(15,'/notice/forward','com.github.automain.common.view.NoticeForwardExecutor','公告跳转'),
(16,'/notice/add','com.github.automain.common.action.NoticeAddExecutor','公告添加'),
(17,'/notice/delete','com.github.automain.common.action.NoticeDeleteExecutor','公告清除'),
(18,'/request/forward','com.github.automain.user.view.RequestForwardExecutor','请求路径映射跳转'),
(19,'/request/list','com.github.automain.user.view.RequestListExecutor','请求路径列表'),
(20,'/request/add','com.github.automain.user.action.RequestAddExecutor','请求路径添加'),
(21,'/request/update','com.github.automain.user.action.RequestUpdateExecutor','请求路径编辑'),
(22,'/request/role/list','com.github.automain.user.view.RequestRoleListExecutor','权限角色列表'),
(23,'/user/captcha','com.github.automain.user.action.CaptchaExecutor','获取验证码图片'),
(24,'/user/login/action','com.github.automain.user.action.LoginActionExecutor','用户登录'),
(25,'/user/logout/action','com.github.automain.user.action.LogoutActionExecutor','用户退出登录'),
(26,'/user/frame','com.github.automain.user.view.FrameExecutor','用户主框架'),
(27,'/user/forward','com.github.automain.user.view.UserForwardExecutor','用户跳转'),
(28,'/user/list','com.github.automain.user.view.UserListExecutor','用户列表'),
(29,'/user/add','com.github.automain.user.action.UserAddExecutor','用户添加'),
(30,'/user/update','com.github.automain.user.action.UserUpdateExecutor','用户编辑'),
(31,'/user/delete','com.github.automain.user.action.UserDeleteExecutor','用户删除'),
(32,'/user/check/exist','com.github.automain.user.action.CheckUserExistExecutor','检查用户名重复'),
(33,'/user/update/pwd','com.github.automain.user.action.UserUpdatePwdExecutor','用户更新密码'),
(34,'/user/reset/pwd','com.github.automain.user.action.ResetPwdExecutor','用户重置密码'),
(35,'/user/role/list','com.github.automain.user.view.UserRoleListExecutor','用户角色列表'),
(36,'/user/grant/role','com.github.automain.user.action.UserGrantRoleExecutor','用户授权'),
(37,'/menu/forward','com.github.automain.user.view.MenuForwardExecutor','菜单跳转'),
(38,'/menu/list','com.github.automain.user.view.MenuListExecutor','菜单列表'),
(39,'/menu/add','com.github.automain.user.action.MenuAddExecutor','菜单添加'),
(40,'/menu/update','com.github.automain.user.action.MenuUpdateExecutor','菜单编辑'),
(41,'/menu/delete','com.github.automain.user.action.MenuDeleteExecutor','菜单删除'),
(42,'/menu/role/list','com.github.automain.user.view.MenuRoleListExecutor','菜单角色列表'),
(43,'/menu/grant/role','com.github.automain.user.action.MenuGrantRoleExecutor','菜单授权'),
(44,'/menu/revoke/role','com.github.automain.user.action.MenuRevokeRoleExecutor','菜单取消分配角色'),
(45,'/role/forward','com.github.automain.user.view.RoleForwardExecutor','角色跳转'),
(46,'/role/list','com.github.automain.user.view.RoleListExecutor','角色列表'),
(47,'/role/add','com.github.automain.user.action.RoleAddExecutor','角色添加'),
(48,'/role/update','com.github.automain.user.action.RoleUpdateExecutor','角色编辑'),
(49,'/role/delete','com.github.automain.user.action.RoleDeleteExecutor','角色删除'),
(50,'/role/grant/menu','com.github.automain.user.action.RoleGrantMenuExecutor','角色分配菜单'),
(51,'/role/grant/user','com.github.automain.user.action.RoleGrantUserExecutor','角色分配用户'),
(52,'/role/grant/request','com.github.automain.user.action.RoleGrantRequestExecutor','角色分配权限'),
(53,'/role/user/list','com.github.automain.user.view.RoleUserListExecutor','角色用户列表'),
(54,'/role/request/list','com.github.automain.user.view.RoleRequestListExecutor','角色权限列表'),
(55,'/role/revoke/user','com.github.automain.user.action.RoleRevokeUserExecutor','角色取消分配用户'),
(56,'/role/revoke/request','com.github.automain.user.action.RoleRevokeRequestExecutor','角色取消分配权限'),
(57,'/schedule/dbstatus','com.github.automain.schedule.DBStatusSchedule','数据库状态任务'),
(58,'/schedule/slowlog','com.github.automain.schedule.SlowLogSchedule','数据库慢查询任务'),
(59,'/monitor/slowlog/forward','com.github.automain.monitor.view.SlowLogForwardExecutor','数据库慢查询跳转'),
(60,'/monitor/slowlog/list','com.github.automain.monitor.view.SlowLogListExecutor','数据库慢查询列表');

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
  `request_mapping_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '请求路径ID',
  `is_delete` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除(0:否,1;是)',
  PRIMARY KEY (`role_request_mapping_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_role_request_mapping` */

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

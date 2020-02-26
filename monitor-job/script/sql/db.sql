CREATE TABLE `app_owner` (
	`owner_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '负责人ID',
	`app_id` VARCHAR(32) NOT NULL COMMENT '应用ID',
	`owner_name` VARCHAR(32) NOT NULL COMMENT '负责人姓名',
	`owner_mobile` VARCHAR(32) NOT NULL DEFAULT '' COMMENT '负责人电话',
	`is_deleted` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '是否删除: 0=否,1=是',
	`create_time` DATETIME NOT NULL COMMENT '创建时间',
	`update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`owner_id`),
	INDEX `app_id` (`app_id`)
)
COMMENT='app负责人表'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=27
;

CREATE TABLE `app_white_list` (
	`white_api_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '白名单ID',
	`app_id` VARCHAR(32) NOT NULL COMMENT '应用ID',
	`api` VARCHAR(128) NOT NULL COMMENT '接口',
	`is_deleted` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '是否删除: 0=否,1=是',
	`create_time` DATETIME NOT NULL COMMENT '创建时间',
	`update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`white_api_id`),
	INDEX `app_id` (`app_id`)
)
COMMENT='白名单，不管接口慢成傻德行，都不管'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;
CREATE TABLE `monitor_config` (
	`monitor_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '监控配置ID',
	`app_id` VARCHAR(32) NOT NULL COMMENT '应用ID',
	`line95` DECIMAL(10,2) UNSIGNED NOT NULL COMMENT '95line标准',
	`api_type` VARCHAR(32) NOT NULL COMMENT '接口类型。URL=http接口；DUBBO=dubbo接口',
	`ding_robot_token` VARCHAR(64) NOT NULL COMMENT '钉钉机器人token',
	`ding_robot_secret` VARCHAR(128) NOT NULL COMMENT '钉钉机器人secret',
	`is_deleted` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '是否删除: 0=否,1=是',
	`create_time` DATETIME NOT NULL COMMENT '创建时间',
	`update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`monitor_id`)
)
COMMENT='监控配置表'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;
CREATE TABLE `monitor_robot_report_attach` (
	`attach_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '附件ID',
	`ding_robot_token` VARCHAR(32) NOT NULL COMMENT '钉钉机器人token',
	`attach_url` VARCHAR(128) NOT NULL COMMENT '附件链接',
	`date_begin` DATE NOT NULL COMMENT '开始时间',
	`date_end` DATE NOT NULL COMMENT '开始时间',
	`is_default` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '是否是默认：0=否；1=是.最近一周的接口数据为默认',
	`is_deleted` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '是否删除: 0=否,1=是',
	`create_time` DATETIME NOT NULL COMMENT '创建时间',
	`update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`attach_id`)
)
COMMENT='接口附件地址'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

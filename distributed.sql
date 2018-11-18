/*
 Navicat Premium Data Transfer

 Source Server         : 118.190.216.3
 Source Server Type    : MySQL
 Source Server Version : 50641
 Source Host           : 118.190.216.3
 Source Database       : distributedlock

 Target Server Type    : MySQL
 Target Server Version : 50641
 File Encoding         : utf-8

 Date: 11/16/2018 13:41:37 PM
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `seckill`
-- ----------------------------
DROP TABLE IF EXISTS `seckill`;
CREATE TABLE `seckill` (
  `seckill_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
  `name` varchar(120) NOT NULL COMMENT '商品名称',
  `number` int(11) NOT NULL COMMENT '库存数量',
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '秒杀开启时间',
  `end_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '秒杀结束时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `version` int(11) NOT NULL COMMENT '版本号',
  PRIMARY KEY (`seckill_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1004 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';

-- ----------------------------
--  Records of `seckill`
-- ----------------------------
BEGIN;
INSERT INTO `seckill` VALUES ('1000', '1000元秒杀iphone8', '100', '2018-11-20 00:00:00', '2018-11-20 00:01:00', '2018-11-16 09:15:00', '0'), ('1001', '500元秒杀ipad2', '100', '2018-11-20 00:00:00', '2018-11-20 00:01:00', '2018-11-16 09:15:00', '0'), ('1002', '300元秒杀小米4', '100', '2018-11-20 00:00:00', '2018-11-20 00:00:00', '2018-11-16 09:15:00', '0'), ('1003', '200元秒杀红米note', '100', '2018-11-20 00:00:00', '2018-11-20 00:00:00', '2018-11-16 09:15:00', '0');
COMMIT;

-- ----------------------------
--  Table structure for `success_killed`
-- ----------------------------
DROP TABLE IF EXISTS `success_killed`;
CREATE TABLE `success_killed` (
  `seckill_id` bigint(20) NOT NULL COMMENT '秒杀商品id',
  `user_id` bigint(20) NOT NULL COMMENT '用户Id',
  `state` tinyint(4) NOT NULL COMMENT '状态标示：-1指无效，0指成功，1指已付款',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`seckill_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';

SET FOREIGN_KEY_CHECKS = 1;

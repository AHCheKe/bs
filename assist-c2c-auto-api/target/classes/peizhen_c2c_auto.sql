/*
 Navicat Premium Data Transfer

 Source Server         : 1
 Source Server Type    : MySQL
 Source Server Version : 80023
 Source Host           : localhost:3306
 Source Schema         : peizhen_c2c_auto

 Target Server Type    : MySQL
 Target Server Version : 80023
 File Encoding         : 65001

 Date: 02/04/2025 16:20:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `admin_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '物理ID',
  `mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `pwd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `real_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '姓名',
  `last_login` datetime(0) NULL DEFAULT NULL COMMENT '上次登录时间',
  `gender` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '性别',
  `status` int(0) NULL DEFAULT NULL COMMENT '状态：0-待审核；1-审核通过；2：冻结；-1：拒绝申请',
  PRIMARY KEY (`admin_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, '18268812145', '123456', '轲', '2025-03-31 10:09:33', '女', 1);

-- ----------------------------
-- Table structure for certify
-- ----------------------------
DROP TABLE IF EXISTS `certify`;
CREATE TABLE `certify`  (
  `certify_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户ID',
  `app_time` datetime(0) NULL DEFAULT NULL COMMENT '申请时间',
  `app_content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '申请理由',
  `attachment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '证明材料附件',
  `certify_status` int(0) NULL DEFAULT NULL COMMENT '申请状态：0待审核；1已通过；2申请失败',
  `certify_time` datetime(0) NULL DEFAULT NULL COMMENT '审核时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`certify_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of certify
-- ----------------------------
INSERT INTO `certify` VALUES (2, 20, '2024-11-03 22:14:42', '执业药师，业余兼职陪诊师，有一定的医疗素养，可以为病人提供更多有价值的帮助。', 'http://129.211.222.131:18080/images/20241103/221429intkp.jpg', 1, NULL, NULL);
INSERT INTO `certify` VALUES (3, 21, '2024-11-07 22:39:39', '执业药师，业余兼职陪诊师，有一定的医疗素养，可以为病人提供更多有价值的帮助。', 'http://129.211.222.131:18080/images/20241103/221429intkp.jpg', 1, '2025-02-05 22:33:25', NULL);
INSERT INTO `certify` VALUES (4, 22, '2024-11-07 22:40:24', '执业药师，业余兼职陪诊师，有一定的医疗素养，可以为病人提供更多有价值的帮助。', 'http://129.211.222.131:18080/images/20241103/221429intkp.jpg', 1, NULL, NULL);
INSERT INTO `certify` VALUES (5, 23, '2024-11-07 22:40:37', '执业药师，业余兼职陪诊师，有一定的医疗素养，可以为病人提供更多有价值的帮助。', 'http://129.211.222.131:18080/images/20241103/221429intkp.jpg', 1, NULL, NULL);
INSERT INTO `certify` VALUES (6, 24, '2025-02-06 09:16:29', '药学院毕业生，有较好的医学常识与素养，熟悉医院的就诊流程。', 'http://129.211.222.131:18080/images/20241103/221429intkp.jpg', 1, NULL, NULL);
INSERT INTO `certify` VALUES (7, 25, '2025-02-06 09:16:10', '一个有耐心的人', 'http://129.211.222.131:18080/images/20241103/221429intkp.jpg', 1, NULL, NULL);
INSERT INTO `certify` VALUES (8, 26, '2025-02-06 09:16:08', '一个有热情的小伙子', 'http://129.211.222.131:18080/images/20241103/221429intkp.jpg', 1, NULL, NULL);
INSERT INTO `certify` VALUES (9, 27, '2025-02-06 09:16:27', '一个有热情的小伙子', 'http://129.211.222.131:18080/images/20241103/221429intkp.jpg', 1, NULL, NULL);
INSERT INTO `certify` VALUES (10, 28, '2025-02-26 09:38:17', '一个好人', 'http://129.211.222.131:18080/images/20241103/221429intkp.jpg', 2, NULL, NULL);
INSERT INTO `certify` VALUES (11, 29, '2025-02-05 22:28:46', '一个有热情的小伙子', 'http://129.211.222.131:18080/images/20241103/221429intkp.jpg', 1, '2025-02-05 22:33:43', NULL);
INSERT INTO `certify` VALUES (12, 30, '2025-02-06 09:16:46', '一个有热情的小伙子', 'http://129.211.222.131:18080/images/20241103/221429intkp.jpg', 1, NULL, NULL);
INSERT INTO `certify` VALUES (13, 31, '2025-02-05 22:30:46', '一个有热情的小伙子', 'http://129.211.222.131:18080/images/20241103/221429intkp.jpg', 1, '2025-02-05 22:33:59', NULL);
INSERT INTO `certify` VALUES (14, 32, '2025-02-05 22:31:17', '一个有热情的小伙子', 'http://129.211.222.131:18080/images/20241103/221429intkp.jpg', 2, '2025-02-05 22:33:56', NULL);
INSERT INTO `certify` VALUES (15, 33, '2025-02-05 15:59:34', '一个有热情的小伙子', 'http://129.211.222.131:18080/images/20241103/221429intkp.jpg', 1, NULL, NULL);
INSERT INTO `certify` VALUES (16, 34, '2025-02-06 09:16:43', '一个有热情的小伙子', 'http://129.211.222.131:18080/images/20241103/221429intkp.jpg', 1, NULL, NULL);
INSERT INTO `certify` VALUES (17, 35, '2025-02-06 09:16:31', '一个有热情的小伙子', 'http://129.211.222.131:18080/images/20241103/221429intkp.jpg', 1, NULL, NULL);
INSERT INTO `certify` VALUES (18, 36, '2025-02-05 22:35:37', '一个有热情的小伙子', 'http://129.211.222.131:18080/images/20241103/221429intkp.jpg', 1, '2025-02-05 22:33:53', NULL);
INSERT INTO `certify` VALUES (19, 37, '2025-02-06 09:16:41', '一个有热情的小伙子', 'http://129.211.222.131:18080/images/20241103/221429intkp.jpg', 1, NULL, NULL);
INSERT INTO `certify` VALUES (20, 38, '2025-02-06 09:16:33', '一个有热情的小伙子', 'http://129.211.222.131:18080/images/20241103/221429intkp.jpg', 1, NULL, NULL);
INSERT INTO `certify` VALUES (21, 39, '2025-02-06 09:16:50', '一个有热情的小伙子', 'http://129.211.222.131:18080/images/20241103/221429intkp.jpg', 1, NULL, NULL);
INSERT INTO `certify` VALUES (22, 40, '2025-02-06 09:16:23', '一个有热情的小伙子', 'http://129.211.222.131:18080/images/20241103/221429intkp.jpg', 1, NULL, NULL);

-- ----------------------------
-- Table structure for config
-- ----------------------------
DROP TABLE IF EXISTS `config`;
CREATE TABLE `config`  (
  `config_id` int(0) NOT NULL AUTO_INCREMENT,
  `conf_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `last_update` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config
-- ----------------------------
INSERT INTO `config` VALUES (1, 'wx_user_openid', 'oufmM6wZMYvPwvdDQzwF7HhKrlH8', '小程序用户的openid', '2025-01-14 17:35:22');
INSERT INTO `config` VALUES (3, 'run_model', 'prouduct', '运行模式', '2024-12-02 22:41:33');

-- ----------------------------
-- Table structure for feedback
-- ----------------------------
DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback`  (
  `feedback_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` int(0) NULL DEFAULT NULL COMMENT '订单ID',
  `service_id` int(0) NULL DEFAULT NULL COMMENT '服务ID',
  `feedback_time` datetime(0) NULL DEFAULT NULL COMMENT '评价时间',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '客户ID',
  `assistant_id` int(0) NULL DEFAULT NULL COMMENT '陪诊师ID',
  `rate` int(0) NULL DEFAULT NULL COMMENT '评分',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '评价内容',
  PRIMARY KEY (`feedback_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of feedback
-- ----------------------------
INSERT INTO `feedback` VALUES (4, 29, 71, '2025-02-28 21:55:05', 8, 23, 5, '很好很好');
INSERT INTO `feedback` VALUES (5, 34, 94, '2025-03-11 22:18:49', 8, 22, 5, '不错不错');
INSERT INTO `feedback` VALUES (6, 35, 95, '2025-03-12 09:31:00', 8, 21, 3, '我觉得还行');

-- ----------------------------
-- Table structure for hospital
-- ----------------------------
DROP TABLE IF EXISTS `hospital`;
CREATE TABLE `hospital`  (
  `hospital_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `hospital_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '医院名称',
  `hospital_tags` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签',
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所在城市',
  `area` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所在区县',
  `main_pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '主图',
  `notice1` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '服务须知',
  `notice2` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '注意事项',
  `tel` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地址',
  `detail` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '详情',
  PRIMARY KEY (`hospital_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of hospital
-- ----------------------------
INSERT INTO `hospital` VALUES (1, '杭州市第一人民医院', '三级甲等综合医院', '杭州市', '上城区', 'http://129.211.222.131:18080/images/20250107/142617dhgim.jpg', '1、初诊、复诊（看结果）、咨询、开药等均须挂号，采取实名制挂号，同时购买《门诊医疗手册》，医保患者注意出示医保卡，挂号为当日当次有效。\n\n挂号地点(一部）：普通门诊、专家门诊在一楼门诊大厅，眼科门诊独立挂号；二部、三部挂号地点都在二楼。每次复诊时应带《门诊医疗手册》（全市通用）、我院或外院的检查结果，以供医生参考，减少不必要的检查。\n\n2、挂号时间：\n\n上午：07:15~11:30', '1、取号时间为当日上午9:30前，下午14:30前。逾时视为自动放弃预约挂号。\n2、您须在预约时间内到一楼、二楼各挂号窗口取号，取号时必须出示与预约时相符的有效证件。\n3、如因事不能如期前来就诊，请您提前一天通知预约接待处。三次无故放弃预约的患者视为违约，您将不再有下次预约机会。\n4、预约时请您留下准确的联系方式，以便专家出诊时间有变，及时与您沟通。', '0411-3829143', '浣纱路261号', '创建于1923年，是杭州市政府及爱国人士自筹资金建立的首家综合性医院。医院年均门诊量超过200万人次，开放床位1200张。');
INSERT INTO `hospital` VALUES (2, '浙江大学医学院附属第一医院', '三级甲等综合医院', '杭州市', '上城区', 'http://129.211.222.131:18080/images/20250110/092342e9s76.jpg', '中心位于杭州市区体育新城规划一号路1号、3号，交通便利，距离后盐高速口5公里，大连北站4公里，与市体育中心隔路相望。乘坐518路、907路公交车在妇女儿童医疗中心站直达。停车场可充分满足停车需求。', '中心拥有一大批先进的医疗仪器设备：国内先进的核磁共振(MRI) 、西门子64层螺旋CT、西门子Ysio数字X光机、西门子乳腺钼靶机、双人用体外授精工作站、胚胎发育24小时监测的活细胞成像系统、遗传分析仪', '0411-66861111', '城站路58号', '该医院创建于1947年，是浙江大学创建的首家附属医院，秉持“严谨求实 卓越创新 国际一流”的发展理念，以综合实力雄厚、医疗质量过硬、学科特色鲜明享誉海内外');
INSERT INTO `hospital` VALUES (3, '浙江大学医学院附属邵逸夫医院', '公立综合性三级甲等医院', '杭州市', '上城区', 'http://129.211.222.131:18080/images/20250116/223222azfp0.jpg', '预约挂号：为减少您的等待时间，建议通过医院官方网站、微信公众号、电话热线或自助挂号机提前预约挂号。请按照预约时间准时到达，过时需重新排队或另约。\n携带证件：就诊时请务必携带有效身份证件（如身份证、医保卡、社保卡等），以便办理挂号、结算及医保报销手续。\n个人防护：疫情期间，请您全程佩戴口罩，保持社交距离，配合体温检测，并主动出示健康码或行程码。如有发热、咳嗽等症状，请直接前往发热门诊就诊。', '费用结算：支持现金、银行卡、支付宝、微信等多种支付方式。医保患者请携带医保卡，以便直接结算报销部分费用。\n保持安静：医院是公共场所，请保持安静，尊重其他患者，避免大声喧哗，共同维护良好的就医环境。\n特殊需求：如有行动不便、听力视力障碍等特殊需求，请及时告知工作人员，我们将尽力提供必要的帮助。\n意见反馈：您的满意是我们工作的动力。就诊结束后，欢迎通过医院意见箱、官方网站或客服热线对我们的服务提出宝贵意见。', '0411-84625234', '庆春东路3号', '邵逸夫医院由香港知名实业家邵逸夫爵士捐资并与浙江省人民政府配套建设，集医疗、教学和科研为一体。');
INSERT INTO `hospital` VALUES (4, '浙江省人民医院', '大型综合性三级甲等医院', '杭州市', '下城区', 'http://129.211.222.131:18080/images/20250116/223537f3q4w.jpg', '预约挂号：为减少您的等待时间，建议通过医院官方网站、微信公众号、电话热线或自助挂号机提前预约挂号。请按照预约时间准时到达，过时需重新排队或另约。\n携带证件：就诊时请务必携带有效身份证件（如身份证、医保卡、社保卡等），以便办理挂号、结算及医保报销手续。\n个人防护：疫情期间，请您全程佩戴口罩，保持社交距离，配合体温检测，并主动出示健康码或行程码。如有发热、咳嗽等症状，请直接前往发热门诊就诊。', '费用结算：支持现金、银行卡、支付宝、微信等多种支付方式。医保患者请携带医保卡，以便直接结算报销部分费用。\n保持安静：医院是公共场所，请保持安静，尊重其他患者，避免大声喧哗，共同维护良好的就医环境。\n特殊需求：如有行动不便、听力视力障碍等特殊需求，请及时告知工作人员，我们将尽力提供必要的帮助。\n意见反馈：您的满意是我们工作的动力。就诊结束后，欢迎通过医院意见箱、官方网站或客服热线对我们的服务提出宝贵意见。', '0411-39062999', '上塘路158号', '浙江省人民医院成立于1984年，是浙江省卫生健康委直属的大型综合性三级甲等医院，学科齐全、设备先进、技术雄厚，享有很高的省内乃至全国声誉');
INSERT INTO `hospital` VALUES (5, '杭州市中医院', '综合三级甲等综合医院', '杭州市', '下城区', 'http://129.211.222.131:18080/images/20250116/223537f3q4w.jpg', '预约挂号：为减少您的等待时间，建议通过医院官方网站、微信公众号、电话热线或自助挂号机提前预约挂号。请按照预约时间准时到达，过时需重新排队或另约。\n携带证件：就诊时请务必携带有效身份证件（如身份证、医保卡、社保卡等），以便办理挂号、结算及医保报销手续。\n个人防护：疫情期间，请您全程佩戴口罩，保持社交距离，配合体温检测，并主动出示健康码或行程码。如有发热、咳嗽等症状，请直接前往发热门诊就诊。', '费用结算：支持现金、银行卡、支付宝、微信等多种支付方式。医保患者请携带医保卡，以便直接结算报销部分费用。\n保持安静：医院是公共场所，请保持安静，尊重其他患者，避免大声喧哗，共同维护良好的就医环境。\n特殊需求：如有行动不便、听力视力障碍等特殊需求，请及时告知工作人员，我们将尽力提供必要的帮助。\n意见反馈：您的满意是我们工作的动力。就诊结束后，欢迎通过医院意见箱、官方网站或客服热线对我们的服务提出宝贵意见。', '0411-39062999', '体育场路453号', '杭州市中医院创建于1952年5月，是华东地区较早建立的医院之一，历经近七十年砥砺前行，目前已发展成为一家集医疗、教学、科研、预防、保健、康复于一体的综合性三级甲等中医院，也是浙江中医药大学附属广兴医院和杭州师范大学附属杭州市中医院。');

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `msg_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sender_id` int(0) NULL DEFAULT NULL COMMENT '小程序用户ID',
  `admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '管理员标识',
  `send_time` datetime(0) NULL DEFAULT NULL COMMENT '发送时间',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '消息内容',
  PRIMARY KEY (`msg_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 47 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message
-- ----------------------------
INSERT INTO `message` VALUES (43, 31, 'user', '2025-03-31 09:44:02', '如果病人要求线下支付怎么办？');
INSERT INTO `message` VALUES (44, 31, 'admin', '2025-03-31 09:49:28', '病人支付完后要把支付记录回传给平台');
INSERT INTO `message` VALUES (45, 8, 'user', '2025-04-02 08:03:34', '你好，我有点事情想咨询一下');
INSERT INTO `message` VALUES (46, 8, 'admin', '2025-04-02 08:03:59', '什么事情想咨询呢？');

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`  (
  `notice_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '物理主键',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `headpic` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '题图',
  `publish_time` datetime(0) NULL DEFAULT NULL COMMENT '发布时间',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '正文内容',
  `tags` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型，以分号;隔开',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notice
-- ----------------------------
INSERT INTO `notice` VALUES (15, '春节期间暂停陪诊服务的通知', NULL, '2025-01-23 13:08:31', '尊敬的用户，您好\n2025年春节期间（1月27日至2月4日）本平台暂停所有陪诊服务，将于农历正月初八（2月5日）恢复各项陪诊服务，给您带来不便，敬请见谅，祝所有用户新春快乐，万事如意。', '公告');
INSERT INTO `notice` VALUES (16, '关于调整陪服务起步价的通知', NULL, '2025-02-04 17:55:33', '尊敬的用户，您好\n感谢您一直以来对我们陪诊服务的支持与信任。为了不断提升服务质量，确保为您提供更加专业、高效的服务体验，我们决定对现有的服务费起步价用进行适度调整。\n\n自2025年2月15日起，我们的服务费用将按照新的标准执行。具体涨价幅度及新费用标准，可在提交陪诊服务时查看。\n\n此次涨价是基于我们对服务成本、市场需求以及行业发展趋势的综合考量。我们深知涨价可能会给您带来一定的不便，但请相信，我们始终致力于为您提供更高品质的服务，确保病人可以得到更全面细致的服务。', '公告');
INSERT INTO `notice` VALUES (17, '欢迎报名参加“产妇护理知识”专题知识讲座', NULL, '2025-02-04 17:58:44', '尊敬的产妇及家属们：\n您好！\n\n为了提升产妇及家属的护理知识和技能，确保母婴健康，我院特举办一场主题为“产妇护理知识”的专题讲座。现将有关事宜通知如下：\n\n讲座时间：2025年2月20日上午9:00-11:00\n\n讲座地点：医院X楼会议室（具体楼层和会议室号请留意后续指引）\n\n讲座内容：\n\n产妇产后的身体变化与恢复\n正确的哺乳姿势与母乳喂养技巧\n产妇营养与饮食建议\n产后常见问题的应对方法\n新生儿护理要点及注意事项\n主讲嘉宾：我院资深妇产科专家XXX医生\n\n参与对象：产妇、产妇家属及其他对产妇护理感兴趣的市民\n\n报名方式：请于2025年2月18日前通过电话（020-38879098）或现场咨询台进行报名，以便我们做好会务安排。\n\n注意事项：\n\n请参与者提前15分钟到达会场签到。\n为保持会场秩序，请将手机调至静音或振动模式。\n讲座期间，我们将提供免费的茶歇服务。\n如有任何疑问，请随时联系我们的工作人员。\n我们诚挚地邀请您参加此次讲座，相信通过专家的讲解与互动，您将能更加深入地了解产妇护理的相关知识，为母婴健康保驾护航。期待您的到来！', '活动');

-- ----------------------------
-- Table structure for service_need
-- ----------------------------
DROP TABLE IF EXISTS `service_need`;
CREATE TABLE `service_need`  (
  `service_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '物理主键',
  `service_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '服务需求类型',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '申请人',
  `publish_time` datetime(0) NOT NULL COMMENT '发布时间',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '服务标题\r\n',
  `service_date` datetime(0) NULL DEFAULT NULL COMMENT '就诊时间',
  `need_pickup` int(0) NULL DEFAULT NULL COMMENT '是否需要接送',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '详情内容',
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所在城市',
  `hospital` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '医院名称',
  `service_tags` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签',
  `price` double NULL DEFAULT NULL COMMENT '服务价格',
  `pics` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '附加图片',
  `mobile` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '联系电话',
  `status` int(0) NULL DEFAULT NULL COMMENT '处理状态：0待接单；1已接单；2已完成；3已反馈',
  `select_model` int(0) NULL DEFAULT NULL COMMENT '陪诊师选择模式',
  `age_range` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '年龄要求范围',
  `gender_need` int(0) NULL DEFAULT NULL COMMENT '性别要求',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '病人位置',
  `lat` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '纬度',
  `lng` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '经度',
  PRIMARY KEY (`service_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 96 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of service_need
-- ----------------------------
INSERT INTO `service_need` VALUES (55, '全程陪诊', 3, '2025-02-26 09:40:59', NULL, '2025-03-27 09:00:00', 0, '测试数据', '杭州市', '省第二人民医院', '沉稳冷静', 280, NULL, '18167890987', 1, 0, '2', 2, '江西省九江市瑞昌市赤乌东路', '29.67658', '115.68103');
INSERT INTO `service_need` VALUES (56, '全程陪诊', 19, '2025-02-26 10:02:39', NULL, '2025-03-28 11:00:00', 0, '', '杭州市', '省中医院', '沉稳冷静', 120, NULL, '18234561234', 0, 0, '2', 0, '江西省九江市瑞昌市赤乌东路', '29.67658', '115.68103');
INSERT INTO `service_need` VALUES (57, '全程陪诊', 19, '2025-02-26 11:43:07', NULL, '2025-02-27 09:00:50', 0, '12', '杭州市', '省中医院', '无特殊要求', 333, NULL, '12134561212', 0, 1, '0', 0, '江西省九江市瑞昌市赤乌东路', '29.67658', '115.68103');
INSERT INTO `service_need` VALUES (58, '全程陪诊', 8, '2025-02-26 14:38:53', NULL, '2025-02-28 09:00:00', 1, '', '杭州市', '浙江省中医院', '开朗外向', 420, NULL, '18200022212', 0, 0, '1', 0, '江西省九江市瑞昌市赤乌东路', '29.67658', '115.68103');
INSERT INTO `service_need` VALUES (59, '全程陪诊', 8, '2025-02-26 14:38:56', NULL, '2025-02-28 09:00:00', 1, '', '杭州市', '浙江省中医院', '开朗外向', 420, NULL, '18200022212', 0, 0, '1', 0, '江西省九江市瑞昌市赤乌东路', '29.67658', '115.68103');
INSERT INTO `service_need` VALUES (60, '全程陪诊', 8, '2025-02-26 14:39:00', NULL, '2025-02-28 09:00:00', 1, '无', '杭州市', '浙江省中医院', '开朗外向', 420, NULL, '18200022212', 0, 0, '1', 0, '江西省九江市瑞昌市赤乌东路', '29.67658', '115.68103');
INSERT INTO `service_need` VALUES (61, '全程陪诊', 8, '2025-02-26 14:39:06', NULL, '2025-02-28 09:00:00', 1, '无', '杭州市', '浙江省中医院', '开朗外向', 420, NULL, '18200022212', 0, 0, '1', 0, '江西省九江市瑞昌市赤乌东路', '29.67658', '115.68103');
INSERT INTO `service_need` VALUES (62, '全程陪诊', 8, '2025-02-26 14:39:10', NULL, '2025-02-28 09:00:00', 1, '无', '杭州市', '浙江省中医院', '沉稳冷静', 420, NULL, '18200022212', 0, 0, '1', 0, '江西省九江市瑞昌市赤乌东路', '29.67658', '115.68103');
INSERT INTO `service_need` VALUES (63, '全程陪诊', 8, '2025-02-26 14:39:15', NULL, '2025-02-28 09:00:00', 1, '无', '杭州市', '浙江省中医院', '安静敏感', 420, NULL, '18200022212', 0, 0, '1', 0, '江西省九江市瑞昌市赤乌东路', '29.67658', '115.68103');
INSERT INTO `service_need` VALUES (64, '全程陪诊', 8, '2025-02-26 14:39:18', NULL, '2025-02-28 09:00:00', 1, '无', '杭州市', '浙江省中医院', '温和耐心', 420, NULL, '18200022212', 0, 0, '1', 0, '江西省九江市瑞昌市赤乌东路', '29.67658', '115.68103');
INSERT INTO `service_need` VALUES (65, '全程陪诊', 8, '2025-02-26 14:39:32', NULL, '2025-02-27 13:00:00', 0, '无', '杭州市', '浙江省中医院', '温和耐心', 420, NULL, '18200022212', 0, 0, '1', 0, '江西省九江市瑞昌市赤乌东路', '29.67658', '115.68103');
INSERT INTO `service_need` VALUES (69, '全程陪诊', 8, '2025-02-26 14:41:37', NULL, '2025-02-27 09:00:08', 0, '', '杭州市', '浙江省中医院', '沉稳冷静', 400, NULL, '12112222121', 0, 0, '0', 2, '江西省九江市瑞昌市赤乌东路', '29.67658', '115.68103');
INSERT INTO `service_need` VALUES (70, '全程陪诊', 8, '2025-02-26 14:42:35', NULL, '2025-02-27 09:00:08', 0, '', '杭州市', '浙江省中医院', '沉稳冷静', 400, NULL, '12112222121', 0, 1, '0', 2, '江西省九江市瑞昌市赤乌东路', '29.67658', '115.68103');
INSERT INTO `service_need` VALUES (71, '全程陪诊', 8, '2025-02-26 16:36:18', NULL, '2025-02-27 09:00:41', 0, '', '杭州市', '浙江省中医院', '无特殊要求', 400, NULL, '12112111111', 2, 1, '0', 0, '江西省九江市瑞昌市赤乌东路', '29.67658', '115.68103');
INSERT INTO `service_need` VALUES (73, '全程陪诊', 8, '2025-02-26 16:38:41', NULL, '2025-02-27 09:00:29', 0, '', '杭州市', '浙江省中医院', '无特殊要求', 350, NULL, '12112112121', 0, 0, '2', 2, '江西省九江市瑞昌市赤乌东路', '29.67658', '115.68103');
INSERT INTO `service_need` VALUES (74, '全程陪诊', 8, '2025-02-26 16:52:17', NULL, '2025-02-28 09:00:00', 0, '', '杭州市', '省中医院', '沉稳冷静', 300, NULL, '18912341298', 0, 0, '1', 0, '江西省九江市瑞昌市赤乌东路', '29.67658', '115.68103');
INSERT INTO `service_need` VALUES (90, '院内陪护', 8, '2025-03-11 19:14:03', '', '2025-03-12 09:00:33', 1, '', '杭州市', '浙江省中医院', '无特殊要求', 560, '', '12112121212', 0, 0, '2', 2, '请点击按钮获取定位', '', '');
INSERT INTO `service_need` VALUES (91, '送取结果', 8, '2025-03-11 19:58:17', '', '2025-03-13 09:00:00', 1, '我为', '杭州市', '浙江省中医院', '沉稳冷静', 100, '', '12112121212', 0, 0, '1', 2, '江西省九江市瑞昌市赤乌东路', '29.67658', '115.68103');
INSERT INTO `service_need` VALUES (92, '送取结果', 8, '2025-03-11 19:59:25', '', '2025-03-13 09:00:00', 1, '我为', '杭州市', '浙江省中医院', '-1', 100, '', '12112121212', 0, 0, '0', 2, '江西省九江市瑞昌市赤乌东路', '29.67658', '115.68103');
INSERT INTO `service_need` VALUES (93, '送取结果', 8, '2025-03-11 19:59:30', '', '2025-03-13 09:00:00', 0, '我为', '杭州市', '浙江省中医院', '-1', 100, '', '12112121212', 0, 0, '0', 0, '江西省九江市瑞昌市赤乌东路', '29.67658', '115.68103');
INSERT INTO `service_need` VALUES (94, '全程陪诊', 8, '2025-03-11 19:59:58', '', '2025-03-12 09:00:39', 0, '', '杭州市', '浙江省中医院', '无特殊要求', 333, '', '12112112121', 2, 0, '0', 0, '江西省九江市瑞昌市赤乌东路', '29.67658', '115.68103');
INSERT INTO `service_need` VALUES (95, '全程陪诊', 8, '2025-03-12 09:25:33', '', '2025-03-13 09:00:06', 0, '无', '杭州市', '浙江省中医院', '无特殊要求', 400, '', '11111111111', 2, 0, '2', 0, '江西省九江市瑞昌市赤乌东路', '29.67658', '115.68103');

-- ----------------------------
-- Table structure for service_order
-- ----------------------------
DROP TABLE IF EXISTS `service_order`;
CREATE TABLE `service_order`  (
  `order_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '物理主键',
  `service_id` int(0) NULL DEFAULT NULL COMMENT '服务需求ID',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '客户ID',
  `order_type` int(0) NULL DEFAULT NULL COMMENT '订单类型：1抢单；2主动预约',
  `assistant_id` int(0) NULL DEFAULT NULL COMMENT '陪诊师ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `accept_time` datetime(0) NULL DEFAULT NULL COMMENT '接单时间',
  `finish_time` datetime(0) NULL DEFAULT NULL COMMENT '完成时间',
  `price` double NULL DEFAULT NULL COMMENT '订单价格',
  `order_status` int(0) NULL DEFAULT NULL COMMENT '订单状态',
  `service_time` datetime(0) NULL DEFAULT NULL COMMENT '就诊时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注，陪诊费用说明',
  `order_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '服务验证码',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 36 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of service_order
-- ----------------------------
INSERT INTO `service_order` VALUES (24, 55, 3, 1, 28, '2025-02-26 09:40:59', '2025-02-14 09:43:46', NULL, 280, 1, '2025-03-27 09:00:00', NULL, '149714');
INSERT INTO `service_order` VALUES (25, 56, 19, 1, 28, '2025-02-26 10:02:39', '2025-02-18 18:47:39', NULL, 120, 0, '2025-03-28 11:00:00', NULL, '399090');
INSERT INTO `service_order` VALUES (26, 57, 19, 2, 28, '2025-02-26 11:43:07', '2025-02-19 18:47:46', NULL, 432, 0, NULL, NULL, NULL);
INSERT INTO `service_order` VALUES (28, 70, 8, 2, 29, '2025-02-26 14:42:35', '2025-03-28 18:48:02', NULL, 400, 0, NULL, NULL, NULL);
INSERT INTO `service_order` VALUES (29, 71, 8, 2, 23, '2025-02-26 16:36:18', '2025-02-28 21:35:32', '2025-02-28 21:54:08', 500, 1, NULL, '', NULL);
INSERT INTO `service_order` VALUES (34, 94, 8, NULL, 22, '2025-03-11 19:59:58', '2025-03-11 22:15:21', '2025-03-11 22:18:40', 300, 3, '2025-03-12 09:00:39', '', '206569');
INSERT INTO `service_order` VALUES (35, 95, 8, NULL, 21, '2025-03-12 09:25:33', '2025-03-12 09:27:11', '2025-03-12 09:30:42', 400, 3, '2025-03-13 09:00:06', '', '999999');

-- ----------------------------
-- Table structure for service_type
-- ----------------------------
DROP TABLE IF EXISTS `service_type`;
CREATE TABLE `service_type`  (
  `service_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `service_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '服务类型：全程陪诊、院内陪护、代办问诊、诊前约号、跑腿买药、送取结果',
  `price` double NULL DEFAULT NULL COMMENT '价格',
  `service_desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '服务描述',
  `service_notice` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '注意事项',
  PRIMARY KEY (`service_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of service_type
-- ----------------------------
INSERT INTO `service_type` VALUES (1, '全程陪诊', 388, '从挂号、取号、看诊到交费取药全流程陪同。', '基础服务时长为4小时，超时或有其它服务要求需要累加费用，最终以陪诊师上传的价格为准。');
INSERT INTO `service_type` VALUES (2, '长期陪诊', 3000, '适用于患有慢性疾病、需要定期复诊的患者，或者行动不便、需要长期医疗照顾的人群。', '按月计费，若有其它费用则以陪诊师上报价格为准。');
INSERT INTO `service_type` VALUES (3, '诊前约号', 50, '热门医院的门诊太难约号？专家号一放号就被约满？找我们可以轻松约号！', '普通号费用为50元，专家号150元。未成功约号则退款。');
INSERT INTO `service_type` VALUES (4, '院内陪护', 588, '生活饮食照料‌、取送检查、卫生清洁、代办院内手续', '该服务与天数相关，请与陪诊师协调最终费用。');
INSERT INTO `service_type` VALUES (5, '跑腿买药', 30, '提供药名，可全城为您寻找购买。处方药需要提供医生处方笺。', '3公里外按5元每公里累加，特殊药品可能需要加价。');
INSERT INTO `service_type` VALUES (6, '送取结果', 80, '提供各大医院的检查检验结果送取', '5公里内80元，超出范围按每公里10元累加。');

-- ----------------------------
-- Table structure for slide_pic
-- ----------------------------
DROP TABLE IF EXISTS `slide_pic`;
CREATE TABLE `slide_pic`  (
  `slide_pic_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pic_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片url',
  `seq` int(0) NULL DEFAULT NULL COMMENT '排序',
  `link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '链接',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`slide_pic_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of slide_pic
-- ----------------------------
INSERT INTO `slide_pic` VALUES (1, 'http://129.211.222.131:18080/images/20250204/113139agm7a.jpg', 1, '', '');
INSERT INTO `slide_pic` VALUES (2, 'http://129.211.222.131:18080/images/20250110/113310z56r9.jpg', 3, '', '');
INSERT INTO `slide_pic` VALUES (3, 'http://129.211.222.131:18080/images/20250110/113325l5cqq.jpg', 2, '', '');

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag`  (
  `tag_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tag_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签名称',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注说明',
  PRIMARY KEY (`tag_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tag
-- ----------------------------
INSERT INTO `tag` VALUES (1, '沉稳冷静', '要求：沉着冷静，给予患者安全感');
INSERT INTO `tag` VALUES (2, '开朗外向', '要求：擅长交流，主动沟通，提供情绪价值');
INSERT INTO `tag` VALUES (3, '温和耐心', '');
INSERT INTO `tag` VALUES (4, '安静敏感', '');
INSERT INTO `tag` VALUES (5, '无障碍沟通', '会手语');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '物理ID',
  `mobile` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `open_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信的openid',
  `role` int(0) NULL DEFAULT NULL COMMENT '角色：1-病人；2-陪诊师',
  `status` int(0) NULL DEFAULT NULL COMMENT '状态：1-正常；-1已禁用',
  `location` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所在城市',
  `pic` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像图片',
  `nick_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `gender` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别',
  `age` int(0) NULL DEFAULT NULL COMMENT '年龄',
  `id_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '身份证号',
  `real_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '真实姓名',
  `assist_desc` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '陪诊师简介',
  `rate` double NULL DEFAULT NULL COMMENT '总评分',
  `total_service` int(0) NULL DEFAULT NULL COMMENT '总服务次数',
  `tags` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签，以;分隔',
  `lat` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '位置：纬度',
  `lng` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '位置：经度',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 59 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '13800138001', 'temp', 1, 1, '杭州市西湖区文三路123号', 'https://images.pexels.com/photos/2173872/pexels-photo-2173872.jpeg?auto=compress&cs=tinysrgb&w=800', '小星星', '女', 28, '330102199501012345', '王晓星', NULL, NULL, NULL, NULL, '30.304555', '120.048107');
INSERT INTO `user` VALUES (2, '13800138002', 'temp', 1, 1, '杭州市上城区解放路456号', 'https://images.pexels.com/photos/357316/pexels-photo-357316.jpeg?auto=compress&cs=tinysrgb&w=800', '老李头', '男', 65, '330103195801023456', '李建国', NULL, NULL, NULL, NULL, '30.298711', '120.178051');
INSERT INTO `user` VALUES (3, '13800138003', 'temp', 1, 1, '杭州市滨江区江南大道789号', 'https://images.pexels.com/photos/1214205/pexels-photo-1214205.jpeg?auto=compress&cs=tinysrgb&w=800', '小雨', '女', 22, '330102199501012345', '赵雨婷', NULL, NULL, NULL, NULL, '30.298753', '120.178051');
INSERT INTO `user` VALUES (4, '13800138004', 'temp', 1, 1, '杭州市拱墅区莫干山路101号', 'https://images.pexels.com/photos/1715092/pexels-photo-1715092.jpeg?auto=compress&cs=tinysrgb&w=800', '大熊', '男', 35, '330103195801023456', '张伟', NULL, NULL, NULL, NULL, '30.304855', '120.048717');
INSERT INTO `user` VALUES (5, '13800138005', 'temp', 1, 1, '杭州市余杭区文一西路202号', 'https://images.pexels.com/photos/11279931/pexels-photo-11279931.jpeg?auto=compress&cs=tinysrgb&w=800', '小鹿', '女', 30, '330104200001034567', '林鹿', NULL, NULL, NULL, NULL, '30.298953', '120.178551');
INSERT INTO `user` VALUES (6, '13800138006', 'temp', 1, 1, '杭州市萧山区市心路303号', 'https://images.pexels.com/photos/16577560/pexels-photo-16577560.jpeg?auto=compress&cs=tinysrgb&w=800', '大山', '男', 70, '330105198801045678', '张大山', NULL, NULL, NULL, NULL, '30.304555', '120.048107');
INSERT INTO `user` VALUES (7, '13800138007', 'temp', 1, 1, '杭州市富阳区桂花路404号', 'https://images.unsplash.com/photo-1558339136-3b4e87294276?w=800&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTB8fCVFNCVCQSVCQSVFNyU4OSVBOXxlbnwwfHwwfHx8MA%3D%3D', '小蜜蜂', '女', 25, '330106199201056789', '杨蜜', NULL, NULL, NULL, NULL, '30.298711', '120.178051');
INSERT INTO `user` VALUES (8, '13800138008', 'oufmM6wZMYvPwvdDQzwF7HhKrlH8', 1, 1, '杭州市临安区青山湖路505号', 'https://images.pexels.com/photos/2173872/pexels-photo-2173872.jpeg?auto=compress&cs=tinysrgb&w=800', '虎子', '男', 40, '330107195201067890', '王虎', NULL, NULL, NULL, NULL, '30.304555', '120.048217');
INSERT INTO `user` VALUES (9, '13800138009', 'temp', 1, 1, '杭州市江干区下沙路606号', 'https://images.pexels.com/photos/2599509/pexels-photo-2599509.jpeg?auto=compress&cs=tinysrgb&w=800', '帅锅', '男', 18, '330108199701078901', '刘帅', NULL, NULL, NULL, NULL, '30.300158', '120.176207');
INSERT INTO `user` VALUES (10, '13800138010', 'temp', 1, 1, '杭州市淳安县千岛湖路707号', 'https://images.pexels.com/photos/27680249/pexels-photo-27680249.jpeg?auto=compress&cs=tinysrgb&w=800', '老陈', '男', 55, '330109198201089012', '陈大海', NULL, NULL, NULL, NULL, '30.300128', '120.176296');
INSERT INTO `user` VALUES (11, '13800138011', 'temp', 1, 1, '杭州市桐庐县迎春路808号', 'https://images.pexels.com/photos/2624077/pexels-photo-2624077.jpeg?auto=compress&cs=tinysrgb&w=800', '小辣椒', '女', 27, '330110200501090123', '胡辣', NULL, NULL, NULL, NULL, '30.300118', '120.176096');
INSERT INTO `user` VALUES (12, '13800138012', 'temp', 1, 1, '杭州市建德市新安江路909号', 'https://images.pexels.com/photos/3057984/pexels-photo-3057984.jpeg?auto=compress&cs=tinysrgb&w=800', '大鸟', '男', 65, '330111196601101234', '赵飞', NULL, NULL, NULL, NULL, '30.300258', '120.176096');
INSERT INTO `user` VALUES (13, '13800138013', 'temp', 1, 1, '杭州市西湖区天目山路1010号', 'https://images.pexels.com/photos/13737443/pexels-photo-13737443.jpeg?auto=compress&cs=tinysrgb&w=800', '小鹿斑比', '女', 19, '330112199601112345', '鹿小鹿', NULL, NULL, NULL, NULL, '30.292186', '120.176647');
INSERT INTO `user` VALUES (14, '13800138014', 'temp', 1, 1, '杭州市上城区延安路1111号', NULL, '老马', '男', 60, '330113197801123456', '马成功', NULL, NULL, NULL, NULL, '30.292686', '120.176607');
INSERT INTO `user` VALUES (15, '13800138015', 'temp', 1, 1, '杭州市滨江区滨康路1212号', NULL, '小苹果', '女', 24, '330114200301134567', '李苹', NULL, NULL, NULL, NULL, '30.292186', '120.176647');
INSERT INTO `user` VALUES (16, '13800138016', 'temp', 1, 1, '杭州市拱墅区湖墅南路1313号', NULL, '大牛', '男', 38, '330115196301145678', '牛大力', NULL, NULL, NULL, NULL, '30.292116', '120.179647');
INSERT INTO `user` VALUES (17, '13800138017', 'temp', 1, 1, '杭州市余杭区良渚路1414号', NULL, '小狐狸', '女', 26, '330116199901156789', '胡丽', NULL, NULL, NULL, NULL, '30.292286', '120.173647');
INSERT INTO `user` VALUES (18, '13800138018', 'temp', 1, 1, '杭州市萧山区人民路1515号', NULL, '老鹰', '男', 50, '330117198301167890', '高飞', NULL, NULL, NULL, NULL, '30.299524', '120.170714');
INSERT INTO `user` VALUES (19, '13800138019', 'temp', 1, 1, '杭州市富阳区富春路1616号', NULL, '小蝴蝶', '女', 21, '330118199701178901', '胡蝶', NULL, NULL, NULL, NULL, '30.232524', '120.152714');
INSERT INTO `user` VALUES (20, '13800138020', 'temp', 1, -1, '杭州市临安区锦城路1717号', NULL, '大狗', '男', 42, '330119197301189012', '汪旺', NULL, NULL, NULL, NULL, '30.290524', '120.192714');
INSERT INTO `user` VALUES (21, '13800238001', 'temp', 2, 1, '杭州市西湖区文二路101号', 'https://images.pexels.com/photos/654696/pexels-photo-654696.jpeg?auto=compress&cs=tinysrgb&w=800', '贴心小张', '女', 24, '330102199301012345', '张小丽', '细心周到，擅长接送服务。', 3, 122, '提供接送，细心', '30.292024', '120.178714');
INSERT INTO `user` VALUES (22, '13800238002', 'temp', 2, 1, '杭州市上城区解放路202号', 'https://images.pexels.com/photos/30858160/pexels-photo-30858160.jpeg?auto=compress&cs=tinysrgb&w=800', '老李陪诊', '男', 40, '330103196801023456', '李强', '经验丰富，擅长老年患者陪诊。', 5, 97, '经验丰富，耐心', '30.121841', '120.259523');
INSERT INTO `user` VALUES (23, '13800238003', 'temp', 2, 1, '杭州市滨江区江南大道303号', 'https://images.pexels.com/photos/3936894/pexels-photo-3936894.jpeg?auto=compress&cs=tinysrgb&w=800', '小雨陪护', '女', 25, '330104199801034567', '赵小雨', '温柔细心，擅长儿童陪诊。', 5, 152, '温柔，儿童陪诊', '30.12141', '120.25953');
INSERT INTO `user` VALUES (24, '13800238004', 'temp', 2, 1, '杭州市拱墅区莫干山路404号', 'https://images.pexels.com/photos/20860586/pexels-photo-20860586.jpeg?auto=compress&cs=tinysrgb&w=800', '大熊陪诊', '男', 21, '330105198501045678', '熊大', '力气大，擅长行动不便患者陪诊。', 4.7, 80, '力气大，行动不便陪诊', '30.121841', '120.259523');
INSERT INTO `user` VALUES (25, '13800238005', 'temp', 2, 1, '杭州市余杭区文一西路505号', 'https://images.pexels.com/photos/11398474/pexels-photo-11398474.jpeg?auto=compress&cs=tinysrgb&w=800', '小鹿陪护', '女', 30, '330106199201056789', '林小鹿', '温柔耐心，擅长心理疏导。', 4.6, 130, '心理疏导，温柔', '30.12341', '120.253523');
INSERT INTO `user` VALUES (26, '13800238006', 'temp', 2, 1, '杭州市萧山区市心路606号', 'https://images.pexels.com/photos/3244392/pexels-photo-3244392.jpeg?auto=compress&cs=tinysrgb&w=800', '老张陪诊', '男', 36, '330107199201067890', '张大山', '经验丰富，擅长老年患者陪诊。', 4.5, 110, '老年陪诊，耐心', '30.186643', '120.22157');
INSERT INTO `user` VALUES (27, '13800238007', 'temp', 2, 1, '杭州市富阳区桂花路707号', 'https://images.pexels.com/photos/698532/pexels-photo-698532.jpeg?auto=compress&cs=tinysrgb&w=800', '小蜜蜂陪护', '女', 26, '330108199701078901', '杨小蜜', '细心周到，擅长接送服务。', 4.4, 140, '提供接送，细心', '30.185643', '120.20157');
INSERT INTO `user` VALUES (28, '13800238008', 'temp', 2, 1, '杭州市临安区青山湖路808号', 'https://images.pexels.com/photos/3394657/pexels-photo-3394657.jpeg?auto=compress&cs=tinysrgb&w=800', '大猫陪诊', '男', 35, '330109198201089012', '王大猫', '细心耐心，擅长术后陪诊。', 4.3, 100, '术后陪诊，细心', '30.18643', '120.27157');
INSERT INTO `user` VALUES (29, '13800238009', 'temp', 2, 1, '杭州市江干区下沙路909号', 'https://images.pexels.com/photos/3209624/pexels-photo-3209624.jpeg?auto=compress&cs=tinysrgb&w=800', '小兔子陪护', '女', 22, '330110200001090123', '刘小兔', '温柔细心，擅长儿童陪诊。', 4.3, 90, '儿童陪诊，温柔', '30.18543', '120.2057');
INSERT INTO `user` VALUES (30, '13800238010', 'temp', 2, 1, '杭州市淳安县千岛湖路1010号', NULL, '老陈陪诊', '男', 27, '330111196301101234', '陈大志', '经验丰富，擅长老年患者陪诊。', 4.1, 115, '老年陪诊，耐心', '30.274', '119.989318');
INSERT INTO `user` VALUES (31, '13800238011', 'temp', 2, 1, '杭州市桐庐县迎春路1111号', NULL, '小辣椒陪护', '女', 28, '330112199401112345', '胡小辣', '热情细心，擅长心理疏导。', 4, 125, '心理疏导，热情', '30.27452', '119.989118');
INSERT INTO `user` VALUES (32, '13800238012', 'temp', 2, 1, '杭州市建德市新安江路1212号', NULL, '大鸟陪诊', '男', 24, '330113197501123456', '赵大鸟', '细心耐心，擅长术后陪诊。', 3.9, 105, '术后陪诊，细心', '30.27499', '119.989318');
INSERT INTO `user` VALUES (33, '13800238013', 'temp', 2, 1, '杭州市西湖区天目山路1313号', NULL, '小鹿斑比陪护', '女', 23, '330114200001134567', '鹿小鹿', '温柔细心，擅长儿童陪诊。', 3.5, 160, '儿童陪诊，温柔', '30.27401', '119.989118');
INSERT INTO `user` VALUES (34, '13800238014', 'temp', 2, 1, '杭州市上城区延安路1414号', NULL, '老马陪诊', '男', 33, '330115196001145678', '马大志', '经验丰富，擅长老年患者陪诊。', 3.3, 85, '老年陪诊，耐心', '22.93772', '113.38424');
INSERT INTO `user` VALUES (35, '13800238015', 'temp', 2, 1, '杭州市滨江区滨康路1515号', NULL, '小苹果陪护', '女', 27, '330116199501156789', '李小苹', '细心周到，擅长接送服务。', 3.3, 135, '提供接送，细心', '30.237324', '120.029039');
INSERT INTO `user` VALUES (36, '13800238016', 'temp', 2, 1, '杭州市拱墅区湖墅南路1616号', '', '大牛陪诊', '男', 40, '330117198201167890', '牛大力', '力气大，擅长行动不便患者陪诊。', 3.2, 75, '力气大，行动不便陪诊', '30.237024', '120.109039');
INSERT INTO `user` VALUES (37, '13800238017', 'temp', 2, 1, '杭州市余杭区良渚路1717号', NULL, '小狐狸陪护', '女', 26, '330118199601178901', '胡小丽', '温柔耐心，擅长心理疏导。', 4.8, 145, '心理疏导，温柔', '30.237024', '119.989118');
INSERT INTO `user` VALUES (39, '13800238018', 'temp', 2, 1, '杭州市萧山区人民路1818号', NULL, '老鹰陪诊', '男', 32, '330119197001189012', '高大鹰', '经验丰富，擅长老年患者陪诊。', 4.7, 70, '老年陪诊，耐心', '30.237024', '119.989118');
INSERT INTO `user` VALUES (40, '13800238019', 'temp', 2, 1, '杭州市富阳区富春路1919号', NULL, '小蝴蝶陪护', '女', 24, '330120200001190123', '胡小蝶', '温柔细心，擅长儿童陪诊。', 4.9, 155, '儿童陪诊，温柔', '22.93772', '120.029039');

-- ----------------------------
-- Table structure for user_tag
-- ----------------------------
DROP TABLE IF EXISTS `user_tag`;
CREATE TABLE `user_tag`  (
  `user_tag_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户ID',
  `tag_id` int(0) NULL DEFAULT NULL COMMENT '标签ID',
  `validate` int(0) NULL DEFAULT NULL COMMENT '审核结果',
  PRIMARY KEY (`user_tag_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 81 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_tag
-- ----------------------------
INSERT INTO `user_tag` VALUES (10, 26, 4, 1);
INSERT INTO `user_tag` VALUES (11, 27, 3, 1);
INSERT INTO `user_tag` VALUES (23, 24, 2, 1);
INSERT INTO `user_tag` VALUES (24, 25, 4, 1);
INSERT INTO `user_tag` VALUES (31, 29, 1, 1);
INSERT INTO `user_tag` VALUES (32, 30, 1, 1);
INSERT INTO `user_tag` VALUES (34, 31, 3, 1);
INSERT INTO `user_tag` VALUES (37, 32, 3, 1);
INSERT INTO `user_tag` VALUES (38, 33, 3, 1);
INSERT INTO `user_tag` VALUES (39, 34, 1, 1);
INSERT INTO `user_tag` VALUES (42, 35, 1, 1);
INSERT INTO `user_tag` VALUES (43, 36, 1, 1);
INSERT INTO `user_tag` VALUES (65, 20, 2, 1);
INSERT INTO `user_tag` VALUES (68, 37, 3, 1);
INSERT INTO `user_tag` VALUES (69, 28, 2, 1);
INSERT INTO `user_tag` VALUES (72, 38, 4, 1);
INSERT INTO `user_tag` VALUES (73, 39, 2, 1);
INSERT INTO `user_tag` VALUES (74, 40, 4, 1);
INSERT INTO `user_tag` VALUES (75, 23, 1, 1);
INSERT INTO `user_tag` VALUES (76, 23, 2, 1);
INSERT INTO `user_tag` VALUES (77, 22, 2, 1);
INSERT INTO `user_tag` VALUES (78, 22, 3, 1);
INSERT INTO `user_tag` VALUES (79, 21, 2, 1);
INSERT INTO `user_tag` VALUES (80, 21, 5, 1);

SET FOREIGN_KEY_CHECKS = 1;

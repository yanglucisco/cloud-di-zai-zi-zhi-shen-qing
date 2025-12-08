/*
 Navicat Premium Dump SQL

 Source Server         : cloud-di-zai-auth-center
 Source Server Type    : MySQL
 Source Server Version : 80026 (8.0.26)
 Source Host           : localhost:13306
 Source Schema         : auth_center

 Target Server Type    : MySQL
 Target Server Version : 80026 (8.0.26)
 File Encoding         : 65001

 Date: 08/12/2025 20:58:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for yl_test
-- ----------------------------
DROP TABLE IF EXISTS `yl_test`;
CREATE TABLE `yl_test`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of yl_test
-- ----------------------------
INSERT INTO `yl_test` VALUES ('1', 'yanglu');
INSERT INTO `yl_test` VALUES ('2', 'zhangsan');
INSERT INTO `yl_test` VALUES ('3', 'LiSi');

SET FOREIGN_KEY_CHECKS = 1;

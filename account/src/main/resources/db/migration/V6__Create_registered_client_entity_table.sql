/*
 Navicat Premium Dump SQL

 Source Server         : cloud_di_zai_auth_center
 Source Server Type    : MySQL
 Source Server Version : 80026 (8.0.26)
 Source Host           : 192.168.1.249:13306
 Source Schema         : auth_center

 Target Server Type    : MySQL
 Target Server Version : 80026 (8.0.26)
 File Encoding         : 65001

 Date: 14/01/2026 09:57:42
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for registered_client_entity
-- ----------------------------
DROP TABLE IF EXISTS `registered_client_entity`;
CREATE TABLE `registered_client_entity`  (
  `id` bigint NOT NULL,
  `delete_flag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `create_user` bigint NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `update_user` bigint NULL DEFAULT NULL,
  `client_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `client_secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `client_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `client_authentication_methods` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `authorization_grant_types` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `redirect_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `post_logout_redirect_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `scopes` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `client_settings` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `token_settings` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
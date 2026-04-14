-- WSN鏅烘収鍐滆仈鑺傜偣閮ㄧ讲鎺ㄨ崘绯荤粺 涓氬姟琛?& 鑿滃崟鍒濆鍖栬剼鏈?-- 鏁版嵁搴擄細ry-demo锛圡ySQL 8.0锛寀tf8mb4锛?-- 鎵ц鍓嶈纭繚宸插垏鎹㈠埌姝ｇ‘鏁版嵁搴擄細USE `ry-demo`;
SET NAMES utf8mb4;

-- ----------------------------
-- 1. 鍐滅敯淇℃伅琛?-- ----------------------------
DROP TABLE IF EXISTS `agri_farmland`;
CREATE TABLE `agri_farmland` (
  `farmland_id`   BIGINT       NOT NULL AUTO_INCREMENT COMMENT '鍐滅敯ID',
  `farmland_name` VARCHAR(100) NOT NULL                COMMENT '鍐滅敯鍚嶇О',
  `boundary_data` TEXT                                 COMMENT '杈圭晫鍧愭爣JSON [[x1,y1],[x2,y2]...]',
  `area`          DECIMAL(12,2)                        COMMENT '闈㈢Н锛堜憨锛?,
  `crop_type`     VARCHAR(50)                          COMMENT '浣滅墿绫诲瀷',
  `grid_rows`     INT          NOT NULL DEFAULT 20     COMMENT '缃戞牸琛屾暟',
  `grid_cols`     INT          NOT NULL DEFAULT 20     COMMENT '缃戞牸鍒楁暟',
  `user_id`       BIGINT                               COMMENT '鎵€灞炵敤鎴稩D',
  `status`        CHAR(1)      NOT NULL DEFAULT '0'    COMMENT '鐘舵€侊紙0姝ｅ父 1鍋滅敤锛?,
  `create_by`     VARCHAR(64)          DEFAULT ''      COMMENT '鍒涘缓鑰?,
  `create_time`   DATETIME                             COMMENT '鍒涘缓鏃堕棿',
  `update_by`     VARCHAR(64)          DEFAULT ''      COMMENT '鏇存柊鑰?,
  `update_time`   DATETIME                             COMMENT '鏇存柊鏃堕棿',
  `remark`        VARCHAR(500)                         COMMENT '澶囨敞',
  PRIMARY KEY (`farmland_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='鍐滅敯淇℃伅琛?;

-- ----------------------------
-- 2. 鐜鍙傛暟鏁版嵁琛?-- ----------------------------
DROP TABLE IF EXISTS `agri_env_data`;
CREATE TABLE `agri_env_data` (
  `data_id`         BIGINT        NOT NULL AUTO_INCREMENT COMMENT '鏁版嵁ID',
  `farmland_id`     BIGINT        NOT NULL               COMMENT '鍐滅敯ID',
  `temperature`     DECIMAL(5,2)                         COMMENT '娓╁害(鈩?',
  `humidity`        DECIMAL(5,2)                         COMMENT '婀垮害(%)',
  `light_intensity` DECIMAL(10,2)                        COMMENT '鍏夌収寮哄害(lux)',
  `soil_moisture`   DECIMAL(5,2)                         COMMENT '鍦熷￥鍚按閲?%)',
  `wind_speed`      DECIMAL(5,2)                         COMMENT '椋庨€?m/s)',
  `record_time`     DATETIME                             COMMENT '璁板綍鏃堕棿',
  `create_by`       VARCHAR(64)           DEFAULT ''     COMMENT '鍒涘缓鑰?,
  `create_time`     DATETIME                             COMMENT '鍒涘缓鏃堕棿',
  PRIMARY KEY (`data_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='鐜鍙傛暟鏁版嵁琛?;

-- ----------------------------
-- 3. 绠楁硶閰嶇疆琛?-- ----------------------------
DROP TABLE IF EXISTS `agri_algorithm_config`;
CREATE TABLE `agri_algorithm_config` (
  `config_id`      BIGINT       NOT NULL AUTO_INCREMENT COMMENT '閰嶇疆ID',
  `algorithm_type` VARCHAR(20)  NOT NULL                COMMENT '绠楁硶绫诲瀷锛坈overage瑕嗙洊浼樺寲/cluster鍒嗙皣浼樺寲锛?,
  `algorithm_name` VARCHAR(100)                         COMMENT '绠楁硶鍚嶇О',
  `param_json`     TEXT                                 COMMENT '绠楁硶鍙傛暟JSON',
  `description`    VARCHAR(500)                         COMMENT '绠楁硶璇存槑',
  `status`         CHAR(1)      NOT NULL DEFAULT '0'    COMMENT '鐘舵€侊紙0姝ｅ父 1鍋滅敤锛?,
  `create_by`      VARCHAR(64)          DEFAULT ''      COMMENT '鍒涘缓鑰?,
  `create_time`    DATETIME                             COMMENT '鍒涘缓鏃堕棿',
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='绠楁硶閰嶇疆琛?;

-- ----------------------------
-- 4. 閮ㄧ讲鏂规琛?-- ----------------------------
DROP TABLE IF EXISTS `agri_deploy_scheme`;
CREATE TABLE `agri_deploy_scheme` (
  `scheme_id`      BIGINT        NOT NULL AUTO_INCREMENT COMMENT '鏂规ID',
  `scheme_name`    VARCHAR(200)                          COMMENT '鏂规鍚嶇О',
  `farmland_id`    BIGINT        NOT NULL                COMMENT '鍐滅敯ID',
  `algorithm_type` VARCHAR(20)   NOT NULL                COMMENT '绠楁硶绫诲瀷锛坈overage/cluster锛?,
  `node_count`     INT                                   COMMENT '鑺傜偣鏁伴噺',
  `coverage_rate`  DECIMAL(5,2)                          COMMENT '瑕嗙洊鐜?%)',
  `energy_balance` DECIMAL(5,2)                          COMMENT '鑳借€楀潎琛″害(%)',
  `result_json`    TEXT                                  COMMENT '閮ㄧ讲缁撴灉JSON',
  `user_id`        BIGINT                                COMMENT '鎵€灞炵敤鎴稩D',
  `status`         CHAR(1)       NOT NULL DEFAULT '0'    COMMENT '鐘舵€侊紙0寰呯‘璁?1宸茬‘璁?2宸插簾寮冿級',
  `create_by`      VARCHAR(64)           DEFAULT ''      COMMENT '鍒涘缓鑰?,
  `create_time`    DATETIME                              COMMENT '鍒涘缓鏃堕棿',
  `update_by`      VARCHAR(64)           DEFAULT ''      COMMENT '鏇存柊鑰?,
  `update_time`    DATETIME                              COMMENT '鏇存柊鏃堕棿',
  `remark`         VARCHAR(500)                          COMMENT '澶囨敞',
  PRIMARY KEY (`scheme_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='閮ㄧ讲鏂规琛?;

-- ----------------------------
-- 5. 閮ㄧ讲鑺傜偣鏄庣粏琛?-- ----------------------------
DROP TABLE IF EXISTS `agri_deploy_node`;
CREATE TABLE `agri_deploy_node` (
  `node_id`         BIGINT        NOT NULL AUTO_INCREMENT COMMENT '鑺傜偣ID',
  `scheme_id`       BIGINT        NOT NULL                COMMENT '鏂规ID',
  `node_code`       VARCHAR(50)                           COMMENT '鑺傜偣缂栧彿',
  `node_type`       VARCHAR(20)           DEFAULT 'sensor' COMMENT '鑺傜偣绫诲瀷锛坰ensor浼犳劅鍣?sink姹囪仛鑺傜偣锛?,
  `pos_x`           DECIMAL(10,4)                         COMMENT 'X鍧愭爣',
  `pos_y`           DECIMAL(10,4)                         COMMENT 'Y鍧愭爣',
  `coverage_radius` DECIMAL(10,2)                         COMMENT '瑕嗙洊鍗婂緞',
  `energy_level`    DECIMAL(5,2)          DEFAULT 100     COMMENT '鑳介噺姘村钩(%)',
  `cluster_id`      INT                                   COMMENT '鎵€灞炵皣ID锛堝垎绨囩畻娉曟椂浣跨敤锛?,
  `is_cluster_head` CHAR(1)               DEFAULT 'N'     COMMENT '鏄惁绨囧ご锛圷/N锛?,
  PRIMARY KEY (`node_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='閮ㄧ讲鑺傜偣鏄庣粏琛?;

-- ----------------------------
-- 6. 鐢ㄦ埛璇勮琛?-- ----------------------------
DROP TABLE IF EXISTS `agri_comment`;
CREATE TABLE `agri_comment` (
  `comment_id`   BIGINT       NOT NULL AUTO_INCREMENT COMMENT '璇勮ID',
  `content`      TEXT         NOT NULL                COMMENT '璇勮鍐呭',
  `image_urls`   VARCHAR(1000)                        COMMENT '鍥剧墖URL锛屽涓€楀彿鍒嗛殧',
  `rating`       INT                  DEFAULT 5       COMMENT '璇勫垎(1-5)',
  `target_type`  VARCHAR(20)                          COMMENT '璇勮瀵硅薄绫诲瀷锛坰ystem/scheme/algorithm锛?,
  `target_id`    BIGINT                               COMMENT '璇勮瀵硅薄ID',
  `is_anonymous` CHAR(1)              DEFAULT 'N'     COMMENT '鏄惁鍖垮悕(Y/N)',
  `user_id`      BIGINT                               COMMENT '璇勮鐢ㄦ埛ID',
  `user_name`    VARCHAR(64)                          COMMENT '鐢ㄦ埛鏄电О',
  `status`       CHAR(1)              DEFAULT '0'     COMMENT '鐘舵€侊紙0姝ｅ父 1闅愯棌锛?,
  `create_by`    VARCHAR(64)          DEFAULT ''      COMMENT '鍒涘缓鑰?,
  `create_time`  DATETIME                             COMMENT '鍒涘缓鏃堕棿',
  PRIMARY KEY (`comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='鐢ㄦ埛璇勮琛?;

-- ----------------------------
-- 7. 涓氬姟閿欒鏃ュ織琛?-- ----------------------------
DROP TABLE IF EXISTS `agri_error_log`;
CREATE TABLE `agri_error_log` (
  `error_id`      BIGINT       NOT NULL AUTO_INCREMENT COMMENT '閿欒ID',
  `error_type`    VARCHAR(50)                          COMMENT '閿欒绫诲瀷锛圖ATA_FORMAT/ALGORITHM_FAIL/UPLOAD_FAIL绛夛級',
  `error_msg`     VARCHAR(500)                         COMMENT '閿欒鎻愮ず淇℃伅',
  `stack_trace`   TEXT                                 COMMENT '鍫嗘爤淇℃伅',
  `request_url`   VARCHAR(200)                         COMMENT '璇锋眰URL',
  `request_param` TEXT                                 COMMENT '璇锋眰鍙傛暟',
  `user_id`       BIGINT                               COMMENT '鎿嶄綔鐢ㄦ埛ID',
  `user_name`     VARCHAR(64)                          COMMENT '鐢ㄦ埛鍚?,
  `create_time`   DATETIME                             COMMENT '鍙戠敓鏃堕棿',
  PRIMARY KEY (`error_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='涓氬姟閿欒鏃ュ織琛?;

-- ----------------------------
-- 鑿滃崟鏉冮檺 SQL
-- ----------------------------
-- 涓€绾ц彍鍗曪細鏅烘収鍐滆仈锛坢enu_id=2000锛?INSERT INTO `sys_menu` (`menu_id`,`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`is_frame`,`is_cache`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`)
VALUES (2000,'鏅烘収鍐滆仈',0,5,'agricultural',NULL,NULL,1,0,'M','0','0',NULL,'#','admin',NOW(),'',NULL,'WSN鏅烘収鍐滆仈鑺傜偣閮ㄧ讲鎺ㄨ崘绯荤粺');

-- 鍐滅敯绠＄悊锛坢enu_id=2001锛?INSERT INTO `sys_menu` (`menu_id`,`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`is_frame`,`is_cache`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`)
VALUES (2001,'鍐滅敯绠＄悊',2000,1,'farmland','agricultural/farmland/index',NULL,1,0,'C','0','0','agricultural:farmland:list','tree','admin',NOW(),'',NULL,'鍐滅敯淇℃伅绠＄悊');

-- 鐜鏁版嵁锛坢enu_id=2002锛?INSERT INTO `sys_menu` (`menu_id`,`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`is_frame`,`is_cache`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`)
VALUES (2002,'鐜鏁版嵁',2000,2,'envdata','agricultural/envdata/index',NULL,1,0,'C','0','0','agricultural:envdata:list','chart','admin',NOW(),'',NULL,'鐜鍙傛暟鏁版嵁绠＄悊');

-- 閮ㄧ讲鏂规锛坢enu_id=2003锛?INSERT INTO `sys_menu` (`menu_id`,`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`is_frame`,`is_cache`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`)
VALUES (2003,'閮ㄧ讲鏂规',2000,3,'deploy','agricultural/deploy/index',NULL,1,0,'C','0','0','agricultural:deploy:list','edit','admin',NOW(),'',NULL,'鑺傜偣閮ㄧ讲鏂规绠＄悊');

-- 鑺傜偣鍙鍖栵紙menu_id=2004锛岄殣钘忎笉鍦ㄤ晶杈规爮鏄剧ず锛?INSERT INTO `sys_menu` (`menu_id`,`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`is_frame`,`is_cache`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`)
VALUES (2004,'鑺傜偣鍙鍖?,2000,4,'deploy/visualize/:schemeId','agricultural/deploy/visualize',NULL,1,0,'C','1','0','agricultural:deploy:visualize','eye','admin',NOW(),'',NULL,'鑺傜偣閮ㄧ讲鍙鍖?);

-- 鐢ㄦ埛璇勮锛坢enu_id=2005锛?INSERT INTO `sys_menu` (`menu_id`,`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`is_frame`,`is_cache`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`)
VALUES (2005,'鐢ㄦ埛璇勮',2000,5,'comment','agricultural/comment/index',NULL,1,0,'C','0','0','agricultural:comment:list','comment','admin',NOW(),'',NULL,'鐢ㄦ埛璇勮绠＄悊');

-- 閿欒鏃ュ織锛坢enu_id=2006锛?INSERT INTO `sys_menu` (`menu_id`,`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`is_frame`,`is_cache`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`)
VALUES (2006,'閿欒鏃ュ織',2000,6,'errorlog','agricultural/errorlog/index',NULL,1,0,'C','0','0','agricultural:errorlog:list','warning','admin',NOW(),'',NULL,'涓氬姟閿欒鏃ュ織');

-- 鎸夐挳鏉冮檺锛堝啘鐢扮鐞嗭級
INSERT INTO `sys_menu` VALUES(2010,'鍐滅敯鏌ヨ',2001,1,'','','',1,0,'F','0','0','agricultural:farmland:query','#','admin',NOW(),'',NULL,'');
INSERT INTO `sys_menu` VALUES(2011,'鍐滅敯鏂板',2001,2,'','','',1,0,'F','0','0','agricultural:farmland:add','#','admin',NOW(),'',NULL,'');
INSERT INTO `sys_menu` VALUES(2012,'鍐滅敯淇敼',2001,3,'','','',1,0,'F','0','0','agricultural:farmland:edit','#','admin',NOW(),'',NULL,'');
INSERT INTO `sys_menu` VALUES(2013,'鍐滅敯鍒犻櫎',2001,4,'','','',1,0,'F','0','0','agricultural:farmland:remove','#','admin',NOW(),'',NULL,'');
INSERT INTO `sys_menu` VALUES(2014,'鍐滅敯瀵煎叆',2001,5,'','','',1,0,'F','0','0','agricultural:farmland:import','#','admin',NOW(),'',NULL,'');

-- 鎸夐挳鏉冮檺锛堢幆澧冩暟鎹級
INSERT INTO `sys_menu` VALUES(2020,'鏁版嵁鏌ヨ',2002,1,'','','',1,0,'F','0','0','agricultural:envdata:query','#','admin',NOW(),'',NULL,'');
INSERT INTO `sys_menu` VALUES(2021,'鏁版嵁鏂板',2002,2,'','','',1,0,'F','0','0','agricultural:envdata:add','#','admin',NOW(),'',NULL,'');
INSERT INTO `sys_menu` VALUES(2022,'鏁版嵁鍒犻櫎',2002,3,'','','',1,0,'F','0','0','agricultural:envdata:remove','#','admin',NOW(),'',NULL,'');
INSERT INTO `sys_menu` VALUES(2023,'妯℃嫙鐢熸垚',2002,4,'','','',1,0,'F','0','0','agricultural:envdata:generate','#','admin',NOW(),'',NULL,'');

-- 鎸夐挳鏉冮檺锛堥儴缃叉柟妗堬級
INSERT INTO `sys_menu` VALUES(2030,'鏂规鏌ヨ',2003,1,'','','',1,0,'F','0','0','agricultural:deploy:query','#','admin',NOW(),'',NULL,'');
INSERT INTO `sys_menu` VALUES(2031,'鏂规鐢熸垚',2003,2,'','','',1,0,'F','0','0','agricultural:deploy:add','#','admin',NOW(),'',NULL,'');
INSERT INTO `sys_menu` VALUES(2032,'鏂规鍒犻櫎',2003,3,'','','',1,0,'F','0','0','agricultural:deploy:remove','#','admin',NOW(),'',NULL,'');

-- 鎸夐挳鏉冮檺锛堣瘎璁猴級
INSERT INTO `sys_menu` VALUES(2040,'璇勮鏌ヨ',2005,1,'','','',1,0,'F','0','0','agricultural:comment:query','#','admin',NOW(),'',NULL,'');
INSERT INTO `sys_menu` VALUES(2041,'鍙戣〃璇勮',2005,2,'','','',1,0,'F','0','0','agricultural:comment:add','#','admin',NOW(),'',NULL,'');
INSERT INTO `sys_menu` VALUES(2042,'鍒犻櫎璇勮',2005,3,'','','',1,0,'F','0','0','agricultural:comment:remove','#','admin',NOW(),'',NULL,'');

-- 鎸夐挳鏉冮檺锛堥敊璇棩蹇楋級
INSERT INTO `sys_menu` VALUES(2050,'鏃ュ織鏌ヨ',2006,1,'','','',1,0,'F','0','0','agricultural:errorlog:query','#','admin',NOW(),'',NULL,'');

-- ----------------------------
-- 绠楁硶閰嶇疆鍒濆鏁版嵁
-- ----------------------------
INSERT INTO `agri_algorithm_config` (`algorithm_type`,`algorithm_name`,`param_json`,`description`,`status`,`create_by`,`create_time`) VALUES
('coverage','瑕嗙洊浼樺寲绠楁硶','{"coverageTarget":0.90,"sensorRadius":3.0,"maxIterations":1000}','鍩轰簬璐績绛栫暐鐨勭綉鏍艰鐩栦紭鍖栫畻娉曪紝閫愭鏀剧疆浼犳劅鍣ㄨ妭鐐圭洿鍒拌鐩栫巼杈惧埌鐩爣鍊硷紝鍑忓皯鐩戞祴鐩插尯銆?,'0','admin',NOW()),
('cluster','鍒嗙皣浼樺寲绠楁硶','{"clusterCount":5,"maxIterations":100,"energyThreshold":0.1}','鍩轰簬LEACH鍗忚鎬濇兂鐨勫垎绨囦紭鍖栫畻娉曪紝闅忔満閫変妇绨囧ご鑺傜偣锛岄潪绨囧ご鑺傜偣灏辫繎鍔犲叆绨囷紝鍧囪　鑺傜偣鑳借€椼€?,'0','admin',NOW());

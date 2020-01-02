package com.hik.practicedemo.model.constant;

/**
 * Created by wangJinChang on 2019/12/23 20:52
 * 缓存相关静态常量
 */
public interface CommonConstants {

    /* minion start */
    String BUCKET_NAME = "test";                    // 桶名

    String FILE_PREFIX = "pub/";                    // 文件夹名
    /* minion end */

    /* redis start */
    String QRC_INFO_CACHE = "qrc_info_";            // 二维码

    String LOGIN_TOKEN = "user_login_";             // 用户登录时cookie的key
    /* redis end */

    /* symbol start */
    String COLON = ":";                             // 冒号

    String BACK_SLASHES = "/";                      // 反斜杠
    /* symbol start */

    /* other start */

    /* other end */
}

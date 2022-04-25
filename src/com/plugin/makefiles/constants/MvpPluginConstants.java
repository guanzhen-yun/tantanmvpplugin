package com.plugin.makefiles.constants;
/**
 * mvp插件常量
 */
public class MvpPluginConstants {
    public static final String JAVA = "JAVA"; //两种语言 java/kotlin
    public static final String KOTLIN = "KOTLIN";
    public static final int ACTIVITY = 1; //两种类型 Activity/Fragment
    public static final int FRAGMENT = 2;

    //目前五个模块可生成Activity/Fragment
    public static final String MODULE_PT_COMMON = "putong-common";
    public static final String MODULE_ACCOUNT = "business_account";
    public static final String MODULE_CORE = "business_core";
    public static final String MODULE_FEED = "business_feed";
    public static final String MODULE_LIVE = "business_live";

    public static final int TYPE_CONSTRAINT = 0; //约束布局
    public static final int TYPE_LINEAR = 1; //线性布局
    public static final int TYPE_RELATIVE = 2; //相对布局
    public static final int TYPE_FRAME = 3; //帧布局
    public static final int TYPE_OTHER = -1; //其他 用户自定义
}

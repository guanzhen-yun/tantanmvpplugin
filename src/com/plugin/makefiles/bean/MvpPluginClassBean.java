package com.plugin.makefiles.bean;

import java.util.List;

/**
 * class实体类
 */
public class MvpPluginClassBean {
    public String packageStr; //package包名
    public List<String> importList; //导包
    public String className; //类名
    public List<String> args; //成员参数
    public List<MvpPluginMethodBean> methodList; //方法
}

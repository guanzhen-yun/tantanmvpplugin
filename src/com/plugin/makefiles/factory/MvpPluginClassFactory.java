package com.plugin.makefiles.factory;

import com.plugin.makefiles.bean.MvpPluginClassBean;
import com.plugin.makefiles.bean.MvpPluginMethodBean;
import com.plugin.makefiles.data.MvpPluginData;
import com.plugin.makefiles.utils.MvpPluginUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * mvp生成工厂--抽象
 */
public abstract class MvpPluginClassFactory {
    public static final int TYPE_ACT = 0;
    public static final int TYPE_FRAG = 1;
    public static final int TYPE_PRESENTER = 2;
    public static final int TYPE_VIEWMODEL = 3;

    protected FileWriter writer;
    protected MvpPluginClassBean bean;

    public static MvpPluginClassFactory getInstance(int type) {
        if (type == TYPE_ACT) {
            return new MvpPluginActFactory();
        } else if (type == TYPE_FRAG) {
            return new MvpPluginFragFactory();
        } else if (type == TYPE_PRESENTER) {
            return new MvpPluginPresenterFactory();
        } else if (type == TYPE_VIEWMODEL) {
            return new MvpPluginViewModelFactory();
        }
        throw new RuntimeException("参数类型错误");
    }

    public void build(FileWriter writer) {
        makeClass();
        setWriter(writer);
        buildClass();
    }

    private void makeClass() {
        bean = new MvpPluginClassBean();
        bean.packageStr = "package " + MvpPluginData.packagePath + MvpPluginData.endStr;
        bean.importList = getImportList();
        bean.className = getClassName();
        bean.args = getArgs();
        bean.methodList = getMethodList();
    }

    private void setWriter(FileWriter writer) {
        this.writer = writer;
    }

    /**
     * 构建类
     */
    private void buildClass() {
        addPackage();
        addImports();
        MvpPluginUtils.addDesc(writer);
        addClassName();
        addArgs();
        addMethods();
        addEnd();
    }

    abstract String getClassName();

    abstract List<String> getImportList();

    abstract String getClassNameLineStr();

    abstract List<String> getArgs();

    abstract List<MvpPluginMethodBean> getMethodList();

    /**
     * 添加包名
     */
    private void addPackage() {
        MvpPluginUtils.addWriterStr(writer, bean.packageStr, false);
        MvpPluginUtils.addBlankLine(writer, true);
    }

    /**
     * 添加导包
     */
    private void addImports() {
        int importSize = bean.importList.size();
        for (int i = 0; i < importSize; i++) {
            MvpPluginUtils.addWriterStr(writer, bean.importList.get(i), i < importSize - 1);
        }
        MvpPluginUtils.addBlankLine(writer, true);
    }

    /**
     * 添加类名
     */
    private void addClassName() {
        MvpPluginUtils.addWriterStr(writer, getClassNameLineStr(), false);
        MvpPluginUtils.addBlankLine(writer, true);
    }

    /**
     * 添加全局变量
     */
    private void addArgs() {
        if (bean.args != null) {
            List<String> args = bean.args;
            for (int i = 0; i < args.size(); i++) {
                String arg = args.get(i);
                MvpPluginUtils.addWriterStr(writer, arg, i < args.size() - 1);
            }
            MvpPluginUtils.addBlankLine(writer, true);
        }
    }

    /**
     * 添加方法
     */
    private void addMethods() {
        List<MvpPluginMethodBean> methodList = bean.methodList;
        for (int i = 0; i < methodList.size(); i++) {
            MvpPluginMethodBean methodBean = methodList.get(i);
            List<String> listContent = methodBean.contents;
            for (int j = 0; j < listContent.size(); j++) {
                MvpPluginUtils.addWriterStr(writer, listContent.get(j), j < listContent.size() - 1);
            }
            MvpPluginUtils.addBlankLine(writer, i < methodList.size() - 1);
        }
    }

    /**
     * 结束
     */
    private void addEnd() {
        try {
            MvpPluginUtils.addWriterStr(writer, "}", false);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static String getEndNewString(String str) {
        return str + MvpPluginData.endStr;
    }
}

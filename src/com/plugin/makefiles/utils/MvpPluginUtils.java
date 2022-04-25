package com.plugin.makefiles.utils;

import com.intellij.openapi.ui.Messages;
import com.plugin.makefiles.constants.MvpPluginConstants;
import com.plugin.makefiles.data.MvpPluginData;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * mvp插件工具类
 */
public class MvpPluginUtils {
    private static void showWarnDialog(String title, String content) {
        Messages.showWarningDialog(content, title);
    }

    private static void showWarnDialog(String content) {
        showWarnDialog("创建失败", content);
    }

    public static void print(String name, String content) {
        showWarnDialog(name, content);
    }

    public static void print(String content) {
        showWarnDialog(content);
    }

    public static void checkFileExist(String classPath) {
        File f = new File(classPath);
        if (f.exists()) {
//            print("老文件存在，删除");
            f.delete();
        }
    }

    /**
     * 插入一个空行
     */
    public static void addBlankLine(FileWriter writer, boolean isBlankLine) {
        try {
            writer.write("\n");
            if (isBlankLine) {
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 插入一行数据
     */
    public static void addWriterStr(FileWriter writer, String str, boolean isNeedNewLine) {
        try {
            writer.write(str);
            if (isNeedNewLine) {
                addBlankLine(writer, false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取class名字 包含后缀
     */
    public static String getClassName() {
        String[] arrayOfStrings = MvpPluginData.classPath.split(File.separator);
        return arrayOfStrings[arrayOfStrings.length - 1];
    }

    /**
     * 获取class类型
     */
    public static String getClassType() {
        if (getClassName().endsWith(".java")) {
            return MvpPluginConstants.JAVA;
        } else if (getClassName().endsWith(".kt")) {
            return MvpPluginConstants.KOTLIN;
        }
        return "";
    }

    /**
     * 获取类的真实名称
     */
    public static String getClassRealName() {
        return getClassName().split("\\.")[0];
    }

    /**
     * 判断Activity是否是Activity
     */
    public static int getClassAlign() {
        if (MvpPluginData.cName.endsWith("Activity") || MvpPluginData.cName.endsWith("Act")) {
            return MvpPluginConstants.ACTIVITY;
        } else if (MvpPluginData.cName.endsWith("Fragment") || MvpPluginData.cName.endsWith("Frag")) {
            return MvpPluginConstants.FRAGMENT;
        } else {
            return -1;
        }
    }

    //获取模块类型
    public static String getModuleType() {
        if (MvpPluginData.classPath.contains("business" + File.separator + "b_account")) {
            return MvpPluginConstants.MODULE_ACCOUNT;
        } else if (MvpPluginData.classPath.contains("business" + File.separator + "b_core")) {
            return MvpPluginConstants.MODULE_CORE;
        } else if (MvpPluginData.classPath.contains("business" + File.separator + "b_feed")) {
            return MvpPluginConstants.MODULE_FEED;
        } else if (MvpPluginData.classPath.contains("business" + File.separator + "b_live")) {
            return MvpPluginConstants.MODULE_LIVE;
        } else if (MvpPluginData.classPath.contains("putong-common")) {
            return MvpPluginConstants.MODULE_PT_COMMON;
        }
        return "";
    }

    /**
     * 获取class缩写 去掉Activity 和Fragment的标识
     */
    public static String getClassShortName() {
        if (MvpPluginData.cName.endsWith("Activity")) {
            return getShortName(MvpPluginData.cName, "Activity");
        } else if (MvpPluginData.cName.endsWith("Act")) {
            return getShortName(MvpPluginData.cName, "Act");
        } else if (MvpPluginData.cName.endsWith("Frag")) {
            return getShortName(MvpPluginData.cName, "Frag");
        } else if (MvpPluginData.cName.endsWith("Fragment")) {
            return getShortName(MvpPluginData.cName, "Fragment");
        }
        return "";
    }

    static String getShortName(String str, String endStr) {
        return str.substring(0, str.length() - endStr.length());
    }

    /**
     * 获取布局文件名称
     */
    public static String getLayoutName() {
        return (getClassAlign() == MvpPluginConstants.ACTIVITY ? "activity" : "fragment") + humpToLine(getClassShortName());
    }

    /**
     * 驼峰转下划线
     */
    static String humpToLine(String str) {
        Pattern pattern = Pattern.compile("[A-Z]");
        Matcher matcher = pattern.matcher(str);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 获取manifest文件路径
     */
    public static String getManifestPath() {
        String manifestName = "AndroidManifest.xml";
        String totalPath = "";
        if (getModuleType().equals(MvpPluginConstants.MODULE_LIVE)) { //直播模块特殊处理
            String beforeStr = MvpPluginData.classPath.split("business" + File.separator + "b_live" + File.separator)[0];
            totalPath = beforeStr + "business" + File.separator + "b_live" + File.separator + "src" + File.separator + "main";
        } else {
            String beforeStr = MvpPluginData.classPath.split(File.separator + "main" + File.separator + "java")[0];
            totalPath = beforeStr + File.separator + "main";
        }
        File manifestFile = new File(totalPath, manifestName);
        if (manifestFile.exists()) {
            return totalPath;
        } else {
            return "";
        }
    }

    /**
     * 获取包名
     */
    public static String getPackageName() throws IOException {
        String manifestName = "AndroidManifest.xml";
        RandomAccessFile raf = new RandomAccessFile(getManifestPath() + File.separator + manifestName, "rw");
        String line = null;
        while ((line = raf.readLine()) != null) {
            if (line.contains("package=\"")) {
                break;
            }
        }
        int size = line.split("\"").length; //  package="com.p1.mobile.putong.core">
        if (size > 1) {
            return line.split("\"")[1];
        }
        return "";
    }

    static String getPackageName(String manifestPath) throws Exception {
        String manifestName = "AndroidManifest.xml";
        RandomAccessFile raf = new RandomAccessFile(manifestPath + File.separator + manifestName, "rw");
        String line = null;
        while ((line = raf.readLine()) != null) {
            if (line.contains("package=\"")) {
                break;
            }
        }
        int size = line.split("\"").length;//  package="com.p1.mobile.putong.core">
        if (size > 1) {
            return line.split("\"")[1];
        }
        return "";
    }

    /**
     * 获取导入的包路径
     */
    public static String getPackagePath() {
        String packagePath = "com" + MvpPluginData.classPath.split("java" + File.separator + "com")[1].replace(File.separator + getClassName(), "").replace(File.separator, ".");
        if (MvpPluginData.isNewPackage) {
            packagePath = packagePath + "." + getClassShortName().toLowerCase();
        }
        return packagePath;
    }

    /**
     * 获取父类
     */
    public static String getParentClassName() {
        if (getClassAlign() == MvpPluginConstants.ACTIVITY && MvpPluginData.isMvp) {
            return "PutongMvpAct";
        } else if (getClassAlign() == MvpPluginConstants.FRAGMENT && MvpPluginData.isMvp) {
            return "PutongMvpFrag";
        } else if (getClassAlign() == MvpPluginConstants.ACTIVITY) {
            return "PutongAct";
        } else if (getClassAlign() == MvpPluginConstants.FRAGMENT) {
            return "PutongFrag";
        }
        return "";
    }

    /**
     * 添加注释说明
     */
    public static void addDesc(FileWriter writer) {
        addWriterStr(writer, "/**", true);
        addWriterStr(writer, " * TODO 类描述信息", true);
        addWriterStr(writer, " */", true);
    }

    public static String getPresenterName() {
        return getClassShortName() + "Presenter";
    }

    public static String getViewModelName() {
        return getClassShortName() + "ViewModel";
    }

    public static File makeNewFile(String fileName) throws IOException {
        String classNewPath = getClassNewPath();
        File newFile = new File(classNewPath, fileName);
        if (newFile.exists()) {
            newFile.delete();
        }
        newFile.createNewFile(); //创建新的文件
        return newFile;
    }

    public static File makeNewXmlFile() throws IOException {
        String layoutPath = getLayoutFilePath();
//        print("layoutPath", layoutPath);
        File file = new File(layoutPath, getLayoutName() + ".xml");
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        return file;
    }

    public static boolean checkXmlIsValid() throws Exception {
        if (MvpPluginData.rootViewType == MvpPluginConstants.TYPE_OTHER && "".equals(MvpPluginData.selfRootName)) {
            print("请输入自定义的根布局名字");
            return false;
        }
        String rootName = getRootName();
        if ("".equals(rootName)) {
            print("根布局类型暂不支持");
            return false;
        }
//        print("rootName", rootName);
        String folder = getFolder();
        if (null == folder) {
            print("获取holder的folder失败");
            return false;
        }
//        print("folder", folder);
        return true;
    }

    /**
     * 获取xml里面的folder 可能为空 为空则不需配置folder和flatten
     */
    public static String getFolder() throws Exception {
        String prefix = getModulePrefix();
        if (!"".equals(prefix)) {
            String newPath = getClassNewPath().replace(MvpPluginData.buildSrcBuildDir, "").replace(File.separator, ".");
            if (newPath.contains(getPackageName() + "." + "ui")) {
                return "";
            } else if (newPath.contains(getPackageName() + ".")) {
                String folderAndHolder = newPath.split(getPackageName() + ".")[1];
                if (folderAndHolder.contains(".")) {
                    return folderAndHolder.split("\\.")[0];
                } else {
                    return "";
                }
            } else {
                return "";
            }
        } else if (getModuleType().equals(MvpPluginConstants.MODULE_LIVE)) {
            String moduleManifestPackageName = getLiveModulePackageName();
            String newPath = getClassNewPath().replace(MvpPluginData.buildSrcBuildDir, "").replace(File.separator, ".");
            if (newPath.contains(moduleManifestPackageName + ".")) {
                String folderAndHolder = newPath.split(moduleManifestPackageName + ".")[1];
                String folderStr = "";
                if (folderAndHolder.contains(".")) {
                    folderStr = folderAndHolder.split("\\.")[0];
                }
                return folderStr;
            } else {
                return "";
            }
        } else if (getModuleType().equals(MvpPluginConstants.MODULE_PT_COMMON)) {
            String folderStr = getClassNewPath().replace(File.separator, ".");
            String p = getPackageName().replace(".common", "");
            if (!folderStr.contains(p + ".")) return "";
            folderStr = folderStr.split(p + ".")[1];
            if (folderStr.startsWith("ui.")) {
                return "";
            } else if (folderStr.contains(".")) {
                return folderStr.split("\\.")[0];
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * 获取live模块Manifest的包名
     */
    private static String getLiveModulePackageName() throws Exception {
        String manifestName = "AndroidManifest.xml";
        String beforeStr = MvpPluginData.classPath.split(File.separator + "main" + File.separator + "java")[0];
        String totalPath = beforeStr + File.separator + "main";
        File manifestFile = new File(totalPath, manifestName);
        if (manifestFile.exists()) {
            return getPackageName(totalPath);
        } else {
            return "";
        }
    }

    /**
     * 获取布局的rootName
     */
    public static String getRootName() {
        if (isRoomType(MvpPluginConstants.TYPE_CONSTRAINT)) {
            return "androidx.constraintlayout.widget.ConstraintLayout";
        } else if (isRoomType(MvpPluginConstants.TYPE_FRAME)) {
            return "FrameLayout";
        } else if (isRoomType(MvpPluginConstants.TYPE_LINEAR)) {
            return "LinearLayout";
        } else if (isRoomType(MvpPluginConstants.TYPE_RELATIVE)) {
            return "RelativeLayout";
        }
        return MvpPluginData.selfRootName;
    }

    private static boolean isRoomType(int roomType) {
        return MvpPluginData.rootViewType == roomType;
    }

    /**
     * 获取新文件的文件路径
     */
    static String getClassNewPath() {
        String classNewPath = MvpPluginData.classPath.replace(File.separator + getClassName(), "");
        if (MvpPluginData.isNewPackage) {
            classNewPath = classNewPath + File.separator + getClassShortName().toLowerCase();
            File file = new File(classNewPath);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        return classNewPath;
    }

    /**
     * 获取holder.bat地址
     */
    static String getHolderBatPath() {
        if (getModuleType().equals(MvpPluginConstants.MODULE_PT_COMMON)) {
            return MvpPluginData.buildSrcBuildDir;
        } else {
            String path = MvpPluginData.classPath.replace(MvpPluginData.buildSrcBuildDir + File.separator, "");
            path = path.replace("business" + File.separator, "");
            return MvpPluginData.buildSrcBuildDir + File.separator + "business" + File.separator + path.split(File.separator)[0];
        }
    }

    //刷新profile文件
    public static void refreshProfile() throws IOException {
        String[] strings = new String[3];
        strings[0] = "/bin/sh";
        strings[1] = "-c";
        strings[2] = "source " + getHolderBatPath() + "/etc/profile";
        Runtime.getRuntime().exec(strings, null, new File(getHolderBatPath()));
    }

    static String getScalaPath() throws IOException {
        String scalaPath = getScalaPathByFilePath("/etc/profile");
        if ("".equals(scalaPath)) {
            scalaPath = getScalaPathByFilePath(getBashProfileRootPath() + "/.bash_profile");
        }
//        print("scalaPath", scalaPath);
        return scalaPath;
    }

    private static String getScalaPathByFilePath(String filePath) throws IOException {
        String scalaPath = "";
        File f = new File(filePath);
        if (f.exists()) {
            RandomAccessFile file = new RandomAccessFile(filePath, "r");
            String line = null;
            while ((line = file.readLine()) != null) {
                if (line.contains("scala") && line.contains("export PATH")) {
                    break;
                }
            }
            if (line != null && line.contains(":")) {
                scalaPath = line.split(":")[1];
                scalaPath = scalaPath.replace("\"", "");
            }
        }
        return scalaPath;
    }

    static String getBashProfileRootPath() {
        return "/Users/" + MvpPluginData.classPath.split("/Users/")[1].split(File.separator)[0];
    }

    /**
     * 生成holder
     */
    public static void runBat() throws IOException {
        String scalaPath = getScalaPath();
        if ("".equals(scalaPath)) {
            print("请配置scala环境变量!");
            return;
        }
        String[] comands;
        if (getModuleType().equals(MvpPluginConstants.MODULE_PT_COMMON)) {
            comands = new String[3];
            comands[0] = getBatString();
            comands[1] = "putong-common";
            if(!"".equals(MvpPluginData.onlyXmlPath)) {
                comands[2] = MvpPluginData.onlyXmlPath;
            } else {
                comands[2] = getLayoutFilePath() + File.separator + getLayoutName() + ".xml";
            }
        } else {
            comands = new String[2];
            comands[0] = getBatString();
            if(!"".equals(MvpPluginData.onlyXmlPath)) {
                comands[1] = MvpPluginData.onlyXmlPath;
            } else {
                comands[1] = getLayoutFilePath() + File.separator + getLayoutName() + ".xml";
            }
        }
        String[] environments = new String[1];
        environments[0] = "PATH=/usr/bin:/bin:" + scalaPath;
        Process p = Runtime.getRuntime().exec(comands, environments, new File(getHolderBatPath()));
        InputStream inputStream = p.getInputStream();
        byte[] buffer = new byte[2048];
        int readBytes = 0;
        StringBuilder stringBuilder = new StringBuilder();
        while ((readBytes = inputStream.read(buffer)) > 0) {
            stringBuilder.append(new String(buffer, 0, readBytes));
        }
//        print(stringBuilder.toString());
        inputStream.close();
        p.destroy();
    }

    /**
     * 获取布局文件路径
     */
    public static String getLayoutFilePath() {
        String beforePath = MvpPluginData.classPath.split("src" + File.separator + "main")[0];
        String layoutPath = beforePath + "src" + File.separator + "main" + File.separator + "res" + File.separator + "layout";
        return layoutPath;
    }

    static String getBatString() {
        if (!"".equals(getModulePrefix())) {
            return "./holders.bat";
        } else if (getModuleType().equals(MvpPluginConstants.MODULE_LIVE)) {
            return "./live_holders.bat";
        } else if (getModuleType().equals(MvpPluginConstants.MODULE_PT_COMMON)) {
            return "./holders.bat";
        } else {
            return "";
        }
    }

    static String getModulePrefix() {
        String prefix = "";
        if (getModuleType().equals(MvpPluginConstants.MODULE_CORE) || getModuleType().equals(MvpPluginConstants.MODULE_ACCOUNT) || getModuleType().equals(MvpPluginConstants.MODULE_FEED)) {
            prefix = getModuleType().replace("business_", "");
        }
        return prefix;
    }

    /**
     * 根据module类型获取holder类的包地址
     */
    public static String getHolderPathByModule() throws IOException {
        String prefix = getModulePrefix();
        if (!"".equals(prefix)) {
            return "com.p1.mobile.putong." + prefix + ".holder";
        } else if (getModuleType().equals(MvpPluginConstants.MODULE_LIVE)) {
            String str = getPackagePath().replace(getPackageName() + ".", "");
            str = getPackageName() + "." + str.split("\\.")[0] + ".holder";
            return str;
        } else if (getModuleType().equals(MvpPluginConstants.MODULE_PT_COMMON)) {
            String str = getPackageName().replace(".common", "") + ".holder";
            return str;
        }
        return "";
    }

    public static String getFilePrefix() {
        if (MvpPluginData.isJava()) {
            return ".java";
        } else {
            return ".kt";
        }
    }

    //删除文件夹及里面文件
    static void deleteFile(File file) {
        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()) {
            System.out.println("文件删除失败,请检查文件路径是否正确");
            return;
        }
        //取得这个目录下的所有子文件对象
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        for (File f : files) {
            //判断子目录是否存在子目录,如果是文件则删除
            if (f.isDirectory()) {
                deleteFile(f);
            } else {
                f.delete();
            }
        }
        //删除空文件夹  for循环已经把上一层节点的目录清空。
        file.delete();
    }

    //检查该Activity是否已经注册了
    public static boolean isRegistActivity() throws IOException {
        RandomAccessFile raf = new RandomAccessFile(getManifestPath() + File.separator + "AndroidManifest.xml", "r");
        boolean isExist = false;
        String classExtra = getPackagePath().replace(getPackageName(), "") + "." + MvpPluginData.cName;
        String line = null;
        while ((line = raf.readLine()) != null) {
            if (line.contains(classExtra)) {
                isExist = true;
                break;
            }
        }
        return isExist;
    }

    /**
     * 获取xml里面的holder名字
     */
    public static String getXmlHolder() throws Exception {
        String classNewPath = getClassNewPath();
//        print("classNewPath", classNewPath);
        String folder = getFolder();

        String prefix = getModulePrefix();
        if (!"".equals(prefix)) {
            String newPath = classNewPath.replace(MvpPluginData.buildSrcBuildDir, "").replace(File.separator, ".");
            if (newPath.contains(getPackageName() + "." + "ui")) {
                if (newPath.contains(getPackageName() + "." + "ui" + ".")) {
                    return newPath.split(getPackageName() + "." + "ui" + ".")[1] + "." + getViewModelName();
                } else {
                    return getViewModelName();
                }
            } else if (newPath.contains(getPackageName() + ".") && !"".equals(folder)) {
                if (newPath.contains(getPackageName() + "." + folder + ".")) {
                    return newPath.split(getPackageName() + "." + folder + ".")[1] + "." + getViewModelName();
                } else {
                    return getViewModelName();
                }
            } else {
                return "";
            }
        } else if (getModuleType().equals(MvpPluginConstants.MODULE_LIVE)) {
            String moduleManifestPackageName = getLiveModulePackageName();
            String newPath = classNewPath.replace(MvpPluginData.buildSrcBuildDir, "").replace(File.separator, ".");
            if (newPath.contains(moduleManifestPackageName + ".")) {
                String folderAndHolder = newPath.split(moduleManifestPackageName + ".")[1];
                if ("".equals(folder)) {
                    return folderAndHolder + "." + getViewModelName();
                } else {
                    return folderAndHolder.replace(folder + ".", "") + "." + getViewModelName();
                }
            } else if (newPath.contains(moduleManifestPackageName)) {
                return getViewModelName();
            } else {
                return "";
            }
        } else if (getModuleType().equals(MvpPluginConstants.MODULE_PT_COMMON)) {
            String holderStr = classNewPath.replace(File.separator, ".");
            String p = getPackageName().replace(".common", "");
            if (!holderStr.contains(p + ".")) {
                return "";
            }
            holderStr = holderStr.split(p + ".")[1];
            if ("".equals(folder)) {
                if (holderStr.contains("ui")) {
                    holderStr = holderStr.replace("ui.", "").replace("ui", "");
                } else {
                    return "";
                }
            } else {
                holderStr = holderStr.replace(folder + ".", "").replace(folder, "");
            }
            if ("".equals(holderStr)) {
                return getViewModelName();
            }
            return holderStr + "." + getViewModelName();
        } else {
            return "";
        }
    }
}

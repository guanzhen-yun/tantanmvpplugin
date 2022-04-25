package com.plugin.makefiles.data;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;
import com.plugin.makefiles.constants.MvpPluginConstants;
import com.plugin.makefiles.utils.MvpPluginUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * mvp当前操作的数据
 */
public class MvpPluginData {
    //类的绝对路径
    public static String classPath;
    //类的名字
    public static String className;
    //类的类型
    public static String classType;
    //类的名称
    public static String cName;
    //类是Activity还是Fragment
    public static int classAlign;
    //class名称缩写(去除Activity Fragment标识的)
    public static String classShortName;
    //获取布局文件名称
    public static String layoutName;
    //xml路径
    public static String layoutPath;
    //获取manifest文件的路径
    public static String manifestPath;
    //获取包名
    public static String packageName;
    //引入的包
    public static String packagePath;
    //布局根布局类型
    public static int rootViewType;
    // 自定义根布局名称
    public static String selfRootName;
    //是否只刷新view
    public static boolean isOnlyRunXml;

    public static boolean isNewPackage; //是否创建文件夹
    public static boolean isMvp; //是否创建presenter

    public static String endStr = ""; //结尾符号 kotlin 没有

    public static String buildSrcBuildDir;

    public static String onlyXmlPath;

    //Build->Prepare All Plugin Modules For Deployment
    public static boolean preAction(AnActionEvent e) {
        Project project = e.getProject();
        // /Users/guanzhen/IdeaProjects/NewMvpPlugin/.idea/misc.xml
        String projectFilePath = project.getProjectFilePath();
        //NewMvpPlugin
        String projectName = project.getName();
        // /Users/guanzhen/IdeaProjects/NewMvpPlugin
        buildSrcBuildDir = projectFilePath.split(projectName + File.separator)[0] + projectName;
        //工程的路径
//        MvpPluginUtils.print("projectPath", buildSrcBuildDir);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        PsiFile psiFileInEditor = PsiUtilBase.getPsiFileInEditor(editor, project);
        //JAVA
        String fileType = psiFileInEditor.getFileType().getName();
        //makefiles
        String fileCurrentPath = psiFileInEditor.getContainingDirectory().getName();
        PsiDirectory parent = psiFileInEditor.getParent();
        List<String> listPath = new ArrayList<>();
        while (parent != null && !parent.getName().equals(projectName)) {
            listPath.add(parent.getName());
            parent = parent.getParent();
        }
        //当前文件的名字 //MakeFilesAction.java
        className = psiFileInEditor.getName();
        // src/com/plugin/makefiles/
        StringBuilder sb = new StringBuilder();
        for (int i = listPath.size() - 1; i >= 0; i--) {
            sb.append(listPath.get(i));
            sb.append(File.separator);
        }
        //类的绝对路径
        classPath = buildSrcBuildDir + File.separator + sb.toString() + className;

//        MvpPluginUtils.print("classPath", classPath);
        //类的名字
//        className = MvpPluginUtils.getClassName();
//        MvpPluginUtils.print("className", className);
        //类的类型
        classType = MvpPluginUtils.getClassType(); //JAVA
        if(className.endsWith(".xml")) {
            isOnlyRunXml = true;
            onlyXmlPath = classPath;
            return true;
        }
        if (!isKotlin() && !isJava()) {
            MvpPluginUtils.print("操作文件类型有误，只能修改java或者kotlin的文件");
            return false;
        }
//        MvpPluginUtils.print("classType", classType);
        if (isJava()) {
            endStr = ";";
        }

        //类的名称 TestActivity
        cName = MvpPluginUtils.getClassRealName();
//        MvpPluginUtils.print("cName", cName);
        //类是Activity还是Fragment
        classAlign = MvpPluginUtils.getClassAlign();
        if (classAlign == -1) {
            MvpPluginUtils.print("该类命名有误，应为Activity或Fragment类型的文件");
            return false;
        }
//        MvpPluginUtils.print("classAlign", classAlign == 1 ? "Activity" : "Fragment");
        //模块类型
        String moduleType = MvpPluginUtils.getModuleType();
        if ("".equals(moduleType)) {
            MvpPluginUtils.print("获取模块类型失败，请核实");
            return false;
        }
//        MvpPluginUtils.print("moduleType", moduleType);
        //class名称缩写(去除Activity Fragment标识的)
        classShortName = MvpPluginUtils.getClassShortName();//Test
        if ("".equals(classShortName)) {
            MvpPluginUtils.print("获取class名称缩写失败");
            return false;
        }
//        MvpPluginUtils.print("classShortName", classShortName);
        //获取布局文件名称
        layoutName = MvpPluginUtils.getLayoutName();
//        MvpPluginUtils.print("layoutName", layoutName);
        //获取manifest文件的路径
        manifestPath = MvpPluginUtils.getManifestPath();
        if ("".equals(manifestPath)) {
            MvpPluginUtils.print("获取Manifest信息失败,确定Manifest文件是否存在");
            return false;
        }
//        MvpPluginUtils.print("manifestPath", manifestPath);
        //获取包名
        try {
            packageName = MvpPluginUtils.getPackageName();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        if ("".equals(packageName)) {
            MvpPluginUtils.print("获取包名失败,确定Manifest文件是否设置了包名信息");
            return false;
        }
//        MvpPluginUtils.print("packageName", packageName);

//        MvpPluginUtils.print("packagePath", packagePath);
        layoutPath = MvpPluginUtils.getLayoutFilePath();
        File file = new File(layoutPath, layoutName + ".xml");
        isOnlyRunXml = file.exists();
        return true;
    }

    static boolean isKotlin() {
        return classType.equals(MvpPluginConstants.KOTLIN);
    }

    public static boolean isJava() {
        return classType.equals(MvpPluginConstants.JAVA);
    }

    public static boolean isActivity() {
        return classAlign == MvpPluginConstants.ACTIVITY;
    }

    public static boolean isFragment() {
        return classAlign == MvpPluginConstants.FRAGMENT;
    }
}

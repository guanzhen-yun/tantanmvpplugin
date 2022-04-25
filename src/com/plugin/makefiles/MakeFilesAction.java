package com.plugin.makefiles;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.util.PairFunction;
import com.plugin.makefiles.data.MvpPluginData;
import com.plugin.makefiles.factory.MvpManifestFactory;
import com.plugin.makefiles.factory.MvpPluginClassFactory;
import com.plugin.makefiles.factory.MvpXmlFactory;
import com.plugin.makefiles.utils.MvpPluginUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自动生成文件
 */
public class MakeFilesAction extends AnAction {
    long time;
    @Override
    public void actionPerformed(AnActionEvent e) {
        time = System.currentTimeMillis();
        boolean isCanRun = MvpPluginData.preAction(e);
        if (!isCanRun) return;
        try {
            MvpPluginUtils.refreshProfile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //直接编译xml
        try {
            if (MvpPluginData.isOnlyRunXml) {//&& !"".equals(MvpPluginUtils.getXmlHolder())
                MvpPluginUtils.print("只编译xml", "确保该xml有绑定holder!");
                MvpPluginUtils.runBat();
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //展示dialog
        MvpDialog mvpDialog = new MvpDialog((hasPresenter, isCreatePack, xmlRootType, selRoot) -> {
            MvpPluginData.rootViewType = xmlRootType;
            MvpPluginData.isNewPackage = isCreatePack;
            MvpPluginData.isMvp = hasPresenter;
            MvpPluginData.selfRootName = selRoot;
            MvpPluginData.packagePath = MvpPluginUtils.getPackagePath();
            try {
                handleCreate();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        mvpDialog.setVisible(true);
    }

    private void handleCreate() throws Exception {
        //1.创建Activity/Fragment
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    createActivityOrFragment();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //2.创建ViewModel
        createViewModel();
        //3.创建Presenter
        if (MvpPluginData.isMvp) {
            createPresenter();
        }
        //4.创建布局文件
        createLayout();
        //5.编译bat文件生成holder
        if (!"".equals(MvpPluginUtils.getXmlHolder())) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        MvpPluginUtils.runBat();
                        javax.swing.SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                MvpShowSuccessDialog mvpShowSuccessDialog = new MvpShowSuccessDialog();
                                mvpShowSuccessDialog.setLabel("生成插件消耗时间" + (System.currentTimeMillis() - time)/ 1000 + "s");
                                mvpShowSuccessDialog.setVisible(true);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        //6.注册Activity
        registActivityManifest();
    }

    void registActivityManifest() throws Exception {
        if (MvpPluginUtils.isRegistActivity()) {
            MvpPluginUtils.print(MvpPluginData.cName + "已经存在不需要重复注册");
            return;
        }
        MvpManifestFactory manifestFactory = MvpManifestFactory.getInstance();
        manifestFactory.registActivity();
    }

    private void createLayout() throws Exception {
        File file = MvpPluginUtils.makeNewXmlFile();
        if (!MvpPluginUtils.checkXmlIsValid()) return;
        FileWriter writer = new FileWriter(file.getPath());
        MvpXmlFactory factory = MvpXmlFactory.getInstance();
        factory.build(writer);
    }

    private void createPresenter() throws Exception {
        String presenterName = MvpPluginUtils.getPresenterName();
        File newFile = MvpPluginUtils.makeNewFile(presenterName + MvpPluginUtils.getFilePrefix());
        FileWriter writer = new FileWriter(newFile.getPath());
        MvpPluginClassFactory factory = MvpPluginClassFactory.getInstance(MvpPluginClassFactory.TYPE_PRESENTER);
        factory.build(writer);
    }

    private void createViewModel() throws Exception {
        String viewModelName = MvpPluginUtils.getViewModelName();
        File newFile = MvpPluginUtils.makeNewFile(viewModelName + MvpPluginUtils.getFilePrefix());
        FileWriter writer = new FileWriter(newFile.getPath());
        MvpPluginClassFactory factory = MvpPluginClassFactory.getInstance(MvpPluginClassFactory.TYPE_VIEWMODEL);
        factory.build(writer);
    }

    private void createActivityOrFragment() throws Exception {
        MvpPluginUtils.checkFileExist(MvpPluginData.classPath);
        File newFile = MvpPluginUtils.makeNewFile(MvpPluginData.className);
        //写入数据
        FileWriter writer = new FileWriter(newFile.getPath());
        int type = MvpPluginData.isActivity() ? MvpPluginClassFactory.TYPE_ACT : MvpPluginClassFactory.TYPE_FRAG;
        MvpPluginClassFactory factory = MvpPluginClassFactory.getInstance(type);
        factory.build(writer);
    }
}

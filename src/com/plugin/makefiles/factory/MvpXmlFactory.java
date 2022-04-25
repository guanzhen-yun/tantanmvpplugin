package com.plugin.makefiles.factory;

import com.plugin.makefiles.data.MvpPluginData;
import com.plugin.makefiles.utils.MvpPluginUtils;

import java.io.*;

/**
 * mvp生成工厂--xml
 */
public class MvpXmlFactory {
    public static MvpXmlFactory getInstance() {
        return new MvpXmlFactory();
    }

    public static void build(FileWriter writer) {
        MvpPluginUtils.addWriterStr(writer, "<?xml version=\"1.0\" encoding=\"utf-8\"?>", true);
        MvpPluginUtils.addWriterStr(writer, "<" + MvpPluginUtils.getRootName() + " xmlns:android=\"http://schemas.android.com/apk/res/android\"", true);
        //防止子控件id附加上父控件的id
        try {
            if (!"".equals(MvpPluginUtils.getXmlHolder())) {
                MvpPluginUtils.addWriterStr(writer, "  flatten=\"true\"", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (!"".equals(MvpPluginUtils.getFolder())) {
                MvpPluginUtils.addWriterStr(writer, "  folder=\"" + MvpPluginUtils.getFolder() + "\"", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (!"".equals(MvpPluginUtils.getXmlHolder())) {
                MvpPluginUtils.addWriterStr(writer, "  holder=\"" + MvpPluginUtils.getXmlHolder() + "\"", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        MvpPluginUtils.addWriterStr(writer, "  android:layout_width=\"match_parent\"", true);
        MvpPluginUtils.addWriterStr(writer, "  android:layout_height=\"match_parent\">", false);
        MvpPluginUtils.addBlankLine(writer, true);
        MvpPluginUtils.addWriterStr(writer, "</" + MvpPluginUtils.getRootName() + ">", false);
        //结束
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

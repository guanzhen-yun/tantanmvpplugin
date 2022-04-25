package com.plugin.makefiles.factory;

import com.plugin.makefiles.data.MvpPluginData;
import com.plugin.makefiles.utils.MvpPluginUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * mvp生成工厂--Manifest
 */
public class MvpManifestFactory {
    public static MvpManifestFactory getInstance() {
        return new MvpManifestFactory();
    }

    /**
     * 注册Activity
     */
    public static void registActivity() throws IOException {
        RandomAccessFile raf = getManifestFile();
        String classExtra = MvpPluginData.packagePath.replace(MvpPluginUtils.getPackageName(), "");
        String line = null;
        long lastPoint = 0;
        boolean isNeedChange = false;
        while ((line = raf.readLine()) != null) {
            final long point = raf.getFilePointer();
            if (line.contains("<activity")) {
                isNeedChange = true;
            }
            if (line.contains("/>") && isNeedChange) {
                lastPoint = point;
                isNeedChange = false;
            }
            if (line.contains("</activity>")) {
                lastPoint = point;
            }
        }

        StringBuilder sb = new StringBuilder();
        boolean isStartEnd = false;
        raf.close();
        raf = getManifestFile();
        while ((line = raf.readLine()) != null) {
            final long point = raf.getFilePointer();
            if (isStartEnd) {
                sb.append("\n" + line);
            }
            if (lastPoint == point) {
                isStartEnd = true;
            }
        }
        raf.close();
        raf = getManifestFile();
        raf.seek(lastPoint);
        raf.writeBytes(addNewActivityInfo(classExtra));
        raf.writeBytes(sb.toString());
        raf.close();
    }

    static RandomAccessFile getManifestFile() throws FileNotFoundException {
        return new RandomAccessFile(MvpPluginData.manifestPath + File.separator + "AndroidManifest.xml", "rw");
    }

    /**
     * Activity注册信息
     */
    static String addNewActivityInfo(String classExtra) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("    <activity");
        sb.append("\n");
        sb.append("      android:name=\"");
        sb.append(classExtra);
        sb.append(".");
        sb.append(MvpPluginData.cName);
        sb.append("\"");
        sb.append("\n");
        sb.append("      android:configChanges=\"orientation|screenSize|screenLayout\"");
        sb.append("\n");
        sb.append("      android:screenOrientation=\"portrait\"");
        sb.append("\n");
        sb.append("      android:theme=\"@style/Theme.P1.Light.NoActionBar\" />");
        return sb.toString();
    }
}

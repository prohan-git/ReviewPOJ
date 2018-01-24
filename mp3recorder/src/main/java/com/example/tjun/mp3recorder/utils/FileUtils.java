package com.example.tjun.mp3recorder.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Created by shuyu on 2016/11/15.
 * 文件管理
 */

public class FileUtils {
    private static final String SD_PATH = Environment.getExternalStorageDirectory().getPath();
    private static final String DATA_PATH = Environment.getDataDirectory().getPath();
    private static final String SD_STATE = Environment.getExternalStorageState();
    public static final String NAME = "MP3Recoder";
    private static String LOG_TAG = "FileUtils";

    public static String getAppPath() {
        StringBuilder sb = new StringBuilder();
        if (SD_STATE.equals(android.os.Environment.MEDIA_MOUNTED)) {
            sb.append(SD_PATH);
        } else {
//            sb.append(DATA_PATH);

        }
        sb.append(File.separator);
        sb.append(NAME);
        sb.append(File.separator);
        return sb.toString();
    }

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else {
                String[] filePaths = file.list();
                for (String path : filePaths) {
                    deleteFile(filePath + File.separator + path);
                }
                file.delete();
            }
        }
    }

    /**
     *
     * @param filePath
     * @param newFile
     * @return
     */
    public static boolean rename(String filePath, File newFile) {
        //1.判断参数阈值
        if (filePath == null || newFile == null) {
            Log.e(LOG_TAG, "Rename: null parameter");
            return false;
        }

        try {
            //5.根据新路径得到File类型数据
            //8.得到原文件File类型数据
            File file = new File(filePath);
            boolean ret = file.renameTo(newFile);
            Log.i(LOG_TAG, "Rename---改名成功？ " + ((ret) ? "yes!" : "no!"));
            return ret;

        } catch (SecurityException e) {
            Log.e(LOG_TAG, "Fail to rename file," + e.toString());
        }
        return false;
    }
}

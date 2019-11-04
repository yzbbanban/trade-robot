package com.yzb.lingo.common.util;

import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 保存文件
 *
 * @author wangban
 * @date 10:11 2019/2/19
 */
public class SaveToFileUtil {

    /**
     * 将地址输出到磁盘
     *
     * @param message
     * @return
     */
    public static boolean outMessageToFile(String message, String path, String name) {

        // 将地址输出到磁盘
        // 输出路径
        String outPath = path;

        // 输出文件
        outPath = outPath + "/" + name;

        // 检查目录是否存在
        File dir = new File(outPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 如果文件夹未隐藏，则设置文件夹未隐藏
        //System.getProperty("user.dir")
        File backupDir = new File(path);
        if (!backupDir.isHidden()) {
            // R ： 只读文件属性。A：存档文件属性。S：系统文件属性。H：隐藏文件属性。
            String sets = "attrib +A \"" + backupDir.getAbsolutePath() + "\"";
            try {
                Runtime.getRuntime().exec(sets);
            } catch (IOException e) {
//                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }

        SimpleDateFormat format = new SimpleDateFormat("_yyyyMMdd_HH_mm_ss_SSS");
        // 输出路径
        outPath = outPath + "/" + name + format.format(new Date()) + ".txt";

        File summarizeFile = new File(outPath);
        OutputStream os = null;
        BufferedWriter out = null;
        try {
            os = new FileOutputStream(summarizeFile, false);
            out = new BufferedWriter(new OutputStreamWriter(os, Charset.forName("UTF-8")));
            out.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

}

package com.sys.monitor.util;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Author willis
 * @desc
 * @since 2020年02月27日 14:55
 */
public class FileUtil {

    public static void zip(File tgtZipFile, File sourceFile) {
        try {
            ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(tgtZipFile));
            //创建缓冲输出流
            BufferedOutputStream bos = new BufferedOutputStream(zipOutputStream);
            mkPackage(zipOutputStream, bos, sourceFile, sourceFile.getName());
            bos.close();
            zipOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打包文件
     *
     * @param zipOutputStream
     * @param bos
     * @param sourceFile
     * @param path            文件在zip包里的绝对路劲
     * @throws Exception
     */
    public static void mkPackage(ZipOutputStream zipOutputStream, BufferedOutputStream bos, File sourceFile, String path) throws Exception {
        String rootDir = path;
        if (sourceFile.isDirectory()) {
            //取出文件夹中的文件（或子文件夹）
            File[] flist = sourceFile.listFiles();
            zipOutputStream.putNextEntry(new ZipEntry(rootDir + File.separator));
            if (flist.length != 0) {
                //如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
                for (int i = 0; i < flist.length; i++) {
                    mkPackage(zipOutputStream, bos, flist[i], rootDir + File.separator + flist[i].getName());
                }
            }
        } else {
            ZipEntry zipEntry = new ZipEntry(rootDir);
            zipOutputStream.putNextEntry(zipEntry);
            FileInputStream fos = new FileInputStream(sourceFile);
            BufferedInputStream bis = new BufferedInputStream(fos);
            int tag;
            //将源文件写入到zip文件中
            while ((tag = bis.read()) != -1) {
                bos.write(tag);
            }
            bis.close();
            fos.close();
        }
    }
}

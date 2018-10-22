package com.ray.lib.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-10-22 11:10
 *  description : 
 */
public class UnZipUtil {

    /**
     * 解压文件，同步方法，请在子线程中使用
     * @param zipFilePath 压缩文件的绝对路劲
     * @throws Exception
     */
    public void unZip(String zipFilePath) throws Exception {
        byte[] buffer = new byte[1024];
        ZipInputStream zis = null;
        try {
            zis = new ZipInputStream(new FileInputStream(zipFilePath));
            ZipEntry zipEntry = zis.getNextEntry();
            while(zipEntry != null){
                String fileName = zipEntry.getName();
                File newFile = new File(StorageUtil.getDownloadDir() + fileName);
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                zipEntry = zis.getNextEntry();
            }
        } finally {
            if (zis != null) {
                zis.closeEntry();
                zis.close();
            }
        }
    }



}

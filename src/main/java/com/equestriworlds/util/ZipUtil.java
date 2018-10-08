/*
 * Decompiled with CFR 0_133.
 */
package com.equestriworlds.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
    private static void getFileList(List<String> fileList, String sourceFolder, File node) {
        if (node.isFile()) {
            fileList.add(ZipUtil.generateZipEntry(sourceFolder, node.getAbsoluteFile().toString()));
        }
        if (node.isDirectory()) {
            String[] subNote;
            for (String filename : subNote = node.list()) {
                ZipUtil.getFileList(fileList, sourceFolder, new File(node, filename));
            }
        }
    }

    private static String generateZipEntry(String sourceFolder, String file) {
        System.out.println(sourceFolder + " " + file);
        return file.substring(sourceFolder.length() + 1, file.length());
    }

    public static void ZipFolders(String sourceFolder, String zipFilename, List<String> folders, List<String> files) {
        block20 : {
            ZipOutputStream zipOutputStream = null;
            FileOutputStream fileOutputStream = null;
            FileInputStream fileInputStream = null;
            BufferedOutputStream bufferedOutputStream = null;
            ArrayList<String> fileList = new ArrayList<String>();
            byte[] buffer = new byte[2048];
            try {
                fileOutputStream = new FileOutputStream(zipFilename);
                bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                zipOutputStream = new ZipOutputStream(bufferedOutputStream);
                for (String file : files) {
                    fileList.add(ZipUtil.generateZipEntry(sourceFolder, new File(file).getAbsoluteFile().toString()));
                }
                for (String folder : folders) {
                    ZipUtil.getFileList(fileList, sourceFolder, new File(folder));
                }
                for (String file : fileList) {
                    int len;
                    ZipEntry entry = new ZipEntry(file);
                    zipOutputStream.putNextEntry(entry);
                    fileInputStream = new FileInputStream(sourceFolder + File.separator + file);
                    while ((len = fileInputStream.read(buffer)) > 0) {
                        zipOutputStream.write(buffer, 0, len);
                    }
                    fileInputStream.close();
                }
                zipOutputStream.flush();
                zipOutputStream.close();
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
            }
            catch (IOException e) {
                e.printStackTrace();
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    }
                    catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (bufferedOutputStream != null) {
                    try {
                        bufferedOutputStream.close();
                    }
                    catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (zipOutputStream != null) {
                    try {
                        zipOutputStream.close();
                    }
                    catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    }
                    catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (bufferedOutputStream == null) break block20;
                try {
                    bufferedOutputStream.close();
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static void UnzipToDirectory(String zipFilePath, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                ZipUtil.extractFile(zipIn, filePath);
            } else {
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }

    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[4096];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
}

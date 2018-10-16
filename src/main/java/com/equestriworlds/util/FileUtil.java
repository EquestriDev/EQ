package com.equestriworlds.util;

import com.equestriworlds.Main;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * IO: Copy, delete...
 */
public class FileUtil {
    private static void DeleteFolder(File folder) {
        if (!folder.exists()) {
            System.out.println("Delete target does not exist: " + folder);
            return;
        }
        File[] files = folder.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    FileUtil.DeleteFolder(f);
                    continue;
                }
                f.delete();
            }
        }
        folder.delete();
    }

    public static void CopyToDirectory(File file, String outputDirectory) {
        block14 : {
            FileInputStream fileInputStream = null;
            FileOutputStream fileOutputStream = null;
            FilterOutputStream bufferedOutputStream = null;
            BufferedInputStream bufferedInputStream = null;
            try {
                int size;
                fileInputStream = new FileInputStream(file);
                bufferedInputStream = new BufferedInputStream(fileInputStream);
                byte[] buffer = new byte[2048];
                fileOutputStream = new FileOutputStream(outputDirectory + "\\" + file.getName());
                bufferedOutputStream = new BufferedOutputStream(fileOutputStream, buffer.length);
                while ((size = bufferedInputStream.read(buffer, 0, buffer.length)) != -1) {
                    bufferedOutputStream.write(buffer, 0, size);
                }
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
                fileOutputStream.flush();
                fileOutputStream.close();
                bufferedInputStream.close();
                fileInputStream.close();
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
                if (bufferedInputStream != null) {
                    try {
                        bufferedInputStream.close();
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
                if (bufferedOutputStream == null) break block14;
                try {
                    bufferedOutputStream.close();
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static void logToFile(String message, String name) {
        try {
            Date dateFile;
            File saveTo;
            SimpleDateFormat dateFormatFile;
            File dataFolder = new File(((Main)Main.getPlugin(Main.class)).getDataFolder() + File.separator + "staffLogs");
            if (!dataFolder.exists()) {
                dataFolder.mkdir();
            }
            if (!(saveTo = new File(dataFolder, (dateFormatFile = new SimpleDateFormat("yy/MM/dd")).format(dateFile = new Date()).replaceAll("/", "-") + "~staffchat.txt")).exists()) {
                saveTo.createNewFile();
            }
            FileWriter fw = new FileWriter(saveTo, true);
            PrintWriter pw = new PrintWriter(fw);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            fw.write("[" + dateFormat.format(date) + "] " + name + " \u00bb " + message);
            fw.write(System.getProperty("line.separator"));
            pw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package amtt.epam.com.amtt.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author IvanBakach
 * @version on 22.09.2015
 */
public class ZipUtil {

    public static final int BUFFER = 8388608;//8 kilobyte buffer

    public static void zip(final String[] filePaths, final String zipFilePath) {
        try {
            BufferedInputStream origin;
            FileOutputStream dest = new FileOutputStream(zipFilePath);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            byte data[] = new byte[BUFFER];

            for (String _file : filePaths) {
                FileInputStream fi = new FileInputStream(_file);
                origin = new BufferedInputStream(fi, BUFFER);

                ZipEntry entry = new ZipEntry(_file.substring(_file.lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;

                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unzip(String zipFile, String targetLocation) {
        try {
            FileInputStream fin = new FileInputStream(zipFile);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry zipEntry;
            while ((zipEntry = zin.getNextEntry()) != null) {
                FileOutputStream fout = new FileOutputStream(targetLocation + zipEntry.getName());
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }

                zin.closeEntry();
                fout.close();

            }
            zin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package amtt.epam.com.amtt.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 @author Ivan_Bakach
 @version on 08.07.2015
 */

public class ReadLargeTextUtil extends Thread{
    private final File file;

    private final ArrayList<CharSequence> lines = new ArrayList<>();

    public ReadLargeTextUtil(File file) {
        this.file = file;
    }

    @Override
    public void run() {
        String line;
        try {
            BufferedReader br = new BufferedReader(new java.io.FileReader(file));
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            br.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public ArrayList<CharSequence> getLines() {
        return lines;
    }
}

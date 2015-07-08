package amtt.epam.com.amtt.util;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import amtt.epam.com.amtt.AmttApplication;
import amtt.epam.com.amtt.R;

/**
 * Created by Ivan_Bakach on 08.07.2015.
 */
public class ReadLargeTextUtil extends Thread{
    private final File file;

    private ArrayList<String> lines = new ArrayList<>();

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

    public ArrayList<String> getLines() {
        return lines;
    }
}

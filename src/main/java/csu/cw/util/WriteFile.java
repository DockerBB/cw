package csu.cw.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class WriteFile {

    public static void writeFile(String filePath, String content){
        try {
            FileWriter fw = new FileWriter(filePath, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

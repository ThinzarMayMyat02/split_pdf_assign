package sample;

import java.io.File;
import java.io.FileWriter;

public class CreateWriteTxt {
    static final String source = "C:/Users/maymyatthinzar/sample_pdf/source_folder/";

    public static void main(String[] args) {
        try {
            File f = new File(source,"sample.txt");
            System.out.println(f.createNewFile());
            FileWriter fw=new FileWriter(f);
            fw.write("blah blah blah blah blah blah blah blah blah ");
            fw.close();
            System.out.println("successfully writed!");
            System.out.println("to path :"+f.toPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

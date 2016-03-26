package grep;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Iskuskov on 26.03.2016.
 */
public class Main {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Запуск: java Grep pattern file(s)...");
            return;
        }

        String pattern = args[0];
        List<File> fileList = new ArrayList<>();
        for (int i = 1; i < args.length; ++i) {
            fileList.add(new File(args[i]));
        }

        try {
            IGrep myGrep = new ParallelGrep();
            System.out.print(myGrep.grep(pattern, fileList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

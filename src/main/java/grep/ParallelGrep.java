package grep;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Iskuskov on 26.03.2016.
 */
public class ParallelGrep implements IGrep {

    // Паттерн для поиска
    private String pattern;

    public StringBuffer grep(File f) throws IOException {
        String filename = f.getName();
        Stream<String> streamFromFiles = Files.lines(f.toPath());
        Collection<String> resultLines = streamFromFiles
                .parallel()
                .filter((s) -> s.contains(pattern))
                .collect(Collectors.toList());

        StringBuffer result = new StringBuffer();
        for (String resultLine: resultLines) {
            result.append(filename + ":" + resultLine + "\n");
        }
        return result;
    }

    public String grep(String pat, List<File> fileList) throws IOException {
        StringBuffer result = new StringBuffer();
        pattern = pat;
        for (File file: fileList) {
            result.append(grep(file));
        }
        return String.valueOf(result);
    }
}




//for (int i=1; i<args.length; i++) {
//        String filename = args[i];
//        //System.out.println("Searching in: " + filename);
//        try {
//        Stream<String> streamFromFiles = Files.lines(Paths.get(filename));
//        //Stream<String> streamFromFiles = Files.lines(Paths.get("testdir\\secondfile.txt"));
//        Collection<String> result = streamFromFiles
//        .parallel()
//        .filter((s) -> s.contains(que))
//        .collect(Collectors.toList());
//        // result can be 0, 1, or >
//        if (!result.isEmpty()) {
//        for (String r: result) {
//        System.out.println("File: " + filename + ", string: " + r);
//        }
//
//        }
//        } catch (IOException e) {
//        System.out.println("No such file: " + e.toString());
//        } catch (UncheckedIOException e) {
//        System.out.println("Cannot read file " + filename + ": check type of file and charset");
//        }
//        }

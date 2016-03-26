package grep;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Iskuskov on 26.03.2016.
 */
public interface IGrep {

    // Поиск по паттерну в данном файле
    public StringBuffer grep(File f) throws IOException;;

    // Поиск  по паттерну  в списке файлов
    public String grep(String pat, List<File> fileList) throws IOException;
}

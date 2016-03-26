package grep;

/**
 * Created by Iskuskov on 25.02.2016.
 */
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

public class NioGrep implements IGrep {

    // Декодер для UTF-8
    private static Charset charset = Charset.forName("UTF-8");
    private static CharsetDecoder decoder = charset.newDecoder();

    // Паттерн для разбиения по строкам
    private static Pattern linePattern = Pattern.compile("(.*)(\\r?\\n|$)", //(".*\r?\n|\\Z",
            Pattern.MULTILINE); //(".*\r?\n");


    // Паттерн для поиска
    private Pattern pattern;

    // Создание паттерна для поиска
    private void compile(String pat) {
        try {
            pattern = Pattern.compile(pat);
        } catch (PatternSyntaxException x) {
            System.err.println(x.getMessage());
            System.exit(1);
        }
    }

    // Используем linePattern для разбиения CharBuffer на строки
    // Применяем входной паттерн для поиска соотвествий
    private StringBuffer grep(File f, CharBuffer cb) {
        Matcher lm = linePattern.matcher(cb);   // Line matcher
        Matcher pm = null;                      // Pattern matcher
        int lines = 0;
        StringBuffer result = new StringBuffer();
        while (lm.find()) {
            lines++;
            CharSequence cs = lm.group(1);       // Текущая линия
            if (pm == null)
                pm = pattern.matcher(cs);
            else
                pm.reset(cs);
            if (pm.find()) {
                result.append(f.getName() + /*":" + lines +*/ ":" + cs + "\n");
                //System.out.print(f + ":" + lines + ":" + cs);
            }
            if (lm.end() == cb.limit())
                break;
        }
        return result;
    }

    // Поиск по паттерну в данном файле
    public StringBuffer grep(File f) throws IOException {
        StringBuffer result = new StringBuffer();

        // Открываем файл и получаем канал для потока
        FileInputStream fis = new FileInputStream(f);
        FileChannel fc = fis.getChannel();

        // Получаем размер файл и создаем буфер MappedByteBuffer
        long sz = fc.size();
        MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, sz);

        // Декодируем файл
        CharBuffer cb = decoder.decode(bb);
        decoder.onMalformedInput(CodingErrorAction.IGNORE);

        // Производим поиск
        result.append(grep(f, cb)); //cb

        // Закрываем канал и поток
        fc.close();

        // Результат поиска
        return result;
    }

    // Поиск в списке файлов
    public String grep(String pat, List<File> fileList)
            throws IOException {

        StringBuffer result = new StringBuffer();
        compile(pat);
        for (File file: fileList) {
            result.append(grep(file));
        }
        return String.valueOf(result);
    }
}
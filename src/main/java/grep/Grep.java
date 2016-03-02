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

public class Grep {

    // Декодер для UTF-8
    private static Charset charset = Charset.forName("UTF-8");
    private static CharsetDecoder decoder = charset.newDecoder();

    // Паттерн для разбиения по строкам
    private static Pattern linePattern = Pattern.compile(".*(\\r?\\n|$)", //(".*\r?\n|\\Z",
            Pattern.MULTILINE); //(".*\r?\n");


    // Паттерн для поиска
    private static Pattern pattern;

    // Создание паттерна для поиска
    private static void compile(String pat) {
        try {
            pattern = Pattern.compile(pat);
        } catch (PatternSyntaxException x) {
            System.err.println(x.getMessage());
            System.exit(1);
        }
    }

    // Используем linePattern для разбиения CharBuffer на строки
    // Применяем входной паттерн для поиска соотвествий
    private static StringBuffer grep(File f, CharBuffer cb) {
        Matcher lm = linePattern.matcher(cb);   // Line matcher
        Matcher pm = null;                      // Pattern matcher
        int lines = 0;
        StringBuffer result = new StringBuffer();
        while (lm.find()) {
            lines++;
            CharSequence cs = lm.group();       // Текущая линия
            if (pm == null)
                pm = pattern.matcher(cs);
            else
                pm.reset(cs);
            if (pm.find()) {
                result.append(f.getName() + ":" + lines + ":" + cs);
                //System.out.print(f + ":" + lines + ":" + cs);
            }
            if (lm.end() == cb.limit())
                break;
        }
        return result;
    }

    // Поиск по паттерну в данном файле
    private static StringBuffer grep(File f) throws IOException {
        StringBuffer result = new StringBuffer();

        // Открываем файл и получаем канал для потока
        FileInputStream fis = new FileInputStream(f);
        FileChannel fc = fis.getChannel();

        // Получаем размер файл и создаем буфер MappedByteBuffer
        int sz = (int)fc.size();
        MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, sz);

        // Декодируем файл
        CharBuffer cb = decoder.decode(bb);

        // Производим поиск
        result.append(grep(f, cb));

        // Закрываем канал и поток
        fc.close();

        // Результат поиска
        return result;
    }

    // Основной метод
    public static String grep(String pat, List<File> fileList)
            throws IOException {

        StringBuffer result = new StringBuffer();
        compile(pat);
        for (File file: fileList) {
            result.append(grep(file));
        }
        return String.valueOf(result);
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Запуск: java Grep pattern file(s)...");
            return;
        }

        String pattern = args[0];
        List<File> fileList = new ArrayList<File>();
        for (int i = 1; i < args.length; ++i) {
            fileList.add(new File(args[i]));
        }

        try {
            System.out.print(grep(pattern, fileList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
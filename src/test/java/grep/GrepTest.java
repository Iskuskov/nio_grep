package grep;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Iskuskov on 01.03.2016.
 */
public class GrepTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    // Temporary files.
    private File tempFile1;
    private File tempFile2;

    // Pattern
    private String pattern;

    // Result
    private String grepResult;

    @Before
    public void setUp() throws Exception {

        // Content
        final String content1 = "11abc\n"
                + "1abc2323\n" + "12121\n"
                + "abc\n" + "abcabc\n" + "sdsd\n";

        final String content2 = "abc\n"
                + "123\n" + "asdas\n"
                + "aabbbc\n" + "abc123";

        // Files
        tempFile1 = testFolder.newFile("temp1.txt");
        tempFile2 = testFolder.newFile("temp2.txt");

        // Write something to files.
        FileUtils.writeStringToFile(tempFile1, content1);
        FileUtils.writeStringToFile(tempFile2, content2);

        // Pattern
        pattern = "abc";

        grepResult = tempFile1.getName() + ":1:11abc\n"
                + tempFile1.getName() + ":2:1abc2323\n"
                + tempFile1.getName() + ":4:abc\n"
                + tempFile1.getName() + ":5:abcabc\n"
                + tempFile2.getName() + ":1:abc\n"
                + tempFile2.getName() + ":5:abc123";
    }

    @Test
    public void testGrepStandartSituation() throws IOException {

        List<File> fileList = new ArrayList<File>();
        fileList.add(tempFile1);
        fileList.add(tempFile2);

        // Verify the content
        Assert.assertEquals(grepResult, Grep.grep(pattern, fileList));

        // Note: File is guaranteed to be deleted after the test finishes.
    }

}
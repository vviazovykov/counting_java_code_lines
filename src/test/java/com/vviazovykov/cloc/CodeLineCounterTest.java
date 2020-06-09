package com.vviazovykov.cloc;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class CodeLineCounterTest {

    private static final String FILE_PATH = "src\\test\\java\\com\\vviazovykov\\cloc\\JavaTestFile.java";
    private static final String CODE_OF_JAVA_TEST_FILE_WITH_SIX_CODE_LINES =
            "package com.vviazovykov.cloc;\n" +
            "\n" +
            "/**\n" +
            " * Class for Test purpose\n" +
            " */\n" +
            "public class JavaTestFile {\n" +
            "\n" +
            "    /**\n" +
            "     * Method to return simple JavaTestFile class name\n" +
            "     *\n" +
            "     * @return simple name of JavaTestFile class\n" +
            "     */\n" +
            "    public String getClassName() {\n" +
            "        return JavaTestFile.class.getSimpleName();\n" +
            "    }\n" +
            "}"
            ;
    private static final String CODE_OF_JAVA_TEST_FILE_WITH_ZERO_CODE_LINES =
            "/**\n" +
            " * Class for Test purpose\n" +
            " */\n" +
            "\n" +
            "    /**\n" +
            "     * Method to return simple JavaTestFile class name\n" +
            "     *\n" +
            "     * @return simple name of JavaTestFile class\n" +
            "     */\n" +
            "    \n" +
            "    // Zero lines of code"
            ;

    // Class under the Test
    private CodeLineCounter CUT;

    @Before
    public void setUp() {
        CUT = new CodeLineCounter();
    }

    @Test
    public void getNumberOfLines_shouldReturnValidNumberOfCodeLinesIfJavaFileIsValid() throws IOException {
        // given
        File testedFileWithSixLinesOfCode =
                createJavaTestFile(FILE_PATH, CODE_OF_JAVA_TEST_FILE_WITH_SIX_CODE_LINES);
        // when
        int actualCount = CUT.getNumberOfLines(testedFileWithSixLinesOfCode);
        // then
        Assert.assertEquals( 6, actualCount);
    }

    @Test
    public void getNumberOfLines_shouldReturnZeroNumberOfCodeLinesIfJavaFileWithEmptyCodeLines() throws IOException {
        // given
        File testedFileWithZeroLinesOfCode =
                createJavaTestFile(FILE_PATH, CODE_OF_JAVA_TEST_FILE_WITH_ZERO_CODE_LINES);
        // when
        int actualCount = CUT.getNumberOfLines(testedFileWithZeroLinesOfCode);
        // then
        Assert.assertEquals( 0, actualCount);
    }

    private File createJavaTestFile(String filePath, String content) throws IOException {
        BufferedWriter output = null;
        File file = null;
        try {
            file = new File(filePath);
            output = new BufferedWriter(new FileWriter(file));
            output.write(content);
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {
            if ( output != null ) {
                output.close();
            }
        }
        return file;
    }

    @After
    public void tearDown() throws IOException {
        File testedFile = new File(FILE_PATH);
        Files.deleteIfExists(testedFile.toPath());
    }
}

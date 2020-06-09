package com.vviazovykov.cloc;

import java.io.File;
import java.util.Scanner;

/**
 * Runner of the application
 */
public class Runner {

    private static final String INDENT_STRING = "  ";
    private static final String ZERO_STRING = "0";
    private static final int INDENT_VALUE = 1;
    private static final String COLON_SEPARATOR = " : ";

    private final CodeLineCounter codeLineCounter;

    public Runner(CodeLineCounter codeLineCounter) {
        this.codeLineCounter = codeLineCounter;
    }

    public static void main(String[] args ) {

        boolean runApp = true;
        while (runApp) {
            System.out.println(
                    "\nPlease, provide full path to file or directory " +
                            "(ex: D:\\TEST_3\\counting_java_code_lines\\src) and press enter. " +
                            "To exit, press \"0\":\n"
            );
            Scanner scanner = new Scanner(System.in);
            String fileOrDirectoryPath = scanner.nextLine();
            if (fileOrDirectoryPath == null || fileOrDirectoryPath.isEmpty()) {
                continue;
            } else if (fileOrDirectoryPath.equals(ZERO_STRING)) {
                runApp = false;
            } else {
                startCountNumberOfLines(fileOrDirectoryPath);
            }
        }
    }

    private static void startCountNumberOfLines(String fileOrDirectoryPath) {

        CodeLineCounter codeLineCounter = new CodeLineCounter();
        Runner runner = new Runner(codeLineCounter);

        File file = new File(fileOrDirectoryPath);
        System.out.println("Number of java file code lines is:");
        System.out.println(file.getName() + COLON_SEPARATOR + runner.getNumberOfLines(file));
        runner.fileTree(file, INDENT_VALUE);
    }

    private void fileTree(File rootFolder, int indent) {

        int temp;
        for (File file : rootFolder.listFiles()) {

            for (int i = 0; i < indent; i++) {
                System.out.print(INDENT_STRING);
            }

            temp = indent;
            if (file.isDirectory()) {
                indent++;
                System.out.println(file.getName() + COLON_SEPARATOR + getNumberOfLines(file));

                fileTree(file, indent);
                indent--;

            } else if (file.isFile()) {
                System.out.println(file.getName() + COLON_SEPARATOR + getNumberOfLines(file));
                indent = temp;
            }
        }
    }

    private int getNumberOfLines(File file) {
        return codeLineCounter.getNumberOfLines(file);
    }
}

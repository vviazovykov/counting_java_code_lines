package com.vviazovykov.cloc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Runner of the application
 *
 */
public class Runner
{
    public Runner() throws IOException {
    }

    public static void main(String[] args ) {

        String fileName = "D:\\TEST_3\\counting_java_code_lines\\src\\main\\java\\com\\vviazovykov\\cloc\\TestClass.java";

        CodeLineCounter lineCounter = new CodeLineCounter();

        int lineNumber = lineCounter.getNumberOfLines(fileName);
        System.out.println("lineNumber=" + lineNumber);


        Path start = Paths.get("D:\\TEST_3\\counting_java_code_lines\\src");
        List<String> collect = null;
        try (Stream<Path> stream = Files.walk(start, Integer.MAX_VALUE)) {
            collect = stream
                    .map(String::valueOf)
                    .sorted()
                    .collect(Collectors.toList());

            collect.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("!!!!!!!!!!!!!!!!!!!!!");

        System.out.println("333333333333333333");

        fileTree(new File("D:\\TEST_3\\counting_java_code_lines\\src"), 0);

        //listDirectory("D:\\TEST_3\\counting_java_code_lines\\src", 0);

        /*Map<String, Integer> map = collect.stream().collect(Collectors.toMap(Function.identity(), CodeLineCounter::getNumberOfLines));

        map.forEach((k, v) -> System.out.println((k + " : " + v)));

        System.out.println("22222222222222222222222222222222");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }


        System.out.println("333333333333333333");

        fileTree(new File("D:\\TEST_3\\counting_java_code_lines\\src"), 0);*/

    }

    public static void fileTree(File mainfolder, int indent) {

        int temp;

        for (File file : mainfolder.listFiles()) {
            for(int i = 0; i < indent; i++) {
                System.out.print("  ");
            }
            temp = indent;
            if (file.isDirectory()) {

                indent++;
                System.out.println(file.getName() + " : " + CodeLineCounter.getNumberOfLines(file.getAbsolutePath()));

                fileTree(file, indent);
                indent--;

            } else if (file.isFile()) {

                System.out.println(file.getName() + " : " + CodeLineCounter.getNumberOfLines(file.getAbsolutePath()));
                indent = temp;
            }
        }
    }

    public static void listDirectory(String dirPath, int level) {
        File dir = new File(dirPath);
        File[] firstLevelFiles = dir.listFiles();
        if (firstLevelFiles != null && firstLevelFiles.length > 0) {
            for (File aFile : firstLevelFiles) {
                for (int i = 0; i < level; i++) {
                    System.out.print("\t");
                }
                if (aFile.isDirectory()) {
                    System.out.println("[" + aFile.getName() + "] : "  );
                    listDirectory(aFile.getAbsolutePath(), level + 1);
                } else {
                    System.out.println(aFile.getName());
                }
            }
        }
    }
}

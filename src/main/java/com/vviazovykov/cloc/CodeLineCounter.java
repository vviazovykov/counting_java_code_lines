package com.vviazovykov.cloc;

import java.io.*;

public class CodeLineCounter {

    private static final String EMPTY_STRING = "";
    private static final String DOUBLE_SLASH_STRING = "//";
    private static final String STAR_SLASH_STRING = "*/";
    private static final String SLASH_STAR_STRING = "/*";
    private static final String QUOTE_STRING = "\"";

    /**
     * Get number of code lines.
     *
     * @param file  - file or directory.
     * @return number of code lines.
     * @throws IOException
     */
    public int getNumberOfLines(File file) {

        int count = 0;

        File[] files;
        if (file.exists() && file.isDirectory()) {
            files = file.listFiles();
            for (File f : files) {
                if (f.isDirectory()) {
                    count = count + getNumberOfLines(f);
                } else {
                    count = count + getNumberOfFileLines(f);
                }
            }
        } else {
            count = count + getNumberOfFileLines(file);
        }
        return count;
    }

    private int getNumberOfFileLines(File file) {
        int count = 0;
        boolean commentStarted = false;
        String line = null;
        BufferedReader buffReader = null;
        try {
            buffReader = new BufferedReader(new FileReader(file));
            while ((line = buffReader.readLine()) != null) {
                line = line.trim();
                if (EMPTY_STRING.equals(line) || line.startsWith(DOUBLE_SLASH_STRING)) {
                    continue;
                }
                if (commentStarted) {
                    if (commentEnded(line)) {
                        line = line.substring(line.indexOf(STAR_SLASH_STRING) + 2).trim();
                        commentStarted = false;
                        if (EMPTY_STRING.equals(line) || line.startsWith(DOUBLE_SLASH_STRING)) {
                            continue;
                        }
                    } else
                        continue;
                }
                if (isSourceCodeLine(line)) {
                    count++;
                }
                if (commentStarted(line)) {
                    commentStarted = true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Checks if in the given line a comment has started and has not ended.
     *
     * @param line
     * @return true if a comment has started and has not ended, false otherwise.
     */
    private boolean commentStarted(String line) {
        // it returns true if line = /* */ /*
        // it returns false if line = /* */
        int index = line.indexOf(SLASH_STAR_STRING);
        if (index < 0) {
            return false;
        }
        int quoteStartIndex = line.indexOf(QUOTE_STRING);
        if (quoteStartIndex != -1 && quoteStartIndex < index) {
            while (quoteStartIndex > -1) {
                line = line.substring(quoteStartIndex + 1);
                int quoteEndIndex = line.indexOf("\"");
                line = line.substring(quoteEndIndex + 1);
                quoteStartIndex = line.indexOf("\"");
            }
            return commentStarted(line);
        }
        return !commentEnded(line.substring(index + 2));
    }

    /**
     * Checks if in the given line a comment has ended and no new comment has not started.
     *
     * @param line
     * @return true if comment has ended and no new comment has not started, false otherwise.
     */
    private boolean commentEnded(String line) {
        // it returns true if line = */ /* */
        // it returns false if line = */ /*
        int index = line.indexOf(STAR_SLASH_STRING);
        if (index < 0) {
            return false;
        } else {
            String subString = line.substring(index + 2).trim();
            if (EMPTY_STRING.equals(subString) || subString.startsWith(DOUBLE_SLASH_STRING)) {
                return true;
            }
            if(commentStarted(subString))
            {
                return false;
            }
            else
            {
                return true;
            }
        }
    }

    /**
     * This method will work only if we are sure that comment has not already started previously.
     * Hence, this method should be called only after {@link #commentStarted(String)} is called.
     * It does not worry if comment has started or not.
     *
     * @param line
     * @return true if there is any valid source code in the given input line, false otherwise.
     */
    private boolean isSourceCodeLine(String line) {
        line = line.trim();
        if (EMPTY_STRING.equals(line) || line.startsWith(DOUBLE_SLASH_STRING)) {
            return false;
        }
        if (line.length() == 1) {
            return true;
        }
        int index = line.indexOf(SLASH_STAR_STRING);
        if (index != 0) {
            return true;
        } else {
            while (line.length() > 0) {
                line = line.substring(index + 2);
                int endCommentPosition = line.indexOf(STAR_SLASH_STRING);
                if (endCommentPosition < 0) {
                    return false;
                }
                if (endCommentPosition == line.length() - 2) {
                    return false;
                } else {
                    String subString = line.substring(endCommentPosition + 2)
                            .trim();
                    if (EMPTY_STRING.equals(subString) || subString.indexOf(DOUBLE_SLASH_STRING) == 0) {
                        return false;
                    } else {
                        if (subString.startsWith(SLASH_STAR_STRING)) {
                            line = subString;
                            continue;
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

package com.github.benshi.mybatis;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class JavaKeywordsUtils {
    private JavaKeywordsUtils() {

    }

    // Java keywords that cannot be used as identifiers
    private static final Set<String> JAVA_KEYWORDS = new HashSet<>(Arrays.asList(
            "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else", "enum", "extends", "false", "final", "finally",
            "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface",
            "long", "native", "new", "null", "package", "private", "protected", "public", "return",
            "short", "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws",
            "transient", "true", "try", "void", "volatile", "while", "var", "record", "yield"));

    private static final Set<Character> NUMBER_KEYWORDS = new HashSet<>(Arrays.asList(
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));

    /**
     * Checks if the provided string is a Java keyword.
     * 
     * @param word the string to check
     * @return true if the string is a Java keyword, false otherwise
     */
    public static boolean isJavaKeyword(String word) {
        return JAVA_KEYWORDS.contains(word);
    }

    public static boolean startWithNumberKeyword(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }
        final char firstChar = word.charAt(0);
        return NUMBER_KEYWORDS.contains(firstChar);
    }
}

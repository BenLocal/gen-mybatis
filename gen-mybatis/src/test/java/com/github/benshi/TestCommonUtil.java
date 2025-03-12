package com.github.benshi;

import java.util.HashMap;
import java.util.Map;

public class TestCommonUtil {
    private TestCommonUtil() {
    }

    public static AutoGenMapperProcessor testMode() {
        AutoGenMapperProcessor processor = new AutoGenMapperProcessor();

        // Set test mode flag to avoid file operations
        Map<String, String> options = new HashMap<>();
        options.put("test.mode", "true");

        try {
            // Use reflection to set test options if needed
            java.lang.reflect.Field testModeField = AutoGenMapperProcessor.class.getDeclaredField("testMode");
            if (testModeField != null) {
                testModeField.setAccessible(true);
                testModeField.set(processor, true);
            }
        } catch (Exception e) {
            // Field might not exist, so we use the options approach as backup
        }

        return processor;
    }
}

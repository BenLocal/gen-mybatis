package com.github.benshi.mybatis;

import org.junit.Test;

import com.google.common.truth.Truth;
import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourceSubjectFactory;

public class AutoGenMapperProcessorTest {
    @Test
    public void testProcess() throws Exception {
        final AutoGenMapperProcessor processor = TestCommonUtil.testMode();
        com.google.common.truth.Truth.assert_().about(JavaSourceSubjectFactory.javaSource())
                .that(JavaFileObjects.forResource("com/github/benshi/TestEntity.java"))
                .processedWith(processor)
                .compilesWithoutError();
    }

    @Test(expected = Exception.class)
    public void testProcessFileNotFound() throws Exception {
        final AutoGenMapperProcessor processor = TestCommonUtil.testMode();
        Truth.assert_().about(JavaSourceSubjectFactory.javaSource())
                .that(JavaFileObjects.forResource("com/github/benshi/NonExistentFile.java"))
                .processedWith(processor)
                .compilesWithoutError();
    }

    @Test
    public void testProcessEmptyFile() throws Exception {
        final AutoGenMapperProcessor processor = TestCommonUtil.testMode();
        Truth.assert_().about(JavaSourceSubjectFactory.javaSource())
                .that(JavaFileObjects.forResource(
                        "com/github/benshi/EmptyEntity.java"))
                .processedWith(processor)
                .compilesWithoutError();
    }
}

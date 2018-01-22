package com.dgrabok.quoter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestsUtils {
    public static void executionExceptionTest(Runnable execution, Class expectedException, String expectedMessage) {
        boolean correct = false;

        try {
            execution.run();
        } catch (Exception e) {
            assertEquals("Incorrect exception class", expectedException, e.getClass());
            assertEquals("Incorrect exception message", expectedMessage, e.getMessage());
            correct = true;
        }

        assertTrue("Exception expected", correct);
    }
}

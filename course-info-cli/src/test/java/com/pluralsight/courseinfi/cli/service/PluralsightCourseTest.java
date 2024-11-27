package com.pluralsight.courseinfi.cli.service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class PluralsightCourseTest {

    @Test
    void durationInMinutes() {
        PluralsightCourse course =
                new PluralsightCourse("id","Test course", "00:05:37", "url", false);
                assertEquals(5, course.durationInMinutes());
    }

    @Test
    void durationInMinutesOverHour() {
        PluralsightCourse course =
                new PluralsightCourse("id","Test course", "01:08:37", "url", false);
        assertEquals(68, course.durationInMinutes());
    }

    @Test
    void durationInMinutesZero() {
        PluralsightCourse course =
                new PluralsightCourse("id","Test course", "00:00:00", "url", false);
        assertEquals(0, course.durationInMinutes());
    }

    // This is how to parametrize a test function
    @ParameterizedTest
    @CsvSource(
            // Each row contains for the first value the input and the second value the expected
           textBlock = """
            00:05:37, 5
            00:00:00, 0
            01:08:37, 68
            """
    )
    void durationInMinutestParametrized(String input, long expected) {
        PluralsightCourse course =
                new PluralsightCourse("id","Test course", input, "url", false);
        assertEquals(expected, course.durationInMinutes());
    }
}
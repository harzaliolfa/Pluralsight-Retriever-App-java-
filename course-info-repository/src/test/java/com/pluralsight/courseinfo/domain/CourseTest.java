package com.pluralsight.courseinfo.domain;

import org.junit.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CourseTest {
    @Test
    public void testEmptyIdCase(){
        assertThrows(IllegalArgumentException.class, () ->new Course("", "java", 5, "https://java.com", Optional.empty()) );
    }

    @Test
    public void testEmptyNameCase(){
        assertThrows(IllegalArgumentException.class, ( () -> new Course("@id1", "", 5, "https://java.com", Optional.of(""))));
    }

}
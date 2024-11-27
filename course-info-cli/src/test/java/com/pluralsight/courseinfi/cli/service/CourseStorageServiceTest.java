package com.pluralsight.courseinfi.cli.service;

import com.pluralsight.courseinfo.repository.CourseRepository;
import com.pluralsight.courseinfo.domain.Course;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CourseStorageServiceTest {

    @Test
    void storePluralSightCourse() throws SQLException {
        // we can't instantiate CourseRepository because we removed the public from the interface for protection purpose
        // so we introduced a mocked class just for testing purposes
        CourseRepository repository = new InMemoryCourseRepository();

        CourseStorageService courseStorageService = new CourseStorageService(repository);

        PluralsightCourse ps1 = new PluralsightCourse("1", "Title1", "01:00:00", "/url-1", false );

        courseStorageService.storePluralSightCourse(List.of(ps1));

        Course expected = new Course("1", "Title1",60 ,"https://app.pluralsight.com/url-1", Optional.empty());
        assertEquals(List.of(expected), repository.getAllCourses());

    }
    static class InMemoryCourseRepository implements CourseRepository{
        private final List<Course> courses = new ArrayList<>();
        @Override
        public void saveCourse(Course course){
            courses.add(course);

        }
        @Override
        public List<Course> getAllCourses(){
            return courses;
        }

        @Override
        public void addNotes(String id, String Notes) {
            throw new UnsupportedOperationException();
        }
    }
}
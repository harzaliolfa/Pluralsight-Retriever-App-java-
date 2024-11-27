package com.pluralsight.courseinfo.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import java.sql.SQLException;
import java.util.List;
import com.pluralsight.courseinfo.domain.Course;

public interface CourseRepository {
    static final Logger LOG = LoggerFactory.getLogger(CourseRepository.class);

    void saveCourse(Course course);
    List<Course> getAllCourses() throws SQLException;
    static  CourseRepository openCourseRepository(String databaseFile){

        return new CourseJdbcRepository(databaseFile);
    }

    void addNotes(String id, String Notes);

}

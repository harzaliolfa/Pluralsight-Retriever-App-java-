package com.pluralsight.courseinfi.cli.service;

import com.pluralsight.courseinfo.repository.CourseRepository;
import com.pluralsight.courseinfo.domain.Course;

import java.net.FileNameMap;
import java.util.List;
import java.util.Optional;

// Here we're turning a pluralsight course into a course

public class CourseStorageService {
    private static final String  PS_BASE_URL = "https://app.pluralsight.com";
    private final CourseRepository courseRepository;

    public CourseStorageService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void storePluralSightCourse(List<PluralsightCourse> psCourses){
        for (PluralsightCourse psCourse: psCourses){
            Course course = new Course(psCourse.id(),
                    psCourse.title(),
                    psCourse.durationInMinutes(),
                    PS_BASE_URL + psCourse.contentUrl(),
                    Optional.empty());
            courseRepository.saveCourse(course);
        }
    }
}

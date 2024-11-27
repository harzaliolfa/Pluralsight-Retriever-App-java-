package com.pluralsight;

import com.pluralsight.courseinfo.repository.CourseRepository;
import com.pluralsight.courseinfo.domain.Course;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.awt.*;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Path("/courses")
public class CourseResource {
    private static final Logger LOG = LoggerFactory.getLogger(CourseResource.class);
    private final CourseRepository courseRepository;

    public CourseResource(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Course> getCourses() {
        try {
            return courseRepository
                    .getAllCourses()
                    .stream()
                    .sorted(Comparator.comparing(Course::id))
                    .toList();

        } catch (SQLException e) {
            LOG.error("Failed to fetch courses", e);
                throw new NotFoundException("Unable to fetch courses at this time.");
        }
    }





}

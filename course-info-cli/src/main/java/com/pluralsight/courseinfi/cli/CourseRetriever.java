package com.pluralsight.courseinfi.cli;
import com.pluralsight.courseinfi.cli.service.CourseRetrieverService;
import com.pluralsight.courseinfi.cli.service.CourseStorageService;
import com.pluralsight.courseinfi.cli.service.PluralsightCourse;
import com.pluralsight.courseinfo.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.List;

import static java.util.function.Predicate.not;

public class CourseRetriever {
    private static final Logger LOG = LoggerFactory.getLogger(CourseRetriever.class);
    public static void main(String... args){
        LOG.info("Course Retriever starting");
        if(args.length == 0){
            LOG.warn("Please provide an author name first");
            return;
        }
        try{
            retrieveCourses(args[0]);
        }catch(Exception e){
            LOG.error("unexpected error", e);
        }

    }

    private static void retrieveCourses(String authorId) throws IOException, InterruptedException {
        LOG.info("Retrieving courses for author '{}'", authorId);
        CourseRetrieverService courseRetrieverService = new CourseRetrieverService();

        CourseRepository courseRepository = CourseRepository.openCourseRepository("./courses.db");

        CourseStorageService courseStorageService = new CourseStorageService(courseRepository);


        List<PluralsightCourse> coursesToStore = courseRetrieverService.getCoursesFor(authorId)
                        .stream()
                        .filter(not(PluralsightCourse::isRetired))
                        .toList();
        LOG.info("Retrieved the following {} courses {}" , coursesToStore.size(), coursesToStore);
        courseStorageService.storePluralSightCourse(coursesToStore);
        LOG.info("Courses successfully stored");
    }


}

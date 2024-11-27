package com.pluralsight.courseinfo.repository;

import com.pluralsight.courseinfo.domain.Course;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

// This class `CourseJdbcRepository` implements the `CourseRepository` interface,
// meaning it will provide implementations for any methods defined in `CourseRepository`.
class CourseJdbcRepository implements CourseRepository {

    // A constant string that holds the database URL template for connecting to an H2 database.
    // It includes a placeholder (`%s`) for the specific database file path, as well as settings:
    // - `AUTO_SERVER=TRUE`: Allows the database to be accessed by multiple processes.
    // - `INIT=RUNSCRIPT FROM './db_init.sql'`: Automatically runs an SQL script (`db_init.sql`)
    //   when the database is initialized, to set up initial tables or data.
    private static final String H2_DATABASE_URL =
            "jdbc:h2:file:%s;AUTO_SERVER=TRUE;INIT=RUNSCRIPT FROM './db_init.sql'";
    private static final String INSERT_COURSE = """
                MERGE INTO Courses ( id, name, duration, url)
                values (?,?,?,?)
            """;
    private static final String ADD_NOTES = """
                Update Courses SET notes = ?
                where id = ?
            """;

    // A final `DataSource` instance that represents the connection pool or connection
    // source to be used by this repository for accessing the database.
    private final DataSource dataSource;

    // Constructor for `CourseJdbcRepository`, which takes a `databaseFile` path as a parameter.
    // This path will specify the location of the H2 database file.
    public CourseJdbcRepository(String databaseFile) {

        // Creates a new `JdbcDataSource` instance specifically for H2 databases.
        JdbcDataSource jdbcDataSource = new JdbcDataSource();

        // Sets the URL of the `JdbcDataSource` to the H2 database URL.
        // The `formatted` method is used to insert the `databaseFile` path into the URL
        // template, finalizing the connection URL.
        jdbcDataSource.setURL(H2_DATABASE_URL.formatted(databaseFile));

        // Assigns the configured `JdbcDataSource` to the `dataSource` field, making it
        // accessible for database operations in this repository.
        this.dataSource = jdbcDataSource;
    }



    @Override
    public void saveCourse(Course course) {

        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(INSERT_COURSE);
            statement.setString(1, course.id());
            statement.setString(2, course.name());
            statement.setLong(3, course.duration());
            statement.setString(4, course.url());
            statement.execute();

        }catch(SQLException e){
            throw new RepositoryException("Failed to save" + course , e);
        }
    }

    @Override
    public List<Course> getAllCourses() {

        // Start a try-with-resources block to automatically close the connection after use
        try (Connection connection = dataSource.getConnection()) {

            // Create a statement object to send SQL queries to the database
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM COURSES");

            // Execute a SQL query to retrieve all records from the "COURSES" table
            ResultSet resultSet = statement.executeQuery();
            LOG.info("SQL Query executed result set :{}", resultSet);

            // Initialize an empty list to store Course objects
            List<Course> courses = new ArrayList<>();

            // Loop through each row in the result set
            while (resultSet.next()) {
                LOG.info("SQL Query executed result set :{}", resultSet.getString(4));

                // Create a Course object using data from the current row in the result set
                Course course = new Course(
                        resultSet.getString(1), // First column as the course ID
                        resultSet.getString(2), // Second column as the course name
                        resultSet.getLong(3),   // Third column as the course duration (e.g., hours)
                        resultSet.getString(4),// Fourth column as the course URL
                        Optional.ofNullable(resultSet.getString(5))
                );

                // Add the Course object to the list
                courses.add(course);
                LOG.info(" retrieved course added to courses ");

            }
            // Return an unmodifiable version of the courses list to prevent outside modification
            return Collections.unmodifiableList(courses);

        } catch (SQLException e) {
            // If a SQL exception occurs, wrap it in a custom RepositoryException and rethrow it
            throw new RepositoryException("Failed to retrieve courses", e);
        }
    }

    @Override
    public void addNotes(String id, String Notes) {
        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(ADD_NOTES);
            statement.setString(1, Notes);
            statement.setString(2, id);
            statement.execute();

        }catch(SQLException e){
            throw new RepositoryException("Failed to add note " + id , e);
        }
    }

}

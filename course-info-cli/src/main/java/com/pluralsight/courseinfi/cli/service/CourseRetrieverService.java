package com.pluralsight.courseinfi.cli.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class CourseRetrieverService {
    private static  final String PS_URI = "https://app.pluralsight.com/profile/data/author/%s/all-content";
    private static final HttpClient CLIENT = HttpClient
            .newBuilder()
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .build();

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public List<PluralsightCourse> getCoursesFor(String authorId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(PS_URI.formatted(authorId)))
                .GET()
                .build();
            try {
                HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
                return switch (response.statusCode()) {

                    // If the status code is 200 (OK), process the response body.
                    case 200 ->  toPluralsightCourses(response);
                    case 404 -> List.of();
                    default -> throw new RuntimeException("Pluralsight API call failed with status code:" + response.statusCode());
                };
            }catch(IOException | InterruptedException e){
                throw  new RuntimeException("Could not call Pluralsight API", e);

            }



    }

    private static List<PluralsightCourse> toPluralsightCourses(HttpResponse<String> response) throws JsonProcessingException {
        // Define the expected return type as a List of PluralsightCourse objects.
        // JavaType helps ObjectMapper know exactly what kind of collection it should map to (List of PluralsightCourse).
        //getTypeFactory() returns a TypeFactory instance that can be used to create complex type definitions.
        // These definitions are useful when converting JSON into Java objects, especially when dealing with nested structures (like collections).
        JavaType returnType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, PluralsightCourse.class);

        // Parse the JSON response body to a List of PluralsightCourse objects and return it.
        // the yield word is used when the switch element is returning some value
        return OBJECT_MAPPER.readValue(response.body(), returnType);
    }

}

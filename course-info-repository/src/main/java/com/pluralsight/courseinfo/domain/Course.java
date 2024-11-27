package com.pluralsight.courseinfo.domain;


import java.util.Optional;

// We created this record to present a course, okay why we did that while we already have a course record in the cli module,
// in fact that record is specific for courses we want to get from the pluralsight Api while we want to add courses from the outside of
// pluralsight api, more than that , the cli should have a dependency on the repository, but not the other way over
/* We used the Course  for domain-related logic and entities not tied to a specific API.*/
public record Course(String id, String name, long duration, String url, Optional<String> notes) {

    // One of the features of a record is that we can declare a compressed constructor where we don't need to specify
    // the parameters , they're already specified in the record signature
    public Course{
        filled(id);
        filled(name);
        filled(url);
        notes.ifPresent(Course::filled);
    }


    private static void filled(String s){
        if(s == null || s.isBlank()){
            throw new IllegalArgumentException("No correct value present");
        }
    }
}

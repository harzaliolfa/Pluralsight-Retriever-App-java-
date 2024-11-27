package com.pluralsight.courseinfi.cli.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Duration;
import java.time.LocalTime;


/*PluralsightCourse to handle Pluralsight-specific data and operations in the CLI.
This separation ensures modularity, reusability, and scalability in your application.*/
@JsonIgnoreProperties(ignoreUnknown = true)
public record PluralsightCourse(String id, String title, String duration, String contentUrl, boolean isRetired) {

    // This function takes a duration = "00:05:37" as a string and converts it to a time duration in minutes
    // LocalTime gives a time like a day hour 15pm for example but we need a duration like 15min so we converted the string duration
    // to time duration using localTime parse method and then we converted it into a normal duration not a day hour
    public long durationInMinutes(){
        return Duration.between(
                LocalTime.MIN,
                LocalTime.parse(duration()))
                .toMinutes();

    }
}

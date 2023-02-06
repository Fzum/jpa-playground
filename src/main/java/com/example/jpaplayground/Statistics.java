package com.example.jpaplayground;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.Month;
import java.util.concurrent.ThreadLocalRandom;

@Entity
public class Statistics {

  @Id
  @GeneratedValue
  private Long id;

  private LocalDate inSchoolSince;

  public Statistics() {
    LocalDate start = LocalDate.of(1989, Month.OCTOBER, 14);
    LocalDate end = LocalDate.now();
    inSchoolSince = between(start, end);
  }

  public static LocalDate between(LocalDate startInclusive, LocalDate endExclusive) {
    long startEpochDay = startInclusive.toEpochDay();
    long endEpochDay = endExclusive.toEpochDay();
    long randomDay = ThreadLocalRandom
        .current()
        .nextLong(startEpochDay, endEpochDay);

    return LocalDate.ofEpochDay(randomDay);
  }
}

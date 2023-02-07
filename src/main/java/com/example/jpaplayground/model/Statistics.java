package com.example.jpaplayground.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Statistics {

  @Id
  @GeneratedValue
  private Long id;

  private LocalDate inSchoolSince;

  public Statistics(LocalDate inSchoolSince) {
    this.inSchoolSince = inSchoolSince;
  }
}

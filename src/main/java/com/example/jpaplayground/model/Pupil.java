package com.example.jpaplayground.model;

import static jakarta.persistence.CascadeType.PERSIST;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Pupil {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  @JoinColumn
  private School school;

  @OneToOne(cascade = PERSIST)
  private Statistics statistics;

  @Builder
  public Pupil(LocalDate inSchoolSince) {
    statistics = new Statistics(inSchoolSince);
  }
}
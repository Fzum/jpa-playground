package com.example.jpaplayground;

import static jakarta.persistence.CascadeType.PERSIST;

import com.example.jpaplayground.data.School;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Setter;

@Entity
@Setter
public class Pupil {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  @JoinColumn
  private School school;

  @OneToOne(cascade = PERSIST)
  private Statistics statistics;

  public Pupil() {
    statistics = new Statistics();
  }
}
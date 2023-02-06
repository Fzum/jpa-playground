package com.example.jpaplayground.data;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.CascadeType.PERSIST;

import com.example.jpaplayground.Pupil;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class School {

  @Id
  @GeneratedValue
  private Long id;

  @OneToMany(mappedBy = "school", cascade = ALL)
  private Set<Pupil> pupils = new HashSet<>();

  public void addPupil(Pupil pupil) {
    pupils.add(pupil);
    pupil.setSchool(this);
  }
}


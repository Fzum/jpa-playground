package com.example.jpaplayground.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class School {

  @Id
  private Long id;

  @OneToMany(mappedBy = "school")
  private Set<Pupil> pupils = new HashSet<>();
}

@Entity
@NoArgsConstructor
class Pupil {

  @Id
  private Long id;

  @ManyToOne
  @JoinColumn
  private School school;
}

@Entity
@NoArgsConstructor
class Character {

  @Id
  private Long id;

  private String character;
}
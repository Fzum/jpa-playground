package com.example.jpaplayground.model;

import static jakarta.persistence.CascadeType.ALL;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class School {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  @OneToMany(mappedBy = "school", cascade = ALL)
  private Set<Pupil> pupils = new HashSet<>();

  public School addPupil(Pupil pupil) {
    pupils.add(pupil);
    pupil.setSchool(this);
    return this;
  }

  @Builder
  public School(String name) {
    this.name = name;
  }

  public static class Specifications {

    public static final Specification<School> groupBySchoolOrderByInSchoolSinceAsc = (root, cq, cb) -> {
      Join<Pupil, Statistics> statisticsOfPupil = root
          .join("pupils")
          .join("statistics");

      Expression<Number> minInSchoolSince = cb.min(statisticsOfPupil.get("inSchoolSince"));

      cq.multiselect(root, minInSchoolSince)
          .groupBy(root)
          .orderBy(cb.asc(minInSchoolSince));

      return cq.getRestriction();
    };


    public static final Specification<School> hasNameOneTwoOrThree = (root, query, builder) ->
        builder.or(root.get("name").in(List.of("1", "2", "3")));
  }


}


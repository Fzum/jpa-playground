package com.example.jpaplayground.data;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.jpaplayground.model.Pupil;
import com.example.jpaplayground.model.School;
import com.example.jpaplayground.model.School.Specifications;
import com.example.jpaplayground.model.Statistics;
import com.example.jpaplayground.repository.SchoolRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

// links:
// https://www.javatpoint.com/jpa-criteria-group-by-clause
// https://www.baeldung.com/hibernate-criteria-queries
// https://docs.oracle.com/javaee/7/tutorial/persistence-criteria003.htm

@DataJpaTest
public class SpecificationAndCriteriaApiTest {

  private @Autowired TestEntityManager testEntityManager;
  private @Autowired SchoolRepository schoolRepository;

  private static final int SCHOOL_COUNT = 5;
  private static final LocalDate BASE_LINE_DATE = LocalDate.of(2000, 1, 1);

  @BeforeEach
  void setUp() {
    IntStream.range(0, SCHOOL_COUNT)
        .forEach(i -> testEntityManager.persist(School.builder().name(String.valueOf(i)).build()
            .addPupil(Pupil.builder().inSchoolSince(BASE_LINE_DATE.plusYears(i + 1)).build())
            .addPupil(Pupil.builder().inSchoolSince(BASE_LINE_DATE.minusYears(i + 1)).build())));
  }

  @Nested
  class CriteriaBuilderApi {

    private @PersistenceContext EntityManager entityManager;

    @Test
    @DisplayName("group by and aggregate fn min with ordering asc")
    @Disabled
    void groupByAndAggregateFnMinWithOrderingAsc() {
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<Object[]> cq = cb.createQuery(
          Object[].class);
      Root<School> school = cq.from(School.class);

      Join<School, Pupil> pupilsOfSchool = school.join("pupils");
      Join<Pupil, Statistics> statisticsOfPupil = pupilsOfSchool.join("statistics");

      CriteriaQuery<Object[]> criteriaQuery = cq.multiselect(
              school,
              cb.min(statisticsOfPupil.get("inSchoolSince"))
          )
          .groupBy(school)
          .orderBy(cb.asc(cb.min(statisticsOfPupil.get("inSchoolSince"))));

      TypedQuery<Object[]> query = entityManager.createQuery(
          criteriaQuery);

      System.out.println(query.getResultList());
    }
  }

  @Nested
  class SpecificationApi {

    private final static Function<School, Stream<LocalDate>> getPupilsInSchoolSince = s -> s.getPupils()
        .stream()
        .map(a -> a.getStatistics().getInSchoolSince());

    @Test
    @DisplayName("should group by school and order by school having the pupil with the min inSchoolSince")
    void shouldGroupBySchoolAndOrderBySchoolHavingThePupilWithTheMinInSchoolSince() {
      // when
      List<School> schools = schoolRepository.findAll(
          School.Specifications.groupBySchoolOrderByInSchoolSinceAsc
      );

      // then
      assertThat(getPupilsInSchoolSince.apply(schools.get(0)))
          .anyMatch(l -> BASE_LINE_DATE.minusYears(SCHOOL_COUNT).equals(l));

      assertThat(getPupilsInSchoolSince.apply(schools.get(1)))
          .anyMatch(l -> BASE_LINE_DATE.minusYears(SCHOOL_COUNT - 1).equals(l));

      assertThat(getPupilsInSchoolSince.apply(schools.get(2)))
          .anyMatch(l -> BASE_LINE_DATE.minusYears(SCHOOL_COUNT - 2).equals(l));

      assertThat(getPupilsInSchoolSince.apply(schools.get(3)))
          .anyMatch(l -> BASE_LINE_DATE.minusYears(SCHOOL_COUNT - 3).equals(l));

      assertThat(getPupilsInSchoolSince.apply(schools.get(4)))
          .anyMatch(l -> BASE_LINE_DATE.minusYears(SCHOOL_COUNT - 4).equals(l));
    }

    @Test
    @DisplayName("should paginated group by school and order by school having the pupil with the min inSchoolSince")
    void shouldPaginatedGroupBySchoolAndOrderBySchoolHavingThePupilWithTheMinInSchoolSince() {
      // when
      Page<School> firstPage = schoolRepository.findAll(
          School.Specifications.groupBySchoolOrderByInSchoolSinceAsc,
          PageRequest.of(0, 2)
      );

      Page<School> secondPage = schoolRepository.findAll(
          School.Specifications.groupBySchoolOrderByInSchoolSinceAsc,
          PageRequest.of(1, 2)
      );

      Page<School> thirdPage = schoolRepository.findAll(
          School.Specifications.groupBySchoolOrderByInSchoolSinceAsc,
          PageRequest.of(2, 2)
      );

      // then
      List<School> firstTwoSchools = firstPage.getContent();

      assertThat(getPupilsInSchoolSince.apply(firstTwoSchools.get(0)))
          .anyMatch(l -> BASE_LINE_DATE.minusYears(SCHOOL_COUNT).equals(l));

      assertThat(getPupilsInSchoolSince.apply(firstTwoSchools.get(1)))
          .anyMatch(l -> BASE_LINE_DATE.minusYears(SCHOOL_COUNT - 1).equals(l));

      List<School> secondTwoSchools = secondPage.getContent();

      assertThat(getPupilsInSchoolSince.apply(secondTwoSchools.get(0)))
          .anyMatch(l -> BASE_LINE_DATE.minusYears(SCHOOL_COUNT - 2).equals(l));

      assertThat(getPupilsInSchoolSince.apply(secondTwoSchools.get(1)))
          .anyMatch(l -> BASE_LINE_DATE.minusYears(SCHOOL_COUNT - 3).equals(l));

      List<School> thirdSchool = thirdPage.getContent();

      assertThat(getPupilsInSchoolSince.apply(thirdSchool.get(0)))
          .anyMatch(l -> BASE_LINE_DATE.minusYears(SCHOOL_COUNT - 4).equals(l));
    }


    @Test
    @DisplayName("should group by school and order by school having the pupil with the min inSchoolSince and combine with specifications")
    void shouldGroupBySchoolAndOrderBySchoolHavingThePupilWithTheMinInSchoolSinceAndCombineWithSpecifications() {
      List<School> schools = schoolRepository.findAll(
          School.Specifications.groupBySchoolOrderByInSchoolSinceAsc
              .and(Specifications.hasNameOneTwoOrThree)
      );

      // then
      assertThat(schools).hasSize(3);
    }

  }


}
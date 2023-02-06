package com.example.jpaplayground.data;

import static org.assertj.core.api.Assertions.fail;

import com.example.jpaplayground.Pupil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

@DataJpaTest
public class SpecificationTest {

  private @Autowired TestEntityManager entityManager;

  private @Autowired SchoolRepository schoolRepository;

  @BeforeEach
  void setUp() {
    IntStream.range(0, 10).forEach(schoolIndex -> {
      var school = entityManager.persist(new School());
      IntStream.range(0, 100).forEach(pupilIndex -> {
            school.addPupil(new Pupil());
            entityManager.persist(school);
          }
      );
    });

    entityManager.flush();

  }

  @Test
  @DisplayName("should paginate and sort by pupils")
  void shouldPaginateAndSortByPupils() {
    // given

    // when
    schoolRepository.findAll(
        new Specification<School>() {
          @Override
          public Predicate toPredicate(Root<School> root, CriteriaQuery<?> query,
              CriteriaBuilder criteriaBuilder) {
            return null;
          }
        },
        PageRequest.of(0, 10, Sort.by("pupils.character.type"))
    );

    // then
    fail("todo: implement");
  }
}
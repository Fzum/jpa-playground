package com.example.jpaplayground.setup;

import com.example.jpaplayground.model.Pupil;
import com.example.jpaplayground.model.School;
import com.example.jpaplayground.repository.SchoolRepository;
import java.time.LocalDate;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataSetupApplicationRunner implements ApplicationRunner {

  private final SchoolRepository schoolRepository;

  private static final int SCHOOL_COUNT = 50;
  private static final LocalDate BASE_LINE_DATE = LocalDate.of(2000, 1, 1);

  @Override
  @Transactional
  public void run(ApplicationArguments args) {
    IntStream.range(0, SCHOOL_COUNT)
        .forEach(i -> schoolRepository.save(School.builder().name(String.valueOf(i + 1)).build()
            .addPupil(Pupil.builder().inSchoolSince(BASE_LINE_DATE.plusYears(i + 1)).build())
            .addPupil(Pupil.builder().inSchoolSince(BASE_LINE_DATE.minusYears(i + 1)).build())));
  }
}

package com.example.jpaplayground;

import com.example.jpaplayground.data.School;
import com.example.jpaplayground.data.SchoolRepository;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class JpaPlaygroundApplication {

  public static void main(String[] args) {
    SpringApplication.run(JpaPlaygroundApplication.class, args);
  }

}

@Component
@RequiredArgsConstructor
class DataSetup implements ApplicationRunner {

  private final SchoolRepository schoolRepository;

  @Override
  @Transactional
  public void run(ApplicationArguments args) {
    IntStream.range(0, 10).forEach(i -> schoolRepository.save(new School()));
    schoolRepository.findAll().forEach(school ->
        IntStream.range(0, 100).forEach(j -> school.addPupil(new Pupil())
    ));
  }
}

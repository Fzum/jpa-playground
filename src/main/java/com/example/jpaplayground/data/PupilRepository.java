package com.example.jpaplayground.data;

import com.example.jpaplayground.Pupil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PupilRepository extends JpaRepository<Pupil, Long>,
    JpaSpecificationExecutor<Pupil> {

}

package com.example.jpaplayground.repository;

import com.example.jpaplayground.model.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long>,
    JpaSpecificationExecutor<School> {

}

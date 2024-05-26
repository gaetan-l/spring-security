package com.training.demo.repository;

import com.training.demo.model.Tutorial;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorialRepository extends ListCrudRepository<Tutorial, Long> {}
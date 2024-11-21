package com.gen_ai.interview.repository;

import com.gen_ai.interview.model.question.TechQuestion;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechQuestionRepository extends JpaRepository<TechQuestion, Long> {
    TechQuestion findTechQuestionById(long id);

    List<TechQuestion> findTechQuestionByPosition(String position);

    List<TechQuestion> findAll();
}

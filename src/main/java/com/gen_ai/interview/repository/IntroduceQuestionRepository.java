package com.gen_ai.interview.repository;

import com.gen_ai.interview.model.question.IntroduceQuestion;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntroduceQuestionRepository extends JpaRepository<IntroduceQuestion, Long> {
    List<IntroduceQuestion> findAll();

    IntroduceQuestion findIntroduceQuestionById(long questionId);
}
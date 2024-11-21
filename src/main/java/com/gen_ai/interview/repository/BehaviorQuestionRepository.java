package com.gen_ai.interview.repository;

import com.gen_ai.interview.model.question.BehaviorQuestion;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BehaviorQuestionRepository extends JpaRepository<BehaviorQuestion, Long> {
    List<BehaviorQuestion> findAll();

    BehaviorQuestion findBehaviorQuestionById(long questionId);
}

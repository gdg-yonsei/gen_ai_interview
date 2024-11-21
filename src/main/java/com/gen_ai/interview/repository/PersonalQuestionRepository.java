package com.gen_ai.interview.repository;

import com.gen_ai.interview.model.Interview;
import com.gen_ai.interview.model.question.PersonalQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalQuestionRepository extends
        JpaRepository<PersonalQuestion, Long> {

    PersonalQuestion findPersonalQuestionById(long questionId);

    boolean existsByInterview(Interview interview);
}

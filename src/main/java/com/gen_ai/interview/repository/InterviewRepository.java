package com.gen_ai.interview.repository;

import com.gen_ai.interview.model.Interview;
import com.gen_ai.interview.model.User;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {
    List<Interview> findByUser(User user);

    Interview findInterviewById(long interviewId);
}

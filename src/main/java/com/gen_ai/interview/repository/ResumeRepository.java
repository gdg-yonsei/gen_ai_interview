package com.gen_ai.interview.repository;

import com.gen_ai.interview.model.Resume;
import com.gen_ai.interview.model.User;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
    Optional<Resume> findById(long id);

    List<Resume> findByUser(User user);

}

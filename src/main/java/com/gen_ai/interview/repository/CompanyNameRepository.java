package com.gen_ai.interview.repository;

import com.gen_ai.interview.model.company.CompanyName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyNameRepository extends JpaRepository<CompanyName, Long> {
    CompanyName findFirstById(long companyId);
}

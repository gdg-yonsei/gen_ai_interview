package com.gen_ai.interview.repository;

import com.gen_ai.interview.model.company.CompanyDept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyDeptRepository extends JpaRepository<CompanyDept, Long> {
    CompanyDept findById(long departmentId);
}

package com.gen_ai.interview.repository;

import com.gen_ai.interview.model.company.CompanyJob;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyJobRepository extends JpaRepository<CompanyJob, Long> {
    CompanyJob findCompanyJobById(long id);

    List<CompanyJob> findByDepartment(String department);


}

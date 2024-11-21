package com.gen_ai.interview.model.company;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "company_job", uniqueConstraints = {
        @UniqueConstraint(columnNames = "job")
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CompanyJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "department")
    private String department;

    @Column(name = "dept_id")
    private int deptId;

    @NotBlank
    @Column(name = "job")
    private String job;
}

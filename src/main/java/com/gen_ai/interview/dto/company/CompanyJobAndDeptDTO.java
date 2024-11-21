package com.gen_ai.interview.dto.company;


import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class CompanyJobAndDeptDTO {
    private int departmentId;
    private String department;
    private List<CompanyJobDTO> job;

    public CompanyJobAndDeptDTO(int departmentId, String department, List<CompanyJobDTO> job) {
        this.departmentId = departmentId;
        this.department = department;
        this.job = job;
    }
}

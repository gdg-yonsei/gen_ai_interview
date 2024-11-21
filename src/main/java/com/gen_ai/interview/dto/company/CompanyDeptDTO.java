package com.gen_ai.interview.dto.company;

import com.gen_ai.interview.model.eunm.CompanyDepartment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDeptDTO {
    private long companyDeptId;
    private String companyDept;

    public CompanyDeptDTO(CompanyDepartment dept) {
    }
}

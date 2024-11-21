package com.gen_ai.interview.dto.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyNameDTO {
    private long companyId;
    private String companyName;
    private String imageUrl;

    public CompanyNameDTO(long companyId, String companyName) {
        this.companyId = companyId;
        this.companyName = companyName;
    }

    public CompanyNameDTO(String companyName, String imageUrl) {
        this.companyName = companyName;
        this.imageUrl = imageUrl;
    }
}

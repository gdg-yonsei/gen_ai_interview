package com.gen_ai.interview.dto.interview;

import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class InterviewStartRequestDTO {
    private String interviewRound;
    private Integer companyId;
    private String companyName;
    private int departmendId;
    private int jobId;
    private long resumeId;
}

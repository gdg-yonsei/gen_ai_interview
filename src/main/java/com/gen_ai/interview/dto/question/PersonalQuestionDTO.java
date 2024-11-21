package com.gen_ai.interview.dto.question;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PersonalQuestionDTO {
    private long questionId;
    private String question;
    private List<String> criteria;
}

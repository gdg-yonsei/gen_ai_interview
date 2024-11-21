package com.gen_ai.interview.controller;

import com.gen_ai.interview.dto.question.CommonQuestionDTO;
import com.gen_ai.interview.dto.question.PersonalQuestionDTO;
import com.gen_ai.interview.service.QuestionService;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/question/")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;
    private final int TECH_QUESTION_AMOUNT = 2;
    private final int BEHAVIOR_QUESTION_AMOUNT = 2;


    @GetMapping("/personal/{interviewId}")
    public List<PersonalQuestionDTO> generate(@PathVariable long interviewId) {
        List<PersonalQuestionDTO> questionDTOS = questionService.generatePersonalQuestion(interviewId);
        return questionDTOS;
    }

    @GetMapping("/tech/{interviewId}")
    public List<CommonQuestionDTO> getTechQuestion(@PathVariable long interviewId) {

        List<CommonQuestionDTO> questionDTOS = questionService.getTechQuestion(interviewId, TECH_QUESTION_AMOUNT);
        return questionDTOS;
    }

    @GetMapping("/behavior/{interviewId}")
    public List<CommonQuestionDTO> getBehaviorQuestion(@PathVariable long interviewId) {
        List<CommonQuestionDTO> questionDTOS = questionService.getBehaviorQuestion(interviewId,
                BEHAVIOR_QUESTION_AMOUNT);
        return questionDTOS;
    }

    @GetMapping("/introduce/{interviewId}")
    public List<CommonQuestionDTO> getIntroduceQuestion(@PathVariable long interviewId) {
        List<CommonQuestionDTO> questionDTOS = questionService.getIntroduceQuestion(interviewId);
        return questionDTOS;
    }

}

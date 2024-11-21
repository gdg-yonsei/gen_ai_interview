package com.gen_ai.interview.model.question;

import com.gen_ai.interview.model.Interview;
import com.gen_ai.interview.model.Resume;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "personal_question")
@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalQuestion extends BaseQuestionEntity {

    @ManyToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;

    @Column(name = "criteria", length = 2000)
    private String criteria;

    @ManyToOne
    @JoinColumn(name = "interview_id")
    private Interview interview;
}

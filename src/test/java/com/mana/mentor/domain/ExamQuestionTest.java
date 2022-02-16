package com.mana.mentor.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mana.mentor.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExamQuestionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExamQuestion.class);
        ExamQuestion examQuestion1 = new ExamQuestion();
        examQuestion1.setId(1L);
        ExamQuestion examQuestion2 = new ExamQuestion();
        examQuestion2.setId(examQuestion1.getId());
        assertThat(examQuestion1).isEqualTo(examQuestion2);
        examQuestion2.setId(2L);
        assertThat(examQuestion1).isNotEqualTo(examQuestion2);
        examQuestion1.setId(null);
        assertThat(examQuestion1).isNotEqualTo(examQuestion2);
    }
}

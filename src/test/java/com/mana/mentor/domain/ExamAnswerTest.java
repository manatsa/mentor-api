package com.mana.mentor.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mana.mentor.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExamAnswerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExamAnswer.class);
        ExamAnswer examAnswer1 = new ExamAnswer();
        examAnswer1.setId(1L);
        ExamAnswer examAnswer2 = new ExamAnswer();
        examAnswer2.setId(examAnswer1.getId());
        assertThat(examAnswer1).isEqualTo(examAnswer2);
        examAnswer2.setId(2L);
        assertThat(examAnswer1).isNotEqualTo(examAnswer2);
        examAnswer1.setId(null);
        assertThat(examAnswer1).isNotEqualTo(examAnswer2);
    }
}

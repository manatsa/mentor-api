package com.mana.mentor.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mana.mentor.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExamMarkTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExamMark.class);
        ExamMark examMark1 = new ExamMark();
        examMark1.setId(1L);
        ExamMark examMark2 = new ExamMark();
        examMark2.setId(examMark1.getId());
        assertThat(examMark1).isEqualTo(examMark2);
        examMark2.setId(2L);
        assertThat(examMark1).isNotEqualTo(examMark2);
        examMark1.setId(null);
        assertThat(examMark1).isNotEqualTo(examMark2);
    }
}

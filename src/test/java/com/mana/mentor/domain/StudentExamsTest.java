package com.mana.mentor.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mana.mentor.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentExamsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentExams.class);
        StudentExams studentExams1 = new StudentExams();
        studentExams1.setId(1L);
        StudentExams studentExams2 = new StudentExams();
        studentExams2.setId(studentExams1.getId());
        assertThat(studentExams1).isEqualTo(studentExams2);
        studentExams2.setId(2L);
        assertThat(studentExams1).isNotEqualTo(studentExams2);
        studentExams1.setId(null);
        assertThat(studentExams1).isNotEqualTo(studentExams2);
    }
}

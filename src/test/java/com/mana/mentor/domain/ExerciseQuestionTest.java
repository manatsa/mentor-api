package com.mana.mentor.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mana.mentor.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExerciseQuestionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExerciseQuestion.class);
        ExerciseQuestion exerciseQuestion1 = new ExerciseQuestion();
        exerciseQuestion1.setId(1L);
        ExerciseQuestion exerciseQuestion2 = new ExerciseQuestion();
        exerciseQuestion2.setId(exerciseQuestion1.getId());
        assertThat(exerciseQuestion1).isEqualTo(exerciseQuestion2);
        exerciseQuestion2.setId(2L);
        assertThat(exerciseQuestion1).isNotEqualTo(exerciseQuestion2);
        exerciseQuestion1.setId(null);
        assertThat(exerciseQuestion1).isNotEqualTo(exerciseQuestion2);
    }
}

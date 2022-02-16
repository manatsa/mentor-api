package com.mana.mentor.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mana.mentor.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExerciseAnswerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExerciseAnswer.class);
        ExerciseAnswer exerciseAnswer1 = new ExerciseAnswer();
        exerciseAnswer1.setId(1L);
        ExerciseAnswer exerciseAnswer2 = new ExerciseAnswer();
        exerciseAnswer2.setId(exerciseAnswer1.getId());
        assertThat(exerciseAnswer1).isEqualTo(exerciseAnswer2);
        exerciseAnswer2.setId(2L);
        assertThat(exerciseAnswer1).isNotEqualTo(exerciseAnswer2);
        exerciseAnswer1.setId(null);
        assertThat(exerciseAnswer1).isNotEqualTo(exerciseAnswer2);
    }
}

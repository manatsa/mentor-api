package com.mana.mentor.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mana.mentor.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExerciseMarkTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExerciseMark.class);
        ExerciseMark exerciseMark1 = new ExerciseMark();
        exerciseMark1.setId(1L);
        ExerciseMark exerciseMark2 = new ExerciseMark();
        exerciseMark2.setId(exerciseMark1.getId());
        assertThat(exerciseMark1).isEqualTo(exerciseMark2);
        exerciseMark2.setId(2L);
        assertThat(exerciseMark1).isNotEqualTo(exerciseMark2);
        exerciseMark1.setId(null);
        assertThat(exerciseMark1).isNotEqualTo(exerciseMark2);
    }
}

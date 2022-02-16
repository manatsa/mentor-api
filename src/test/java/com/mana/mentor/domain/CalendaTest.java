package com.mana.mentor.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mana.mentor.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CalendaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Calenda.class);
        Calenda calenda1 = new Calenda();
        calenda1.setId(1L);
        Calenda calenda2 = new Calenda();
        calenda2.setId(calenda1.getId());
        assertThat(calenda1).isEqualTo(calenda2);
        calenda2.setId(2L);
        assertThat(calenda1).isNotEqualTo(calenda2);
        calenda1.setId(null);
        assertThat(calenda1).isNotEqualTo(calenda2);
    }
}

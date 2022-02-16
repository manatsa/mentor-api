package com.mana.mentor.repository;

import com.mana.mentor.domain.ExamQuestion;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ExamQuestion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, Long> {
    @Query("select examQuestion from ExamQuestion examQuestion where examQuestion.creator.login = ?#{principal.username}")
    List<ExamQuestion> findByCreatorIsCurrentUser();

    @Query("select examQuestion from ExamQuestion examQuestion where examQuestion.modifier.login = ?#{principal.username}")
    List<ExamQuestion> findByModifierIsCurrentUser();
}

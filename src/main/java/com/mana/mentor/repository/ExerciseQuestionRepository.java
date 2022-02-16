package com.mana.mentor.repository;

import com.mana.mentor.domain.ExerciseQuestion;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ExerciseQuestion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExerciseQuestionRepository extends JpaRepository<ExerciseQuestion, Long> {
    @Query("select exerciseQuestion from ExerciseQuestion exerciseQuestion where exerciseQuestion.creator.login = ?#{principal.username}")
    List<ExerciseQuestion> findByCreatorIsCurrentUser();

    @Query("select exerciseQuestion from ExerciseQuestion exerciseQuestion where exerciseQuestion.modifier.login = ?#{principal.username}")
    List<ExerciseQuestion> findByModifierIsCurrentUser();
}

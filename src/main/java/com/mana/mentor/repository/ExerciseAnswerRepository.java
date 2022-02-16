package com.mana.mentor.repository;

import com.mana.mentor.domain.ExerciseAnswer;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ExerciseAnswer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExerciseAnswerRepository extends JpaRepository<ExerciseAnswer, Long> {
    @Query("select exerciseAnswer from ExerciseAnswer exerciseAnswer where exerciseAnswer.creator.login = ?#{principal.username}")
    List<ExerciseAnswer> findByCreatorIsCurrentUser();

    @Query("select exerciseAnswer from ExerciseAnswer exerciseAnswer where exerciseAnswer.modifier.login = ?#{principal.username}")
    List<ExerciseAnswer> findByModifierIsCurrentUser();
}

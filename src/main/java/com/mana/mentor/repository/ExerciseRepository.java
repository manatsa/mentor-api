package com.mana.mentor.repository;

import com.mana.mentor.domain.Exercise;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Exercise entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    @Query("select exercise from Exercise exercise where exercise.creator.login = ?#{principal.username}")
    List<Exercise> findByCreatorIsCurrentUser();

    @Query("select exercise from Exercise exercise where exercise.modifier.login = ?#{principal.username}")
    List<Exercise> findByModifierIsCurrentUser();
}

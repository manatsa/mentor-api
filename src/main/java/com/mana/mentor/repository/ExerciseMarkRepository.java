package com.mana.mentor.repository;

import com.mana.mentor.domain.ExerciseMark;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ExerciseMark entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExerciseMarkRepository extends JpaRepository<ExerciseMark, Long> {
    @Query("select exerciseMark from ExerciseMark exerciseMark where exerciseMark.creator.login = ?#{principal.username}")
    List<ExerciseMark> findByCreatorIsCurrentUser();

    @Query("select exerciseMark from ExerciseMark exerciseMark where exerciseMark.modifier.login = ?#{principal.username}")
    List<ExerciseMark> findByModifierIsCurrentUser();
}

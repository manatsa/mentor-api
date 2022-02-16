package com.mana.mentor.repository;

import com.mana.mentor.domain.Lesson;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Lesson entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    @Query("select lesson from Lesson lesson where lesson.creator.login = ?#{principal.username}")
    List<Lesson> findByCreatorIsCurrentUser();

    @Query("select lesson from Lesson lesson where lesson.modifier.login = ?#{principal.username}")
    List<Lesson> findByModifierIsCurrentUser();
}

package com.mana.mentor.repository;

import com.mana.mentor.domain.Lecture;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Lecture entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {
    @Query("select lecture from Lecture lecture where lecture.creator.login = ?#{principal.username}")
    List<Lecture> findByCreatorIsCurrentUser();

    @Query("select lecture from Lecture lecture where lecture.modifier.login = ?#{principal.username}")
    List<Lecture> findByModifierIsCurrentUser();
}

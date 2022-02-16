package com.mana.mentor.repository;

import com.mana.mentor.domain.Subject;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Subject entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    @Query("select subject from Subject subject where subject.creator.login = ?#{principal.username}")
    List<Subject> findByCreatorIsCurrentUser();

    @Query("select subject from Subject subject where subject.modifier.login = ?#{principal.username}")
    List<Subject> findByModifierIsCurrentUser();
}

package com.mana.mentor.repository;

import com.mana.mentor.domain.School;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the School entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {
    @Query("select school from School school where school.creator.login = ?#{principal.username}")
    List<School> findByCreatorIsCurrentUser();

    @Query("select school from School school where school.modifier.login = ?#{principal.username}")
    List<School> findByModifierIsCurrentUser();
}

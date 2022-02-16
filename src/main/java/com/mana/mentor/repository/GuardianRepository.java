package com.mana.mentor.repository;

import com.mana.mentor.domain.Guardian;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Guardian entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GuardianRepository extends JpaRepository<Guardian, Long> {
    @Query("select guardian from Guardian guardian where guardian.creator.login = ?#{principal.username}")
    List<Guardian> findByCreatorIsCurrentUser();

    @Query("select guardian from Guardian guardian where guardian.modifier.login = ?#{principal.username}")
    List<Guardian> findByModifierIsCurrentUser();
}

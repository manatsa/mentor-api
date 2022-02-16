package com.mana.mentor.repository;

import com.mana.mentor.domain.Agency;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Agency entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AgencyRepository extends JpaRepository<Agency, Long> {
    @Query("select agency from Agency agency where agency.creator.login = ?#{principal.username}")
    List<Agency> findByCreatorIsCurrentUser();

    @Query("select agency from Agency agency where agency.modifier.login = ?#{principal.username}")
    List<Agency> findByModifierIsCurrentUser();
}

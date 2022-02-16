package com.mana.mentor.repository;

import com.mana.mentor.domain.Calenda;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Calenda entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CalendaRepository extends JpaRepository<Calenda, Long> {
    @Query("select calenda from Calenda calenda where calenda.creator.login = ?#{principal.username}")
    List<Calenda> findByCreatorIsCurrentUser();

    @Query("select calenda from Calenda calenda where calenda.modifer.login = ?#{principal.username}")
    List<Calenda> findByModiferIsCurrentUser();
}

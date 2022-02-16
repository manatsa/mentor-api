package com.mana.mentor.repository;

import com.mana.mentor.domain.Country;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Country entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    @Query("select country from Country country where country.creator.login = ?#{principal.username}")
    List<Country> findByCreatorIsCurrentUser();

    @Query("select country from Country country where country.modifier.login = ?#{principal.username}")
    List<Country> findByModifierIsCurrentUser();
}

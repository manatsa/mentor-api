package com.mana.mentor.repository;

import com.mana.mentor.domain.Example;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Example entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExampleRepository extends JpaRepository<Example, Long> {
    @Query("select example from Example example where example.creator.login = ?#{principal.username}")
    List<Example> findByCreatorIsCurrentUser();

    @Query("select example from Example example where example.modifier.login = ?#{principal.username}")
    List<Example> findByModifierIsCurrentUser();
}

package com.mana.mentor.repository;

import com.mana.mentor.domain.News;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the News entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    @Query("select news from News news where news.creator.login = ?#{principal.username}")
    List<News> findByCreatorIsCurrentUser();

    @Query("select news from News news where news.modifier.login = ?#{principal.username}")
    List<News> findByModifierIsCurrentUser();
}

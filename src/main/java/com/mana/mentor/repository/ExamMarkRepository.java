package com.mana.mentor.repository;

import com.mana.mentor.domain.ExamMark;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ExamMark entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExamMarkRepository extends JpaRepository<ExamMark, Long> {
    @Query("select examMark from ExamMark examMark where examMark.creator.login = ?#{principal.username}")
    List<ExamMark> findByCreatorIsCurrentUser();

    @Query("select examMark from ExamMark examMark where examMark.modifier.login = ?#{principal.username}")
    List<ExamMark> findByModifierIsCurrentUser();
}

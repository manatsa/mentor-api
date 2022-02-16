package com.mana.mentor.repository;

import com.mana.mentor.domain.Exam;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Exam entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    @Query("select exam from Exam exam where exam.creator.login = ?#{principal.username}")
    List<Exam> findByCreatorIsCurrentUser();

    @Query("select exam from Exam exam where exam.modifier.login = ?#{principal.username}")
    List<Exam> findByModifierIsCurrentUser();
}

package com.mana.mentor.repository;

import com.mana.mentor.domain.ExamAnswer;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ExamAnswer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExamAnswerRepository extends JpaRepository<ExamAnswer, Long> {
    @Query("select examAnswer from ExamAnswer examAnswer where examAnswer.creator.login = ?#{principal.username}")
    List<ExamAnswer> findByCreatorIsCurrentUser();
}

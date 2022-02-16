package com.mana.mentor.repository;

import com.mana.mentor.domain.StudentExams;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the StudentExams entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentExamsRepository extends JpaRepository<StudentExams, Long> {}

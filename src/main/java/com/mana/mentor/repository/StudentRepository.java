package com.mana.mentor.repository;

import com.mana.mentor.domain.Student;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Student entity.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("select student from Student student where student.creator.login = ?#{principal.username}")
    List<Student> findByCreatorIsCurrentUser();

    @Query("select student from Student student where student.modifier.login = ?#{principal.username}")
    List<Student> findByModifierIsCurrentUser();

    @Query(
        value = "select distinct student from Student student left join fetch student.subjects left join fetch student.lessons left join fetch student.exercises left join fetch student.exams",
        countQuery = "select count(distinct student) from Student student"
    )
    Page<Student> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct student from Student student left join fetch student.subjects left join fetch student.lessons left join fetch student.exercises left join fetch student.exams"
    )
    List<Student> findAllWithEagerRelationships();

    @Query(
        "select student from Student student left join fetch student.subjects left join fetch student.lessons left join fetch student.exercises left join fetch student.exams where student.id =:id"
    )
    Optional<Student> findOneWithEagerRelationships(@Param("id") Long id);
}

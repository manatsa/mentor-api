package com.mana.mentor.repository;

import com.mana.mentor.domain.Teacher;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Teacher entity.
 */
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    @Query("select teacher from Teacher teacher where teacher.creator.login = ?#{principal.username}")
    List<Teacher> findByCreatorIsCurrentUser();

    @Query("select teacher from Teacher teacher where teacher.modfier.login = ?#{principal.username}")
    List<Teacher> findByModfierIsCurrentUser();

    @Query(
        value = "select distinct teacher from Teacher teacher left join fetch teacher.subjects",
        countQuery = "select count(distinct teacher) from Teacher teacher"
    )
    Page<Teacher> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct teacher from Teacher teacher left join fetch teacher.subjects")
    List<Teacher> findAllWithEagerRelationships();

    @Query("select teacher from Teacher teacher left join fetch teacher.subjects where teacher.id =:id")
    Optional<Teacher> findOneWithEagerRelationships(@Param("id") Long id);
}

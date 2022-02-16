package com.mana.mentor.repository;

import com.mana.mentor.domain.Attachment;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Attachment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    @Query("select attachment from Attachment attachment where attachment.creator.login = ?#{principal.username}")
    List<Attachment> findByCreatorIsCurrentUser();

    @Query("select attachment from Attachment attachment where attachment.modifier.login = ?#{principal.username}")
    List<Attachment> findByModifierIsCurrentUser();
}

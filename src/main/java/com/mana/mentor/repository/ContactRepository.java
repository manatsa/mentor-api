package com.mana.mentor.repository;

import com.mana.mentor.domain.Contact;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Contact entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    @Query("select contact from Contact contact where contact.creator.login = ?#{principal.username}")
    List<Contact> findByCreatorIsCurrentUser();

    @Query("select contact from Contact contact where contact.modifier.login = ?#{principal.username}")
    List<Contact> findByModifierIsCurrentUser();
}

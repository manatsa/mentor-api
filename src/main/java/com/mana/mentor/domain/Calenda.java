package com.mana.mentor.domain;

import com.mana.mentor.domain.enumeration.NewsEventCategory;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Calenda.
 */
@Entity
@Table(name = "calenda")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Calenda implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Column(name = "description")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private NewsEventCategory category;

    @NotNull
    @Column(name = "event_start_date", nullable = false)
    private Instant eventStartDate;

    @NotNull
    @Column(name = "event_end_date", nullable = false)
    private Instant eventEndDate;

    @NotNull
    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @ManyToOne
    private User creator;

    @ManyToOne
    private User modifer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Calenda id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventName() {
        return this.eventName;
    }

    public Calenda eventName(String eventName) {
        this.setEventName(eventName);
        return this;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDescription() {
        return this.description;
    }

    public Calenda description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public NewsEventCategory getCategory() {
        return this.category;
    }

    public Calenda category(NewsEventCategory category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(NewsEventCategory category) {
        this.category = category;
    }

    public Instant getEventStartDate() {
        return this.eventStartDate;
    }

    public Calenda eventStartDate(Instant eventStartDate) {
        this.setEventStartDate(eventStartDate);
        return this;
    }

    public void setEventStartDate(Instant eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public Instant getEventEndDate() {
        return this.eventEndDate;
    }

    public Calenda eventEndDate(Instant eventEndDate) {
        this.setEventEndDate(eventEndDate);
        return this;
    }

    public void setEventEndDate(Instant eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public String getLocation() {
        return this.location;
    }

    public Calenda location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Instant getDateCreated() {
        return this.dateCreated;
    }

    public Calenda dateCreated(Instant dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Calenda lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public User getCreator() {
        return this.creator;
    }

    public void setCreator(User user) {
        this.creator = user;
    }

    public Calenda creator(User user) {
        this.setCreator(user);
        return this;
    }

    public User getModifer() {
        return this.modifer;
    }

    public void setModifer(User user) {
        this.modifer = user;
    }

    public Calenda modifer(User user) {
        this.setModifer(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Calenda)) {
            return false;
        }
        return id != null && id.equals(((Calenda) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Calenda{" +
            "id=" + getId() +
            ", eventName='" + getEventName() + "'" +
            ", description='" + getDescription() + "'" +
            ", category='" + getCategory() + "'" +
            ", eventStartDate='" + getEventStartDate() + "'" +
            ", eventEndDate='" + getEventEndDate() + "'" +
            ", location='" + getLocation() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}

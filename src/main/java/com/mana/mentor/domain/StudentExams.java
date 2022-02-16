package com.mana.mentor.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StudentExams.
 */
@Entity
@Table(name = "student_exams")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StudentExams implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "completed", nullable = false)
    private Boolean completed;

    @NotNull
    @Column(name = "finish_date", nullable = false)
    private Instant finishDate;

    @Column(name = "mark")
    private Integer mark;

    @Column(name = "total")
    private Integer total;

    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StudentExams id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getCompleted() {
        return this.completed;
    }

    public StudentExams completed(Boolean completed) {
        this.setCompleted(completed);
        return this;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Instant getFinishDate() {
        return this.finishDate;
    }

    public StudentExams finishDate(Instant finishDate) {
        this.setFinishDate(finishDate);
        return this;
    }

    public void setFinishDate(Instant finishDate) {
        this.finishDate = finishDate;
    }

    public Integer getMark() {
        return this.mark;
    }

    public StudentExams mark(Integer mark) {
        this.setMark(mark);
        return this;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    public Integer getTotal() {
        return this.total;
    }

    public StudentExams total(Integer total) {
        this.setTotal(total);
        return this;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Instant getDateCreated() {
        return this.dateCreated;
    }

    public StudentExams dateCreated(Instant dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public StudentExams lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentExams)) {
            return false;
        }
        return id != null && id.equals(((StudentExams) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentExams{" +
            "id=" + getId() +
            ", completed='" + getCompleted() + "'" +
            ", finishDate='" + getFinishDate() + "'" +
            ", mark=" + getMark() +
            ", total=" + getTotal() +
            ", dateCreated='" + getDateCreated() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
